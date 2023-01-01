package com.saber.spring_camel_mongodb_demo.repositories;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.saber.spring_camel_mongodb_demo.dto.inventory.InventoryDto;
import org.bson.Document;

import java.util.List;

public interface InventoryRepository {

    InsertOneResult insertOneInventory(InventoryDto inventoryDto);
    InsertManyResult insertManyInventory(List<InventoryDto> inventories);
    DeleteResult deleteInventory(String item);
    MongoCursor<Document> findAll();
    MongoCursor<Document>  findAllByStatus(String status);
    MongoCursor<Document>  findAllInStatuses(List<String> statuses);
    MongoCursor<Document>  findAllByStatusAndQtyLt(String status,Integer qty);
    MongoCursor<Document>  findAllByStatusOrQtyLt(String status,Integer qty);
    MongoCursor<Document>  findAllByStatusAndQtyLtAndItem(String status, Integer qty, char itemCharStart);



}
