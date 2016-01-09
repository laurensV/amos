CREATE TABLE profile(
	id serial PRIMARY KEY,
	name varchar(40) NOT NULL CHECK (name <> '')
);

CREATE TABLE website(
	id serial PRIMARY KEY,
	dna_settings json NOT NULL,
	url varchar(512) NOT NULL CHECK (url <> '')
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE dna(
	id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	profile_id integer REFERENCES profile (id)
);