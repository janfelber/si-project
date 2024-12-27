ALTER TABLE ONLY rock_solid.article
  ADD COLUMN user_id BIGINT
  REFERENCES rock_solid."user"(id);