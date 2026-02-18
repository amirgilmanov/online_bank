package com.example.online_bank.service;

import is.tagomor.woothee.Classifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserAgentService {

    /**
     * в беке смотрим в случае если пароль совпал, а deviceId не пустой и существует в базе, то ищем пользователя с таким deviceId
     * ПРОВЕРКА USERAGENT
     * сравнение UserAgent немного отличается от того, что в базе - изменяем UserAgent
     */
    public boolean checkUserAgent(String requestedUserAgent, String trustedUserAgent) {

        Map<String, String> parsedRequestedUserAgent = parseUserAgent(requestedUserAgent);
        Map<String, String> parsedGivenUserAgent = parseUserAgent(trustedUserAgent);

        if (!checkEqualsUserAgentParam("os", parsedRequestedUserAgent, parsedGivenUserAgent)) {
            return false;
        }

        if (!checkEqualsUserAgentParam("name", parsedRequestedUserAgent, parsedGivenUserAgent)) {
            return false;
        }

        if (!checkEqualsUserAgentParam("category", parsedRequestedUserAgent, parsedGivenUserAgent)) {
            return false;
        }
        // Если мы дошли сюда, значит ОС и Тип устройства совпали.
        // Версия браузера (version) нас не волнует — она обновилась сама.
        return true;
    }

    private boolean checkEqualsUserAgentParam(String uaRequestParam, Map<String, String> parsedGivenUserAgent, Map<String, String> parsedRequestedUserAgent) {
        return parsedGivenUserAgent.get(uaRequestParam).equals(parsedRequestedUserAgent.get(uaRequestParam));
    }

    private Map<String, String> parseUserAgent(String userAgent) {
        return Classifier.parse(userAgent);
    }
}
