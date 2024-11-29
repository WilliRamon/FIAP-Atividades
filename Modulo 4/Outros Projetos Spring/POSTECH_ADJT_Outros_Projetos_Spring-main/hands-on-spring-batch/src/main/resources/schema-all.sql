DROP TABLE IF EXISTS person;

create table person
(
    id               serial,
    nome             varchar(240),
    street_name      varchar(240),
    number           varchar(240),
    city             varchar(240),
    country          varchar(240),
    email            varchar(240),
    phoneNumber      varchar(240),
    create_date_time timestamp
)
