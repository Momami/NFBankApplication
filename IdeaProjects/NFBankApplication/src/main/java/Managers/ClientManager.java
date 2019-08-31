package Managers;

import Classes.Client;
import Classes.History;
import Classes.Account;
import NewExceptions.DateException;
import NewExceptions.IdNotValidException;
import NewExceptions.NameIsNullException;
import NewExceptions.UsernameNotValidException;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ClientManager implements ManagerDB{
    private Client client;
    private Connection con;

    public ClientManager(Connection con, Client client) {
        this.con = con;
        this.client = client;
    }

    public void create() throws SQLException{
        String createSQL = "INSERT INTO client (unique_id, username, password, birth_date, [name], surname) VALUES " +
                "(?, ?, ?, ?, ?, ?);";
        PreparedStatement psstmt = con.prepareStatement(createSQL);
        psstmt.setString(1, client.getIdClient());
        psstmt.setString(2, client.getUsername());
        psstmt.setString(3, client.getPassword());
        if (client.getBirthOfDate() != null) {
            String dt = client.getBirthOfDate().toString();
            psstmt.setString(4, dt);
        }
        else{
            psstmt.setNull(4, Types.DATE);
        }
        psstmt.setString(5, client.getFirstName());
        psstmt.setString(6, client.getLastName());
        psstmt.executeUpdate();
        HistoryManager.createHistoryClient(con, client, new Date((new java.util.Date()).getTime()),
                History.Action.CREATE);
    }

    public void delete() throws SQLException{
        String selectAccounts = "SELECT * FROM account WHERE id_client = ?";
        PreparedStatement selAcc = con.prepareStatement(selectAccounts);
        selAcc.setString(1, client.getIdClient());
        ResultSet res = selAcc.executeQuery();
        try {
            while (res.next()) {
                Account account = new Account(res.getString(1), res.getFloat(2),
                        res.getDate(3), res.getDate(4),
                        Account.AccountStatus.getStatus(Integer.parseInt(res.getString(6))),
                        client.getIdClient());
                HistoryManager.createHistoryAccount(con, account, new Date((new java.util.Date()).getTime()),
                        History.Action.DELETE);
            }
        }
        catch (Exception e){}

        String deleteAccounts = "DELETE FROM account WHERE id_client = ?";
        PreparedStatement delAcc = con.prepareStatement(deleteAccounts);
        delAcc.setString(1, client.getIdClient());
        delAcc.executeUpdate();

        String deleteSql = "DELETE FROM client where unique_id = ?";
        PreparedStatement prepStmt = con.prepareStatement(deleteSql);
        prepStmt.setString(1, client.getIdClient());
        prepStmt.executeUpdate();
        HistoryManager.createHistoryClient(con, client, new Date((new java.util.Date()).getTime()),
                History.Action.DELETE);
    }

    private String prepareSelectOld(String upd) throws SQLException{
        String sqlOld = "SELECT " + upd + " FROM client where unique_id = ?";
        PreparedStatement stmtSelect = con.prepareStatement(sqlOld);
        stmtSelect.setString(1, client.getIdClient());
        ResultSet old = stmtSelect.executeQuery();
        String oldValue = null;
        while(old.next()) {
            oldValue = old.getString(upd);
        }
        return oldValue;
    }

    private PreparedStatement prepareUpdate(String upd)throws SQLException{
        String updSql = "UPDATE client " +
                "SET " + upd + " = ? where unique_id = ?";
        PreparedStatement stmt = con.prepareStatement(updSql);
        stmt.setString(2, client.getIdClient());
        return stmt;
    }

    private void createHistory(String old, String newElem, String nameField, String idClient) throws SQLException{
        List<String> elem = new ArrayList<String>();
        elem.add(nameField);
        elem.add(old);
        elem.add(newElem);
        HistoryManager.createHistoryUpdate(con, History.ObjectType.CLIENT, elem, idClient,
                new Date((new java.util.Date()).getTime()));
    }

    public void updateUniqueId(String newId) throws SQLException, IdNotValidException {
        String oldVal = prepareSelectOld("unique_id");
        PreparedStatement stmt = prepareUpdate("unique_id");
        client.setIdClient(newId);
        stmt.setString(1, newId);
        stmt.executeUpdate();
        createHistory(oldVal, newId, "unique_id", oldVal);
    }

    public void updateUsername(String newUsername) throws SQLException, UsernameNotValidException {
        String oldVal = prepareSelectOld("username");
        PreparedStatement stmt = prepareUpdate("username");
        client.setUsername(newUsername);
        stmt.setString(1, newUsername);
        stmt.executeUpdate();
        createHistory(oldVal, newUsername, "username", client.getIdClient());
    }

    public void updatePassword(String newPassword) throws SQLException {
        String oldVal = prepareSelectOld("password");
        PreparedStatement stmt = prepareUpdate("password");
        client.setPassword(newPassword);
        stmt.setString(1, newPassword);
        stmt.executeUpdate();
        createHistory(oldVal, newPassword, "password", client.getIdClient());
    }

    public void updateBirthDate(Date newDate) throws SQLException, DateException {
        String oldVal = prepareSelectOld("birth_date");
        PreparedStatement stmt = prepareUpdate("birth_date");
        client.setBirthOfDate(newDate);
        stmt.setString(1, newDate.toString());
        stmt.executeUpdate();
        createHistory(oldVal, newDate.toString(), "birth_date", client.getIdClient());
    }

    public void updateFirstName(String newFname) throws SQLException, NameIsNullException {
        String oldVal = prepareSelectOld("name");
        PreparedStatement stmt = prepareUpdate("name");
        client.setFirstName(newFname);
        stmt.setString(1, newFname);
        stmt.executeUpdate();
        createHistory(oldVal, newFname, "name", client.getIdClient());
    }

    public void updateLastName(String newLname) throws SQLException, NameIsNullException {
        String oldVal = prepareSelectOld("surname");
        PreparedStatement stmt = prepareUpdate("surname");
        client.setLastName(newLname);
        stmt.setString(1, newLname);
        stmt.executeUpdate();
        createHistory(oldVal, newLname, "surname", client.getIdClient());
    }

    public List<Client> select() throws SQLException {
        String selectSql = "SELECT * FROM client where unique_id = ?";
        PreparedStatement stmt = con.prepareStatement(selectSql);
        stmt.setString(1, client.getIdClient());
        ResultSet rs = stmt.executeQuery();
        List<Client> result = new ArrayList<Client>();
        while (rs.next()) {
            try {
                Date bdate = null;
                if (rs.getString("birth_date") != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    bdate = new java.sql.Date(dateFormat.parse(rs.getString("birth_date")).getTime());
                }
                Client cl = new Client(rs.getString("unique_id"), rs.getString("username"),
                        rs.getString("password"), bdate, rs.getString("name"),
                        rs.getString("surname"));
                result.add(cl);
            }
            catch(Exception e){}
        }
        return result;
    }
}
