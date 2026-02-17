
ALTER TABLE posts RENAME COLUMN category TO category_id;

ALTER TABLE posts ALTER COLUMN category_id TYPE BIGINT
USING category_id::bigint;;

ALTER TABLE posts
ADD CONSTRAINT fk_post_category
FOREIGN KEY (category_id)
REFERENCES categories(id);
