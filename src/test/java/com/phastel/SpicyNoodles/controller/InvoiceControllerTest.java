package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Invoice;
import com.phastel.SpicyNoodles.entity.User;
import com.phastel.SpicyNoodles.service.InvoiceService;
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
public class InvoiceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(invoiceController)
            .setControllerAdvice(new com.phastel.SpicyNoodles.exception.RestExceptionHandler())
            .build();
    }

    @Test
    public void createInvoice_ShouldReturnCreatedInvoice() throws Exception {
        Invoice invoice = new Invoice();
        User user = new User();
        user.setId(1L);
        invoice.setUser(user);
        invoice.setTotalPrice(100.0);

        when(invoiceService.createInvoice(any(Invoice.class))).thenReturn(invoice);

        mockMvc.perform(post("/api/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invoice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(100.0));
    }

    @Test
    public void getInvoiceById_ShouldReturnInvoice() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setTotalPrice(100.0);

        when(invoiceService.getInvoiceById(1L)).thenReturn(invoice);

        mockMvc.perform(get("/api/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalPrice").value(100.0));
    }

    @Test
    public void getAllInvoices_ShouldReturnAllInvoices() throws Exception {
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        invoice1.setTotalPrice(100.0);

        Invoice invoice2 = new Invoice();
        invoice2.setId(2L);
        invoice2.setTotalPrice(200.0);

        List<Invoice> invoices = Arrays.asList(invoice1, invoice2);

        when(invoiceService.getAllInvoices()).thenReturn(invoices);

        mockMvc.perform(get("/api/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].totalPrice").value(100.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].totalPrice").value(200.0));
    }

    @Test
    public void updateInvoice_ShouldReturnUpdatedInvoice() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setTotalPrice(150.0);

        when(invoiceService.updateInvoice(any(Invoice.class))).thenReturn(invoice);

        mockMvc.perform(put("/api/invoices/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invoice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalPrice").value(150.0));
    }

    @Test
    public void deleteInvoice_ShouldReturnOk() throws Exception {
        doNothing().when(invoiceService).deleteInvoice(1L);

        mockMvc.perform(delete("/api/invoices/1"))
                .andExpect(status().isOk());

        verify(invoiceService, times(1)).deleteInvoice(1L);
    }

    @Test
    public void getInvoiceById_WhenInvoiceNotFound_ShouldReturnNotFound() throws Exception {
        when(invoiceService.getInvoiceById(1L))
                .thenThrow(new IllegalArgumentException("Invoice not found with id: 1"));

        mockMvc.perform(get("/api/invoices/1"))
                .andExpect(status().isNotFound());
    }
} 