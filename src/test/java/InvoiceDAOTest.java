import dao.InvoiceDAO;
import entities.Invoice;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

public class InvoiceDAOTest {

    public InvoiceDAO invoiceDAO() throws SQLException {
        Connection connection;
        try {
            connection =  DriverManager.getConnection(ConnectionUtils.URL.value,
                    ConnectionUtils.USER.value, ConnectionUtils.PASSWORD.value);
            return new InvoiceDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new SQLException();
    }

    @Test
    public void get() throws SQLException {
        Invoice expected = new Invoice(2, new Date(Calendar.YEAR + 119, 2, 2), 2);
        assertEquals(expected, invoiceDAO().get(2));
    }

    @Test
    public void getAll() throws SQLException {
        InvoiceDAO invoiceDAO = invoiceDAO();
        List<Invoice> expected = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            expected.add(new Invoice(i, new Date(Calendar.YEAR + 119, i, i), i));
        }
        for (int i = 11; i < 21; i++) {
            expected.add(new Invoice(i, new Date(Calendar.YEAR + 119, i , i), i-10));
        }
        assertEquals(expected, invoiceDAO.getAll());
    }

    @Test
    public void save() throws SQLException {
        InvoiceDAO invoiceDAO = invoiceDAO();

        invoiceDAO.save(new Invoice(21, new Date(Calendar.YEAR+118, 11,12), 11));
        Invoice expected = new Invoice(21, new Date(Calendar.YEAR+118, 11,12), 11);
        assertEquals(expected, invoiceDAO.get(21));
    }

    @Test
    public void update() throws SQLException {
        InvoiceDAO invoiceDAO = invoiceDAO();

        invoiceDAO.update(new Invoice(21, new Date(Calendar.YEAR+119, 1,1), 11));
        Invoice expected = new Invoice(21, new Date(Calendar.YEAR+119, 1,1), 11);
        assertEquals(expected, invoiceDAO.get(21));
    }

    @Test(expected = IllegalStateException.class)
    public void delete() throws SQLException {
        InvoiceDAO invoiceDAO = invoiceDAO();

        invoiceDAO.delete(invoiceDAO.get(23));
        assertNotNull(invoiceDAO.get(21));
    }
}