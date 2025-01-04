DROP TABLE rock_solid.review_column_category;
DROP TABLE rock_solid.review;


CREATE TABLE rock_solid.columns (
  id BIGSERIAL PRIMARY KEY ,
  column_name VARCHAR(255),
  category_id BIGINT,
  type VARCHAR,
 FOREIGN KEY (category_id) REFERENCES rock_solid.categories(id) ON DELETE CASCADE
);

CREATE TABLE rock_solid.reviews (
                                  id BIGSERIAL PRIMARY KEY ,
  article_id BIGINT,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rock_solid.review_details (
  id BIGSERIAL PRIMARY KEY ,
  review_id BIGINT,
  column_id BIGINT,
  value VARCHAR,
  FOREIGN KEY (column_id) REFERENCES rock_solid.columns(id) ON DELETE CASCADE,
  FOREIGN KEY (review_id) REFERENCES rock_solid.reviews(id) ON DELETE CASCADE
);

CREATE TABLE rock_solid.choices (
   id BIGSERIAL PRIMARY KEY ,
   choice_name VARCHAR
);

CREATE TABLE rock_solid.column_choices (
                                  id BIGSERIAL PRIMARY KEY ,
                                  choice_id BIGINT,
                                  column_id BIGINT,
                                  FOREIGN KEY (column_id) REFERENCES rock_solid.columns(id) ON DELETE CASCADE,
                                  FOREIGN KEY (choice_id) REFERENCES rock_solid.choices(id) ON DELETE CASCADE

);

INSERT INTO rock_solid.choices (choice_name) VALUES ('A');
INSERT INTO rock_solid.choices (choice_name) VALUES ('B');
INSERT INTO rock_solid.choices (choice_name) VALUES ('C');
INSERT INTO rock_solid.choices (choice_name) VALUES ('D');
INSERT INTO rock_solid.choices (choice_name) VALUES ('E');
INSERT INTO rock_solid.choices (choice_name) VALUES ('Fx');
INSERT INTO rock_solid.choices (choice_name) VALUES ('Yes');
INSERT INTO rock_solid.choices (choice_name) VALUES ('No');


INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Aktuálnosť a náročnosť práce', 1, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Zorientovanie sa študenta v danej problematike prostredníctvom analýzou domácej a zahraničnej literatúry', 1, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Vhodnosť zvolených metód spracovania riešenej problematiky', 1, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Rozsah a úroveň dosiahnutých výsledkov', 1, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Analýza a interpretácia výsledkov a formulácia záverov práce', 1, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Prehľadnosť a logická štruktúra práce', 2, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Formálna, jazyková a štylistická úroveň práce', 2, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Práca zodpovedá šablóne určenej pre ŠVK', 3, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Chýba názov práce v slovenskom alebo anglickom jazyku', 3, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Chýba meno autora alebo školiteľa', 3, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Chýba pracovná emailová adresa autora alebo školiteľa', 3, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Chýba abstrakt v slovenskom alebo anglickom jazyku', 3, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Abstrakt nespĺňa rozsah 100–150 slov', 3, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Chýbajú kľúčové slová v slovenskom alebo anglickom jazyku', 3, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Chýba „Úvod“, „Výsledky a diskusia“ alebo „Záver“', 3, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Nie sú uvedené zdroje a použitá literatúra', 3, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('V texte chýbajú referencie na zoznam bibliografie', 3, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('V texte chýbajú referencie na použité obrázky a/alebo tabuľky', 3, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Obrázkom a/alebo tabuľkám chýba popis', 3, 'dropdown');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Prínos (silné stránky) práce', 4, 'text');
INSERT INTO rock_solid.columns (column_name, category_id, type) VALUES ('Nedostatky (slabé stránky) práce', 4, 'text');


INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (1, 1);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (1, 2);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (1, 3);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (1, 4);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (1, 5);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (1, 6);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (2, 1);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (2, 2);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (2, 3);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (2, 4);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (2, 5);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (2, 6);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (3, 1);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (3, 2);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (3, 3);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (3, 4);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (3, 5);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (3, 6);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (4, 1);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (4, 2);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (4, 3);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (4, 4);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (4, 5);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (4, 6);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (5, 1);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (5, 2);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (5, 3);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (5, 4);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (5, 5);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (5, 6);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (6, 1);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (6, 2);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (6, 3);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (6, 4);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (6, 5);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (6, 6);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (7, 1);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (7, 2);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (7, 3);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (7, 4);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (7, 5);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (7, 6);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (8, 7);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (8, 8);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (9, 7);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (9, 8);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (10, 7);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (10, 8);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (11, 7);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (11, 8);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (12, 7);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (12, 8);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (13, 7);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (13, 8);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (14, 7);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (14, 8);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (15, 7);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (15, 8);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (16, 7);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (16, 8);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (17, 7);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (17, 8);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (18, 7);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (18, 8);

INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (19, 7);
INSERT INTO rock_solid.column_choices (column_id, choice_id) VALUES (19, 8);










-- ALTER TABLE rock_solid.review_column_category RENAME TO columns;
-- ALTER TABLE rock_solid.columns DROP COLUMN review_id;
--
-- CREATE TABLE rock_solid.columns (
--                                   id BIGSERIAL PRIMARY KEY ,
--                                   column_name VARCHAR,
--                                   category_id BIGINT,
--                                   FOREIGN KEY (category_id) REFERENCES rock_solid.categories(id) ON DELETE CASCADE
-- );
--
-- DROP TABLE rock_solid.review;
--
-- CREATE TABLE rock_solid.reviews (
--                                   id BIGSERIAL PRIMARY KEY ,
--                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );
--
-- CREATE TABLE rock_solid.review_details (
--                                          id BIGSERIAL PRIMARY KEY,
--                                          value VARCHAR
-- );