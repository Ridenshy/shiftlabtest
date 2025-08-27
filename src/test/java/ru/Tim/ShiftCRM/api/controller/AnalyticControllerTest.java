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
import ru.Tim.ShiftCRM.api.dto.analytics.request.BadSellerRequest;
import ru.Tim.ShiftCRM.api.dto.analytics.response.SellerBestPeriodResponse;
import ru.Tim.ShiftCRM.api.dto.analytics.response.TopSellerResponse;
import ru.Tim.ShiftCRM.api.dto.seller.responce.SellerDto;
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

    private SellerDto sellerDto;
    private TopSellerResponse topSellerResponse;
    private SellerBestPeriodResponse bestPeriodResponse;
    private BadSellerRequest badSellerRequest;

    @BeforeEach
    void setUp() {
        sellerDto = SellerDto.builder()
                .id(1L)
                .name("Евгений")
                .contactInfo("e@mail.ru")
                .registrationDate(LocalDateTime.now().minusDays(5))
                .build();

        topSellerResponse = TopSellerResponse.builder()
                .topSeller(sellerDto)
                .sellerAmount(BigDecimal.valueOf(10000))
                .build();

        bestPeriodResponse = SellerBestPeriodResponse.builder()
                .startOfPeriod(LocalDate.now().minusDays(5))
                .endOfPeriod(LocalDate.now().minusDays(3))
                .density(2.5)
                .build();

        badSellerRequest = new BadSellerRequest(
                BigDecimal.valueOf(1000),
                LocalDate.now().minusDays(30),
                LocalDate.now()
        );
    }

    @Test
    void getTopSeller_withValidPeriod_returnsTopSeller() throws Exception {

        when(analyticsService.getBestSeller("DAY")).thenReturn(topSellerResponse);

        mockMvc.perform(get("/apiV1/analytic/getTopSeller/")
                        .param("datePeriodType", "DAY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topSeller.id").value(1L))
                .andExpect(jsonPath("$.topSeller.name").value("Евгений"))
                .andExpect(jsonPath("$.sellerAmount").value(10000));

        verify(analyticsService, times(1)).getBestSeller("DAY");
    }

    @Test
    void getTopSeller_withInvalidPeriod_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/analytic/getTopSeller/")
                        .param("datePeriodType", "INVALID"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTopSeller_withNullPeriod_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/analytic/getTopSeller/"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTopSeller_withNoData_returnsNotFound() throws Exception {
        when(analyticsService.getBestSeller("DAY"))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Не было найдено продавца за заданный период"));

        mockMvc.perform(get("/apiV1/analytic/getTopSeller/")
                        .param("datePeriodType", "DAY"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBadSellers_withValidRequest_returnsBadSellersPage() throws Exception {
        List<SellerDto> sellers = List.of(sellerDto);
        Page<SellerDto> page = new PageImpl<>(sellers, PageRequest.of(0, 10), sellers.size());

        when(analyticsService.getBadSellers(eq(0), eq(10), any(BadSellerRequest.class))).thenReturn(page);

        mockMvc.perform(get("/apiV1/analytic/getBadSellers")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badSellerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1L));

        verify(analyticsService, times(1)).getBadSellers(eq(0), eq(10), any(BadSellerRequest.class));
    }

    @Test
    void getBadSellers_withDefaultParameters_usesDefaultValues() throws Exception {
        List<SellerDto> sellers = List.of(sellerDto);
        Page<SellerDto> page = new PageImpl<>(sellers, PageRequest.of(0, 50), sellers.size());

        when(analyticsService.getBadSellers(eq(0), eq(50), any(BadSellerRequest.class))).thenReturn(page);

        mockMvc.perform(get("/apiV1/analytic/getBadSellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badSellerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        verify(analyticsService, times(1)).getBadSellers(eq(0), eq(50), any(BadSellerRequest.class));
    }

    @Test
    void getBadSellers_withInvalidPage_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/analytic/getBadSellers")
                        .param("page", "-1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badSellerRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getBadSellers_withInvalidSize_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/analytic/getBadSellers")
                        .param("page", "0")
                        .param("size", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badSellerRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getBadSellers_withInvalidRequestBody_returnsBadRequest() throws Exception {
        BadSellerRequest invalidRequest = new BadSellerRequest(null, null, null);

        mockMvc.perform(get("/apiV1/analytic/getBadSellers")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getSellerBestPeriod_withValidSellerId_returnsBestPeriod() throws Exception {
        when(analyticsService.getSellerBestPeriod(eq(1L))).thenReturn(bestPeriodResponse);

        mockMvc.perform(get("/apiV1/analytic/getSellerBestPeriod")
                        .param("sellerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startOfPeriod").exists())
                .andExpect(jsonPath("$.endOfPeriod").exists())
                .andExpect(jsonPath("$.density").value(2.5));

    }

    @Test
    void getSellerBestPeriod_withNullSellerId_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/analytic/getSellerBestPeriod"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSellerBestPeriod_withNonExistentSeller_returnsNotFound() throws Exception {
        when(analyticsService.getSellerBestPeriod(999L))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Не было найдено продавца с id 999"));

        mockMvc.perform(get("/apiV1/analytic/getSellerBestPeriod")
                        .param("sellerId", "999"))
                .andExpect(status().isNotFound());

        verify(analyticsService, times(1)).getSellerBestPeriod(999L);
    }

    @Test
    void getSellerBestPeriod_withFutureRegistrationDate_returnsBadRequest() throws Exception {
        when(analyticsService.getSellerBestPeriod(1L))
                .thenThrow(new IllegalArgumentException("Дата регистрации продавца не может быть больше текущей"));

        mockMvc.perform(get("/apiV1/analytic/getSellerBestPeriod")
                        .param("sellerId", "1"))
                .andExpect(status().isBadRequest());

        verify(analyticsService, times(1)).getSellerBestPeriod(1L);
    }

    @Test
    void getSellerBestPeriod_withNoTransactions_returnsNotFound() throws Exception {
        when(analyticsService.getSellerBestPeriod(eq(1L)))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Не было найдено транзакций у данного продавца"));

        mockMvc.perform(get("/apiV1/analytic/getSellerBestPeriod")
                        .param("sellerId", "1"))
                .andExpect(status().isNotFound());

    }

    @Test
    void getBadSellers_withEmptyResult_returnsEmptyPage() throws Exception {
        Page<SellerDto> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(analyticsService.getBadSellers(eq(0), eq(10), any(BadSellerRequest.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/apiV1/analytic/getBadSellers")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badSellerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));

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

        mockMvc.perform(get("/apiV1/analytic/getSellerBestPeriod")
                        .param("sellerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startOfPeriod").value(startDate.toString()))
                .andExpect(jsonPath("$.endOfPeriod").value(endDate.toString()))
                .andExpect(jsonPath("$.density").value(3.0));

    }


}
