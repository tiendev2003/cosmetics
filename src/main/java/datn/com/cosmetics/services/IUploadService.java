package datn.com.cosmetics.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {
    List<String> uploadImages(MultipartFile[] files) throws IOException;
}
