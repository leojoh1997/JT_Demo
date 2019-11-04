package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

/*
 * 定义用户拦截器，实现用户权限判断
 * spring版本4，必须重写拦截器方法
 * spring版本5，不必重写全部，需要哪个重写哪个 default
 */

@Component
public class UserInterceptor implements HandlerInterceptor{
	
	@Autowired
	private JedisCluster jedisCluster;
	
	/*
	 * 重写prehandler校验用户是否登录
	 * boolean:
	 * 		true:请求放行
	 * 		false：请求拦截
	 * 			一般会有重定向执行
	 */
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Cookie[] cookies = request.getCookies();
		String ticket = null;
		if(cookies.length>0) {
			for (Cookie cookie : cookies) {
				if("JT_TICKET".equals(cookie.getName())) {
					ticket = cookie.getValue();
					break;
				}
			}
		}
		
		if(!StringUtils.isEmpty(ticket)) {
			String userJSON = jedisCluster.get(ticket);
			if(!StringUtils.isEmpty(userJSON)) {
				//方式1：初中级程序员必会request
				//User user = ObjectMapperUtil.toObject(userJSON, User.class);
				//request.setAttribute("JT_USER", user);
				
				//方式二：使用ThreadLocal实现
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
				UserThreadLocal.set(user);
				
				return true;
			}
		}
		//重定向到用户登录页面
		response.sendRedirect("/user/login.html");
		return false;//表示请求拦截
	}
	
	/*
	 * 删除ThreadLocal防止内存泄漏
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		UserThreadLocal.remove();
	}
	
	
}
