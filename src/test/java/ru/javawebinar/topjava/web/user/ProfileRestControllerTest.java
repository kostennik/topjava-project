package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.AbstractRestController;

import static ru.javawebinar.topjava.TestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.web.user.ProfileRestController.REST_URL;

class ProfileRestControllerTest extends AbstractRestController<User> {

    @Autowired
    private UserService userService;

    public ProfileRestControllerTest() {
        super(REST_URL, User.class, IGNORE_FIELDS);
    }

    @Test
    void get() throws Exception {
        super.get("", USER);
    }

    @Test
    void delete() throws Exception {
        super.delete("", () -> userService.get(USER_ID));
    }

    @Test
    void update() throws Exception {
        User updated = UserTestData.getUpdated();
        super.update("", updated);
        assertMatch(IGNORE_FIELDS, userService.get(USER_ID), updated);
    }
}