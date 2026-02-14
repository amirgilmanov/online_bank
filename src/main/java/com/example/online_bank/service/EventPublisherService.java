package com.example.online_bank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisherService<I> {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(I event) {
        log.debug("Send event {}", event);
        try {
            applicationEventPublisher.publishEvent(event);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
