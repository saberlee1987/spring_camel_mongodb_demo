package com.saber.spring_camel_mongodb_demo.routes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.saber.spring_camel_mongodb_demo.exceptions.BadRequestException;
import com.saber.spring_camel_mongodb_demo.exceptions.DataNotFoundException;
import com.saber.spring_camel_mongodb_demo.exceptions.ResourceDuplicationException;
import com.saber.spring_camel_mongodb_demo.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.support.processor.PredicateValidationException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

@Slf4j
public class AbstractRestRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		
		
		onException(ConnectException.class)
				.handled(true)
				.maximumRedeliveries(0)
				.log(LoggingLevel.ERROR,"Error for correlation : ${in.header.correlation} , ConnectException with error "+exceptionMessage())
				.to(String.format("direct:%s",Routes.TIMEOUT_EXCEPTION_HANDLER_ROUTE));
		
		
		onException(SocketException.class)
				.handled(true)
				.maximumRedeliveries(0)
				.log(LoggingLevel.ERROR,"Error for correlation : ${in.header.correlation} , SocketException with error "+exceptionMessage())
				.to(String.format("direct:%s",Routes.TIMEOUT_EXCEPTION_HANDLER_ROUTE));
		
		onException(SocketTimeoutException.class)
				.handled(true)
				.maximumRedeliveries(0)
				.log(LoggingLevel.ERROR,"Error for correlation : ${in.header.correlation} , SocketTimeoutException with error "+exceptionMessage())
				.to(String.format("direct:%s",Routes.TIMEOUT_EXCEPTION_HANDLER_ROUTE));
		
		
		onException(JsonMappingException.class)
				.handled(true)
				.maximumRedeliveries(0)
				.log(LoggingLevel.ERROR,"Error for JsonMappingException with error "+exceptionMessage())
				.to(String.format("direct:%s",Routes.JSON_EXCEPTION_HANDLER_ROUTE));
		
		
		onException(JsonParseException.class)
				.handled(true)
				.maximumRedeliveries(0)
				.log(LoggingLevel.ERROR,"Error for correlation : ${in.header.correlation} , JsonParseException with error "+exceptionMessage())
				.to(String.format("direct:%s",Routes.JSON_EXCEPTION_HANDLER_ROUTE));
		
		onException(BeanValidationException.class)
				.handled(true)
				.maximumRedeliveries(0)
				.log(LoggingLevel.ERROR,"Error for correlation : ${in.header.correlation} , BeanValidationException with error "+exceptionMessage())
				.to(String.format("direct:%s",Routes.BEAN_VALIDATION_EXCEPTION_HANDLER_ROUTE));
		
		onException(PredicateValidationException.class)
				.handled(true)
				.maximumRedeliveries(0)
				.log(LoggingLevel.ERROR,"Error for correlation : ${in.header.correlation} , PredicateValidationException with error "+exceptionMessage())
				.to(String.format("direct:%s",Routes.PREDICATE_EXCEPTION_HANDLER_ROUTE));

		onException(ResourceDuplicationException.class)
				.maximumRedeliveries(0)
				.handled(true)
				.log(LoggingLevel.ERROR,"Error for  correlation : ${in.header.correlation}  ResourceDuplicationException with error ===> "+exceptionMessage())
				.to(String.format("direct:%s",Routes.RESOURCE_DUPLICATION_EXCEPTION_ROUTE));

		onException(ResourceNotFoundException.class)
				.maximumRedeliveries(0)
				.handled(true)
				.log(LoggingLevel.ERROR,"Error for  correlation : ${in.header.correlation}  ResourceNotFoundException with error ===> "+exceptionMessage())
				.to(String.format("direct:%s",Routes.RESOURCE_NOTFOUND_EXCEPTION_ROUTE));

		onException(BadRequestException.class)
				.maximumRedeliveries(0)
				.handled(true)
				.log(LoggingLevel.ERROR,"Error for  correlation : ${in.header.correlation}  BadRequestException with error ===> "+exceptionMessage())
				.to(String.format("direct:%s",Routes.BAD_REQUEST_EXCEPTION_ROUTE));

		onException(DataNotFoundException.class)
				.maximumRedeliveries(0)
				.handled(true)
				.log(LoggingLevel.ERROR,"Error for  correlation : ${in.header.correlation}  DataNotFoundException with error ===> "+exceptionMessage())
				.to(String.format("direct:%s",Routes.DATA_NOTFOUND_EXCEPTION_ROUTE));

		onException(HttpOperationFailedException.class)
				.handled(true)
				.maximumRedeliveries(0)
				.log(LoggingLevel.ERROR,"Error for correlation : ${in.header.correlation} , HttpOperationFailedException with error "+exceptionMessage())
				.to(String.format("direct:%s",Routes.HTTP_OPERATION_EXCEPTION_HANDLER_ROUTE));

		from(String.format("direct:%s", Routes.THROWS_RESOURCE_DUPLICATION_EXCEPTION_ROUTE))
				.routeId( Routes.THROWS_RESOURCE_DUPLICATION_EXCEPTION_ROUTE)
				.routeGroup(Routes.EXCEPTION_HANDLER_ROUTE_GROUP)
				.process(exchange -> {
					String nationalCode= exchange.getIn().getHeader(Headers.nationalCode,String.class);
					throw new ResourceDuplicationException(String.format("person with nationalCode %s already exist",nationalCode));
				});

		from(String.format("direct:%s",Routes.THROWS_RESOURCE_NOTFOUND_EXCEPTION_ROUTE) )
				.routeId(Routes.THROWS_RESOURCE_NOTFOUND_EXCEPTION_ROUTE)
				.routeGroup(Routes.EXCEPTION_HANDLER_ROUTE_GROUP)
				.process(exchange -> {
					String nationalCode = exchange.getIn().getHeader(Headers.nationalCode,String.class);
					throw new ResourceNotFoundException(String.format("person with nationalCode %s does not exist",nationalCode));
				});
		from(String.format("direct:%s",Routes.THROWS_DATA_NOTFOUND_EXCEPTION_ROUTE) )
				.routeId(Routes.THROWS_DATA_NOTFOUND_EXCEPTION_ROUTE)
				.routeGroup(Routes.EXCEPTION_HANDLER_ROUTE_GROUP)
				.process(exchange -> {
					throw new DataNotFoundException("your data notfound");
				});
	}
}