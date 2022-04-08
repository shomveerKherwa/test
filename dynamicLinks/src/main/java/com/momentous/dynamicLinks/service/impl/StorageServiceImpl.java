package com.momentous.dynamicLinks.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.momentous.dynamicLinks.api.AmazonS3Api;
import com.momentous.dynamicLinks.service.StorageService;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation = Paths.get("upload-dir");
    
    @Autowired
    AmazonS3Api amazonS3Api;
    
    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void store(MultipartFile file) {
        if(file.isEmpty()) {
            throw new RuntimeException("PLease selct an file to upload");
        }
        try {
            InputStream inputStream = file.getInputStream();
            Path destination = this.rootLocation.resolve(
                    Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            amazonS3Api.fileUpload(inputStream);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(rootLocation, 1)
                        .filter(path -> !path.equals(this.rootLocation))
                        .map(rootLocation::relativize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

}
