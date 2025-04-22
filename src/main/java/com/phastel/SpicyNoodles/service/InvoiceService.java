package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.Invoice;
import com.phastel.SpicyNoodles.entity.InvoiceDetails;
import java.util.List;

public interface InvoiceService {
    Invoice createInvoice(Invoice invoice);
    Invoice updateInvoice(Invoice invoice);
    void deleteInvoice(Long id);
    Invoice getInvoiceById(Long id);
    List<Invoice> getAllInvoices();
    List<Invoice> getInvoicesByUserId(Long userId);
    
    // InvoiceDetails operations
    InvoiceDetails addInvoiceDetail(Long invoiceId, InvoiceDetails invoiceDetail);
    void removeInvoiceDetail(Long invoiceId, Long softDrinkId, Long dishId);
    List<InvoiceDetails> getInvoiceDetails(Long invoiceId);
} 