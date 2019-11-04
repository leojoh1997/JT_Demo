package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Reference(timeout = 3000, check = false)
	private DubboUserService dubboUserService;
	@Autowired
	private JedisCluster jedisCluster;
	
	/**
	 * 实现用户页面跳转
	 * http://www.jt.com/user/register.html
	 * http://www.jt.com/user/login.html
	 */
	@RequestMapping("/{moduleName}")
	public String login(@PathVariable String moduleName) {
		return moduleName;
	}
	
	/*
	 * 实现用户的新增
	 */
	@ResponseBody
	@RequestMapping("/doRegister")
	public SysResult insertUser(User user) {
		dubboUserService.insertUser(user);
		return SysResult.success();
	}
	
	@RequestMapping("doLogin")
	@ResponseBody
	public SysResult doLogin(User user, HttpServletResponse response) {
		//1.获取服务器加密秘钥
		String ticket = dubboUserService.doLogin(user);
		if(StringUtils.isEmpty(ticket)) {
			return SysResult.fail();
		}
		//2.需要将数据保存到cookie中
		Cookie cookie = new Cookie("JT_TICKET", ticket);
		cookie.setMaxAge(7*24*3600);
		//设定cookie的使用权限.
		cookie.setPath("/");
		//设定cookie共享!!!!
		cookie.setDomain("jt.com");
		//将cookie写入浏览器
		response.addCookie(cookie);
		return SysResult.success();
	}
	
	/*
	 * 1.删除redis  1.获取cookie 2.获取值（密钥）
	 * 2.删除cookie 根据key
	 * 3.重定向页面
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		//1.获取cookie数据
		Cookie[] cookies = request.getCookies();
		//2.判断cookie是否有值
		String ticket = null;
		if(cookies.length>0) {
			for (Cookie cookie : cookies) {
				if("JT_TICKET".equals(cookie.getName())) {
					ticket = cookie.getValue();
					break;
				}
			}
		}
		//3.删除redis
		if(!StringUtils.isEmpty(ticket)) {
			jedisCluster.del(ticket);
		}
		/**
		 * 4.删除cookie
		 * setMaxAge
		 * 		>0  表示设定超时时间
		 * 		=0  表示立即删除cookie
		 * 		-1 当会话关闭后,删除cookie
		 */
		Cookie cookie = new Cookie("TI_TICKET", "");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		cookie.setDomain("jt.com");
		response.addCookie(cookie);
		//重定向到系统首页
		return "redirect:/";
	}
	
}
