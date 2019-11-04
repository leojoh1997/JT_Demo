package com.jt.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.anno.Cache_Find;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

@RequestMapping("/web/item")
@RestController
public class WebItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/findItemById")
	@Cache_Find(seconds = 7*24*3600)
	public Item findItemById(Long itemId) {
		return itemService.findItemById(itemId);
		
	}
	
	@RequestMapping("/findItemDescById")
	@Cache_Find(seconds = 7*24*3600)
	public ItemDesc findItemDescById(Long itemId) {
		return itemService.findItemDescById(itemId);
	}
	
}
