-- Make sure the uuid-ossp extension is enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create the country table
CREATE TABLE IF NOT EXISTS "country"
(
    id UUID UNIQUE NOT NULL DEFAULT uuid_generate_v1() PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO "country" (id, name)
VALUES
    (uuid_generate_v1(), 'France'),
    (uuid_generate_v1(), 'United Kingdom'),
    (uuid_generate_v1(), 'The Netherlands');

-- Create the geolocation table
CREATE TABLE IF NOT EXISTS "geolocation"
(
    id UUID UNIQUE NOT NULL DEFAULT uuid_generate_v1() PRIMARY KEY,
    city VARCHAR(255) NOT NULL,
    country_id UUID,
    FOREIGN KEY (country_id) REFERENCES country(id)
);

-- Insert geo data
INSERT INTO "geolocation" (id, city, country_id)
VALUES
    (uuid_generate_v1(), 'Paris', (SELECT id FROM "country" WHERE name = 'France' LIMIT 1)),
    (uuid_generate_v1(), 'Lyon', (SELECT id FROM "country" WHERE name = 'France' LIMIT 1)),
    (uuid_generate_v1(), 'London', (SELECT id FROM "country" WHERE name = 'United Kingdom' LIMIT 1)),
    (uuid_generate_v1(), 'Manchester', (SELECT id FROM "country" WHERE name = 'United Kingdom' LIMIT 1)),
    (uuid_generate_v1(), 'Amsterdam', (SELECT id FROM "country" WHERE name = 'The Netherlands' LIMIT 1)),
    (uuid_generate_v1(), 'Rotterdam', (SELECT id FROM "country" WHERE name = 'The Netherlands' LIMIT 1));

-- Create the museum table if it doesn't exist
CREATE TABLE IF NOT EXISTS "museum"
(
    id UUID UNIQUE NOT NULL DEFAULT uuid_generate_v1() PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    geo_id UUID,
    FOREIGN KEY (geo_id) REFERENCES geolocation(id)
);

-- Insert museum data
INSERT INTO "museum" (id, title, geo_id)
VALUES
    (uuid_generate_v1(), 'Louvre Museum', (SELECT id FROM "geolocation" WHERE city = 'Paris' LIMIT 1)),
    (uuid_generate_v1(), 'Centre Pompidou', (SELECT id FROM "geolocation" WHERE city = 'Paris' LIMIT 1)),
    (uuid_generate_v1(), 'Musée des Confluences', (SELECT id FROM "geolocation" WHERE city = 'Lyon' LIMIT 1)),
    (uuid_generate_v1(), 'British Museum', (SELECT id FROM "geolocation" WHERE city = 'London' LIMIT 1)),
    (uuid_generate_v1(), 'Tate Modern', (SELECT id FROM "geolocation" WHERE city = 'London' LIMIT 1)),
    (uuid_generate_v1(), 'Victoria and Albert Museum', (SELECT id FROM "geolocation" WHERE city = 'London' LIMIT 1)),
    (uuid_generate_v1(), 'National Football Museum', (SELECT id FROM "geolocation" WHERE city = 'Manchester' LIMIT 1)),
    (uuid_generate_v1(), 'Rijksmuseum', (SELECT id FROM "geolocation" WHERE city = 'Amsterdam' LIMIT 1)),
    (uuid_generate_v1(), 'Van Gogh Museum', (SELECT id FROM "geolocation" WHERE city = 'Amsterdam' LIMIT 1)),
    (uuid_generate_v1(), 'Stedelijk Museum', (SELECT id FROM "geolocation" WHERE city = 'Amsterdam' LIMIT 1)),
    (uuid_generate_v1(), 'Boijmans Van Beuningen Museum', (SELECT id FROM "geolocation" WHERE city = 'Rotterdam' LIMIT 1));

