DROP TABLE if EXISTS account;
DROP TABLE if EXISTS balance;
DROP TABLE if exists transactions;

--CREATE SEQUENCE id_seq;
CREATE SEQUENCE acc_id_seq;
CREATE SEQUENCE txn_id_seq;

CREATE TABLE account(
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    account_id TEXT NOT NULL DEFAULT 'CA-081-'||nextval('acc_id_seq'),
    customer_id VARCHAR
);

ALTER SEQUENCE acc_id_seq OWNED BY account.account_id;

CREATE TABLE balance(
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    currency VARCHAR,
    amount DECIMAL(12,2),
    acc_id bigint
);

CREATE TABLE transactions(
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    transaction_id TEXT NOT NULL DEFAULT 'TXN-'||nextval('txn_id_seq'),
    description VARCHAR,
    direction VARCHAR,
    currency VARCHAR,
    amount DECIMAL(12,2),
    acc_id bigint
);

ALTER SEQUENCE txn_id_seq OWNED BY transactions.transaction_id;