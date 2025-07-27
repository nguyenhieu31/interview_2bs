package org.com.document.service;

import org.com.document.entity.Ward;

public interface WardService {
    Ward checkExist(String wardCode, String provinceCode);
}
