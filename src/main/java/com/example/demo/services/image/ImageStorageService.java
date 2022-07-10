package com.example.demo.services.image;

import com.example.demo.exceptions.ImageStorageException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ImageStorageService {

    String storeImage(MultipartFile image) throws ImageStorageException;

    Optional<Resource> loadImageAsResource(String imageName);

    public boolean deleteImage(String imageName);

}
