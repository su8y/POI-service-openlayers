package com.example.core.file;

import com.example.core.file.common.TargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<UploadImage, String> {

    @Query("select i from UploadImage i where i.targetId = :targetId and i.type like :targetType")
    List<UploadImage> findUploadImagesByTarget(@Param("targetId") Long targetId, @Param("targetType") TargetType targetType);
}
