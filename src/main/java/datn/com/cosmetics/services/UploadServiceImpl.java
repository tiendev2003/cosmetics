package datn.com.cosmetics.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadServiceImpl implements IUploadService {

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public List<String> uploadImages(MultipartFile[] files) throws IOException {
        return fileStorageService.storeMultipleFiles(files).stream()
                .map(fileName -> "/uploads/" + fileName)
                .collect(Collectors.toList());
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        return "/uploads/" + fileStorageService.storeFile(file);
    }

    @Override
    public List<String> getUploadedImages() throws IOException {
        return fileStorageService.loadAllFiles().stream()
                .map(fileName -> "/uploads/" + fileName)
                .collect(Collectors.toList());
    }
}
