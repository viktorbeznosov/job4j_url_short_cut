create table if not exists sites (
    id              bigserial primary key,
    site            varchar(255) not null,
    login           varchar(120) not null,
    password_hash   varchar(255) not null,
    created_at      timestamp without time zone not null default now(),

    constraint uq_sites_site unique (site),
    constraint uq_sites_login unique (login)
);