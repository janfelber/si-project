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
@Table(name = "column_choices", schema = "rock_solid")
public class ColumnChoices {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "choice_id", referencedColumnName = "id" ,nullable = false)
  private Choice choice;

  @ManyToOne
  @JoinColumn(name = "column_id", referencedColumnName = "id", nullable = false)
  private Columns column;

}
