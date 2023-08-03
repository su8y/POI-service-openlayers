package com.example.core.repository;

import com.example.core.model.Category;
import com.example.core.model.common.Space;
import lombok.Data;

@Data
public class SearchParam {
    private Space space;
    private Category selectedValue;
    private String inputText;
}
