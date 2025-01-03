CREATE TABLE rock_solid.notification (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT,
  is_read BOOLEAN,
  message VARCHAR,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES rock_solid."user"(id) ON DELETE CASCADE
  )