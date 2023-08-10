package com.example.core.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "upload_image")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadImage {
    @Id
    String storeFilename;
    String originalFilename;
    String ext;

    @CreationTimestamp
    LocalDateTime createAt;

    Long targetId;
    String type;
}
