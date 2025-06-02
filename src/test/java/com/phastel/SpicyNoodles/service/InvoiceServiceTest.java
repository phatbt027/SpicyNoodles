package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.Invoice;
import com.phastel.SpicyNoodles.entity.User;
import com.phastel.SpicyNoodles.repository.InvoiceRepository;
import com.phastel.SpicyNoodles.service.impl.InvoiceServiceImpl;
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
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private Invoice testInvoice;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        testInvoice = new Invoice();
        testInvoice.setId(1L);
        testInvoice.setUser(testUser);
        testInvoice.setTotalPrice(100.0);
        testInvoice.setStatus(Invoice.OrderStatus.PENDING);
        testInvoice.setCustomerName("John Doe");
        testInvoice.setOrderTime(LocalDateTime.now());
    }

    @Test
    void whenCreateInvoice_thenInvoiceIsCreated() {
        // Given
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);

        // When
        Invoice createdInvoice = invoiceService.createInvoice(testInvoice);

        // Then
        assertThat(createdInvoice).isNotNull();
        assertThat(createdInvoice.getCustomerName()).isEqualTo(testInvoice.getCustomerName());
        verify(invoiceRepository).save(any(Invoice.class));
    }

    @Test
    void whenUpdateInvoice_thenInvoiceIsUpdated() {
        // Given
        when(invoiceRepository.existsById(testInvoice.getId())).thenReturn(true);
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);

        // When
        Invoice updatedInvoice = invoiceService.updateInvoice(testInvoice);

        // Then
        assertThat(updatedInvoice).isNotNull();
        assertThat(updatedInvoice.getCustomerName()).isEqualTo(testInvoice.getCustomerName());
        verify(invoiceRepository).save(any(Invoice.class));
    }

    @Test
    void whenUpdateNonExistentInvoice_thenThrowException() {
        // Given
        when(invoiceRepository.existsById(testInvoice.getId())).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> invoiceService.updateInvoice(testInvoice))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invoice not found");
    }

    @Test
    void whenDeleteInvoice_thenInvoiceIsDeleted() {
        // Given
        when(invoiceRepository.existsById(testInvoice.getId())).thenReturn(true);

        // When
        invoiceService.deleteInvoice(testInvoice.getId());

        // Then
        verify(invoiceRepository).deleteById(testInvoice.getId());
    }

    @Test
    void whenDeleteNonExistentInvoice_thenThrowException() {
        // Given
        when(invoiceRepository.existsById(testInvoice.getId())).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> invoiceService.deleteInvoice(testInvoice.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invoice not found");
    }

    @Test
    void whenGetInvoiceById_thenReturnInvoice() {
        // Given
        when(invoiceRepository.findById(testInvoice.getId())).thenReturn(Optional.of(testInvoice));

        // When
        Invoice foundInvoice = invoiceService.getInvoiceById(testInvoice.getId());

        // Then
        assertThat(foundInvoice).isNotNull();
        assertThat(foundInvoice.getId()).isEqualTo(testInvoice.getId());
    }

    @Test
    void whenGetNonExistentInvoiceById_thenThrowException() {
        // Given
        when(invoiceRepository.findById(testInvoice.getId())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> invoiceService.getInvoiceById(testInvoice.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invoice not found");
    }

    @Test
    void whenGetAllInvoices_thenReturnAllInvoices() {
        // Given
        List<Invoice> invoices = Arrays.asList(testInvoice);
        when(invoiceRepository.findAll()).thenReturn(invoices);

        // When
        List<Invoice> foundInvoices = invoiceService.getAllInvoices();

        // Then
        assertThat(foundInvoices).isNotEmpty();
        assertThat(foundInvoices).hasSize(1);
        assertThat(foundInvoices.get(0).getCustomerName()).isEqualTo(testInvoice.getCustomerName());
    }

    @Test
    void whenGetInvoicesByUserId_thenReturnMatchingInvoices() {
        // Given
        List<Invoice> invoices = Arrays.asList(testInvoice);
        when(invoiceRepository.findByUserId(testUser.getId())).thenReturn(invoices);

        // When
        List<Invoice> foundInvoices = invoiceService.getInvoicesByUserId(testUser.getId());

        // Then
        assertThat(foundInvoices).isNotEmpty();
        assertThat(foundInvoices.get(0).getUser().getId()).isEqualTo(testUser.getId());
    }
} 