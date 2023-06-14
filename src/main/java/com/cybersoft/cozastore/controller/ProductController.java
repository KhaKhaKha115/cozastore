package com.cybersoft.cozastore.controller;

import com.cybersoft.cozastore.payload.request.ProductResquest;
import com.cybersoft.cozastore.payload.response.BaseResponse;
import com.cybersoft.cozastore.service.ProductService;
import com.cybersoft.cozastore.service.imp.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    @Value("${root.file.path}")
    private String rootPath;

    @Value("${host.name}")
    private String hostName;

    @Autowired
    IProductService iProductService;

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailProduct(
            @PathVariable int id
    ){
        BaseResponse response = new BaseResponse();
        response.setData(iProductService.getDetailProduct(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getProductByCategory(
            HttpServletRequest request,
            @PathVariable int id){

        logger.trace("Hello Trace Logger");
        logger.debug("Hello Debug");
        logger.info("Hello Info");
        logger.warn("Hello Warning");
        logger.error("Hello Error");

        String hostNameReq = request.getHeader("host");

        BaseResponse response = new BaseResponse();
        response.setData(iProductService.getProductByCategoryId(hostNameReq, id));

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
