package com.saber.spring_camel_mongodb_demo.controllers;


import com.saber.spring_camel_mongodb_demo.dto.AddPersonResponseDto;
import com.saber.spring_camel_mongodb_demo.dto.ErrorResponse;
import com.saber.spring_camel_mongodb_demo.dto.PersonDto;
import com.saber.spring_camel_mongodb_demo.dto.PersonResponse;
import com.saber.spring_camel_mongodb_demo.routes.Headers;
import com.saber.spring_camel_mongodb_demo.services.PersonService;
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
@Tag(name = "${spring.application.name}", description = "${spring.application.name}")
@RequestMapping(value = "${service.api.base-path}/person", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class PersonController {


    private final PersonService personService;

	@GetMapping(value = "/find/{nationalCode}")
	@Operation(summary = "findByNationalCode", description = "findByNationalCode api", method = "GET",
			parameters = {
					@Parameter(name = "nationalCode", in = ParameterIn.PATH, required = true, example = "0079028748", description = "nationalCode")
			})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PersonDto.class))}),
			@ApiResponse(responseCode = "400", description = "BAD_REQUEST",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
			@ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
			@ApiResponse(responseCode = "401", description =  "UNAUTHORIZED",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
			@ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
			@ApiResponse(responseCode = "503", description ="SERVICE_UNAVAILABLE",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
			@ApiResponse(responseCode = "504", description = "GATEWAY_TIMEOUT",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),

	})
	public ResponseEntity<PersonDto> findPersonByNationalCode(@PathVariable(name = "nationalCode")
													@NotBlank(message = "nationalCode is Required")
													@Size(min = 10, max = 10, message = "nationalCode must be 10 digit")
													@Pattern(regexp = "\\d+", message = "Please Enter correct nationalCode")
													@Valid
													String nationalCode,HttpServletRequest httpServletRequest) {

		String correlation = getCorrelation(httpServletRequest);
		return ResponseEntity.ok(this.personService.findPersonByNationalCode(correlation,nationalCode));
	}

    @GetMapping(value = "/findAll")
    @Operation( summary = "findAll persons", description = "findAll persons api", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PersonResponse.class))}),
			@ApiResponse(responseCode = "400", description = "BAD_REQUEST",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
			@ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
			@ApiResponse(responseCode = "401", description =  "UNAUTHORIZED",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
			@ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
			@ApiResponse(responseCode = "503", description ="SERVICE_UNAVAILABLE",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
			@ApiResponse(responseCode = "504", description = "GATEWAY_TIMEOUT",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),

	})
    public ResponseEntity<PersonResponse> findAllPersons(HttpServletRequest httpServletRequest) {
		String correlation = getCorrelation(httpServletRequest);
        return ResponseEntity.ok(this.personService.findAllPerson(correlation));
    }
//
	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "add person", description = "add person", method = "POST",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "add person",
					content = @Content(mediaType = "application/json",
							schema = @Schema(name = "addPerson", title = "addPerson", implementation = PersonDto.class)
							, examples = @ExampleObject(name = "addPerson", summary = "addPerson",
							value = "{\n" +
									"  \"firstName\": \"saber\",\n" +
									"  \"lastName\": \"Azizi\",\n" +
									"  \"nationalCode\": \"0079028748\",\n" +
									"  \"age\": 34,\n" +
									"  \"email\": \"saberazizi66@yahoo.com\",\n" +
									"  \"mobile\": \"09365627895\",\n" +
									"   \"country\": \"iran\",\n" +
									"   \"language\" : \"persian\",\n" +
									"   \"birthDate\" : \"1366/09/16\" \n" +
									"}")
					)
			))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AddPersonResponseDto.class))}),
			@ApiResponse(responseCode = "400", description = "BAD_REQUEST",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
			@ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
			@ApiResponse(responseCode = "401", description =  "UNAUTHORIZED",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
			@ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
			@ApiResponse(responseCode = "503", description ="SERVICE_UNAVAILABLE",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
			@ApiResponse(responseCode = "504", description = "GATEWAY_TIMEOUT",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),

	})
	public ResponseEntity<AddPersonResponseDto> addPerson(@RequestBody @NotNull(message = "body is Required") @Valid PersonDto personDto, HttpServletRequest httpServletRequest)
	{
		String correlation = getCorrelation(httpServletRequest);
		return ResponseEntity.ok(personService.addPerson(personDto,correlation));
	}
//
//	@PutMapping(value = "/update/{nationalCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	@Operation(tags = {"update person"}, summary = "update person", description = "update person", method = "PUT",
//			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "update person",
//					content = @Content(mediaType = "application/json",
//							schema = @Schema(name = "update person", title = "update person", implementation = PersonDto.class)
//							, examples = @ExampleObject(name = "updatePerson", summary = "updatePerson",
//							value = "{\"firstname\": \"saber\",\"lastname\": \"Azizi\",\"nationalCode\": \"0079028748\",\"age\": 34,\"email\": \"saberazizi66@yahoo.com\",\"mobile\": \"09365627895\"}")
//					)
//			), parameters = {
//			@Parameter(name = "nationalCode", in = ParameterIn.PATH, required = true, example = "0079028748")
//	}
//	)
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "Success",
//					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UpdatePersonResponseDto.class))}),
//			@ApiResponse(responseCode = "400", description = "BAD_REQUEST",
//					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//			@ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE",
//					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//			@ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
//					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//			@ApiResponse(responseCode = "504", description = "GATEWAY_TIMEOUT",
//					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//	})
//	public ResponseEntity<UpdatePersonResponseDto> updatePerson(@RequestBody
//													  @NotNull(message = "body is Required") @Valid
//															  PersonDto personDto,
//													  @PathVariable(name = "nationalCode")
//													  @NotBlank(message = "nationalCode is Required")
//													  @Size(min = 10, max = 10, message = "nationalCode must be 10 digit")
//													  @Pattern(regexp = "\\d+", message = "please enter valid nationalCode")
//															  String nationalCode,HttpServletRequest httpServletRequest) {
//		String correlation = getCorrelation(httpServletRequest);
//		return ResponseEntity.ok(personService.updatePersonByNationalCode(nationalCode, personDto,correlation));
//	}
//
//	@DeleteMapping(value = "/delete/{nationalCode}", produces = MediaType.APPLICATION_JSON_VALUE)
//	@Operation(tags = {"delete person by nationalCode"}, summary = "delete person by nationalCode", description = "delete person by nationalCode", method = "GET",
//			parameters = {
//					@Parameter(name = "nationalCode", in = ParameterIn.PATH, required = true, example = "0079028748", description = "nationalCode")
//			})
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "Success",
//					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeletePersonDto.class))}),
//			@ApiResponse(responseCode = "400", description = "BAD_REQUEST",
//					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//			@ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE",
//					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//			@ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
//					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//			@ApiResponse(responseCode = "504", description = "GATEWAY_TIMEOUT",
//					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//	})
//	public ResponseEntity<DeletePersonDto> deletePersonByNationalCode(@PathVariable(name = "nationalCode")
//																		  @NotBlank(message = "nationalCode is Required")
//																		  @Size(min = 10, max = 10, message = "nationalCode must be 10 digit")
//																		  @Pattern(regexp = "\\d+", message = "please enter valid nationalCode")
//																				  String nationalCode,HttpServletRequest httpServletRequest) {
//		String correlation = getCorrelation(httpServletRequest);
//		return ResponseEntity.ok(personService.deletePersonByNationalCode(nationalCode,correlation));
//	}

	private String getCorrelation(HttpServletRequest httpServletRequest) {
		String correlation = "";
		correlation = httpServletRequest.getHeader(Headers.correlation);
		if (correlation == null || correlation.isEmpty()) {
			correlation = UUID.randomUUID().toString();
		}
		return correlation;
	}

}