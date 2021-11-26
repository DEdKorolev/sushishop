INSERT INTO users (id, archive, email, name, password, role)
VALUES (1, false, 'mail@mail.ru', 'admin', '$2a$10$4Y28EiICV2ZEEFPeo76RrOSBCUzQ9rFiVNZgndePNKCDo/NooLTc6', 'ADMIN');

ALTER SEQUENCE user_seq RESTART WITH 2;