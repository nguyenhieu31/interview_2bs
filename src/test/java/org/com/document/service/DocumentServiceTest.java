package org.com.document.service;

import lombok.extern.slf4j.Slf4j;
import org.com.document.dto.request.DocumentSearchRequest;
import org.com.document.dto.request.DocumentUploadRequest;
import org.com.document.dto.response.DocumentSearchResponse;
import org.com.document.dto.response.PageResponse;
import org.com.document.entity.*;
import org.com.document.exception.ApiException;
import org.com.document.repository.DocumentRepository;
import org.com.document.service.impl.DocumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class DocumentServiceTest {
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private DocumentTypeService documentTypeService;
    @Mock
    private StatusService statusService;
    @Mock
    private ProvinceService provinceService;
    @Mock
    private WardService wardService;
    @InjectMocks
    private DocumentServiceImpl documentService;
    private DocumentUploadRequest request;
    private List<Document> mockDocuments;

    @BeforeEach
    void setup() {
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
        mockDocuments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Document doc = new Document();
            doc.setId(UUID.randomUUID());
            doc.setCompanyName("Company " + i);
            doc.setCompanyPhoneNumber("123456789");
            doc.setCompanyFax("Fax");
            doc.setAddress("Address");
            doc.setCompanyEmail("email@company.com");
            doc.setTaxCode("TaxCode");
            doc.setNswCode("NSW");
            doc.setReceiveTime(LocalDateTime.now());
            doc.setLastModifiedTime(LocalDateTime.now());

            DocumentType docType = new DocumentType();
            docType.setCode("BCT0600099");
            docType.setName("Cấp Giấy Phép Xuất Khẩu Tiền Chất Sử Dụng Trong Lĩnh Vực Công Nghiệp");
            doc.setDocumentType(docType);

            Status status = new Status();
            status.setId(UUID.fromString("0ba950dd-6a0c-429b-8dae-811c71f94f52"));
            status.setName("Từ Chối");
            doc.setStatus(status);

            Province province = new Province();
            province.setCode("01");
            province.setName("Hà Nội");
            doc.setProvince(province);

            Ward ward = new Ward();
            ward.setCode("10114");
            ward.setName("Thị trấn Kim Bài");
            ward.setProvince(province);
            doc.setWard(ward);

            mockDocuments.add(doc);
        }
    }

    @Test
    void testSaveDocument_failed_documentTypeNotFound() throws Exception{
        request.setDocumentType("invalid");
        ApiException apiException = new ApiException(400, "documentType", "Document type not found.");
        when(documentTypeService.checkExist(request.getDocumentType())).thenThrow(apiException);

        ApiException thrown = assertThrows(ApiException.class, () -> {
            documentService.save(request);
        });

        assertEquals("Document type not found.", thrown.getMessage());
        verify(documentRepository, never()).save(any());
    }

    @Test
    void testSaveDocument_failed_statusNotFound() throws Exception{
        request.setStatus(UUID.fromString("2a4090f1-7156-4f2e-8581-0335f0e72419"));
        ApiException apiException = new ApiException(400, "status", "Status not found.");
        when(statusService.checkExist(request.getStatus())).thenThrow(apiException);

        ApiException thrown = assertThrows(ApiException.class, () -> {
            documentService.save(request);
        });

        assertEquals("Status not found.", thrown.getMessage());
        verify(documentRepository, never()).save(any());
    }

    @Test
    void testSaveDocument_failed_provinceNotFound() throws Exception{
        request.setProvinceCode("113");
        ApiException apiException = new ApiException(400, "province", "Province not found.");
        when(provinceService.checkExist(request.getProvinceCode())).thenThrow(apiException);

        ApiException thrown = assertThrows(ApiException.class, () -> {
            documentService.save(request);
        });

        assertEquals("Province not found.", thrown.getMessage());
        verify(documentRepository, never()).save(any());
    }

    @Test
    void testSaveDocument_failed_wardNotFound() throws Exception{
        request.setWardCode("113");
        request.setProvinceCode(null);
        ApiException apiException = new ApiException(400, "ward", "Ward not found.");
        when(wardService.checkExist(request.getWardCode(),request.getProvinceCode())).thenThrow(apiException);
        ApiException thrown = assertThrows(ApiException.class, () -> {
            documentService.save(request);
        });
        assertEquals("Ward not found.", thrown.getMessage());
        verify(documentRepository, never()).save(any());

        request.setProvinceCode("01");
        ApiException apiException2 = new ApiException(400, "ward", "Ward not found.");
        when(wardService.checkExist(request.getWardCode(),request.getProvinceCode())).thenThrow(apiException2);
        ApiException thrown2 = assertThrows(ApiException.class, () -> {
            documentService.save(request);
        });
        assertEquals("Ward not found.", thrown2.getMessage());
        verify(documentRepository, never()).save(any());
    }

    @Test
    void testSaveDocument_success_wardAndProvinceIsNull() throws Exception{
        request.setProvinceCode(null);
        request.setWardCode(null);
        DocumentType mockDocumentType = new DocumentType();
        Status mockStatus = new Status();
        when(documentTypeService.checkExist(request.getDocumentType())).thenReturn(mockDocumentType);
        when(statusService.checkExist(request.getStatus())).thenReturn(mockStatus);

        when(documentRepository.save(any(Document.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String result = documentService.save(request);

        assertEquals("Save document is successful", result);
        verify(documentRepository, times(1)).save(any(Document.class));
    }

    @Test
    void testSaveDocument_success() throws Exception{
        DocumentType mockDocumentType = new DocumentType();
        Status mockStatus = new Status();
        Province mockProvince = new Province();
        Ward mockWard = new Ward();
        when(documentTypeService.checkExist(request.getDocumentType())).thenReturn(mockDocumentType);
        when(statusService.checkExist(request.getStatus())).thenReturn(mockStatus);
        when(provinceService.checkExist(request.getProvinceCode())).thenReturn(mockProvince);
        when(wardService.checkExist(request.getWardCode(), request.getProvinceCode())).thenReturn(mockWard);

        when(documentRepository.save(any(Document.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String result = documentService.save(request);

        assertEquals("Save document is successful", result);
        verify(documentRepository, times(1)).save(any(Document.class));
    }

    @Test
    void testSearchDocument_failed_statusInvalid() throws Exception{
        DocumentSearchRequest documentSearchRequest = DocumentSearchRequest.builder()
                .documentType("")
                .companyName("")
                .provinceCode("")
                .taxCode("")
                .status("1")
                .build();
        ApiException thrown = assertThrows(ApiException.class, ()->{
            documentService.search(0,5,documentSearchRequest);
        });
        assertEquals("Invalid UUID format.", thrown.getMessage());
        verify(documentRepository, never()).searchDocuments(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(Pageable.class)
        );
    }

    @Test
    void testSearchDocument_success_inputAllBlank() throws Exception{
        DocumentSearchRequest documentSearchRequest = DocumentSearchRequest.builder()
                .documentType("")
                .companyName("")
                .provinceCode("")
                .taxCode("")
                .status("")
                .build();
        Page<Document> mockPage = new PageImpl<>(mockDocuments, PageRequest.of(0, 5), 5);
        when(documentRepository.searchDocuments(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(Pageable.class)
        )).thenReturn(mockPage);

        PageResponse<List<DocumentSearchResponse>> result = documentService.search(0, 5, documentSearchRequest);

        assertEquals(5, result.getList().size());
        assertEquals(5, result.getPageSize());
        assertEquals(0, result.getPageNum());
        verify(documentRepository, times(1)).searchDocuments(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                any(),
                any(Pageable.class)
        );
    }

    @Test
    void testSearchDocument_success_statusValid() throws Exception{
        DocumentSearchRequest documentSearchRequest = DocumentSearchRequest.builder()
                .documentType("")
                .companyName("")
                .provinceCode("")
                .taxCode("")
                .status("0ba950dd-6a0c-429b-8dae-811c71f94f52")
                .build();
        Page<Document> mockPage = new PageImpl<>(mockDocuments, PageRequest.of(0, 5), 5);
        when(documentRepository.searchDocuments(
                eq(""),
                eq(""),
                eq(""),
                eq(""),
                eq(UUID.fromString(documentSearchRequest.getStatus())),
                any(Pageable.class)
        )).thenReturn(mockPage);

        PageResponse<List<DocumentSearchResponse>> result = documentService.search(0, 5, documentSearchRequest);

        assertEquals(5, result.getList().size());
        assertEquals(5, result.getPageSize());
        assertEquals(0, result.getPageNum());
        verify(documentRepository, times(1)).searchDocuments(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(Pageable.class)
        );
    }

    @Test
    void testSearchDocument_success_companyNameValueMatch() throws Exception {
        String companyName = "Company 1";
        DocumentSearchRequest documentSearchRequest = DocumentSearchRequest.builder()
                .documentType("")
                .companyName(companyName.toLowerCase())
                .provinceCode("")
                .taxCode("")
                .status("")
                .build();

        List<Document> filteredDocument = mockDocuments.stream()
                .filter(doc -> doc.getCompanyName().toLowerCase().contains(companyName.toLowerCase()))
                .toList();
        Page<Document> page = new PageImpl<>(filteredDocument);

        when(documentRepository.searchDocuments(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(page);

        PageResponse<List<DocumentSearchResponse>> result = documentService
                .search(0, 5, documentSearchRequest);

        assertEquals(1, result.getList().size());
        assertEquals("Company 1", result.getList().get(0).getCompanyName());

        verify(documentRepository, times(1)).searchDocuments(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                isNull(),
                any(Pageable.class)
        );
    }

    @Test
    void testSearchDocument_success_companyNameValueNotMatch() throws Exception {
        String companyName = "company 12";
        DocumentSearchRequest documentSearchRequest = DocumentSearchRequest.builder()
                .documentType("")
                .companyName(companyName)
                .provinceCode("")
                .taxCode("")
                .status("")
                .build();
        List<Document> filteredDocument = mockDocuments.stream()
                .filter(doc -> doc.getCompanyName().toLowerCase().contains(companyName.toLowerCase()))
                .toList();
        Page<Document> page = new PageImpl<>(filteredDocument);

        when(documentRepository.searchDocuments(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(page);

        PageResponse<List<DocumentSearchResponse>> result = documentService.search(0, 5, documentSearchRequest);

        assertEquals(0, result.getList().size());

        verify(documentRepository, times(1)).searchDocuments(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                isNull(),
                any(Pageable.class)
        );
    }

}
