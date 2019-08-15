package com.fulaan.myschool.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.groupdiscussion.service.EaseMobService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.ExcelTeacherRecord.GradeAndClass;
import com.fulaan.myschool.service.ExcelService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.fulaan.teacher.service.TeacherService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.PinYin2Abbreviation;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.school.*;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.props.Resources;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.MD5Utils;
import com.sys.utils.NameShowUtils;
import com.sys.utils.RespObj;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 人员管理，一般用于人员导入
 * @author fourer
 *
 */
@Controller
@RequestMapping("/user/mamager")
public class UserManagerController extends BaseController {
	
	private static final Logger importLog =Logger.getLogger("IMPORTLOG");
	
    @Resource
    private UserService userService;
    @Resource
    private SchoolService schoolService;
    @Resource
    private TeacherService teacherService;
    @Resource
    private ClassService classService;
    @Resource
    private TeacherClassSubjectService teacherClassSubjectService;
    @Autowired
    private EaseMobService easeMobService;
    
    

    @RequestMapping("/down/school/members/load")
    @UserRoles({UserRole.HEADMASTER})
    public String loadSchoolUser()
    {
    	return "myschool/school_member_load";
    }
    

	/**
     * 数据导入页面
     * @return
     */
    @RequestMapping("/load/importexcel")
    public String loadImportPage()
    {
    	return "myschool/importexcel";
    }
    
    
   
    /**
     * 数据检测 
     * @param file
     * @param logo
     * @param type
     * @param schoolName
     * @param regid
     * @param pwd
     * @param schoolId
     * @param token
     * @param req
     * @return
     * @throws IOException
     */
    private static final String seg="<br/>";
    
    
    
