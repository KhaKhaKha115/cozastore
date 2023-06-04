package com.cybersoft.cozastore.controller;

import com.cybersoft.cozastore.payload.request.ProductResquest;
import com.cybersoft.cozastore.payload.response.BaseResponse;
import com.cybersoft.cozastore.service.ProductService;
import com.cybersoft.cozastore.service.imp.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductByCategory(@PathVariable int id){
        BaseResponse response = new BaseResponse();
        response.setData(iProductService.getProductByCategoryId(id));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Cách upload file chính:
     * C1: Chuyển file về dạng base64
     *   + Từ file chuyển thành chuỗi, đẩy chuỗi lên server
     *   + Từ chuỗi của file trên server chuyển chuỗi thành file
     * - Nhược điểm: tăng 1.5 kích thước file
     * - Ưu điểm: vì file đã chuyển thành chuỗi nên lưu trữ dc dưới dạng chuỗi
     *
     * C2: Dùng MultipartFile
     *   + Mở 1 luồng đọc vào file (stream)
     *   + Lưu file trong database: tenhinh.png
     *
     */
    @PostMapping("")
    public ResponseEntity<?> addProduct(@Valid ProductResquest productResquest ){
//        // Lấy tên file:
//        String filename = file.getOriginalFilename();
//        String rootFolder = "/Users/khakhakha/Documents/myproject/image ";// Đường dẫn lưu file
//
//        Path pathRoot = Paths.get(rootFolder);
//        if(!Files.exists(pathRoot)){
//            //Tạo folder
//            Files.createDirectory(pathRoot);
//        }
//        //pathRoot.resolve(filename): "/Users/khakhakha/Documents/myproject/image/
//        Files.copy(file.getInputStream(),pathRoot.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

        String filename = productResquest.getFile().getOriginalFilename();
        try {
            String rootFolder = "/Users/khakhakha/Documents/myproject/image";
            Path pathRoot = Paths.get(rootFolder);
            if (!Files.exists(pathRoot)) {
                Files.createDirectory(pathRoot);
            }
            Files.copy(productResquest.getFile().getInputStream(), pathRoot.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            boolean b = iProductService.addProduct(productResquest);
            System.out.println(b);
        }catch(Exception a){

        }

        return new ResponseEntity<>(filename,HttpStatus.OK);
    }


}
