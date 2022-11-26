package com.pinyougou.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class UploadUtil {

    /**
     * 本地文件上传
     * @param confFileName  连接tracker的配置文件的全路径 : classpath:tracker.conf
     * @param fileName 本地文件名称,classpath:xx.xxx
     * @throws Exception
     */
    public static  void upload(String confFileName, String fileName) throws Exception {

        //解析类路径
        confFileName = getConfig(confFileName);
        fileName = getConfig(fileName);

        //读取配置文件并初始化
        ClientGlobal.init(confFileName);

        //创建Tracker客户端
        TrackerClient trackerClient = new TrackerClient();

        //连接Tracker获得TrackerServer
        TrackerServer trackerServer = trackerClient.getConnection();

        //通过TrackerServer来创建一个Storage客户端
        StorageClient storageClient = new StorageClient(trackerServer, null);

        //通过Storage客户端实现文件上传，参数：本地文件的绝对路径，扩展名
        String[] returnValues = storageClient.upload_file(fileName, null, null);

        for (String value : returnValues)
        {
            System.out.println(value);
        }
    }

    /**
     * 远程文件上传
     * @param confFileName  连接tracker的配置文件的全路径
     * @param fileBytes  文件字节数组
     * @param suffix  文件后缀名
     * @throws Exception
     */
    public static String[] upload(String confFileName, byte[] fileBytes, String suffix) throws Exception {

        //解析类路径
        confFileName = getConfig(confFileName);

        //读取配置文件并初始化
        ClientGlobal.init(confFileName);

        //创建Tracker客户端
        TrackerClient trackerClient = new TrackerClient();

        //连接Tracker获得TrackerServer
        TrackerServer trackerServer = trackerClient.getConnection();

        //通过TrackerServer来创建一个Storage客户端
        StorageClient storageClient = new StorageClient(trackerServer, null);

        //通过Storage客户端实现文件上传，参数：本地文件的绝对路径，扩展名
        String[] returnValues = storageClient.upload_file(fileBytes, suffix, null);

        return returnValues;
    }

    public static String getConfig(String path)
    {
        //类路径下的文件
        if (path.startsWith("classpath:"))
        {
            path = path.replace("classpath:", UploadUtil.class.getResource("/").getPath());
        }
        return path;
    }

    public static void main(String[] args) throws Exception {
        upload("classpath:tracker.conf", "classpath:1.png");
    }
}