    @ResponseBody
    @RequestMapping("datacheck")
    public RespObj dataCheck(@RequestParam("file") MultipartFile file,@ObjectIdType(isRequire=false) ObjectId schoolId,String token,HttpServletRequest req) throws IOException
    {
    	
        String resToken=Resources.getProperty("token","123456");
        
        if(StringUtils.isBlank(token) || !resToken.equalsIgnoreCase(resToken))
        {
        	return new RespObj(Constant.FAILD_CODE, "token秘钥错误");
        }
        
   	    final Map<String,Object> resMap= ExcelService.findTeacherAndStudentRecord(file.getInputStream());
   	    @SuppressWarnings("unchecked")
		List<ExcelTeacherRecord> excelTeacherRecords=(List<ExcelTeacherRecord>)resMap.get("teacherList");
   	    
   	    Set<String> classNameSet =new HashSet<String>();
   	    
   	    if(null!=schoolId)
   	    {
   	    	List<ClassEntry> clist=classService.findClassInfoBySchoolId(schoolId,new BasicDBObject("nm",1));
   	    	if(null!=clist && !clist.isEmpty())
   	    	{
   	    		for(ClassEntry ce:clist)
   	    		{
   	    			String name=ce.getName().replace("(", "（").replace(")", "）");
   	    			classNameSet.add(name);
   	    		}
   	    	}
   	    }
   	    
   	    //验证老师必填
   	    StringBuffer teacherstr =new StringBuffer();
   	    for(ExcelTeacherRecord er:excelTeacherRecords)
   	    {
   	    	
   	    	StringBuffer erStr =new StringBuffer();
   	    	String name=er.getTeacherName();
   	    	int sex=er.getSex();
   	    
   	    	if(StringUtils.isBlank(name))
   	    	{
   	    		erStr.append("老师姓名为空");
   	    		teacherstr.append(erStr.toString()).append(seg);
   	    		continue;
   	    	}
   	    	if(sex!=0 && sex!=1)
   	    	{
   	    		erStr.append("老师"+name+"性别错误; ");
   	    		teacherstr.append(erStr.toString()).append(seg);
   	    	}
   	    	
   	    	for(GradeAndClass gac:er.getGradeAndClassList())
   	    	{
   	    		String gacName=gac.getClassName().replace("(", "（").replace(")", "）");
	    	    classNameSet.add(gacName);
   	    	}
   	    }

   	    
   	    //验证学生必填
        @SuppressWarnings("unchecked")
		List<ExcelStudentRecord> studentRecords=(List<ExcelStudentRecord>)resMap.get("studentList");
        StringBuffer stuentstr =new StringBuffer();
	    for(ExcelStudentRecord er:studentRecords)
	    {
	    	StringBuffer erStr =new StringBuffer();
	    	String name=er.getStudentName();
	    	int sex=er.getSex();
	    	int grandInt =er.getGradeCode();
	    	String classStr=er.getClassName();
	    	
	    	if(StringUtils.isBlank(name))
	    	{
	    		erStr.append("学生姓名为空");
	    		stuentstr.append(erStr.toString()).append(seg);
	    		continue;
	    	}
	    	if(sex!=0 && sex!=1)
	    	{
	    		erStr.append("学生"+name+"性别错误; ");
	    	}
	    	
	    	if(grandInt<=0 || grandInt>=12 )
	    	{
	    		erStr.append("学生"+name+"年级代码错误; ");
	    	}
	    	
	    	if(StringUtils.isBlank(classStr))
	    	{
	    		erStr.append("学生"+name+"班级名称为空; ");
	    	}
	    	else 
	    	{
	    		classStr=classStr.replace("(", "（").replace(")", "）");
	    		if(!classNameSet.contains(classStr))
	    		{
	    			erStr.append("学生"+name+"班级["+classStr+"] 无法找到" );
	    		}
	    	}
	    	
	    	if(erStr.length()>0)
	    	{
	    	 stuentstr.append(erStr.toString()).append(seg);
	    	}
	    }
	    
	    teacherstr.append(stuentstr.toString());
	    RespObj obj =new RespObj(Constant.SUCCESS_CODE);
	    if(teacherstr.length()>0)
	    {
	    	obj =new RespObj(Constant.FAILD_CODE);
	    }
	    
	    
	    
	    if(!obj.getCode().equals("200"))
	    {
	    	 obj.setMessage(teacherstr.toString());
	    	 return obj;
	    }
	    
	    String alertStr=getAlertString(excelTeacherRecords, studentRecords,schoolId);
	    teacherstr.append(alertStr);
	    obj.setMessage(teacherstr.toString());
	    
    	return obj;
    }
    
    
    
    /**
	 
	    4：如果有学科名称，建议年级代码 年级名称 班级名称做补全处理；
	         年级名称 年级代码 班级名称 要同时出现，并且出现时一致 （建议命名参考模板）
	         如果有英文"("统一替换成"（"
	         
    **/
    
