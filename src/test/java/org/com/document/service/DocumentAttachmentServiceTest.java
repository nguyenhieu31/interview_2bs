package org.com.document.service;

import lombok.extern.slf4j.Slf4j;
import org.com.document.dto.request.DocumentAttachmentRequest;
import org.com.document.dto.request.FileUploadRequest;
import org.com.document.dto.response.FileDataResponse;
import org.com.document.entity.Document;
import org.com.document.entity.DocumentAttachmentType;
import org.com.document.entity.DocumentType;
import org.com.document.enums.ErrorCode;
import org.com.document.exception.ApiException;
import org.com.document.http.FileServerClient;
import org.com.document.repository.DocumentAttachmentRepository;
import org.com.document.service.impl.DocumentAttachmentServiceImpl;
import org.com.document.utils.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class DocumentAttachmentServiceTest {
    @Mock
    private DocumentAttachmentRepository documentAttachmentRepository;

    @Mock
    private DocumentService documentService;

    @Mock
    private DocumentAttachmentTypeService documentAttachmentTypeService;

    @Mock
    private FileServerClient fileServerClient;

    @InjectMocks
    private DocumentAttachmentServiceImpl documentAttachmentService;

    private DocumentAttachmentRequest requestDefault;

    @BeforeEach
    void initData(){
        requestDefault = DocumentAttachmentRequest.builder()
                .documentId("90bfff00-262b-44b3-a0b5-2f89a49efa47")
                .docAttachmentTypeCode("1")
                .file(new MockMultipartFile("file",
                        "hello.txt",
                        MediaType.TEXT_PLAIN_VALUE,
                        "Hello, World!".getBytes()))
                .build();
    }

    @Test
    void testUploadDocument_failed_notSelectedFile(){
        requestDefault.setFile(new MockMultipartFile(
                "file",
                null,
                null,
                new byte[0]
        ));
        ApiException thrown = assertThrows(ApiException.class, () -> {
            documentAttachmentService.save(requestDefault);
        });

        assertEquals("File is required.", thrown.getMessage());
        verify(documentAttachmentRepository, never()).save(any());
    }

    @Test
    void testUploadDocument_failed_documentInvalid(){
        requestDefault.setDocumentId("test");

        ApiException apiExceptionFormat = new ApiException(
                ErrorCode.BAD_REQUEST.getStatusCode().value(),
                "documentId",
                "Invalid uuid format."
        );
        when(documentService.checkExist(eq("test")))
                .thenThrow(apiExceptionFormat);
        ApiException thrownFormat = assertThrows(ApiException.class, () -> {
            documentAttachmentService.save(requestDefault);
        });

        assertEquals("Invalid uuid format.", thrownFormat.getMessage());
        verify(documentAttachmentRepository, never()).save(any());

        requestDefault.setDocumentId("");
        ApiException apiExceptionEmpty = new ApiException(
                ErrorCode.BAD_REQUEST.getStatusCode().value(),
                "documentId",
                "DocumentID is required."
        );
        when(documentService.checkExist(eq("")))
                .thenThrow(apiExceptionEmpty);
        ApiException thrownEmpty = assertThrows(ApiException.class, () -> {
            documentAttachmentService.save(requestDefault);
        });

        assertEquals("DocumentID is required.", thrownEmpty.getMessage());
        verify(documentAttachmentRepository, never()).save(any());
    }

    @Test
    void testUploadDocument_failed_documentAttachTypeCodeNotFound(){
        requestDefault.setDocAttachmentTypeCode("test");
        Document document = Document.builder()
                .id(UUID.fromString("90bfff00-262b-44b3-a0b5-2f89a49efa47"))
                .build();

        ApiException apiException = new ApiException(
                ErrorCode.BAD_REQUEST.getStatusCode().value(),
                "documentAttachmentType",
                "Document attachment type not found."
        );
        when(documentService.checkExist(anyString()))
                .thenReturn(document);
        when(documentAttachmentTypeService.checkExist(eq("test")))
                .thenThrow(apiException);

        ApiException thrown = assertThrows(ApiException.class, () -> {
            documentAttachmentService.save(requestDefault);
        });

        assertEquals("Document attachment type not found.", thrown.getMessage());
        verify(documentAttachmentRepository, never()).save(any());
    }

    @Test
    void testUploadDocument_failed_fileInvalid() {
        try (MockedStatic<FileUtils> utilities = Mockito.mockStatic(FileUtils.class)) {
            IOException ioException = new IOException("Error when try convert file to base64.");
            requestDefault.setFile(new MockFailFile(
                    "file",
                    "fail.txt",
                    MediaType.TEXT_PLAIN_VALUE,
                    new byte[0],
                    ioException
            ));
            ApiException apiException = new ApiException(
                    ErrorCode.BAD_REQUEST.getStatusCode().value(),
                    "file",
                    "Error when try convert file to base64."
            );
            utilities.when(() -> FileUtils.getFileName(eq(requestDefault.getFile())))
                    .thenReturn("pass");
            utilities.when(() -> FileUtils.convertToBase64(eq(requestDefault.getFile())))
                    .thenThrow(apiException);

            ApiException thrown = assertThrows(ApiException.class, () -> documentAttachmentService.save(requestDefault));

            assertEquals("Error when try convert file to base64.", thrown.getMessage());
            verify(documentAttachmentRepository, never()).save(any());
        }
    }

    @Test
    void testUploadDocument_failed_uploadFileOnFileServerFail() {
        try(MockedStatic<FileUtils> utilities = Mockito.mockStatic(FileUtils.class)){
            Document mockDocument= Document.builder()
                    .id(UUID.fromString(requestDefault.getDocumentId()))
                    .documentType(DocumentType.builder()
                            .code("BCT0600099")
                            .build())
                    .build();
            DocumentAttachmentType mockDocumentType = DocumentAttachmentType.builder()
                    .code(requestDefault.getDocAttachmentTypeCode())
                    .name("test")
                    .build();
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Input byte array has wrong 4-byte ending unit.");
            utilities.when(() -> FileUtils.getFileName(eq(requestDefault.getFile())))
                    .thenReturn("pass");
            utilities.when(() -> FileUtils.convertToBase64(eq(requestDefault.getFile())))
                    .thenReturn("string data");
            when(documentService.checkExist(eq(requestDefault.getDocumentId())))
                    .thenReturn(mockDocument);
            when(documentAttachmentTypeService.checkExist(eq(requestDefault.getDocAttachmentTypeCode())))
                    .thenReturn(mockDocumentType);
            when(fileServerClient.uploadFile(any(FileUploadRequest.class))).thenThrow(illegalArgumentException);

            ApiException thrown = assertThrows(ApiException.class, () -> documentAttachmentService.save(requestDefault));

            assertEquals("Input byte array has wrong 4-byte ending unit.", thrown.getMessage());
            verify(documentAttachmentRepository, never()).save(any());
        }
    }

    @Test
    void testUploadDocument_success() throws Exception {
        try(MockedStatic<FileUtils> utilities = Mockito.mockStatic(FileUtils.class)){
            Document mockDocument= Document.builder()
                    .id(UUID.fromString(requestDefault.getDocumentId()))
                    .documentType(DocumentType.builder()
                            .code("BCT0600099")
                            .build())
                    .build();
            DocumentAttachmentType mockDocumentType = DocumentAttachmentType.builder()
                    .code(requestDefault.getDocAttachmentTypeCode())
                    .name("test")
                    .build();
            utilities.when(() -> FileUtils.getFileName(eq(requestDefault.getFile())))
                    .thenReturn("pass");
            utilities.when(() -> FileUtils.convertToBase64(eq(requestDefault.getFile())))
                    .thenReturn("string data");
            FileDataResponse mockFileDataResponse = FileDataResponse.builder()
                    .id(UUID.randomUUID())
                    .fileExtension("txt")
                    .filePath("/path/hello.txt")
                    .filePathWord("hello.txt")
                    .isExportToWord(false)
                    .originalFileName("hello.txt")
                    .originalFileNameWord("")
                    .type("")
                    .build();

            when(documentService.checkExist(anyString()))
                    .thenReturn(mockDocument);
            when(documentAttachmentTypeService.checkExist(anyString()))
                    .thenReturn(mockDocumentType);
            when(fileServerClient.uploadFile(any()))
                    .thenReturn(mockFileDataResponse);

            String result = documentAttachmentService.save(requestDefault);

            assertEquals("Save document attachment is successful", result);
            verify(documentAttachmentRepository, times(1)).save(any());
        }
    }

}
