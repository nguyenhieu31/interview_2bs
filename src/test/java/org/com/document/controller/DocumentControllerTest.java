package org.com.document.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.com.document.dto.request.DocumentSearchRequest;
import org.com.document.dto.request.DocumentUploadRequest;
import org.com.document.dto.response.DocumentSearchResponse;
import org.com.document.dto.response.PageResponse;
import org.com.document.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DocumentController.class)
public class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private DocumentService documentService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Validator validator;
    private DocumentUploadRequest request;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        request = DocumentUploadRequest.builder()
                .companyName("Company A")
                .companyPhoneNumber("0777888999")
                .companyFax("0236366777")
                .address("123 hoàn kiếm, hà nội")
                .companyEmail("contact5@gmail.com")
                .taxCode("1234567897")
                .nswCode("NSW00117788")
                .documentType("BCT0600098")
                .status(UUID.fromString("2a4090f1-7156-4f2e-8581-0335f0e72410"))
                .provinceCode("01")
                .wardCode("00343")
                .build();
    }

    @Test
    void testSaveDocument_failed_inputIsNull() throws Exception{
        DocumentUploadRequest requestIsNull = new DocumentUploadRequest();
        Set<ConstraintViolation<DocumentUploadRequest>> violations = validator.validate(requestIsNull);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 9);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("companyName")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("The company name isn't blank.")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("companyPhoneNumber")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("The phone number isn't blank.")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("companyFax")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("The company fax isn't blank.")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("address")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("address isn't blank.")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("companyEmail")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("Email isn't blank.")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("taxCode")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("The tax code isn't blank.")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nswCode")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("Code NSW isn't blank.")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("The status is required.")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("documentType")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("The document type isn't blank.")));
    }

    @Test
    void testSaveDocument_failed_companyPhoneNumberIsInvalid() throws Exception{
        request.setCompanyPhoneNumber("a123456782");
        Set<ConstraintViolation<DocumentUploadRequest>> violations1 = validator.validate(request);
        assertFalse(violations1.isEmpty());
        assertEquals(violations1.size(), 1);
        assertEquals("The phone number must be have 10 digits.", violations1.iterator().next().getMessage());

        request.setCompanyPhoneNumber("09765112245");
        Set<ConstraintViolation<DocumentUploadRequest>> violations2 = validator.validate(request);
        assertFalse(violations2.isEmpty());
        assertEquals(violations2.size(), 1);
        assertEquals("The phone number must be have 10 digits.", violations2.iterator().next().getMessage());
    }

    @Test
    void testSaveDocument_failed_emailInvalid() throws Exception{
        request.setCompanyEmail("hieutest");
        Set<ConstraintViolation<DocumentUploadRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
        assertEquals("Email invalid.", violations.iterator().next().getMessage());
    }

    @Test
    void testSaveDocument_failed_taxCodeInvalid() throws Exception{
        request.setTaxCode("123456789");
        Set<ConstraintViolation<DocumentUploadRequest>> violations1 = validator.validate(request);
        assertFalse(violations1.isEmpty());
        assertEquals(violations1.size(), 1);
        assertEquals("The tax code must be have 10 or 13 digits.", violations1.iterator().next().getMessage());

        request.setTaxCode("123456789a");
        Set<ConstraintViolation<DocumentUploadRequest>> violations2 = validator.validate(request);
        assertFalse(violations2.isEmpty());
        assertEquals(violations2.size(), 1);
        assertEquals("The tax code must be have 10 or 13 digits.", violations2.iterator().next().getMessage());
    }

    @Test
    void testSearchDocument_failed_inputAllNull() throws Exception{
        DocumentSearchRequest documentSearchRequest = new DocumentSearchRequest();
        Set<ConstraintViolation<DocumentSearchRequest>> violations = validator.validate(documentSearchRequest);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 5);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("taxCode")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Tax code is required.")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("companyName")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The company name is required.")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("documentType")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The document type is required.")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("provinceCode")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Province code is required.")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Status is required.")));
    }

    @Test
    void testSaveDocument_success() throws Exception {

        when(documentService.save(any(DocumentUploadRequest.class)))
                .thenReturn("Save document is successful");

        mockMvc.perform(post("/document/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Save document is successful"));

        verify(documentService, times(1)).save(any(DocumentUploadRequest.class));
    }

    @Test
    void testSearchDocument_success() throws Exception {
        DocumentSearchRequest documentSearchRequest = DocumentSearchRequest.builder()
                .documentType("")
                .companyName("company 1")
                .provinceCode("")
                .taxCode("")
                .status("")
                .build();
        List<DocumentSearchResponse> responseList = List.of(
                new DocumentSearchResponse(
                        UUID.fromString("2a4090f1-7156-4f2e-8581-0335f0e72410"),
                        "company 1",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );
        PageResponse<List<DocumentSearchResponse>> pageResponse = new PageResponse<>();
        pageResponse.setPageSize(5);
        pageResponse.setPageNum(0);
        pageResponse.setTotalPages(1);
        pageResponse.setList(responseList);
        when(documentService.search(anyInt(),anyInt(),any(DocumentSearchRequest.class)))
                .thenReturn(pageResponse);

        mockMvc.perform(post("/document/search")
                        .param("pageSize", "5")
                        .param("pageNum", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(documentSearchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pageSize").value(5))
                .andExpect(jsonPath("$.data.list[0].companyName").value("company 1"));

        verify(documentService, times(1)).search(anyInt(),anyInt(), any(DocumentSearchRequest.class));
    }

}
