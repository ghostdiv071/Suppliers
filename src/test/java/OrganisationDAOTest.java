import dao.OrganisationDAO;
import entities.Organisation;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrganisationDAOTest {

    public OrganisationDAO organisationDAO() throws SQLException {
        Connection connection;
        try {
            connection =  DriverManager.getConnection(ConnectionUtils.URL.value,
                    ConnectionUtils.USER.value, ConnectionUtils.PASSWORD.value);
            return new OrganisationDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new SQLException();
    }

    @Test
    public void get() throws SQLException {
        Organisation expected = new Organisation(2, "name2", 2, "2");
        assertEquals(expected, organisationDAO().get(2));
    }

    @Test
    public void getAll() throws SQLException {
        List<Organisation> expected = new ArrayList<>();
        for (int i = 1; i < 12; i++) {
            expected.add(new Organisation(i,"name" + i, i, String.valueOf(i)));
        }
        assertEquals(expected, organisationDAO().getAll());
    }

    @Test
    public void save() throws SQLException {
        OrganisationDAO organisationDAO = organisationDAO();

        organisationDAO.save(new Organisation(12,"testName", 111, "111"));
        Organisation expected = new Organisation(12, "testName", 111, "111");
        assertEquals(expected, organisationDAO.get(12));
    }

    @Test
    public void update() throws SQLException {
        OrganisationDAO organisationDAO = organisationDAO();

        organisationDAO.update(new Organisation(12,"test", 110, "110"));
        Organisation expected = new Organisation(12, "test", 110, "110");
        assertEquals(expected, organisationDAO.get(12));
    }

    @Test(expected = IllegalStateException.class)
    public void delete() throws SQLException {
        OrganisationDAO organisationDAO = organisationDAO();

        organisationDAO.delete(new Organisation(12, "test", 110, "110"));
        assertNull(organisationDAO.get(12));
    }
}