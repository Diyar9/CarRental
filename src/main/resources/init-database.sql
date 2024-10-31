-- Skapa tabellen "cars" om den inte redan finns
CREATE TABLE IF NOT EXISTS cars (
                                    id SERIAL PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL,
    price_per_day DOUBLE PRECISION NOT NULL
    );

-- Skapa unikt index på "name"-kolumnen om det inte finns
CREATE UNIQUE INDEX IF NOT EXISTS unique_car_name_idx ON cars(name);

-- Lägg in bilar om de inte redan finns
INSERT INTO cars (name, price_per_day)
VALUES
    ('Volvo S60', 1500),
    ('Volkswagen Golf', 1333),
    ('Ford Mustang', 3000),
    ('Ford Transit', 2400)
    ON CONFLICT (name) DO NOTHING;
