package ru.Tim.ShiftCRM.api.swagger.OpenApi;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import ru.Tim.ShiftCRM.api.model.seller.request.NewSellerRequest;
import ru.Tim.ShiftCRM.api.model.seller.responce.SellerResponse;
import ru.Tim.ShiftCRM.api.model.seller.request.UpdatedSellerRequest;

@Tag(name = "Seller", description = "Управление продавцами")
public interface SellerApi {

    @Operation(
            summary = "Получение всех продавцов",
            description = "Возвращает Страницу \"Page\" с продавцами",
            responses = {
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
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Ошибка валидации",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = ErrorResponse.class,
                                            description = "Вывод ошибки параметра",
                                            examples =
                                                    """
                                                            {
                                                                "message": "Параметр size должен быть больше 0"
                                                            }
                                                    """
                                    )
                            )
                    )
            }
    )
    ResponseEntity<Page<SellerResponse>> getAll(
            @Parameter(name = "page", description = "Номер страницы")
            @RequestParam(defaultValue = "0")
            @PositiveOrZero(message = "Параметр page должен быть больше или равен 0")
            int page,
            @Parameter(name = "size", description = "Размер страницы")
            @RequestParam(defaultValue = "50")
            @Positive(message = "Параметр size должен быть больше 0")
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
                                            implementation = SellerResponse.class,
                                            description = "response",
                                            example =
                        """
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
                                            implementation = ErrorResponse.class,
                                            description = "response message",
                                            examples =
                                                    """
                                                            {
                                                                "message": "Не было найдено продавца с id 1"
                                                            }
                                                    """
                                    )
                            )
                    )
            }
    )
    ResponseEntity<SellerResponse> getSellerInfo(
            @Parameter(description = "Id продавца")
            @PathVariable
            @NotNull(message = "Переменная пути id не должна быть Null")
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
                                            implementation = SellerResponse.class,
                                            description = "response message",
                                            example =
                                                    """
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
                            responseCode = "409",
                            description = "Неуспешная попытка создания из-за существования продавца с такой контактной информацией",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = ErrorResponse.class,
                                            description = "response message",
                                            examples =
                                                    """
                                                            {
                                                                "message": "Продавец с контактной информацией +79538848834 существует"
                                                            }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Поле в теле запроса не валидно",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Object.class,
                                            description = "Невалидное поле - ошибка",
                                            examples =
                                                    """
                                                            {
                                                                "name": "не должно равняться null"
                                                            }
                                                    """
                                    )
                            )
                    )
            }
    )
    ResponseEntity<SellerResponse> createSeller(@RequestBody NewSellerRequest newSellerRequest);

    @Operation(
            summary = "Обновление информации о продавце по его id",
            description = "Принимает в параметре данные о продавце в формате json и обновляет информацию",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное обновление продавца",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = SellerResponse.class,
                                            description = "response message",
                                            example =
                                                    """
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
                            description = "Продавец не был найден",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = ErrorResponse.class,
                                            description = "response message",
                                            example =
                                                    """
                                                        {
                                                            "message": "Продавец с id 1 не был найден"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Неуспешная попытка обновления из-за существования продавца с такой контактной информацией",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = ErrorResponse.class,
                                            description = "response message",
                                            example =
                                                    """
                                                        {
                                                            "message": "Продавец с контактной информацией +79538848834 существует"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Поле в теле запроса не валидно",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Object.class,
                                            description = "Невалидное поле - ошибка",
                                            examples =
                                                    """
                                                            {
                                                                "name": "не должно равняться null"
                                                            }
                                                    """
                                    )
                            )
                    )
            }
    )
    ResponseEntity<SellerResponse> updateSeller(
            @PathVariable
            @NotNull(message = "Переменная пути id не должна быть Null")
            Long id,
            @RequestBody
            UpdatedSellerRequest updatedSellerRequest
    );

    @Operation(
            summary = "Удаление продавца",
            description = "Удаляет продавца из базы данных по его id",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Успешное удаление продавца"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Продавец не был найден",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = ErrorResponse.class,
                                            description = "response message",
                                            example =
                                                    """
                                                        {
                                                            "message": "Не было найдено продавца с id 1
                                                        }
                                                    """
                                    )
                            )
                    )
            }
    )
    ResponseEntity<Void> deleteSeller(@PathVariable Long id);
}
