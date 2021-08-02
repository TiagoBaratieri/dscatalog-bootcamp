package com.baratieri.dscatalog.repositories;

import com.baratieri.dscatalog.entities.Product;
import com.baratieri.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1l;
        nonExistingId = 1000l;
        countTotalProducts = 25l;
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdNull(){
       Product product = Factory.createProduct();
       product.setId(null);

       product = repository.save(product);
       Assertions.assertNotNull(product.getId());
       Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void findByIdShouldReturnNonEmptyOptionalWhenIdDoesNotExist(){

     repository.findById(existingId);

     Optional<Product> result = repository.findById(existingId);
     Assertions.assertTrue(result.isPresent());

    }

    @Test
    public void findByIdShouldNullObjectWhenIdExists(){

        repository.findById(nonExistingId);

        Optional<Product> result = repository.findById(nonExistingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }


    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, ()-> {
           repository.deleteById(nonExistingId);
        });
    }
}
