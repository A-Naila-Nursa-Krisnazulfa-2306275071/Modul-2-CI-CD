package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @InjectMocks
    ProductController productController;

    @Mock
    ProductService service;

    @Mock
    Model model;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateProductPage() {
        String viewName = productController.createProductPage(model);

        assertEquals("createProduct", viewName);

        verify(model, times(1)).addAttribute(eq("product"), any(Product.class));
    }

    @Test
    void testCreateProductPost() {
        Product product = new Product();
        product.setProductName("phuwin i love you 123");
        product.setProductQuantity(96);

        String viewName = productController.createProductPost(product, model);

        assertEquals("redirect:list", viewName);
        verify(service, times(1)).create(product);
    }

    @Test
    void testProductListPage() {
        Product product1 = new Product();
        product1.setProductId("id-unik-1");
        product1.setProductName("phuwin i love you 123");
        product1.setProductQuantity(11);

        Product product2 = new Product();
        product2.setProductId("id-unik-2");
        product2.setProductName("johnny faker 96");
        product2.setProductQuantity(22);

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        when(service.findAll()).thenReturn(products);

        String viewName = productController.productListPage(model);

        assertEquals("productList", viewName);
        verify(service, times(1)).findAll();
        verify(model, times(1)).addAttribute("products", products);
    }

    @Test
    void testProductListPageIfEmpty() {
        List<Product> emptyProducts = new ArrayList<>();
        when(service.findAll()).thenReturn(emptyProducts);

        String viewName = productController.productListPage(model);

        assertEquals("productList", viewName);
        verify(service, times(1)).findAll();
        verify(model, times(1)).addAttribute("products", emptyProducts);
    }

    @Test
    void testEditProductPageSuccess() {
        Product product = new Product();
        product.setProductId("edit-123");
        product.setProductName("phuwin i love you 123");
        product.setProductQuantity(77);

        when(service.findProductById("edit-123")).thenReturn(product);

        String viewName = productController.editProductPage("edit-123", model);

        assertEquals("editProduct", viewName);
        verify(service, times(1)).findProductById("edit-123");
        verify(model, times(1)).addAttribute("product", product);
    }

    @Test
    void testEditProductPageFailIfProductNotFound() {
        when(service.findProductById("ghost-id-96")).thenReturn(null);

        String viewName = productController.editProductPage("ghost-id-96", model);

        assertEquals("redirect:/product/list", viewName);
        verify(service, times(1)).findProductById("ghost-id-96");

        verify(model, never()).addAttribute(eq("product"), any());
    }

    @Test
    void testEditProductPost() {
        Product product = new Product();
        product.setProductId("fake-form-id");
        product.setProductName("johnny faker 96");
        product.setProductQuantity(321);

        String viewName = productController.editProductPost("real-path-id-123", product);

        assertEquals("redirect:list", viewName);

        assertEquals("real-path-id-123", product.getProductId());
        assertEquals("johnny faker 96", product.getProductName());
        assertEquals(321, product.getProductQuantity());

        verify(service, times(1)).updateProduct(product);
    }

    @Test
    void testDeleteProduct() {
        String viewName = productController.deleteProduct("trash-id-123");

        assertEquals("redirect:/product/list", viewName);
        verify(service, times(1)).deleteProductById("trash-id-123");
    }
}