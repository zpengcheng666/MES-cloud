package cn.iocoder.yudao.module.infra.service.file;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.io.FileUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.FileCreateReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.FilePresignedUrlRespVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileConfigDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileDO;
import cn.iocoder.yudao.module.infra.dal.mysql.file.FileMapper;
import cn.iocoder.yudao.module.infra.framework.file.core.client.FileClient;
import cn.iocoder.yudao.module.infra.framework.file.core.client.FileClientConfig;
import cn.iocoder.yudao.module.infra.framework.file.core.client.s3.FilePresignedUrlRespDTO;
import cn.iocoder.yudao.module.infra.framework.file.core.client.s3.S3FileClient;
import cn.iocoder.yudao.module.infra.framework.file.core.client.s3.S3FileClientConfig;
import cn.iocoder.yudao.module.infra.framework.file.core.utils.FileTypeUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.infra.enums.ErrorCodeConstants.FILE_NOT_EXISTS;

/**
 * 文件 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileConfigService fileConfigService;


    @Resource
    private FileMapper fileMapper;

    @Override
    public PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO) {
        return fileMapper.selectPage(pageReqVO);
    }

    @Override
    @SneakyThrows
    public String createFile(String name, String path, byte[] content,String bucket) {
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type,bucket);

        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        file.setType(type);
        file.setSize(content.length);
        fileMapper.insert(file);
        return url;
    }

    @Override
    @SneakyThrows
    public String createMinioFile(String name, String path, byte[] content,String bucket) {

        FileConfigDO fileConfigDO =  fileConfigService.getMasterConfig();
        String type = FileTypeUtils.getMineType(content, name);
        S3FileClientConfig clientConfig = (S3FileClientConfig) fileConfigDO.getConfig();
        S3FileClient client = new S3FileClient(fileConfigDO.getId(), clientConfig);
        client.init();
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        String fullPath = client.upload(content, path, type,bucket);
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(fullPath);
        file.setType(type);
        file.setSize(content.length);
        fileMapper.insert(file);
        return fullPath;
    }

    @Override
    public Long createFile(FileCreateReqVO createReqVO) {
        FileDO file = BeanUtils.toBean(createReqVO, FileDO.class);
        fileMapper.insert(file);
        return file.getId();
    }

    @Override
    public void deleteFile(Long id) throws Exception {
        // 校验存在
        FileDO file = validateFileExists(id);

        // 从文件存储器中删除
        FileClient client = fileConfigService.getFileClient(file.getConfigId());
        Assert.notNull(client, "客户端({}) 不能为空", file.getConfigId());
        client.delete(file.getPath());

        // 删除记录
        fileMapper.deleteById(id);
    }

    @Override
    public void deleteMinioFile(Long id) throws Exception {
        FileConfigDO fileConfigDO =  fileConfigService.getMasterConfig();
        FileDO file = validateFileExists(id);
        S3FileClientConfig clientConfig = (S3FileClientConfig) fileConfigDO.getConfig();
        S3FileClient client = new S3FileClient(fileConfigDO.getId(), clientConfig);
        client.init();
        client.delete(file.getPath());
    }

    private FileDO validateFileExists(Long id) {
        FileDO fileDO = fileMapper.selectById(id);
        if (fileDO == null) {
            throw exception(FILE_NOT_EXISTS);
        }
        return fileDO;
    }

    @Override
    public byte[] getFileContent(Long configId, String path) throws Exception {
        FileClient client = fileConfigService.getFileClient(configId);
        Assert.notNull(client, "客户端({}) 不能为空", configId);
        return client.getContent(path);
    }

    @Override
    public FilePresignedUrlRespVO getFilePresignedUrl(String path) throws Exception {
        FileClient fileClient = fileConfigService.getMasterFileClient();
        FilePresignedUrlRespDTO presignedObjectUrl = fileClient.getPresignedObjectUrl(path);
        return BeanUtils.toBean(presignedObjectUrl, FilePresignedUrlRespVO.class,
                object -> object.setConfigId(fileClient.getId()));
    }

    @Override
    /*这个注解的作用是允许在方法或构造函数中抛出指定的异常，而不需要在方法签名中声明这些异常。
    通常，Java要求在方法签名中声明可能抛出的检查型异常，但是使用SneakyThrows注解可以绕过这个限制，悄无声息地抛出异常。
    这对于一些需要隐式处理异常的场景非常有用。*/
    @SneakyThrows
    public String createMinioFileNew(String name, String path, byte[] content,String bucket) {
        FileConfigDO fileConfigDO =  fileConfigService.getMasterConfig();
        String type = FileTypeUtils.getMineType(content, name);
        S3FileClientConfig clientConfig = (S3FileClientConfig) fileConfigDO.getConfig();
        S3FileClient client = new S3FileClient(fileConfigDO.getId(), clientConfig);
        client.init();
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }

        String fullPathNew = client.upload(content, path, type,bucket);
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(fullPathNew);
        file.setType(type);
        file.setSize(content.length);
        fileMapper.insert(file);
        return fullPathNew + "," +name;
    }
}
