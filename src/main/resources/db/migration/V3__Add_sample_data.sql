-- Sample data for testing (optional)
-- This migration is for demonstration purposes only
-- In production, you would not include sample data in migrations

-- Note: This is commented out to avoid inserting test data in production
-- Uncomment the following lines if you want to add sample data for testing

/*
INSERT INTO accounts (account_number, account_number_hash, account_holder_name, email, phone_number, email_hash, phone_hash, balance, account_type, status, version) VALUES
('1234567890', SHA2('1234567890', 256), 'John Doe', 'john.doe@example.com', '+1234567890', SHA2('john.doe@example.com', 256), SHA2('+1234567890', 256), 1000.00, 'CHECKING', 'ACTIVE', 0),
('0987654321', SHA2('0987654321', 256), 'Jane Smith', 'jane.smith@example.com', '+0987654321', SHA2('jane.smith@example.com', 256), SHA2('+0987654321', 256), 2500.00, 'SAVINGS', 'ACTIVE', 0);
*/
