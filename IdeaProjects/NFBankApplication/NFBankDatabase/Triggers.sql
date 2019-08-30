USE [NFBankDB]
GO

CREATE TRIGGER Delete_Client ON client
INSTEAD OF DELETE
AS
DECLARE @idClient varchar(20)
IF @@ROWCOUNT = 1
BEGIN
	SELECT @idClient = unique_id
	FROM deleted

DELETE FROM account
WHERE account.id_client = @idClient

DELETE FROM client
WHERE client.unique_id = @idClient

END
GO

CREATE TRIGGER Upd_client_active ON client
FOR UPDATE
AS

IF @@ROWCOUNT = 1
BEGIN

IF UPDATE(active)
BEGIN
	DECLARE @act_insert BIT, @idClient varchar(20)
	SELECT @act_insert = active, @idClient = unique_id
		FROM inserted

	IF @act_insert = 1
	BEGIN
		UPDATE account
			SET [status] = (SELECT id FROM account_status WHERE name = 'open')
			WHERE id_client = @idClient
	END;

	ELSE
	BEGIN
		UPDATE account
			SET [status] = (SELECT id FROM account_status WHERE name = 'suspend')
			WHERE id_client = @idClient
	END;

END;
END;
GO


--DROP TRIGGER Upd_client_active
--DROP TRIGGER Delete_client
--GO