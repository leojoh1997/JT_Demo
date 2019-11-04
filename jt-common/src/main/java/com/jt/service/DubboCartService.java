package com.jt.service;

import java.util.List;

import com.jt.pojo.Cart;
import com.jt.pojo.Order;

public interface DubboCartService {

	List<Cart> findCartListByUserId(Long userId);

	void updateNum(Cart cart);

	void deleteCart(Cart cart);

	void insertCart(Cart cart);


}
