package com.cybersoft.osahaneat.service;

import com.cybersoft.osahaneat.dto.FoodDTO;
import com.cybersoft.osahaneat.entity.CategoryRestaurant;
import com.cybersoft.osahaneat.entity.Food;
import com.cybersoft.osahaneat.repository.FoodRepository;
import com.cybersoft.osahaneat.service.imp.FileStorageServiceImp;
import com.cybersoft.osahaneat.service.imp.MenuServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService implements MenuServiceImp {
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    FileStorageServiceImp fileStorageServiceImp;

    @Override
    public boolean insertFood(MultipartFile file, String name, String description, double price, String instruction, int cate_res_id) {
        boolean isInsertSuccess = false;
        boolean isSuccess = fileStorageServiceImp.saveFile(file);
        if (isSuccess){
            //lưu dũ liệu khi file đã được lưu thành công
            try {
                Food food = new Food();
                food.setName(name);
                food.setDescription(description);
                food.setPrice(price);
                food.setIntruction(instruction);
                food.setImage(file.getOriginalFilename());

                CategoryRestaurant categoryRestaurant = new CategoryRestaurant();
                categoryRestaurant.setId(cate_res_id);
                food.setCategoryRestaurant(categoryRestaurant);
                foodRepository.save(food);
               isInsertSuccess = true;
            }catch (Exception e){
                System.out.println("Error insert food "+e.getMessage());
            }
        }
        return isInsertSuccess;
    }

    @Override
    @Cacheable("food")
    public List<FoodDTO> getAllFood() {
        List<Food> list = foodRepository.findAll();
        List<FoodDTO> dtoList = new ArrayList<>();
        for(Food food : list){
            FoodDTO foodDTO = new FoodDTO();
            foodDTO.setName(food.getName());
            foodDTO.setImage(food.getImage());

            dtoList.add(foodDTO);
        }
        return dtoList;
    }
}
