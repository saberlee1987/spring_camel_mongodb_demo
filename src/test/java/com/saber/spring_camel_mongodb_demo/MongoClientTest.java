package com.saber.spring_camel_mongodb_demo;

import com.saber.spring_camel_mongodb_demo.dto.inventory.AddInventoryResponseDto;
import com.saber.spring_camel_mongodb_demo.dto.inventory.InventoryDto;
import com.saber.spring_camel_mongodb_demo.dto.inventory.InventoryResponseDto;
import com.saber.spring_camel_mongodb_demo.dto.inventory.SizeDto;
import com.saber.spring_camel_mongodb_demo.services.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MongoClientTest {

    @Autowired
    private InventoryService inventoryService;

    @Test
    public void testFindAllInventories(){

        InventoryResponseDto inventoryResponseDto = inventoryService.findAll();
        System.out.println(inventoryResponseDto);
    }

    @Test
    public void testFindAllByStatus(){
        InventoryResponseDto inventoryResponseDto = inventoryService.findAllByStatus("A");
        System.out.println(inventoryResponseDto);
    }
    @Test
    public void testFindAllInStatuses(){
        InventoryResponseDto inventoryResponseDto = inventoryService.findAllInStatuses(List.of("A", "B"));
        System.out.println(inventoryResponseDto);
    }
    @Test
    public void testFindAllStatusAndQtLt(){
        InventoryResponseDto inventoryResponseDto = inventoryService.findAllByStatusAndQtyLt("A",35);
        System.out.println(inventoryResponseDto);
    }

    @Test
    public void testFindAllByStatuesOrQtLt(){
        InventoryResponseDto inventoryResponseDto = inventoryService.findAllByStatusOrQtyLt("A",25);
        System.out.println(inventoryResponseDto);
    }
    @Test
    public void testFindAllStatusAndQtyLtAndItem(){
        InventoryResponseDto inventoryResponseDto = inventoryService.findAllByStatusAndQtyLtAndItem("A",25,'p');
        System.out.println(inventoryResponseDto);
    }
    @Test
    public void testInsetInventory(){
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setItem("java");
        inventoryDto.setQty(45);
        SizeDto size = new SizeDto();
        size.setH(45);
        size.setW(20);
        size.setUom("cm");
        inventoryDto.setSize(size);
        inventoryDto.setStatus("B");
        AddInventoryResponseDto addInventoryResponseDto = inventoryService.insertOneInventory(inventoryDto);
        System.out.println(addInventoryResponseDto);
    }
}
