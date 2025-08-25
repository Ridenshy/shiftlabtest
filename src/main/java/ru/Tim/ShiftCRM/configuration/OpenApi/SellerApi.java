package ru.Tim.ShiftCRM.configuration.OpenApi;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Tim.ShiftCRM.dto.seller.request.NewSellerDto;
import ru.Tim.ShiftCRM.dto.seller.responce.SellerDto;
import ru.Tim.ShiftCRM.dto.seller.request.UpdatedSellerDto;

@Tag(name = "Seller", description = "Управление продавцами")
public interface SellerApi {

    @Operation(
            summary = "Получение всех продавцов",
            description = "Возвращает Страницу \"Page\" с продавцами"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Успешный запрос",
            content = @Content(
                    schema = @Schema(
                            implementation = Object.class,
                            description = "Page<SellerDto>",
                            example = """
                    {
                        "content": [
                            {
                                "id": 1,
                                "name": "Александр",
                                "contactInfo": "+79538848834",
                                "registrationDate": "2022-08-24T17:25:38.319521"
                            },
                            {
                                "id": 2,
                                "name": "Евгений",
                                "contactInfo": "evg@gmail.com",
                                "registrationDate": "2025-08-24T17:37:08.977665"
                            }
                        ],
                        "page": {
                            "size": 10,
                            "number": 0,
                            "totalElements": 2,
                            "totalPages": 1
                        }
                    }
                """
                    )
            )
    )
    ResponseEntity<Page<SellerDto>> getAll(
            @RequestParam(defaultValue = "0")
            @PositiveOrZero
            @Parameter(name = "page", description = "Номер страницы")
            int page,
            @RequestParam(defaultValue = "50")
            @Positive
            @Parameter(name = "size", description = "Размер страницы")
            int size
    );

    @Operation(
            summary = "Получение информации о продавце",
            description = "Возвращает Seller по его id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный запрос",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = SellerDto.class,
                                            description = "SellerDto",
                                            example = """
                            {
                                "id": 1,
                                "name": "Александр",
                                "contactInfo": "+79538848834",
                                "registrationDate": "2022-08-24T17:25:38.319521"
                            }
                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "В базе данных нет пользователя с таким id",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "response message",
                                            example = "Не найдено продовца с id 1"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<SellerDto> getSellerInfo(
            @PathVariable
            @Parameter(description = "Id продавца")
            Long id
    );

    @Operation(
            summary = "Создание продавца",
            description = "Принимает в теле параметры продавца в формате Json для сохранения в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Успешное создание",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "response message",
                                            example = "Продацец с id %d создан"
                                    )
                            )

                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Неуспешная попытка создания из-за существования продавца с такой контактной информацией",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "response message",
                                            example = "Продавец с контактной информацией +79538848834 существует"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<String> createSeller(@RequestBody NewSellerDto newSellerDto);

    @Operation(
            summary = "Обновление информации о продавце по его id",
            description = "Принимает в параметре данные о продавце в формате json и обновляет информацию",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное обновление продавца",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "response message",
                                            example = "Продавец c id 1 обновлен"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Продавец не был найден",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "response message",
                                            example = "Не было найдено продовца с id 1"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Неуспешная попытка обновления из-за существования продавца с такой контактной информацией",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "response message",
                                            example = "Продавец с контактной информацией +79538848834 существует"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<String> updateSeller(
            @PathVariable Long id,
            @RequestBody UpdatedSellerDto updatedSellerDto
    );

    @Operation(
            summary = "Удаление продавца",
            description = "Удаляет продавца из базы данных по его id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление продавца",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "response message",
                                            example = "Продавец с id 1 удален"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Продавец не был найден",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "response message",
                                            example = "Не было найдено продовца с id 1"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<String> deleteSeller(@PathVariable Long id);
}
