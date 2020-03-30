package com.ls.l7mall_2.service.impl;

import com.google.common.collect.Lists;
import com.ls.l7mall_2.service.FileService;
import com.ls.l7mall_2.util.FTPServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author laijs
 * @date 2020-3-29-14:21
 */
@Service("fileService")
public class FileServiceImpl implements FileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    
    @Override
    public String upload(MultipartFile multipartFile, String path) {
        // 获取原文件名
        String fileName = multipartFile.getOriginalFilename();
        // 分离拓展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 使用UUID和拓展名生成一个用于上传的不重复的文件名
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);

        // 根据路径生成目录
        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setExecutable(true);
            fileDir.mkdirs();
        }
        // 根据路径和文件名生成目标文件类
        File targetFile = new File(path, uploadFileName);
        // 生成文件
        try {
            multipartFile.transferTo(targetFile);
            // 至此文件已经成功上传至tomcat服务器

            // 将文件从tomcat服务器上传至FTPServer服务器中
            boolean upload = FTPServerUtils.upload(Lists.newArrayList(targetFile));
            if(!upload){
                logger.error("上传文件异常");
                return  null;
            }
            // 将文件从tomcat服务器中删除
            targetFile.delete();

        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }
    
}
