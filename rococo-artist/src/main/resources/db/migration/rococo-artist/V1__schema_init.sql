-- Make sure the uuid-ossp extension is enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create the artist table if it doesn't exist
CREATE TABLE IF NOT EXISTS "artist"
(
    id UUID UNIQUE NOT NULL DEFAULT uuid_generate_v1() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    biography TEXT NOT NULL,
    photo BYTEA NOT NULL
);

INSERT INTO "artist" (id, name, biography, photo)
VALUES (
           uuid_generate_v1(),
           'Vincent van Gogh',
           'Vincent van Gogh was a Dutch Post-Impressionist painter who is among the most famous and influential figures in the history of Western art.',
           DECODE('iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==', 'base64')
       ),
       (
           uuid_generate_v1(),
           'Frida Kahlo',
           'Frida Kahlo was a Mexican painter known for her many portraits, self-portraits, and works inspired by nature and Mexican culture. Her work explores identity, the human body, and death.',
           DECODE('iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==', 'base64')
       ),
       (
           uuid_generate_v1(),
           'Leonardo da Vinci',
           'Claude Monet was a French painter and a founder of Impressionism. His work is known for capturing light and atmosphere, especially in his Water Lilies and garden scenes.',
           DECODE('iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==', 'base64')
       )
    ;
