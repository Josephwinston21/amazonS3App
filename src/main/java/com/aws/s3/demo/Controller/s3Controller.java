package com.aws.s3.demo.Controller;

import com.aws.s3.demo.Service.awsS3Service;
import org.apache.http.entity.ByteArrayEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class s3Controller {
    @Autowired
    public awsS3Service s3Service;


    @PostMapping("/upload")
    public void upload(MultipartFile file){
        s3Service.uploadFile(file);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) throws IOException {
        byte[] data = s3Service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition","attachment:fileName:\""+fileName+"\"")
                .body(resource);

    }

    @DeleteMapping("/delete/{file}")
    public ResponseEntity<String> delete(@PathVariable String file){
        return new ResponseEntity<>(s3Service.deleteFile(file), HttpStatus.OK);
    }
}
