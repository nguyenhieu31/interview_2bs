package org.com.document.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private int pageNum;
    private int pageSize;
    private int totalPages;
    private T list;
}
