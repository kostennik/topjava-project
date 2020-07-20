package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.function.Executable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestData.assertMatch;
import static ru.javawebinar.topjava.TestData.contentJson;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.UserTestData.*;

public abstract class AbstractRestController<T extends AbstractBaseEntity> extends AbstractControllerTest {

    private final String restUrl;
    private final Class<T> tClass;
    private final String[] ignoredFields;

    public AbstractRestController(String restUrl, Class<T> tClass, String[] ignoredFields) {
        this.restUrl = restUrl;
        this.tClass = tClass;
        this.ignoredFields = ignoredFields;
    }

    protected void get(String param, T data) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(restUrl + param))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(ignoredFields, tClass, data));
    }

    protected void delete(String id, Executable exec) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(restUrl + id))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, exec);
    }

    protected void update(String id, T updated) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(restUrl + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
    }

    protected Integer createWithLocation(T newData) throws Exception {
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(restUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newData)))
                .andExpect(status().isCreated());

        T created = readFromJson(action, tClass);
        Integer newId = created.getId();
        newData.setId(newId);
        assertMatch(ignoredFields, created, newData);
        return newId;
    }

    protected void getAll(ResultMatcher matcher) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(restUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(matcher);
    }

}
