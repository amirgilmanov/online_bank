package com.example.online_bank.service.listener;

import com.example.online_bank.domain.event.UpdateBonusAccountEvent;
import com.example.online_bank.service.BonusAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UpdateBonusAccountListener {
    private final BonusAccountService bonusAccountService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void onUpdateBonusAccountEvent(UpdateBonusAccountEvent event) {
        bonusAccountService.depositBonus(event.accountNumber(), event.points());
    }
}
