CREATE DATABASE kbase

-- USERS
CREATE TABLE users(
    id UUID PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- PROJECTS
CREATE TABLE projects(
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(300)
);

-- DOCUMENTS
CREATE TABLE documents(
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL,
    uploaded_by UUID,
    name VARCHAR(255) NOT NULL,
    file_type VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    storage_key VARCHAR(300) NOT NULL UNIQUE,
    uploaded_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_project_documents
                      FOREIGN KEY (project_id)
                      REFERENCES projects(id)
                      ON DELETE CASCADE,

    CONSTRAINT fk_owner_documents
                      FOREIGN KEY (uploaded_by)
                      REFERENCES users(id)
                      ON DELETE SET NULL
);

-- PROJECT MEMBERS
CREATE TABLE project_members(
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL,
    member_id UUID NOT NULL,
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
                            UNIQUE (project_id, member_id),

    CONSTRAINT uq_project_member_role
                            UNIQUE (project_id)
                            WHERE role = 'OWNER'
);