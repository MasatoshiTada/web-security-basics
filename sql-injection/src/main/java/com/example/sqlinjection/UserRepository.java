package com.example.sqlinjection;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcClient jdbcClient;

    public UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<User> findByUsernameAndPasswordBad(String email, String password) {
        String sql = "SELECT email, name, password" +
                " FROM users" +
                " WHERE email = '" + email + "'" +
                " AND password = '" + password + "'";
        List<User> userList = jdbcClient.sql(sql)
                .query(new DataClassRowMapper<>(User.class))
                .list();
        if (userList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(userList.get(0));
    }

    public Optional<User> findByUsernameAndPassword(String email, String password) {
        String sql = """
                SELECT email, name, password
                FROM users
                WHERE email = ?
                AND password = ?
                """;
         Optional<User> userOptional = jdbcClient.sql(sql)
                .param(email)
                .param(password)
                .query(new DataClassRowMapper<>(User.class))
                .optional();
        return userOptional;
    }
}
