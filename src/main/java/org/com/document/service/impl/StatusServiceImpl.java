package org.com.document.service.impl;

import lombok.RequiredArgsConstructor;
import org.com.document.entity.Status;
import org.com.document.enums.ErrorCode;
import org.com.document.exception.ApiException;
import org.com.document.repository.StatusRepository;
import org.com.document.service.StatusService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;
    @Override
    public Status checkExist(UUID statusId) {
        Optional<Status> statusExist = statusRepository.findById(statusId);
        if(statusExist.isEmpty()){
            throw new ApiException(
                    ErrorCode.BAD_REQUEST.getStatusCode().value(),
                    "status",
                    "Status not found."
            );
        }
        return statusExist.get();
    }
}
