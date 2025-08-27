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
import ru.Tim.ShiftCRM.api.dto.seller.request.NewSellerDto;
import ru.Tim.ShiftCRM.api.dto.seller.request.UpdatedSellerDto;
import ru.Tim.ShiftCRM.api.dto.seller.responce.SellerDto;
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

    private SellerDto sellerDto;
    private NewSellerDto newSellerDto;
    private UpdatedSellerDto updatedSellerDto;

    @BeforeEach
    void setUp() {
        sellerDto = SellerDto.builder()
                .id(1L)
                .name("Евгений")
                .contactInfo("e@mail.ru")
                .registrationDate(LocalDateTime.now().minusDays(5))
                .build();

        newSellerDto = new NewSellerDto("Новый", "test@mail.ru");

        updatedSellerDto = new UpdatedSellerDto("Обновленный", "updated@mail.ru");
    }

    @Test
    void getAll_withCorrectData_returnsAllSellersPage() throws Exception {
        List<SellerDto> sellers = List.of(sellerDto);
        Page<SellerDto> page = new PageImpl<>(sellers, PageRequest.of(0, 10), sellers.size());

        when(sellerService.getAllSellers(0, 10)).thenReturn(page);

        mockMvc.perform(get("/apiV1/seller/getAll")
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
        List<SellerDto> sellers = List.of(sellerDto);
        Page<SellerDto> page = new PageImpl<>(sellers, PageRequest.of(0, 50), sellers.size());

        when(sellerService.getAllSellers(0, 50)).thenReturn(page);

        mockMvc.perform(get("/apiV1/seller/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        verify(sellerService, times(1)).getAllSellers(0, 50);
    }

    @Test
    void getAll_withInvalidPage_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/seller/getAll")
                        .param("page", "-1")
                        .param("size", "10")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_withInvalidSize_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/seller/getAll")
                        .param("page", "0")
                        .param("size", "0")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSellerInfo_withValidId_returnsSeller() throws Exception {
        when(sellerService.getSellerInfo(1L)).thenReturn(sellerDto);

        mockMvc.perform(get("/apiV1/seller/getInfo/{id}", 1L))
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

        mockMvc.perform(get("/apiV1/seller/getInfo/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(sellerService, times(1)).getSellerInfo(999L);
    }

    @Test
    void getSellerInfo_withNullId_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/apiV1/seller/getInfo/"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createSeller_withValidData_returnsCreated() throws Exception {
        when(sellerService.saveNewSeller(any(NewSellerDto.class))).thenReturn(1L);

        mockMvc.perform(post("/apiV1/seller/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSellerDto)))
                .andExpect(status().isCreated());

        verify(sellerService, times(1)).saveNewSeller(any(NewSellerDto.class));
    }

    @Test
    void createSeller_withInvalidData_returnsBadRequest() throws Exception {
        NewSellerDto invalidDto = new NewSellerDto("", "");

        mockMvc.perform(post("/apiV1/seller/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void createSeller_withDuplicateContactInfo_returnsConflict() throws Exception {
        when(sellerService.saveNewSeller(any(NewSellerDto.class)))
                .thenThrow(new ContactInfoAlreadyExistsException("Продавец с контактной информацией test@mail.ru существует"));

        mockMvc.perform(post("/apiV1/seller/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSellerDto)))
                .andExpect(status().isConflict());

    }

    @Test
    void updateSeller_withValidData_returnsOk() throws Exception {
        doNothing().when(sellerService).updateSeller(any(UpdatedSellerDto.class), eq(1L));

        mockMvc.perform(patch("/apiV1/seller/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSellerDto)))
                .andExpect(status().isOk());

        verify(sellerService, atLeastOnce()).updateSeller(any(UpdatedSellerDto.class), eq(1L));
    }

    @Test
    void updateSeller_withNonExistentId_returnsNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Не было найдено продавца с id 999"))
                .when(sellerService).updateSeller(any(UpdatedSellerDto.class), eq(999L));

        mockMvc.perform(patch("/apiV1/seller/update/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSellerDto)))
                .andExpect(status().isNotFound());

    }

    @Test
    void updateSeller_withDuplicateContactInfo_returnsConflict() throws Exception {
        doThrow(new ContactInfoAlreadyExistsException("Продавец с контактной информацией updated@mail.ru существует"))
                .when(sellerService).updateSeller(any(UpdatedSellerDto.class), eq(1L));

        mockMvc.perform(patch("/apiV1/seller/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSellerDto)))
                .andExpect(status().isConflict());

        verify(sellerService, times(1)).updateSeller(any(UpdatedSellerDto.class), eq(1L));
    }

    @Test
    void updateSeller_withInvalidData_returnsBadRequest() throws Exception {
        UpdatedSellerDto invalidDto = new UpdatedSellerDto("", ""); // пустые поля

        mockMvc.perform(patch("/apiV1/seller/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void deleteSeller_withValidId_returnsOk() throws Exception {
        doNothing().when(sellerService).deleteSeller(1L);

        mockMvc.perform(delete("/apiV1/seller/delete/{id}", 1L))
                .andExpect(status().isOk());

        verify(sellerService, times(1)).deleteSeller(1L);
    }

    @Test
    void deleteSeller_withNonExistentId_returnsNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Не было найдено продавца с id 999"))
                .when(sellerService).deleteSeller(999L);

        mockMvc.perform(delete("/apiV1/seller/delete/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(sellerService, times(1)).deleteSeller(999L);
    }

    @Test
    void deleteSeller_withNullId_returnsBadRequest() throws Exception {
        mockMvc.perform(delete("/apiV1/seller/delete/"))
                .andExpect(status().is4xxClientError());

        verify(sellerService, never()).deleteSeller(anyLong());
    }

}
