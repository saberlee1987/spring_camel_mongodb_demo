package com.saber.spring_camel_mongodb_demo.routes;

import com.mongodb.client.model.Filters;
import com.saber.spring_camel_mongodb_demo.dto.ConditionEnum;
import com.saber.spring_camel_mongodb_demo.dto.person.PersonResponse;
import com.saber.spring_camel_mongodb_demo.exceptions.BadRequestException;
import org.apache.camel.Exchange;
import org.apache.camel.component.mongodb.MongoDbConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class FindAllPersonByAgeRoute extends AbstractRestRouteBuilder {


    @Override
    public void configure() throws Exception {
        super.configure();

        from(String.format("direct:%s", Routes.FIND_PERSON_BY_AGE_ROUTE_GATEWAY))
                .routeId(Routes.FIND_PERSON_BY_AGE_ROUTE_GATEWAY)
                .routeGroup(Routes.FIND_PERSON_BY_AGE_ROUTE_GROUP)
                .log("Request for find person with age ${in.header.age} with correlation ${in.header.correlation}")
                .setHeader(MongoDbConstants.OPERATION_HEADER, constant("findAll"))
                .process(exchange -> {
                    Integer age = exchange.getIn().getHeader(Headers.age, Integer.class);
                    ConditionEnum condition = exchange.getIn().getHeader(Headers.condition, ConditionEnum.class);
                    switch (condition) {
                        case Equal:
                            exchange.getIn().setHeader(MongoDbConstants.CRITERIA, Filters.eq("age", age));
                            break;
                        case LESSThan:
                            exchange.getIn().setHeader(MongoDbConstants.CRITERIA, Filters.lt("age", age));
                            break;
                        case LessEqual:
                            exchange.getIn().setHeader(MongoDbConstants.CRITERIA, Filters.lte("age", age));
                            break;
                        case GreatThan:
                            exchange.getIn().setHeader(MongoDbConstants.CRITERIA, Filters.gt("age", age));
                            break;
                        case GreatEqual:
                            exchange.getIn().setHeader(MongoDbConstants.CRITERIA, Filters.gte("age", age));
                            break;
                        default:
                            throw new BadRequestException("condition","condition is invalid");
                    }
                })
                .to("mongodb:{{camel.component.mongodb.mongo-connection}}?database={{spring.data.mongodb.dataBaseCollection}}&collection={{spring.data.mongodb.collection}}")
                .log("Response for find person with age ${in.header.age} with condition : ${in.header.condition} correlation ${in.header.correlation} ===> ${in.body}")
                .to(String.format("direct:%s", Routes.FIND_PERSON_BY_AGE_ROUTE_GATEWAY_OUT));

        from(String.format("direct:%s", Routes.FIND_PERSON_BY_AGE_ROUTE_GATEWAY_OUT))
                .routeId(Routes.FIND_PERSON_BY_AGE_ROUTE_GATEWAY_OUT)
                .routeGroup(Routes.FIND_PERSON_BY_AGE_ROUTE_GROUP)
                .choice()
                .when(body().isEqualTo("") )
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
