package com.example.online_bank.service;

import com.example.online_bank.dto.AccountDtoResponse;
import com.example.online_bank.dto.OperationInfoDtoResponse;
import com.example.online_bank.entity.User;
import com.example.online_bank.mapper.AccountMapper;
import com.example.online_bank.mapper.OperationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistoryService {

    private final AccountService accountService;
    private final UserService userService;
    private final AccountMapper accountMapper;
    private final OperationService operationService;
    private final OperationMapper operationMapper;

    public List<AccountDtoResponse> findAllAccountsByUser(String userToken) {
        User user = userService.findByToken(userToken);
        return accountService.findAllAccountsByHolder(user).stream()
                .map(accountMapper::toDtoResponse)
                .toList();
    }

    public List<OperationInfoDtoResponse> showAllUserOperationHistory(String token, int page, int size) {
        User user = userService.findByToken(token);
        return operationMapper.toOperationInfoDtoList(operationService.showAllUserOperationPortion(user, page, size));
    }

    public List<OperationInfoDtoResponse> findOperationByAccountNumberPortion(String accountNumber, long page, long size) {
        return operationService.findByAccountNumberPortion(accountNumber, page, size);
    }


}
