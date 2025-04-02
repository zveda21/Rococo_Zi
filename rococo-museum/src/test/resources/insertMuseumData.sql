INSERT INTO public.country (id, name)
VALUES
    (RANDOM_UUID(), 'France'),
    (RANDOM_UUID(), 'United Kingdom'),
    (RANDOM_UUID(), 'The Netherlands');

INSERT INTO public.geolocation (id, city, country_id)
VALUES
    (RANDOM_UUID(), 'Paris', (SELECT id FROM country WHERE name = 'France' LIMIT 1)),
    (RANDOM_UUID(), 'Lyon', (SELECT id FROM country WHERE name = 'France' LIMIT 1)),
    (RANDOM_UUID(), 'London', (SELECT id FROM country WHERE name = 'United Kingdom' LIMIT 1)),
    (RANDOM_UUID(), 'Manchester', (SELECT id FROM country WHERE name = 'United Kingdom' LIMIT 1)),
    (RANDOM_UUID(), 'Amsterdam', (SELECT id FROM country WHERE name = 'The Netherlands' LIMIT 1)),
    (RANDOM_UUID(), 'Rotterdam', (SELECT id FROM country WHERE name = 'The Netherlands' LIMIT 1));

INSERT INTO public.museum (id, title, description, photo, geo_id)
VALUES
    ('e6d450a1-7d5d-4ba7-a8d5-60b263d38dca', 'Louvre Museum',
    'The world-renowned museum in Paris, home to the Mona Lisa and countless historical artworks.',
    'iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==',
    (SELECT id FROM geolocation WHERE city = 'Paris' LIMIT 1)),

    ('4f95d49b-3be9-4a74-9e43-b5b3330b02dc', 'Musée d''Orsay',
    'A spectacular museum in Paris housed in a former railway station, famous for its impressionist collections.',
    'iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==',
    (SELECT id FROM geolocation WHERE city = 'Paris' LIMIT 1)),

    ('4800753b-fd5a-46ce-b66e-03c64aa70990', 'British Museum',
    'One of the largest museums in the world, featuring artifacts like the Rosetta Stone and Egyptian mummies.',
    'iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==',
    (SELECT id FROM geolocation WHERE city = 'London' LIMIT 1));