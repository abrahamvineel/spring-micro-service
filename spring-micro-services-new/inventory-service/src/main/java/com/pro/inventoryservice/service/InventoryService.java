package com.pro.inventoryservice.service;

import com.pro.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCode(skuCode)
                .stream()
                .map(inventory -> );
    }
}
