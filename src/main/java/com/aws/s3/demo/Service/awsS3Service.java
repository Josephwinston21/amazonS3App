package com.aws.s3.demo.Service;

import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.aws.s3.demo.Configuration.awsS3Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
@Slf4j
public class awsS3Service {
    @Autowired
    protected awsS3Config s3Config;
    @Value("${application.bucket.name}")
    protected String storageBucket;

    @Autowired
    public awsS3Service(awsS3Config s3Config) {
        this.s3Config = s3Config;
    }

    public String uploadFile(MultipartFile file) {
        File myFile = getIndividualFile(file);
        String fileName = file.getOriginalFilename() + "_" + System.currentTimeMillis();
        s3Config.S3Client().putObject(new PutObjectRequest(storageBucket, fileName, myFile));
        log.info("Successfully Uploaded the File");
        myFile.delete();
        return "Successfully Uploaded the file to s3:" + fileName;
    }

    public String deleteFile(String file) {
        s3Config.S3Client().deleteObject(storageBucket, file);
        log.info("Successfully deleted the file");
        return "Successfully deleted the file" + file;
    }

    public byte[] downloadFile(String fileName) throws IOException {
        S3Object obj = s3Config.S3Client().getObject(new GetObjectRequest(storageBucket, fileName));
        S3ObjectInputStream s3ObjectInputStream = obj.getObjectContent();
        return IOUtils.toByteArray(s3ObjectInputStream);
    }

    public File getIndividualFile(MultipartFile fileObj) {
        File convertedFile = new File(fileObj.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(fileObj.getBytes());
        } catch (IOException e) {
            log.error("Unable to find the file:" + convertedFile);
        }
        return convertedFile;
    }
}



