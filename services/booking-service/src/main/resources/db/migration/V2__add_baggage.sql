-- booking-service · migration V2: Add baggage fields to booking table

ALTER TABLE booking 
ADD COLUMN IF NOT EXISTS baggage_weight_kg INT DEFAULT 0,
ADD COLUMN IF NOT EXISTS baggage_fee DECIMAL(12,2) DEFAULT 0.00;
