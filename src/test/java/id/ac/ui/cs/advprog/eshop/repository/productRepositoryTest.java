package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("phuwin-i-love-123-you");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("naruto-hinata-123-098765");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("sasuke-sakura-456-112233");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());

        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testUpdateProductSuccess() {
        Product product = new Product();
        product.setProductId("luffy-zoro-111-aaaaaa");
        product.setProductName("Nama Lama");
        product.setProductQuantity(10);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("luffy-zoro-111-aaaaaa");
        updatedProduct.setProductName("Nama Baru");
        updatedProduct.setProductQuantity(999);

        Product result = productRepository.updateProduct(updatedProduct);

        assertNotNull(result);
        assertEquals("luffy-zoro-111-aaaaaa", result.getProductId());
        assertEquals("Nama Baru", result.getProductName());
        assertEquals(999, result.getProductQuantity());

        Product stored = productRepository.findProductById("luffy-zoro-111-aaaaaa");
        assertNotNull(stored);
        assertEquals("Nama Baru", stored.getProductName());
        assertEquals(999, stored.getProductQuantity());
    }

    @Test
    void testUpdateProductFailIfIdNotFound() {
        Product product = new Product();
        product.setProductId("tanjiro-nezuko-222-bbbbbb");
        product.setProductName("Produk Asli");
        product.setProductQuantity(50);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("inosuke-zenitsu-333-cccccc"); // tidak pernah dibuat
        updatedProduct.setProductName("Harusnya Gagal");
        updatedProduct.setProductQuantity(0);

        Product result = productRepository.updateProduct(updatedProduct);

        assertNull(result);

        Product stored = productRepository.findProductById("tanjiro-nezuko-222-bbbbbb");
        assertNotNull(stored);
        assertEquals("Produk Asli", stored.getProductName());
        assertEquals(50, stored.getProductQuantity());
    }

    @Test
    void testDeleteProductSuccess() {
        Product product = new Product();
        product.setProductId("gojo-geto-444-dddddd");
        product.setProductName("Produk Untuk Dihapus");
        product.setProductQuantity(1);
        productRepository.create(product);

        assertNotNull(productRepository.findProductById("gojo-geto-444-dddddd"));

        productRepository.deleteProductById("gojo-geto-444-dddddd");

        assertNull(productRepository.findProductById("gojo-geto-444-dddddd"));

        Iterator<Product> it = productRepository.findAll();
        assertFalse(it.hasNext());
    }

    @Test
    void testDeleteProductFailIfIdNotFound() {
        Product product = new Product();
        product.setProductId("itachi-shisui-555-eeeeee");
        product.setProductName("Produk Tetap Ada");
        product.setProductQuantity(123);
        productRepository.create(product);

        productRepository.deleteProductById("kakashi-obito-666-ffffff");

        Product stillThere = productRepository.findProductById("itachi-shisui-555-eeeeee");
        assertNotNull(stillThere);
        assertEquals("Produk Tetap Ada", stillThere.getProductName());
        assertEquals(123, stillThere.getProductQuantity());

        Iterator<Product> it = productRepository.findAll();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
    }

    @Test
    void testFindProductByIdSuccess() {
        Product product = new Product();
        product.setProductId("phuwin-i-love-123-you");
        product.setProductName("phuwin i love you 123");
        product.setProductQuantity(7);
        productRepository.create(product);

        Product found = productRepository.findProductById("phuwin-i-love-123-you");

        assertNotNull(found);
        assertEquals("phuwin i love you 123", found.getProductName());
        assertEquals(7, found.getProductQuantity());
    }
}