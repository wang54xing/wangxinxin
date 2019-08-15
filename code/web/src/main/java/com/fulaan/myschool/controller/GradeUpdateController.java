package com.fulaan.myschool.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.pojo.school.ClassEntry;
import com.pojo.school.Grade;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;

/**
 * 用于学年升级
 * @author fourer
 *
 */
@Controller
@RequestMapping("/grade")
public class GradeUpdateController extends BaseController {

	private static final Logger logger=Logger.getLogger("GradeUpdate");
	
	private static Set<Integer> noNeedUpdateSet =new HashSet<Integer>();
	private static Map<Integer,String> nameMap =new HashMap<Integer, String>();
	static
	{
		noNeedUpdateSet.add(6);
		noNeedUpdateSet.add(9);
		noNeedUpdateSet.add(14);
		noNeedUpdateSet.add(12);
		noNeedUpdateSet.add(13);
		
		nameMap.put(1, "一年级");
		nameMap.put(2, "二年级");
		nameMap.put(3, "三年级");
		nameMap.put(4, "四年级");
		nameMap.put(5, "五年级");
		nameMap.put(6, "六年级");
		
		nameMap.put(7, "初一");
		nameMap.put(8, "初二");
		nameMap.put(9, "初三");
		
		nameMap.put(10, "高一");
		nameMap.put(11, "高二");
		nameMap.put(12, "高三");
	}
	
	private static final Object lock=new Object();
	
	private SchoolService schoolService =new SchoolService();
	private ClassService classService =new ClassService();
	private TeacherClassSubjectService teacherClassSubjectService =new TeacherClassSubjectService();
	
	
	@UserRoles({UserRole.HEADMASTER,UserRole.ADMIN})
	@RequestMapping("/update")
	@ResponseBody
	public RespObj gradeUpdate( ObjectId schoolId  )
	{
		synchronized (lock) {
			
			String gradeName=getYearString()+"毕业班";
			
			SchoolEntry school=schoolService.getSchoolEntry(schoolId,Constant.FIELDS);
			
		    boolean updated =false;
			for(Grade g:school.getGradeList())
			{
				if(g.getName().equals(gradeName))
				{
					updated=true;
				}
			}
			
			
			if(updated)
			{
				RespObj obj =new RespObj(Constant.FAILD_CODE, "已经升级过");
				return obj;
			}
			
			logger.info("school name:"+school.getName());
			logger.info("school Id:"+school.getID());
			logger.info("user:"+getSessionValue());
			
			Grade grade =new Grade(gradeName, -1, null, null);
			schoolService.addGrade(schoolId.toString(), grade);
			//再次获取
			school=schoolService.getSchoolEntry(schoolId,Constant.FIELDS);
			
			Set<Integer> gradeSet =new HashSet<Integer>();
			Map<Integer,ObjectId> gradeIntegerMap =new HashMap<Integer, ObjectId>();
			ObjectId biyeObjectId=null;
			for(Grade gg:school.getGradeList())
			{
				if(gg.getGradeType()!=-1)
				{
				  gradeSet.add(gg.getGradeType());
				  gradeIntegerMap.put(gg.getGradeType(), gg.getGradeId());
				}
				if(gg.getName().equals(gradeName))
				{
					biyeObjectId=gg.getGradeId();
				}
			}
			List<ClassEntry> classes=classService.findClassInfoBySchoolId(schoolId,Constant.FIELDS);
			for(ClassEntry ce:classes)
			{
				  try {
					updateClassGrade(ce,gradeSet,gradeIntegerMap,biyeObjectId);
					Thread.sleep(5);
				} catch (Exception e) {
					
					logger.info("++++++++++++++++++++++++++++++++++++++++++++"+ce.getID().toString()+","+ce.getName());
					logger.info("++++++++++++++++++++++++++++++++++++++++++++"+e.getMessage());
				
				}
		    }
			
			return RespObj.SUCCESS;
		}
		
	}
	
	
	private String getYearString()
	{
		return DateTimeUtils.convert(System.currentTimeMillis(), DateTimeUtils.DATE_YEAE);
	}
	
