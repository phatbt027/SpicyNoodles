package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.SoftDrink;
import com.phastel.SpicyNoodles.service.SoftDrinkService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SoftDrinkControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SoftDrinkService softDrinkService;

    @InjectMocks
    private SoftDrinkController softDrinkController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SoftDrinkControllerTest() {
        mockMvc = MockMvcBuilders.standaloneSetup(softDrinkController).build();
    }

    @Test
    public void createSoftDrink_ShouldReturnCreatedSoftDrink() throws Exception {
        SoftDrink softDrink = new SoftDrink();
        softDrink.setName("Coca Cola");
        softDrink.setPrice(2.5);

        when(softDrinkService.createSoftDrink(any(SoftDrink.class))).thenReturn(softDrink);

        mockMvc.perform(post("/api/soft-drinks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(softDrink)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Coca Cola"))
                .andExpect(jsonPath("$.price").value(2.5));
    }

    @Test
    public void getSoftDrinkById_ShouldReturnSoftDrink() throws Exception {
        SoftDrink softDrink = new SoftDrink();
        softDrink.setId(1L);
        softDrink.setName("Coca Cola");

        when(softDrinkService.getSoftDrinkById(1L)).thenReturn(softDrink);

        mockMvc.perform(get("/api/soft-drinks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Coca Cola"));
    }

    @Test
    public void getAllSoftDrinks_ShouldReturnAllSoftDrinks() throws Exception {
        SoftDrink softDrink1 = new SoftDrink();
        softDrink1.setId(1L);
        softDrink1.setName("Coca Cola");

        SoftDrink softDrink2 = new SoftDrink();
        softDrink2.setId(2L);
        softDrink2.setName("Pepsi");

        List<SoftDrink> softDrinks = Arrays.asList(softDrink1, softDrink2);

        when(softDrinkService.getAllSoftDrinks()).thenReturn(softDrinks);

        mockMvc.perform(get("/api/soft-drinks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Coca Cola"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Pepsi"));
    }

    @Test
    public void updateSoftDrink_ShouldReturnUpdatedSoftDrink() throws Exception {
        SoftDrink softDrink = new SoftDrink();
        softDrink.setId(1L);
        softDrink.setName("Updated Coca Cola");
        softDrink.setPrice(3.0);

        when(softDrinkService.updateSoftDrink(any(SoftDrink.class))).thenReturn(softDrink);

        mockMvc.perform(put("/api/soft-drinks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(softDrink)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Coca Cola"));
    }

    @Test
    public void deleteSoftDrink_ShouldReturnOk() throws Exception {
        doNothing().when(softDrinkService).deleteSoftDrink(1L);

        mockMvc.perform(delete("/api/soft-drinks/1"))
                .andExpect(status().isOk());

        verify(softDrinkService, times(1)).deleteSoftDrink(1L);
    }

    @Test
    public void getSoftDrinkById_WhenSoftDrinkNotFound_ShouldReturnNotFound() throws Exception {
        when(softDrinkService.getSoftDrinkById(1L))
                .thenThrow(new IllegalArgumentException("Soft drink not found with id: 1"));

        mockMvc.perform(get("/api/soft-drinks/1"))
                .andExpect(status().isNotFound());
    }
} 