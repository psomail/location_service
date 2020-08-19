package ru.logisticplatform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.RestError;
import ru.logisticplatform.repository.RestErrorRepository;


@Service
@Slf4j
public class RestErrorServiceImpl implements RestErrorService {

    private final RestErrorRepository restErrorRepository;

    @Autowired
    public RestErrorServiceImpl(RestErrorRepository restErrorRepository){
        this.restErrorRepository = restErrorRepository;
    }


    @Override
    public RestError findById(Long id) {

        RestError restError = this.restErrorRepository.findById(id).orElse(null);

        if (restError == null){
            log.warn("IN RestErrorServiceImpl findById - no restError found by id: {}", id);
            return null;
        }

        return restError;
    }
}
