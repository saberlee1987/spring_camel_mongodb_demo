package com.saber.spring_camel_mongodb_demo.routes;

import com.saber.spring_camel_mongodb_demo.dto.person.PersonResponse;
import org.apache.camel.Exchange;
import org.apache.camel.component.mongodb.MongoDbConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class FindAllPersonRoute extends AbstractRestRouteBuilder {


    @Override
    public void configure() throws Exception {
        super.configure();

        from(String.format("direct:%s", Routes.FIND_ALL_PERSON_ROUTE_GATEWAY))
                .routeId(Routes.FIND_ALL_PERSON_ROUTE_GATEWAY)
                .routeGroup(Routes.FIND_ALL_PERSON_ROUTE_GROUP)
                .log("Request for find all person with correlation ${in.header.correlation}")
                .setHeader(MongoDbConstants.OPERATION_HEADER, constant("findAll"))
                .to("mongodb:{{camel.component.mongodb.mongo-connection}}?database={{spring.data.mongodb.dataBaseCollection}}&collection={{spring.data.mongodb.collection}}")
                .log("Response for find all person with correlation ${in.header.correlation} ===> ${in.body}")
                .to(String.format("direct:%s", Routes.FIND_ALL_PERSON_ROUTE_GATEWAY_OUT));

        from(String.format("direct:%s", Routes.FIND_ALL_PERSON_ROUTE_GATEWAY_OUT))
                .routeId(Routes.FIND_ALL_PERSON_ROUTE_GATEWAY_OUT)
                .routeGroup(Routes.FIND_ALL_PERSON_ROUTE_GROUP)
                .marshal().json(JsonLibrary.Jackson)
                .process(exchange -> {
                    String jsonBody = exchange.getIn().getBody(String.class);
                    jsonBody = String.format("{\"persons\":%s}", jsonBody);
                    exchange.getIn().setBody(jsonBody);
                })
                .unmarshal().json(JsonLibrary.Jackson, PersonResponse.class)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

    }
}
