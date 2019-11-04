package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Reference(timeout = 3000, check = false)
	private DubboOrderService dubboOrderService;
	@Reference(timeout = 3000, check = false)
	private DubboCartService dubboCartService;
	
	/**
	 * 跳转订单确认页面
	 *页面跳转: order-cart
	 *页面取值: ${carts}
	 */
	@RequestMapping("/create")
	public String creat(Model model) {
		Long userId = UserThreadLocal.get().getId();
		List<Cart> carts = dubboCartService.findCartListByUserId(userId);
		model.addAttribute("carts", carts);
		return "order-cart";
	}
	
	/*
	 * 实现订单入库操作
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult saveOrder(Order order) {
		Long userId = UserThreadLocal.get().getId();
		order.setUserId(userId);
		//需要返回页面数据
		String orderId = dubboOrderService.saveOrder(order);
		return SysResult.success(orderId);
	}
	
	/*
	 * 实现订单查询
	 */
	@RequestMapping("/success")
	public String findOrderById(String id, Model model) {
		Order order = dubboOrderService.findOrderById(id);
		model.addAttribute("order", order);
		return "success";
	}
	
}
