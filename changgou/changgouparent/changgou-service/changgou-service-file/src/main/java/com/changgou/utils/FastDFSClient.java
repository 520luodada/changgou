package com.changgou.utils;

import com.changgou.file.FastDFSFile;
import org.aspectj.weaver.ast.Var;
import org.csource.common.MyException;
import org.csource.fastdfs.*;

import org.csource.common.NameValuePair;
import org.springframework.core.io.ClassPathResource;


import javax.sound.sampled.Port;
import java.io.IOException;

public class FastDFSClient {
    static {
        try {   // 初始化配置
            String path = "fdfs_client.conf";
            String path1 = new ClassPathResource(path).getPath();

            ClientGlobal.init(path1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] updaload(FastDFSFile fastDFSFile) {
        try {

            String name = fastDFSFile.getName();
            byte[] file_buff = fastDFSFile.getContent();
            String ext_name = fastDFSFile.getExt();

            NameValuePair[] meta_list = new NameValuePair[1];
            meta_list[0] = new NameValuePair(fastDFSFile.getAuthor());


            // 创建跟踪服务器客户端
            TrackerClient trackerClient = new TrackerClient();
            // 获取连接

            TrackerServer connection = trackerClient.getConnection();
            // 创建 存储服务器客户端
            StorageClient storageClient = new StorageClient(connection, null);
            String[] strings = storageClient.upload_file(file_buff, ext_name, meta_list);
            return strings;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 上传到服务器不需要那么多
    public static String[] updaload2(String ext_name, byte[] file_buff, String des) {
        try {


            NameValuePair[] meta_list = new NameValuePair[1];
            meta_list[0] = new NameValuePair(des);


            // 创建跟踪服务器客户端
            TrackerClient trackerClient = new TrackerClient();
            // 获取连接

            TrackerServer connection = trackerClient.getConnection();
            // 创建 存储服务器客户端
            StorageClient storageClient = new StorageClient(connection, null);
            String[] strings = storageClient.upload_file(file_buff, ext_name, meta_list);
            return strings;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取 服务器地址  用于返回 拼接成完整地址   最后
    public static String getUrl() throws IOException {
        try {
            // 创建跟踪服务器客户端
            TrackerClient trackerClient = new TrackerClient();
            // 获取连接


            TrackerServer connection = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(connection, null);
            // IP地址
            String hostAddress = connection.getInetSocketAddress().getAddress().getHostAddress();
            int port = ClientGlobal.getG_tracker_http_port();
            String url = "http://" + hostAddress + ":" + port;
            return url;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }


    // 文件下载

    public static   byte[] download(String groupname, String fiename) {
        try {


            // 创建跟踪服务器客户端
            TrackerClient trackerClient = new TrackerClient();
            // 获取连接

            TrackerServer connection = trackerClient.getConnection();
            // 创建 存储服务器客户端
            StorageClient storageClient = new StorageClient(connection, null);
            byte[] bytes = storageClient.download_file(groupname, fiename);
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // 文件删除

    public static void deleteFile(String groupname, String fiename) {
        try {
            // 创建跟踪服务器客户端
            TrackerClient trackerClient = new TrackerClient();
            // 获取连接

            TrackerServer connection = trackerClient.getConnection();
            // 创建 存储服务器客户端
            StorageClient storageClient = new StorageClient(connection, null);
            storageClient.delete_file(groupname, fiename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // getStorageServerInfo  获取存储服务信息
    public static StorageServer getStorageServerInfo(String groupName){
        try {
            // 创建跟踪服务器客户端
            TrackerClient trackerClient =new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            // 创建存储服务器客户端
           StorageServer storageServer=trackerClient.getStoreStorage(trackerServer,groupName);
            System.out.println("存储服务器的信息为："+storageServer);
            return storageServer;
        } catch (Exception e) {
            e.printStackTrace();
        }
return  null;
    }


    // getStorageServerInfo  获取多个存储服务信息
    public static  ServerInfo[] getStorageServerInfos(String groupName,String fileName){
        try {
            // 创建跟踪服务器客户端
            TrackerClient trackerClient =new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            // 创建存储服务器客户端

            ServerInfo[] fetchStorages = trackerClient.getFetchStorages(trackerServer, groupName, fileName);


            return fetchStorages;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}


