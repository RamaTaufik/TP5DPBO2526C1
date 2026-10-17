import java.sql.*;

public class Database {
    private Connection connection;
    private Statement statement;

    public Database() {
        try {
            // Ubah bagian ini jika ada konfigurasi MySQL yang berbeda/ingin menggunakan user lain
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost: 3306/db_people",
                "root",
                ""
            );
            statement = connection.createStatement();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet selectQuery(String sql) {
        try {
            statement.executeQuery(sql);

            return statement.getResultSet();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insertUpdateDeleteQuery(String sql) {
        try {
            return statement.executeUpdate(sql);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Statement getStatement() {
        return statement;
    }
}
