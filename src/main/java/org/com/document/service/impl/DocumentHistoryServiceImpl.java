package org.com.document.service.impl;

import lombok.RequiredArgsConstructor;
import org.com.document.dto.request.DocumentHistoryRequest;
import org.com.document.entity.Document;
import org.com.document.entity.DocumentHistory;
import org.com.document.exception.ApiException;
import org.com.document.repository.DocumentHistoryRepository;
import org.com.document.service.DocumentHistoryService;
import org.com.document.service.DocumentService;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentHistoryServiceImpl implements DocumentHistoryService {
    private final DocumentService documentService;
    private final DocumentHistoryRepository documentHistoryRepository;
    @Override
    public String save(DocumentHistoryRequest request) throws Exception {
        try{
            Document document = documentService.checkExist(request.getDocumentId());
            DocumentHistory documentHistory = DocumentHistory.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .reason(request.getReason())
                    .action(request.getAction())
                    .messageContent(request.getMessageContent())
                    .document(document)
                    .build();
            documentHistoryRepository.save(documentHistory);
            return "Save document history is successful";
        }catch (ApiException e){
            throw e;
        }catch (Exception e){
            throw new Exception(e);
        }
    }
}
