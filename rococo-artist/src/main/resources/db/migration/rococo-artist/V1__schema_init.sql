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