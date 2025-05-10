package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Storage;
import com.phastel.SpicyNoodles.entity.StorageId;
import com.phastel.SpicyNoodles.entity.Material;
import com.phastel.SpicyNoodles.entity.Branch;
import com.phastel.SpicyNoodles.service.StorageService;
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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class StorageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private StorageController storageController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(storageController).build();
    }

    @Test
    public void createStorage_ShouldReturnCreatedStorage() throws Exception {
        Storage storage = new Storage();
        StorageId storageId = new StorageId();
        storageId.setMaterialId(1L);
        storageId.setBranchId(1L);
        storage.setId(storageId);
        storage.setQuantity(100);
        storage.setDateOfEntry(LocalDateTime.now());

        when(storageService.createStorage(any(Storage.class))).thenReturn(storage);

        mockMvc.perform(post("/api/storages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(100));
    }

    @Test
    public void getStorageById_ShouldReturnStorage() throws Exception {
        Storage storage = new Storage();
        StorageId storageId = new StorageId();
        storageId.setMaterialId(1L);
        storageId.setBranchId(1L);
        storage.setId(storageId);
        storage.setQuantity(100);

        when(storageService.getStorageById(1L, 1L)).thenReturn(storage);

        mockMvc.perform(get("/api/storages/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(100));
    }

    @Test
    public void getAllStorage_ShouldReturnAllStorage() throws Exception {
        Storage storage1 = new Storage();
        StorageId storageId1 = new StorageId();
        storageId1.setMaterialId(1L);
        storageId1.setBranchId(1L);
        storage1.setId(storageId1);
        storage1.setQuantity(100);

        Storage storage2 = new Storage();
        StorageId storageId2 = new StorageId();
        storageId2.setMaterialId(2L);
        storageId2.setBranchId(2L);
        storage2.setId(storageId2);
        storage2.setQuantity(200);

        List<Storage> storageList = Arrays.asList(storage1, storage2);

        when(storageService.getAllStorages()).thenReturn(storageList);

        mockMvc.perform(get("/api/storages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity").value(100))
                .andExpect(jsonPath("$[1].quantity").value(200));
    }

    @Test
    public void updateStorage_ShouldReturnUpdatedStorage() throws Exception {
        Storage storage = new Storage();
        StorageId storageId = new StorageId();
        storageId.setMaterialId(1L);
        storageId.setBranchId(1L);
        storage.setId(storageId);
        storage.setQuantity(150);

        when(storageService.updateStorage(any(Storage.class))).thenReturn(storage);

        mockMvc.perform(put("/api/storages/1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(150));
    }

    @Test
    public void deleteStorage_ShouldReturnOk() throws Exception {
        doNothing().when(storageService).deleteStorage(1L, 1L);

        mockMvc.perform(delete("/api/storages/1/1"))
                .andExpect(status().isOk());

        verify(storageService, times(1)).deleteStorage(1L, 1L);
    }

    @Test
    public void getStorageById_WhenStorageNotFound_ShouldReturnNotFound() throws Exception {
        when(storageService.getStorageById(1L, 1L))
                .thenThrow(new IllegalArgumentException("Storage not found"));

        mockMvc.perform(get("/api/storages/1/1"))
                .andExpect(status().isNotFound());
    }
} 