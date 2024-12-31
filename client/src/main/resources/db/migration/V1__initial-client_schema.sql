create schema if not exists client;

create table if not exists client.users (
    id serial primary key,
    full_name varchar(50) not null check (length(trim(full_name)) >= 3),
    phone char(11) not null unique,
    email varchar(50) not null unique,
    address varchar(100)
);
