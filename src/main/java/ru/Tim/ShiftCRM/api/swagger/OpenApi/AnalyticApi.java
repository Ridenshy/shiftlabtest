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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import ru.Tim.ShiftCRM.api.annotation.IsEnum;
import ru.Tim.ShiftCRM.api.model.analytics.SellerBestPeriodResponse;
import ru.Tim.ShiftCRM.api.model.analytics.TopSellerResponse;
import ru.Tim.ShiftCRM.api.model.seller.responce.SellerResponse;
import ru.Tim.ShiftCRM.core.enums.DatePeriod;

import java.math.BigDecimal;
import java.time.LocalDate;


@Tag(name = "Analytic", description = "Аналитические методы")
public interface AnalyticApi {

    @Operation(
            summary = "Получение лучшего продавца за период",
            description = "Возвращает информацию о лучшем продавце за указанный период",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный запрос",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = TopSellerResponse.class,
                                            description = "Информация о лучшем продавце",
                                            example =
                                                    """
                                                            {
                                                                "topSeller": {
                                                                    "id": 2,
                                                                    "name": "Евгений2",
                                                                    "contactInfo": "+79538848834",
                                                                    "registrationDate": "2025-08-25T22:39:07.610133"
                                                                },
                                                                "sellerAmount": 6000
                                                            }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Не валидный параметр периода",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = ErrorResponse.class,
                                            description = "response",
                                            examples =
                                                    """
                                                    {
                                                        "message": "Поле должно быть: DAY, WEEK, MONTH, QUARTER, YEAR"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    ResponseEntity<TopSellerResponse> getTopSeller(
            @Parameter(
                    description = "Тип временного периода",
                    required = true,
                    example = "WEEK",
                    schema = @Schema(
                            type = "string",
                            allowableValues = {"DAY", "WEEK", "MONTH", "QUARTER", "YEAR"}
                    )
            )
            @RequestParam
            @NotNull(message = "Параметр datePeriodType не должно быть Null")
            @IsEnum(enumClass = DatePeriod.class,
                    message = "Параметр datePeriodType должно быть: DAY, WEEK, MONTH, QUARTER, YEAR")
            String datePeriodType
    );

    @Operation(
            summary = "Получение списка худших продавцов",
            description = "Возвращает страницу с продавцами, показавшими продажи меньше указанной суммы за период",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный запрос",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Page.class,
                                            description = "Page из SellerDto с информацией о продавцах",
                                            example =
                                                    """
                                                            {
                                                                "content": [
                                                                    {
                                                                        "id": 1,
                                                                        "name": "Евгений",
                                                                        "contactInfo": "+79538848833",
                                                                        "registrationDate": "2025-08-25T22:21:36.790148"
                                                                    },
                                                                    {
                                                                        "id": 2,
                                                                        "name": "Евгений2",
                                                                        "contactInfo": "+79538848834",
                                                                        "registrationDate": "2025-08-25T22:39:07.610133"
                                                                    },
                                                                    {
                                                                        "id": 3,
                                                                        "name": "Евгений3",
                                                                        "contactInfo": "+79538848835",
                                                                        "registrationDate": "2025-08-25T22:43:50.607108"
                                                                    }
                                                                ],
                                                                "page": {
                                                                    "size": 50,
                                                                    "number": 0,
                                                                    "totalElements": 3,
                                                                    "totalPages": 1
                                                                }
                                                            }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Параметр не валидный",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = ErrorResponse.class,
                                            description = "Невалидное поле - ошибка",
                                            examples =
                                                    """
                                                            {
                                                                "message": "Параметр minDate не должен быть Null"
                                                            }
                                                    """
                                    )
                            )
                    )
            }
    )
    ResponseEntity<Page<SellerResponse>> getBadSellers(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0")
            @PositiveOrZero(message = "Параметр page должен быть больше или равен 0")
            int page,

            @Parameter(description = "Размер страницы", example = "50")
            @RequestParam(defaultValue = "50")
            @Positive(message = "Параметр size должен быть больше 0")
            int size,

            @Parameter(description = "Минимальная сумма", example = "1000.00")
            @RequestParam
            @NotNull(message = "Параметр minAmount не должен быть Null")
            BigDecimal minAmount,
            @Parameter(description = "Минимальная дата", example = "2024-12-12")
            @RequestParam
            @NotNull(message = "Параметр minDate не должен быть Null")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate minDate,
            @Parameter(description = "Максимальная дата", example = "2025-12-12")
            @NotNull(message = "Параметр maxDate не должен быть Null")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate maxDate
    );

    @Operation(
            summary = "Получение лучшего периода для продавца",
            description = "Возвращает информацию о периоде, когда продавец показал наилучшие результаты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный запрос",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = SellerBestPeriodResponse.class,
                                            description = "Информация о лучшем периоде продавца",
                                            example =
                                                    """
                                                        {
                                                            "startOfPeriod": "2024-01-01",
                                                            "endOfPeriod": "2024-01-05",
                                                            "density": 12.0
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Продавец не найден",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = ErrorResponse.class,
                                            description = "Сообщение об ошибке",
                                            examples =
                                                    """
                                                            {
                                                                "message": "Не было найдено продавца с id 1"
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
                                            description = "Сообщение об ошибке",
                                            examples =
                                                    """
                                                            {
                                                                "message": "Параметр sellerId не должен быть Null"
                                                            }
                                                    """
                                    )
                            )
                    )
            }
    )
    ResponseEntity<SellerBestPeriodResponse> getSellerBestPeriod(
            @Parameter(
                    description = "ID продавца",
                    required = true,
                    example = "1"
            )
            @RequestParam
            @NotNull(message = "Параметр sellerId не должен быть Null")
            Long sellerId
    );
}
