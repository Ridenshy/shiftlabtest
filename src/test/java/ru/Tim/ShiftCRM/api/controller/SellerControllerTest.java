package ru.Tim.ShiftCRM.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.Tim.ShiftCRM.api.model.seller.request.NewSellerRequest;
import ru.Tim.ShiftCRM.api.model.seller.request.UpdatedSellerRequest;
import ru.Tim.ShiftCRM.api.model.seller.responce.SellerResponse;
import ru.Tim.ShiftCRM.api.exception.ContactInfoAlreadyExistsException;
import ru.Tim.ShiftCRM.config.ControllerConfiguration;
import ru.Tim.ShiftCRM.core.service.SellerService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SellerController.class)
@Import(ControllerConfiguration.class)
public class SellerControllerTest {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private SellerResponse sellerResponse;
    private NewSellerRequest newSellerRequest;
    private UpdatedSellerRequest updatedSellerRequest;

    @BeforeEach
    void setUp() {
        sellerResponse = SellerResponse.builder()
                .id(1L)
                .name("Евгений")
                .contactInfo("e@mail.ru")
                .registrationDate(LocalDateTime.now().minusDays(5))
                .build();

        newSellerRequest = new NewSellerRequest("Новый", "test@mail.ru");

        updatedSellerRequest = new UpdatedSellerRequest("Обновленный", "updated@mail.ru");
    }

    @Test
    void getAll_withCorrectData_returnsAllSellersPage() throws Exception {
        List<SellerResponse> sellers = List.of(sellerResponse);
        Page<SellerResponse> page = new PageImpl<>(sellers, PageRequest.of(0, 10), sellers.size());

        when(sellerService.getAllSellers(0, 10)).thenReturn(page);

        mockMvc.perform(get("/apiV1/sellers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Евгений"));

        verify(sellerService, times(1)).getAllSellers(0, 10);
    }

    @Test
    void getAll_withDefaultParameters_usesDefaultValues() throws Exception {
        List<SellerResponse> sellers = List.of(sellerResponse);
        Page<SellerResponse> page = new PageImpl<>(sellers, PageRequest.of(0, 50), sellers.size());

        when(sellerService.getAllSellers(0, 50)).thenReturn(page);

        mockMvc.perform(get("/apiV1/sellers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        verify(sellerService, times(1)).getAllSellers(0, 50);
    }

    @Test
    void getAll_withInvalidPage_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/sellers")
                        .param("page", "-1")
                        .param("size", "10")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_withInvalidSize_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/sellers")
                        .param("page", "0")
                        .param("size", "0")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSellerInfo_withValidId_returnsSeller() throws Exception {
        when(sellerService.getSellerInfo(1L)).thenReturn(sellerResponse);

        mockMvc.perform(get("/apiV1/sellers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Евгений"))
                .andExpect(jsonPath("$.contactInfo").value("e@mail.ru"));

        verify(sellerService, times(1)).getSellerInfo(1L);
    }

    @Test
    void getSellerInfo_withNonExistentId_returnsNotFound() throws Exception {
        when(sellerService.getSellerInfo(999L))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Не было найдено продавца с id 999"));

        mockMvc.perform(get("/apiV1/sellers/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(sellerService, times(1)).getSellerInfo(999L);
    }

    @Test
    void getSellerInfo_withNullId_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/sellers/"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createSeller_withValidData_returnsCreated() throws Exception {
        when(sellerService.saveNewSeller(any(NewSellerRequest.class))).thenReturn(any(SellerResponse.class));

        mockMvc.perform(post("/apiV1/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSellerRequest)))
                .andExpect(status().isCreated());

        verify(sellerService, times(1)).saveNewSeller(any(NewSellerRequest.class));
    }

    @Test
    void createSeller_withInvalidData_returnsBadRequest() throws Exception {
        NewSellerRequest invalidDto = new NewSellerRequest("", "");

        mockMvc.perform(post("/apiV1/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void createSeller_withDuplicateContactInfo_returnsConflict() throws Exception {
        when(sellerService.saveNewSeller(any(NewSellerRequest.class)))
                .thenThrow(new ContactInfoAlreadyExistsException("Продавец с контактной информацией test@mail.ru существует"));

        mockMvc.perform(post("/apiV1/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSellerRequest)))
                .andExpect(status().isConflict());

    }

    @Test
    void updateSeller_withValidData_returnsOk() throws Exception {
        SellerResponse updatedSeller = SellerResponse.builder()
                .id(1L)
                .name("Updated Seller")
                .build();

        when(sellerService.updateSeller(any(UpdatedSellerRequest.class), eq(1L)))
                .thenReturn(updatedSeller);

        mockMvc.perform(patch("/apiV1/sellers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSellerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Seller"));

    }

    @Test
    void updateSeller_withNonExistentId_returnsNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Не было найдено продавца с id 999"))
                .when(sellerService).updateSeller(any(UpdatedSellerRequest.class), eq(999L));

        mockMvc.perform(patch("/apiV1/sellers/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSellerRequest)))
                .andExpect(status().isNotFound());

    }

    @Test
    void updateSeller_withDuplicateContactInfo_returnsConflict() throws Exception {
        doThrow(new ContactInfoAlreadyExistsException("Продавец с контактной информацией updated@mail.ru существует"))
                .when(sellerService).updateSeller(any(UpdatedSellerRequest.class), eq(1L));

        mockMvc.perform(patch("/apiV1/sellers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSellerRequest)))
                .andExpect(status().isConflict());

        verify(sellerService, times(1)).updateSeller(any(UpdatedSellerRequest.class), eq(1L));
    }

    @Test
    void updateSeller_withInvalidData_returnsBadRequest() throws Exception {
        UpdatedSellerRequest invalidDto = new UpdatedSellerRequest("", ""); // пустые поля

        mockMvc.perform(patch("/apiV1/sellers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void deleteSeller_withValidId_returnsOk() throws Exception {
        doNothing().when(sellerService).deleteSeller(1L);

        mockMvc.perform(delete("/apiV1/sellers/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(sellerService, times(1)).deleteSeller(1L);
    }

    @Test
    void deleteSeller_withNonExistentId_returnsNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Не было найдено продавца с id 999"))
                .when(sellerService).deleteSeller(999L);

        mockMvc.perform(delete("/apiV1/sellers/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(sellerService, times(1)).deleteSeller(999L);
    }

    @Test
    void deleteSeller_withNullId_returnsBadRequest() throws Exception {
        mockMvc.perform(delete("/apiV1/sellers/"))
                .andExpect(status().is4xxClientError());

        verify(sellerService, never()).deleteSeller(anyLong());
    }

}
