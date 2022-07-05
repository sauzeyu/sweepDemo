package com.vecentek.back.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-06-29 10:49
 */
@RestController
public class TestController {
    @RequestMapping(value = "/testFlag", method = RequestMethod.PUT)
    public String flagTest(HttpServletResponse response) {
        response.setHeader("flag", "fanlapja4gd66fb4be9fd123f6oycD1up5XTWYmen");
        return "hello";
    }

}
