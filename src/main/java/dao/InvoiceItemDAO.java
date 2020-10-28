package dao;

import entities.InvoiceItem;
import entities.Organisation;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public final class InvoiceItemDAO implements DAO<InvoiceItem>{

    final Connection connection;

    @Override
    public InvoiceItem get(int id) {
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT id, price, amount, nomenclature, invoice_id FROM invoice_item WHERE id = " + id)) {
                while (rs.next()) {
                    return new InvoiceItem(rs.getInt("id"),
                            rs.getLong("price"),
                            rs.getInt("amount"),
                            rs.getInt("nomenclature"),
                            rs.getInt("invoice_id"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        throw new IllegalStateException("Record with id " + id + "not found");
    }

    @Override
    public List<InvoiceItem> getAll() {
        final List<InvoiceItem> result = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT id, price, amount, nomenclature, invoice_id FROM invoice_item")) {
                while (rs.next()) {
                    result.add(new InvoiceItem(rs.getInt("id"),
                            rs.getLong("price"),
                            rs.getInt("amount"),
                            rs.getInt("nomenclature"),
                            rs.getInt("invoice_id")));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public void save(InvoiceItem entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO invoice_item(price, amount, nomenclature, invoice_id) VALUES(?,?, ?, ?)")
        ){
            int count = 1;
            preparedStatement.setLong(count++, entity.getPrice());
            preparedStatement.setInt(count++, entity.getAmount());
            preparedStatement.setInt(count, entity.getNomenclature());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(InvoiceItem entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE invoice_item SET price = ?, amount = ?, nomenclature = ?, invoice_id = ? WHERE id = ?"
        )
        ){
            int count = 1;
            preparedStatement.setLong(count++, entity.getPrice());
            preparedStatement.setInt(count++, entity.getAmount());
            preparedStatement.setInt(count++, entity.getNomenclature());
            preparedStatement.setInt(count++, entity.getInvoice_ID());
            preparedStatement.setInt(count, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(InvoiceItem entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM invoice_item WHERE id = ?")
        ) {
            preparedStatement.setInt(1, entity.getId());
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Record with id = " + entity.getId() + " not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
