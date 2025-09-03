/*
 * Database connection class for QUAN_LY_NHA_HANG_DAC_SAN_VIET_NAM
 */
package CONNECTIONDATA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CONNECTIONSQLSERVER implements IConnector {
    
    private final String UserName;
    private final String PassWord;
    private final String DataBaseName;
    private final String ServerName;
    private final String DriverClass;
    private final String DriverURL;
    private Connection conn = null;

    public CONNECTIONSQLSERVER() {
        try {
            this.UserName = "sa";
            this.PassWord = "123456";
            this.DataBaseName = "QUAN_LY_NHA_HANG_DAC_SAN_VIET_NAM";
            this.ServerName = "DESKTOP-534RB4A";
            this.DriverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            this.DriverURL = "jdbc:sqlserver://" + ServerName + ":1433;databaseName=" + DataBaseName + 
                            ";user=" + UserName + ";password=" + PassWord + 
                            ";encrypt=false;trustServerCertificate=false";
            Class.forName(DriverClass);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CONNECTIONSQLSERVER.class.getName()).log(Level.SEVERE, "Failed to load SQL Server driver", ex);
            throw new RuntimeException("Failed to initialize database driver", ex);
        }
    }

    public CONNECTIONSQLSERVER(String UserName, String PassWord, String DataBaseName, String ServerName, String DriverClass, String DriverURL) {
        this.UserName = UserName;
        this.PassWord = PassWord;
        this.DataBaseName = DataBaseName;
        this.ServerName = ServerName;
        this.DriverClass = DriverClass;
        this.DriverURL = DriverURL;
    }

    @Override
    public Connection Open() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(DriverURL);
            System.out.println("Kết nối thành công!");
        }
        return conn;
    }

    @Override
    public void Close(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
            System.out.println("Đóng kết nối thành công!");
        }
    }

   @Override
    public Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(DriverURL, UserName, PassWord);
            System.out.println("Tạo kết nối cơ sở dữ liệu thành công");
            return conn;
        } catch (SQLException e) {
            System.err.println("Lỗi tạo kết nối: " + e.getMessage());
            return null;
        }
    }
}