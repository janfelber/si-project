package com.rocksolid.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColumnDTO {

  private Long id;
  private Long category_id;
  private String category_name;
  private String column_name;
  private String type;
  private List<ColumnChoicesDTO> choices;

}
