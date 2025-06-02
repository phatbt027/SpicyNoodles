package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.Storage;
import com.phastel.SpicyNoodles.entity.StorageId;
import com.phastel.SpicyNoodles.entity.Ingredient;
import com.phastel.SpicyNoodles.entity.Branch;
import com.phastel.SpicyNoodles.repository.StorageRepository;
import com.phastel.SpicyNoodles.service.impl.StorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

    @Mock
    private StorageRepository storageRepository;

    @InjectMocks
    private StorageServiceImpl storageService;

    private Storage testStorage;
    private Ingredient testIngredient;
    private Branch testBranch;
    private StorageId testStorageId;

    @BeforeEach
    void setUp() {
        testIngredient = new Ingredient();
        testIngredient.setId(1L);
        testIngredient.setName("Test Ingredient");

        testBranch = new Branch();
        testBranch.setId(1L);
        testBranch.setAddress("Test Address");

        testStorageId = new StorageId(testIngredient.getId(), testBranch.getId());

        testStorage = new Storage();
        testStorage.setId(testStorageId);
        testStorage.setIngredient(testIngredient);
        testStorage.setBranch(testBranch);
        testStorage.setQuantity(100);
        testStorage.setDateOfEntry(LocalDateTime.now());
        testStorage.setExpirationDate(LocalDateTime.now().plusDays(30));
    }

    @Test
    void whenCreateStorage_thenStorageIsCreated() {
        // Given
        when(storageRepository.save(any(Storage.class))).thenReturn(testStorage);

        // When
        Storage createdStorage = storageService.createStorage(testStorage);

        // Then
        assertThat(createdStorage).isNotNull();
        assertThat(createdStorage.getIngredient().getId()).isEqualTo(testIngredient.getId());
        assertThat(createdStorage.getBranch().getId()).isEqualTo(testBranch.getId());
        verify(storageRepository).save(any(Storage.class));
    }

    @Test
    void whenUpdateStorage_thenStorageIsUpdated() {
        // Given
        when(storageRepository.existsById(testStorageId)).thenReturn(true);
        when(storageRepository.save(any(Storage.class))).thenReturn(testStorage);

        // When
        Storage updatedStorage = storageService.updateStorage(testStorage);

        // Then
        assertThat(updatedStorage).isNotNull();
        assertThat(updatedStorage.getQuantity()).isEqualTo(testStorage.getQuantity());
        verify(storageRepository).save(any(Storage.class));
    }

    @Test
    void whenUpdateNonExistentStorage_thenThrowException() {
        // Given
        when(storageRepository.existsById(testStorageId)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> storageService.updateStorage(testStorage))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Storage not found");
    }

    @Test
    void whenDeleteStorage_thenStorageIsDeleted() {
        // Given
        when(storageRepository.existsById(testStorageId)).thenReturn(true);

        // When
        storageService.deleteStorage(testIngredient.getId(), testBranch.getId());

        // Then
        verify(storageRepository).deleteById(testStorageId);
    }

    @Test
    void whenDeleteNonExistentStorage_thenThrowException() {
        // Given
        when(storageRepository.existsById(testStorageId)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> storageService.deleteStorage(testIngredient.getId(), testBranch.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Storage not found");
    }

    @Test
    void whenGetStorageById_thenReturnStorage() {
        // Given
        when(storageRepository.findById(testStorageId)).thenReturn(Optional.of(testStorage));

        // When
        Storage foundStorage = storageService.getStorageById(testIngredient.getId(), testBranch.getId());

        // Then
        assertThat(foundStorage).isNotNull();
        assertThat(foundStorage.getId()).isEqualTo(testStorageId);
    }

    @Test
    void whenGetNonExistentStorageById_thenThrowException() {
        // Given
        when(storageRepository.findById(testStorageId)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> storageService.getStorageById(testIngredient.getId(), testBranch.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Storage not found");
    }

    @Test
    void whenGetAllStorages_thenReturnAllStorages() {
        // Given
        List<Storage> storages = Arrays.asList(testStorage);
        when(storageRepository.findAll()).thenReturn(storages);

        // When
        List<Storage> foundStorages = storageService.getAllStorages();

        // Then
        assertThat(foundStorages).isNotEmpty();
        assertThat(foundStorages).hasSize(1);
        assertThat(foundStorages.get(0).getId()).isEqualTo(testStorageId);
    }

    @Test
    void whenGetStoragesByBranch_thenReturnMatchingStorages() {
        // Given
        List<Storage> storages = Arrays.asList(testStorage);
        when(storageRepository.findByBranchId(testBranch.getId())).thenReturn(storages);

        // When
        List<Storage> foundStorages = storageService.getStoragesByBranch(testBranch.getId());

        // Then
        assertThat(foundStorages).isNotEmpty();
        assertThat(foundStorages.get(0).getBranch().getId()).isEqualTo(testBranch.getId());
    }

    @Test
    void whenGetStoragesByIngredient_thenReturnMatchingStorages() {
        // Given
        List<Storage> storages = Arrays.asList(testStorage);
        when(storageRepository.findByIngredientId(testIngredient.getId())).thenReturn(storages);

        // When
        List<Storage> foundStorages = storageService.getStoragesByIngredient(testIngredient.getId());

        // Then
        assertThat(foundStorages).isNotEmpty();
        assertThat(foundStorages.get(0).getIngredient().getId()).isEqualTo(testIngredient.getId());
    }

    @Test
    void whenGetExpiredStorages_thenReturnExpiredStorages() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        testStorage.setExpirationDate(now.minusDays(1));
        List<Storage> storages = Arrays.asList(testStorage);
        when(storageRepository.findByExpirationDateBefore(now)).thenReturn(storages);

        // When
        List<Storage> foundStorages = storageService.getExpiredStorages(now);

        // Then
        assertThat(foundStorages).isNotEmpty();
        assertThat(foundStorages.get(0).getExpirationDate()).isBefore(now);
    }

    @Test
    void whenUpdateStorageQuantity_thenQuantityIsUpdated() {
        // Given
        when(storageRepository.findById(testStorageId)).thenReturn(Optional.of(testStorage));
        when(storageRepository.save(any(Storage.class))).thenReturn(testStorage);

        // When
        Storage updatedStorage = storageService.updateStorageQuantity(
            testIngredient.getId(), testBranch.getId(), 50);

        // Then
        assertThat(updatedStorage).isNotNull();
        assertThat(updatedStorage.getQuantity()).isEqualTo(50);
        verify(storageRepository).save(any(Storage.class));
    }
} 