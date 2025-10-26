DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'beverage_type') THEN
    CREATE TYPE beverage_type AS ENUM ('TEA', 'COFFEE');
  END IF;

  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'tea_type') THEN
    CREATE TYPE brew_method AS ENUM ('BLACK','GREEN','WHITE','HERBAL','FRUIT','OOLONG');
  END IF;
END$$;