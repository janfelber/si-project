CREATE TABLE rock_solid.review (
  id BIGSERIAL PRIMARY KEY ,
  work_relevance_difficulty VARCHAR,
  student_orientation_literature_analysis VARCHAR,
  method_appropriateness VARCHAR,
  results_scope_level VARCHAR,
  results_analysis_conclusions VARCHAR,
  work_clarity_structure VARCHAR,
  language_formality_level VARCHAR,
  compliance_svk_template VARCHAR,
  missing_title VARCHAR,
  missing_author_or_supervisor VARCHAR,
  missing_email_author_or_supervisor VARCHAR,
  missing_abstract VARCHAR,
  abstract_word_count VARCHAR,
  missing_keywords VARCHAR,
  missing_sections_intro_results_conclusions VARCHAR,
  missing_sources_literature VARCHAR,
  missing_references_bibliography VARCHAR,
  missing_references_images_tables VARCHAR,
  missing_image_table_descriptions VARCHAR,
  work_contribution_strong_points VARCHAR,
  work_contribution_weak_points VARCHAR,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rock_solid.categories (
  id BIGSERIAL PRIMARY KEY ,
  category_name VARCHAR(255) NOT NULL
);

CREATE TABLE rock_solid.review_column_category (
  review_id BIGINT,
  column_name VARCHAR(255),
  category_id BIGINT,
  PRIMARY KEY (review_id, column_name),
  FOREIGN KEY (review_id) REFERENCES rock_solid.review(id) ON DELETE CASCADE,
  FOREIGN KEY (category_id) REFERENCES rock_solid.categories(id) ON DELETE CASCADE
);

INSERT INTO rock_solid.categories (category_name) VALUES ('Obsah práce');
INSERT INTO rock_solid.categories (category_name) VALUES ('Štruktúra práce');
INSERT INTO rock_solid.categories (category_name) VALUES ('Dodržiavanie pravidiel');
INSERT INTO rock_solid.categories (category_name) VALUES ('Hodnotenie');




