package com.saber.spring_camel_mongodb_demo.services;

import com.saber.spring_camel_mongodb_demo.dto.inventory.AddInventoryResponseDto;
import com.saber.spring_camel_mongodb_demo.dto.inventory.DeleteInventoryResponseDto;
import com.saber.spring_camel_mongodb_demo.dto.inventory.InventoryDto;
import com.saber.spring_camel_mongodb_demo.dto.inventory.InventoryResponseDto;

import java.util.List;

public interface InventoryService {

    AddInventoryResponseDto insertOneInventory(InventoryDto inventoryDto);

    AddInventoryResponseDto insertManyInventory(List<InventoryDto> inventories);

    DeleteInventoryResponseDto deleteInventory(String item);

    InventoryResponseDto findAll();

    InventoryResponseDto findAllByStatus(String status);

    InventoryResponseDto findAllInStatuses(List<String> statuses);

    InventoryResponseDto findAllByStatusAndQtyLt(String status, Integer qty);

    InventoryResponseDto findAllByStatusOrQtyLt(String status, Integer qty);

    InventoryResponseDto findAllByStatusAndQtyLtAndItem(String status, Integer qty, char itemCharStart);


}
