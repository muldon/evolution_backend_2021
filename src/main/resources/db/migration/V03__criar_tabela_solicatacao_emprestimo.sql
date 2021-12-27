CREATE TABLE solicitacao_emprestimo (
	uuid uuid NOT NULL DEFAULT uuid_generate_v4(),
	cliente_uuid uuid NOT NULL,
	valor decimal NOT NULL,
	data_primeira_parcela timestamp NOT NULL,
	quantidade_parcela int NOT NULL,
	CONSTRAINT solicitacao_emprestimo_pk PRIMARY KEY (uuid),
	CONSTRAINT fk_solicitacao_emprestimo_cliente_uuid FOREIGN KEY ("cliente_uuid") REFERENCES cliente("uuid")
);
