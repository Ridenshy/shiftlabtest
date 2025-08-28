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
import ru.Tim.ShiftCRM.api.model.analytics.SellerBestPeriodResponse;
import ru.Tim.ShiftCRM.api.model.analytics.TopSellerResponse;
import ru.Tim.ShiftCRM.api.model.seller.responce.SellerResponse;
import ru.Tim.ShiftCRM.config.ControllerConfiguration;
import ru.Tim.ShiftCRM.core.service.AnalyticsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(ControllerConfiguration.class)
@WebMvcTest(AnalyticController.class)
public class AnalyticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnalyticsService analyticsService;

    private SellerResponse sellerResponse;
    private TopSellerResponse topSellerResponse;
    private SellerBestPeriodResponse bestPeriodResponse;

    @BeforeEach
    void setUp() {
        sellerResponse = SellerResponse.builder()
                .id(1L)
                .name("Евгений")
                .contactInfo("e@mail.ru")
                .registrationDate(LocalDateTime.now().minusDays(5))
                .build();

        topSellerResponse = TopSellerResponse.builder()
                .topSeller(sellerResponse)
                .sellerAmount(BigDecimal.valueOf(10000))
                .build();

        bestPeriodResponse = SellerBestPeriodResponse.builder()
                .startOfPeriod(LocalDate.now().minusDays(5))
                .endOfPeriod(LocalDate.now().minusDays(3))
                .density(2.5)
                .build();

    }

    @Test
    void getTopSeller_withValidPeriod_returnsTopSeller() throws Exception {

        when(analyticsService.getBestSeller("DAY")).thenReturn(topSellerResponse);

        mockMvc.perform(get("/apiV1/analytics/getTopSeller/")
                        .param("datePeriodType", "DAY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topSeller.id").value(1L))
                .andExpect(jsonPath("$.topSeller.name").value("Евгений"))
                .andExpect(jsonPath("$.sellerAmount").value(10000));

        verify(analyticsService, times(1)).getBestSeller("DAY");
    }

    @Test
    void getTopSeller_withInvalidPeriod_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/analytics/getTopSeller/")
                        .param("datePeriodType", "INVALID"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTopSeller_withNullPeriod_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/analytics/getTopSeller/"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTopSeller_withNoData_returnsNotFound() throws Exception {
        when(analyticsService.getBestSeller("DAY"))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Не было найдено продавца за заданный период"));

        mockMvc.perform(get("/apiV1/analytics/getTopSeller/")
                        .param("datePeriodType", "DAY"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBadSellers_withValidRequest_returnsBadSellersPage() throws Exception {
        List<SellerResponse> sellers = List.of(sellerResponse);
        Page<SellerResponse> page = new PageImpl<>(sellers, PageRequest.of(0, 10), sellers.size());

        when(analyticsService.getBadSellers(eq(0), eq(10), any(BigDecimal.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(page);

        mockMvc.perform(get("/apiV1/analytics/getBadSellers")
                        .param("page", "0")
                        .param("size", "10")
                        .param("minAmount", "1000.00")
                        .param("minDate", "2024-01-01")
                        .param("maxDate", "2024-12-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1L));

        verify(analyticsService, times(1)).getBadSellers(eq(0), eq(10), any(BigDecimal.class), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getBadSellers_withDefaultParameters_usesDefaultValues() throws Exception {
        List<SellerResponse> sellers = List.of(sellerResponse);
        Page<SellerResponse> page = new PageImpl<>(sellers, PageRequest.of(0, 50), sellers.size());

        when(analyticsService.getBadSellers(eq(0), eq(50), any(BigDecimal.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(page);

        mockMvc.perform(get("/apiV1/analytics/getBadSellers")
                        .param("minAmount", "1000.00")
                        .param("minDate", "2024-01-01")
                        .param("maxDate", "2024-12-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        verify(analyticsService, times(1)).getBadSellers(eq(0), eq(50), any(BigDecimal.class), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getBadSellers_withInvalidPage_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/analytics/getBadSellers")
                        .param("page", "-1")
                        .param("size", "10")
                        .param("minAmount", "1000.00")
                        .param("minDate", "2024-01-01")
                        .param("maxDate", "2024-12-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getBadSellers_withInvalidSize_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/analytics/getBadSellers")
                        .param("page", "0")
                        .param("size", "0")
                        .param("minAmount", "1000.00")
                        .param("minDate", "2024-01-01")
                        .param("maxDate", "2024-12-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getBadSellers_withInvalidRequestBody_returnsBadRequest() throws Exception {

        mockMvc.perform(get("/apiV1/analytics/getBadSellers")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getSellerBestPeriod_withValidSellerId_returnsBestPeriod() throws Exception {
        when(analyticsService.getSellerBestPeriod(eq(1L))).thenReturn(bestPeriodResponse);

        mockMvc.perform(get("/apiV1/analytics/getSellerBestPeriod")
                        .param("sellerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startOfPeriod").exists())
                .andExpect(jsonPath("$.endOfPeriod").exists())
                .andExpect(jsonPath("$.density").value(2.5));

    }

    @Test
    void getSellerBestPeriod_withNullSellerId_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/analytics/getSellerBestPeriod"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSellerBestPeriod_withNonExistentSeller_returnsNotFound() throws Exception {
        when(analyticsService.getSellerBestPeriod(999L))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Не было найдено продавца с id 999"));

        mockMvc.perform(get("/apiV1/analytics/getSellerBestPeriod")
                        .param("sellerId", "999"))
                .andExpect(status().isNotFound());

        verify(analyticsService, times(1)).getSellerBestPeriod(999L);
    }

    @Test
    void getSellerBestPeriod_withFutureRegistrationDate_returnsBadRequest() throws Exception {
        when(analyticsService.getSellerBestPeriod(1L))
                .thenThrow(new IllegalArgumentException("Дата регистрации продавца не может быть больше текущей"));

        mockMvc.perform(get("/apiV1/analytics/getSellerBestPeriod")
                        .param("sellerId", "1"))
                .andExpect(status().isBadRequest());

        verify(analyticsService, times(1)).getSellerBestPeriod(1L);
    }

    @Test
    void getSellerBestPeriod_withNoTransactions_returnsNotFound() throws Exception {
        when(analyticsService.getSellerBestPeriod(eq(1L)))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Не было найдено транзакций у данного продавца"));

        mockMvc.perform(get("/apiV1/analytics/getSellerBestPeriod")
                        .param("sellerId", "1"))
                .andExpect(status().isNotFound());

    }

    @Test
    void getBadSellers_withEmptyResult_returnsEmptyPage() throws Exception {
        Page<SellerResponse> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(analyticsService.getBadSellers(eq(0), eq(50), any(BigDecimal.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/apiV1/analytics/getBadSellers")
                        .param("page", "0")
                        .param("size", "10")
                        .param("minAmount", "1000.00")
                        .param("minDate", "2024-01-01")
                        .param("maxDate", "2024-12-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));

    }

    @Test
    void getSellerBestPeriod_verifiesDataIntegrity() throws Exception {
        LocalDate startDate = LocalDate.now().minusDays(5);
        LocalDate endDate = LocalDate.now().minusDays(3);
        SellerBestPeriodResponse response = SellerBestPeriodResponse.builder()
                .startOfPeriod(startDate)
                .endOfPeriod(endDate)
                .density(3.0)
                .build();

        when(analyticsService.getSellerBestPeriod(eq(1L))).thenReturn(response);

        mockMvc.perform(get("/apiV1/analytics/getSellerBestPeriod")
                        .param("sellerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startOfPeriod").value(startDate.toString()))
                .andExpect(jsonPath("$.endOfPeriod").value(endDate.toString()))
                .andExpect(jsonPath("$.density").value(3.0));

    }


}
