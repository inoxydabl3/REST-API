insert into roles (id, role) values
    (0, 'ADMIN'),
    (1, 'USER')
;

insert into users (username, password, id_role) values
    ('admin', '$2a$10$rKRGKXVF3T0icaJIkDes9.ovFDuLjPfic/KMmvXSK/1MdwxTQyuuW', 0),   -- (admin, password)
    ('user', '$2a$10$rKRGKXVF3T0icaJIkDes9.ovFDuLjPfic/KMmvXSK/1MdwxTQyuuW', 1)     -- (user, password)
;

insert into customers (name, surname, id_user) values
    ('Kassie','Tonn', 1),
    ('Efram','Finn', 2),
    ('Nereyda','Phinney', 1),
    ('Carrol','Mckinsey', 1),
    ('Liba','Conant', 1),
    ('Marlicia','Eggers', 1),
    ('Allyson','Scalise', 1),
    ('Blythe','Sweatman', 2),
    ('Hilario','Hove', 2),
    ('Clayton','Just', 2),
    ('Chery','Canady', 1),
    ('Raman','Grimsley', 1),
    ('Norman','Hafer', 1),
    ('Rebekan','Brigman', 2),
    ('Fidencio','Wenzel', 1),
    ('Nicolette','Chasse', 1),
    ('Kristine','Banner', 2),
    ('Tauheedah','Stutz', 1),
    ('Katiria','Bent', 1),
    ('Shonteria','Mcafee', 2),
    ('Charlana','Rester', 1),
    ('Annabelle','Laclair', 2),
    ('Graig','Reider', 1),
    ('Phylicia','Lash', 1),
    ('Katira','Spraggins', 1)
;