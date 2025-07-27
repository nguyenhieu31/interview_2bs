package org.com.document.service;

import org.com.document.entity.DocumentType;

public interface DocumentTypeService {
    DocumentType checkExist(String code);
}
