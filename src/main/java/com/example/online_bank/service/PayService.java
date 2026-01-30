package com.example.online_bank.service;

import com.example.online_bank.domain.dto.FinanceOperationDto;
import com.example.online_bank.domain.dto.OperationDtoResponse;
import com.example.online_bank.domain.dto.TransferDto;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.domain.event.UpdateUserStatEvent;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.enums.PartnerCategory;
import com.example.online_bank.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PayService {
    private final AccountService accountService;
    private final BankService bankService;
    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

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

        FinanceOperationDto serviceDto = createRecipientDto(transferDto, recipientCurrencyCode);
        OperationDtoResponse servisesResponse = bankService.makeDeposit(serviceDto);

        User user = accountRepository.findHolderByAccountNumber(senderOperationResponse.accountNumber()).orElseThrow(
                () -> new EntityNotFoundException("Entity not found")
        );
        UpdateUserStatEvent updateUserStatEvent = new UpdateUserStatEvent(user, category, transferDto.recipientRequestAmount(), LocalDate.now());
        applicationEventPublisher.publishEvent(updateUserStatEvent);
        return servisesResponse;
    }

    private FinanceOperationDto createRecipientDto(TransferDto transferDto, CurrencyCode recipientCurrencyCode) {
        return new FinanceOperationDto(transferDto.recipientInfo().accountNumberTo(),
                transferDto.recipientRequestAmount(),
                transferDto.description(),
                recipientCurrencyCode);
    }
}
