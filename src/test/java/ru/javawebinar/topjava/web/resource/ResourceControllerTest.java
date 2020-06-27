package ru.javawebinar.topjava.web.resource;

import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceControllerTest extends AbstractControllerTest {

    @Test
    void getCss() throws Exception {
        mockMvc.perform(get("/resources/css/style.css")
                .contentType("text/css"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}