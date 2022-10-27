package com.saber.spring_camel_mongodb_demo.routes;

public interface Routes {

	String FIND_ALL_PERSON_ROUTE_GROUP = "find-all-person-route-group";
	String FIND_ALL_PERSON_ROUTE_GATEWAY = "find-all-person-route-gateway";
	String FIND_ALL_PERSON_ROUTE_GATEWAY_OUT = "find-all-person-route-gateway-out";
	
	String FIND_PERSON_BY_NATIONAL_CODE_ROUTE = "find-person-by-nationalCode-route";
	String FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP = "find-person-by-nationalCode-route-group";
	String FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY = "find-person-by-nationalCode-route-gateway";
	String FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT = "find-person-by-nationalCode-route-gateway-out";

	String FIND_PERSON_BY_AGE_ROUTE_GROUP = "find-person-by-age-route-group";
	String FIND_PERSON_BY_AGE_ROUTE_GATEWAY = "find-person-by-age-route-gateway";
	String FIND_PERSON_BY_AGE_ROUTE_GATEWAY_OUT = "find-person-by-age-route-gateway-out";

	String FIND_PERSON_BY_COUNTRY_ROUTE_GROUP = "find-person-by-country-route-group";
	String FIND_PERSON_BY_COUNTRY_ROUTE_GATEWAY = "find-person-by-country-route-gateway";
	String FIND_PERSON_BY_COUNTRY_ROUTE_GATEWAY_OUT = "find-person-by-country-route-gateway-out";

	String FIND_PERSON_BY_COUNTRY_AND_LANGUAGE_ROUTE_GROUP = "find-person-by-country-and-language-route-group";
	String FIND_PERSON_BY_COUNTRY_AND_LANGUAGE_ROUTE_GATEWAY = "find-person-by-country-and-language-route-gateway";
	String FIND_PERSON_BY_COUNTRY_AND_LANGUAGE_ROUTE_GATEWAY_OUT = "find-person-by-country-and-language-route-gateway-out";



	String ADD_PERSON_ROUTE_GROUP = "add-person-route-group";
	String ADD_PERSON_ROUTE_GATEWAY = "add-person-route-gateway";
	String ADD_PERSON_ROUTE_GATEWAY_OUT = "add-person-route-gateway-out";
	
	String UPDATE_PERSON_ROUTE_ID = "update-person-route-id";
	String UPDATE_PERSON_ROUTE = "update-person-route";
	String UPDATE_PERSON_ROUTE_GROUP = "update-person-route-group";
	String UPDATE_PERSON_ROUTE_GATEWAY = "update-person-route-gateway";
	String UPDATE_PERSON_ROUTE_GATEWAY_OUT = "update-person-route-gateway-out";
	
	String DELETE_PERSON_BY_NATIONAL_CODE_ROUTE_ID = "delete-person-by-nationalCode-route-id";
	String DELETE_PERSON_BY_NATIONAL_CODE_ROUTE = "delete-person-by-nationalCode-route";
	String DELETE_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP = "delete-person-by-nationalCode-route-group";
	String DELETE_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY = "delete-person-by-nationalCode-route-gateway";
	String DELETE_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT = "delete-person-by-nationalCode-route-gateway-out";

	String RESOURCE_NOTFOUND_EXCEPTION_ROUTE = "resource-notfound-exception-route";
	String DATA_NOTFOUND_EXCEPTION_ROUTE = "data-notfound-exception-route";
	String BAD_REQUEST_EXCEPTION_ROUTE = "bad-request-exception-route";
	String RESOURCE_DUPLICATION_EXCEPTION_ROUTE = "resource-duplication-exception-route";

	String THROWS_RESOURCE_DUPLICATION_EXCEPTION_ROUTE = "throws-resource-duplication-exception-route";
	String THROWS_RESOURCE_NOTFOUND_EXCEPTION_ROUTE = "throws-resource-notfound-exception-route";
	String THROWS_DATA_NOTFOUND_EXCEPTION_ROUTE = "throws-data-notfound-exception-route";

	String HTTP_OPERATION_EXCEPTION_HANDLER_ROUTE = "http-operation-exception-handler-route";
	String TIMEOUT_EXCEPTION_HANDLER_ROUTE = "timeout-exception-handler-route";
	String JSON_EXCEPTION_HANDLER_ROUTE = "json-exception-handler-route";
	String BEAN_VALIDATION_EXCEPTION_HANDLER_ROUTE = "bean-validation-exception-handler-route";
	String PREDICATE_EXCEPTION_HANDLER_ROUTE = "predicate-exception-handler-route";
	String EXCEPTION_HANDLER_ROUTE_GROUP = "exception-handler-route-group";
}