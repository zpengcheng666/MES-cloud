package com.miyu.cloud.macs.restServer.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import cn.iocoder.yudao.module.infra.api.file.dto.FileCreateReqDTO;
import cn.iocoder.yudao.module.system.api.permission.UserRoleApi;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.macs.dal.dataobject.deviceConfig.DeviceConfigDO;
import com.miyu.cloud.macs.dal.dataobject.device.DeviceDO;
import com.miyu.cloud.macs.dal.dataobject.door.DoorDO;
import com.miyu.cloud.macs.dal.dataobject.file.MacsFileDO;
import com.miyu.cloud.macs.dal.dataobject.region.RegionDO;
import com.miyu.cloud.macs.dal.dataobject.user.UserDO;
import com.miyu.cloud.macs.dal.dataobject.userRoleRegion.UserRoleRegionDO;
import com.miyu.cloud.macs.dal.dataobject.visitor.VisitorDO;
import com.miyu.cloud.macs.dal.dataobject.visitorRegion.VisitorRegionDO;
import com.miyu.cloud.macs.dal.mysql.deviceConfig.DeviceConfigMapper;
import com.miyu.cloud.macs.dal.mysql.device.DeviceMapper;
import com.miyu.cloud.macs.dal.mysql.door.DoorMapper;
import com.miyu.cloud.macs.dal.mysql.file.MacsFileMapper;
import com.miyu.cloud.macs.dal.mysql.region.RegionMapper;
import com.miyu.cloud.macs.dal.mysql.user.UserMapper;
import com.miyu.cloud.macs.dal.mysql.userRoleRegion.UserRoleRegionMapper;
import com.miyu.cloud.macs.dal.mysql.visitor.VisitorMapper;
import com.miyu.cloud.macs.dal.mysql.visitorRegion.VisitorRegionMapper;
import com.miyu.cloud.macs.restServer.api.MacsInteractiveApi;
import com.miyu.cloud.macs.restServer.entity.MacsRestImageUploadVO;
import com.miyu.cloud.macs.restServer.entity.MacsRestPersonUpdateVO;
import com.miyu.cloud.macs.restServer.service.MacsInteractiveService;
import com.miyu.cloud.macs.restServer.service.MacsRestService;
import com.miyu.cloud.macs.service.utils.StringEncryptorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
@Service
@Transactional
public class MacsRestServiceImpl implements MacsRestService {

    public String imgPath = System.getProperty("user.dir");

    private boolean localSave = false;
    @Autowired
    private MacsFileMapper macsFileMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VisitorMapper visitorMapper;
    @Autowired
    private DoorMapper doorMapper;
    @Autowired
    private DeviceConfigMapper deviceConfigMapper;
    @Autowired
    private VisitorRegionMapper visitorRegionMapper;
    @Autowired
    private UserRoleRegionMapper userRoleRegionMapper;
    @Resource
    private UserRoleApi userRoleApi;
    @Resource
    private MacsInteractiveApi macsInteractiveApi;
    @Autowired
    private MacsInteractiveService macsInteractiveService;
    @Autowired
    private DeviceMapper deviceMapper;
    @Resource
    private FileApi fileApi;
    @Autowired
    private Environment environment;
    @Autowired
    private RegionMapper regionMapper;

