package com.saber.spring_camel_mongodb_demo.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.saber.spring_camel_mongodb_demo.dto.inventory.AddInventoryResponseDto;
import com.saber.spring_camel_mongodb_demo.dto.inventory.DeleteInventoryResponseDto;
import com.saber.spring_camel_mongodb_demo.dto.inventory.InventoryDto;
import com.saber.spring_camel_mongodb_demo.dto.inventory.InventoryResponseDto;
import com.saber.spring_camel_mongodb_demo.repositories.InventoryRepository;
import com.saber.spring_camel_mongodb_demo.services.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ObjectMapper mapper;

    @Override
    public AddInventoryResponseDto insertOneInventory(InventoryDto inventoryDto) {
        InsertOneResult insertOneResult = inventoryRepository.insertOneInventory(inventoryDto);
        AddInventoryResponseDto addInventoryResponseDto = new AddInventoryResponseDto();
        if (insertOneResult.wasAcknowledged()) {
            addInventoryResponseDto.setCode(0);
            addInventoryResponseDto.setText("inventory inserted successfully");
        } else {
            addInventoryResponseDto.setCode(-1);
            addInventoryResponseDto.setText("inventory inserted failed");
        }
        return addInventoryResponseDto;
    }

    @Override
    public AddInventoryResponseDto insertManyInventory(List<InventoryDto> inventories) {
        InsertManyResult insertManyResult = inventoryRepository.insertManyInventory(inventories);
        AddInventoryResponseDto addInventoryResponseDto = new AddInventoryResponseDto();
        if (insertManyResult.wasAcknowledged()) {
            addInventoryResponseDto.setCode(0);
            addInventoryResponseDto.setText("inventories inserted successfully");
        } else {
            addInventoryResponseDto.setCode(-1);
            addInventoryResponseDto.setText("inventories inserted failed");
        }
        return addInventoryResponseDto;
    }

    @Override
    public DeleteInventoryResponseDto deleteInventory(String item) {
        DeleteResult deleteResult = inventoryRepository.deleteInventory(item);
        DeleteInventoryResponseDto deleteInventoryResponseDto = new DeleteInventoryResponseDto();
        if (deleteResult.wasAcknowledged()) {
            deleteInventoryResponseDto.setCode(0);
            deleteInventoryResponseDto.setText("inventories inserted successfully");
            deleteInventoryResponseDto.setDeletedCount(deleteResult.getDeletedCount());
        } else {
            deleteInventoryResponseDto.setCode(-1);
            deleteInventoryResponseDto.setText("inventories inserted failed");
        }
        return deleteInventoryResponseDto;
    }

    @Override
    public InventoryResponseDto findAll() {
        MongoCursor<Document> inventoriesDocument = inventoryRepository.findAll();
        return getInventoryResponse(inventoriesDocument);
    }

    @Override
    public InventoryResponseDto findAllByStatus(String status) {
        MongoCursor<Document> inventoriesDocument = inventoryRepository.findAllByStatus(status);
        return getInventoryResponse(inventoriesDocument);
    }

    @Override
    public InventoryResponseDto findAllInStatuses(List<String> statuses) {
        MongoCursor<Document> inventoriesDocument = inventoryRepository.findAllInStatuses(statuses);
        return getInventoryResponse(inventoriesDocument);
    }

    @Override
    public InventoryResponseDto findAllByStatusAndQtyLt(String status, Integer qty) {
        MongoCursor<Document> inventoriesDocument = inventoryRepository.findAllByStatusAndQtyLt(status,qty);
        return getInventoryResponse(inventoriesDocument);
    }

    @Override
    public InventoryResponseDto findAllByStatusOrQtyLt(String status, Integer qty) {
        MongoCursor<Document> inventoriesDocument = inventoryRepository.findAllByStatusOrQtyLt(status,qty);
        return getInventoryResponse(inventoriesDocument);
    }

    @Override
    public InventoryResponseDto findAllByStatusAndQtyLtAndItem(String status, Integer qty, char itemCharStart) {
        MongoCursor<Document> inventoriesDocument = inventoryRepository.findAllByStatusAndQtyLtAndItem(status,qty,itemCharStart);
        return getInventoryResponse(inventoriesDocument);
    }

    private InventoryResponseDto getInventoryResponse(MongoCursor<Document> inventoryCursor) {
        try {
            InventoryResponseDto inventoryResponseDto = new InventoryResponseDto();
            List<InventoryDto> inventories = new ArrayList<>();
            JsonWriterSettings jsonWriterSettings = JsonWriterSettings.builder()
                    .indent(true)
                    .objectIdConverter((objectId, writer) -> {
                        writer.writeString(objectId.toString());

                    })
                    .build();
            while (inventoryCursor.hasNext()) {
                Document next = inventoryCursor.next();
                inventories.add(mapper.readValue(next.toJson(jsonWriterSettings), InventoryDto.class));
            }
            inventoryResponseDto.setInventories(inventories);
            return inventoryResponseDto;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
