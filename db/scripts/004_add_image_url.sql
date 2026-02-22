-- Add image_url column to beverage table
ALTER TABLE beverage ADD COLUMN IF NOT EXISTS image_url VARCHAR(500);

COMMENT ON COLUMN beverage.image_url IS 'URL to beverage image stored in Supabase Storage';
