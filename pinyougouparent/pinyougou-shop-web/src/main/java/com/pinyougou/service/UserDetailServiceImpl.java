package com.pinyougou.service;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellersgoods.service.SellerService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailServiceImpl implements UserDetailsService {

    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //构建角色集合
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        TbSeller seller = sellerService.findOne(username);
        if (seller == null || !seller.getStatus().equals("1"))
        {
            return null;
        }
        return new User(username, seller.getPassword(), authorities);
    }
}
