create table if not exists usuario (
    id uuid primary key,
    external_provider varchar(50) not null,
    external_subject varchar(255) not null,
    nome varchar(255),
    email varchar(255),
    cpf varchar(20),
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    constraint uk_usuario_provider_sub unique (external_provider, external_subject)
);
