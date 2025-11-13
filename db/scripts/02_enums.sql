DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'beverage_type') THEN
    CREATE TYPE beverage_type AS ENUM ('TEA', 'COFFEE', 'OTHER');
  END IF;
END$$;