    @Override
    public void saveInformation(MacsRestImageUploadVO imageUpload, String remoteAddr) throws Exception {
        String id = imageUpload.getId();
        int infoType = imageUpload.getInfoType();
        boolean isVisitor = imageUpload.isVisitor();

        int delCount = this.deleteOldInfo(id, isVisitor, infoType);
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        String port = environment.getProperty("server.port");

        String key = StringEncryptorUtil.generateKey();
        List<Map<String,Object>> fileList = getFileList(imageUpload, remoteAddr);
        List<String> fileIdList = new ArrayList<>();
        for (Map<String,Object> map : fileList) {
            String nameF = (String) map.get("name");
            int index = nameF.lastIndexOf(".");
            String name = infoType + UUID.randomUUID().toString().replace("-","") + nameF.substring(index);
            String type = (String) map.get("type");
            long size = (long) map.get("size");

            byte[] bytes = (byte[]) map.get("bytes");
            if (localSave) {
                byte[] encryptedBytes = SecureUtil.aes(key.getBytes(CharsetUtil.CHARSET_UTF_8)).encrypt(bytes);
                FileOutputStream outputStream = new FileOutputStream(imgPath + name);
                outputStream.write(encryptedBytes);
                outputStream.close();
            } else {
                FileCreateReqDTO fileCreateReqDTO = new FileCreateReqDTO();
                fileCreateReqDTO.setContent(bytes);
                fileCreateReqDTO.setName(name);
                String file = fileApi.createFile(fileCreateReqDTO).getData();
                int index1 = file.lastIndexOf("/") + 1;
                imgPath = file.substring(0, index1);
                name = file.substring(index1);
            }

            MacsFileDO fileDO = new MacsFileDO();
            fileDO.setName(name);
            fileDO.setType(type);
            fileDO.setSize(size);
            fileDO.setPath(imgPath);
            fileDO.setInfoType(infoType);
            if (localSave) fileDO.setSalt(key);
            if (isVisitor) {
                fileDO.setVisitorId(id);
            } else {
                fileDO.setUserId(id);
            }
            int count = macsFileMapper.insert(fileDO);
            if (localSave) {
                fileIdList.add("http://" + hostAddress + ":" + port + "/macs/rest/getImageByName/" + fileDO.getId());
            } else {
                fileIdList.add(imgPath + name);
            }
        }
        if (isVisitor) {
            VisitorDO visitorDO = visitorMapper.selectById(id);
            if (infoType == 1) visitorDO.setCode(imageUpload.getCardNumber());
            if (infoType == 2) {
                if (fileIdList.size() > 0) {
                    visitorDO.setFacePicture(fileIdList.get(0));
                }
            }
            if (infoType == 3) {
                visitorDO.setFingerprintPicture(JsonUtils.toJsonString(fileIdList));
            }
            if (visitorDO.getStatus() == 0) visitorDO.setStatus(1);
            visitorMapper.updateById(visitorDO);
        } else {
            UserDO userDO = userMapper.selectById(id);
            if (infoType == 1) userDO.setCode(imageUpload.getCardNumber());
            if (infoType == 2) {
                if (fileIdList.size() > 0) {
                    userDO.setFacePicture(fileIdList.get(0));
                }
            }
            if (infoType == 3) {
                userDO.setFingerprintPicture(JsonUtils.toJsonString(fileIdList));
            }
            userMapper.updateById(userDO);
        }
    }

    private List<Map<String,Object>> getFileList(MacsRestImageUploadVO imageUpload, String remoteAddr) {
        List<Map<String,Object>> result = new ArrayList<>();
        if (imageUpload.getFileList() != null) {
            for (MultipartFile multipartFile : imageUpload.getFileList()) {
                Map<String,Object> map = new HashMap<>();
                map.put("name",multipartFile.getOriginalFilename());
                map.put("type",multipartFile.getContentType());
                map.put("size",multipartFile.getSize());
                try {
                    map.put("bytes",IoUtil.readBytes(multipartFile.getInputStream()));
                } catch (IOException ignored) {
                }
                result.add(map);
            }
        }
        if (imageUpload.getFileListAddr() != null) {
            for (String path : imageUpload.getFileListAddr()) {
                Map<String,Object> map = new HashMap<>();
                ResponseEntity<byte[]> responseEntity = new RestTemplate().exchange(path, HttpMethod.GET, null, byte[].class);
                List<String> list = responseEntity.getHeaders().get("Content-Type");
                map.put("name",path);
                map.put("type", Objects.requireNonNull(responseEntity.getHeaders().getContentType()).toString());
                map.put("size",responseEntity.getHeaders().getContentLength());
                map.put("bytes",responseEntity.getBody());
                result.add(map);
            }
        }
        return result;
    }

    private int deleteOldInfo(String id, boolean isVisitor, int infoType) {
        if (infoType == 1) return 0;
        QueryWrapper<MacsFileDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("info_type", infoType);
        if (isVisitor) {
            queryWrapper.eq("visitor_id", id);
        } else {
            queryWrapper.eq("user_id", id);
        }
        List<MacsFileDO> List = macsFileMapper.selectList(queryWrapper);
        int count = 0;
        for (MacsFileDO fileDO : List) {
            if (fileDO.getSalt() != null) {
                File file = new File(fileDO.getPath() + fileDO.getName());
                if (file.exists()) {
                    boolean delete = file.delete();
                    if (delete) {
                        count++;
                    }
                }
            } else {

            }
            macsFileMapper.deleteById(fileDO);
        }
        return count;
    }

