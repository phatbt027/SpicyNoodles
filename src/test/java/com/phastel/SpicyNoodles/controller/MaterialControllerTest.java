package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Material;
import com.phastel.SpicyNoodles.service.MaterialService;
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
public class MaterialControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private MaterialController materialController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(materialController)
            .setControllerAdvice(new com.phastel.SpicyNoodles.exception.RestExceptionHandler())
            .build();
    }

    @Test
    public void createMaterial_ShouldReturnCreatedMaterial() throws Exception {
        Material material = new Material();
        material.setName("Flour");
        material.setCategory("Dry Goods");
        material.setDescription("All-purpose flour");

        when(materialService.createMaterial(any(Material.class))).thenReturn(material);

        mockMvc.perform(post("/api/materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(material)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Flour"))
                .andExpect(jsonPath("$.category").value("Dry Goods"));
    }

    @Test
    public void getMaterialById_ShouldReturnMaterial() throws Exception {
        Material material = new Material();
        material.setId(1L);
        material.setName("Flour");

        when(materialService.getMaterialById(1L)).thenReturn(material);

        mockMvc.perform(get("/api/materials/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Flour"));
    }

    @Test
    public void getAllMaterials_ShouldReturnAllMaterials() throws Exception {
        Material material1 = new Material();
        material1.setId(1L);
        material1.setName("Flour");

        Material material2 = new Material();
        material2.setId(2L);
        material2.setName("Sugar");

        List<Material> materials = Arrays.asList(material1, material2);

        when(materialService.getAllMaterials()).thenReturn(materials);

        mockMvc.perform(get("/api/materials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Flour"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Sugar"));
    }

    @Test
    public void updateMaterial_ShouldReturnUpdatedMaterial() throws Exception {
        Material material = new Material();
        material.setId(1L);
        material.setName("Updated Flour");
        material.setCategory("Updated Category");

        when(materialService.updateMaterial(any(Material.class))).thenReturn(material);

        mockMvc.perform(put("/api/materials/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(material)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Flour"));
    }

    @Test
    public void deleteMaterial_ShouldReturnOk() throws Exception {
        doNothing().when(materialService).deleteMaterial(1L);

        mockMvc.perform(delete("/api/materials/1"))
                .andExpect(status().isOk());

        verify(materialService, times(1)).deleteMaterial(1L);
    }

    @Test
    public void getMaterialById_WhenMaterialNotFound_ShouldReturnNotFound() throws Exception {
        when(materialService.getMaterialById(1L))
                .thenThrow(new IllegalArgumentException("Material not found with id: 1"));

        mockMvc.perform(get("/api/materials/1"))
                .andExpect(status().isNotFound());
    }
} 