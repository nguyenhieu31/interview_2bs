package org.com.document.service.impl;

import lombok.RequiredArgsConstructor;
import org.com.document.constant.AppConstant;
import org.com.document.dto.request.DocumentAttachmentRequest;
import org.com.document.dto.request.FileUploadRequest;
import org.com.document.dto.response.FileDataResponse;
import org.com.document.entity.Document;
import org.com.document.entity.DocumentAttachment;
import org.com.document.entity.DocumentAttachmentType;
import org.com.document.enums.ErrorCode;
import org.com.document.exception.ApiException;
import org.com.document.http.FileServerClient;
import org.com.document.repository.DocumentAttachmentRepository;
import org.com.document.service.DocumentAttachmentService;
import org.com.document.service.DocumentAttachmentTypeService;
import org.com.document.service.DocumentService;
import org.com.document.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentAttachmentServiceImpl implements DocumentAttachmentService {
    private final DocumentAttachmentRepository documentAttachmentRepository;
    private final DocumentService documentService;
    private final DocumentAttachmentTypeService documentAttachmentTypeService;
    private final FileServerClient fileServerClient;
    @Value("${base-url}")
    private String baseUrl;
    @Override
    public String save(DocumentAttachmentRequest request) throws Exception {
        try{
            String fileName = FileUtils.getFileName(request.getFile());
            if(fileName.isEmpty()){
                throw new ApiException(
                        ErrorCode.BAD_REQUEST.getStatusCode().value(),
                        "file",
                        "File is required."
                );
            }
            String base64Data = FileUtils.convertToBase64(request.getFile());
            Document document = documentService.checkExist(request.getDocumentId());
            DocumentAttachmentType documentAttachmentType = documentAttachmentTypeService.checkExist(request.getDocAttachmentTypeCode());
            FileUploadRequest fileUploadRequest = FileUploadRequest.builder()
                    .nswCode(document.getNswCode())
                    .isExportToWord(false)
                    .fileName(fileName)
                    .documentType(document.getDocumentType().getCode())
                    .base64Data(base64Data)
                    .build();
            FileDataResponse fileDataResponse = fileServerClient.uploadFile(fileUploadRequest);
            if(fileDataResponse == null){
                throw new ApiException(
                        ErrorCode.BAD_REQUEST.getStatusCode().value(),
                        "file",
                        "Can't upload document to file server!!!"
                );
            }
            String url = baseUrl + AppConstant.GET_FILE_API + fileDataResponse.getId();
            DocumentAttachment documentAttachment = DocumentAttachment.builder()
                    .documentAttachmentType(documentAttachmentType)
                    .document(document)
                    .fileName(fileName)
                    .link(url)
                    .build();
            documentAttachmentRepository.save(documentAttachment);
            return "Save document attachment is successful";
        }catch (ApiException e){
            throw e;
        }catch (Exception e){
            throw new Exception(e);
        }
    }
}
