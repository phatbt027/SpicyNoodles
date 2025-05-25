package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Invoice;
import com.phastel.SpicyNoodles.entity.OrderItem;
import com.phastel.SpicyNoodles.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
        return ResponseEntity.ok(invoiceService.createInvoice(invoice));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @RequestBody Invoice invoice) {
        invoice.setId(id);
        return ResponseEntity.ok(invoiceService.updateInvoice(invoice));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Invoice>> getInvoicesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByUserId(userId));
    }

    @GetMapping("/{invoiceId}/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable Long invoiceId) {
        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        return ResponseEntity.ok(invoice.getItems());
    }
} 