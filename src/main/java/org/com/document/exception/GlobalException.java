package org.com.document.exception;

import lombok.extern.slf4j.Slf4j;
import org.com.document.dto.response.ErrorItem;
import org.com.document.dto.response.ErrorResponse;
import org.com.document.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalException {
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException exception){
        ErrorItem errorItem = null;
        if(exception.getKey() != null){
            errorItem = ErrorItem.builder()
                    .errorKey(exception.getKey())
                    .errorMsg(exception.getMessage())
                    .build();
        }
        ErrorResponse errorResponse = new ErrorResponse(exception.getCode(),"Having error in program.", errorItem != null ? List.of(errorItem) : null);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getCode()));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleApiException(IllegalArgumentException exception){
        ErrorItem errorItem = ErrorItem.builder()
                .errorKey("unknown")
                .errorMsg(exception.getMessage())
                .build();
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BAD_REQUEST.getStatusCode().value(),"Having error in program.", errorItem != null ? List.of(errorItem) : null);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ErrorItem> errorItems = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->{
            ErrorItem errorItem = ErrorItem.builder()
                    .errorKey(error.getField())
                    .errorMsg(error.getDefaultMessage())
                    .build();
            errorItems.add(errorItem);
        });
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.BAD_REQUEST.getStatusCode().value())
                .message("invalid input")
                .errorItems(errorItems)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getCode()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleUnWantedException(Exception e){
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode().value(),
                "Oops!! something went wrong.",
                null
                );
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getCode()));
    }
}
