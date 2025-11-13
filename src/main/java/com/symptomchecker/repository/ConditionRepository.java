package com.symptomchecker.repository;

import com.symptomchecker.config.DatabaseConfig;
import com.symptomchecker.model.Condition;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository responsible for retrieving condition data from MySQL.
 */
public class ConditionRepository {

    private static final String LOAD_CONDITIONS_SQL =
        "SELECT c.id, c.name, c.advice, cs.symptom " +
            "FROM conditions c " +
            "LEFT JOIN condition_symptoms cs ON c.id = cs.condition_id " +
            "ORDER BY c.id";

    /**
     * Load all conditions defined in the database along with their associated symptoms.
     *
     * @return list of conditions; never {@code null}
     */
    public List<Condition> loadConditions() {
        Map<Integer, ConditionBuilder> builders = new LinkedHashMap<>();

        try (Connection connection = DriverManager.getConnection(
            DatabaseConfig.DB_URL,
            DatabaseConfig.DB_USER,
            DatabaseConfig.DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(LOAD_CONDITIONS_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String advice = resultSet.getString("advice");
                String symptom = resultSet.getString("symptom");

                ConditionBuilder builder = builders.computeIfAbsent(id, key -> new ConditionBuilder(name, advice));
                if (symptom != null) {
                    builder.addSymptom(symptom);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load conditions from the database.", e);
        }

        List<Condition> conditions = new ArrayList<>(builders.size());
        for (ConditionBuilder builder : builders.values()) {
            conditions.add(builder.build());
        }
        return conditions;
    }

    private static class ConditionBuilder {
        private final String name;
        private final String advice;
        private final List<String> symptoms = new ArrayList<>();

        ConditionBuilder(String name, String advice) {
            this.name = name;
            this.advice = advice;
        }

        void addSymptom(String symptom) {
            symptoms.add(symptom);
        }

        Condition build() {
            return new Condition(name, symptoms, advice);
        }
    }
}

