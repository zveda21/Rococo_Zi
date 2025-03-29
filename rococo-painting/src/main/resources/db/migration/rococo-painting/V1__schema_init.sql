-- Make sure the uuid-ossp extension is enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS "painting" (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),  -- UUID default generation
    title VARCHAR(255) NOT NULL,                     -- Title column
    description TEXT NOT NULL,                       -- Description column
    content BYTEA,                                   -- Content as bytea (binary data for the image)
    museum_id UUID,                                  -- Foreign key for museum
    artist_id UUID                                   -- Foreign key for artist
);

INSERT INTO "painting" (title, description)
VALUES
    ('Starry Night', 'A famous painting by Vincent van Gogh, depicting a swirling night sky over a village.'),
    ('The Persistence of Memory', 'A surrealist painting by Salvador Dalí, featuring melting clocks.'),
    ('The Scream', 'An iconic expressionist painting by Edvard Munch, depicting an agonized figure against a turbulent sky.'),
    ('Mona Lisa', 'A portrait by Leonardo da Vinci, featuring a woman with an enigmatic expression.'),
    ('The Night Watch', 'A famous group portrait painted by Rembrandt, showing a militia company of Amsterdam.'),
    ('Guernica', 'A mural-sized painting by Pablo Picasso, depicting the horrors of the bombing of Guernica during the Spanish Civil War.'),
    ('The Birth of Venus', 'An iconic painting by Sandro Botticelli, depicting the goddess Venus emerging from the sea.'),
    ('The Girl with a Pearl Earring', 'A famous painting by Johannes Vermeer, featuring a young girl wearing a pearl earring.'),
    ('The Kiss', 'A romantic painting by Gustav Klimt, showcasing a couple in an intimate embrace covered in gold leaf.'),
    ('Water Lilies', 'A series of impressionist paintings by Claude Monet, depicting his flower garden at Giverny during different times of day.');
