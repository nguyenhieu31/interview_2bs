package org.com.document.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDataResponse implements Serializable {
    private UUID id;
    private String type;
    private String nswCode;
    private String filePath;
    private String originalFileName;
    private String fileExtension;
    private Boolean isExportToWord;
    private String filePathWord;
    private String originalFileNameWord;
}
