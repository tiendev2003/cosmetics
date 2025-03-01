package datn.com.cosmetics.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import datn.com.cosmetics.bean.response.ApiResponse;
import datn.com.cosmetics.services.IUploadService;

@RestController
@RequestMapping("/api/upload")
public class ApiUploadController {

    @Autowired
    private IUploadService uploadService;

    @PostMapping("/image")
    public ResponseEntity<ApiResponse<List<String>>> uploadImages(@RequestParam("files") MultipartFile[] files) {
        try {
            List<String> urls = uploadService.uploadImages(files);
            return ResponseEntity.ok(ApiResponse.success(urls, "Images uploaded successfully"));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
