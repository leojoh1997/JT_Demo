package com.jt.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

@Service
public class ItemCatServiceImp implements ItemCatService{

	@Autowired
	private ItemCatMapper itemCatMapper;
	//@Autowired//后期缓存处理利用AOP完成
	private ShardedJedis jedis;
	
	
	@Override
	public String findItemCatNameById(long itemCatId) {
		ItemCat itemCat = itemCatMapper.selectById(itemCatId);
		return itemCat.getName();
	}
	
	public List<ItemCat> findItemCatList(Long parentId){
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent_id", parentId);
		List<ItemCat> itemCatList = itemCatMapper.selectList(queryWrapper);
		return itemCatList;
	}

	@Override
	public List<EasyUITree> findEasyUITreeList(Long parentId) {
		List<ItemCat> itemCatList = findItemCatList(parentId);
		List<EasyUITree> easyUITreeList = new ArrayList<EasyUITree>();
		for (ItemCat itemCat : itemCatList) {
			EasyUITree easyUITree = new EasyUITree();
			String state = itemCat.getIsParent()?"closed":"open";
			easyUITree.setId(itemCat.getId())
					  .setText(itemCat.getName())
					  .setState(state);
			easyUITreeList.add(easyUITree);
		}
		
		return easyUITreeList;
	}

	/**
	 * 添加缓存的实现
	 * 业务思路:
	 * 		key:value		
	 * key: key必须保证唯一,存取使用同一个key/可读性强
	 * value: List<EasyUITree>的json数据
	 * 	
	 * 	1.通过生成的key查询redis缓存服务器.
	 *  2.null表示没有数据,需要查询数据库,之后将数据转化为json.保存
	 *  到redis中,方便下次调用.
	 *  3.!null,表示redis缓存中有json数据.需要将json转化为对象.之后返回
	 * 
	 */	
	@Override
	public List<EasyUITree> findfindEasyUITreeCache(Long parentId) {
		List<EasyUITree> easyUITreeList = new ArrayList<EasyUITree>();
		String key = "ITEM_CAT" + parentId;
		String result = jedis.get(key);
		if(StringUtils.isEmpty(result)) {
			easyUITreeList = findEasyUITreeList(parentId);
			String value = ObjectMapperUtil.toJSON(easyUITreeList);
			jedis.set(key, value);
			System.out.println("从数据库中查询");
		}else {
			easyUITreeList = ObjectMapperUtil.toObject(result, easyUITreeList.getClass());
			System.out.println("从缓存中查询");
		}
		
		return easyUITreeList;
	}
	
}
