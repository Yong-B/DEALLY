package com.example.product.file;

import com.example.product.domain.Item;
import com.example.product.file.domain.UploadFile;
import com.example.product.file.repository.UploadFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    private final UploadFileRepository uploadFileRepository;

    public FileStore(UploadFileRepository uploadFileRepository) {
        this.uploadFileRepository = uploadFileRepository;
    }

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    // ✅ 여러 개의 파일 저장 (Item과 연결)
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles, Item item) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {  // ✅ 올바른 파일 체크
                storeFileResult.add(storeFile(multipartFile,item));
            }
        }
        return storeFileResult;
    }

    // ✅ 단일 파일 저장 (Item과 연결)
    public UploadFile storeFile(MultipartFile multipartFile, Item item) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        UploadFile uploadFile = new UploadFile(originalFilename, storeFileName, item);
        return uploadFileRepository.save(uploadFile);  // ✅ DB에 저장
    }

    // ✅ 저장 파일명 생성 (UUID + 확장자)
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // ✅ 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}