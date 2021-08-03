package com.baratieri.dscatalog.services;

import com.baratieri.dscatalog.dto.ProductDTO;
import com.baratieri.dscatalog.entities.Category;
import com.baratieri.dscatalog.entities.Product;
import com.baratieri.dscatalog.repositories.CategoryRepository;
import com.baratieri.dscatalog.repositories.ProductRepository;
import com.baratieri.dscatalog.services.exceptions.DataBaseException;
import com.baratieri.dscatalog.services.exceptions.ResourceNotFoundException;
import com.baratieri.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private Category category;
    private Product product;
    private ProductDTO productDTO;
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1l;
        nonExistingId = 2l;
        dependentId = 3l;
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        category = Factory.createCategory();
        page = new PageImpl<>(List.of(product));

        Mockito.when(repository.getOne(existingId)).thenReturn(product);
        Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }

    @Test
    public void updateShouldThrowResourceNotfoundExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            service.update(nonExistingId,productDTO);
        });
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExist(){
        ProductDTO result = service.update(existingId,productDTO);

        Assertions.assertNotNull(result);

    }

    @Test
    public void findByIdShouldThrowResourceNotfoundExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            service.findById(nonExistingId);
        });
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists(){
        ProductDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);

    }


    @Test
    public void deleteShouldThrowDataBaseExceptionWhenDependentId(){
        Assertions.assertThrows(DataBaseException.class, ()->{
           service.delete(dependentId);
        });

        Mockito.verify(repository, times(1)).deleteById(dependentId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){

        Assertions.assertDoesNotThrow(()->{
            service.delete(existingId);
        });

        Mockito.verify(repository, times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExisting(){
        Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            service.delete(nonExistingId);
        });

        Mockito.verify(repository, times(1)).deleteById(nonExistingId);
    }



}
