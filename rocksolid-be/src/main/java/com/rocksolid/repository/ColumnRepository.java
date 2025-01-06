package com.rocksolid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocksolid.module.Columns;

public interface ColumnRepository extends JpaRepository<Columns, Long> {


}
