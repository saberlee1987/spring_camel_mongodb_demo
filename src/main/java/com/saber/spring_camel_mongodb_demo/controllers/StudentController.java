package com.saber.spring_camel_mongodb_demo.controllers;


import com.saber.spring_camel_mongodb_demo.dto.*;
import com.saber.spring_camel_mongodb_demo.dto.student.*;
import com.saber.spring_camel_mongodb_demo.routes.Headers;
import com.saber.spring_camel_mongodb_demo.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@RestController
@Validated
@Tag(name = "${spring.application.name}(student)", description = "student api")
@RequestMapping(value = "${service.api.base-path}/student", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping(value = "/findByNationalCode/{nationalCode}")
    @Operation(summary = "findByNationalCode", description = "findByNationalCode api", method = "GET",
            parameters = {
                    @Parameter(name = "nationalCode", in = ParameterIn.PATH, required = true, example = "0079028748", description = "nationalCode")
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StudentDto.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "503", description = "SERVICE_UNAVAILABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "504", description = "GATEWAY_TIMEOUT",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),

    })
    public ResponseEntity<StudentDto> findPersonByNationalCode(@PathVariable(name = "nationalCode")
                                                              @NotBlank(message = "nationalCode is Required")
                                                              @Size(min = 10, max = 10, message = "nationalCode must be 10 digit")
                                                              @Pattern(regexp = "\\d+", message = "Please Enter correct nationalCode")
                                                              @Valid
                                                                      String nationalCode, HttpServletRequest httpServletRequest) {

        String correlation = getCorrelation(httpServletRequest);
        return ResponseEntity.ok(this.studentService.findPersonByNationalCode(correlation, nationalCode));
    }

    @GetMapping(value = "/findByStudentNumber/{studentNumber}")
    @Operation(summary = "findByStudentNumber", description = "findByStudentNumber api", method = "GET",
            parameters = {
                    @Parameter(name = "studentNumber", in = ParameterIn.PATH, required = true, example = "943175737", description = "studentNumber")
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StudentDto.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "503", description = "SERVICE_UNAVAILABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "504", description = "GATEWAY_TIMEOUT",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),

    })
    public ResponseEntity<StudentDto> findPersonByStudentNumber(@PathVariable(name = "studentNumber")
                                                               @NotBlank(message = "studentNumber is Required")
                                                               @Pattern(regexp = "\\d+", message = "Please Enter correct studentNumber")
                                                               @Valid
                                                                       String studentNumber, HttpServletRequest httpServletRequest) {

        String correlation = getCorrelation(httpServletRequest);
        return ResponseEntity.ok(this.studentService.findPersonByStudentNumber(correlation, studentNumber));
    }

    @GetMapping(value = "/findAllByNationalCodeAndStudentNumber")
    @Operation(summary = "findAllByNationalCodeAndStudentNumber", description = "findAllByNationalCodeAndStudentNumber api", method = "GET",
            parameters = {
                    @Parameter(name = "nationalCode", in = ParameterIn.QUERY, required = true, example = "0079028748", description = "nationalCode"),
                    @Parameter(name = "studentNumber", in = ParameterIn.QUERY, required = true, example = "943175737", description = "studentNumber"),
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StudentDto.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "503", description = "SERVICE_UNAVAILABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "504", description = "GATEWAY_TIMEOUT",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),

    })
    public ResponseEntity<StudentDto> findAllByNationalCodeAndStudentNumber(
                                                                        @RequestParam(name = "nationalCode") @NotBlank(message = "nationalCode is required") String nationalCode,
                                                                      @RequestParam(name = "studentNumber") @NotBlank(message = "studentNumber is required") String studentNumber,
                                                                        HttpServletRequest httpServletRequest) {

        String correlation = getCorrelation(httpServletRequest);
        return ResponseEntity.ok(this.studentService.findStudentByNationalCodeAndStudentNumber(correlation, nationalCode, studentNumber));
    }
    @GetMapping(value = "/findAll")
    @Operation(summary = "findAll students", description = "findAll students api", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "503", description = "SERVICE_UNAVAILABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "504", description = "GATEWAY_TIMEOUT",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),

    })
    public ResponseEntity<StudentResponse> findAllStudents(HttpServletRequest httpServletRequest) {
        String correlation = getCorrelation(httpServletRequest);
        return ResponseEntity.ok(this.studentService.findAllStudents(correlation));
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "add student", description = "add student", method = "POST",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "add student",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(name = "addStudent", title = "addStudent", implementation = StudentDto.class)
                            , examples = @ExampleObject(name = "addStudent", summary = "addStudent",
                            value = "{\n" +
                                    "  \"firstName\": \"saber\",\n" +
                                    "  \"lastName\": \"Azizi\",\n" +
                                    "  \"nationalCode\": \"0079028748\",\n" +
                                    "   \"studentNumber\": \"943175737\",\n" +
                                    "   \"field\" : \"computer\",\n" +
                                    "  \"age\": 35,\n" +
                                    "  \"email\": \"saberazizi66@yahoo.com\",\n" +
                                    "  \"mobile\": \"09365627895\",\n" +
                                    "  \"country\": \"iran\",\n" +
                                    "  \"language\": \"persian\",\n" +
                                    "  \"birthDate\": \"1366/09/16\"\n" +
                                    "}")
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AddStudentResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "503", description = "SERVICE_UNAVAILABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "504", description = "GATEWAY_TIMEOUT",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),

    })
    public ResponseEntity<AddStudentResponseDto> addStudent(@RequestBody @NotNull(message = "body is Required") @Valid StudentDto studentDto, HttpServletRequest httpServletRequest) {
        String correlation = getCorrelation(httpServletRequest);
        return ResponseEntity.ok(studentService.insertStudent(correlation,studentDto));
    }

    @PutMapping(value = "/addTermToStudent", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "addTermToStudent", description = "addTermToStudent", method = "PUT",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "addTermToStudent",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(name = "addTermToStudent", title = "addTermToStudent", implementation = StudentTermCourseDto.class)
                            , examples = @ExampleObject(name = "updatePerson by nationalCode", summary = "updatePerson by nationalCode")
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AddTermStudentResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "503", description = "SERVICE_UNAVAILABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "504", description = "GATEWAY_TIMEOUT",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),

    })
    public ResponseEntity<AddTermStudentResponseDto> addTermToStudentTerms(@RequestBody @NotNull(message = "body is Required") @Valid StudentTermCourseDto studentTermCourseDto, HttpServletRequest httpServletRequest) {
        String correlation = getCorrelation(httpServletRequest);
        return ResponseEntity.ok(studentService.addTermToStudentTerms(correlation,studentTermCourseDto));
    }

    private String getCorrelation(HttpServletRequest httpServletRequest) {
        String correlation;
        correlation = httpServletRequest.getHeader(Headers.correlation);
        if (correlation == null || correlation.isEmpty()) {
            correlation = UUID.randomUUID().toString();
        }
        return correlation;
    }

}