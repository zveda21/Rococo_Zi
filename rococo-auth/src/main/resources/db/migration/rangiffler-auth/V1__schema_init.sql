create extension if not exists "uuid-ossp";

create table if not exists "user"
(
    id                      UUID unique        not null default uuid_generate_v1(),
    username                varchar(50) unique not null,
    password                varchar(255)       not null,
    enabled                 boolean            not null,
    account_non_expired     boolean            not null,
    account_non_locked      boolean            not null,
    credentials_non_expired boolean            not null,
    primary key (id, username)
);

CREATE TABLE IF NOT EXISTS "authorities" (
    id        UUID NOT NULL DEFAULT uuid_generate_v1(),
    user_id   UUID NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT pk_authorities PRIMARY KEY (id),
    CONSTRAINT fk_authorities_users FOREIGN KEY (user_id) REFERENCES "user"(id)
);