	/**
	 * 
	 * @param ce
	 * @throws Exception 
	 */
	private  void updateClassGrade(ClassEntry ce,Set<Integer> gradeSet,Map<Integer,ObjectId> gradeIntegerMap,ObjectId biyeObjectId) throws Exception
	{
		
		int thisGrade=0;
		for(Map.Entry<Integer,ObjectId> entry:gradeIntegerMap.entrySet())
		{
			if(entry.getValue().equals(ce.getGradeId()))
			{
				thisGrade=entry.getKey();
				break;
			}
		}
		
		if(thisGrade==0)
		{
			logger.info( ce.getID().toString()+","+ce.getName()+";没有找到对应的年级或者该班级已经毕业");
			return;
		}
		
		if(noNeedUpdateSet.contains(thisGrade) )
		{
			classService.updateGrade(ce.getID(), biyeObjectId);
			String newName =newClassName(ce.getName());
			classService.updateClassName(ce.getID(), newName);
			teacherClassSubjectService.updateClassName(ce.getID(), newName);
			logger.info( ce.getID().toString()+","+ce.getName()+";该班级已经升级至毕业班；新班级名称："+newName);
		}
		else
		{
			thisGrade =thisGrade+1;
			if(!gradeSet.contains(thisGrade))
			{
				logger.info("add grade"+thisGrade);
				ObjectId gid=new ObjectId();
				Grade g =new Grade(gid,getGradeName(thisGrade), thisGrade, null, null);
				schoolService.addGrade(ce.getSchoolId().toString(), g);
				gradeSet.add(thisGrade);
				gradeIntegerMap.put(thisGrade, gid);
				logger.info(ce.getID().toString()+","+ce.getName()+";添加新的年级"+g.getBaseEntry().toString());
			}
			
			ObjectId newGradeId =gradeIntegerMap.get(thisGrade);
			classService.updateGrade(ce.getID(), newGradeId);;
			String newName =newClassName(ce.getName());
			classService.updateClassName(ce.getID(), newName);
			teacherClassSubjectService.updateClassName(ce.getID(), newName);
			logger.info( ce.getID().toString()+","+ce.getName()+";该班级已经升级到年级"+thisGrade+"；新班级名称："+newName);
		}
	}
	
	
	private String getGradeName(int gradeType)
	{
		if(nameMap.containsKey(gradeType))
		{
			return nameMap.get(gradeType);
		}
		return "";
	}
	
	
	public static String newClassName(String name)
	{

        if(name.contains("初三("))
        {
            name = name.substring(0,name.indexOf("初三("))+"2015毕业("+name.substring(name.indexOf("初三(")+3);
            return name;
        }
        if(name.contains("初三（"))
        {
            name = name.substring(0,name.indexOf("初三（"))+"2015毕业（"+name.substring(name.indexOf("初三（")+3);
            return name;
        }
        if(name.contains("初3("))
        {
            name = name.substring(0,name.indexOf("初3("))+"2015毕业("+name.substring(name.indexOf("初3(")+3);
            return name;
        }
        if(name.contains("初3（"))
        {
            name = name.substring(0,name.indexOf("初3（"))+"2015毕业（"+name.substring(name.indexOf("初3（")+3);
            return name;
        }
        if(name.contains("高三("))
        {
            name = name.substring(0,name.indexOf("高三("))+"2015毕业("+name.substring(name.indexOf("高三(")+3);
            return name;
        }
        if(name.contains("高三（"))
        {
            name = name.substring(0,name.indexOf("高三（"))+"2015毕业（"+name.substring(name.indexOf("高三（")+3);
            return name;
        }
        if(name.contains("高3("))
        {
            name = name.substring(0,name.indexOf("高3("))+"2015毕业("+name.substring(name.indexOf("高3(")+3);
            return name;
        }
        if(name.contains("高3（"))
        {
            name = name.substring(0,name.indexOf("高3（"))+"2015毕业（"+name.substring(name.indexOf("高3（")+3);
            return name;
        }


		if(name.contains("一("))
		{
            name = name.substring(0,name.indexOf("一("))+"二("+name.substring(name.indexOf("一(")+2);
			return name;
		}
        if(name.contains("一（"))
        {
            name = name.substring(0,name.indexOf("一（"))+"二（"+name.substring(name.indexOf("一（")+2);
            return name;
        }
        if(name.contains("1("))
        {
            name = name.substring(0,name.indexOf("1("))+"二("+name.substring(name.indexOf("1(")+2);
            return name;
        }
        if(name.contains("1（"))
        {
            name = name.substring(0,name.indexOf("1（"))+"二（"+name.substring(name.indexOf("1（")+2);
            return name;
        }



        if(name.contains("二("))
        {
            name = name.substring(0,name.indexOf("二("))+"三("+name.substring(name.indexOf("二(")+2);
            return name;
        }
        if(name.contains("二（"))
        {
            name = name.substring(0,name.indexOf("二（"))+"三（"+name.substring(name.indexOf("二（")+2);
            return name;
        }
        if(name.contains("2("))
        {
            name = name.substring(0,name.indexOf("2("))+"三("+name.substring(name.indexOf("2(")+2);
            return name;
        }
        if(name.contains("2（"))
        {
            name = name.substring(0,name.indexOf("2（"))+"三（"+name.substring(name.indexOf("2（")+2);
            return name;
        }





        if(name.contains("三("))
        {
            name = name.substring(0,name.indexOf("三("))+"四("+name.substring(name.indexOf("三(")+2);
            return name;
        }
        if(name.contains("三（"))
        {
            name = name.substring(0,name.indexOf("三（"))+"四（"+name.substring(name.indexOf("三（")+2);
            return name;
        }
        if(name.contains("3("))
        {
            name = name.substring(0,name.indexOf("3("))+"四("+name.substring(name.indexOf("3(")+2);
            return name;
        }
        if(name.contains("3（"))
        {
            name = name.substring(0,name.indexOf("3（"))+"四（"+name.substring(name.indexOf("3（")+2);
            return name;
        }

        if(name.contains("四("))
        {
            name = name.substring(0,name.indexOf("四("))+"五("+name.substring(name.indexOf("四(")+2);
            return name;
        }
        if(name.contains("四（"))
        {
            name = name.substring(0,name.indexOf("四（"))+"五（"+name.substring(name.indexOf("四（")+2);
            return name;
        }
        if(name.contains("4("))
        {
            name = name.substring(0,name.indexOf("4("))+"五("+name.substring(name.indexOf("4(")+2);
            return name;
        }
        if(name.contains("4（"))
        {
            name = name.substring(0,name.indexOf("4（"))+"五（"+name.substring(name.indexOf("4（")+2);
            return name;
        }


        if(name.contains("五("))
        {
            name = name.substring(0,name.indexOf("五("))+"六("+name.substring(name.indexOf("五(")+2);
            return name;
        }
        if(name.contains("五（"))
        {
            name = name.substring(0,name.indexOf("五（"))+"六（"+name.substring(name.indexOf("五（")+2);
            return name;
        }
        if(name.contains("5("))
        {
            name = name.substring(0,name.indexOf("5("))+"六("+name.substring(name.indexOf("5(")+2);
            return name;
        }
        if(name.contains("5（"))
        {
            name = name.substring(0,name.indexOf("5（"))+"六（"+name.substring(name.indexOf("5（")+2);
            return name;
        }

        if(name.contains("六("))
        {
            name = name.substring(0,name.indexOf("六("))+"2015毕业("+name.substring(name.indexOf("六(")+2);
            return name;
        }
        if(name.contains("六（"))
        {
            name = name.substring(0,name.indexOf("六（"))+"2015毕业（"+name.substring(name.indexOf("六（")+2);
            return name;
        }
        if(name.contains("6("))
        {
            name = name.substring(0,name.indexOf("6("))+"2015毕业("+name.substring(name.indexOf("6(")+2);
            return name;
        }
        if(name.contains("6（"))
        {
            name = name.substring(0,name.indexOf("6（"))+"2015毕业（"+name.substring(name.indexOf("6（")+2);
            return name;
        }


        if(name.contains("七("))
        {
            name = name.substring(0,name.indexOf("七("))+"八("+name.substring(name.indexOf("七(")+2);
            return name;
        }
        if(name.contains("七（"))
        {
            name = name.substring(0,name.indexOf("七（"))+"八（"+name.substring(name.indexOf("七（")+2);
            return name;
        }
        if(name.contains("7("))
        {
            name = name.substring(0,name.indexOf("7("))+"八("+name.substring(name.indexOf("7(")+2);
            return name;
        }
        if(name.contains("7（"))
        {
            name = name.substring(0,name.indexOf("7（"))+"八（"+name.substring(name.indexOf("7（")+2);
            return name;
        }


        if(name.contains("八("))
        {
            name = name.substring(0,name.indexOf("八("))+"九("+name.substring(name.indexOf("八(")+2);
            return name;
        }
        if(name.contains("八（"))
        {
            name = name.substring(0,name.indexOf("八（"))+"九（"+name.substring(name.indexOf("八（")+2);
            return name;
        }
        if(name.contains("8("))
        {
            name = name.substring(0,name.indexOf("8("))+"九("+name.substring(name.indexOf("8(")+2);
            return name;
        }
        if(name.contains("8（"))
        {
            name = name.substring(0,name.indexOf("8（"))+"九（"+name.substring(name.indexOf("8（")+2);
            return name;
        }


        if(name.contains("九("))
        {
            name = name.substring(0,name.indexOf("九("))+"2015毕业("+name.substring(name.indexOf("九(")+2);
            return name;
        }
        if(name.contains("九（"))
        {
            name = name.substring(0,name.indexOf("九（"))+"2015毕业（"+name.substring(name.indexOf("九（")+2);
            return name;
        }
        if(name.contains("9("))
        {
            name = name.substring(0,name.indexOf("9("))+"2015毕业("+name.substring(name.indexOf("9(")+2);
            return name;
        }
        if(name.contains("9（"))
        {
            name = name.substring(0,name.indexOf("9（"))+"2015毕业（"+name.substring(name.indexOf("9（")+2);
            return name;
        }

		

		
		

		
		return name;
	}
	