    /**
     * 对比用户访客同步情况并发送至门禁控制器
     */
    @Override
    public void checkUserAndVisitor() throws URISyntaxException {
        List<DeviceDO> deviceList = deviceMapper.selectList();
        for (DeviceDO device : deviceList) {
            List<DoorDO> doorDOS = doorMapper.selectList(new QueryWrapper<DoorDO>().eq("device_id", device.getId()));
            if (doorDOS.size() == 0) continue;
            String regionId = doorDOS.get(0).getRegionId();
            URI uri = macsInteractiveService.getUriByDeviceCode(device.getCode());
            if (uri == null) continue;
            List<DeviceConfigDO> deviceConfigS = deviceConfigMapper.selectList(new QueryWrapper<DeviceConfigDO>().eq("device_id", device.getId()));
            if (deviceConfigS.size() == 0) {
                DeviceConfigDO deviceConfigDO = new DeviceConfigDO();
                deviceConfigDO.setDeviceId(device.getId());
                deviceConfigS.add(deviceConfigDO);
                deviceConfigMapper.insert(deviceConfigDO);
            }
            DeviceConfigDO deviceConfig = deviceConfigS.get(0);
            String userC = deviceConfig.getUsers();
            String visitorC = deviceConfig.getVisitors();

            //获取所有拥有权限的用户
            List<MacsRestPersonUpdateVO> userList = getAllUserByRegion(device, regionId);
            List<MacsRestPersonUpdateVO> usersN = updatePerson(uri, userC, userList);
            deviceConfig.setUsers(JsonUtils.toJsonString(usersN));

            //获取所有拥有权限的访客
            List<MacsRestPersonUpdateVO> visitorList = getAllVisitorByRegion(device, regionId);
            List<MacsRestPersonUpdateVO> visitorsN = updatePerson(uri, visitorC, visitorList);
            deviceConfig.setVisitors(JsonUtils.toJsonString(visitorsN));

            deviceConfigMapper.updateById(deviceConfig);
        }
    }

    private List<MacsRestPersonUpdateVO> getAllUserByRegion(DeviceDO device, String regionId) {
        List<MacsRestPersonUpdateVO> list = new ArrayList<>();
        List<String> userIdList = new ArrayList<>();
        RegionDO regionDO = regionMapper.selectById(regionId);
        if (regionDO.getPublicStatus()) {
            return getAllUserToRestPerson(list, device);
        }
        List<UserRoleRegionDO> userRoleRegionDOList = userRoleRegionMapper.selectList(new QueryWrapper<UserRoleRegionDO>().eq("region_id", regionId));
        for (UserRoleRegionDO userRoleRegionDO : userRoleRegionDOList) {

            String userId = userRoleRegionDO.getUserId();
            if (userId != null) {
                if (userIdList.contains(userId)) continue;
                MacsRestPersonUpdateVO person = new MacsRestPersonUpdateVO();
                List<UserDO> userDOs = userMapper.selectList(new QueryWrapper<UserDO>().eq("user_id", userId));
                if (userDOs == null || userDOs.size() == 0) continue;
                UserDO userDO = userDOs.get(0);
                person.setPersonId(userId);
                person.setCardNo(userDO.getCode());
                person.setFacePath(userDO.getFacePicture());
                person.setFingerprintPathList(JsonUtils.parseArray(userDO.getFingerprintPicture(),String.class));
                person.setDeviceNumber(device.getCode());
                list.add(person);
                userIdList.add(userId);
                continue;
            }

            String roleId = userRoleRegionDO.getRoleId();
            if (roleId == null) continue;
            List<String> adminUserIdList = userRoleApi.getUserIdByRole(roleId).getData();
            if (adminUserIdList == null || adminUserIdList.size() == 0) continue;
            List<UserDO> userDOS = userMapper.selectList(new QueryWrapper<UserDO>().in("user_id", adminUserIdList));
            for (UserDO userDO : userDOS) {
                String id = userDO.getId();
                if (userIdList.contains(id)) continue;
                MacsRestPersonUpdateVO person = new MacsRestPersonUpdateVO().setPersonId(id);
                person.setDeviceNumber(device.getCode());
                person.setPersonId(userId);
                person.setCardNo(userDO.getCode());
                person.setFacePath(userDO.getFacePicture());
                person.setFingerprintPathList(JsonUtils.parseArray(userDO.getFingerprintPicture(),String.class));
                userIdList.add(id);
                list.add(person);
            }
        }
        return list;
    }

