package com.example.demo.services.image;

import com.example.demo.exceptions.ImageStorageException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class ImageStorageServiceImpl implements ImageStorageService {

    private final @NonNull Path imagesPath;

    public ImageStorageServiceImpl(Path imagesPath) throws ImageStorageException {
        this.imagesPath = imagesPath;
        try {
            Files.createDirectories(imagesPath);
        } catch (Exception e) {
            throw new ImageStorageException("Could not create the directory where the image files will be stored.", e);
        }
    }

    private String getFileExtension(String fileName) throws IllegalArgumentException {
        int index = fileName.indexOf('.');
        return index == -1 ? "" : fileName.substring(index + 1);
    }

    private String createDisplayName(String originalFilename) {
        String name = UUID.randomUUID().toString();
        String extension = getFileExtension(originalFilename);
        if (!extension.isBlank()) {
            return name + '.' + extension;
        }
        return name;
    }

    private String createUniqueName(String originalFilename) {
        String imageName;
        do {
            imageName = createDisplayName(originalFilename);
        }
        while (imagesPath.resolve(imageName).toFile().exists());
        return imageName;
    }

    @Override
    public String storeImage(MultipartFile image) throws ImageStorageException {
        String imageName = createUniqueName(image.getOriginalFilename());
        Path targetLocation = imagesPath.resolve(imageName);
        try {
            Files.copy(image.getInputStream(), targetLocation);
            return imageName;
        } catch (IOException e) {
            throw new ImageStorageException(
                    String.format("Could not store image: '%s'. Please try again!", imageName), e);
        }
    }

    @Override
    public Optional<Resource> loadImageAsResource(String imageName) {
        try {
            Path imagePath = imagesPath.resolve(imageName).normalize();
            Resource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
                return Optional.of(resource);
            }
        } catch (MalformedURLException e) {
            log.error(String.format("Image not found %s", imageName), e);
        }
        return Optional.empty();
    }

    @Override
    public Path getImagesPath() {
        return imagesPath;
    }

    @Override
    public boolean deleteImage(String imageName) {
        try {
            Files.delete(imagesPath.resolve(imageName));
            return true;
        } catch (IOException e) {
            log.error(String.format("Error ocurred while deleting file '%s'", imageName), e);
            return false;
        }
    }

}
