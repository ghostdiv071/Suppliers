CREATE TABLE invoice (
    id serial NOT NULL UNIQUE,
    date date NOT NULL,
    organisation_id int references public.organisation (id) NOT NULL,
    PRIMARY KEY (id)
);