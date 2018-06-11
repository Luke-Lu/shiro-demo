package com.shiro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Luke
 * @date 2018/5/31.
 */
@Controller
public class ShareController {

    @ResponseBody
    @RequestMapping(value = "/share", method = RequestMethod.GET)
    public String share() {

        return "share the code!";
    }


}
