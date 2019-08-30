USE [NFBankDB]
GO

CREATE TABLE account_status(
	id integer NOT NULL PRIMARY KEY IDENTITY(1,1),
	name varchar(7) NOT NULL
);
GO

CREATE TABLE object_type(
	id integer NOT NULL PRIMARY KEY IDENTITY(1,1),
	otype varchar(7) NOT NULL
);
GO

CREATE TABLE audit_action(
	id integer NOT NULL PRIMARY KEY IDENTITY(1,1),
	a_action varchar(6) NOT NULL
);
GO

CREATE TABLE client(
	unique_id varchar(20) NOT NULL PRIMARY KEY, 
	username varchar(20) NOT NULL UNIQUE,
	[password] varchar(30) NOT NULL,
	birth_date date NOT NULL,
	name varchar(30) NOT NULL,
	surname varchar(30) NOT NULL,
	active bit NOT NULL DEFAULT 1,
);
GO

CREATE TABLE account(
	unique_id varchar(20) NOT NULL PRIMARY KEY, 
	balance float  NOT NULL,
	open_date date  NOT NULL,
	close_date date,
	id_client varchar(20) NOT NULL FOREIGN KEY REFERENCES client(unique_id),
	[status] integer NOT NULL FOREIGN KEY REFERENCES account_status(id)
);
GO

CREATE TABLE [audit](
	id bigint NOT NULL PRIMARY KEY IDENTITY(1, 1), 
	[object_id] varchar(20) NOT NULL,
	object_type integer NOT NULL FOREIGN KEY REFERENCES object_type(id),
	action_date date  NOT NULL,
	action_id integer NOT NULL FOREIGN KEY REFERENCES audit_action(id),
	new_value varchar(max)
);
GO


INSERT INTO account_status(name) 
VALUES ('open'), ('closed'), ('suspend');
go

INSERT INTO object_type(otype) 
VALUES ('client'), ('account');
go

INSERT INTO audit_action(a_action) 
VALUES ('create'), ('update'), ('delete');
go



--Для тестирования работы базы и триггеров--

/*
DROP TABLE [account]
DROP TABLE [audit]
DROP TABLE client
drop table audit_action
drop table account_status
drop table object_type
GO
*/

/*
insert into client (unique_id, username, [password], birth_date, name, surname) 
	values ('12312343212343212343', 'momami', 'milkace90', '1998-12-21',
	'Милена', 'Целикина')

insert into account(unique_id, balance, open_date, close_date, id_client, [status])
	values ('12345678909876543212', 1074.0, '2016-09-05', NULL, 1, 1)

update client
set active = 1
where id = 1

select * from client
select * from account

delete from client where id = 1


select * from client
select * from account
select * from audit

delete from audit
delete from client
*/