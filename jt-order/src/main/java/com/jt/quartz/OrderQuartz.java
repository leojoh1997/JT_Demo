package com.jt.quartz;

import java.util.Calendar;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderMapper;
import com.jt.pojo.Order;

//准备订单定时任务
@Component
public class OrderQuartz extends QuartzJobBean{

	@Autowired
	private OrderMapper orderMapper;

	/*
	 * 业务需求
	 * 		如果超过30分钟，订单状态由1改为交易关闭6
	 * 判断条件	当前时间-创建时间>30分钟
	 */
	@Override
	@Transactional
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//利用时间工具类
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -30);
		Date timeOut = calendar.getTime();
		
		Order order = new Order();
		order.setStatus(6)
			 .setUpdated(new Date());
		UpdateWrapper<Order> updateWrapper = new UpdateWrapper<Order>();
		updateWrapper.eq("status", 1)
					 .lt("created", timeOut);
		orderMapper.update(order, updateWrapper);
		System.out.println("订单定时任务执行完成！");
	}
}
