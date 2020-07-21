package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.AbstractRestController;

import static ru.javawebinar.topjava.TestData.assertMatch;
import static ru.javawebinar.topjava.TestData.contentJson;
import static ru.javawebinar.topjava.UserTestData.*;

class AdminRestControllerTest extends AbstractRestController<User> {

    private static final String REST_URL = AdminRestController.REST_URL + '/';

    @Autowired
    private UserService userService;

    public AdminRestControllerTest() {
        super(REST_URL, User.class);
    }

    @Test
    void get() throws Exception {
        super.get(String.valueOf(ADMIN_ID), ADMIN);
    }

    @Test
    void getByEmail() throws Exception {
        super.get("by?email=" + USER.getEmail(), USER);
    }

    @Test
    void delete() throws Exception {
        super.delete(String.valueOf(USER_ID), () -> userService.get(USER_ID));
    }

    @Test
    void update() throws Exception {
        User updated = UserTestData.getUpdated();
        super.update(String.valueOf(USER_ID), updated);
        assertMatch(userService.get(USER_ID), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        User newUser = UserTestData.getNew();
        int newId = super.createWithLocation(newUser);
        assertMatch(userService.get(newId), newUser);
    }

    @Test
    void getAll() throws Exception {
        super.getAll(contentJson(User.class, ADMIN, USER));
    }
}