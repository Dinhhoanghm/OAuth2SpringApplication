create table if not exists "user"
(
  id                serial
  constraint user_pk
  primary key,
  username          varchar(200),
  password          varchar(500),
  first_name        varchar(200),
  last_name         varchar(100),
  email             varchar(200),
  enabled           boolean,
  email_verified    boolean,
  role              varchar(100),
  is_admin          boolean,
  dept              varchar(200),
  iat               integer,
  exp               integer,
  sub               varchar(100),
  subscription_type varchar(100),
  org_id            integer
  );

comment on column "user".role is 'USER - ADMIN';

alter table "user"
  owner to admin;

create table if not exists account_verifycation
(
  id         varchar(300),
  user_id    integer,
  created_at timestamp
  );

alter table account_verifycation
  owner to admin;

create table if not exists oauth_token
(
  id                      serial
  constraint oauth_token_pk
  primary key,
  refresh_token           text,
  access_token            text,
  user_id                 integer,
  access_expiration_time  timestamp,
  refresh_expiration_time timestamp
);

alter table oauth_token
  owner to admin;

create table if not exists reset_password_token
(
  id              serial
  constraint reset_password_token_pk
  primary key,
  user_id         integer,
  created_at      timestamp default now(),
  expiration_time timestamp,
  token           varchar(200)
  );

alter table reset_password_token
  owner to admin;

create table if not exists charge_plan
(
  id           serial
  constraint charge_plan_pk
  primary key,
  name         varchar(200),
  team_size    integer,
  project_size integer,
  storage      double precision,
  support_type varchar(200),
  money        double precision
  );

alter table charge_plan
  owner to admin;

create table if not exists plan_user
(
  id      serial
  constraint plan_user_pk
  primary key,
  user_id integer,
  plan_id integer,
  status  varchar
);

alter table plan_user
  owner to admin;

create table if not exists billing_history
(
  id           serial
  constraint billing_history_pk
  primary key,
  plan_user_id integer,
  status       varchar,
  created_at   timestamp default now(),
  paid_at      timestamp,
  amount       double precision,
  session_id   varchar(300)
  );

alter table billing_history
  owner to admin;

create table if not exists organization
(
  id   serial
  constraint organization_pk
  primary key,
  name varchar(200)
  );

alter table organization
  owner to admin;

