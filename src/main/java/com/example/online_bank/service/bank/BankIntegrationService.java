package com.example.online_bank.service.bank;

import com.example.online_bank.dto.OperationDtoResponse;
import com.example.online_bank.dto.finance_dto.DepositMoneyDtoRequest;
import com.example.online_bank.dto.transaction.TransactionDtoRequest;
import com.example.online_bank.dto.transaction.TransactionDtoResponse;
import com.example.online_bank.entity.bank.AbstractBank;
import com.example.online_bank.service.finance.FinanceServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class BankIntegrationService {
    private final FinanceServiceV2 financeServiceV2;
    private final RestTemplate restTemplate;
    private final AbstractBank bank;
    private static final String HEADER_ACCOUNT_ID_TOKEN_NAME = "recipientAccountId";
    private static final String HEADER_TOKEN_USER_NAME = "token";

    @Autowired
    public BankIntegrationService(FinanceServiceV2 financeServiceV2, RestTemplate restTemplate, AbstractBank bank) {
        this.financeServiceV2 = financeServiceV2;
        this.restTemplate = restTemplate;
        this.bank = bank;
    }

    public TransactionDtoResponse transferMoneyToPartnerBank(
            TransactionDtoRequest transactionDto,
            String senderAccountId,
            String recipientAccountId,
            String senderToken,
            String recipientToken
    ) {
        String fullUrl = createPartnerFullUrl(bank);
        DepositMoneyDtoRequest dtoRequest = new DepositMoneyDtoRequest(transactionDto.amount(),
                transactionDto.description(),
                transactionDto.paymentCurrencyCode());
        RequestEntity<DepositMoneyDtoRequest> request = RequestEntity.post(fullUrl)
                .header(HEADER_ACCOUNT_ID_TOKEN_NAME, recipientAccountId)
                .header(HEADER_TOKEN_USER_NAME, recipientToken)
                .body(dtoRequest);

        restTemplate.exchange(request, OperationDtoResponse.class);
        financeServiceV2.makePayment(senderAccountId, transactionDto.amount(), transactionDto.description(),
                senderToken, transactionDto.paymentCurrencyCode());

        return new TransactionDtoResponse(
                transactionDto.amount(),
                dtoRequest.description(),
                transactionDto.name(),
                transactionDto.surname(),
                transactionDto.patronymic(),
                senderAccountId,
                transactionDto.bankName(),
                recipientAccountId
        );
    }

    public String info() {
        return bank.getName();
    }

    private String createPartnerFullUrl(AbstractBank bank) {
        String partnerBankBaseUrl = bank.getUrl();
        String postfixUrl = "/operation/receive";
       return partnerBankBaseUrl + postfixUrl;
    }

    //4. Создать сервис "Переводы в другие банки". В данном сервисе будет происходить интеграция с другими банками.
    //4.1 Создать метод перевод в другой банк, на вход - имя банка, сумма, описание, фио от кого пришло.
    //4.1.1 Метод отправит POST запрос по адресу - bank.partner.url + /operations/receive.
    //5. Добавить в банк сервис (этап3 пункт3) метод "перевод в другой банк".
    //На вход токен пользователя, сумма, описание, счет с которого списание будет
    //5.1. Сделать перевод в другой банк (пункт 4.1).
    //5.2. Списать деньги со счета, проверив что пользователь является владельцем счета.
}
