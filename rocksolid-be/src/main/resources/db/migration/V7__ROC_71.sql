ALTER TABLE rock_solid.conference DROP COLUMN form_id;
ALTER TABLE rock_solid.conference DROP COLUMN article_id;

ALTER TABLE rock_solid.article ADD COLUMN conference_id BIGINT;
ALTER TABLE rock_solid.article ADD FOREIGN KEY (conference_id) REFERENCES rock_solid.conference(id) ON DELETE CASCADE;

CREATE TABLE rock_solid.user_conference (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT,
  conference_id BIGINT,
  FOREIGN KEY (user_id) REFERENCES rock_solid."user"(id) ON DELETE CASCADE,
  FOREIGN KEY (conference_id) REFERENCES rock_solid.conference(id) ON DELETE CASCADE
);