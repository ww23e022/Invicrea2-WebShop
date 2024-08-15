package at.technikum.Invicrea2WebShopbackend.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileStorage {

    String upload(MultipartFile file);

    InputStream load(String id);
}
