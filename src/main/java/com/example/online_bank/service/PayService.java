package com.example.online_bank.service;

import com.example.online_bank.domain.dto.FinanceOperationDto;
import com.example.online_bank.domain.dto.OperationDtoResponse;
import com.example.online_bank.domain.dto.TransferDto;
import com.example.online_bank.domain.event.UpdateUserStatEvent;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.enums.PartnerCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayService {
    private final AccountService accountService;
    private final BankService bankService;

    public OperationDtoResponse payServices(TransferDto transferDto, PartnerCategory category) {
        CurrencyCode recipientCurrencyCode = accountService.findCurrencyCode(transferDto.recipientInfo().accountNumberTo());

        //снимаем деньги со счета отправителя
        OperationDtoResponse senderOperationResponse = bankService.makePayment(
                new FinanceOperationDto(
                        transferDto.senderInfo().accountNumberFrom(),
                        transferDto.recipientRequestAmount(),
                        transferDto.description(),
                        recipientCurrencyCode)
        );

        FinanceOperationDto recipientDto = createRecipientDto(transferDto, recipientCurrencyCode);

        bankService.makeDeposit(recipientDto);
        accountRepository.findHolderByAccountNumber();
        new UpdateUserStatEvent()


    }
}