    private List<MacsRestPersonUpdateVO> getAllUserToRestPerson(List<MacsRestPersonUpdateVO> list, DeviceDO device) {
        List<UserDO> userDOS = userMapper.selectList();
        for (UserDO userDO : userDOS) {
            String id = userDO.getId();
            MacsRestPersonUpdateVO person = new MacsRestPersonUpdateVO().setPersonId(id);
            person.setDeviceNumber(device.getCode());
            person.setPersonId(userDO.getUserId());
            person.setCardNo(userDO.getCode());
            person.setFacePath(userDO.getFacePicture());
            person.setFingerprintPathList(JsonUtils.parseArray(userDO.getFingerprintPicture(),String.class));
            list.add(person);
        }
        return list;
    }

    private List<MacsRestPersonUpdateVO> getAllVisitorByRegion(DeviceDO device, String regionId) {
        List<VisitorRegionDO> visitorRegionDOList = visitorRegionMapper.getAuthorizedVisitorByRegion(regionId);
        List<MacsRestPersonUpdateVO> list = new ArrayList<>();
        Map<String,VisitorRegionDO> map = new HashMap<>();
        RegionDO regionDO = regionMapper.selectById(regionId);
        if (regionDO.getPublicStatus()) {
            return getAllVisitorToRestPerson(list, device);
        }
        for (VisitorRegionDO visitorRegionDO : visitorRegionDOList) {
            String visitorId = visitorRegionDO.getVisitorId();
            if (map.containsKey(visitorId)) {
                VisitorRegionDO visitorRegion = map.get(visitorId);
                if (visitorRegion.getEffectiveDate().isAfter(visitorRegionDO.getEffectiveDate())) {
                    visitorRegion.setEffectiveDate(visitorRegionDO.getEffectiveDate());
                }
                if (visitorRegion.getInvalidDate().isBefore(visitorRegionDO.getInvalidDate())) {
                    visitorRegion.setInvalidDate(visitorRegionDO.getInvalidDate());
                }
            } else {
                map.put(visitorId,visitorRegionDO);
            }
        }
        for (Map.Entry<String, VisitorRegionDO> entry : map.entrySet()) {
            MacsRestPersonUpdateVO updateVO = new MacsRestPersonUpdateVO();
            updateVO.setPersonId(entry.getKey());
            updateVO.setDeviceNumber(device.getCode());
            VisitorDO visitorDO = visitorMapper.selectById(entry.getKey());
            VisitorRegionDO value = entry.getValue();
            updateVO.setValidStart(value.getEffectiveDate().toString());
            updateVO.setValidEnd(value.getInvalidDate().toString());
            updateVO.setCardNo(visitorDO.getCode());
            updateVO.setFacePath(visitorDO.getFacePicture());
            updateVO.setFingerprintPathList(JsonUtils.parseArray(visitorDO.getFingerprintPicture(),String.class));
            list.add(updateVO);
        }
        return list;
    }

    private List<MacsRestPersonUpdateVO> getAllVisitorToRestPerson(List<MacsRestPersonUpdateVO> list, DeviceDO device) {
        List<VisitorDO> VisitorDOs = visitorMapper.selectList();
        for (VisitorDO visitorDO : VisitorDOs) {
            MacsRestPersonUpdateVO updateVO = new MacsRestPersonUpdateVO();
            updateVO.setPersonId(visitorDO.getId());
            updateVO.setDeviceNumber(device.getCode());
            updateVO.setCardNo(visitorDO.getCode());
            updateVO.setFacePath(visitorDO.getFacePicture());
            updateVO.setFingerprintPathList(JsonUtils.parseArray(visitorDO.getFingerprintPicture(),String.class));
            list.add(updateVO);
        }
        return list;
    }

    private List<MacsRestPersonUpdateVO> updatePerson(URI uri, String personC, List<MacsRestPersonUpdateVO> personList) {
        List<MacsRestPersonUpdateVO> personN = new ArrayList<>();
        if (personC == null || personC.equals("")) {
            boolean b = personN.addAll(addUserAndVisitor(uri, personList));
        } else {
            List<MacsRestPersonUpdateVO> maps = JsonUtils.parseArray(personC, MacsRestPersonUpdateVO.class);
            personN.addAll(maps);
            List<MacsRestPersonUpdateVO> add = new ArrayList<>();
            List<MacsRestPersonUpdateVO> update = new ArrayList<>();
            List<MacsRestPersonUpdateVO> del= new ArrayList<>();
            boolean b = compareDiff(personList, maps, personN, add, update, del);
            boolean b1 = personN.addAll(addUserAndVisitor(uri, add));
            boolean b2 = personN.addAll(updateUserAndVisitor(uri, update));
            boolean b3 = personN.removeAll(deleteUserAndVisitor(uri, del));
        }
        return personN;
    }

