import dao.*;
import entities.*;

import org.flywaydb.core.Flyway;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class Main {

    public static void main(String[] args) {
        final Flyway flyway = Flyway.configure()
                .dataSource(ConnectionUtils.URL.value,
                        ConnectionUtils.USER.value,
                        ConnectionUtils.PASSWORD.value)
                .locations("db")
                .load();
        flyway.clean();
        flyway.migrate();

        try (Connection connection = DriverManager.getConnection(ConnectionUtils.URL.value,
                ConnectionUtils.USER.value, ConnectionUtils.PASSWORD.value)) {
            System.out.println("Connection is Ok");

            final OrganisationDAO organisationDAO = new OrganisationDAO(connection);
            final InvoiceDAO invoiceDAO = new InvoiceDAO(connection);
            final InvoiceItemDAO itemDAO = new InvoiceItemDAO(connection);
            final NomenclatureDAO nomenclatureDAO = new NomenclatureDAO(connection);

            createData(organisationDAO, invoiceDAO, itemDAO, nomenclatureDAO);

        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }

        System.out.println("Completed!");
    }

    public static ArrayList<Integer> executeFirst(Connection connection) throws SQLException {

        final String query = "select organisation.id, organisation.name\n" +
                "\tfrom invoice_item \n" +
                "\tjoin invoice on invoice_item.invoice_id = invoice.id\n" +
                "\tjoin organisation on invoice.organisation_id = organisation.id\n" +
                "\torder by invoice_item.amount\n" +
                "\tlimit 10;";

        return tryConnectAndExecute(connection, query);
    }

    public static ArrayList<Integer> executeSecond(Connection connection, long limit) throws SQLException {

        final String query = "select organisation.id, organisation.name\n" +
                "\tfrom invoice_item \n" +
                "\tjoin invoice on invoice_item.invoice_id = invoice.id\n" +
                "\tjoin organisation on invoice.organisation_id = organisation.id\n" +
                "\tgroup by organisation.id\n" +
                "\thaving sum(invoice_item.price) > '" + limit + "' \n" +
                "\torder by organisation.id;";

        return tryConnectAndExecute(connection, query);
    }

    private static ArrayList<Integer> tryConnectAndExecute(Connection connection, String query) throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    ids.add(id);
                    //System.out.println("id:" + id + " name: " + name);
                }
            }
        }

        return ids;
    }

    public static ArrayList<Long> executeThird(Connection connection, Date date, long interval) throws SQLException {

        final String query = "select invoice.id, invoice_item.amount, invoice_item.price," +
                "invoice_item.price*invoice_item.amount as total\n" +
                "\tfrom invoice\n" +
                "\tjoin invoice_item on invoice.id = invoice_item.id\n" +
                "\twhere invoice.date >= '" + date + "'::date and invoice.date < ('" + date + "'::date + '" + interval + " day'::interval);";

        ArrayList<Long> totals = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int amount = rs.getInt("amount");
                    long price = rs.getLong("price");
                    long total = amount * price;
                    totals.add(total);
                    //System.out.println("id:" + id + " amount:" + amount + " price:" + price + " total:" + total);
                }
            }
        }

        return totals;
    }

    public static BigDecimal executeFourth(Connection connection, Date date, long interval) throws SQLException {

        final String query = "select avg(invoice_item.price*invoice_item.amount) as average\n" +
                "\tfrom invoice\n" +
                "\tjoin invoice_item on invoice.id = invoice_item.id\n" +
                "\twhere invoice.date >= '" + date +"'::date and invoice.date < ('" + date +"'::date + '" + interval + " day'::interval);";

        BigDecimal average = new BigDecimal(0);

        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    average = rs.getBigDecimal("average");
                    //System.out.println("average: " + average);
                }
            }
        }

        return average;
    }

    public static ArrayList<String> executeFifth(Connection connection, Date date, long interval) throws SQLException {

        final String query = "select nomenclature.name as nomenclature, organisation.name as organisation\n" +
                "\tfrom organisation\n" +
                "\tjoin invoice on organisation.id = invoice.organisation_id\n" +
                "\tjoin invoice_item on invoice.id = invoice_item.invoice_id\n" +
                "\tjoin nomenclature on invoice_item.nomenclature = nomenclature.id\n" +
                "\twhere (invoice.date >= '" + date + "'::date and invoice.date < ('" + date + "'::date + '" + interval +" day'::interval)) \n" +
                "\tgroup by nomenclature.name, organisation.name, invoice.date\n" +
                "\torder by organisation.name;";

        ArrayList<String> nomenclatures = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    String nomenclature = rs.getString("nomenclature");
                    String organisation = rs.getString("organisation");
                    nomenclatures.add(nomenclature);
                    //System.out.println("nomenclature: " + nomenclature + " organisation: " + organisation);
                }
            }
        }

        return nomenclatures;
    }

    private static void createData(OrganisationDAO organisationDAO, InvoiceDAO invoiceDAO, InvoiceItemDAO itemDAO, NomenclatureDAO nomenclatureDAO) {
        for (int i = 1; i < 12; i++) {
            organisationDAO.save(new Organisation("name" + i, i, String.valueOf(i)));
        }

        for (int i = 1; i < 11; i++) {
            invoiceDAO.save(new Invoice(new Date(Calendar.YEAR + 119, i, i), organisationDAO.get(i).getId()));
        }

        for (int i = 11; i < 21; i++) {
            invoiceDAO.save(new Invoice(new Date(Calendar.YEAR + 119, i , i), organisationDAO.get(i-10).getId()));
        }

        for (int i = 1; i < 11; i++) {
            nomenclatureDAO.save(new Nomenclature(i*10, "name" + i*2));
        }

        for (int i = 1; i < 11; i++) {
            itemDAO.save(new InvoiceItem(i*100, i*3, nomenclatureDAO.get(i).getId(), invoiceDAO.get(i).getId()));
        }

        for (int i = 11; i < 21; i++) {
            itemDAO.save(new InvoiceItem(i*100, i*3, nomenclatureDAO.get(i-10).getId(), invoiceDAO.get(i).getId()));
        }
    }

}
