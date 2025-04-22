package com.phastel.SpicyNoodles.service.impl;

import com.phastel.SpicyNoodles.entity.*;
import com.phastel.SpicyNoodles.repository.InvoiceRepository;
import com.phastel.SpicyNoodles.repository.InvoiceDetailsRepository;
import com.phastel.SpicyNoodles.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailsRepository invoiceDetailsRepository;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceDetailsRepository invoiceDetailsRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailsRepository = invoiceDetailsRepository;
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

    @Override
    public InvoiceDetails addInvoiceDetail(Long invoiceId, InvoiceDetails invoiceDetail) {
        Invoice invoice = getInvoiceById(invoiceId);
        invoiceDetail.setInvoice(invoice);
        return invoiceDetailsRepository.save(invoiceDetail);
    }

    @Override
    public void removeInvoiceDetail(Long invoiceId, Long softDrinkId, Long dishId) {
        InvoiceDetailsId id = new InvoiceDetailsId();
        id.setSoftDrinkId(softDrinkId);
        id.setDishId(dishId);
        invoiceDetailsRepository.deleteById(id);
    }

    @Override
    public List<InvoiceDetails> getInvoiceDetails(Long invoiceId) {
        return invoiceDetailsRepository.findByInvoiceId(invoiceId);
    }
} 