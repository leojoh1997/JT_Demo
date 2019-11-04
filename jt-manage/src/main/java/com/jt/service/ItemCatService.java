package com.jt.service;

import java.util.List;

import com.jt.vo.EasyUITree;

public interface ItemCatService {

	String findItemCatNameById(long itemCatId);

	List<EasyUITree> findEasyUITreeList(Long parentId);

	List<EasyUITree> findfindEasyUITreeCache(Long parentId);

}
