package com.pojo.school;

import com.sys.constants.Constant;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 年级
 * @author fourer
 *
 */
public enum GradeType {

	
	XIAO1(1, SchoolType.PRIMARY.getType(), "一年级",new Integer[]{SubjectType.OVERSEA_ENGLISH.getId(),SubjectType.OVERSEA_STUDY_RECOMMEND.getId(),SubjectType.COMPUTER.getId()}),
	XIAO2(2, SchoolType.PRIMARY.getType(), "二年级",new Integer[]{SubjectType.OVERSEA_ENGLISH.getId(),SubjectType.OVERSEA_STUDY_RECOMMEND.getId(),SubjectType.COMPUTER.getId()}),
	XIAO3(3, SchoolType.PRIMARY.getType(), "三年级",new Integer[]{SubjectType.OVERSEA_ENGLISH.getId(),SubjectType.OVERSEA_STUDY_RECOMMEND.getId(),SubjectType.COMPUTER.getId()}),
	XIAO4(4, SchoolType.PRIMARY.getType(), "四年级",new Integer[]{SubjectType.OVERSEA_ENGLISH.getId(),SubjectType.OVERSEA_STUDY_RECOMMEND.getId(),SubjectType.COMPUTER.getId()}),
	XIAO5(5, SchoolType.PRIMARY.getType(), "五年级",new Integer[]{SubjectType.OVERSEA_ENGLISH.getId(),SubjectType.OVERSEA_STUDY_RECOMMEND.getId(),SubjectType.COMPUTER.getId()}),
	XIAO6(6, SchoolType.PRIMARY.getType(), "六年级",new Integer[]{SubjectType.OVERSEA_ENGLISH.getId(),SubjectType.OVERSEA_STUDY_RECOMMEND.getId(),SubjectType.COMPUTER.getId()}),
	
	
	CHU1(7,SchoolType.JUNIOR.getType(),"初一",new Integer[]{SubjectType.OVERSEA_STUDY_RECOMMEND.getId(),SubjectType.COMPUTER.getId()}),
	CHU2(8,SchoolType.JUNIOR.getType(),"初二",new Integer[]{SubjectType.OVERSEA_STUDY_RECOMMEND.getId(),SubjectType.COMPUTER.getId()}),
	CHU3(9,SchoolType.JUNIOR.getType(),"初三",new Integer[]{SubjectType.OVERSEA_STUDY_RECOMMEND.getId(),SubjectType.COMPUTER.getId()}),
	CHU4(14,SchoolType.JUNIOR.getType(),"中考总复习",new Integer[]{SubjectType.OVERSEA_STUDY_RECOMMEND.getId(),SubjectType.COMPUTER.getId()}),

	
	GAO1(10,SchoolType.SENIOR.getType(),"高一",new Integer[]{SubjectType.OVERSEA_STUDY_RECOMMEND.getId(),SubjectType.COMPUTER.getId(),SubjectType.UNIVERSITY_STYLE.getId()}),
	GAO2(11,SchoolType.SENIOR.getType(),"高二",new Integer[]{SubjectType.OVERSEA_STUDY_RECOMMEND.getId(),SubjectType.COMPUTER.getId(),SubjectType.UNIVERSITY_STYLE.getId()}),
	GAO3(12,SchoolType.SENIOR.getType(),"高三",new Integer[]{SubjectType.OVERSEA_STUDY_RECOMMEND.getId(),SubjectType.COMPUTER.getId(),SubjectType.UNIVERSITY_STYLE.getId()}),
	GAO_FUXI(13,SchoolType.SENIOR.getType(),"高考总复习",new Integer[]{SubjectType.OVERSEA_STUDY_RECOMMEND.getId(),SubjectType.COMPUTER.getId()}),
	
	
	XIAO_GRADUATE(15, -1, "小学毕业班",new Integer[]{}),
	CHU_GRADUATE(16, -1, "初中毕业班",new Integer[]{}),
	GAO_GRADUATE(17, -1, "高中毕业班",new Integer[]{}),
	;

	private int id;
	private int schoolType;
	private String name;
	
	
	/**
	 * 本年级不包括哪些科目
	 */
	private Integer[] removeSubjectIds;

	private GradeType(int id, int schoolType, String name,Integer[] removeSubjectIds) {
		this.id = id;
		this.schoolType = schoolType;
		this.name = name;
		this.removeSubjectIds=removeSubjectIds;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(int schoolType) {
		this.schoolType = schoolType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Integer[] getRemoveSubjectIds() {
		return removeSubjectIds;
	}

	public void setRemoveSubjectIds(Integer[] removeSubjectIds) {
		this.removeSubjectIds = removeSubjectIds;
	}

	/**
	 * 通过学习类型和科目查找云课程年级
	 * @param schoolType
	 * @param subject 科目ID；不等于-1时生效
	 * @return
	 */
	public static List<GradeType> getGradeTypes(int schoolType, int subject)
	{
		List<GradeType> list =new ArrayList<GradeType>();
		for(GradeType sjt:GradeType.values())
		{
			if(sjt.getSchoolType()==schoolType)
			{
				
					if(null== sjt.getRemoveSubjectIds() ||sjt.getRemoveSubjectIds().length==0 || subject==Constant.NEGATIVE_ONE)
					{
						 list.add(sjt);
						 continue;
					}
					if(ArrayUtils.indexOf(sjt.getRemoveSubjectIds(), subject)<0)
					{
						 list.add(sjt);
					}
			}
		}
		return list;
	}
	
	/**
	 * 通过学习类型和科目查找云课程年级ID
	 * @param schoolType
	 * @param subject 科目ID；不等于-1时生效
	 * @return
	 */
	public static List<Integer> getGradeTypeIds(int schoolType, int subject)
	{
		List<Integer> list =new ArrayList<Integer>();
		List<GradeType> types= getGradeTypes(schoolType, subject);
		for(GradeType sjt:types)
		{
			list.add(sjt.getId());
		}
		return list;
	}
	
	
	
	public static GradeType getGradeType(int id)
	{
		for(GradeType sjt:GradeType.values())
		{
			if(sjt.getId()==id)
			{
			  return sjt;
			}
		}
		return null;
	}

    /**
     * 根据schoolType得到GradeType
     * @param schoolType
     * @return
     */
    public static  List<GradeType> getGradeTypeBySchoolType(int schoolType)
    {
        List<GradeType> retList =new ArrayList<GradeType>();

        for(GradeType sjt:GradeType.values())
        {
            if((schoolType&sjt.getSchoolType())==sjt.getSchoolType()&&sjt.getId()<13)
            {
                retList.add(sjt);
            }
        }
        return retList;
    }
	

}
