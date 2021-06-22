package pl.adamd.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.adamd.model.Task;
import pl.adamd.model.TaskRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerLightIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository repo;


    @Test
    void httpGet_returnsGivenTask() throws Exception {
        // given
        when(repo.findById(anyInt()))
                .thenReturn(Optional.of(new Task("foo", LocalDateTime.now())));

        // when + then
        mockMvc.perform(get("/tasks/123"))
                .andDo(print())
                .andExpect(content().string(containsString("\"description\":\"foo\"")));
    }

    @Test
    void httpPost_createTask() throws Exception {
        // given
        when(repo.save(any()))
                .thenReturn(new Task("bro", LocalDateTime.now()));

        String productJson = "{\n" +
                "  \"description\": \"bro\"\n" +
                "}";

        // when + then
        MockHttpServletRequestBuilder requestBuilder = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(content().string(containsString("bro")));
    }

}
