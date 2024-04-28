package com.ecust.base.dto;

import lombok.Data;

@Data
public class SearchBooksDTO {
    private Long currentPage;
    private Long pageSize;
    private String inputText;
    private String label;
}
