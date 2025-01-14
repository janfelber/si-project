ALTER TABLE rock_solid.article ADD COLUMN pdf_file_path VARCHAR;

ALTER TABLE rock_solid.article DROP COLUMN file_path;

ALTER TABLE rock_solid.article ADD COLUMN word_file_path VARCHAR;