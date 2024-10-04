//package com.example.online_bank.repository.account;
//
//import com.example.online_bank.entity.account.AccountV1;
//import com.example.online_bank.entity.User;
//import com.example.online_bank.repository.CustomRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Repository
//@RequiredArgsConstructor
//public class AccountRepository  {//implements CustomRepository<AccountV1> {
////    private final Map<String, AccountV1> accountMap = new HashMap<>();
////
////    @Override
//    public void save(AccountV1 accountV1) {
////        accountMap.put(accountV1.getId(), accountV1);
////    }
////
////    @Override
////    public List<AccountV1> findAll() {
////        return accountMap.values().stream().toList();
////    }
////
////    public Optional<AccountV1> findById(String id) {
////        return Optional.of(accountMap.get(id));
////    }
////
////    public List<AccountV1> findByUser(User user) {
////        return accountMap.values().stream()
////                .filter(e -> e.getHolder().equals(user))
////                .collect(Collectors.toList());
////    }
//}
//
