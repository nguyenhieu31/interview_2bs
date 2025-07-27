package org.com.document.service.impl;

import lombok.RequiredArgsConstructor;
import org.com.document.entity.DocumentType;
import org.com.document.enums.ErrorCode;
import org.com.document.exception.ApiException;
import org.com.document.repository.DocumentTypeRepository;
import org.com.document.service.DocumentTypeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentTypeServiceImpl implements DocumentTypeService {
    private final DocumentTypeRepository documentTypeRepository;
    @Override
    public DocumentType checkExist(String code) {
        Optional<DocumentType> checkExist = documentTypeRepository.findByCode(code);
        if(checkExist.isEmpty()){
            throw new ApiException(
                    ErrorCode.BAD_REQUEST.getStatusCode().value(),
                    "documentType",
                    "Document type not found."
            );
        }
        return checkExist.get();
    }
}
