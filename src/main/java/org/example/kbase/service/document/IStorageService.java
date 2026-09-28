package org.example.kbase.service.document;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {

    void store(MultipartFile file, String storageKey);

    Resource load(String storageKey);

    void delete(String storageKey);

}
