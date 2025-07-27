package org.com.document.service;

import org.com.document.entity.Status;

import java.util.UUID;

public interface StatusService {
    Status checkExist(UUID statusId);
}
