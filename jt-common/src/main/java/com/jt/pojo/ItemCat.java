package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("tb_item_cat")
public class ItemCat extends BasePojo{

	@TableId(type = IdType.AUTO)
	private long id;//主键自增长
	private long parentId; //商品分类父级id
	private String name;
	private Integer status;
	private Integer sortOrder;
	private Boolean isParent; //是否为父级
	
	
	
}
