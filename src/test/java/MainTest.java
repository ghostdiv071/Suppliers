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

    public Connection setConnection() throws SQLException {
        Connection connection;
        try {
            connection =  DriverManager.getConnection(ConnectionUtils.URL.value,
                    ConnectionUtils.USER.value, ConnectionUtils.PASSWORD.value);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new SQLException();
    }

    @Test
    public void firstQuery() throws SQLException {
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] result = new int[expected.length];

        ArrayList<Integer> temp = Main.executeFirst(setConnection());
        for (int i = 0; i < temp.size(); i++) {
            result[i] = temp.get(i);
        }

        assertArrayEquals(expected, result);
    }

    @Test
    public void secondQuery() throws SQLException {
        int[] expected = {4, 5, 6, 7, 8, 9, 10};
        long limit = 1700;
        int[] result = new int[expected.length];

        ArrayList<Integer> temp = Main.executeSecond(setConnection(), limit);
        for (int i = 0; i < temp.size(); i++) {
            result[i] = temp.get(i);
        }

        assertArrayEquals(expected, result);
    }

    @Test
    public void thirdQuery() throws SQLException {
        long[] expected = {7500, 10800, 14700, 19200};
        long[] result = new long[expected.length];
        Date date = new Date(Calendar.YEAR+119, 4, 5);
        long interval = 150;

        ArrayList<Long> temp = Main.executeThird(setConnection(), date, interval);
        for (int i = 0; i < temp.size(); i++) {
            result[i] = temp.get(i);
        }

        assertArrayEquals(expected, result);
    }

    @Test
    public void fourthQuery() throws SQLException {
        BigDecimal expected = new BigDecimal("13050.000000000000");
        Date date = new Date(Calendar.YEAR+119, 4,5);
        long interval = 150;
        BigDecimal result;

        result = Main.executeFourth(setConnection(), date, interval);

        assertEquals(expected, result);
    }

    @Test
    public void fifthQuery() throws SQLException {
        String[] expected = {"name10", "name12", "name14", "name16"};
        String[] result = new String[expected.length];
        Date date = new Date(Calendar.YEAR+119, 4,5);
        long interval = 150;

        ArrayList<String > temp = Main.executeFifth(setConnection(), date, interval);
        for (int i = 0; i < temp.size(); i++) {
            result[i] = temp.get(i);
        }

        assertArrayEquals(expected, result
        );
    }

}