package com.example.online_bank.service;

import com.example.online_bank.domain.dto.BuyCurrencyDto;
import com.example.online_bank.domain.dto.FinanceOperationDto;
import com.example.online_bank.domain.dto.OperationDtoResponse;
import com.example.online_bank.domain.dto.TransactionDto;
import com.example.online_bank.domain.entity.Account;
import com.example.online_bank.domain.model.AbstractBank;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.exception.TransferException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class BankIntegrationService {
    private final RestTemplate restTemplate;
    private final AbstractBank bank;
    private static final String HEADER_NAME = "token";
    private static final String POSTFIX_URL = "/operation/receive";
    private final AccountService accountService;
    private final ValidateFinanceService validateFinanceService;
    private final FinanceService financeService;
    private final ValidateAccountService validateAccountService;

    private static final int SENDER_INDEX = 0;
    private static final int RECIPIENT_INDEX = 1;

    public String getBankInfo() {
        return bank.getName();
    }

//    /**
//     * Перевод в партнерский банк(на другом порту или по одному)
//     * <p>
//     * На вход - имя банка, сумма, описание, фио от кого пришло.
//     *
//     * @param dto Cодержит информацию об отправителе и получателе,
//     *            количестве отправленных денег, описание и время операции.
//     *            <p>
//     *            Если названия банков отправителя и получателя одинаковы - то выполняется пополнение.
//     *            <p>
//     *            Если нет - метод отправит POST запрос по адресу - bank.partner.url + /operations/receive.
//     *            Делает списание со счета отправителя и начисление на счет получателя и создает две операции(операция
//     *            списания и начисления)
//     */
//    @Transactional
//    public List<OperationDtoResponse> transferMoney(TransactionDto dto) {
//        Account senderAccount = accountService.findByAccountNumber(dto.senderInfoDto().accountNumber());
//        Account recipientAccount = accountService.findByAccountNumber(dto.recipientInfoDto().accountNumber());
//
//        BigDecimal comparedAmount = compareAmount(recipientAccount, senderAccount, dto.amount());
//        List<FinanceOperationDto> financeDtos = createFinanceDtos(
//                senderAccount,
//                recipientAccount,
//                dto.description(),
//                dto.description(),
//                dto.amount(),
//                comparedAmount
//        );
//
//        return List.of(
//                financeService.withdrawMoney(getHolderToken(senderAccount), financeDtos.getFirst(), true),
//                executeRequestByBankNameMatch(dto, getHolderToken(recipientAccount), financeDtos.get(RECIPIENT_INDEX))
//        );
//    }

//    //TODO 26.03.2025: сделать рефакторинг списания и пополнения
//    @Transactional
//    public List<OperationDtoResponse> buyCurrency(BuyCurrencyDto dto, String token) {
//        validateAccountService.validateAccountExistsByUserToken(token);
//
//        Account baseAccount = accountService.findByAccountNumber(dto.baseAccountNumber());
//        Account targetAccount = accountService.findByAccountNumber(dto.targetAccountNumber());
//
//        BigDecimal comparedAmount = compareAmount(baseAccount, targetAccount, dto.amount());
//        List<String> descriptions = createBuyCurrencyDescriptions(dto);
//
//        List<FinanceOperationDto> financeDtos = createFinanceDtos(
//                baseAccount,
//                targetAccount,
//                descriptions.getFirst(),
//                descriptions.get(RECIPIENT_INDEX),
//                dto.amount(),
//                comparedAmount
//        );
//
//        return List.of(
//                financeService.withdrawMoney(getHolderToken(baseAccount), financeDtos.getFirst(), true),
//                financeService.depositMoney(token, financeDtos.get(RECIPIENT_INDEX), false)
//        );
//    }

    private List<String> createBuyCurrencyDescriptions(BuyCurrencyDto dto) {
        String baseAccountPostfix = "валюты со счета %s".formatted(dto.baseAccountNumber());
        String targetAccountPostfix = "валюты со счета %s".formatted(dto.targetAccountNumber());
        return List.of("Продажа %s".formatted(baseAccountPostfix), "Покупка %s".formatted(targetAccountPostfix));
    }

    private List<FinanceOperationDto> createFinanceDtos(
            Account baseAccount,
            Account targetAccount,
            String senderDescr,
            String recipientDescr,
            BigDecimal senderAmount,
            BigDecimal recipientAmount
    ) {
        return List.of(
                FinanceOperationDto.builder()
                        .accountNumber(baseAccount.getAccountNumber())
                        .description(senderDescr)
                        .amount(senderAmount)
                        .currencyCode(baseAccount.getCurrencyCode())
                        .build(),

                FinanceOperationDto.builder()
                        .accountNumber(targetAccount.getAccountNumber())
                        .description(recipientDescr)
                        .amount(recipientAmount)
                        .currencyCode(targetAccount.getCurrencyCode())
                        .build()
        );
    }

    /**
     * Получить токен пользователя
     * <p>
     * Из сущности счет достается пользователь, а уже потом токен пользователя
     *
     * @param entity Сущность Счет
     * @return Токен пользователя
     */
    private String getHolderToken(Account entity) {
        return "token";
        //entity.getHolder().getToken();
    }

    /**
     * Метод проверки кодов валюты между отправителем и получателем если код валюты был разный
     * <p>
     *
     * @param senderAccount    Счет отправителя
     * @param recipientAccount Счет получателя
     * @param amount           Количество денег
     * @return Сумму, которая была сконвертирована в валюту получателя (если код валют был разный)
     * либо изначальную сумму (если код валюты у отправителя и получателя был одинаковый)
     */
    private BigDecimal compareAmount(Account senderAccount, Account recipientAccount, BigDecimal amount) {
        CurrencyCode senderCurrencyCode = senderAccount.getCurrencyCode();
        CurrencyCode recipientCurrencyCode = recipientAccount.getCurrencyCode();
        return validateFinanceService
                .validateSenderAndRecipientCurrencyCode(senderCurrencyCode, recipientCurrencyCode, amount);
    }

    /**
     * Сравнить имена банков отправителя и получателя
     *
     * @param dto содержит в себе информацию об обоих клиентах, в том числе и название банка
     * @return true если имена одинаковые
     */
    private boolean compareBankName(TransactionDto dto) {
        String senderBankName = dto.senderInfoDto().bankName();
        String recipientBankName = dto.recipientInfoDto().bankName();
        return senderBankName.equals(recipientBankName);
    }

    /**
     * @param bank Банк, содержит в себе информацию о названии банка и url адресе партнерского порта
     *             производит конкатенацию строк, где первая содержит в себе порт приложения, а постфиксом является
     *             url из OperationController т.е создается адрес и мы как бы имитируем то, что мы открыли приложение
     *             с другим портом
     *             <p>
     *             Пример: мы разворачиваем приложение на порту 8080, а bank хранит в себе информацию о
     *             приложении развернутом на другом порте и с помощью банка мы получаем префикс этого порта и через
     *             одно приложение отправляем запрос другому приложению
     * @return сформированный url
     */
    private String createPartnerFullUrl(AbstractBank bank) {
        String partnerBankBaseUrl = bank.getUrl();
        return partnerBankBaseUrl + POSTFIX_URL;
    }

    /**
     * Создает запрос для отправки партнерскому банку
     *
     * @param dto         DTO с финансовой информацией, включает в себя номер счета, количество денег, описание и код валюты
     * @param url         Адрес запроса
     * @param headerValue Значение токена
     * @return requestEntity, который нужно отправить через restTemplate
     */
    private RequestEntity<FinanceOperationDto> createRequest(FinanceOperationDto dto, String url, String headerValue) {
        return RequestEntity.post(url)
                .header(HEADER_NAME, headerValue)
                .body(dto);
    }

    /**
     * @param requestBody тело для отправки запроса, содержит в себе номер счета,
     *                    количество денег, описание и код валюты
     *                    <p>
     *                    Отправляет запрос по сформированному адресу
     */
    private OperationDtoResponse exchangeRequest(RequestEntity<FinanceOperationDto> requestBody) {
        return restTemplate.exchange(requestBody, OperationDtoResponse.class).getBody();
    }

    private OperationDtoResponse createAndSendRequest(FinanceOperationDto recipientDto, String recipientToken) {
        String fullUrl = createPartnerFullUrl(bank);
        RequestEntity<FinanceOperationDto> request = createRequest(recipientDto, fullUrl, recipientToken);
        try {
            return exchangeRequest(request);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new TransferException("Ошибка при отправке перевода %s".formatted(e.getMessage()));
        }
    }

//    /**
//     * Этот метод по результату сравнения имен банка делает простое зачисление - если имена совпали,
//     * создает и отправляет запрос партнерскому банку если имена банков разнятся
//     *
//     * @param dto            DTO транзакции
//     * @param recipientToken Токен получателя
//     * @param recipientDto   Финансовое dto получателя
//     * @return DTO после завершения операции
//     */
//    private OperationDtoResponse executeRequestByBankNameMatch(TransactionDto dto, String recipientToken, FinanceOperationDto recipientDto) {
//        return compareBankName(dto) ? financeService.depositMoney(recipientToken, recipientDto, true)
//                : createAndSendRequest(recipientDto, recipientToken);
//    }
}
