package dao;

import entities.Organisation;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public final class OrganisationDAO implements DAO<Organisation> {

    final Connection connection;

    @Override
    public Organisation get(int id) {
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT id, name, taxpayer_id, checking_account FROM organisation WHERE id = " + id)) {
                while (rs.next()) {
                    return new Organisation(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getLong("taxpayer_id"),
                            rs.getString("checking_account"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        throw new IllegalStateException("Record with id " + id + "not found");

    }

    @Override
    public List<Organisation> getAll() {
        final List<Organisation> result = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT id, name, taxpayer_id, checking_account FROM organisation")) {
                while (rs.next()) {
                    result.add(new Organisation(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getLong("taxpayer_id"),
                            rs.getString("checking_account")));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public void save(Organisation entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO organisation(name, taxpayer_id, checking_account) VALUES(?,?,?)")
        ){
            int count = 1;
            preparedStatement.setString(count++, entity.getName());
            preparedStatement.setLong(count++, entity.getTaxpayerID());
            preparedStatement.setString(count, entity.getCheckingAccount());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Organisation entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE organisation SET name = ?, taxpayer_id = ?, checking_account = ? WHERE id"
        )
        ){
            int count = 1;
            preparedStatement.setInt(count++, entity.getId());
            preparedStatement.setString(count++, entity.getName());
            preparedStatement.setLong(count++, entity.getTaxpayerID());
            preparedStatement.setString(count, entity.getCheckingAccount());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Organisation entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM organisation WHERE id = ?")
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
