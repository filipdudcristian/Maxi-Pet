package com.example.MaxiPet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    /**
     *
     * @return
     */
    @GetMapping("/")
    public ModelAndView homePage()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        return modelAndView;
    }


    /**
     *
     * @return
     */
    @GetMapping("/AdminPage/AdminPage")
    public ModelAndView adminPage()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/AdminPage/AdminPage");
        return modelAndView;
    }

    /**
     *
     * @return
     */
    @GetMapping("/ClientPage/AdminPage")
    public ModelAndView clientPage()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/ClientPage/AdminPage");
        return modelAndView;
    }

}
