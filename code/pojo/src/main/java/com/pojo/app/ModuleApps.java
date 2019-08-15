package com.pojo.app;

/**
 * 模块应该
 * @author fourer
 *
 */
public enum ModuleApps {

	userlogin(0,"用户登录","/user/login"),
	
	extensionClass(1,"拓展课管理","/extensionClass"),
	notice(2,"通知","/mynotice"),
	askleave(3,"教师请假","/askleave"),

	
	
	microcourse(4,"微课资源","/microcourse"),
	teacherDictionary(5,"教材管理","/teacher/dictionary"),
	homework(6,"作业备忘录","/homework"),
	
	
	
	versionquestionnaire(7,"问卷调查","/versionquestionnaire"),
	vote(8,"投票选举","/vote"),
	homeSchool(9,"微校园","/homeSchool"),
	
	
	practiceMge(10,"在线练习/错题本","/practiceMge"),
	testpaper(11,"组卷","/testpaper"),
	analyse(12,"成绩分析","/analyse"),
	;
	
	
	private int index;
	private String name;
	private String beginUrl;
	
	
	private ModuleApps(int index, String name, String beginUrl) {
		this.index = index;
		this.name = name;
		this.beginUrl = beginUrl;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBeginUrl() {
		return beginUrl;
	}
	public void setBeginUrl(String beginUrl) {
		this.beginUrl = beginUrl;
	}
	
	
	
	
	public static ModuleApps getModuleApps(int type)
	{
		for(ModuleApps app:ModuleApps.values())
		{
			if(app.getIndex()==type)
			{
				return app;
			}
		}
		return null;
	}
	
}