    /**
     * 得到提示文字
     * @param excelTeacherRecords
     * @param studentRecords
     * @return
     */
    private String getAlertString(List<ExcelTeacherRecord> excelTeacherRecords,List<ExcelStudentRecord> studentRecords,ObjectId schoolId)
    {
    	
    	StringBuilder builder =new StringBuilder();
    	
    	Set<String> teacherSet =new HashSet<String>();
    	//老师数据
    	List<UserEntry> userList=userService.getTeacherEntryBySchoolId(schoolId, new BasicDBObject("nm",1));
    	for(UserEntry ue:userList)
    	{
    		teacherSet.add( NameShowUtils.showName(ue.getUserName()));
    	}
    	
    	for(ExcelTeacherRecord er:excelTeacherRecords)
    	{
    		if(er.getPostionDesc().length()>15)
    		{
    			builder.append(er.getTeacherName()).append("职位描述  [").append(er.getPostionDesc()).append(" ] 大于15个字符").append(seg);
    		}
    		
    		if(teacherSet.contains(er.getTeacherName()))
    		{
    			builder.append("老师用户["+er.getTeacherName()+"] 已经存在").append(seg);
    		}
    		
    		for(GradeAndClass gac:er.getGradeAndClassList())
    		{
    			if(StringUtils.isNotBlank(gac.getSubjectName()) && (StringUtils.isBlank(gac.getClassName())  || StringUtils.isBlank(gac.getGradeName()) || 0==gac.getGradeCode()))
    			{
    				builder.append("老师用户["+er.getTeacherName()+"] 学科名字是 ").append(gac.getSubjectName()).append(",已经存在，建议补全年级代码 ，年级名称和 班级名称").append(seg);
    				break;
    			}
    			String ss=gac.getClassName()+gac.getGradeName()+gac.getGradeCode();
    			if(StringUtils.isNotBlank(ss)  && (StringUtils.isBlank(gac.getClassName())  || StringUtils.isBlank(gac.getGradeName()) || 0==gac.getGradeCode()))
    			{
    				builder.append("老师用户["+er.getTeacherName()+"]").append(" 建议补全年级代码 ，年级名称和 班级名称 ").append(seg);
    				break;
    			}
    		}
    	}
    	
    	//学生数据
    	
    	//学生名字->班级名字
    	Map<String,String> studentClassNameMap =new HashMap<String, String>();
    	//学生ID->班级名称
    	Map<ObjectId,String> studentClassMap =new HashMap<ObjectId, String>();
    	List<ClassEntry> clist=classService.findClassInfoBySchoolId(schoolId,new BasicDBObject("nm",1).append("stus", 1));
    	for(ClassEntry ce:clist)
    	{
    		String classStr=ce.getName().replace("(", "（").replace(")", "）");
    		for(ObjectId studId:ce.getStudents())
    		{
    			studentClassMap.put(studId, classStr);
    		}
    	}
    	Map<ObjectId, UserEntry> userMap=	userService.getUserEntryMap(studentClassMap.keySet(), new BasicDBObject("nm",1));
    	UserEntry ue=null;
    	String userName ="";
    	for(Map.Entry<ObjectId,String> entry:studentClassMap.entrySet())
    	{
    		ue=userMap.get(entry.getKey());
    		if(null!=ue)
    		{
    			userName=ue.getUserName();
    			studentClassNameMap.put(userName, entry.getValue());
    		}
    	}
    	
    	for(ExcelStudentRecord esr:studentRecords)
    	{
    		if(studentClassNameMap.containsKey(esr.getStudentName()))
    		{
    			String thisClassName=esr.getClassName().replace("(", "（").replace(")", "）");
    			if(thisClassName.equals(studentClassNameMap.get(esr.getStudentName())))
    			{
    				builder.append("学生用户[ "+esr.getStudentName()+" ] 已经存在于班级 "+thisClassName).append(seg);
    			}
    		}
    	}
    	return builder.toString();
    }
    /**
     * excel数据导入
     * @param file
     * @return
     * @throws Exception 
     */
    @RequestMapping("/import")
    public String importData(@RequestParam("file") MultipartFile file,    @RequestParam("logo") MultipartFile logo,                 Integer type,String schoolName, @ObjectIdType(isRequire=false) ObjectId regid,String pwd,@ObjectIdType(isRequire=false) ObjectId schoolId,String token,HttpServletRequest req) throws Exception {
    	
        String fileName = file.getOriginalFilename();
        
        if (!fileName.endsWith(".xls")) {
            throw new Exception("文件格式错误！");
        }
        
        String resToken=Resources.getProperty("token","123456");
        
        if(StringUtils.isBlank(token) || !resToken.equalsIgnoreCase(resToken))
        {
        	 throw new Exception("token error！");
        }
        
        if(null==regid && null==schoolId)
        {
        	 throw new Exception("Param error");
        }
        
        if(null!=regid && null!=schoolId)
        {
        	 throw new Exception("Param error");
        }
        
        importLog.info("\r\n");
        importLog.info("\r\n");
        importLog.info("\r\n");
        importLog.info("******************************************************************************************************");
        importLog.info("******************************************************************************************************");
        importLog.info("******************************************************************************************************");
        importLog.info("begin import data;"+DateTimeUtils.getChineseDate());
        importLog.info("type="+type);
        importLog.info("schoolName="+schoolName);
        importLog.info("regid="+regid);
        importLog.info("pwd="+pwd);
        importLog.info("session="+getSessionValue());
        
        //表示已经有该学校
        if(null!=schoolId)
        {
        	return importDataFromExcel(file,schoolId,logo,req);
        }
        //新增一个学校，并且导入师生
        return addSchoolAndImportDataFromExcel(file,logo,type,schoolName,regid,pwd,req);
    }
    
    
    /**
     * excel数据导入 
     * @param file
     * @return
     * @throws IllegalParamException
     * @throws IOException
     */
    public String importDataFromExcel( MultipartFile file,final ObjectId schoolId,MultipartFile logo,HttpServletRequest req) throws IllegalParamException, IOException {
	    final SchoolEntry schoolEntry= schoolService.getSchoolEntry(schoolId,Constant.FIELDS);
	    if(null==schoolEntry)
	    {
	        	throw new IllegalParamException("Can not find school for SchoolId:"+schoolId);
	    }
	    
	    if(null!=logo)
	    {
	        String path = req.getServletContext().getRealPath("/upload/logos/");
	        File targetFile = new File(path, schoolId.toString()+"."+FilenameUtils.getExtension(logo.getOriginalFilename()));
	        FileUtils.copyInputStreamToFile(logo.getInputStream(), targetFile);
	    	schoolService.update(schoolEntry.getID(), "logo", "/upload/logos/"+schoolId.toString()+"."+FilenameUtils.getExtension(logo.getOriginalFilename()));
	    }
        importLog.info("School["+schoolEntry.getName()+"] begin import data;");
        final Map<String,Object> resMap= ExcelService.findTeacherAndStudentRecord(file.getInputStream());
        taskStart(schoolEntry, resMap);
        return "myschool/success";
    }
    
