package com.jt.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		int start = (page-1)*rows;
		int total = itemMapper.selectCount(null);//获取商品信息记录总数
		List<Item> itemList = itemMapper.findItemByPage(start,rows);
		return new EasyUITable(total, itemList);
	}
	
	@Transactional //事务控制
	@Override
	public void saveItem(Item item, ItemDesc itemDesc) {
		item.setStatus(1) //表示正常
			.setCreated(new Date())
			.setUpdated(item.getCreated());
		itemMapper.insert(item);
		
		//完成商品详情入库
		itemDesc.setItemId(item.getId())
				.setCreated(item.getCreated())
				.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}

	@Transactional
	@Override
	public void updateItem(Item item, ItemDesc itemDesc) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		
		itemDesc.setItemId(item.getId())
				.setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}
	
	/**
	 * 批量更新
	 */
	@Override
	public void updateStatus(Long[] ids, int status) {
		Item item = new Item();
		item.setStatus(status)
			.setUpdated(new Date());
		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<Item>();
		List<Long> idList = Arrays.asList(ids);
		updateWrapper.in("id", idList);
		itemMapper.update(item, updateWrapper);
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		return itemDescMapper.selectById(itemId);
	}

	@Transactional
	@Override
	public void deleteItems(Long[] ids) {
		List<Long> idList = Arrays.asList(ids);
		itemMapper.deleteBatchIds(idList);
		itemDescMapper.deleteItemDesc(ids);
	}

	@Override
	public Item findItemById(Long itemId) {
		return itemMapper.selectById(itemId);
	}
	
	
	
	
	
	
	
}
