CREATE TABLE rock_solid.sections (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

ALTER TABLE rock_solid.article DROP COLUMN section;

ALTER TABLE rock_solid.article
  ADD COLUMN section_id BIGINT, ADD CONSTRAINT fk_section_id
  FOREIGN KEY (section_id)
    REFERENCES rock_solid.sections (id);