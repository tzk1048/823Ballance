package szk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class OraDbConnect {
    static String hostname;
    static String sid;
    static String username;
    static String pass;
    static Connection con = null;

    public OraDbConnect(String chostname, String csid, String cusername, String cpass) {
        hostname = chostname;
        sid = csid;
        username = cusername;
        pass = cpass;
    }

    public boolean OraConnect() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@" + hostname + ":1521/" + sid,username,pass);
            return false;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
            return true;
        }
    }

    public ResultSet ExcecuteQuery(String sql) {

        try {
            Statement smt = con.createStatement();
            ResultSet rs = smt.executeQuery(sql);

            return rs;
            
        } catch (Exception e) {
            // TODO: handle exception

            return null;
        }

    }

    public Vector<Vector<String>> getSqlResult(ResultSet rs) {

        Vector<Vector<String>> vectors = new Vector<>();
        
        try {

            int size = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Vector<String> vec = new Vector<>();
                for (int i = 0; i < size; i++) {
                    vec.add(rs.getString(i+1));
                }
                vectors.add(vec);
            }
            return vectors;

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
            return null;
        }
    }

    public int getSqlResultCount(ResultSet rs) throws SQLException {
        return rs.getMetaData().getColumnCount();
    }

    public int insertDB(String sql) {
        int result = 0;
        try {
            Statement stm = con.createStatement();
            result = stm.executeUpdate(sql);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    public boolean DBCommit() {
        try {
            con.commit();
            return false;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("コミット失敗");
            return true;
        }
    }

    public boolean CloseDB() {
        try {
            con.close();
            return false;
        } catch (Exception e) {
            //TODO: handle exception
            return true;
        }
    }

}
