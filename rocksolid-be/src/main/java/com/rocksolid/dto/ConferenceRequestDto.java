package com.rocksolid.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConferenceRequestDto {
    private String date_from;
    private String date_to;
    private int year;
    private String name;
    private String description;
}
