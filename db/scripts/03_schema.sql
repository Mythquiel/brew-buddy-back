CREATE TABLE IF NOT EXISTS beverage (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  type beverage_type NOT NULL,          -- TEA / COFFEE
  name TEXT NOT NULL,
  brew_time_min_sec SMALLINT,
  brew_time_max_sec SMALLINT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),

  CONSTRAINT chk_brew_time_range
    CHECK (
      (brew_time_min_sec IS NULL AND brew_time_max_sec IS NULL)
      OR (brew_time_min_sec IS NOT NULL AND brew_time_max_sec IS NOT NULL
          AND brew_time_min_sec >= 0
          AND brew_time_max_sec >= brew_time_min_sec)
    )
);

CREATE TABLE IF NOT EXISTS beverage_quantity (
  beverage_id UUID PRIMARY KEY REFERENCES beverage(id) ON DELETE CASCADE,
  quantity_grams INTEGER NOT NULL DEFAULT 0,
  CONSTRAINT chk_quantity_nonnegative CHECK (quantity_grams >= 0)
);

CREATE TABLE IF NOT EXISTS tag (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS beverage_tag (
  beverage_id UUID REFERENCES beverage(id) ON DELETE CASCADE,
  tag_id UUID REFERENCES tag(id) ON DELETE CASCADE,
  PRIMARY KEY (beverage_id, tag_id)
);

CREATE TABLE IF NOT EXISTS brew_log (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  beverage_id UUID NOT NULL REFERENCES beverage(id) ON DELETE CASCADE,
  amount_used SMALLINT NOT NULL,
  brewed_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT chk_amount_positive CHECK (amount_used > 0)
);

CREATE INDEX IF NOT EXISTS idx_brew_log_beverage ON brew_log(beverage_id);
CREATE INDEX IF NOT EXISTS idx_brew_log_brewed_at ON brew_log(brewed_at DESC);
CREATE INDEX IF NOT EXISTS idx_beverage_name ON beverage((lower(name)));
