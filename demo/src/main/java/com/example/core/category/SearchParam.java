package com.example.core.category;

import com.example.core.util.Space;
import lombok.Data;

@Data
public class SearchParam {
    private Space space;
    private Category selectedValue;
    private String inputText;
}
