package Check;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckUnique {
    public static boolean checkUniqueField(Connection con, String table, String nameField, String elem)
    throws SQLException {
        String sql = "SELECT " + nameField + " FROM " + table + " WHERE " + nameField + " = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, elem);
        ResultSet res = stmt.executeQuery();
        if (res.next()){
            return false;
        }
        return true;
    }

}
