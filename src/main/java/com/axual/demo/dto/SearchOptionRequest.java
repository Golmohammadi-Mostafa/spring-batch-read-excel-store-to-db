package com.axual.demo.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class SearchOptionRequest {
    private String searchFilter;
    @Min(value = 0,message = "page must be greater than or equal to 0")
    private Integer page = 0;
    @Min(value = 1, message = "size must be greater than or equal to 1")
    @Max(value = 200, message = "size must not be greater than 200")
    private Integer size = 10;
}