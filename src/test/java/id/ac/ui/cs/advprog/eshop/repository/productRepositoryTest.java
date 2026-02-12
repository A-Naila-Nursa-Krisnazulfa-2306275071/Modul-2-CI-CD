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
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
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
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("da9f9de6-90b1-437d-a0bf-d0821dde9096");
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
        // buat product awal dan simpan ke repository
        Product product = new Product();
        product.setProductId("11111111-1111-1111-1111-111111111111");
        product.setProductName("Nama Lama");
        product.setProductQuantity(10);
        productRepository.create(product);

        // buat object "updatedProduct" dengan ID yang sama, tapi nilai name & quantity yang baru
        Product updatedProduct = new Product();
        updatedProduct.setProductId("11111111-1111-1111-1111-111111111111"); // ID harus sama biar ketemu
        updatedProduct.setProductName("Nama Baru");
        updatedProduct.setProductQuantity(999);

        // update
        Product result = productRepository.updateProduct(updatedProduct);

        // Update berhasil, result tidak null dan nilainya sudah berubah
        assertNotNull(result);
        assertEquals("11111111-1111-1111-1111-111111111111", result.getProductId());
        assertEquals("Nama Baru", result.getProductName());
        assertEquals(999, result.getProductQuantity());

        // pastikan data di repository benar-benar ikut berubah
        Product stored = productRepository.findProductById("11111111-1111-1111-1111-111111111111");
        assertNotNull(stored);
        assertEquals("Nama Baru", stored.getProductName());
        assertEquals(999, stored.getProductQuantity());
    }

    @Test
    void testUpdateProductFailIfIdNotFound() {
        // simpan satu product asli
        Product product = new Product();
        product.setProductId("22222222-2222-2222-2222-222222222222");
        product.setProductName("Produk Asli");
        product.setProductQuantity(50);
        productRepository.create(product);

        // buat updatedProduct dengan ID yang tidak ada di repository
        Product updatedProduct = new Product();
        updatedProduct.setProductId("33333333-3333-3333-3333-333333333333"); // tidak pernah dibuat
        updatedProduct.setProductName("Harusnya Gagal");
        updatedProduct.setProductQuantity(0);

        // Update harus gagal karena ID tidak ditemukan
        Product result = productRepository.updateProduct(updatedProduct);

        // karena ID tidak ditemukan, return null
        assertNull(result);

        // pastikan product asli tidak berubah
        Product stored = productRepository.findProductById("22222222-2222-2222-2222-222222222222");
        assertNotNull(stored);
        assertEquals("Produk Asli", stored.getProductName());
        assertEquals(50, stored.getProductQuantity());
    }

    @Test
    void testDeleteProductSuccess() {
        // simpan product lalu delete by ID
        Product product = new Product();
        product.setProductId("44444444-4444-4444-4444-444444444444");
        product.setProductName("Produk Untuk Dihapus");
        product.setProductQuantity(1);
        productRepository.create(product);

        // Pastikan product ada
        assertNotNull(productRepository.findProductById("44444444-4444-4444-4444-444444444444"));

        // delete
        productRepository.deleteProductById("44444444-4444-4444-4444-444444444444");

        // setelah dilakukan delete, product tidak boleh ditemukan lagi
        assertNull(productRepository.findProductById("44444444-4444-4444-4444-444444444444"));

        // repository harus kosong (karena cuma ada 1 data)
        Iterator<Product> it = productRepository.findAll();
        assertFalse(it.hasNext());
    }

    @Test
    void testDeleteProductFailIfIdNotFound() {
        // simpan satu product
        Product product = new Product();
        product.setProductId("55555555-5555-5555-5555-555555555555");
        product.setProductName("Produk Tetap Ada");
        product.setProductQuantity(123);
        productRepository.create(product);

        // hapus pakai ID yang salah / tidak ada
        productRepository.deleteProductById("66666666-6666-6666-6666-666666666666");

        // karena ID tidak ketemu, repository tidak boleh berubah
        Product stillThere = productRepository.findProductById("55555555-5555-5555-5555-555555555555");
        assertNotNull(stillThere);
        assertEquals("Produk Tetap Ada", stillThere.getProductName());
        assertEquals(123, stillThere.getProductQuantity());


        Iterator<Product> it = productRepository.findAll();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
    }
}
