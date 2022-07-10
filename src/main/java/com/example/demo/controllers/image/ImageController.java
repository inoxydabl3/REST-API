package com.example.demo.controllers.image;

import com.example.demo.exceptions.responsestatus.ImageNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequestMapping(path = "${app.imagesEndpoint}")
public interface ImageController {

    @GetMapping(path = "/{imageName}")
    ResponseEntity<Resource> imageFile(@PathVariable String imageName, HttpServletRequest request)
            throws IOException, ImageNotFoundException;

}
