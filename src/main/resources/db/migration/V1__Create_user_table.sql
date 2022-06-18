CREATE TABLE users
(
    id        UUID NOT NULL,
    email     VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    user_role VARCHAR(255) NOT NULL,
    username  VARCHAR(255) NOT NULL,
    primary key (id)
);