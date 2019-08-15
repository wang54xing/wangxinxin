<%@page import="com.pojo.school.SchoolNavigationModuleDTO.SchoolNavigationDTO"%>
<%@page import="com.pojo.school.SchoolNavigationModuleDTO"%>
<%@page import="java.util.List"%>
<%@page import="org.bson.types.ObjectId"%>
<%@page import="com.fulaan.school.service.SchoolNavigationService"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

   <ul class="left-nav">
   
   
   
   <%
	    SchoolNavigationService s=new SchoolNavigationService();
	    List<SchoolNavigationModuleDTO> dtos=s.loadNavs(new ObjectId(), 2);
	    for(int i=0;i<dtos.size();i++)
	    {
	    	SchoolNavigationModuleDTO mDTO=dtos.get(i);
	    	SchoolNavigationDTO dto=mDTO.getDto();
	    	List<SchoolNavigationDTO> list=mDTO.getList();
	    	
	    	if(null!=dto)
	    	{
	    		%>
	    		 <li class="er" id="<%= dto.getId() %>" >
	                <img src="/static_new/images/navv_left_2.png">
	                <span><%=dto.getName() %></span>
	                <s class="iconfont">&#xe61c;</s>
	             </li>
	    		<%
	    		
	    		if(null!=list && list.size()>0)
	    		{
	    			
	    			%>
	    			<dl>
                     <%
                     for(int j=0;j<list.size();j++)
                     {
                    	 SchoolNavigationDTO jDTo=list.get(j);
                    	 %>
                    	 <a href="<%=jDTo.getLink() %>" id="<%=jDTo.getId() %>"><dt><%=jDTo.getName() %></dt></a>
                    	 <%
                     }
                     %>
                    </dl>
	    			<%
	    		}
	    	}
	    }
   %>
  </ul>