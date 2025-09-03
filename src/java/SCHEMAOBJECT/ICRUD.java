/*
 * Interface for CRUD operations
 */
package SCHEMAOBJECT;

import java.sql.SQLException;
import java.util.List;

public interface ICRUD<T, ID> {
    List<T> getAll() throws SQLException;
    T getById(ID id) throws SQLException;
    boolean insert(T entity) throws SQLException;
    boolean update(T entity) throws SQLException;
    boolean delete(ID id) throws SQLException;
}