package dao;

import entities.Nomenclature;
import entities.Organisation;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public final class NomenclatureDAO implements DAO<Nomenclature> {

    final Connection connection;

    @Override
    public Nomenclature get(int id) {
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT id, internal_code, name FROM nomenclature WHERE id = " + id)) {
                while (rs.next()) {
                    return new Nomenclature(rs.getInt("id"),
                            rs.getLong("internal_code"),
                            rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        throw new IllegalStateException("Record with id " + id + "not found");
    }

    @Override
    public List<Nomenclature> getAll() {
        final List<Nomenclature> result = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT id, internal_code, name FROM nomenclature")) {
                while (rs.next()) {
                    result.add(new Nomenclature(rs.getInt("id"),
                            rs.getLong("internal_code"),
                            rs.getString("name")));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public void save(Nomenclature entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO nomenclature(internal_code, name) VALUES(?,?)")
        ){
            int count = 1;
            preparedStatement.setLong(count++, entity.getInternalCode());
            preparedStatement.setString(count, entity.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Nomenclature entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE nomenclature SET name = ?, taxpayer_id = ?, checking_account = ? WHERE id = ?"
        )
        ){
            int count = 1;
            preparedStatement.setLong(count++, entity.getInternalCode());
            preparedStatement.setString(count++, entity.getName());
            preparedStatement.setInt(count, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Nomenclature entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM nomenclature WHERE id = ?")
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
