package ru.logisticplatform.service;

import ru.logisticplatform.model.RestMessage;

public interface RestMessageService {

    RestMessage findById(Long id);

    RestMessage findByCode(String code);
}
