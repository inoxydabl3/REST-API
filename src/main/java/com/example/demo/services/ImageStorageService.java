package com.example.demo.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    String storeImage(MultipartFile image);

    Resource loadImageAsResource(String imageName);

    public boolean deleteImage(String imageName);

}
