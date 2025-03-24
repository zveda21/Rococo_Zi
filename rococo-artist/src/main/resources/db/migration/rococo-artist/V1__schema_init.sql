create extension if not exists "uuid-ossp";

create table if not exists "artist"
(
    id       UUID unique  not null default uuid_generate_v1() primary key,
    name varchar(255) not null,
    biography text  not null,
    photo bytea not null
);