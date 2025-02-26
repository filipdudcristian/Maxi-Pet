package com.example.MaxiPet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.example.MaxiPet.dto.Builders.ReviewBuilder;
import com.example.MaxiPet.dto.ReviewDTO;
import com.example.MaxiPet.entity.Product;
import com.example.MaxiPet.entity.Review;
import com.example.MaxiPet.entity.User;
import com.example.MaxiPet.repository.ProductRepository;
import com.example.MaxiPet.repository.ReviewRepository;
import com.example.MaxiPet.repository.UserRepository;
import com.example.MaxiPet.service.ReviewService;
import com.example.MaxiPet.validators.ReviewValidator;
import com.example.MaxiPet.dto.ReviewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReviewServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewValidator reviewValidator;

    @InjectMocks
    private ReviewService reviewService;

    private User user;
    private Product product;
    private Review review;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setName("Doe");
        user.setEmail("john.doe@example.com");

        product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setStock(10);
        product.setDiscountedPrice(10.0F);

        review = new Review();
        review.setId(1);
        review.setProduct(product);
        review.setText("Great product!");
        review.setUser(user);
        review.setRating(5);
    }

    @Test
    public void testCreateReview_Success() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> {
            Review review = i.getArgument(0);
            review.setId(1);
            return review;
        });

        ReviewDTO reviewDTO = ReviewBuilder.toReviewDTO(review);
        ReviewDTO result = reviewService.createReview(reviewDTO);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals(1, result.getProductId());
        assertEquals("Great product!", result.getText());
        assertEquals(5, result.getRating());

        verify(reviewValidator).validateReviewFields(reviewDTO);
        verify(userRepository).findById(1);
        verify(productRepository).findById(1);
        verify(reviewRepository).save(any(Review.class));
    }
}
