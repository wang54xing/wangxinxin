package com.pojo.school;

import java.util.ArrayList;
import java.util.List;

import com.pojo.app.IdValuePairDTO;

/**
 * 部门详情
 * @author fourer
 *
 */
public class DepartmentDTO {

	private IdValuePairDTO dep;
	private List<IdValuePairDTO> member=new ArrayList<IdValuePairDTO>();
	
	public IdValuePairDTO getDep() {
		return dep;
	}
	public void setDep(IdValuePairDTO dep) {
		this.dep = dep;
	}
	public List<IdValuePairDTO> getMember() {
		return member;
	}
	public void setMember(List<IdValuePairDTO> member) {
		this.member = member;
	}
	
	
}
