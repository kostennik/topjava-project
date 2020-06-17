package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        } else {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        }
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)",
                user.getRoles(),
                user.getRoles().size(),
                (ps, role) -> {
                    ps.setInt(1, user.getId());
                    ps.setString(2, role.name());
                });
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        Map<Integer, User> userMap = new HashMap<>();
        jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON u.id=r.user_id WHERE id=?", getHandler(userMap), id);
        return userMap.get(id);
    }

    @Override
    public User getByEmail(String email) {
        Map<Integer, User> userMap = new HashMap<>();
        jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON u.id=r.user_id WHERE email=?", getHandler(userMap), email);
        return userMap.values().stream().findFirst().orElse(null);
    }

    @Override
    public List<User> getAll() {
        Map<Integer, User> userMap = new HashMap<>();
        jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON u.id=r.user_id ORDER BY name, email", getHandler(userMap));
        return new ArrayList<>(userMap.values());
    }

    private RowCallbackHandler getHandler(Map<Integer, User> userMap) {
        return rs -> {
            do {
                int userId = rs.getInt("id");
                User user;
                if ((user = userMap.get(userId)) != null) {
                    user.getRoles().add(Role.valueOf(rs.getString("role")));
                } else {
                    user = new User(
                            userId,
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getInt("calories_per_day"),
                            rs.getBoolean("enabled"),
                            rs.getDate("registered"),
                            Collections.singletonList(Role.valueOf(rs.getString("role"))));
                    userMap.put(userId, user);
                }
            } while (rs.next());
        };
    }
}
