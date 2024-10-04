//package com.example.online_bank.service.account;
//
//import com.example.online_bank.dto.transaction.TransactionDtoRequest;
//import com.example.online_bank.entity.account.AccountV1;
//import com.example.online_bank.entity.User;
//import com.example.online_bank.exception.account_exception.AccountNotFoundException;
//import com.example.online_bank.exception.account_exception.NegativeAccountBalance;
//import com.example.online_bank.repository.account.AccountRepository;
//import com.example.online_bank.service.other.CodeGeneratorService;
//import com.example.online_bank.service.user.UserRegistrationService;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Random;
//
//@Service
//@RequiredArgsConstructor
//public class AccountService {
//    @NonNull
//    private final AccountRepository accountRepository;
//    private final CodeGeneratorService codeGeneratorService;
//
////    public String createAccountForUser(User user) {
////        AccountV1 accountV1 = AccountV1
////                .builder()
////                .accountBalance(BigDecimal.ZERO)
////                .holder(user)
////                .id(codeGeneratorService.generatedAccountId())
////                .build();
////        accountRepository.save(accountV1);
////        return accountV1.getId();
////    }
//
//    //2.3. Занести деньги насчет (номер счета, сумма).
//    //Увеличивает остаток счета. Если счета не существует - ошибка.
////    public void depositMoney(String accountId, BigDecimal deposit) {
////        AccountV1 accountV1 = accountRepository.findById(accountId)
////                .orElseThrow(() -> new AccountNotFoundException("счета с таким номером не найдено"));
////        accountV1.setAccountBalance(deposit);
////    }
////
////    //2.4. Списать деньги со счета (номер счета, сумма). Уменьшает остаток счета.
////    // Остаток после операции не может быть отрицательный.
////    public void withdrawMoney(String accountId, BigDecimal amount) {
////        AccountV1 accountV1 = accountRepository.findById(accountId)
////                .orElseThrow(() -> new AccountNotFoundException("счета с таким номером не найдено"));
////        BigDecimal newBalance = accountV1.getAccountBalance().subtract(amount);
////        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
////            throw new NegativeAccountBalance("недостаточно средств на балансе");
////        } else {
////            accountV1.setAccountBalance(newBalance);
////        }
////    }
//
////    //2.5. Получить счета пользователя (на вход пользователь), на выход список его счетов.
////    public List<AccountV1> getAllAccounts(User user) {
////        return accountRepository.findByUser(user);
////    }
////
////    //2.6. Получить остаток по счету(номер счета). На выход остаток. Если счета не существует - ошибка.
////    public BigDecimal getBalance(String id) {
////
////        AccountV1 accountV1 = accountRepository.findById(id)
////                .orElseThrow(() -> new AccountNotFoundException("счета с таким номером не найдено"));
////        return accountV1.getAccountBalance();
////    }
//
////    /**
////     * Проверка принадлежности счета к пользователю
////     *
////     * @param user      пользователь
////     * @param accountId номер счета
////     * @return Возвращает: принадлежит или нет.
////     */
////    public boolean checkUserAccount(User user, String accountId) {
////        AccountV1 accountV1 = accountRepository.findById(accountId)
////                .orElseThrow(() -> new AccountNotFoundException("счета с таким номером не найдено"));
////        return accountV1.getHolder().equals(user);
////    }
//
////    public void transferMoney(
////            String senderAccountId,
////            String recipientAccountId,
////            TransactionDtoRequest dto) {
////        withdrawMoney(senderAccountId, dto.amount());
////        depositMoney(recipientAccountId, dto.amount());
////    }
////
////    public AccountV1 findById(String accountId) {
////        return accountRepository.findById(accountId).orElseThrow();
////    }
//}