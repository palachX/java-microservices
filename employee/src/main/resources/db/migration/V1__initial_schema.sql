create schema if not exists employee;

create table if not exists employee.users (
    id serial primary key,
    full_name varchar(50) not null check (length(trim(full_name)) >= 3),
    phone char(11) not null unique
);
