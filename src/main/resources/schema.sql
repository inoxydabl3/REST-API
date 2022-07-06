DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
    id int not null auto_increment primary key,
    name varchar(255) not null,
    surname varchar(255) not null,
    photo varchar(255)
);