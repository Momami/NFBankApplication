package Managers;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ManagerDB<T> {
    void create() throws SQLException;
    void update(String upd, String name) throws SQLException;
    void delete() throws SQLException;
    List<T> select() throws SQLException;

}
