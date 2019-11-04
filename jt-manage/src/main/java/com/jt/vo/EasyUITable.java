package com.jt.vo;

import java.util.List;

import com.jt.pojo.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 
 * 为了EasyUI数据回显 封装vo对象
 * 
 * @author LeoJoh
 *
 */

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUITable {
	
	private Integer total; //商品记录总数
	private List<Item> rows; //利用分页算法查询当前页的数据

}
