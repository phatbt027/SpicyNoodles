package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.InvoiceDetails;
import com.phastel.SpicyNoodles.entity.InvoiceDetailsId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceDetailsRepository extends JpaRepository<InvoiceDetails, InvoiceDetailsId> {
    List<InvoiceDetails> findByInvoiceId(Long invoiceId);
} 