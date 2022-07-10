package com.example.demo.controllers.image;

import com.example.demo.exceptions.responsestatus.ImageNotFoundException;
import com.example.demo.services.image.ImageStorageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageControllerImpl implements ImageController {

    private final @NonNull ImageStorageService imageStorageService;

    @Override
    public ResponseEntity<Resource> imageFile(String imageName, HttpServletRequest request)
            throws IOException, ImageNotFoundException {
        Optional<Resource> maybeResource = imageStorageService.loadImageAsResource(imageName);
        if (maybeResource.isEmpty()) {
            throw new ImageNotFoundException(String.format("Image not found %s", imageName));
        }
        Resource resource = maybeResource.get();
        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
