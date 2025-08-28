package ru.Tim.ShiftCRM.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.Tim.ShiftCRM.api.model.transaction.request.NewTransactionRequest;
import ru.Tim.ShiftCRM.api.model.transaction.response.TransactionResponse;
import ru.Tim.ShiftCRM.config.ControllerConfiguration;
import ru.Tim.ShiftCRM.core.enums.PaymentType;
import ru.Tim.ShiftCRM.core.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(ControllerConfiguration.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionService transactionService;

    private TransactionResponse transactionResponse;
    private NewTransactionRequest newTransactionRequest;

    @BeforeEach
    void setUp() {
        transactionResponse = TransactionResponse.builder()
                .id(1L)
                .sellerId(1L)
                .amount(BigDecimal.valueOf(1000))
                .paymentType(PaymentType.CASH.name())
                .transactionDate(LocalDateTime.now())
                .build();

        newTransactionRequest = new NewTransactionRequest(
                1L,
                BigDecimal.valueOf(500),
                "CASH"
        );
    }

    @Test
    void getAll_withCustomParameters_returnsAllTransactionsPage() throws Exception {
        List<TransactionResponse> transactions = List.of(transactionResponse);
        Page<TransactionResponse> page = new PageImpl<>(transactions, PageRequest.of(0, 10), transactions.size());

        when(transactionService.getAllTransactions(0, 10)).thenReturn(page);

        mockMvc.perform(get("/apiV1/transactions")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].amount").value(1000));

        verify(transactionService, times(1)).getAllTransactions(0, 10);
    }

    @Test
    void getAll_withDefaultParameters_usesDefaultValues() throws Exception {
        // Arrange
        List<TransactionResponse> transactions = List.of(transactionResponse);
        Page<TransactionResponse> page = new PageImpl<>(transactions, PageRequest.of(0, 50), transactions.size());

        when(transactionService.getAllTransactions(0, 50)).thenReturn(page);

        mockMvc.perform(get("/apiV1/transactions")) // без параметров
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        verify(transactionService, times(1)).getAllTransactions(0, 50);
    }

    @Test
    void getAll_withInvalidPage_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/transactions")
                        .param("page", "-1")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getAll_withInvalidSize_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/transactions")
                        .param("page", "0")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getTransactionInfo_withValidId_returnsTransaction() throws Exception {
        when(transactionService.getTransactionInfo(1L)).thenReturn(transactionResponse);

        mockMvc.perform(get("/apiV1/transactions/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.paymentType").value("CASH"));

        verify(transactionService, times(1)).getTransactionInfo(1L);
    }

    @Test
    void getTransactionInfo_withNonExistentId_returnsNotFound() throws Exception {
        when(transactionService.getTransactionInfo(999L))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Не было найдено транзакции с id 999"));

        mockMvc.perform(get("/apiV1/transactions/{id}", 999L))
                .andExpect(status().isNotFound());

    }

    @Test
    void createTransaction_withValidData_returnsCreated() throws Exception {
        when(transactionService.createTransaction(any(NewTransactionRequest.class))).thenReturn(1L);

        mockMvc.perform(post("/apiV1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTransactionRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Транзакция создана с id 1"));
    }

    @Test
    void createTransaction_withInvalidData_returnsBadRequest() throws Exception {
        NewTransactionRequest invalidDto = new NewTransactionRequest(null, null, ""); // невалидные данные

        mockMvc.perform(post("/apiV1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void createTransaction_withNonExistentSeller_returnsNotFound() throws Exception {
        when(transactionService.createTransaction(any(NewTransactionRequest.class)))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Не было найдено продавца с id 999"));

        mockMvc.perform(post("/apiV1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTransactionRequest)))
                .andExpect(status().isNotFound());

        verify(transactionService, times(1)).createTransaction(any(NewTransactionRequest.class));
    }

    @Test
    void getSellerTransactions_withValidData_returnsTransactionsPage() throws Exception {
        List<TransactionResponse> transactions = List.of(transactionResponse);
        Page<TransactionResponse> page = new PageImpl<>(transactions, PageRequest.of(0, 10), transactions.size());

        when(transactionService.getTransactionsBySeller(1L, 0, 10)).thenReturn(page);

        mockMvc.perform(get("/apiV1/transactions/sellerTransactions/{id}", 1L)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].sellerId").value(1L));

        verify(transactionService, times(1)).getTransactionsBySeller(1L, 0, 10);
    }

    @Test
    void getSellerTransactions_withDefaultParameters_usesDefaultValues() throws Exception {
        List<TransactionResponse> transactions = List.of(transactionResponse);
        Page<TransactionResponse> page = new PageImpl<>(transactions, PageRequest.of(0, 50), transactions.size());

        when(transactionService.getTransactionsBySeller(1L, 0, 50)).thenReturn(page);

        mockMvc.perform(get("/apiV1/transactions/sellerTransactions/{id}", 1L)) // без параметров
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        verify(transactionService, times(1)).getTransactionsBySeller(1L, 0, 50);
    }

    @Test
    void getSellerTransactions_withNonExistentSeller_returnsNotFound() throws Exception {
        when(transactionService.getTransactionsBySeller(999L, 0, 10))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Не было найдено продавца с id 999"));

        mockMvc.perform(get("/apiV1/transactions/sellerTransactions/{id}", 999L)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isNotFound());

        verify(transactionService, times(1)).getTransactionsBySeller(999L, 0, 10);
    }

    @Test
    void getSellerTransactions_withInvalidPage_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/transactions/sellerTransactions/{id}", 1L)
                        .param("page", "-1")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getSellerTransactions_withInvalidSize_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/transactions/sellerTransactions/{id}", 1L)
                        .param("page", "0")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());

        verify(transactionService, never()).getTransactionsBySeller(anyLong(), anyInt(), anyInt());
    }

    @Test
    void getSellerTransactions_withInvalidSellerId_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/transactions/sellerTransactions/"))
                .andExpect(status().is4xxClientError());

    }


}
