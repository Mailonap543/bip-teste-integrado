
INSERT INTO BENEFICIO (NOME, DESCRICAO, VALOR, ATIVO, VERSION) VALUES
('Vale Alimentacao', 'Cartao para compras de alimentos', 500.00, TRUE, 0),
('Vale Transporte', 'Ajuda de custo para transporte', 200.00, TRUE, 0);

INSERT INTO CONTA (TITULAR, SALDO, ATIVA, VERSION) VALUES
('Maria Silva', 3000.00, TRUE, 0),
('Joao Santos', 1500.00, TRUE, 0),
('Ana Pereira', 500.00, TRUE, 0);

-- Admin user (password: admin123 - BCrypt encoded)
INSERT INTO USUARIO (USERNAME, PASSWORD, NOME, EMAIL, ATIVO) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrador', 'admin@example.com', TRUE);

INSERT INTO USUARIO_ROLES (USUARIO_ID, ROLE) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER');

-- Regular user (password: user123 - BCrypt encoded)
INSERT INTO USUARIO (USERNAME, PASSWORD, NOME, EMAIL, ATIVO) VALUES
('user', '$2a$10$EqKcp1WFKAr1GHUEo1ZkCOdBphzTYYbxA6.qcXGfaplSW2f/5yKn6', 'Usuario Comum', 'user@example.com', TRUE);

INSERT INTO USUARIO_ROLES (USUARIO_ID, ROLE) VALUES
(2, 'ROLE_USER');
