package com.jt.test.redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;

public class TestJSON {
	
	/*
	 * 对象转化json时调用对象的get某某方法
	 */
	@Test
	public void toJson() throws IOException {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(1000L);
		itemDesc.setItemDesc("我是商品描述信息！");
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(itemDesc);
		System.out.println("json"+json);
		
		//将json串转化为对象格式
		ItemDesc idesc = mapper.readValue(json, ItemDesc.class);
		System.out.println("对象"+idesc);
	}
	
	/*
	 * 将list集合转化为json
	 */
	@Test
	public void testList() throws IOException {
		ItemDesc itemDesc1 = new ItemDesc();
		itemDesc1.setItemId(1001L);
		itemDesc1.setItemDesc("我是商品描述信息1！");
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemId(1002L);
		itemDesc2.setItemDesc("我是商品描述信息2！");
		ItemDesc itemDesc3 = new ItemDesc();
		itemDesc3.setItemId(1003L);
		itemDesc3.setItemDesc("我是商品描述信息3！");
		
		List<ItemDesc> list = new ArrayList<ItemDesc>();
		list.add(itemDesc1);
		list.add(itemDesc2);
		list.add(itemDesc3);
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(list);
		System.out.println(json);
		
		//将list JSON串转化为List对象
		List<ItemDesc> list1 = mapper.readValue(json, list.getClass());
		System.out.println(list1);
	}
	
}
