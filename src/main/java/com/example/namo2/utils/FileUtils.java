package com.example.namo2.utils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.namo2.config.BaseException;
import com.example.namo2.config.BaseResponseStatus;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.namo2.config.BaseResponseStatus.FILE_NAME_EXCEPTION;
import static com.example.namo2.config.BaseResponseStatus.S3_FAILURE;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtils {
    private final S3Uploader s3Uploader;

    private String createFileName(String originalFileName) throws BaseException {
        return "memo/" + UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) throws BaseException {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new BaseException(FILE_NAME_EXCEPTION);
        }
    }

    public List<String> uploadImages(List<MultipartFile> files) throws BaseException {
        try {
            List<String> urls = new ArrayList<>();
            for (MultipartFile file : files) {
                if (Optional.ofNullable(file).isPresent()) {
                    urls.add(uploadImage(file));
                }
            }
            return urls;
        } catch (BaseException e) {
            throw new BaseException(FILE_NAME_EXCEPTION);
        }
    }

    private String uploadImage(MultipartFile file) throws BaseException {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        try (InputStream inputStream = file.getInputStream()) {
            s3Uploader.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new BaseException(BaseResponseStatus.S3_FAILURE);
        }
        return s3Uploader.getFileUrl(fileName);
    }


    public void delete(String url) throws BaseException{
        try {
            int IndexOf = url.indexOf("memo");
            String key = url.substring(IndexOf);
            s3Uploader.delete(key);
        } catch (SdkClientException e) {
            throw new BaseException(S3_FAILURE);
        }
    }
}
