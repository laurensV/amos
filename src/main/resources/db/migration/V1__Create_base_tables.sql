CREATE TABLE profile(
	id serial PRIMARY KEY,
	name varchar(40) NOT NULL CHECK (name <> ''),
	latitude real,
	longitude real,
	time_of_day integer
);

CREATE TABLE website(
	id serial PRIMARY KEY,
	dna_settings json NOT NULL,
	url varchar(512) NOT NULL CHECK (url <> ''),
    generation integer
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE individual(
	id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	profile_id integer,
	-- profile_id integer REFERENCES profile (id),
  	website_id integer REFERENCES website (id),
  	phenotype json NOT NULL,
    generation integer
);

CREATE TABLE individual_goal(
    id serial PRIMARY KEY,
	individual_id UUID REFERENCES individual (id),
	name integer,
  	score integer
);
