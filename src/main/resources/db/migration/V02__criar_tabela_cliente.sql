CREATE TABLE cliente (
	uuid uuid NOT NULL DEFAULT uuid_generate_v4(),
	nome varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	cpf varchar(255) NOT NULL,
	rg varchar(255) NOT NULL,
	endereco varchar(255) NOT NULL,
	renda decimal NOT NULL,
	senha varchar(255) NOT NULL,
	CONSTRAINT cliente_pk PRIMARY KEY (uuid)
);
