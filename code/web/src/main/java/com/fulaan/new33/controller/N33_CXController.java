package com.fulaan.new33.controller;

import com.db.user.UserDao;
import com.fulaan.annotation.SessionNeedless;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.mails.MailUtils;
import com.sys.utils.RespObj;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/n33CX")
public class N33_CXController {

	private UserDao userDao = new UserDao();
	private static final Logger logger =Logger.getLogger(MailUtils.class);
	/**
	 * 更新超星用户Id
	 *
	 * @return
	 */
	@SessionNeedless
	@RequestMapping(method = RequestMethod.POST,value = "/updateUser")
	@ResponseBody
	public RespObj updateUser(@RequestBody Map map){
		logger.info("-------------------接收到参数-------------------");

		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		String typeString = (String) map.get("type");
		logger.info("-------------------type:" + typeString + "-------------------");
		Integer type = Integer.parseInt(typeString);
		String content = (String) map.get("content");
		logger.info("-------------------content:" + content + "-------------------");
		try {
			if(type == 1){
				logger.info("-------------------修改用户ID开始-------------------");
				String [] strArray = content.split("\\|\\|");
				String oldUserId = strArray[0];
				String newUserId = strArray[1];
				logger.info("-------------------老用户ID为" + oldUserId + ",新用户ID为" + newUserId + "-----------------------------------");
				UserEntry entry = userDao.searchUserByUserBind(9,oldUserId);
				entry.getUserBind().setBindValue(newUserId);
				userDao.update(entry.getID(),entry);
				obj.setMessage("更新成功");
				logger.info("-------------------修改用户ID结束-------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}
}
