package com.example.MaxiPet.validators;

import com.example.MaxiPet.constants.ReviewConstants;
import com.example.MaxiPet.dto.ReviewDTO;
import com.example.MaxiPet.entity.Review;
import com.example.MaxiPet.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Log4j2
public class ReviewValidator {

    private final ReviewRepository reviewRepository;
    private final UserValidator userValidator;
    private final ProductValidator productValidator;

    public void validateReviewId(Integer reviewId) throws Exception {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (review.isEmpty()) {
            log.warn(ReviewConstants.REVIEW_NOT_FOUND, reviewId);
            throw new Exception(ReviewConstants.EXCEPTION_REVIEW_NOT_FOUND);
        }
    }

    public void validateReviewDTOForUpdate(ReviewDTO reviewDTO) throws Exception {
        if (reviewDTO == null) {
            log.warn(ReviewConstants.DTO_EMPTY);
            throw new Exception(ReviewConstants.EXCEPTION_DTO_NULL);
        }

        validateReviewId(reviewDTO.getId());
        validateReviewFields(reviewDTO);
    }

    public void validateReviewFields(ReviewDTO reviewDTO) throws Exception {
        userValidator.validateUserId(reviewDTO.getUserId());
        productValidator.validateProductId(reviewDTO.getProductId());

        if (reviewDTO.getText() == null || reviewDTO.getText().isEmpty()) {
            log.warn(ReviewConstants.TEXT_EMPTY);
            throw new Exception(ReviewConstants.EXCEPTION_TEXT_EMPTY);
        }
        if (reviewDTO.getRating() == null || reviewDTO.getRating() < 0 || reviewDTO.getRating() > 5) {
            log.warn(ReviewConstants.RATING_INVALID);
            throw new Exception(ReviewConstants.EXCEPTION_RATING_INVALID);
        }
    }
}
