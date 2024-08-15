package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.model.File;
import at.technikum.Invicrea2WebShopbackend.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public File upload(@RequestParam("file") MultipartFile toUpload) {
        return fileService.upload(toUpload);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> retrieve(@PathVariable UUID id) {
        File file = fileService.findById(id);

        Resource resource = fileService.asResource(file);
        MediaType mediaType = MediaType.parseMediaType(file.getContentType());

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .body(resource);
    }
}
