package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.Invoice;
import java.util.List;

public interface InvoiceService {
    Invoice createInvoice(Invoice invoice);
    Invoice updateInvoice(Invoice invoice);
    void deleteInvoice(Long id);
    Invoice getInvoiceById(Long id);
    List<Invoice> getAllInvoices();
    List<Invoice> getInvoicesByUserId(Long userId);
} 