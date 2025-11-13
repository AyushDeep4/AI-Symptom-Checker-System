package com.symptomchecker.config;

/**
 * Holds MySQL connection details for JDBC usage.
 *
 * <p>
 * Update the {@code DB_URL}, {@code DB_USER}, and {@code DB_PASSWORD} constants to match
 * your local MySQL setup. For production code, consider loading these values from
 * environment variables or a configuration file instead of hard-coding them.
 * </p>
 */
public final class DatabaseConfig {

    private DatabaseConfig() {
        // utility class
    }

    /**
     * JDBC connection URL. Example for local MySQL:
     * jdbc:mysql://localhost:3306/symptom_checker?useSSL=false&serverTimezone=UTC
     */
    public static final String DB_URL =
        System.getProperty("SYMPTOM_DB_URL", "jdbc:mysql://localhost:3306/symptom_checker?useSSL=false&serverTimezone=UTC");

    /**
     * Database username. Replace with your MySQL username or supply via system property SYMPTOM_DB_USER.
     */
    public static final String DB_USER =
        System.getProperty("SYMPTOM_DB_USER", "root");

    /**
     * Database password. Replace with your MySQL password or supply via system property SYMPTOM_DB_PASSWORD.
     */
    public static final String DB_PASSWORD =
        System.getProperty("SYMPTOM_DB_PASSWORD", "password");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL JDBC driver not found on the classpath.", e);
        }
    }
}

