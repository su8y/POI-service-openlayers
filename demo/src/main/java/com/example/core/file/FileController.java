package com.example.core.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileController {

    @Value("${file.dir}")
    String fileDir;

    @GetMapping("/download/{imageName}")
    @ResponseBody
    public ResponseEntity<Resource> downloadImage(@PathVariable(name="imageName") String imageName) throws MalformedURLException {
        //image dir application.yml 의 설정
        File uploads = new File(fileDir);
        Path uploadPath = uploads.toPath().resolve(imageName).normalize();
        Resource resource = new UrlResource(uploadPath.toUri());

        if (!resource.exists() || !resource.isReadable())
            return ResponseEntity.notFound().build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
