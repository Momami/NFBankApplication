package Managers;

import Classes.Account;

import java.math.BigInteger;
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
        psstmt.setString(6, account.getIdClient().toString());
        psstmt.executeUpdate();
        account.setId(getIdFromDB());
    }

    public long getIdFromDB() throws SQLException{
        String sql = "SELECT id FROM account WHERE unique_id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, account.getIdAccount());
        ResultSet acc = stmt.executeQuery();
        long result = 0;
        while(acc.next()){
            result = acc.getLong("id");
        }
        return result;
    }

    public void delete() throws SQLException{
        String deleteSql = "DELETE FROM account where id = ?";
        PreparedStatement prepStmt = con.prepareStatement(deleteSql);
        prepStmt.setString(1, Long.toString(account.getId()));
        prepStmt.executeUpdate();
    }

    public void update(String upd, String name) throws SQLException {
        String updSql = "UPDATE account " +
                            "SET " + upd + " = ? where id = ?";
        PreparedStatement stmt = con.prepareStatement(updSql);
        if (name != null){
            stmt.setString(1, name);
        }
        else{
            stmt.setNull(1, Types.TIMESTAMP);
        }
        stmt.setString(2, Long.toString(account.getId()));
        stmt.executeUpdate();
    }

    public List<Account> select() throws SQLException {
        String selectSql = "SELECT * FROM account where id = ?";
        PreparedStatement prepstmt = con.prepareStatement(selectSql);
        prepstmt.setString(1, Long.toString(account.getId()));
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
                BigInteger idClient = new BigInteger(rs.getString("id_client"));
                Account account = new Account(id, balance, open, close, status, idClient);
                result.add(account);
            }
            catch (Exception e){}
        }
        return result;
    }
}
