package NewGreenhouse;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PhvaluesDAODB {

    public PhvaluesDAODB() { }

    public Connection Connect () {
        Connection con = null;
        try {
            Properties p = new Properties();
            p.load(new FileInputStream("src/main/java/NewGreenhouse/Settings.properties"));
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(p.getProperty("connectionString"),
                    p.getProperty("username"),
                    p.getProperty("password"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return con;
    }

    public List<Phvalues> GetLatestWeekValues() throws SQLException {
        List<Phvalues> phvaluesList = new ArrayList<Phvalues>();

        Connection con = Connect();

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, hum, temp, lum, kwh_p_day, time, record_type FROM phvalues as a\n" +
                "WHERE time >= DATE_SUB(current_timestamp(), INTERVAL 7 DAY)\n" +
                "UNION\n" +
                "(SELECT id, hum, temp, lum, kwh_p_day, time, record_type FROM phvalues as b\n" +
                "WHERE time <= DATE_SUB(current_timestamp(), INTERVAL 7 DAY) ORDER BY time DESC LIMIT 1)\n" +
                "ORDER BY time");

        while (rs.next()) {
            Phvalues temp = new Phvalues();
            temp.setId(rs.getInt("id"));
            temp.setHum(rs.getInt("hum"));
            temp.setTemp(rs.getFloat("temp"));
            temp.setLum(rs.getInt("lum"));
            temp.setKwhPDay(rs.getFloat("kwh_p_day"));
            temp.setTime(rs.getTimestamp("time"));
            temp.setRecordType(rs.getString("record_type"));
            phvaluesList.add(temp);
        }

        con.close();
        return phvaluesList;
    }

    public Phvalues getLatestValues() throws SQLException {
        Phvalues phvalues = new Phvalues();

        Connection con = Connect();

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, hum, temp, lum, kwh_p_day, time, record_type FROM phvalues " +
                "WHERE time = (SELECT MAX(time) FROM phvalues)");

        while (rs.next()) {
            phvalues.setId(rs.getInt("id"));
            phvalues.setHum(rs.getInt("hum"));
            phvalues.setTemp(rs.getFloat("temp"));
            phvalues.setLum(rs.getInt("lum"));
            phvalues.setKwhPDay(rs.getFloat("kwh_p_day"));
            phvalues.setTime(rs.getTimestamp("time"));
            phvalues.setRecordType(rs.getString("record_type"));
        }
        con.close();
        return phvalues;
    }

    public boolean addPhvalues(Phvalues p) throws SQLException {
        String query = "insert into phvalues (id, hum, temp, lum, kwh_p_day, time, record_type) \n" +
                "values (null, ?, ?, ?, ?, now(), ?);";
        int rowChanged = 0;

        Connection con = Connect();

        PreparedStatement stmt = con.prepareStatement(query);

        stmt.setInt(1, p.getHum());
        stmt.setFloat(2, p.getTemp());
        stmt.setInt(3, p.getLum());
        stmt.setFloat(4, p.getKwhPDay());
        stmt.setString(5, p.getRecordType());

        rowChanged = stmt.executeUpdate();
        if (rowChanged == 1){
            con.close();
            return true;
        }

        con.close();
        return false;
    }
}
