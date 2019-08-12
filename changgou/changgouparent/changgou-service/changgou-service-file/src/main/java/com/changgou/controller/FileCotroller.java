package com.changgou.controller;

import com.changgou.file.FastDFSFile;
import com.changgou.utils.FastDFSClient;
import io.lettuce.core.ConnectionId;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.aspectj.weaver.ast.Var;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageServer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.ch.IOUtil;

import java.io.FileOutputStream;
import java.io.IOException;


@RestController
@CrossOrigin
@RequestMapping("/file")
public class FileCotroller {
    @RequestMapping("/uploadfile")
    public String uploadfile(@PathVariable(value = "file")MultipartFile file){
        try {
            byte[] bytes = file.getBytes();// 文件内容
            String name = file.getOriginalFilename(); // 文件名
            // 文件扩展名file.getOriginalFilename()

            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            String author="soluo";
            String md5="aaa";

            FastDFSFile fastDFSFile = new FastDFSFile(name, bytes, ext, md5, author);
            String[] updaload = FastDFSClient.updaload(fastDFSFile);
                return FastDFSClient.getUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/uploadfile2")
    public String uploadfile2(@PathVariable(value = "file") MultipartFile file){
        try {
            byte[] bytes = file.getBytes();// 文件内容
            String name = file.getOriginalFilename(); // 文件名
            // 文件扩展名file.getOriginalFilename()
            String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String[] strings = FastDFSClient.updaload2(ext,bytes,null);
            return FastDFSClient.getUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // 文件下载

    public void download(String groupname, String filename){
        // groupname 在返回路径中 可以看到 （是属于 服务器的某个文件）
        try {

            byte[] fileByte = FastDFSClient.download(groupname, filename);
            IOUtils.write(fileByte,new FileOutputStream("d:/"+filename+".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    // 文件删除

    public void delete(@PathVariable(value = "file") MultipartFile file){
        // groupname 在返回路径中 可以看到 （是属于 服务器的某个文件）
        try {
            String groupname="  ";
            String filename=file.getName();
       FastDFSClient.deleteFile(groupname,filename);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // 获取存储服务器信息

    public void getStorageServerInfo( @PathVariable(value = "groupname") String groupname ) {
        // groupname 在返回路径中 可以看到 （是属于 服务器的某个文件）
        try {


            StorageServer group = FastDFSClient.getStorageServerInfo(groupname);
            String hostAddress = group.getInetSocketAddress().getAddress().getHostAddress();
            int storePathIndex = group.getStorePathIndex();// 存储服务器脚标准
            int port = group.getInetSocketAddress().getPort();
            System.out.println("服务器的IP地址为："+hostAddress);
            System.out.println("服务器的角标为："+hostAddress);
            System.out.println("服务器的端口为："+hostAddress);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取多个存储服务器信息

    public void getStorageServerInfos(String groupname,String filename ) {
        // groupname 在返回路径中 可以看到 （是属于 服务器的某个文件）
        try {


            ServerInfo[] storageServerInfos = FastDFSClient.getStorageServerInfos(groupname, filename);
            for (ServerInfo storageServerInfo : storageServerInfos) {
                String ipAddr = storageServerInfo.getIpAddr();
                int port = storageServerInfo.getPort();
                System.out.println("服务器的IP地址为："+ipAddr);

                System.out.println("服务器的端口为："+port);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    }
