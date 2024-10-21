package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.File;
import at.technikum.Invicrea2WebShopbackend.repository.FileRepository;
import at.technikum.Invicrea2WebShopbackend.storage.FileStorage;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final FileStorage fileStorage;

    public File save ( File file ) {
        return fileRepository.save( file );
    }

    private static final List<String> ALLOWED_IMAGE_TYPES =
            List.of( "image/jpeg", "image/png", "image/gif" );

    public File upload ( MultipartFile toUpload ) {
        if (!isImage( toUpload )) {
            throw new IllegalArgumentException( "Only image files (JPEG, PNG, GIF) are allowed." );
        }

        String externalId = fileStorage.upload( toUpload );
        File file = new File( );
        file.setName( toUpload.getOriginalFilename( ) );
        file.setExternalId( externalId );
        file.setContentType( toUpload.getContentType( ) );
        return fileRepository.save( file );
    }

    private boolean isImage ( MultipartFile file ) {
        String contentType = file.getContentType( );
        return contentType != null && ALLOWED_IMAGE_TYPES.contains( contentType );
    }

    /*public File upload(MultipartFile toUpload) {
        String externalId = fileStorage.upload(toUpload);
        File file = new File();
        file.setName(toUpload.getOriginalFilename());
        file.setExternalId(externalId);
        file.setContentType(toUpload.getContentType());
        return fileRepository.save(file);
    }*/

    public File findById ( UUID id ) {
        return fileRepository.findById( id )
                .orElseThrow( EntityNotFoundException::new );
    }

    public Resource asResource ( File file ) {
        InputStream stream = fileStorage.load( file.getExternalId( ) );
        return new InputStreamResource( stream );
    }

    public List<File> getAllFiles () {
        return fileRepository.findAll( );
    }
}
