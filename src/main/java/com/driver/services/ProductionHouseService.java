package com.driver.services;


import com.driver.EntryDto.ProductionHouseEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.repository.ProductionHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProductionHouseService {

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addProductionHouseToDb(ProductionHouseEntryDto productionHouseEntryDto){
        ProductionHouse productionHouse1 = productionHouseRepository.findByName(productionHouseEntryDto.getName());

        if (productionHouse1 == null) {
            //make dto -> entity
            ProductionHouse productionHouse = new ProductionHouse();
            productionHouse.setName(productionHouseEntryDto.getName());
            productionHouse.setRatings(0.0);
            productionHouse.setWebSeriesList(new ArrayList<>());
            productionHouseRepository.save(productionHouse);
            return 0;
        }
        return null;
    }



}