package com.rocksolid.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConferenceRequestDto {
    private Long article_id;
    private String date_from;
    private String date_to;
    private Long form_id;
    private int year;
    private String name;
}
