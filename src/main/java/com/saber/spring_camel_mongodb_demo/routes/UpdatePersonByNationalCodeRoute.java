package com.saber.spring_camel_mongodb_demo.routes;

import com.mongodb.client.model.Filters;
import com.saber.spring_camel_mongodb_demo.dto.PersonDto;
import com.saber.spring_camel_mongodb_demo.dto.UpdatePersonResponseDto;
import org.apache.camel.Exchange;
import org.apache.camel.component.mongodb.MongoDbConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.bson.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpdatePersonByNationalCodeRoute extends AbstractRestRouteBuilder {


    @Override
    public void configure() throws Exception {
        super.configure();

        from(String.format("direct:%s", Routes.UPDATE_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY))
                .routeId(Routes.UPDATE_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY)
                .routeGroup(Routes.UPDATE_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .setHeader(Headers.requestBody,simple("${in.body}"))
                .setHeader(Headers.nationalCode,simple("${in.body.nationalCode}"))
                .log("Request for update person with nationalCode ${in.header.nationalCode} with correlation ${in.header.correlation} with body ${in.body}")
                .to(String.format("direct:%s", Routes.UPDATE_PERSON_BY_NATIONAL_CODE_ROUTE));

        from(String.format("direct:%s", Routes.UPDATE_PERSON_BY_NATIONAL_CODE_ROUTE))
                .routeId(Routes.UPDATE_PERSON_BY_NATIONAL_CODE_ROUTE)
                .routeGroup(Routes.UPDATE_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE))
                .choice()
                .when(body().isNull())
                .to(String.format("direct:%s", Routes.THROWS_RESOURCE_NOTFOUND_EXCEPTION_ROUTE))
                .otherwise()
                .to(String.format("direct:%s",Routes.DATE_CONVERTER_ROUTE))
                .setHeader(MongoDbConstants.OPERATION_HEADER, constant("update"))
                .process(exchange -> {
                    String nationalCode = exchange.getIn().getHeader(Headers.nationalCode,String.class);
                    exchange.getIn().setHeader(MongoDbConstants.CRITERIA, Filters.eq(Headers.nationalCode, nationalCode));
                })
                .marshal().json(JsonLibrary.Jackson,PersonDto.class)
                .unmarshal().json(JsonLibrary.Jackson)
                .process(exchange -> {
                    @SuppressWarnings("unchecked")
                    Map<String,Object> jsonMap = (Map<String, Object>) exchange.getIn().getBody();
                    BsonDocument personDocument = new BsonDocument();
                    jsonMap.remove("nationalCode");
                    jsonMap.forEach((key,value)->{
                        personDocument.append(key,new BsonString(value.toString()));
                    });
                    BsonDocument document = new BsonDocument("$set",personDocument);
                    exchange.getIn().setBody(document);
                })
                .log("Request for update person with nationalCode ${in.header.nationalCode} with correlation ${in.header.correlation} with body ${in.body}")
                .to("mongodb:{{camel.component.mongodb.mongo-connection}}?database={{spring.data.mongodb.dataBaseCollection}}&collection={{spring.data.mongodb.collection}}&outputType=Document")
                .log("Response for update person with nationalCode ${in.header.nationalCode} with correlation ${in.header.correlation} ===> ${in.body}")
                .to(String.format("direct:%s", Routes.UPDATE_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT));

        from(String.format("direct:%s", Routes.UPDATE_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT))
                .routeId(Routes.UPDATE_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT)
                .routeGroup(Routes.UPDATE_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .marshal().json(JsonLibrary.Jackson,UpdatePersonResponseDto.class)
                .unmarshal().json(JsonLibrary.Jackson,UpdatePersonResponseDto.class)
                .process(exchange -> {
                    UpdatePersonResponseDto responseDto = exchange.getIn().getBody(UpdatePersonResponseDto.class);
                    responseDto.setCode(0);
                    responseDto.setText("your person updated with successfully");
                    exchange.getIn().setBody(responseDto);
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

    }
}
