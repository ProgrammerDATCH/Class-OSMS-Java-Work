package com.osms.model;

import com.osms.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseModel {
    protected Connection connection;
    protected PreparedStatement preparedStatement;
    protected ResultSet resultSet;

    protected void openConnection() throws SQLException {
        connection = DatabaseUtil.getConnection();
    }

    protected void closeConnection() {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Object> executeQuery(String query, Object... params) throws SQLException {
        List<Object> results = new ArrayList<>();
        try {
            openConnection();
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                results.add(mapResultSet(resultSet));
            }
        } finally {
            closeConnection();
        }
        return results;
    }

    public int executeUpdate(String query, Object... params) throws SQLException {
        try {
            openConnection();
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.executeUpdate();
        } finally {
            closeConnection();
        }
    }

    protected abstract Object mapResultSet(ResultSet resultSet) throws SQLException;
} 