    /**
     * 先增加一个学校然后倒入数据
     * @param file
     * @return
     * @throws IllegalParamException
     * @throws IOException
     */
    public String addSchoolAndImportDataFromExcel(@RequestParam("file") MultipartFile file,MultipartFile logo,Integer type,String schoolName,ObjectId regid,String pwd,HttpServletRequest req) throws IllegalParamException, IOException {
        final  SchoolEntry schoolEntry =new SchoolEntry(type, schoolName, regid, pwd);
        ObjectId schoolId= new ObjectId();
        schoolEntry.setID(schoolId);
        if(null!=logo)
        {
          schoolEntry.setLogo("/upload/logos/"+schoolId.toString()+"."+FilenameUtils.getExtension(logo.getOriginalFilename()));
        }
        String path = req.getServletContext().getRealPath("/upload/logos/");
        File targetFile = new File(path, schoolId.toString()+"."+FilenameUtils.getExtension(logo.getOriginalFilename()));
        FileUtils.copyInputStreamToFile(logo.getInputStream(), targetFile);
        schoolEntry.setModuleStr("1,2,3,4,5,7,8,9,10,11");
        schoolService.addSchoolEntry(schoolEntry);
        final Map<String,Object> resMap= ExcelService.findTeacherAndStudentRecord(file.getInputStream());
        taskStart(schoolEntry,resMap);
        return "myschool/success";
    }
    
