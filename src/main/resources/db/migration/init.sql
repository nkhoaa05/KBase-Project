CREATE DATABASE kbase IF NOT EXIST
USE DATABASE kbase;

CREATE TABLE users(
    id UUID PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE projects(
    id UUID PRIMARY KEY,
    owner_id UUID,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(300),

    CONSTRAINT fk_project_owner
                     FOREIGN KEY (owner_id)
                     REFERENCES users(id)
);

CREATE TABLE documents(
    id UUID PRIMARY KEY,
    project_id UUID,
    owner_id UUID,
    name VARCHAR(100) NOT NULL,
    file_type VARCHAR(10) NOT NULL,
    file_size BIGINT NOT NULL,
    storage_key VARCHAR(300) NOT NULL UNIQUE,
    create_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_project_documents
                      FOREIGN KEY (project_id)
                      REFERENCES projects(id)
                      ON DELETE CASCADE,

    CONSTRAINT fk_owner_documents
                      FOREIGN KEY (owner_id)
                      REFERENCES users(id)
                      ON DELETE CASCADE
);

CREATE TABLE project_members(
    id UUID PRIMARY KEY,
    project_id UUID,
    member_id UUID,
    role VARCHAR(50) NOT NULL,

    CONSTRAINT fk_project_member_project
                            FOREIGN KEY (project_id)
                            REFERENCES projects(id)
                            ON DELETE CASCADE,

    CONSTRAINT fk_project_member_user
                            FOREIGN KEY (member_id)
                            REFERENCES users(id)
                            ON DELETE CASCADE,

    CONSTRAINT uq_project_member
                            UNIQUE (project_id, member_id)
);