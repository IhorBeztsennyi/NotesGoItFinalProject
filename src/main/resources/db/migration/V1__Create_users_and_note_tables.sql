CREATE TABLE users
(
    id        UUID NOT NULL,
    email     VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    user_role VARCHAR(255) NOT NULL,
    username  VARCHAR(255) NOT NULL,
    primary key (id)
);

CREATE TABLE note
(
    id        UUID NOT NULL,
    name     VARCHAR(255) NOT NULL,
    content  VARCHAR(255) NOT NULL,
    access_type VARCHAR(255) NOT NULL,
    user_id  UUID NOT NULL,
    primary key (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

