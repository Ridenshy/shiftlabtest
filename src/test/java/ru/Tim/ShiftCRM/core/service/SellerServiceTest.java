package ru.Tim.ShiftCRM.core.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.Tim.ShiftCRM.api.model.seller.request.NewSellerRequest;
import ru.Tim.ShiftCRM.api.model.seller.request.UpdatedSellerRequest;
import ru.Tim.ShiftCRM.api.model.seller.responce.SellerResponse;
import ru.Tim.ShiftCRM.api.exception.ContactInfoAlreadyExistsException;
import ru.Tim.ShiftCRM.config.TestcontainersConfiguration;
import ru.Tim.ShiftCRM.core.entity.Seller;
import ru.Tim.ShiftCRM.core.repository.SellerRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Testcontainers
@Transactional
public class SellerServiceTest {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.createNativeQuery("TRUNCATE TABLE sellers RESTART IDENTITY CASCADE").executeUpdate();
        List<Seller> sellers = List.of(
                Seller.builder()
                        .name("Евгений")
                        .contactInfo("evg@gmail.com")
                        .registrationDate(LocalDateTime.now().minusDays(15))
                        .build(),
                Seller.builder()
                        .name("Александр")
                        .contactInfo("+79619998877")
                        .registrationDate(LocalDateTime.now().minusDays(10))
                        .build(),
                Seller.builder()
                        .name("Михаил")
                        .contactInfo("mi@mail.ru")
                        .registrationDate(LocalDateTime.now().minusDays(2))
                        .build()
        );
        sellerRepository.saveAll(sellers);
    }

    @Test
    void getAllSellers_returnPagedSellers(){
        Page<SellerResponse> result = sellerService.getAllSellers(0, 10);
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(3, result.getTotalElements());
        List<SellerResponse> sellers = result.getContent();
        assertEquals("Евгений", sellers.get(0).getName());
        assertEquals("Михаил", sellers.get(2).getName());
    }

    @Test
    void getAllSellers_returnedIncorrectPage() {
        Page<SellerResponse> result = sellerService.getAllSellers(5, 2);
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(3, result.getTotalElements());
    }

    @Test
    void getSellerInfo_withCorrectId_returnedSeller() {
        Long id = 1L;
        SellerResponse result = sellerService.getSellerInfo(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Евгений", result.getName());
    }

    @Test
    void getSellerInfo_withNotExistsId_throwsException() {
        Long id = 999L;
        assertThrows(
                EntityNotFoundException.class,
                () -> sellerService.getSellerInfo(id)
        );
    }

    @Test
    void saveNewSeller_withCorrectData() {
        NewSellerRequest dto = new NewSellerRequest(
                "Продавец",
                "p@mail.ru"
        );
        SellerResponse sellerResponse = sellerService.saveNewSeller(dto);
        assertTrue(sellerRepository.existsById(sellerResponse.getId()));
    }

    @Test
    void saveNewSeller_withExistsContact_throwsException() {
        NewSellerRequest dto = new NewSellerRequest(
                "Продавец",
                "mi@mail.ru"
        );
        assertThrows(
                ContactInfoAlreadyExistsException.class,
                () -> sellerService.saveNewSeller(dto)
        );
    }

    @Test
    void updateSeller_withCorrectData() {
        Long id = 1L;
        UpdatedSellerRequest dto = new UpdatedSellerRequest(
                "Продавец",
                "ddd@gmail.com"
        );
        sellerService.updateSeller(dto, id);
        Seller entity = sellerRepository.findById(id).orElse(null);
        assertNotNull(entity);
        assertEquals(dto.getContactInfo(), entity.getContactInfo());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    void updateSeller_withNotExistsId_throwsException() {
        Long id = 999L;
        UpdatedSellerRequest dto = new UpdatedSellerRequest(
                "Продавец",
                "gg@gmail.com"
        );
        assertThrows(
                EntityNotFoundException.class,
                () -> sellerService.updateSeller(dto, id)
        );
        assertFalse(sellerRepository.existsById(id));
    }

    @Test
    void updateById_withSomeExistsContactInfo_throwsException() {
        Long id = 1L;
        UpdatedSellerRequest dto = new UpdatedSellerRequest(
                "Евгений",
                "mi@mail.ru"
        );
        assertThrows(
                ContactInfoAlreadyExistsException.class,
                () -> sellerService.updateSeller(dto, id)
        );
        Seller entity = sellerRepository.findById(id).orElse(null);
        assertNotNull(entity);
        assertNotEquals(dto.getContactInfo(), entity.getContactInfo());
    }

    @Test
    void deleteById_withCorrectData(){
        Long id = 2L;
        sellerService.deleteSeller(id);
        assertFalse(sellerRepository.existsById(id));
    }

    @Test
    void deleteById_withNotExistsId_throwsException(){
        Long id = 5L;
        assertThrows(
                EntityNotFoundException.class,
                () -> sellerService.deleteSeller(id)
        );
        assertFalse(sellerRepository.existsById(id));
    }

}
