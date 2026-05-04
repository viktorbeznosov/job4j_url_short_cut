create table if not exists sites (
    id              bigserial primary key,
    site            varchar(255) not null,
    login           varchar(120) not null,
    password_hash   varchar(255) not null,
    created_at      timestamp without time zone not null default now(),

    constraint uq_sites_site unique (site),
    constraint uq_sites_login unique (login)
);

create table if not exists urls (
    id              bigserial primary key,
    site_id         bigint not null,
    original_url    text not null,
    code            varchar(32) not null,
    total_visits    bigint not null default 0,
    created_at      timestamp without time zone not null default now(),

    constraint fk_urls_site
        foreign key (site_id) references sites(id)
        on delete cascade,

    constraint uq_urls_code unique (code)
);
