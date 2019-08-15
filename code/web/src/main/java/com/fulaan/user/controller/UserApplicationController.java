package com.fulaan.user.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.pojo.user.ApplicationUseInfo;
import com.sys.mails.MailUtils;
/**
 * 用户申请
 * @author fourer
 *
 */
@Controller
@RequestMapping("/user/application")
public class UserApplicationController extends BaseController {

	    private static final Logger logger=Logger.getLogger(UserApplicationController.class);
	    private static final String  NewLine="<br/>";
	    
	    /*
	    *首页申请试用按钮
	    *直接跳转
	    *
	    * */
	    @SessionNeedless
	    @RequestMapping(value = "/application-use",method = RequestMethod.GET)
	    public String go2ApplicationPage(){
	        return "application/application";
	    }

	    @SessionNeedless
	    @RequestMapping(value = "/form",method = RequestMethod.POST)
	    public String handleApplicationForm(@Validated @ModelAttribute ApplicationUseInfo applicationUseInfo,BindingResult bindingResult){
	        MailUtils sendMail=new MailUtils();
	        StringBuilder stringBuilder=new StringBuilder();
	        stringBuilder.append("姓名："+applicationUseInfo.getUserName()+NewLine);
	        stringBuilder.append("性别："+applicationUseInfo.getSex()+NewLine);
	        stringBuilder.append("手机号码："+applicationUseInfo.getCellPhoneNumber()+NewLine);
	        stringBuilder.append("邮箱："+applicationUseInfo.getEmail()+NewLine);
	        stringBuilder.append("地址："+applicationUseInfo.getAddress()+NewLine);
	        stringBuilder.append("学校名称："+applicationUseInfo.getSchoolName()+NewLine);
	        stringBuilder.append("年级："+applicationUseInfo.getGrade()+NewLine);
	        stringBuilder.append("班级："+applicationUseInfo.getClazz()+NewLine);
	        stringBuilder.append("老师所带科目："+applicationUseInfo.getSubject()+NewLine);
	        stringBuilder.append("申请学生账号人数："+applicationUseInfo.getStuNumber()+NewLine);
	        stringBuilder.append("申请老师账号人数："+applicationUseInfo.getTeacherCount()+NewLine);
	        stringBuilder.append("从何处了解k6kt："+applicationUseInfo.getKnowFromWhere());
	        try {
	            sendMail.sendMail("申请试用","catherine.zhao@fulaan.com",stringBuilder.toString());
	        } catch (Exception e) {
	            logger.error("申请试用邮件发送失败");
	            logger.error("", e);
	        }
	        return "redirect:/";
	    }
}
