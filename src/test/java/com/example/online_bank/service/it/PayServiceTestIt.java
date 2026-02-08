package com.example.online_bank.service.it;

import com.example.online_bank.OnlineBankApplication;
import com.example.online_bank.repository.UserRepository;
import com.example.online_bank.service.BankPartnerService;
import com.example.online_bank.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ContextConfiguration;

@RequiredArgsConstructor
@ContextConfiguration(classes = OnlineBankApplication.class)
@SpringBootTest(classes = OnlineBankApplication.class)
@Slf4j
class PayServiceTestIt {
    @Autowired
    private PayService payService;
    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;
    @MockBean
    private BankPartnerService bankPartnerService;
    @MockBean
    private UserRepository userRepository;

    @Test
    void successPay(){
       // Quest.builder()
        //.

    }
}