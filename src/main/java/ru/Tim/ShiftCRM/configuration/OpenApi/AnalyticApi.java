package ru.Tim.ShiftCRM.configuration.OpenApi;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.Tim.ShiftCRM.annotation.validation.IsEnum;
import ru.Tim.ShiftCRM.dto.analytics.request.BadSellerRequest;
import ru.Tim.ShiftCRM.dto.analytics.response.SellerBestPeriodResponse;
import ru.Tim.ShiftCRM.dto.analytics.response.TopSellerResponse;
import ru.Tim.ShiftCRM.dto.seller.responce.SellerDto;
import ru.Tim.ShiftCRM.enums.DatePeriod;


@Tag(name = "Analytic", description = "Аналитические методы")
public interface AnalyticApi {

    @Operation(
            summary = "Получение лучшего продавца за период",
            description = "Возвращает информацию о лучшем продавце за указанный временной период",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный запрос",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = TopSellerResponse.class,
                                            description = "Информация о лучшем продавце",
                                            example = """
                            {
                                {
                                    "id": 1,
                                    "name": "Иван Петров",
                                    "contactInfo": "ev@gmail.com,
                                    "registrationDate": "2023-05-15T10:30:00"
                                },
                                "sellerAmount":10000.00
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
                                            implementation = String.class,
                                            description = "response",
                                            example = "Допустимые значения enum"
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
                            allowableValues = {"DAY", "WEEK", "MONTH", "QUOTER", "YEAR"}
                    )
            )
            @IsEnum(enumClass = DatePeriod.class,
                    message = "Поле должно быть: DAY, WEEK, MONTH, QUOTER, YEAR")
            String datePeriodType
    );

    @Operation(
            summary = "Получение списка худших продавцов",
            description = "Возвращает страницу с продавцами, показавшими продажи меньше указаной суммы за период",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный запрос",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Object.class,
                                            description = "Page<SellerDto> с информацией о продавцах",
                                            example = """
                            {
                                "content": [
                                    {
                                        "id": 1,
                                        "name": "Иван Петров",
                                        "contactInfo": "ev@gmail.com,
                                        "registrationDate": "2023-05-15T10:30:00"
                                    }
                                ],
                                "page": {
                                    "size": 50,
                                    "number": 0,
                                    "totalElements": 1,
                                    "totalPages": 1
                                }
                            }
                        """
                                    )
                            )
                    )
            }
    )
    ResponseEntity<Page<SellerDto>> getBadSellers(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0")
            @PositiveOrZero
            int page,

            @Parameter(description = "Размер страницы", example = "50")
            @RequestParam(defaultValue = "50")
            @Positive
            int size,

            @Parameter(description = "Критерии для поиска худших продавцов")
            @Validated @RequestBody
            BadSellerRequest badSellerRequest
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
                                            example = """
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
                                            implementation = String.class,
                                            description = "Сообщение об ошибке",
                                            example = "Не было найдено продовца с id 1"
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
            @NotNull
            Long sellerId
    );
}
