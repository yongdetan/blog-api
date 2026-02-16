INSERT INTO users (first_name, last_name, email, password_hash, user_role)
VALUES (
        'admin',
        'admin',
        'admin@test.com',
        '$2a$10$V74n35X4L2PXjR75gHgcLePTzDlPSGjBMNsyN0XRxHqHg6.4JC2am',
        'ADMIN'
       )
ON CONFLICT (email) DO NOTHING;