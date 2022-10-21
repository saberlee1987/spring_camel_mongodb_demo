package com.saber.spring_camel_mongodb_demo.routes;

import com.mongodb.client.model.Filters;
import com.saber.spring_camel_mongodb_demo.dto.PersonDto;
import org.apache.camel.Exchange;
import org.apache.camel.component.mongodb.MongoDbConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class FindPersonByNationalCodeRoute extends AbstractRestRouteBuilder {


    @Override
    public void configure() throws Exception {
        super.configure();

        from(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY))
                .routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY)
                .routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .log("Request for find person with nationalCode ${in.header.nationalCode} with correlation ${in.header.correlation}")
                .to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE))
                .to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT));

        from(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE))
                .routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE)
                .routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .setHeader(MongoDbConstants.OPERATION_HEADER, constant("findOneByQuery"))
                .process(exchange -> {
                    String nationalCode = exchange.getIn().getHeader(Headers.nationalCode,String.class);
                    exchange.getIn().setHeader(MongoDbConstants.CRITERIA, Filters.eq(Headers.nationalCode,nationalCode));
                })
                .to("mongodb:{{camel.component.mongodb.mongo-connection}}?database={{spring.data.mongodb.dataBaseCollection}}&collection={{spring.data.mongodb.collection}}&outputType=Document")
                .log("Response for find person with nationalCode ${in.header.nationalCode} with correlation ${in.header.correlation} ===> ${in.body}");

        from(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT))
                .routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT)
                .routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .choice()
                .when(body().isNull())
                     .to(String.format("direct:%s",Routes.THROWS_RESOURCE_NOTFOUND_EXCEPTION_ROUTE))
                .otherwise()
                .marshal().json(JsonLibrary.Jackson)
                .unmarshal().json(JsonLibrary.Jackson, PersonDto.class)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .end();

    }
}
