package com.example.core.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUtil {

    @Value("${file.dir}")
    private String fileDir;

    public List<UploadImage> uploadImages(List<MultipartFile> files) throws IOException {
        List<UploadImage> uploadImageList = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;
            String originalFilename = file.getOriginalFilename();

            int indexBeforeExt = originalFilename.lastIndexOf(".");
            String ext = originalFilename.substring(indexBeforeExt + 1);

            String UUID = java.util.UUID.randomUUID().toString();
            String storeFilename = String.format("%s.%s", UUID, ext);

            File uploads = new File(fileDir);
//            Resource resource = resourceLoader.getResource(fileDir);

            Path path = uploads.toPath();
            Path uploadPath = path.resolve(storeFilename).normalize();


            Assert.state(!Files.exists(uploadPath), storeFilename + "File already exsits");

            UploadImage buildEntity = UploadImage.builder()
                    .storeFilename(storeFilename)
                    .originalFilename(originalFilename)
                    .ext(ext)
                    .build();


            file.transferTo(uploadPath);
            uploadImageList.add(buildEntity);
        }
        return uploadImageList;

    }
}
