package com.rocksolid.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rocksolid.module.Columns;
import com.rocksolid.repository.ColumnRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ColumnServiceImpl implements ColumnService {

  private final ColumnRepository columnRepository;

  @Override
  public List<Columns> getAllColumns() {
    return columnRepository.findAll();
  }

}
