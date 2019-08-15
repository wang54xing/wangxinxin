package com.pojo.school;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 导航模块DTO
 * @author fourer
 *
 */
public class SchoolNavigationModuleDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7102373004349313195L;
	
	
	private SchoolNavigationDTO dto;
	private List<SchoolNavigationDTO> list=new ArrayList<SchoolNavigationModuleDTO.SchoolNavigationDTO>();

	
	public SchoolNavigationModuleDTO(SchoolNavigationDTO dto,
			List<SchoolNavigationDTO> list) {
		super();
		this.dto = dto;
		this.list = list;
	}
	public SchoolNavigationDTO getDto() {
		return dto;
	}
	public void setDto(SchoolNavigationDTO dto) {
		this.dto = dto;
	}
	public List<SchoolNavigationDTO> getList() {
		return list;
	}
	public void setList(List<SchoolNavigationDTO> list) {
		this.list = list;
	}
	

	public static class SchoolNavigationDTO implements Comparable<SchoolNavigationDTO>,Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -8372669864952762199L;
		
		private int type;
		private String id;
		private String name;
		private String cssName;
		private int sort;
		private int modleId;
		private String link;
		private String image;
		
		public SchoolNavigationDTO (){}
		
		public SchoolNavigationDTO (SchoolNavigationEntry e)
		{
			this.type=e.getType();
			this.id=e.getId();
			this.name=e.getName();
			this.cssName=e.getCssClassName();
			this.sort=e.getSort();
			this.modleId=e.getModleId();
			this.image=e.getImage();
			if(e.getList().size()==1)
			{
				this.link=e.getList().get(0).getLink();
			}
		}
		
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		public String getCssName() {
			return cssName;
		}
		public void setCssName(String cssName) {
			this.cssName = cssName;
		}
		public int getSort() {
			return sort;
		}
		public void setSort(int sort) {
			this.sort = sort;
		}
		
		
		public int getModleId() {
			return modleId;
		}

		public void setModleId(int modleId) {
			this.modleId = modleId;
		}
		
		

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SchoolNavigationDTO other = (SchoolNavigationDTO) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

		@Override
		public int compareTo(SchoolNavigationDTO o) {
			 int i0= this.getModleId()*100+this.getSort();
             if(this.getType()==1)
             {
             	 i0= this.getModleId()*100;
             }
             int i1= o.getModleId()*100+o.getSort();
             if(o.getType()==1)
             {
             	 i1= o.getModleId()*100;
             }
             return i0-i1;
		}

		@Override
		public String toString() {
			return "SchoolNavigationDTO [type=" + type + ", id=" + id
					+ ", name=" + name + ", cssName=" + cssName + ", sort="
					+ sort + ", modleId=" + modleId + ", link=" + link
					+ ", image=" + image + "]";
		}
		
		
		
	}


	@Override
	public String toString() {
		return "SchoolNavigationModuleDTO [dto=" + dto + ", list=" + list + "]";
	}



}
