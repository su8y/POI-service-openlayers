package com.example.core.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final ResourceLoader resourceLoader;
    private final FileRepository fileRepository;

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

            Resource resource = resourceLoader.getResource("classpath:" + fileDir);

            Path path = resource.getFile().toPath();
            Path uploadPath = path.resolve(storeFilename).normalize();

            Assert.state(!Files.exists(uploadPath), storeFilename + "File already exsits");

            UploadImage buildEntity = UploadImage.builder()
                    .storeFilename(storeFilename)
                    .originalFilename(originalFilename)
                    .ext(ext)
                    .build();
            UploadImage save = fileRepository.save(buildEntity);


            file.transferTo(uploadPath);
            uploadImageList.add(save);
        }
        return uploadImageList;

    }
}
