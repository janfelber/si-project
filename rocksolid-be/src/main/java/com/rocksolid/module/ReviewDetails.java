package com.rocksolid.module;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review_details", schema = "rock_solid")
public class ReviewDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "\"review\"")
  private Reviews review;

  @ManyToOne
  @JoinColumn(name = "\"column_id\"")
  private Columns column_id;

  @ManyToOne
  @JoinColumn(name = "\"value\"")
  private Choice value;

  private String text_value;

}
