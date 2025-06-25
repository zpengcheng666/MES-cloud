package com.miyu.module.pdm.netty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class ZipUtil {

    private final static int buffer = 1024;

    private static void createDirectory(String directory, String subDirectory) {
        String dir[];
        File fl = new File(directory);
        try {
            if (subDirectory == "" && fl.exists() != true) {
                fl.mkdir();
            } else if (subDirectory != "") {
                dir = subDirectory.replace('\\', '/').split("/");
                for (int i = 0; i < dir.length; i++) {
                    File subFile = new File(directory + File.separator + dir[i]);
                    if (subFile.exists() == false) {
                        subFile.mkdir();
                    }
                    directory += File.separator + dir[i];
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static String unZip(String zipFilePath, String outputDirectory) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath,"GB18030");
            Enumeration<?> e = zipFile.getEntries();
            ZipEntry zipEntry = null;
            createDirectory(outputDirectory, "");
            while (e.hasMoreElements()) {
                zipEntry = (ZipEntry) e.nextElement();
//                System.out.println("unzip " + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName().trim();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(outputDirectory + File.separator + name);
                    if (!f.exists()) {
                        f.mkdir();
                    }

                } else {
                    String fileName = zipEntry.getName();
                    fileName = fileName.replace('\\', '/');
                    if (fileName.indexOf("/") != -1) {
                        createDirectory(outputDirectory, fileName.substring(0, fileName.lastIndexOf("/")));
                        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                    }
                    File f = new File(outputDirectory + File.separator + zipEntry.getName());
                    f.createNewFile();
                    InputStream in = zipFile.getInputStream(zipEntry);
                    FileOutputStream out = new FileOutputStream(f);
                    byte[] by = new byte[buffer];
                    int c;
                    while ((c = in.read(by)) != -1) {
                        out.write(by, 0, c);
                    }
                    in.close();
                    out.close();
                }
            }
//            System.out.println("unzip finished");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return outputDirectory;
    }

    public static boolean zip(String srcDirName, String zipFilePath) {
        boolean flag = false;
        try {
            File srcdir = new File(srcDirName);
            if (!srcdir.exists())
                throw new RuntimeException(srcDirName + " is not exist!");
            Project prj = new Project();
            Zip zip_ = new Zip();
            zip_.setProject(prj);
            zip_.setDestFile(new File(zipFilePath));

            FileSet fileSet = new FileSet();
            fileSet.setProject(prj);
            fileSet.setDir(srcdir);
            zip_.addFileset(fileSet);

            zip_.execute();
            flag = true;
            System.out.println("zip finished");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return flag;
    }



    /**
     * @Title: compress
     * @Description: TODO
     * @param filePaths 需要压缩的文件地址列表（绝对路径）
     * @param zipFilePath 需要压缩到哪个zip文件（无需创建这样一个zip，只需要指定一个全路径）
     * @param keepDirStructure 压缩后目录是否保持原目录结构
     * @throws IOException
     * @return int  压缩成功的文件个数
     */
    public static int compress(List<String> filePaths, String zipFilePath,Boolean keepDirStructure) throws IOException{
        byte[] buf = new byte[1024];
        File zipFile = new File(zipFilePath);
        //zip文件不存在，则创建文件，用于压缩
        if(!zipFile.exists())
            zipFile.createNewFile();
        int fileCount = 0;//记录压缩了几个文件？
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            for(int i = 0; i < filePaths.size(); i++){
                String relativePath = filePaths.get(i);
                if(StringUtils.isEmpty(relativePath)){
                    continue;
                }
                File sourceFile = new File(relativePath);//绝对路径找到file
                if(sourceFile == null || !sourceFile.exists()){
                    continue;
                }
                FileInputStream fis = new FileInputStream(sourceFile);
                if(keepDirStructure!=null && keepDirStructure){
                    //保持目录结构
                    zos.putNextEntry(new ZipEntry(relativePath));
                }else{
                    //直接放到压缩包的根目录
                    zos.putNextEntry(new ZipEntry(sourceFile.getName()));
                }
                //System.out.println("压缩当前文件："+sourceFile.getName());
                int len;
                while((len = fis.read(buf)) > 0){
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                fis.close();
                fileCount++;
            }
            zos.close();
            //System.out.println("压缩完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileCount;
    }
}
