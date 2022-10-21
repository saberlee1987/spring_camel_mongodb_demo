package com.saber.spring_camel_mongodb_demo.services.impl;

import com.saber.spring_camel_mongodb_demo.dto.HelloRequestDto;
import com.saber.spring_camel_mongodb_demo.dto.HelloResponseDto;
import com.saber.spring_camel_mongodb_demo.services.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HelloServiceImpl implements HelloService {
    @Override
    public HelloResponseDto sayHello(HelloRequestDto dto) {
        log.info("Request for sayHello ===> {}",dto);
        String message = String.format("Hello %s %s",
                dto.getFirstName(),dto.getLastName());
        HelloResponseDto responseDto = new HelloResponseDto();
        responseDto.setMessage(message);
        log.info("Response for sayHello ===> {}",responseDto);
        return responseDto;
    }
}
