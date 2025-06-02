package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.Invoice;
import com.phastel.SpicyNoodles.entity.User;
import com.phastel.SpicyNoodles.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class InvoiceRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;
    private Invoice invoice;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("testuser@example.com");
        user.setRole(Role.STAFF);
        user.setEnabled(true);
        user = userRepository.save(user);

        invoice = new Invoice();
        invoice.setUser(user);
        invoice.setTotalPrice(100.0);
        invoice.setStatus(Invoice.OrderStatus.PENDING);
        invoice.setCustomerName("John Doe");
        invoice.setOrderTime(LocalDateTime.now());
    }

    @Test
    void whenSaveInvoice_thenInvoiceIsSaved() {
        Invoice saved = invoiceRepository.save(invoice);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCustomerName()).isEqualTo(invoice.getCustomerName());
    }

    @Test
    void whenFindByUserId_thenReturnInvoice() {
        entityManager.persist(invoice);
        entityManager.flush();
        List<Invoice> found = invoiceRepository.findByUserId(user.getId());
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void whenDeleteInvoice_thenInvoiceIsRemoved() {
        Invoice saved = entityManager.persist(invoice);
        entityManager.flush();
        invoiceRepository.deleteById(saved.getId());
        assertThat(invoiceRepository.findById(saved.getId())).isEmpty();
    }
} 