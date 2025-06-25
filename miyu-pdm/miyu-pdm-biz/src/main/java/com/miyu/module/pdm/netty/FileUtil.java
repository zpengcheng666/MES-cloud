package com.miyu.module.pdm.netty;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileUtil {
    /**
     * MultipartFile转File
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public static File MultipartFileToFile(MultipartFile multipartFile) throws IOException {
        String multipartFileName = multipartFile.getOriginalFilename();
        String suffix=multipartFileName.substring(multipartFileName.lastIndexOf("."));
        File tempFile = File.createTempFile("tempFile", suffix);
        File file=new File(tempFile.getParent()+File.separator+multipartFileName);
        tempFile.renameTo(file);
        multipartFile.transferTo(file);
        tempFile.deleteOnExit();
        tempFile.delete();
        return file;
    }

    public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = new FileOutputStream(convFile)) {
            int byteRead = 0;
            byte[] buffer = new byte[1024];
            while ((byteRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, byteRead);
            }
        }
        return convFile;
    }
}
