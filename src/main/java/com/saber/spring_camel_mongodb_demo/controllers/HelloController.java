package com.saber.spring_camel_mongodb_demo.controllers;

import com.saber.spring_camel_mongodb_demo.dto.HelloRequestDto;
import com.saber.spring_camel_mongodb_demo.dto.HelloResponseDto;
import com.saber.spring_camel_mongodb_demo.services.HelloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${service.api.base-path}/hello",produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "hello service",description = "hello service")
@AllArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @RequestMapping(value = "/sayHello",method = {RequestMethod.POST})
    @Operation(summary = "sayHello",description = "sayHello",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    description = "helloRequestDto",
                    content = @Content(mediaType =MediaType.APPLICATION_JSON_VALUE,
                            schema =@Schema(implementation = HelloRequestDto.class)
                   ,examples = @ExampleObject(value = "{\"firstName\": \"saber\",\"lastName\": \"Azizi\"}")))
    )
    public ResponseEntity<HelloResponseDto> sayHello(@RequestBody HelloRequestDto body){
        HelloResponseDto responseDto = helloService.sayHello(body);
        return ResponseEntity.ok(responseDto);
    }
}