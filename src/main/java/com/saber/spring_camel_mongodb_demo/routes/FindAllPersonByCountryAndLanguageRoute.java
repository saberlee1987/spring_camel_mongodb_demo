package com.saber.spring_camel_mongodb_demo.routes;

import com.mongodb.client.model.Filters;
import com.saber.spring_camel_mongodb_demo.dto.PersonResponse;
import org.apache.camel.Exchange;
import org.apache.camel.component.mongodb.MongoDbConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class FindAllPersonByCountryAndLanguageRoute extends AbstractRestRouteBuilder {


    @Override
    public void configure() throws Exception {
        super.configure();

        from(String.format("direct:%s", Routes.FIND_PERSON_BY_COUNTRY_AND_LANGUAGE_ROUTE_GATEWAY))
                .routeId(Routes.FIND_PERSON_BY_COUNTRY_AND_LANGUAGE_ROUTE_GATEWAY)
                .routeGroup(Routes.FIND_PERSON_BY_COUNTRY_AND_LANGUAGE_ROUTE_GROUP)
                .log("Request for find person with country ${in.header.country} and language ${in.header.language} with correlation ${in.header.correlation}")
                .setHeader(MongoDbConstants.OPERATION_HEADER, constant("findAll"))
                .process(exchange -> {
                    String country = exchange.getIn().getHeader(Headers.country, String.class);
                    String language = exchange.getIn().getHeader(Headers.language, String.class);
                    exchange.getIn().setHeader(MongoDbConstants.CRITERIA, Filters.and(
                            Filters.eq("country", country)
                            , Filters.eq("language", language)
                    ));
                })
                .to("mongodb:{{camel.component.mongodb.mongo-connection}}?database={{spring.data.mongodb.dataBaseCollection}}&collection={{spring.data.mongodb.collection}}")
                .log("Response for find person with country ${in.header.country} and language ${in.header.language}  with condition : ${in.header.condition} correlation ${in.header.correlation} ===> ${in.body}")
                .to(String.format("direct:%s", Routes.FIND_PERSON_BY_COUNTRY_AND_LANGUAGE_ROUTE_GATEWAY_OUT));

        from(String.format("direct:%s", Routes.FIND_PERSON_BY_COUNTRY_AND_LANGUAGE_ROUTE_GATEWAY_OUT))
                .routeId(Routes.FIND_PERSON_BY_COUNTRY_AND_LANGUAGE_ROUTE_GATEWAY_OUT)
                .routeGroup(Routes.FIND_PERSON_BY_COUNTRY_AND_LANGUAGE_ROUTE_GROUP)
                .choice()
                .when(body().isEqualTo(""))
                .to(String.format("direct:%s", Routes.THROWS_DATA_NOTFOUND_EXCEPTION_ROUTE))
                .otherwise()
                .marshal().json(JsonLibrary.Jackson)
                .process(exchange -> {
                    String jsonBody = exchange.getIn().getBody(String.class);
                    jsonBody = String.format("{\"persons\":%s}", jsonBody);
                    exchange.getIn().setBody(jsonBody);
                })
                .unmarshal().json(JsonLibrary.Jackson, PersonResponse.class)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .end();

    }
}
