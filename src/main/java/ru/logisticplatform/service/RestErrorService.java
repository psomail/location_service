package ru.logisticplatform.service;

import ru.logisticplatform.model.RestError;

public interface RestErrorService {

    RestError findById(Long id);
}
