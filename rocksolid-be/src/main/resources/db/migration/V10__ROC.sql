ALTER TABLE rock_solid.article
  ADD COLUMN reviewer_id BIGINT;


ALTER TABLE rock_solid.article
  ADD FOREIGN KEY (reviewer_id) REFERENCES rock_solid.user(id) ON DELETE CASCADE;