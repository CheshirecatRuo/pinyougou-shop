package com.pinyougou.seckill.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/loading")
    public String loading(HttpServletRequest request)
    {
        String url = request.getHeader("referer");
        if (StringUtils.isNotBlank(url))
        {
            return "redirect:" + url;
        }
        return "redirect:/seckill-index.html";
    }
}
