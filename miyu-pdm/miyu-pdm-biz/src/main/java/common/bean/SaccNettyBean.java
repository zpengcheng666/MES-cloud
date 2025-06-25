package common.bean;

import java.io.File;
import java.io.Serializable;

public class SaccNettyBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // 文件
    private File file;
    // 文件名
    private String fileName;
    // 文件字节数组
    private byte[] bytes;
    // 文件读写起始坐标
    private int starPos;
    // 文件读写结束坐标
    private int endPos;
    // 方法名称
    private String methodName;
    // 参数的JSON形式字符串
    private String paramJsonStr;
    // 判断是上传还是下载
    private String upOrDownLoadFlag;
    // 文件长度
    private long fileLength;
    // 开始行
    private Long start;

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long l) {
        this.fileLength = l;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getStarPos() {
        return starPos;
    }

    public void setStarPos(int starPos) {
        this.starPos = starPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public String getParamJsonStr() {
        return paramJsonStr;
    }

    public void setParamJsonStr(String paramJsonStr) {
        this.paramJsonStr = paramJsonStr;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUpOrDownLoadFlag() {
        return upOrDownLoadFlag;
    }

    public void setUpOrDownLoadFlag(String upOrDownLoadFlag) {
        this.upOrDownLoadFlag = upOrDownLoadFlag;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

}
