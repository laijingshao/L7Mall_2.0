package com.ls.l7mall_2.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author laijs
 * @date 2020-3-29-14:20
 */

public interface FileService {
    public String upload(MultipartFile multipartFile, String path);
}
