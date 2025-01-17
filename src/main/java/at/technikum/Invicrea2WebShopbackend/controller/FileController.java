package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.model.File;
import at.technikum.Invicrea2WebShopbackend.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public File upload ( @RequestParam("file") MultipartFile toUpload ) {
        try {
            return fileService.upload( toUpload );
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST, e.getMessage( ) );
        }
    }

    /*@PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public File upload(@RequestParam("file") MultipartFile toUpload) {
        return fileService.upload(toUpload);
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<Resource> retrieve ( @PathVariable UUID id ) {
        File file = fileService.findById( id );

        Resource resource = fileService.asResource( file );
        MediaType mediaType = MediaType.parseMediaType( file.getContentType( ) );

        return ResponseEntity
                .ok( )
                .contentType( mediaType )
                .body( resource );
    }

    @GetMapping()
    public ResponseEntity<List<File>> getAllFiles () {
        List<File> files = fileService.getAllFiles( );
        return ResponseEntity.ok( files );
    }
}
