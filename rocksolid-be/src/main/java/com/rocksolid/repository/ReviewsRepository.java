package com.rocksolid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocksolid.module.Reviews;

public interface ReviewsRepository extends JpaRepository<Reviews, Long> {

}
