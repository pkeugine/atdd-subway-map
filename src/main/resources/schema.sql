create table if not exists STATION(
    id bigint auto_increment not null,
    name varchar(255) unique not null,
    PRIMARY KEY (id)
);

create table if not exists LINE (
    id bigint auto_increment not null,
    name varchar(255) unique not null,
    color varchar(50) unique not null,
    primary key (id)
);