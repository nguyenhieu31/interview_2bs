package org.com.document.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorItem {
    private String errorKey;
    private String errorMsg;
}
