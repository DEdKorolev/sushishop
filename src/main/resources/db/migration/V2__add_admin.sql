INSERT INTO users (id, email, name, password, role, address)
VALUES (1, 'admin@mail.ru', 'admin', '$2a$10$4Y28EiICV2ZEEFPeo76RrOSBCUzQ9rFiVNZgndePNKCDo/NooLTc6', 'ADMIN', 'Ленина 1');

ALTER SEQUENCE user_seq RESTART WITH 2;