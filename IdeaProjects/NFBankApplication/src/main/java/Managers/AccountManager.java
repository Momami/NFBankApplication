package Managers;

import Classes.Account;
import Classes.History;
import NewExceptions.DateException;
import NewExceptions.IdNotValidException;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AccountManager implements ManagerDB{
    private Account account;
    private Connection con;

    public AccountManager(Connection con, Account account){
        this.account = account;
        this.con = con;
    }

    public void create() throws SQLException {
        String createSQL;

        createSQL = "INSERT INTO account (unique_id, balance, open_date, close_date, status, id_client) VALUES " +
                    "(?, ?, ?, ?, ?, ?);";

        PreparedStatement psstmt = con.prepareStatement(createSQL);
        psstmt.setString(1, account.getIdAccount());
        psstmt.setString(2, Float.toString(account.getBalance()));
        psstmt.setString(3, account.getOpen_date().toString());
        if (account.getClose_date() != null)
            psstmt.setString(4, account.getClose_date().toString());
        else
            psstmt.setNull(4, Types.DATE);
        psstmt.setString(5, account.getStatus().getId());
        psstmt.setString(6, account.getIdClient());
        psstmt.executeUpdate();
        HistoryManager.createHistoryAccount(con, account, new Date((new java.util.Date()).getTime()),
                History.Action.CREATE);
    }

    public void delete() throws SQLException{
        String deleteSql = "DELETE FROM account where unique_id = ?";
        PreparedStatement prepStmt = con.prepareStatement(deleteSql);
        prepStmt.setString(1, account.getIdAccount());
        prepStmt.executeUpdate();
        HistoryManager.createHistoryAccount(con, account, new Date((new java.util.Date()).getTime()),
                History.Action.DELETE);
    }

    private String prepareSelectOld(String upd) throws SQLException{
        String sqlOld = "SELECT " + upd + " FROM account where unique_id = ?";
        PreparedStatement stmtSelect = con.prepareStatement(sqlOld);
        stmtSelect.setString(1, account.getIdAccount());
        ResultSet old = stmtSelect.executeQuery();
        String oldValue = null;
        while(old.next()) {
            oldValue = old.getString(upd);
        }
        return oldValue;
    }

    private PreparedStatement prepareUpdate(String upd)throws SQLException{
        String updSql = "UPDATE account " +
                "SET " + upd + " = ? where unique_id = ?";
        PreparedStatement stmt = con.prepareStatement(updSql);
        stmt.setString(2, account.getIdAccount());
        return stmt;
    }

    private void createHistory(String old, String newElem, String nameField, String idAcc) throws SQLException{
        List<String> elem = new ArrayList<String>();
        elem.add(nameField);
        elem.add(old);
        elem.add(newElem);
        HistoryManager.createHistoryUpdate(con, History.ObjectType.ACCOUNT, elem, idAcc,
                new Date((new java.util.Date()).getTime()));
    }

    public void updateUniqueId(String newId) throws SQLException, IdNotValidException {
        String oldVal = prepareSelectOld("unique_id");
        PreparedStatement stmt = prepareUpdate("unique_id");
        account.setIdAccount(newId);
        stmt.setString(1, newId);
        stmt.executeUpdate();
        createHistory(oldVal, newId, "unique_id", oldVal);
    }

    public void updateBalance(float newBal) throws SQLException{
        String oldVal = prepareSelectOld("balance");
        PreparedStatement stmt = prepareUpdate("balance");
        account.setBalance(newBal);
        stmt.setString(1, Float.toString(newBal));
        stmt.executeUpdate();
        createHistory(oldVal, Float.toString(newBal), "balance", account.getIdAccount());
    }

    public void updateOpenDate(Date open) throws SQLException, DateException {
        String oldVal = prepareSelectOld("open_date");
        PreparedStatement stmt = prepareUpdate("open_date");
        account.setOpen_date(open);
        stmt.setString(1, open.toString());
        stmt.executeUpdate();
        createHistory(oldVal, open.toString(), "open_date", account.getIdAccount());
    }

    public void updateCloseDate(Date close) throws SQLException{
        String oldVal = prepareSelectOld("close_date");
        PreparedStatement stmt = prepareUpdate("close_date");
        account.setClose_date(close);
        if (close != null){
            stmt.setString(1, close.toString());
        }
        else{
            stmt.setNull(1, Types.DATE);
        }
        stmt.executeUpdate();
        createHistory(oldVal, close != null ? close.toString() : null, "close_date", account.getIdAccount());
    }

    public void updateStatus(Account.AccountStatus status) throws SQLException{
        String oldVal = prepareSelectOld("status");
        PreparedStatement stmt = prepareUpdate("status");
        account.setStatus(status);
        stmt.setString(1, status.getId());
        stmt.executeUpdate();
        createHistory(Account.AccountStatus.getStatus(Integer.parseInt(oldVal)).toString(),
                status.toString(), "status", account.getIdAccount());
    }



    public List<Account> select() throws SQLException {
        String selectSql = "SELECT * FROM account where unique_id = ?";
        PreparedStatement prepstmt = con.prepareStatement(selectSql);
        prepstmt.setString(1, account.getIdAccount());
        ResultSet rs = prepstmt.executeQuery();
        List<Account> result = new ArrayList<Account>();
        while (rs.next()) {
            try {
                String id = rs.getString("unique_id");
                float balance = Float.parseFloat(rs.getString("balance"));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date open = new java.sql.Date(dateFormat.parse(rs.getString("open_date")).getTime());
                Date close = null;
                if (rs.getString("close_date") != null)
                    close = new java.sql.Date(dateFormat.parse(rs.getString("close_date")).getTime());
                ResultSet stat = (con.prepareStatement("SELECT name FROM account_status WHERE name = " +
                        rs.getString("status"))).executeQuery();
                Account.AccountStatus status = Account.AccountStatus.getStatus(Integer.parseInt
                        (rs.getString("status")));
                String idClient = rs.getString("id_client");
                Account account = new Account(id, balance, open, close, status, idClient);
                result.add(account);
            }
            catch (Exception e){}
        }
        return result;
    }
}
