package org.com.document.service.impl;

import lombok.RequiredArgsConstructor;
import org.com.document.dto.request.DocumentSearchRequest;
import org.com.document.dto.request.DocumentUploadRequest;
import org.com.document.dto.response.DocumentSearchResponse;
import org.com.document.dto.response.PageResponse;
import org.com.document.entity.*;
import org.com.document.enums.ErrorCode;
import org.com.document.exception.ApiException;
import org.com.document.repository.DocumentRepository;
import org.com.document.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentTypeService documentTypeService;
    private final StatusService statusService;
    private final ProvinceService provinceService;
    private final WardService wardService;
    @Override
    public String save(DocumentUploadRequest request) throws Exception {
        try{
            DocumentType documentType = documentTypeService.checkExist(request.getDocumentType());
            Status status = statusService.checkExist(request.getStatus());
            Province province = null;
            if(request.getProvinceCode() != null){
                province = provinceService.checkExist(request.getProvinceCode());
            }
            Ward ward = null;
            if(request.getWardCode() != null){
                ward = wardService.checkExist(request.getWardCode(), request.getProvinceCode());
            }
            LocalDateTime now = LocalDateTime.now();
            Document document = Document.builder()
                    .province(province)
                    .ward(ward)
                    .documentType(documentType)
                    .status(status)
                    .companyName(request.getCompanyName().trim())
                    .companyPhoneNumber(request.getCompanyPhoneNumber().trim())
                    .address(request.getAddress().trim())
                    .companyFax(request.getCompanyFax().trim())
                    .companyEmail(request.getCompanyEmail())
                    .taxCode(request.getTaxCode())
                    .nswCode(request.getNswCode())
                    .receiveTime(now)
                    .lastModifiedTime(now)
                    .build();
            documentRepository.save(document);
            return "Save document is successful";
        }catch (ApiException e){
            throw e;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Document checkExist(String documentId) {
        UUID documentUUID = null;
        if(!documentId.isEmpty()){
            try{
                documentUUID = UUID.fromString(documentId);
            }catch (IllegalArgumentException e){
                throw new ApiException(
                        ErrorCode.BAD_REQUEST.getStatusCode().value(),
                        "documentId",
                        "Invalid uuid format."
                );
            }
        }else{
            throw new ApiException(
                    ErrorCode.BAD_REQUEST.getStatusCode().value(),
                    "documentId",
                    "DocumentID is required."
            );
        }
        Optional<Document> documentExist = documentRepository.findById(documentUUID);
        if(documentExist.isEmpty()){
            throw new ApiException(
                    ErrorCode.BAD_REQUEST.getStatusCode().value(),
                    "documentId",
                    "Document not found."
            );
        }
        return documentExist.get();
    }

    @Override
    public PageResponse<List<DocumentSearchResponse>> search(int pageNum, int pageSize, DocumentSearchRequest request) throws Exception {
        try{
            UUID statusUUID = null;
            if (StringUtils.hasText(request.getStatus())) {
                try {
                    statusUUID = UUID.fromString(request.getStatus());
                } catch (IllegalArgumentException e) {
                    throw new ApiException(400, "status", "Invalid UUID format.");
                }
            }
            Pageable pageable = PageRequest.of(pageNum,pageSize, Sort.by(Sort.Direction.ASC,"receiveTime"));
            Page<Document> documents = documentRepository.searchDocuments(
                    request.getTaxCode(),
                    request.getCompanyName().toLowerCase(),
                    request.getProvinceCode(),
                    request.getDocumentType(),
                    statusUUID,
                    pageable
            );
            PageResponse<List<DocumentSearchResponse>> pageResponse = new PageResponse<>();
            List<DocumentSearchResponse> documentSearchResponses = new ArrayList<>();
            documents.forEach(document -> {
                DocumentSearchResponse response = DocumentSearchResponse.builder()
                        .id(document.getId())
                        .companyName(document.getCompanyName())
                        .companyPhoneNumber(document.getCompanyPhoneNumber())
                        .companyFax(document.getCompanyFax())
                        .address(document.getAddress())
                        .companyEmail(document.getCompanyEmail())
                        .taxCode(document.getTaxCode())
                        .nswCode(document.getNswCode())
                        .receiveTime(document.getReceiveTime())
                        .lastModifiedTime(document.getLastModifiedTime())
                        .documentTypeName(document.getDocumentType().getName())
                        .statusName(document.getStatus().getName())
                        .provinceName(document.getProvince() != null ? document.getProvince().getName() : null)
                        .wardName(document.getWard() != null ? document.getWard().getName() : null)
                        .build();
                documentSearchResponses.add(response);
            });
            pageResponse.setPageNum(documents.getNumber());
            pageResponse.setPageSize(documents.getSize());
            pageResponse.setTotalPages(documents.getTotalPages());
            pageResponse.setList(documentSearchResponses);
            return pageResponse;
        }catch (ApiException e){
            throw e;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
