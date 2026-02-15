
-- create table for users
CREATE TABLE IF NOT EXISTS users
(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    email varchar(255) NOT NULL UNIQUE,
    password_hash varchar(255) NOT NULL,
    user_role varchar(50) NOT NULL DEFAULT 'USER'
        CHECK (user_role IN ('USER','ADMIN')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);
