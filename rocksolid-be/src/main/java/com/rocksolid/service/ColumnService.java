package com.rocksolid.service;

import java.util.List;

import com.rocksolid.module.Choice;
import com.rocksolid.module.Columns;

import jakarta.persistence.Column;

public interface ColumnService {
  List<Columns> getAllColumns();

}
