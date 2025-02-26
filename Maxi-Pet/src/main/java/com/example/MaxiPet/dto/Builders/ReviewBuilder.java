package com.example.MaxiPet.dto.Builders;

import com.example.MaxiPet.dto.ReviewDTO;
import com.example.MaxiPet.entity.Review;

import java.util.Collections;
import java.util.List;

public class ReviewBuilder {

    public static ReviewDTO toReviewDTO(Review review) {
        return  ReviewDTO.builder()
                .id(review.getId())
                .text(review.getText())
                .rating(review.getRating())
                .productId(review.getProduct().getId())
                .userId(review.getUser().getId())
                .prodName(review.getProduct().getName())
                .build();
    }

    public static List<ReviewDTO> toReviewDTOList(List<Review> reviewList) {
        if (reviewList == null || reviewList.isEmpty()) {
            return Collections.emptyList();
        }
        return reviewList.stream().map(r -> ReviewBuilder.toReviewDTO(r)).toList();
    }
}
