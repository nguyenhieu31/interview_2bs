package org.com.document.service.impl;

import lombok.RequiredArgsConstructor;
import org.com.document.entity.DocumentAttachmentType;
import org.com.document.enums.ErrorCode;
import org.com.document.exception.ApiException;
import org.com.document.repository.DocumentAttachmentTypeRepository;
import org.com.document.service.DocumentAttachmentTypeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentAttachmentTypeServiceImpl implements DocumentAttachmentTypeService {
    private final DocumentAttachmentTypeRepository repository;
    @Override
    public DocumentAttachmentType checkExist(String docAttachTypeCode) {
        Optional<DocumentAttachmentType> docExist = repository.findByCode(docAttachTypeCode);
        if(docExist.isEmpty()){
            throw new ApiException(
                    ErrorCode.BAD_REQUEST.getStatusCode().value(),
                    "documentAttachmentType",
                    "Document attachment type not found."
            );
        }
        return docExist.get();
    }
}
