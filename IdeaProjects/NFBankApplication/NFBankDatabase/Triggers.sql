USE [NFBankDB]
GO

CREATE TRIGGER Delete_Client ON client
INSTEAD OF DELETE
AS
DECLARE @idClient INT
IF @@ROWCOUNT = 1
BEGIN
	SELECT @idClient = id
	FROM deleted

DELETE FROM account
WHERE account.id_client = @idClient

DELETE FROM client
WHERE client.id = @idClient

END
GO

CREATE TRIGGER Upd_client_active ON client
FOR UPDATE
AS

IF @@ROWCOUNT = 1
BEGIN

IF UPDATE(active)
BEGIN
	DECLARE @act_insert BIT, @idClient INT
	DECLARE @cPlace INT, @cPatient INT
	SELECT @act_insert = active, @idClient = id
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