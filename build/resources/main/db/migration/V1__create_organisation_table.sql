CREATE TABLE organisation (
    id serial NOT NULL UNIQUE,
    name varchar(255) NOT NULL UNIQUE,
    taxpayer_id bigint NOT NULL UNIQUE,
    checking_account varchar(20) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);