package kr.co.pawong.pwbe.test.application.service;

import kr.co.pawong.pwbe.test.application.port.in.PrintLogUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PrintLogService implements PrintLogUseCase {

    @Override
    public void printLog(String message) {
        log.info(message);
    }
}
