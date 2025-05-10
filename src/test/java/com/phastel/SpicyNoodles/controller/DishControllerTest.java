package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Dish;
import com.phastel.SpicyNoodles.service.DishService;
import com.phastel.SpicyNoodles.exception.RestExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
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
public class DishControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DishService dishService;

    @InjectMocks
    private DishController dishController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        dishController = new DishController(dishService);
        mockMvc = MockMvcBuilders.standaloneSetup(dishController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    public void createDish_ShouldReturnCreatedDish() throws Exception {
        Dish dish = new Dish();
        dish.setName("Spicy Noodles");
        dish.setPrice(10.5);
        dish.setDescription("Hot and spicy noodles");

        when(dishService.createDish(any(Dish.class))).thenReturn(dish);

        mockMvc.perform(post("/api/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dish)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Spicy Noodles"))
                .andExpect(jsonPath("$.price").value(10.5));
    }

    @Test
    public void getDishById_ShouldReturnDish() throws Exception {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Spicy Noodles");

        when(dishService.getDishById(1L)).thenReturn(dish);

        mockMvc.perform(get("/api/dishes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Spicy Noodles"));
    }

    @Test
    public void getAllDishes_ShouldReturnAllDishes() throws Exception {
        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setName("Spicy Noodles");

        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Mild Noodles");

        List<Dish> dishes = Arrays.asList(dish1, dish2);

        when(dishService.getAllDishes()).thenReturn(dishes);

        mockMvc.perform(get("/api/dishes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Spicy Noodles"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Mild Noodles"));
    }

    @Test
    public void updateDish_ShouldReturnUpdatedDish() throws Exception {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Updated Spicy Noodles");
        dish.setPrice(12.0);

        when(dishService.updateDish(any(Dish.class))).thenReturn(dish);

        mockMvc.perform(put("/api/dishes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dish)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Spicy Noodles"));
    }

    @Test
    public void deleteDish_ShouldReturnOk() throws Exception {
        doNothing().when(dishService).deleteDish(1L);

        mockMvc.perform(delete("/api/dishes/1"))
                .andExpect(status().isOk());

        verify(dishService, times(1)).deleteDish(1L);
    }

    @Test
    public void getDishById_WhenDishNotFound_ShouldReturnNotFound() throws Exception {
        when(dishService.getDishById(1L))
                .thenThrow(new IllegalArgumentException("Dish not found with id: 1"));

        mockMvc.perform(get("/api/dishes/1"))
                .andExpect(status().isNotFound());
    }
} 