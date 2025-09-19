-- This migration fixes the Flyway schema history by marking failed migrations as resolved
-- Since the tables already exist (created by Hibernate), we just need to update the schema history

-- Update the failed migration record to mark it as resolved
UPDATE flyway_schema_history 
SET success = 1, 
    checksum = 0,
    description = 'Create idempotency table (resolved)'
WHERE version = 2 AND success = 0;
