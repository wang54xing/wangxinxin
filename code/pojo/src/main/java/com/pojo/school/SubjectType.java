package com.pojo.school;

import java.util.ArrayList;
import java.util.List;

/**
 * 科目信息
 * @author fourer
 *
 */
public enum SubjectType {

	CHINESE(1,"语文",SchoolType.PRIMARY.getType() | SchoolType.JUNIOR.getType() | SchoolType.SENIOR.getType() ),
	MATH(2,"数学",SchoolType.PRIMARY.getType() | SchoolType.JUNIOR.getType() | SchoolType.SENIOR.getType() ),
	ENGLISH(3,"英语",SchoolType.PRIMARY.getType() | SchoolType.JUNIOR.getType() | SchoolType.SENIOR.getType() ),
	PHYSICS(4,"物理", SchoolType.JUNIOR.getType() | SchoolType.SENIOR.getType() ),
	CHEMISTRY(5,"化学", SchoolType.JUNIOR.getType() | SchoolType.SENIOR.getType()),
	BIOLOGY(6,"生物",  SchoolType.JUNIOR.getType() | SchoolType.SENIOR.getType() ),
	GEOGRAPHY(7,"地理",SchoolType.SENIOR.getType() | SchoolType.JUNIOR.getType() ),
	HISTORY(8,"历史",SchoolType.SENIOR.getType() | SchoolType.JUNIOR.getType()),
	POLITICS(9,"政治",SchoolType.SENIOR.getType() | SchoolType.JUNIOR.getType()),
	SPEECH(10,"名人演讲",SchoolType.UNIVERSITY.getType()),
	MUSIC_SPORT_ART(11,"音体美",SchoolType.PRIMARY.getType()),
	SCIENCE(12,"科学",SchoolType.PRIMARY.getType() ),
	OLYMPIAD_MATH(13,"奥数",SchoolType.PRIMARY.getType() ),
	OVERSEA_ENGLISH(14,"课外英语",SchoolType.PRIMARY.getType()),
	OVERSEA_STUDY_RECOMMEND(15,"海外学习推荐",SchoolType.PRIMARY.getType() | SchoolType.JUNIOR.getType() | SchoolType.SENIOR.getType()),
	COMPUTER(16,"计算机",SchoolType.PRIMARY.getType() | SchoolType.JUNIOR.getType() | SchoolType.SENIOR.getType()),
    UNIVERSITY_STYLE(17,"大学风采",SchoolType.SENIOR.getType()),
    GOOD_LESSON(18,"精品微课",SchoolType.PRIMARY.getType() | SchoolType.JUNIOR.getType() | SchoolType.SENIOR.getType() ),
	OTHERS(0,"其他",0),
	UNINIT(-1, "未初始化",0),
	;
	
	private int id;
	private String name;
	private int type; //与SchoolType.type保持一致
	
	
	private SubjectType(int id, String name, int type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	

	public static List<SubjectType> getSubjectTypes(int schoolType)
	{
		List<SubjectType> list =new ArrayList<SubjectType>();
		for(SubjectType sjt:SubjectType.values())
		{
			if((sjt.getType() & schoolType)==schoolType)
			{
				list.add(sjt);
			}
		}
		return list;
	}
	
	public static SubjectType getSubjectType(int id)
	{
		for(SubjectType sjt:SubjectType.values())
		{
			if(sjt.getId()==id)
			{
				return (sjt);
			}
		}
		
		return null;
	}
	
	public static SubjectType getSubjectType(String name)
	{
		for(SubjectType sjt:SubjectType.values())
		{
			if(sjt.getName().equals(name))
			{
				return (sjt);
			}
		}
		
		return null;
	}
	
	
	/**
	 * 返回是不是普通课程
	 * 语文，数学，外语，历史，地理，生物，物理，化学，政治 是普通课程
	 * @param name
	 * @return
	 */
	public static Boolean isCommonSubject(String name)
	{
		SubjectType ty=getSubjectType(name);
		if(null!=ty && ty.getId()>=1 && ty.getId()<=9)
		{
			return true;
		}
		return false;
	}
}