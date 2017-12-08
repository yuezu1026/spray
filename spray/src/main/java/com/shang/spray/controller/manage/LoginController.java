package com.shang.spray.controller.manage;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.shang.spray.controller.BaseController;

/**
 * info:
 * Created by shang on 16/7/26.
 */
@RestController
public class LoginController extends BaseController{

    @RequestMapping(value = "",method = RequestMethod.GET)
    public ModelAndView index(ModelAndView view) {
        view.setViewName("login");
        return view;
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public ModelAndView login(ModelAndView view) {
        view.setViewName("login");
        return view;
    }

}
