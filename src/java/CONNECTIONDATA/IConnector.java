/*
 * Interface for database connection management
 */
package CONNECTIONDATA;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnector {
    /**
     * Opens a new database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    Connection Open() throws SQLException;

    /**
     * Closes the database connection
     * @param conn Connection to close
     * @throws SQLException if closing fails
     */
    void Close(Connection conn) throws SQLException;

    /**
     * Gets the current connection
     * @return Connection object
     */
    Connection getConnection();
}