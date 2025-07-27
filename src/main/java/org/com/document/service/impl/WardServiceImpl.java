package org.com.document.service.impl;

import lombok.RequiredArgsConstructor;
import org.com.document.entity.Ward;
import org.com.document.enums.ErrorCode;
import org.com.document.exception.ApiException;
import org.com.document.repository.WardRepository;
import org.com.document.service.WardService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WardServiceImpl implements WardService {
    private final WardRepository wardRepository;
    @Override
    public Ward checkExist(String wardCode, String provinceCode) {
        Optional<Ward> wardExist;
        if(provinceCode == null){
            wardExist = wardRepository.findByCode(wardCode);
        }else{
            wardExist = wardRepository.findByCodeAndProvince_Code(wardCode, provinceCode);
        }
        if(wardExist.isEmpty()){
            throw new ApiException(
                    ErrorCode.BAD_REQUEST.getStatusCode().value(),
                    "ward",
                    "Ward not found."
            );
        }
        return wardExist.get();
    }
}
