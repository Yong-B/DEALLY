package com.example.product.file.repository;

import com.example.product.file.domain.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
    Optional<UploadFile> findUploadFileById(Long id);
}
