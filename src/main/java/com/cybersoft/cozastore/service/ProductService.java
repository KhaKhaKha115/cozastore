package com.cybersoft.cozastore.service;

import com.cybersoft.cozastore.entity.CategoryEntity;
import com.cybersoft.cozastore.entity.ColorEntity;
import com.cybersoft.cozastore.entity.ProductEntity;
import com.cybersoft.cozastore.entity.SizeEntity;
import com.cybersoft.cozastore.payload.request.ProductResquest;
import com.cybersoft.cozastore.payload.response.ProductResponse;
import com.cybersoft.cozastore.repository.ProductRepository;
import com.cybersoft.cozastore.service.imp.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductResponse> getProductByCategoryId(int id) {
        List<ProductEntity> list = productRepository.findByCategoryId(id);
        List<ProductResponse> productResponseList = new ArrayList<>();

        for (ProductEntity data : list) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setName(data.getName());
            productResponse.setImage(data.getImage());
            productResponse.setPrice(data.getPrice());

            productResponseList.add(productResponse);
        }

        return productResponseList;
    }
    @Override
    public boolean addProduct(ProductResquest pr){
        try {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setName(pr.getName());
            productEntity.setImage(pr.getFile().getOriginalFilename());
            productEntity.setPrice(pr.getPrice());
            productEntity.setQuantity(pr.getQuantity());

            ColorEntity cl = new ColorEntity();
            cl.setId(pr.getColorId());

            SizeEntity se = new SizeEntity();
            se.setId(pr.getSizeId());

            CategoryEntity ce = new CategoryEntity();
            ce.setId(pr.getCategoryId());

            productEntity.setColor(cl);
            productEntity.setSize(se);
            productEntity.setCategory(ce);

            productRepository.save(productEntity);
            return true;
        }catch(Exception e){
            System.out.println("Kiemtra " + e.getMessage());
            return false;
        }

    }
}
