DROP TABLE IF EXISTS roles, users, customers;

CREATE TABLE roles (
    id int not null,
    role varchar(10) not null,
    primary key(id)
);

CREATE TABLE users (
    id int not null auto_increment,
    username varchar(50) not null,
    password varchar(60) not null,
    id_role int,
    primary key(id),
    foreign key(id_role) references roles(id) on delete set null on update cascade
);

CREATE TABLE customers (
    id int not null auto_increment,
    name varchar(255) not null,
    surname varchar(255) not null,
    photo varchar(255),
    id_user int,
    primary key(id),
    foreign key(id_user) references users(id) on delete set null on update cascade
);