/**
 * 
 */
 var operId;
 var pageTag=0; // 0 月历   1列表 2周历
 var typeValue; // type 1通知 2日程 3课程
 
 
  $(document).ready(function(){
	jQuery("#menu0").css("background-color","#6e6e6e");  
    $.ajax({
	  url: '/calendar/classinfos.do',
	  type: 'post',
	  contentType: 'application/json',
	  success: function (res)
	  {
	   for(var i=0;i<res.length;i++)
	   {
	    var html="<option value='"+res[i].id+"'>"+res[i].classname+"</option>";
	    jQuery(".cla_select").append(html);
	   }

	  }
	});
  });
    
 function clear()
 {
	 jQuery("#cla_begin_time,#cla_end_time,#cla_name,#event_name,#cla_begin_time_event,#cla_end_time_event").val("");
	 jQuery("#cla_share,#cla_rt").val(0);
 }
 function addClass()
 {
   operId="";
   clear();
   jQuery("#classOPer").find(".popup_text_II_friest").text("新建课程");
   jQuery("#classOPer").show();
 }
 
 
 function addSchedule()
 {
   operId="";		 
   clear();
   jQuery("#eventOper").find(".popup_text_II_friest").text("新建事项");
   jQuery("#eventOper").show();
 }
 
 
 
 function editClass(id,obj)
 {
   clear();
   jQuery("#classOPer").find(".popup_text_II_friest").text("编辑课程");
   operId=id;
   typeValue=3;
   getInfos();
   jQuery("#classOPer").show();
 }
 
 
 function editSchedule(id,obj)
 {
   clear();
   jQuery("#eventOper").find(".popup_text_II_friest").text("编辑事项");
   operId=id;
   typeValue=2;
   jQuery("#event_name").val(getShowText(id));
   getInfos();
   jQuery("#eventOper").show();
 }
 
 
 
 function getShowText(id)
 {
	  var message=jQuery.trim(jQuery("#m_"+id).html());
	  return message;
 }
 
 function hide(id)
 {
   jQuery("#"+id).hide();
 }
 
 function addClassOper()
 {
	 
  var rt=jQuery("#cla_rt").val();
  var name=jQuery("#cla_name").val();
  if(!name || name.length>20)
  {
   alert("课程名字为空或者长度超过20字");
   return;
  }
  
  var beginLong;
  var endLong=-1;
  var beginTime;
   try
   {
	    if(!jQuery("#cla_begin_time").val())
	    {
	    	 alert("请正确选择开始时间");
	    	 return;
	    }
         var beginTimeStr=jQuery("#cla_begin_time").val()+":00";
         beginTimeStr=beginTimeStr.replace(/-/g,"/");
         beginTime=new Date(beginTimeStr);
         beginLong=beginTime.getTime();
   }catch(x)
   {
        alert("请正确选择时间");
        return;
   }
 
   try
   {
	   
	   if(rt>0  )
	   {
		   if(jQuery("#cla_end_time").val())
			{
		          var endTimeStr=jQuery("#cla_end_time").val()+":00";
		          endTimeStr=endTimeStr.replace(/-/g,"/");
		          var endTime=new Date(endTimeStr);
		                              
		          if(isNaN(beginTime.getTime()) || isNaN(endTime.getTime()))
		          {
		              alert("请正确输入时间");
		              return;
		          }
		          if(endTime.getTime()<beginTime.getTime())
		          {
		           alert("结束时间早于开始时间,请修改");
		           return;
		          }
		          endLong=endTime.getTime();
			}
		   else
			{
			   alert("请正确选择结束时间");
		        return;
			}
	   }
   }catch(x)
   {
        alert("时间选择有误！");
        return;
   }
   
   var share =jQuery("#cla_share").val();
   
    var data={
                "type":"3",
                "beginTime":beginLong,
                "endTime":endLong,
                "cla":share,
                "rt":rt,
                "message":name,
                "id":operId
           };
     try
     {
     addCal(3,data);
     }catch(x){}     
 }
 
 /**
  * 添加一个日程
  */
 function addEventOper()
 {
  var rt=jQuery("#cla_rt_event").val();
  var name=jQuery("#event_name").val();
  if(!name || name.length>100)
  {
   alert("事项名字为空或者长度超过100字");
   return;
  }
  
  
  
  var beginLong;
  var endLong=-1;
  var beginTime;
   try
   {
	    if(!jQuery("#cla_begin_time_event").val())
	    {
	    	 alert("请正确选择开始时间");
	    	 return;
	    }
         var beginTimeStr=jQuery("#cla_begin_time_event").val()+":00";
         beginTimeStr=beginTimeStr.replace(/-/g,"/");
         beginTime=new Date(beginTimeStr);
         beginLong=beginTime.getTime();
   }catch(x)
   {
        alert("请正确选择时间");
        return;
   }
 
   try
   {
	   
	   if(rt>0  )
	   {
		   if(jQuery("#cla_end_time_event").val())
			{
		          var endTimeStr=jQuery("#cla_end_time_event").val()+":00";
		          endTimeStr=endTimeStr.replace(/-/g,"/");
		          var endTime=new Date(endTimeStr);
		                              
		          if(isNaN(beginTime.getTime()) || isNaN(endTime.getTime()))
		          {
		              alert("请正确输入时间");
		              return;
		          }
		          if(endTime.getTime()<beginTime.getTime())
		          {
		           alert("结束时间早于开始时间,请修改");
		           return;
		          }
		          endLong=endTime.getTime();
			}
		   else
			{
			   alert("请正确选择结束时间");
		        return;
			}
	   }
   }catch(x)
   {
        alert("时间选择有误！");
        return;
   }
   
   var share =jQuery("#cla_share_event").val();
    var data={
                "type":"2",
                "beginTime":beginLong,
                "endTime":endLong,
                "cla":share,
                "rt":rt,
                "message":name,
                "id":operId
           };
     try
     {
     addCal(2,data);
     }catch(x){alert(x)}     
 }
 
 function addCal(type,data)
 {
    $.ajax({
				                url: '/calendar/event.do',
				                type: 'POST',
				                contentType: 'application/json',
				                data: data,
				                contentType:"application/x-www-form-urlencoded; charset=utf-8",
				                success: function (res) 
				                {
				                   if(res.code=="200")
				                   {
				                	        if(pageTag==0)
				                	        {
				                    	     addMonth(0);
				                	        }
				                	         if(pageTag==1 || pageTag==2)
				                	         {
				                	        	 addWeek(0);
				                	        	 selectEvent();
				                	         }
				                             clear();
				                             jQuery("#classOPer,#eventOper").hide();
				                   }else
				                   {
				                    alert(res.message);
				                   }
				                }
				            });
 }
 
 
 function getInfos()
 {
	    $.ajax({
            url: '/calendar/detail.do',
            type: 'POST',
            contentType: 'application/json',
            data: {"id":operId},
            contentType:"application/x-www-form-urlencoded; charset=utf-8",
            success: function (res) 
            {
              
            	   if(res.code=="200")
            		{
            		   jQuery("#cla_begin_time,#cla_begin_time_event").val(res.message.beginTime);
            		   jQuery("#cla_name,#event_name").val(res.message.message);
            		   
            		   
            			 var deleteText=" 您确定要删除<span class='popup_SS_II'>\""+res.message.message+"\"</span>吗？";
            			 
            			 jQuery("#delete_title_span").html(deleteText);
            			 
            		}
            }
        });
 }
 
 
 
 
 function firstAddEvent(ty,showTime,message,operId,divCount)
 {
	 var html='<div>';
	 if(ty==3)
	 {
		 html+='<span class="calender_center_info_AN_IIII">[课程]</span>';
     }
	 if(ty==2)
	 {
		 html+='<span class="calender_center_info_AN_IIII">[事项]</span>';
     }
    
     html+='<div>'+showTime+'</div>';
     html+='<div class="calender_center_info_AN_V">'+message+'</div>';
     html+='</div>';
     
    
     if(divCount==0) //没有事件
      {
    	 jQuery("#"+operId +" > dl > dd").append(html);
      }
     else
      {
    	 jQuery("#"+operId +" > dl > dd").empty();
    	 html+='<div class="calender_center_info_AN_VI">今天还有<span>'+divCount+'</span>件事</div>';
    	 jQuery("#"+operId +" > dl > dd").prepend(html);
      }
    
 }
 
 /**
  * 
  * @param type 1通知 2日程 3课程
  * @param id
  */
 function deleteMod(type,id,obj)
 {
	 operId=id;
	 var typeText="删除通知";
	 if(type==2) typeText="删除日程";
	 if(type==3) typeText="删除课程";
	 jQuery("#delete_span").text(typeText);
	 getInfos();
	// var deleteText=" 您确定要删除<span class='popup_SS_II'>"+jQuery(obj).parents("li").find(".calender_Z_text_V").text()+"</span>吗？";
	 //jQuery("#delete_title_span").html(deleteText);
	 jQuery("#delete_div").show();
	 typeValue=type;
 }
 
 
 
 
 
 function deleteMod1(type,id,obj)
 {
	 operId=id;
	 var typeText="删除通知";
	 if(type==2) typeText="删除日程";
	 if(type==3) typeText="删除课程";
	 jQuery("#delete_span").text(typeText);
	 getInfos();
	 //var deleteText=" 您确定要删除<span class='popup_SS_II'>"+jQuery(obj).parents("li").find(".calender_center_info_TBT_V_NR").text()+"</span>吗？";
	 //jQuery("#delete_title_span").html(deleteText);
	// operId=id;
	 
	 jQuery("#delete_div").show();
	 typeValue=type;
 }
 
 
 
 
 function deleteOper()
 {
	 $.ajax({
         url: '/calendar/del.do',
         type: 'POST',
         contentType: 'application/json',
         data: {"id":operId,"type":typeValue},
         contentType:"application/x-www-form-urlencoded; charset=utf-8",
         success: function (res) 
         {
            if(res.code=="200")
            {
         	     //jQuery("#"+operId).remove();
            	 addWeek(0);
	        	 selectEvent();
         	     jQuery("#delete_div").hide();
            }else
            {
                 alert(res.message);
            }
         }
     });
 }
 
 
 
 function goPage(tag)
 {
	 if(tag!=pageTag)
     {
		 if(tag==0) location.href='/calendar/month';
		 if(tag==1) location.href='/calendar/list';
		 if(tag==2) location.href='/calendar/week';
	 }
 }
 
 
 
 function loginout(t) {
	    $.ajax({
	    	 url: "/user/logout.do",
	        type: "post",
	        dataType: "json",
	        data: {
	            'inJson': true
	        },
	        success: function (data) {
	        	window.location.href = "/";
	        }
	    });
	}
 