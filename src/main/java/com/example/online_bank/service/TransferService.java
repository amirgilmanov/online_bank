package com.example.online_bank.service;

import com.example.online_bank.domain.dto.FinanceOperationDto;
import com.example.online_bank.domain.dto.OperationDtoResponse;
import com.example.online_bank.domain.dto.TransferDto;
import com.example.online_bank.domain.model.AbstractBank;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.exception.TransferException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {
    private final RestTemplate restTemplate;
    private final AbstractBank bank;
    private static final String POSTFIX_URL = "/operation/receive";
    private final AccountService accountService;
    private final BankService bankService;

    /**
     * Получить информацию о текущем банке
     */
    public String getBankInfo() {
        return bank.getName();
    }

    /**
     * Перевод в партнерский банк(на другом порту или по одному)
     * <p>
     * На вход - имя банка, сумма, описание, фио от кого пришло.
     *
     * @param transferDto Cодержит информацию об отправителе и получателе,
     *                    количестве отправленных денег, описание и время операции.
     *                    <p>
     *                    Если названия банков отправителя и получателя одинаковы - то выполняется пополнение.
     *                    <p>
     *                    Если нет - метод отправит POST запрос по адресу - bank.partner.url + /operations/receive.
     *                    Делает списание со счета отправителя и начисление на счет получателя и создает две операции(операция
     *                    списания и начисления)
     */
    @Transactional
    public List<OperationDtoResponse> transferMoney(
            TransferDto transferDto) {

        // не нужно делать сравнение: мы передаем имя банка и уже заранее знаем, что операция - "перевод в другой банк", и поэтому не будем делать такие сравнения
        // т.к. в dto сумма заранее определенна, как сумма в валюте получателя, то не нужно производить конвертацию при отправке на пополнение для получателя, отсюда пропадает
        // необходимость в получение кода валюты отправителя, мы отправим ему код валюты получателя, и банк сервис сам сконвертирует сумму для списания со счета отправителя
        CurrencyCode recipientAccountCurrencyCode = accountService.findCurrencyCode(transferDto.recipientInfo().accountNumberTo());

        //снимаем бабло у отправителя можно еще на уровне банка сверить счета на валюту и снять бабло у отправителя в банк сервисе.
        OperationDtoResponse senderOperationResponse = bankService.makePayment(
                new FinanceOperationDto(
                        transferDto.senderInfo().accountNumberFrom(),
                        transferDto.recipientRequestAmount(),
                        transferDto.description(),
                        recipientAccountCurrencyCode)
        );

        //Отправляем запрос на пополнение по адресу банка для получателя:
        //Подготавливаем данные для пополнения, отправляем запрос
        //1) делаем url
        String url = createTransferUrl(bank);
        //2) создаем запрос
        OperationDtoResponse recipientOperationResponse = createAndSendRequest(
                new FinanceOperationDto(transferDto.recipientInfo().accountNumberTo(),
                        transferDto.recipientRequestAmount(),
                        transferDto.description(),
                        recipientAccountCurrencyCode),
                url
        );
        //Лист возвращается пока для тестового варианта, чтобы видеть результат сразу у получателя и отправителя.
        //Позже буду возвращать только dtoResponse отправителя
        return List.of(senderOperationResponse, recipientOperationResponse);
    }

    /**
     * Содержит в себе методы создания и отправки запроса, где все обернуто в try-catch
     */
    private OperationDtoResponse createAndSendRequest(FinanceOperationDto recipientDto, String url) {
        RequestEntity<FinanceOperationDto> request = createRequest(recipientDto, url);
        try {
            return exchangeRequest(request);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new TransferException("Ошибка при отправке перевода %s".formatted(e.getMessage()));
        }
    }

    /**
     * Формирует URL
     *
     * @param bank Банк, содержит в себе название банка и url партнерского порта.
     *             <p>
     *             производит конкатенацию строк, где первая содержит в себе порт приложения, а постфиксом является
     *             url из OperationController т.е создается адрес и мы как бы имитируем то, что мы открыли приложение
     *             с другим портом
     *             <p>
     *             Пример: мы разворачиваем приложение на порту 8080, а bank хранит в себе информацию о
     *             приложении развернутом на другом порте и с помощью банка мы получаем префикс этого порта и через
     *             одно приложение отправляем запрос другому приложению
     * @return сформированный url
     */
    private String createTransferUrl(AbstractBank bank) {
        String partnerBankBaseUrl = bank.getUrl();
        return partnerBankBaseUrl + POSTFIX_URL;
    }

    /**
     * Создает запрос для отправки партнерскому банку
     *
     * @param dto DTO с финансовой информацией, включает в себя номер счета, количество денег, описание и код валюты
     * @param url Адрес запроса
     * @return requestEntity, который нужно отправить через restTemplate
     */
    private RequestEntity<FinanceOperationDto> createRequest(FinanceOperationDto dto, String url) {
        return RequestEntity.post(url)
                .body(dto);
    }

    /**
     * Отправляет запрос по сформированному адресу
     *
     * @param requestBody тело для отправки запроса, содержит в себе номер счета,
     *                    количество денег, описание и код валюты
     *                    <p>
     */
    private OperationDtoResponse exchangeRequest(RequestEntity<FinanceOperationDto> requestBody) {
        return restTemplate.exchange(requestBody, OperationDtoResponse.class).getBody();
    }
}