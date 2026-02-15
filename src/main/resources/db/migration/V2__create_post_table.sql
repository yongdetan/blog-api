
-- create table for post
CREATE TABLE posts
(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title varchar(255) NOT NULL,
    content TEXT NOT NULL,
    status varchar(50) NOT NULL DEFAULT 'DRAFT'
        CHECK(status IN ('DRAFT','PUBLIC','PRIVATE')),
    author_id BIGINT NOT NULL,
    category varchar(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT fk_post_author FOREIGN KEY (author_id) REFERENCES
    users(id) ON DELETE CASCADE
);

-- create table for post_tags
CREATE TABLE post_tags
(
    tag varchar(255),
    post_id BIGINT NOT NULL,

    CONSTRAINT fk_tag_post FOREIGN KEY (post_id) REFERENCES
    posts(id) ON DELETE CASCADE
);