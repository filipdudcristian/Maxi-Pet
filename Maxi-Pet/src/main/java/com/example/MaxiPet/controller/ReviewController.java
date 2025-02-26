package com.example.MaxiPet.controller;

import com.example.MaxiPet.dto.ProductDTO;
import com.example.MaxiPet.dto.ReviewDTO;
import com.example.MaxiPet.service.ReviewService;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@Validated
@RequestMapping(value = "/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getAllReviewsForProduct(@PathVariable Integer id) throws Exception {
        List<ReviewDTO> reviewDTOList = reviewService.getReviewsByProductId(id);
        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);

    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAllReviewsForUser(@PathVariable Integer id) throws Exception {
        List<ReviewDTO> reviewDTOList = reviewService.getReviewsByUserId(id);
        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }

    /**
     *
     * @return
     */
    @GetMapping("/allReviews")
    public ResponseEntity<?> get() {
        List<ReviewDTO> reviewDTOList = reviewService.getReviews();
        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);

    }

    /**
     *
     * @param reviewDTO
     * @return
     */
    @SneakyThrows
    @PostMapping
    public ResponseEntity<?> addReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO createdReview = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(createdReview, HttpStatus.OK);
    }

    /**
     *
     * @param reviewDTO
     * @return
     * @throws Exception
     */
    @PutMapping()
    public ResponseEntity<?> updateReview(@Valid @RequestBody ReviewDTO reviewDTO) throws Exception {
        ReviewDTO createdReview = reviewService.updateReview(reviewDTO);
        return new ResponseEntity<>(createdReview, HttpStatus.OK);

    }

    @PostMapping("/update")
    public ModelAndView update(@Valid @ModelAttribute ReviewDTO reviewDTO) {
        ModelAndView mav=new ModelAndView("redirect:/review/all");
        try {
            ReviewDTO updatedOrder = reviewService.updateReview(reviewDTO);
            mav.addObject("reviews",updatedOrder);
            return mav;
        } catch (Exception e) {
            return mav.addObject("error","Produsul cu id-ul respectiv nu exista");
        }
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id) throws Exception {
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteReview")
    public ModelAndView deleteProductPage(@RequestParam("id") Integer id) throws Exception {
        reviewService.deleteReview(id);
        return new ModelAndView("redirect:/review/all");
    }

    @PostMapping("/insertReview")
    public ModelAndView createReview(@ModelAttribute ReviewDTO reviewDTO) throws Exception {
        ModelAndView mav=new ModelAndView("redirect:/user/client/orders");
        reviewService.createReview(reviewDTO);
        return mav;
    }
    @GetMapping("/all")
    public ModelAndView getAllReviews() {
        ModelAndView mav=new ModelAndView("/ClientPage/reviews");
        List<ReviewDTO> reviewDTOList = reviewService.getReviews();

        mav.addObject("reviews",reviewDTOList);
        return mav;
    }

    @GetMapping("/reviews")
    public ModelAndView getAllReviewsAdmin() {
        ModelAndView mav=new ModelAndView("/AdminPage/reviews");
        List<ReviewDTO> reviewDTOList = reviewService.getReviews();

        mav.addObject("reviews",reviewDTOList);
        return mav;
    }
}
