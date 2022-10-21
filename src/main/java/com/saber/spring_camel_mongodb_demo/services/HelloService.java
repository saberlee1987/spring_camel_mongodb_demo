package com.saber.spring_camel_mongodb_demo.services;

import com.saber.spring_camel_mongodb_demo.dto.HelloRequestDto;
import com.saber.spring_camel_mongodb_demo.dto.HelloResponseDto;

public interface HelloService {
    HelloResponseDto sayHello(HelloRequestDto dto);
}
