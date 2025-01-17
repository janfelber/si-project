package com.rocksolid.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocksolid.dto.ColumnChoicesDTO;
import com.rocksolid.dto.ColumnDTO;
import com.rocksolid.module.Columns;
import com.rocksolid.service.ColumnService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/columns")
public class ColumnController {

  private ColumnService columnService;

  @GetMapping("/getcolumns")
  @PreAuthorize("hasAuthority('student:read') or hasAuthority('reviewer:read') or hasAuthority('admin:read')")
  public List<ColumnDTO> getAllColumns() {
    return columnService.getAllColumns().stream()
        .map(column -> new ColumnDTO(
            column.getId(),
            column.getCategory().getId(),
            column.getCategory().getCategory_name(),
            column.getColumn_name(),
            column.getType(),
            column.getChoices().stream()
                .map(choice -> new ColumnChoicesDTO(choice.getChoice().getId(), choice.getChoice().getChoice_name()))
                .collect(Collectors.toList())
        ))
        .collect(Collectors.toList());
  }

}
