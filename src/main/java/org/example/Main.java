package org.example;


import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mysql";
        String user = "root";
        String password = "root";

        List<User> users = new ArrayList<>();

        try(FileWriter allUserFw = new FileWriter("allUsers.sql")) {
            try (Connection connection = DriverManager.getConnection(url, user, password);
                 Statement statement = connection.createStatement()) {

                // Execute the first query
                try (ResultSet usersResultSet = statement.executeQuery("SELECT user, host FROM mysql.user")) {
                    while (usersResultSet.next()) {
                        String username = usersResultSet.getString("user");
                        String host = usersResultSet.getString("host");
                        users.add(new User(username, host));
                    }
                }

                for (User u : users) {
                    System.out.println(u);
                    try (FileWriter fileWriter = new FileWriter(u.username + "-" + u.host + ".sql")) {
                        var query = String.format("show create user '%s'@'%s'", u.username, u.host);
                        try (ResultSet createUserRs = statement.executeQuery(query)) {
                            while (createUserRs.next()) {
                                String sqlText = createUserRs.getString(1) + ";\n";
                                fileWriter.write(sqlText);
                                allUserFw.write(sqlText);
                            }
                        }

                        query = String.format("SHOW GRANTS FOR '%s'@'%s'", u.username, u.host);
                        try (ResultSet grantsResultSet = statement.executeQuery(query)) {
                            while (grantsResultSet.next()) {
                                String sqlText = grantsResultSet.getString(1) + ";\n";
                                fileWriter.write(sqlText);
                                allUserFw.write(sqlText);
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}