    private boolean compareDiff(List<MacsRestPersonUpdateVO> list,
                                List<MacsRestPersonUpdateVO> maps,
                                List<MacsRestPersonUpdateVO> now,
                                List<MacsRestPersonUpdateVO> listAdd,
                                List<MacsRestPersonUpdateVO> listUpdate,
                                List<MacsRestPersonUpdateVO> listDel) {
        listDel.addAll(maps);
        for (MacsRestPersonUpdateVO person : list) {
            boolean isNew = true;
            for (MacsRestPersonUpdateVO map : maps) {
                if (map.getPersonId().equals(person.getPersonId())) {
                    listDel.remove(map);
                    isNew = false;
                    if (hasChange(person,map)) {
                        listUpdate.add(person);
                    } else {
                        now.add(map);
                    }
                    break;
                }
            }
            if (isNew) {
                listAdd.add(person);
            }
        }
        return true;
    }

    private boolean hasChange(MacsRestPersonUpdateVO now, MacsRestPersonUpdateVO old) {
        if (now.getValidStart() != null) {
            if (!now.getValidStart().equals(old.getValidStart())) return true;
        }
        if (now.getValidEnd() != null) {
            if (!now.getValidEnd().equals(old.getValidEnd())) return true;
        }
        if (!now.getCardNo().equals(old.getCardNo())) return true;
        if (!now.getFacePath().equals(old.getFacePath())) return true;
        List<String> fingerprintPathListN = now.getFingerprintPathList();
        List<String> fingerprintPathListO = old.getFingerprintPathList();
        if (fingerprintPathListN != null) {
            if (fingerprintPathListO != null) {
                if (fingerprintPathListN.size() != fingerprintPathListO.size()) return true;
                for (String newPath : fingerprintPathListN) {
                    if (!fingerprintPathListO.contains(newPath)) return true;
                }
            } else return true;
        } else if (fingerprintPathListO != null) return true;

        return false;
    }

    /**
     * 添加用户访客,发送至门禁控制器
     */
    @Override
    public List<MacsRestPersonUpdateVO> addUserAndVisitor(URI uri, List<MacsRestPersonUpdateVO> userList) {
        List<MacsRestPersonUpdateVO> list = new ArrayList<>();
        generateRestPerson(userList);
        for (MacsRestPersonUpdateVO personUpdateVO : userList) {
            String id = macsInteractiveApi.addUserVisitorToDevice(uri, personUpdateVO);
            if (id != null) list.add(personUpdateVO.setUserID(id));
        }
        return list;
    }

    /**
     * 更新用户访客,发送至门禁控制器
     */
    @Override
    public List<MacsRestPersonUpdateVO> updateUserAndVisitor(URI uri, List<MacsRestPersonUpdateVO> userList) {
        List<MacsRestPersonUpdateVO> list = new ArrayList<>();
        generateRestPerson(userList);
        for (MacsRestPersonUpdateVO personUpdateVO : userList) {
            boolean b = macsInteractiveApi.updateUserVisitorToDevice(uri, personUpdateVO);
            if (b) list.add(personUpdateVO);
        }
        return list;
    }

    private void generateRestPerson(List<MacsRestPersonUpdateVO> userList) {

    }

    /**
     * 删除用户访客,发送至门禁控制器
     */
    @Override
    public List<MacsRestPersonUpdateVO> deleteUserAndVisitor(URI uri, List<MacsRestPersonUpdateVO> userList) {
        List<MacsRestPersonUpdateVO> list = new ArrayList<>();
        for (MacsRestPersonUpdateVO personUpdateVO : userList) {
            boolean b = macsInteractiveApi.deleteUserVisitorFromDevice(uri, personUpdateVO);
            if (b) list.add(personUpdateVO);
        }
        return list;
    }

    @Override
    public void deleteAllUserAndVisitor(DeviceDO device) throws URISyntaxException {
        URI uri = macsInteractiveService.getUriByDeviceCode(device.getCode());
        MacsRestPersonUpdateVO personUpdateVO = new MacsRestPersonUpdateVO();
        personUpdateVO.setDeviceNumber(device.getCode());
        macsInteractiveApi.deleteAllUserVisitorFromDevice(uri,personUpdateVO);
    }
}
