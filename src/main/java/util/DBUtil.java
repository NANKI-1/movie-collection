package util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBUtil {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    // 静态代码块，加载配置文件
    static {
        try {
            // 1. 读取配置文件
            InputStream is = DBUtil.class.getClassLoader()
                    .getResourceAsStream("db.properties");
            Properties props = new Properties();
            props.load(is);

            // 2. 获取配置
            driver = props.getProperty("jdbc.driver");
            url = props.getProperty("jdbc.url");
            username = props.getProperty("jdbc.username");
            password = props.getProperty("jdbc.password");

            // 3. 加载驱动
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // 关闭连接（查询用）
    public static void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 关闭连接（增删改用）
    public static void close(PreparedStatement pstmt, Connection conn) {
        close(null, pstmt, conn);
    }

    // 测试连接
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("数据库连接成功！");
        } catch (SQLException e) {
            System.out.println("数据库连接失败！");
            e.printStackTrace();
        }
    }
}