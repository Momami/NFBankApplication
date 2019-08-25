package Managers;

import Classes.Client;
import Classes.History;


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
        client.setId(getIdFromDB());
        HistoryManager.createHistoryClient(con, client, new Date((new java.util.Date()).getTime()),
                History.Action.CREATE);
    }

    public long getIdFromDB() throws SQLException{
        String sql = "SELECT * FROM client WHERE unique_id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, client.getIdClient());
        ResultSet acc = stmt.executeQuery();
        long result = 0;
        while (acc.next()) {
            result = acc.getLong("id");
        }
        return result;
    }

    public void delete() throws SQLException{
        String deleteSql = "DELETE FROM client where id = ?";
        PreparedStatement prepStmt = con.prepareStatement(deleteSql);
        prepStmt.setString(1, Long.toString(client.getId()));
        prepStmt.executeUpdate();
        HistoryManager.createHistoryClient(con, client, new Date((new java.util.Date()).getTime()),
                History.Action.DELETE);
    }

    public void update(String upd, String name) throws SQLException {
        String sqlOld = "SELECT " + upd + " FROM client where id = ?";
        PreparedStatement stmtSelect = con.prepareStatement(sqlOld);
        stmtSelect.setString(1, Long.toString(client.getId()));
        ResultSet old = stmtSelect.executeQuery();
        String oldValue = null;
        while(old.next()){
            oldValue = old.getString(upd);
        }
        String updSql = "UPDATE client " +
                "SET " + upd + " = ? where id = ?";
        PreparedStatement stmt = con.prepareStatement(updSql);
        if (name != null){
            stmt.setString(1, name);
        }
        else{
            stmt.setNull(1, Types.TIMESTAMP);
        }
        stmt.setString(2, Long.toString(client.getId()));
        stmt.executeUpdate();
        List<String> elem = new ArrayList<String>();
        elem.add(upd);
        elem.add(oldValue);
        elem.add(name);
        HistoryManager.createHistoryUpdate(con, History.ObjectType.CLIENT, elem, client.getId(),
                new Date((new java.util.Date()).getTime()));
    }

    public List<Client> select() throws SQLException {
        String selectSql = "SELECT * FROM client where id = ?";
        PreparedStatement stmt = con.prepareStatement(selectSql);
        stmt.setString(1, Long.toString(client.getId()));
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
