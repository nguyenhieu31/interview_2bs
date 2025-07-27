package org.com.document.service.impl;

import lombok.RequiredArgsConstructor;
import org.com.document.entity.Province;
import org.com.document.enums.ErrorCode;
import org.com.document.exception.ApiException;
import org.com.document.repository.ProvinceRepository;
import org.com.document.service.ProvinceService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProvinceServiceImpl implements ProvinceService {
    private final ProvinceRepository provinceRepository;
    @Override
    public Province checkExist(String code) {
        Optional<Province> province = provinceRepository.findByCode(code);
        if(province.isEmpty()){
            throw new ApiException(
                    ErrorCode.BAD_REQUEST.getStatusCode().value(),
                    "province",
                    "Province not found."
            );
        }
        return province.get();
    }
}
