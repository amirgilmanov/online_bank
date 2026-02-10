package com.example.online_bank.service.listener;

import com.example.online_bank.domain.event.UpdateBonusAccountEvent;
import com.example.online_bank.service.BonusAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
public class UpdateBonusAccountListener {
    private final BonusAccountService bonusAccountService;

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Async
    public void onUpdateBonusAccountEvent(UpdateBonusAccountEvent event) {
        bonusAccountService.depositBonus(event.accountNumber(), event.points());
    }
}
