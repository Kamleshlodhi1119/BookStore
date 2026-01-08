--INSERT INTO books (ID, ISBN, TITLE, DESCRIPTION, PRICE, STOCKQUANTITY, CREATEDAT, UPDATEDAT) VALUES
--(1, '9780140449136', 'Meditations', 'Marcus Aurelius - Meditations', 199.0, 10, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
--(2, '9780140449266', 'The Odyssey', 'Homer - The Odyssey', 299.0, 5, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO books (id, isbn, title, description, price, stock_quantity)
VALUES 
(1, '9780134685991', 'Effective Java', 'Best practices for Java programming', 450.00, 10),

(2, '9781492078005', 'Spring Boot in Action', 'Comprehensive Spring Boot guide', 550.00, 15),

(3, '9780596009205', 'Head First Design Patterns', 'Design patterns explained in simple way', 600.00, 20);





INSERT INTO USERS (ID, USERNAME, EMAIL, PASSWORD_HASH, CREATED_AT, UPDATED_AT)
VALUES
(1, 'john_doe', 'john@example.com', '$2a$10$BfJxZaw/Aj1r2jJ0dXGueeWuk..hashedPasswordHere', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2, 'admin', 'admin@bookstore.com', '$2a$10$E2h8HkF67RJhI0uGhashedPasswordHere', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(3, 'kamlesh', 'kamlesh@example.com', '$2a$10$gi8..someHashedPassHere', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
