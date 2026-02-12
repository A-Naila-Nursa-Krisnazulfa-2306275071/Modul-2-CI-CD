package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testCreateProduct() {
        Product product = new Product();
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        when(productRepository.create(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product created = productService.create(product);

        assertNotNull(created.getProductId());
        assertDoesNotThrow(() -> UUID.fromString(created.getProductId())); // valid UUID format

        verify(productRepository, times(1)).create(product);
    }

    @Test
    void testFindAllProducts() {
        Product product1 = new Product();
        product1.setProductId("id-1");
        product1.setProductName("Produk 1");
        product1.setProductQuantity(10);

        Product product2 = new Product();
        product2.setProductId("id-2");
        product2.setProductName("Produk 2");
        product2.setProductQuantity(20);

        Iterator<Product> iterator = Arrays.asList(product1, product2).iterator();
        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> result = productService.findAll();

        assertEquals(2, result.size());
        assertEquals("id-1", result.get(0).getProductId());
        assertEquals("id-2", result.get(1).getProductId());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindProductById() {
        Product product = new Product();
        product.setProductId("abc");
        product.setProductName("Produk A");
        product.setProductQuantity(5);

        when(productRepository.findProductById("abc")).thenReturn(product);

        Product result = productService.findProductById("abc");

        assertNotNull(result);
        assertEquals("abc", result.getProductId());
        assertEquals("Produk A", result.getProductName());
        assertEquals(5, result.getProductQuantity());

        verify(productRepository, times(1)).findProductById("abc");
    }

    @Test
    void testUpdateProduct() {
        Product updated = new Product();
        updated.setProductId("xyz");
        updated.setProductName("Nama Baru");
        updated.setProductQuantity(999);

        when(productRepository.updateProduct(updated)).thenReturn(updated);

        Product result = productService.updateProduct(updated);

        assertNotNull(result);
        assertEquals("xyz", result.getProductId());
        assertEquals("Nama Baru", result.getProductName());
        assertEquals(999, result.getProductQuantity());

        verify(productRepository, times(1)).updateProduct(updated);
    }

    @Test
    void testDeleteProductById() {
        String id = "del-1";
        productService.deleteProductById(id);
        verify(productRepository, times(1)).deleteProductById(id);
    }
}
