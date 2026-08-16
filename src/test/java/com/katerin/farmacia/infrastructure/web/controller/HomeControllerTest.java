package com.katerin.farmacia.infrastructure.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHealthcheckEndopoint() throws Exception{
        mockMvc.perform(get("/healtcheck"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UP"));
        }

    @Test
    void testGetMedicationsEndpoint() throws Exception{
        mockMvc.perform(get("/api/v1/medications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Paracetamol"))
                .andExpect(jsonPath("$[1].name").value("Ibuprofeno"))
                .andExpect(jsonPath("$[2].name").value("Amoxicilina"));
        }

    @Test
    void testGetMedicationByIdSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/medications/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.name").value("Ibuprofeno"))
                .andExpect(jsonPath("$.price").value(1200));
        }

    @Test
    void testGetMedicationByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/medications/99"))
                .andExpect(status().isNotFound());
    }
    
}
