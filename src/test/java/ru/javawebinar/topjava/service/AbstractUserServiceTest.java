package ru.javawebinar.topjava.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.TestData.assertMatch;

public abstract class AbstractUserServiceTest extends AbstractServiceTest {

    @Autowired
    protected UserService service;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() throws Exception {
        cacheManager.getCache("users").clear();
    }

    @Test
    void create() {
        User newUser = getNew();
        User created = service.create(new User(newUser));
        Integer newId = created.getId();
        newUser.setId(newId);
        assertMatch(USER_IGNORE, created, newUser);
        assertMatch(USER_IGNORE, service.get(newId), newUser);
    }

    @Test
    void duplicateMailCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.ROLE_USER)));
    }

    @Test
    void delete() {
        service.delete(USER_ID);
        assertThrows(NotFoundException.class, () ->
                service.delete(USER_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }

    @Test
    void get() {
        User user = service.get(ADMIN_ID);
        assertMatch(USER_IGNORE, user, ADMIN);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void getByEmail() {
        User user = service.getByEmail("admin@gmail.com");
        assertMatch(USER_IGNORE, user, ADMIN);
    }

    @Test
    void update() {
        User updated = getUpdated();
        service.update(new User(updated));
        assertMatch(USER_IGNORE, service.get(USER_ID), updated);
    }

    @Test
    void getAll() {
        List<User> all = service.getAll();
        assertMatch(USER_IGNORE, all, ADMIN, USER);
    }
}