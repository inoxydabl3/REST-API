DROP TABLE IF EXISTS todos;

CREATE TABLE customers (
    id int auto_increment primary key,
    name varchar(250),
    surname varchar(250),
    photo varchar(250)
);