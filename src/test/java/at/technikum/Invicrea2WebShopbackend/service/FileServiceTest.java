package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.File;
import at.technikum.Invicrea2WebShopbackend.repository.FileRepository;
import at.technikum.Invicrea2WebShopbackend.storage.FileStorage;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileStorage fileStorage;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    void setUp () {
        MockitoAnnotations.openMocks( this );
    }

    @Test
    public void testSave () {
        // Arrange
        File file = new File( );
        when( fileRepository.save( any( File.class ) ) ).thenReturn( file );

        // Act
        File result = fileService.save( file );

        // Assert
        assertEquals( file, result );
        verify( fileRepository ).save( file );
    }

    @Test
    public void testUpload () {
        // Arrange
        String externalId = "some-external-id";
        String fileName = "test.txt";
        String contentType = "text/plain";

        when( multipartFile.getOriginalFilename( ) ).thenReturn( fileName );
        when( multipartFile.getContentType( ) ).thenReturn( contentType );
        when( fileStorage.upload( multipartFile ) ).thenReturn( externalId );
        when( fileRepository.save( any( File.class ) ) ).thenAnswer( invocation -> invocation.getArgument( 0 ) );

        // Act
        File result = fileService.upload( multipartFile );

        // Assert
        assertEquals( fileName, result.getName( ) );
        assertEquals( contentType, result.getContentType( ) );
        assertEquals( externalId, result.getExternalId( ) );
        verify( fileStorage ).upload( multipartFile );
        verify( fileRepository ).save( any( File.class ) );
    }

    @Test
    public void testFindById_Found () {
        // Arrange
        UUID id = UUID.randomUUID( );
        File file = new File( );
        when( fileRepository.findById( id ) ).thenReturn( Optional.of( file ) );

        // Act
        File result = fileService.findById( id );

        // Assert
        assertEquals( file, result );
        verify( fileRepository ).findById( id );
    }

    @Test
    public void testFindById_NotFound () {
        // Arrange
        UUID id = UUID.randomUUID( );
        when( fileRepository.findById( id ) ).thenReturn( Optional.empty( ) );

        // Act & Assert
        assertThrows( EntityNotFoundException.class, () -> fileService.findById( id ) );
        verify( fileRepository ).findById( id );
    }

    @Test
    public void testAsResource () {
        // Arrange
        String externalId = "some-external-id";
        InputStream inputStream = new ByteArrayInputStream( new byte[0] );
        when( fileStorage.load( externalId ) ).thenReturn( inputStream );

        File file = new File( );
        file.setExternalId( externalId );

        // Act
        Resource result = fileService.asResource( file );

        // Assert
        assertTrue( result instanceof InputStreamResource );
        verify( fileStorage ).load( externalId );
    }

    @Test
    public void testGetAllFiles () {
        // Arrange
        List<File> files = List.of( new File( ), new File( ) );
        when( fileRepository.findAll( ) ).thenReturn( files );

        // Act
        List<File> result = fileService.getAllFiles( );

        // Assert
        assertEquals( 2, result.size( ) );
        verify( fileRepository ).findAll( );
    }
}