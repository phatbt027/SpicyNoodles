package com.phastel.SpicyNoodles.service.impl;

import com.phastel.SpicyNoodles.entity.Invoice;
import com.phastel.SpicyNoodles.repository.InvoiceRepository;
import com.phastel.SpicyNoodles.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice updateInvoice(Invoice invoice) {
        if (!invoiceRepository.existsById(invoice.getId())) {
            throw new IllegalArgumentException("Invoice not found with id: " + invoice.getId());
        }
        return invoiceRepository.save(invoice);
    }

    @Override
    public void deleteInvoice(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new IllegalArgumentException("Invoice not found with id: " + id);
        }
        invoiceRepository.deleteById(id);
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with id: " + id));
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public List<Invoice> getInvoicesByUserId(Long userId) {
        return invoiceRepository.findByUserId(userId);
    }
} 