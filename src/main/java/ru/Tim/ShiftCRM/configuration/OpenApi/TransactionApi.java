package ru.Tim.ShiftCRM.configuration.OpenApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Tim.ShiftCRM.dto.transaction.request.NewTransactionDto;
import ru.Tim.ShiftCRM.dto.transaction.response.TransactionDto;

@Tag(name = "Transaction", description = "Управление транзакциями")
public interface TransactionApi {

    @Operation(
            summary = "Получение всех транзакций",
            description = "Возвращает страницу со всеми транзакциями в системе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный запрос",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Object.class,
                                            description = "Page<TransactionDto>",
                                            example =
                                    """
                                            {
                                                "content": [
                                                    {
                                                        "id": 1,
                                                        "sellerId": 3,
                                                        "amount": 3000,
                                                        "paymentType": "CARD",
                                                        "transactionDate": "2025-08-25T21:17:50.100457"
                                                    },
                                                    {
                                                        "id": 2,
                                                        "sellerId": 3,
                                                        "amount": 2000,
                                                        "paymentType": "CASH",
                                                        "transactionDate": "2025-08-25T21:17:56.530883"
                                                    }
                                                ],
                                                "page": {
                                                    "size": 50,
                                                    "number": 0,
                                                    "totalElements": 2,
                                                    "totalPages": 1
                                                }
                                            }
                                    """
                                    )
                            )
                    )
            }
    )
    ResponseEntity<Page<TransactionDto>> getAll(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0")
            int page,

            @Parameter(description = "Размер страницы", example = "50")
            @RequestParam(defaultValue = "50")
            int size
    );

    @Operation(
            summary = "Получение информации о транзакции",
            description = "Возвращает информацию о транзакции по её ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный запрос",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = TransactionDto.class,
                                            description = "TransactionDto",
                                            example = """
                            {
                                "id": 2,
                                "sellerId": 3,
                                "amount": 2000,
                                "paymentType": "CASH",
                                "transactionDate": "2025-08-25T21:17:56.530883"
                            }
                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Транзакция не найдена",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "Сообщение об ошибке",
                                            example = "Транзакция с id 1 не найдена"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<TransactionDto> getTransactionInfo(
            @Parameter(description = "ID транзакции", required = true, example = "1")
            @PathVariable
            Long id
    );

    @Operation(
            summary = "Создание новой транзакции",
            description = "Создает новую транзакцию",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Транзакция успешно создана",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "Сообщение об успехе",
                                            example = "Транзакция создана с id 15"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Невалидные данные транзакции",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = String.class,
                                            example = "Недпустимые значения enum"
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
                                            example = "Продавец с id 1 не найден"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<String> createTransaction(
            @Parameter(description = "Данные для создания транзакции", required = true)
            @RequestBody
            NewTransactionDto newTransactionDto
    );

    @Operation(
            summary = "Получение транзакций продавца",
            description = "Возвращает все транзакции конкретного продавца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный запрос",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Object.class,
                                            description = "Page<TransactionDto>",
                                            example =
                                  """
                                           {
                                               "content": [
                                                   {
                                                       "id": 1,
                                                       "sellerId": 3,
                                                       "amount": 3000,
                                                       "paymentType": "CARD",
                                                       "transactionDate": "2025-08-25T21:17:50.100457"
                                                   },
                                                   {
                                                       "id": 2,
                                                       "sellerId": 3,
                                                       "amount": 2000,
                                                       "paymentType": "CASH",
                                                       "transactionDate": "2025-08-25T21:17:56.530883"
                                                   }
                                               ],
                                               "page": {
                                                   "size": 50,
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
                            responseCode = "404",
                            description = "Продавец не найден",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "Сообщение об ошибке",
                                            example = "Продавец с id 1 не найден"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<Page<TransactionDto>> getSellerTransactions(
            @Parameter(description = "ID продавца", required = true, example = "5")
            @PathVariable
            Long id,

            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0")
            int page,

            @Parameter(description = "Размер страницы", example = "50")
            @RequestParam(defaultValue = "50")
            int size
    );
}
