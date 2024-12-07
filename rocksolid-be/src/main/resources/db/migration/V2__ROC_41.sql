ALTER TABLE rock_solid."article"
  ADD COLUMN "co_authors" VARCHAR NOT NULL;

ALTER TABLE rock_solid."article"
  ADD COLUMN "article_description" VARCHAR NOT NULL;

ALTER TABLE rock_solid."article"
  ADD COLUMN "key_words" VARCHAR NOT NULL;