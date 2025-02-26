package com.example.MaxiPet.service;

import com.example.MaxiPet.dto.Builders.ReviewBuilder;
import com.example.MaxiPet.dto.ReviewDTO;
import com.example.MaxiPet.entity.*;
import com.example.MaxiPet.repository.ProductRepository;
import com.example.MaxiPet.repository.ReviewRepository;
import com.example.MaxiPet.repository.UserRepository;
import com.example.MaxiPet.validators.ReviewValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewValidator reviewValidator;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository, ReviewValidator reviewValidator){
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.reviewValidator = reviewValidator;
    }

    /**
     *
     * @param reviewDTO
     * @return
     * @throws Exception
     */
    public ReviewDTO createReview(ReviewDTO reviewDTO) throws Exception {
        reviewValidator.validateReviewFields(reviewDTO);

        Optional<User> optionalUser = userRepository.findById(reviewDTO.getUserId());

        Optional<Product> optionalProduct = productRepository.findById(reviewDTO.getProductId());

        Review review = Review.builder()
                .user(optionalUser.get())
                .product(optionalProduct.get())
                .text(reviewDTO.getText())
                .rating(reviewDTO.getRating())
                .build();

        review = reviewRepository.save(review);

        return ReviewBuilder.toReviewDTO(review);
    }

    /**
     *
     * @param reviewDTO
     * @return
     * @throws Exception
     */
    public ReviewDTO updateReview(ReviewDTO reviewDTO) throws Exception {
        reviewValidator.validateReviewDTOForUpdate(reviewDTO);

        Optional<Review> optionalReview = reviewRepository.findById(reviewDTO.getId());

        Review review = optionalReview.get();

        review.setText(reviewDTO.getText());
        review.setRating(reviewDTO.getRating());

        Review updatedReview = reviewRepository.save(review);

        return ReviewBuilder.toReviewDTO(updatedReview);
    }

    /**
     *
     * @param reviewId
     * @return
     * @throws Exception
     */
    public ReviewDTO getReview(Integer reviewId) throws Exception {
        reviewValidator.validateReviewId(reviewId);
        Optional<Review> review = reviewRepository.findById(reviewId);

        return ReviewBuilder.toReviewDTO(review.get());
    }

    /**
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public List<ReviewDTO> getReviewsByUserId(Integer userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            log.warn("The user with id {} was not found in the DB!", userId);
            throw new Exception("User not found");
        }

        List<Review> reviewList = reviewRepository.getAllByUser(user.get());

        return ReviewBuilder.toReviewDTOList(reviewList);
    }

    /**
     *
     * @param productId
     * @return
     * @throws Exception
     */
    public List<ReviewDTO> getReviewsByProductId(Integer productId) throws Exception {
        Optional<Product> product = productRepository.findById(productId);
        if(product.isEmpty()) {
            log.warn("The product with id {} was not found in the DB!", productId);
            throw new Exception("Product not found");
        }

        List<Review> reviewList = reviewRepository.getAllByProduct(product.get());

        return ReviewBuilder.toReviewDTOList(reviewList);
    }

    /**
     *
     * @return
     */
    public List<ReviewDTO> getReviews() {
        List<Review> reviewList = (List<Review>) reviewRepository.findAll();
        return ReviewBuilder.toReviewDTOList(reviewList);
    }

    /**
     *
     * @param reviewId
     * @throws Exception
     */
    public void deleteReview(Integer reviewId) throws Exception {
        reviewValidator.validateReviewId(reviewId);
        Optional<Review> review = reviewRepository.findById(reviewId);
        reviewRepository.deleteById(reviewId);
    }

}
