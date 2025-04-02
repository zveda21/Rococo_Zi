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
    description TEXT NOT NULL,
    photo BYTEA NOT NULL,
    geo_id UUID,
    FOREIGN KEY (geo_id) REFERENCES geolocation(id)
);

-- Insert museum data
INSERT INTO "museum" (id, title, description, photo, geo_id)
VALUES
    (uuid_generate_v1(), 'Louvre Museum',
    'The world-renowned museum in Paris, home to the Mona Lisa and countless historical artworks.',
    decode('iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==', 'base64'),
    (SELECT id FROM "geolocation" WHERE city = 'Paris' LIMIT 1)),

    (uuid_generate_v1(), 'Musée d''Orsay',
    'A spectacular museum in Paris housed in a former railway station, famous for its impressionist collections.',
    decode('iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==', 'base64'),
    (SELECT id FROM "geolocation" WHERE city = 'Paris' LIMIT 1)),

    (uuid_generate_v1(), 'British Museum',
    'One of the largest museums in the world, featuring artifacts like the Rosetta Stone and Egyptian mummies.',
    decode('iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==', 'base64'),
    (SELECT id FROM "geolocation" WHERE city = 'London' LIMIT 1)),

    (uuid_generate_v1(), 'Tate Modern',
    'A modern art gallery on the banks of the Thames, showcasing contemporary artworks and installations.',
    decode('iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==', 'base64'),
    (SELECT id FROM "geolocation" WHERE city = 'London' LIMIT 1)),

    (uuid_generate_v1(), 'Science and Industry Museum',
    'Located in Manchester, this museum celebrates Britain’s industrial heritage and technological advancements.',
    decode('iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==', 'base64'),
    (SELECT id FROM "geolocation" WHERE city = 'Manchester' LIMIT 1)),

    (uuid_generate_v1(), 'Van Gogh Museum',
    'Dedicated to the works of Vincent van Gogh, this Amsterdam museum houses the largest collection of his paintings.',
    decode('iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==', 'base64'),
    (SELECT id FROM "geolocation" WHERE city = 'Amsterdam' LIMIT 1)),

    (uuid_generate_v1(), 'Rijksmuseum',
    'A Dutch national museum in Amsterdam, famous for works by Rembrandt, Vermeer, and more.',
    decode('iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==', 'base64'),
    (SELECT id FROM "geolocation" WHERE city = 'Amsterdam' LIMIT 1)),

    (uuid_generate_v1(), 'Boijmans Van Beuningen Museum',
    'A Rotterdam museum featuring an eclectic mix of old masters, surrealists, and contemporary art.',
    decode('iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==', 'base64'),
    (SELECT id FROM "geolocation" WHERE city = 'Rotterdam' LIMIT 1)),

    (uuid_generate_v1(), 'Musée des Beaux-Arts de Lyon',
    'One of the largest fine art museums in France, featuring works from ancient Egypt to modern art.',
    decode('iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==', 'base64'),
    (SELECT id FROM "geolocation" WHERE city = 'Lyon' LIMIT 1)),

    (uuid_generate_v1(), 'Museum of Fine Arts of Lyon',
    'A treasure trove of European paintings, sculptures, and decorative arts, housed in a historic abbey.',
    decode('iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==', 'base64'),
    (SELECT id FROM "geolocation" WHERE city = 'Lyon' LIMIT 1));