package Managers;


import java.sql.SQLException;
import java.util.List;

public interface ManagerDB<T> {
    void create() throws SQLException;
    void delete() throws SQLException;
    List<T> select() throws SQLException;
}
