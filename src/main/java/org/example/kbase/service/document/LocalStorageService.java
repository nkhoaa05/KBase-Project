package org.example.kbase.service.document;


import jakarta.annotation.PostConstruct;
import org.example.kbase.common.exception.BadRequestException;
import org.example.kbase.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
@ConditionalOnProperty(
        name = "storage.type",
        havingValue = "local",
        matchIfMissing = true
)
public class LocalStorageService implements IStorageService{

    private final Path rootLocation;

    public LocalStorageService(
            @Value("${storage.local.upload-dir}") String uploadDir
    ){
        this.rootLocation = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not initialize storage directory", e
            );
        }
    }

    @Override
    public void store(MultipartFile file,
                      String storageKey) {

        try{
            Path destination = rootLocation
                    .resolve(storageKey)
                    .normalize();

            if (!destination.startsWith(rootLocation)) {
                throw new BadRequestException("Invalid storage path");
            }

            Files.createDirectories(destination.getParent());

            try (var inputStream = file.getInputStream()) {

                Files.copy(
                        inputStream,
                        destination,
                        StandardCopyOption.REPLACE_EXISTING
                );

            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to store file", e);
        }

    }

    @Override
    public Resource load(String storageKey) {

        try {
            Path file = rootLocation
                    .resolve(storageKey)
                    .normalize();

            if (!file.startsWith(rootLocation)) {
                throw new BadRequestException("Invalid storage path");
            }

            Resource resource =
                    new UrlResource(file.toUri());

            if (!resource.exists()) {
                throw new ResourceNotFoundException("File not found");
            }

            return resource;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not load file", e
            );
        }
    }

    @Override
    public void delete(String storageKey) {

        try {
            Path file = rootLocation
                    .resolve(storageKey)
                    .normalize();

            if (!file.startsWith(rootLocation)) {
                throw new ResourceNotFoundException("Invalid storage path");
            }

            Files.deleteIfExists(file);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not delete file", e
            );
        }
    }
}
