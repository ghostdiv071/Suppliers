package dao;

import entities.Invoice;
import entities.Organisation;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public final class InvoiceDAO implements DAO<Invoice> {

    final Connection connection;

    @Override
    public Invoice get(int id) {
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT id, date, organisation_id FROM invoice WHERE id = " + id)) {
                while (rs.next()) {
                    return new Invoice(rs.getInt("id"),
                            rs.getDate("date"),
                            rs.getInt("organisation_id"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        throw new IllegalStateException("Record with id " + id + "not found");
    }

    @Override
    public List<Invoice> getAll() {
        final List<Invoice> result = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT id, date, organisation_id FROM invoice")) {
                while (rs.next()) {
                    result.add(new Invoice(rs.getInt("id"),
                            rs.getDate("date"),
                            rs.getInt("organisation_id")));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public void save(Invoice entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO invoice(date, organisation_id) VALUES(?,?)")
        ){
            int count = 1;
            preparedStatement.setDate(count++, entity.getDate());
            preparedStatement.setInt(count, entity.getOrganisation_id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Invoice entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE invoice SET date = ?, organisation_id = ? WHERE id = ?"
        )
        ){
            int count = 1;
            preparedStatement.setDate(count++, entity.getDate());
            preparedStatement.setInt(count++, entity.getOrganisation_id());
            preparedStatement.setInt(count, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Invoice entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM invoice WHERE id = ?")
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
