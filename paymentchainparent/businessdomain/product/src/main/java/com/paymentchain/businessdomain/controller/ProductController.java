package com.paymentchain.businessdomain.controller;

import com.paymentchain.businessdomain.entities.Product;
import com.paymentchain.businessdomain.exceptions.BussinesRuleException;
import com.paymentchain.businessdomain.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;


@RestController
@RequestMapping("/producto")
public class ProductController {


    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping()
    public ResponseEntity<List<Product>> list() {
        List<Product> products = productoRepository.findAll();
        return ResponseEntity.ok(products);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable(name = "id") long id) throws BussinesRuleException {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BussinesRuleException(
                        "NOT_FOUND",
                        "Producto con id " + id + " no encontrado",
                        HttpStatus.NOT_FOUND
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable long id, @RequestBody Product input) {
        Product save = productoRepository.save(input);
        return ResponseEntity.ok(save);
    }

    @PostMapping
    public ResponseEntity<Product> post(@RequestBody Product input) throws BussinesRuleException, UnknownHostException {
      if (input.getCode() == null || input.getCode().trim().isEmpty()){
          throw new BussinesRuleException(
                  "VALIDATION",
                  "El codigo del producto es requerido",
                  HttpStatus.BAD_REQUEST
          );
      }
      if (input.getName() == null || input.getName().trim().isEmpty()){
          throw new BussinesRuleException(
                  "VALIDATION",
                  "El nombre completo del producto es requerido",
                  HttpStatus.BAD_REQUEST
          );
      }

      Product saved = productoRepository.save(input);
      return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();

    }
}
