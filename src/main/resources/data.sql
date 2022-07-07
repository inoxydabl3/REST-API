insert into roles (id, role) values (0, 'ADMIN'), (1, 'USER');

insert into customers (name, surname, photo) values
    ('Kassie','Tonn',''),
    ('Efram','Finn',''),
    ('Nereyda','Phinney',''),
    ('Carrol','Mckinsey',''),
    ('Liba','Conant',''),
    ('Marlicia','Eggers',''),
    ('Allyson','Scalise',''),
    ('Blythe','Sweatman',''),
    ('Hilario','Hove',''),
    ('Clayton','Just',''),
    ('Chery','Canady',''),
    ('Raman','Grimsley',''),
    ('Norman','Hafer',''),
    ('Rebekan','Brigman',''),
    ('Fidencio','Wenzel',''),
    ('Nicolette','Chasse',''),
    ('Kristine','Banner',''),
    ('Tauheedah','Stutz',''),
    ('Katiria','Bent',''),
    ('Shonteria','Mcafee',''),
    ('Charlana','Rester',''),
    ('Annabelle','Laclair',''),
    ('Graig','Reider',''),
    ('Phylicia','Lash',''),
    ('Katira','Spraggins','')
;

insert into users (username, password, id_role) values
    ('admin', '$2a$10$rKRGKXVF3T0icaJIkDes9.ovFDuLjPfic/KMmvXSK/1MdwxTQyuuW', 0),   -- (admin, password)
    ('user', '$2a$10$rKRGKXVF3T0icaJIkDes9.ovFDuLjPfic/KMmvXSK/1MdwxTQyuuW', 1)     -- (user, password)
;