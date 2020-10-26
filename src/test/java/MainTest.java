import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.*;

public class MainTest {

    final static String dbName = "Suppliers";
    final static String url = "jdbc:postgresql://127.0.0.1:5432/" + dbName;
    final static String user = "postgres";
    final static String password = "anna";

    @Test
    public void firstQuery() throws SQLException {
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] result = new int[expected.length];

        try (Connection connection = DriverManager.getConnection(url, user, password)){
            ArrayList<Integer> temp = Main.executeFirst(connection);
            for (int i = 0; i < temp.size(); i++) {
                result[i] = temp.get(i);
            }
        }

        assertArrayEquals(expected, result);
    }

    @Test
    public void secondQuery() throws SQLException {
        int[] expected = {4, 5, 6, 7, 8, 9, 10};
        long limit = 1700;
        int[] result = new int[expected.length];

        try (Connection connection = DriverManager.getConnection(url, user, password)){
            ArrayList<Integer> temp = Main.executeSecond(connection, limit);
            for (int i = 0; i < temp.size(); i++) {
                result[i] = temp.get(i);
            }
        }

        assertArrayEquals(expected, result);
    }

    @Test
    public void thirdQuery() throws SQLException {
        long[] expected = {7500, 10800, 14700, 19200};
        long[] result = new long[expected.length];
        Date date = new Date(Calendar.YEAR+119, 4, 5);
        long interval = 150;

        try (Connection connection = DriverManager.getConnection(url, user, password)){
            ArrayList<Long> temp = Main.executeThird(connection, date, interval);
            for (int i = 0; i < temp.size(); i++) {
                result[i] = temp.get(i);
            }
        }

        assertArrayEquals(expected, result);
    }

    @Test
    public void fourthQuery() throws SQLException {
        BigDecimal expected = new BigDecimal("13050.000000000000");
        Date date = new Date(Calendar.YEAR+119, 4,5);
        long interval = 150;
        BigDecimal result = new BigDecimal("0");

        try (Connection connection = DriverManager.getConnection(url, user, password)){
            result = Main.executeFourth(connection, date, interval);
        }

        assertEquals(expected, result);
    }

    @Test
    public void fifthQuery() throws SQLException {
        String[] expected = {"name10", "name12", "name14", "name16"};
        String[] result = new String[expected.length];
        Date date = new Date(Calendar.YEAR+119, 4,5);
        long interval = 150;

        try (Connection connection = DriverManager.getConnection(url, user, password)){
            ArrayList<String > temp = Main.executeFifth(connection, date, interval);
            for (int i = 0; i < temp.size(); i++) {
                result[i] = temp.get(i);
            }
        }

        assertArrayEquals(expected, result);
    }

}