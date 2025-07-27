package org.com.document.service;

import org.com.document.entity.DocumentAttachmentType;

public interface DocumentAttachmentTypeService {
    DocumentAttachmentType checkExist(String docAttachTypeCode);
}
