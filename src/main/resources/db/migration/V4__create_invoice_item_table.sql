CREATE TABLE invoice_item (
    id serial NOT NULL UNIQUE,
    price bigint check (price > 0),
    amount int check (amount >= 0),
    nomenclature int references public.nomenclature (id) NOT NULL,
    invoice_id int references public.invoice (id) NOT NULL,
    PRIMARY KEY (id)
);