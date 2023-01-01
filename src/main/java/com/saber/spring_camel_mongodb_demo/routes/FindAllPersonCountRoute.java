package com.saber.spring_camel_mongodb_demo.routes;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.saber.spring_camel_mongodb_demo.dto.ConditionEnum;
import com.saber.spring_camel_mongodb_demo.dto.person.PersonResponseCountDto;
import com.saber.spring_camel_mongodb_demo.exceptions.BadRequestException;
import org.apache.camel.Exchange;
import org.apache.camel.component.mongodb.MongoDbConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FindAllPersonCountRoute extends AbstractRestRouteBuilder {


    @Override
    public void configure() throws Exception {
        super.configure();

        from(String.format("direct:%s", Routes.FIND_ALL_PERSON_COUNT_WITH_AGE_ROUTE_GATEWAY))
                .routeId(Routes.FIND_ALL_PERSON_COUNT_WITH_AGE_ROUTE_GATEWAY)
                .routeGroup(Routes.FIND_ALL_PERSON_COUNT_WITH_AGE_ROUTE_GROUP)
                .log("Request for find all person count with correlation ${in.header.correlation}")
                .setHeader(MongoDbConstants.OPERATION_HEADER, constant("aggregate"))
                .process(exchange -> {
                    List<Bson> bsonList = new ArrayList<>();
                    Integer age = exchange.getIn().getHeader(Headers.age, Integer.class);
                    ConditionEnum condition = exchange.getIn().getHeader(Headers.condition, ConditionEnum.class);
                    switch (condition) {
                        case Equal:
                            bsonList.add(Aggregates.match(Filters.eq("age", age)));
                            break;
                        case LESSThan:
                            bsonList.add(Aggregates.match(Filters.lt("age", age)));
                            break;
                        case LessEqual:
                             bsonList.add(Aggregates.match(Filters.lte("age", age)));
                            break;
                        case GreatThan:
                             bsonList.add(Aggregates.match(Filters.gt("age", age)));
                            break;
                        case GreatEqual:
                             bsonList.add(Aggregates.match(Filters.gte("age", age)));
                            break;
                        default:
                            throw new BadRequestException("condition","condition is invalid");
                    }
                    bsonList.add(Aggregates.count());
                    exchange.getIn().setBody(bsonList );
                })
                .to("mongodb:{{camel.component.mongodb.mongo-connection}}?database={{spring.data.mongodb.dataBaseCollection}}&collection={{spring.data.mongodb.collection}}&outputType=Document")
                .log("Response for find all person count  with correlation ${in.header.correlation} ===> ${in.body}")
                .to(String.format("direct:%s", Routes.FIND_ALL_PERSON_COUNT_WITH_AGE_ROUTE_GATEWAY_OUT));

        from(String.format("direct:%s", Routes.FIND_ALL_PERSON_COUNT_WITH_AGE_ROUTE_GATEWAY_OUT))
                .routeId(Routes.FIND_ALL_PERSON_COUNT_WITH_AGE_ROUTE_GATEWAY_OUT)
                .routeGroup(Routes.FIND_ALL_PERSON_COUNT_WITH_AGE_ROUTE_GROUP)
                .choice()
                .when(body().isEqualTo("") )
                .to(String.format("direct:%s", Routes.THROWS_DATA_NOTFOUND_EXCEPTION_ROUTE))
                .otherwise()
                .marshal().json(JsonLibrary.Jackson)
                .process(exchange -> {
                    String jsonBody = exchange.getIn().getBody(String.class);
                    jsonBody = jsonBody.replaceAll("\\[","");
                    jsonBody = jsonBody.replaceAll("]","");
                    exchange.getIn().setBody(jsonBody);
                })
                .unmarshal().json(JsonLibrary.Jackson, PersonResponseCountDto.class)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .end();

    }
}
