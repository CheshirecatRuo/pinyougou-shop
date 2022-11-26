package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.http.Result;
import com.pinyougou.pojo.TbCart;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/list")
    public List<TbCart> list()
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //用户未登录
        String json = CookieUtil.getCookieValue(request, "CookieName", "UTF-8");
        List<TbCart> cookieCarts = JSON.parseArray(json, TbCart.class);
        if ("anonymousUser".equals(username))
        {
            return cookieCarts;
        }
        else
        {
            List<TbCart> redisCarts = cartService.findCartListFromRedis(username);
            if (cookieCarts != null && cookieCarts.size() > 0)
            {
                redisCarts = cartService.mergeCarts(redisCarts, cookieCarts);

                cartService.addGoodsToRedis(username, redisCarts);

                CookieUtil.deleteCookie(request, response, "CookieName");
            }
            return redisCarts;
        }

    }

    @RequestMapping("/add")
    @CrossOrigin(origins = "http://localhost:9004", allowCredentials = "true")
    public Result add(Long itemId, Integer num)
    {
        //解决跨域问题
        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:9004");
        //response.setHeader("Access-Control-Allow-Credentials", "true");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<TbCart> carts = list();
        carts = cartService.add(carts, itemId, num);

        if ("anonymousUser".equals(username)) {

            String json = JSON.toJSONString(carts);

            CookieUtil.setCookie(request, response, "CookieName", json, 2600 * 24 * 30, "UTF-8");
        }
        else
        {
            cartService.addGoodsToRedis(username, carts);
        }

        return new Result(true, "加入购物车成功");

    }

}