    /**
     * 启动导数据线程
     * @param schoolEntry
     * @param resMap
     */
	private void taskStart(final SchoolEntry schoolEntry,final Map<String, Object> resMap)
    {
		Thread thread =new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					
					 List<ObjectId> uList =new ArrayList<ObjectId>();
					handleExcelMapData(resMap,schoolEntry,uList);
					importLog.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%result%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
					
					
					importLog.info("注册环信begin*************");
					
//					List<String> userlist = groupDiscussionService.selUserBySchoolid(schoolEntry.getID().toString());
//				    for (String chatid : userlist) {
//				                easeMobService.createNewUser(chatid);
//				     }
				     
				    importLog.info("注册环信end*************"); 
				    importLog.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%result%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
				} catch (IllegalParamException e) {
					importLog.error("", e);
				}
			}
		});
        thread.start();
	}
	
	
	 
    /**
     * 处理excel导入的数据
     * @param resMap
     * @throws IllegalParamException
     */
    public void handleExcelMapData(final Map<String, Object> resMap,SchoolEntry schoolEntry, final List<ObjectId> uList)
			throws IllegalParamException {
		@SuppressWarnings("unchecked")
		List<ExcelTeacherRecord> excelTeacherRecords=(List<ExcelTeacherRecord>)resMap.get("teacherList");
        @SuppressWarnings("unchecked")
		List<ExcelStudentRecord> studentRecords=(List<ExcelStudentRecord>)resMap.get("studentList");
        final List<ClassEntry> classList= classService.findClassInfoBySchoolId(schoolEntry.getID(),Constant.FIELDS);
        
        final List<Grade> grads=  schoolEntry.getGradeList();
        
        final List<Subject> subjects =schoolEntry.getSubjects();
        
        if(null!=excelTeacherRecords && !excelTeacherRecords.isEmpty())
        {
          importLog.info("Parse teacher count:"+excelTeacherRecords.size());
          
          for(ExcelTeacherRecord excelTeacherRecord:excelTeacherRecords)
          {
        	  try
        	  {
        	     handleTeacher(excelTeacherRecord,schoolEntry,classList,grads,subjects,uList);
        	     Thread.sleep(2);
        	  }catch(Exception ex)
        	  {
        		  importLog.error("", ex);
        	  }
          }
        }
        
        if(null!=studentRecords && !studentRecords.isEmpty())
        {
        	importLog.info("Parse student count:"+studentRecords.size());
          
          for(ExcelStudentRecord ExcelStudentRecord:studentRecords)
          {
        	  try
        	  {
        	    handleStudent(ExcelStudentRecord,schoolEntry,classList,grads,uList);
        	    Thread.sleep(2);
        	  }catch(Exception ex)
        	  {
        		  importLog.error("", ex);
        	  }
          }
        }
	}

    private void handleTeacher( ExcelTeacherRecord excelTeacherRecord,SchoolEntry schoolEntry,List<ClassEntry> classList,final List<Grade> grads,List<Subject> subjects,final List<ObjectId> uList) throws Exception
    {
    	
      String name= getRightUserName(excelTeacherRecord.getTeacherName());

      excelTeacherRecord.setTeacherName(name);
      
  	  //处理用户
  	  UserEntry e=new UserEntry(excelTeacherRecord.getTeacherName(), MD5Utils.getMD5String(schoolEntry.getInitialPassword()), excelTeacherRecord.getSex(), null,excelTeacherRecord.getPostionDesc());
		e.setLetter(PinYin2Abbreviation.cn2py(excelTeacherRecord.getTeacherName()));
  	  e.setRole(excelTeacherRecord.getRole());
  	  
  	  if(excelTeacherRecord.getGradeAndClassList().size()>0 && excelTeacherRecord.getRole()==UserRole.HEADMASTER.getRole() )
  	  {
  		  e.setRole((UserRole.HEADMASTER.getRole() | UserRole.TEACHER.getRole() ));
  	  }
  	  
  	  e.setSchoolID(schoolEntry.getID());
  	  
  	  e.setAvatar("head-" + "default-head.jpg");
  	  e.setNickName(name);
  	  ObjectId insert= userService.addUser(e);
  	  createEaseMob(e);
  	  importLog.info("add teacher user:"+e);
  	  
  	  uList.add(insert);
  	  //处理年级
  	  for(GradeAndClass gac:excelTeacherRecord.getGradeAndClassList())
  	  {
  		importLog.info(gac);
  		  ObjectId gradeId=null;
  		  ClassEntry ce=null;
  		  Subject subject=null;
  		  //处理年级
  		  if(StringUtils.isNotBlank(gac.getGradeName()))
  		  {
	  		  Grade gr=isContainGrade(grads,gac.getGradeCode());
	  		  if(null!=gr)
	  		  {
	  			importLog.info("		Find grade:"+gr);
	  			  if(gac.isLeaderGrade()) //是年级组长
	  			  {
	  				  schoolService.addGradeLeader(schoolEntry.getID(), gr.getGradeId(), insert);
	  			  }
	  		  }
	  		  else
	  		  {
	  			   gr =new Grade(gac.getGradeName(), gac.getGradeCode(), gac.isLeaderGrade()?insert:null,new ObjectId());
	  			   schoolService.addGrade(schoolEntry.getID().toString(), gr);
	  			   grads.add(gr);
	  			 importLog.info("		Add grade"+gr);
	  		  }
	  		 gradeId=gr.getGradeId();
  		  }
  		 
  		  //处理班级
  		 
  		  if(StringUtils.isNotBlank(gac.getClassName()) && null!=gradeId)
  		  {
	  		  ce=isContainClass(classList,gac.getClassName(),gradeId);
	  		  
	  		  if(null!=ce)
	  		  {
	  			importLog.info("Find ClassEntry:"+ce);
	  			  classService.addTeacher(ce.getID(), insert);
	  		  }
	  		  else
	  		  {
	  			   ce =new ClassEntry(schoolEntry.getID(), gradeId, gac.getClassName(), gac.isMaster()?insert:null, 0);
	  			   List<ObjectId> teachers=new ArrayList<ObjectId>();
	  			   teachers.add(insert);
	  			   ce.setTeachers(teachers);
	  			   importLog.info("Add ClassEntry:"+ce);
	  			   classService.addClassEntry(ce);
	  			   classList.add(ce);
	  		  }
	  		  
	  		  if(gac.isMaster())
 			  {
 				  classService.updateMaster(ce.getID(), insert);
 			  }
  		  }
  		  
  		  //处理科目
  		  if(StringUtils.isNotBlank(gac.getSubjectName()))
  		  {
	  		  subject=  isContaniSubject(subjects,gac.getSubjectName());
	  		  if(null==subject) //不包含
	  		  {
	  			  subject=new Subject(gac.getSubjectName(), gradeId);
	  			  schoolService.addSubject2School(schoolEntry.getID(), subject);
	  			  subjects.add(subject);
	  			  importLog.info("Add Subject:"+subject);
	  		  }
	  		  else
	  		  {
	  			importLog.info("Find Subject:"+subject);
	  		  }
	  		  List<ObjectId>  gids= subject.getGradeIds();
  			  if(!gids.contains(gradeId))
  			  {
  				  gids.add(gradeId);
  				  schoolService.updateGradeForSubject(schoolEntry.getID(), subject.getSubjectId(), gids);
  				  subject.setGradeIds(gids);;
  			  }
  		  }
  		  if(null!=ce && null!=subject)
  		  {
	  		  IdValuePair classPair =new IdValuePair(ce.getID(), ce.getName());
	  		  IdValuePair subjectPair =new IdValuePair(subject.getSubjectId(), subject.getName());
	  		  TeacherClassSubjectEntry teacherClassSubjectEntry  =new TeacherClassSubjectEntry(insert, classPair, subjectPair);
	  		  ObjectId tcsid=  teacherClassSubjectService.addTeacherClassSubjectEntry(teacherClassSubjectEntry);
	  		  
	  		  importLog.info("Add TeacherClassSubjectEntry:"+teacherClassSubjectEntry);
  		  }
  	  }
    }

    private void handleStudent( ExcelStudentRecord ExcelStudentRecord,SchoolEntry schoolEntry,List<ClassEntry> classList,final List<Grade> grads,final List<ObjectId> uList) throws Exception
    {
 
      String name= getRightUserName(ExcelStudentRecord.getStudentName());
     
      ExcelStudentRecord.setStudentName(name);
     
      ObjectId fatherId=new ObjectId();
      ObjectId matherId=new ObjectId();
      ObjectId studentId=new ObjectId();
      //处理用户
  	  UserEntry e=new UserEntry(ExcelStudentRecord.getStudentName(), MD5Utils.getMD5String(schoolEntry.getInitialPassword()), ExcelStudentRecord.getSex(), null);
	  e.setLetter(PinYin2Abbreviation.cn2py(ExcelStudentRecord.getStudentName()));
  	  e.setRole(UserRole.STUDENT.getRole());
  	  e.setSchoolID(schoolEntry.getID());
  	  e.setID(studentId);
  	  if(StringUtils.isNotBlank(ExcelStudentRecord.getStudyNumber()))
  	  {
  		  e.setStudyNum(ExcelStudentRecord.getStudyNumber());
  	  }
  	  List<ObjectId> parentIds =new ArrayList<ObjectId>();
  	  parentIds.add(fatherId);
  	  parentIds.add(matherId);
  	  
  	  e.setConnectIds(parentIds);
  	  if(StringUtils.isNotBlank(ExcelStudentRecord.getXuejiNumber()))
  	  {
  		  e.setRegisterNum(ExcelStudentRecord.getXuejiNumber());
  	  }
  	  e.setAvatar("head-" + "default-head.jpg");
  	  e.setNickName(ExcelStudentRecord.getStudentName());
  	 
  	  ObjectId insert= userService.addUser(e);
  	  createEaseMob(e);
  	  uList.add(insert);
  	  importLog.info("add student user:"+e);
  	  //爸爸
  	  List<ObjectId> sunIds =new ArrayList<ObjectId>();
  	  sunIds.add(studentId);
	  
  	  UserEntry pe=new UserEntry(ExcelStudentRecord.getStudentName()+"爸爸", MD5Utils.getMD5String(schoolEntry.getInitialPassword()), Constant.ONE, null);
		pe.setLetter(PinYin2Abbreviation.cn2py(pe.getUserName()));
		pe.setRole(UserRole.PARENT.getRole());
  	  pe.setSchoolID(schoolEntry.getID());
  	  pe.setID(fatherId);
  	  pe.setConnectIds(sunIds);
  	  pe.setAvatar("head-" + "default-head.jpg");
  	  pe.setNickName(ExcelStudentRecord.getStudentName()+"爸爸");
  	  userService.addUser(pe);
  	createEaseMob(pe);
  	  importLog.info("add student father user:"+pe);
  	   
  	   
  	   
  	   
  	   //妈妈
  	  pe=new UserEntry(ExcelStudentRecord.getStudentName()+"妈妈", MD5Utils.getMD5String(schoolEntry.getInitialPassword()), Constant.ZERO, null);
		pe.setLetter(PinYin2Abbreviation.cn2py(pe.getUserName()));
		pe.setRole(UserRole.PARENT.getRole());
 	  pe.setSchoolID(schoolEntry.getID());
 	  pe.setID(matherId);
 	  pe.setConnectIds(sunIds);
 	  pe.setAvatar("head-" + "default-head.jpg");
 	  pe.setNickName(ExcelStudentRecord.getStudentName()+"妈妈");
 	  userService.addUser(pe);
 	 createEaseMob(pe);
 	  importLog.info("add student mather user:"+pe);
  	   
  	   
  	  ClassEntry classEntry= isContainClass(classList, grads, ExcelStudentRecord.getGradeCode(), ExcelStudentRecord.getClassName());
  	  if(null==classEntry)
  	  {
  		  importLog.info("Can not find ClassEntry");
  		  return;
  	  }
	  	classService.addStudent(classEntry.getID(), insert);
    }
    

    
    private void createEaseMob(UserEntry ue)
    {
    	try
    	{
    	 easeMobService.createNewUser(ue.getChatId());
    	}catch(Exception ex)
    	{
    		
    	}
    }
    
    
    
    /**
     * 某个学校是否包含某一个年级
     * @param grads
     * @return
     */
    public Grade isContainGrade( List<Grade> grads,int  gradeCode)
    {
    	 for(Grade gr:grads)
    	 {
    		 if(gr.getGradeType()==gradeCode)
    		 {
    			 return gr;
    		 }
    	 }
    	 return null;
    }
    
    
    /**
     * 是否包含某个班级
     * @param classList
     * @param className
     * @return
     */
    private ClassEntry isContainClass(List<ClassEntry> classList,String className,ObjectId gradeId)
    {
    	if(null==classList || classList.isEmpty())
    		return null;
    	for(ClassEntry e:classList)
    	{
    		if(e.getName().equals(className) && e.getGradeId().equals(gradeId))
    			return e;
    	}
    	return null;
    }
    
    
    /**
     * 是否包含某个班级
     * @param classList
     * @param className
     * @return
     */
    private ClassEntry isContainClass(List<ClassEntry> classList, List<Grade> grads,int gradeCode,String className)
    {
    	if(null==classList || classList.isEmpty())
    		return null;
    	
    	ObjectId gradeId=null;
    	for(Grade gr:grads)
    	{
    		if(gr.getGradeType()==gradeCode)
    		{
    			gradeId=gr.getGradeId();
    			break;
    		}
    	}
    	if(null!=gradeId)
    	{
    	
	    	for(ClassEntry e:classList)
	    	{
	    		if(e.getName().equals(className) && e.getGradeId().equals(gradeId))
	    			return e;
	    	}
    	}
    	return null;
    }
    
    /**
     * 某个学校是否包含某一个学科
     * @param subject
     * @return
     */
    private Subject isContaniSubject( List<Subject> subjects,String subject)
    {
    	if(subjects.isEmpty())
    		return null;
    	for(Subject sub:subjects)
    	{
    		if(sub.getName().equals(subject))
    		{
    			return sub;
    		}
    	}
    	return null;
    }
    
    
    private String getRightUserName(String name) throws Exception
    {
    	UserEntry e=userService.searchUserByUserName(name);
    	if(e==null)
    	{
    		return name;
    	}
    	
    	for(int i=1;i<Integer.MAX_VALUE;i++)
    	{
    		if(i== 2 || i== 44 || i== 24 || i== 34  || i== 54 || i== 64  || i== 74 || i== 84  || i== 94 ||  i==4 || i==13 || i==14 || i==38 || i==250)
    		{
    			continue;
    		}
    		String newName=name+String.valueOf(i);
    		
    		if(name.indexOf("4")>=0)
    		{
    			continue;
    		}
    		
    		e=userService.searchUserByUserName(newName);
    		if(e==null)
        	{
        		return newName;
        	}
    		Thread.sleep(2);
    	}
    	return null;
    }

    /**
     * 批量导入人员
     * @return
     */
    @RequestMapping("batchExportPersonPage")
    public String batchExportPersonPage()
    {
        return "myschool/batchExportPersonPage";
    }

    @RequestMapping("downloadPersonExcelModel")
    @ResponseBody
    public void downloadPersonExcelModel(HttpServletRequest request,HttpServletResponse response)
            throws IOException {
        try {
            String path="/WEB-INF/download/师生基本信息模板.xls";
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis=request.getServletContext().getResourceAsStream(path);

            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition","attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/force-download");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * excel数据导入
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping("/batchExportPerson")
    @ResponseBody
    public boolean batchExportPerson(@RequestParam("file") MultipartFile file,HttpServletRequest req) throws Exception {
        ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
        String fileName = file.getOriginalFilename().toLowerCase();
        if (!fileName.endsWith(".xls")&&!fileName.endsWith(".xlsx")) {
            throw new Exception("文件格式错误！");
            //return false;
        }
        String token=Resources.getProperty("token");

        if(!token.equalsIgnoreCase(token))
        {
            throw new Exception("token error！");
            //return false;
        }
        importDataFromExcel(file, schoolId,null,req);
        return true;
    }

}
