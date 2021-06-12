drop table STATION;
drop table LINE;
drop table SECTION;

create table if not exists STATION (
    id bigint auto_increment not null,
    name varchar(255) unique not null,
    primary key (id)
);

create table if not exists LINE (
    id bigint auto_increment not null,
    name varchar(255) unique not null,
    color varchar(50) unique not null
);

create table if not exists SECTION (
    id bigint auto_increment not null,
    line_id bigint not null,
    up_station_id bigint not null,
    down_station_id bigint not null,
    distance int not null,
    primary key (id),
    foreign key (up_station_id) references STATION(id),
    foreign key (down_station_id) references STATION(id)
);