-- CLIENTS
-- create sequence client_seq start 1 increment 1;

create table clients (
                       client_id varchar(255) not null,
                       last_name varchar(255),
                       first_name varchar(255),
                       patrinymic varchar(255),
                       date_or_birth timestamp,
                       passport_num varchar(255),
                       passport_valid_to_date timestamp,
                       primary key (client_id)
);
-- ACCOUNTS
-- create sequence account_seq start 1 increment 1;

create table accounts (
                         account_num varchar(255) not null,
                         valid_to date,
                         client varchar(255),
                         primary key (account_num)
);

-- LINK BETWEEN CLIENTS AND ACCOUNTS
alter table accounts
    add constraint accounts_fk_clients
        foreign key (client) references clients;

-- CARDS
-- create sequence card_seq start 1 increment 1;

create table cards (
                            card_num varchar(255) not null,
                            account_num varchar(255),
                            primary key (card_num)
);

-- LINK BETWEEN CARDS AND ACCOUNTS
alter table cards
    add constraint cards_fk_accounts
    foreign key (account_num) references accounts;

-- TERMINALS
-- create sequence terminals_seq start 1 increment 1;

create table terminals (
                          terminal_id varchar(255) not null,
                          terminal_type varchar(255),
                          terminal_city varchar(255),
                          terminal_address varchar(255),
                          primary key (terminal_id)
);

-- CARDS IN TERMINAL
create table transactions (
                             trans_id varchar(255),
                             trans_date timestamp,
                             card_num varchar(255) not null,
                             open_type varchar(255),
                             amt number,
                             open_result varchar(255),
                             terminal varchar(255)

);

alter table transactions
    add constraint transactions_fk_cards
    foreign key (card_num) references cards;

alter table transactions
    add constraint transactions_fk_terminals
    foreign key (terminal) references terminals;