	public static void main(String[] args) {
		System.out.println("一（1）数学------->"+GradeUpdateController.newClassName("一（1）数学"));
        System.out.println("本部校区六（5）------->"+GradeUpdateController.newClassName("本部校区六（5）数学"));
        System.out.println("长流小学一（1）------->"+GradeUpdateController.newClassName("长流小学一（1）"));
        System.out.println("二（3）数学------->"+GradeUpdateController.newClassName("二（3）数学"));
        System.out.println("四（2）------->"+GradeUpdateController.newClassName("四（2）"));
        System.out.println("五（2）------->"+GradeUpdateController.newClassName("五（2）"));
        System.out.println("三（1）------->"+GradeUpdateController.newClassName("三（1）"));
        System.out.println("六（1）------->"+GradeUpdateController.newClassName("六（1）"));
        System.out.println("七（1）------->"+GradeUpdateController.newClassName("七（1）"));
        System.out.println("八（1）------->"+GradeUpdateController.newClassName("八（1）"));
        System.out.println("九（1）------->"+GradeUpdateController.newClassName("九（1）"));
        System.out.println("初一（1）------->"+GradeUpdateController.newClassName("初一（1）"));
        System.out.println("高二（5）------->"+GradeUpdateController.newClassName("高二（5）"));
        System.out.println("高三（5）------->"+GradeUpdateController.newClassName("高三（5）"));
        System.out.println("高一（5）------->"+GradeUpdateController.newClassName("高一（5）"));
        System.out.println("五铺中心校八（3）------->"+GradeUpdateController.newClassName("五铺中心校八（3）"));
        System.out.println("六（2）班------->"+GradeUpdateController.newClassName("六（2）班"));
        System.out.println("一（2）班------->"+GradeUpdateController.newClassName("一（2）班"));
        System.out.println("二（2）班------->"+GradeUpdateController.newClassName("二（2）班"));
        System.out.println("三（2）班------->"+GradeUpdateController.newClassName("三（2）班"));
        System.out.println("四（2）班------->"+GradeUpdateController.newClassName("四（2）班"));
        System.out.println("高一（33）------->"+GradeUpdateController.newClassName("高一（33）"));
        System.out.println("初二（9）------->"+GradeUpdateController.newClassName("初二（9）"));
        System.out.println("初三（9）------->"+GradeUpdateController.newClassName("初三（9）"));
        System.out.println("初三（9）班------->"+GradeUpdateController.newClassName("初三（9）班"));

	}
	
	
	
	
}
