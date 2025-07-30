package org.com.document.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.com.document.dto.request.DocumentAttachmentRequest;
import org.com.document.service.DocumentAttachmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DocumentAttachmentController.class)
public class DocumentAttachmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private DocumentAttachmentService documentAttachmentService;
    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testUploadDocument_failed_inputAllNull(){
        DocumentAttachmentRequest requestIsNull = new DocumentAttachmentRequest();
        Set<ConstraintViolation<DocumentAttachmentRequest>> violations = validator.validate(requestIsNull);
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
        assertTrue(violations.stream().anyMatch(v-> v.getPropertyPath().toString().equals("documentId")));
        assertTrue(violations.stream().anyMatch(v-> v.getMessage().equals("Document ID isn't blank")));
        assertTrue(violations.stream().anyMatch(v-> v.getPropertyPath().toString().equals("docAttachmentTypeCode")));
        assertTrue(violations.stream().anyMatch(v-> v.getMessage().equals("Doc attachment type code isn't blank.")));
        assertTrue(violations.stream().anyMatch(v-> v.getPropertyPath().toString().equals("file")));
        assertTrue(violations.stream().anyMatch(v-> v.getMessage().equals("File is required.")));
    }

    @Test
    void testUploadDocument_success() throws Exception {
        DocumentAttachmentRequest request = DocumentAttachmentRequest.builder()
                .documentId("90bfff00-262b-44b3-a0b5-2f89a49efa47")
                .docAttachmentTypeCode("7e039ca9-ad0a-466a-a5a1-2b914d022ad9")
                .file(new MockMultipartFile("file",
                        "hello.txt",
                        MediaType.TEXT_PLAIN_VALUE,
                        "Hello, World!".getBytes()))
                .build();
        when(documentAttachmentService.save(any(DocumentAttachmentRequest.class)))
                .thenReturn("Save document attachment is successful");

        mockMvc.perform(multipart("/document-attachment/upload")
                        .file((MockMultipartFile) request.getFile())
                        .param("documentId", request.getDocumentId())
                        .param("docAttachmentTypeCode", request.getDocAttachmentTypeCode())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Save document attachment is successful"));

        verify(documentAttachmentService, times(1)).save(any(DocumentAttachmentRequest.class));
    }

    @Test
    void testDownloadDocument_success() throws Exception {
        when(documentAttachmentService.download(anyString()))
                .thenReturn("url link");

        mockMvc.perform(get("/document-attachment/download")
                .param("docAttachmentId", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("url link"));
        verify(documentAttachmentService, times(1)).download(anyString());
    }

}
