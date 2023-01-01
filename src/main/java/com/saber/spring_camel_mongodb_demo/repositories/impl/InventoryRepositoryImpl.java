package com.saber.spring_camel_mongodb_demo.repositories.impl;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.saber.spring_camel_mongodb_demo.dto.inventory.InventoryDto;
import com.saber.spring_camel_mongodb_demo.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class InventoryRepositoryImpl implements InventoryRepository {

    private final MongoClient mongoClient;

    private MongoCollection<Document> inventoryCollection() {
        MongoDatabase mydb = mongoClient.getDatabase("mydb");
        return mydb.getCollection("inventory");
    }

    @Override
    public InsertOneResult insertOneInventory(InventoryDto inventoryDto) {
        MongoCollection<Document> inventoryCollection = inventoryCollection();
        return inventoryCollection.insertOne(Document.parse(inventoryDto.toString()));
    }

    @Override
    public InsertManyResult insertManyInventory(List<InventoryDto> inventories) {
        MongoCollection<Document> inventoryCollection = inventoryCollection();
        List<Document> inventoryDocuments = new ArrayList<>();
        for (InventoryDto inventory : inventories) {
            inventoryDocuments.add(Document.parse(inventory.toString()));
        }
        return inventoryCollection.insertMany(inventoryDocuments);
    }

    @Override
    public DeleteResult deleteInventory(String item) {
        MongoCollection<Document> inventoryCollection = inventoryCollection();
        return inventoryCollection.deleteMany(Filters.eq("item", item));
    }

    @Override
    public MongoCursor<Document> findAll() {
        MongoCollection<Document> inventoryCollection = inventoryCollection();
        FindIterable<Document> documents = inventoryCollection.find();
        return documents.cursor();
    }

    @Override
    public MongoCursor<Document> findAllByStatus(String status) {
        MongoCollection<Document> inventoryCollection = inventoryCollection();
        FindIterable<Document> inventoryIterable = inventoryCollection.find(Filters.eq("status", status));
        return inventoryIterable.iterator();
    }

    @Override
    public MongoCursor<Document> findAllInStatuses(List<String> statuses) {
        MongoCollection<Document> inventoryCollection = inventoryCollection();
        FindIterable<Document> inventoryIterable = inventoryCollection.find(Filters.in("status", statuses));
        return inventoryIterable.iterator();
    }

    @Override
    public MongoCursor<Document> findAllByStatusAndQtyLt(String status, Integer qty) {
        MongoCollection<Document> inventoryCollection = inventoryCollection();
        FindIterable<Document> inventoryIterable = inventoryCollection.find(
                Filters.and(
                        Filters.eq("status", status),
                        Filters.lt("qty", qty)
                )
        );
        return inventoryIterable.iterator();
    }

    @Override
    public MongoCursor<Document> findAllByStatusOrQtyLt(String status, Integer qty) {
        MongoCollection<Document> inventoryCollection = inventoryCollection();
        FindIterable<Document> inventoryIterable = inventoryCollection.find(
                Filters.or(
                        Filters.eq("status", status),
                        Filters.lt("qty", qty)
                )
        );
        return inventoryIterable.iterator();
    }

    @Override
    public MongoCursor<Document> findAllByStatusAndQtyLtAndItem(String status, Integer qty, char itemCharStart) {
        MongoCollection<Document> inventoryCollection = inventoryCollection();
        FindIterable<Document> inventoryIterable = inventoryCollection.find(
                Filters.and(
                        Filters.eq("status", status),
                        Filters.or(
                                Filters.lt("qty", qty),
                                Filters.regex("item", "^p")
                        )
                )
        );
        return inventoryIterable.iterator();
    }
}
