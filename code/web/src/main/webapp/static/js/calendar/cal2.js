/**
 * 
 */

var pageTag; //1 天历  2周历  3月历  4列表
var operId;
var eventClass={1:"calender_center_info_TBT_RL_yellow",2:"calender_center_info_TBT_RL_blue",3:"calender_center_info_TBT_RL_red"}
var isNotice=false;
var isl;
var origBeginTime;  //当前操作的时间
 function go2private(){
        window.location.href="/message";
    }
    function getPrivateLetterCount() {
        $.ajax({
            url: "/letter/count.do",
            type: "post",
            data: {
                'inJson': true
            },
            success: function (data) {
                if (data > 0) {
                    jQuery("#xyd").text(data);
                } else {
                    jQuery("#email").show();
                    jQuery("#letterSpan").hide();
                    jQuery("#yun").hide();
                }
            }
        });
    }
	
	
	
	Date.prototype.pattern=function(fmt) {         
    var o = {         
    "M+" : this.getMonth()+1, //月份         
    "d+" : this.getDate(), //日         
    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时         
    "H+" : this.getHours(), //小时         
    "m+" : this.getMinutes(), //分         
    "s+" : this.getSeconds(), //秒         
    "q+" : Math.floor((this.getMonth()+3)/3), //季度         
    "S" : this.getMilliseconds() //毫秒         
    };         
    var week = {         
    "0" : "/u65e5",         
    "1" : "/u4e00",         
    "2" : "/u4e8c",         
    "3" : "/u4e09",         
    "4" : "/u56db",         
    "5" : "/u4e94",         
    "6" : "/u516d"        
    };         
    if(/(y+)/.test(fmt)){         
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));         
    }         
    if(/(E+)/.test(fmt)){         
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[this.getDay()+""]);         
    }         
    for(var k in o){         
        if(new RegExp("("+ k +")").test(fmt)){         
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));         
        }         
    }         
    return fmt;         
}     
	
	
	
	 function goPage(tag)
	 {
			 if(tag==0) location.href='/calendar/day';
			 if(tag==1) location.href='/calendar/week';
			 if(tag==2) location.href='/calendar/month';
			 if(tag==3) location.href='/calendar/list';
	 }
	
	function select()
	{
		var type=jQuery("#typeSelect").val();
		
		var $objs;
		if(pageTag==1) $objs=jQuery(".calender_center_info_TBT_RL_IIII").find("div");
		if(pageTag==2) $objs=jQuery(".calender_center_info_ZL_ZZZ_I").find("div");
		if(pageTag==3) $objs=jQuery(".calender_center_info_YL_VII").find("div");
		if(pageTag==4) $objs=jQuery("#u0,#u1,#u2,#u3,#u4,#u5,#u6").find("li:gt(0)");
		
		if(type==0)
		{
			$objs.each(function(){
				jQuery(this).show();
			});
		}
		else
		{
			$objs.each(function(){
				var thisType=jQuery(this).attr("id").substr(0,1);
				if(thisType==type)
				{
					jQuery(this).show();
				}
				else
				{
					jQuery(this).hide();
				}
			});
		}
	
	}
	
	function clear()
	{
		jQuery("input,textarea").val("");
	}
	
	function initValue()
	{
		jQuery("#insert_kecheng").attr("value","1");
	    jQuery("#insert_richeng").attr("value","2");
	    jQuery("#dayInput1").attr("value","0");
	    jQuery("#dayInput2").attr("value","1");
	    jQuery("#dayInput3").attr("value","2");
	    jQuery("#dayRepeat1").attr("value","1");
	    jQuery("#dayRepeat2").attr("value","0");
	    jQuery("#otherRepeat1").attr("value","0");
	    jQuery("#otherRepeat2").attr("value","1");
	    jQuery("#otherRepeat3").attr("value","2");
		jQuery("#singleDelete").attr("value","0");
		jQuery("#singleDelete").attr("checked",true);
		jQuery("#multiDelete").attr("value","1");
	}
	
	
	
	function showInsert()
	{
		operId="";
		jQuery("#deleteBtn").hide();
		jQuery("#cancelBtn").show();
		initValue();
	    jQuery("#otherDIV,#dayDIV").hide();

	    jQuery("#insertSpan").text("新建");
	    jQuery(".popup_text_XJ_top").show();
		jQuery("#insertDIV").show();
//禁止鼠标向下滚动
		document.onmousewheel = function() {return false;}



		$('input:radio[name=I]')[0].checked = true
	}
	
	
	function hideAll()
	{
		clear();
		jQuery("#insertDIV,#editDIV,#deleteDIV").hide();
		document.onmousewheel = function() {return true;}

	}
	
	//String type,String title,String content,String beginTime,String endTime,
     // String lp,String dv,String edt,String edv
	
	
	function getTime(s)
	{
		try
		{
		  var str=s+":00";
		  str=str.replace(/-/g,"/");
	      var longValue=new Date(str).getTime();
	      return longValue; 
		}catch(x){
			
		} 
		return -1;
	}
	
	
	function getDateStr(t)
	{
		var tl=parseInt(t,10);//转为整形
		var date=new Date(tl);//正确
		var bt=date.pattern("yyyy-MM-dd HH:mm");
		return bt;
	}
	
	function showLp()
	{
		var value =jQuery("#lpSelect").val();
		if(value==-1)
		{
			jQuery("#dayDIV").hide();
			jQuery("#otherDIV").hide();
		}
		else if(value==1)
		{
			jQuery("#dayDIV").show();
			jQuery("#otherDIV").hide();
		}
		else
			{
			  jQuery("#dayDIV").hide();
			  jQuery("#otherDIV").show();
			}
	}
	
	
	function edit(obj)
	{
		
		var id=jQuery(obj).data("data");
		operId=id
		
		var cla=jQuery(obj).attr("class");
		if(cla =='calender_center_info_TBT_RL_red')
			{
			isNotice =true;
			}
		else
			{
			isNotice =false;
			}
		
		origBeginTime=jQuery(obj).data("origBeginTime");
		var operDate=new Date(origBeginTime);
		var nowBt=operDate.pattern("yyyy-MM-dd");
		jQuery("#real_day1,#real_day2").text(nowBt);
		
		
		var dateStr=jQuery(obj).data("dateStr");
		if(dateStr)
		{
			jQuery(".date_span").val(dateStr);
		}
		jQuery("#deleteBtn").show();
		jQuery("#cancelBtn").hide();
		detail();
	    jQuery("#insertSpan").text("编辑");
	  //  jQuery(".popup_text_XJ_top").hide(); 
		jQuery("#insertDIV").show();
	}
	
	function showDelete()
	{
		if(isNotice)
		{
		 alert("通知不可以删除");
		 return;
		}
		initValue();
		jQuery("#deleteDIV").show();
	}

	function delOper()
	{
	  del1();
	}
	

	function del()
	{
		if(isNotice)
			{
			 alert("通知不可以删除");
			 return;
			}
		if(operId)
		{
			 $.ajax({
	             url: '/calendar/del.do',
	             type: 'POST',
	             contentType: 'application/json',
	             data: {"operId":operId},
	             contentType:"application/x-www-form-urlencoded; charset=utf-8",
	             success: function (res) 
	             {
	                if(res.code=="200")
	                {
	                	flushData();
	             	    hideAll();
	                }else
	                {
	                 alert(res.message);
	                }
	             }
	         });
		}
	}
	
	
	function del1()
	{
		if(isNotice)
		{
		 alert("通知不可以删除");
		 return;
		}
		if(operId)
		{
			 var type=jQuery("input[name='VV']:checked").val();
			 var bt=jQuery.trim(jQuery("#real_day1").text());
			 $.ajax({
	             url: '/calendar/del1.do',
	             type: 'POST',
	             contentType: 'application/json',
	             data: {"operId":operId,"type":type,"bt":bt},
	             contentType:"application/x-www-form-urlencoded; charset=utf-8",
	             success: function (res) 
	             {
	                if(res.code=="200")
	                {
	                	flushData();
	             	    hideAll();
	                }else
	                {
	                 alert(res.message);
	                }
	             }
	         });
		}
	}
	
	
	function flushData()
	{
		 if(pageTag==1 )
 	     {
			 getEvent();
 	     }
		 if(pageTag==2 )
 	     {
			 addWeek(0);
 	     }
		 if(pageTag==3 )
 	     {
			 addMonth(0);
 	     }
		 if(pageTag==4 )
 	     {
			 addWeek(0);
 	     }
	}
	
	function detail()
	{
		if(operId)
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
	                	  initValue();
	                	  var event=res.message;
	                	  
	                	   isl=event.isl;
	                	   if(isl==0) //不是循环时间
	                		{
	                		   jQuery("#deleteSapn2").hide();
	                		}
	                	   else
	                		{
	                		   jQuery("#deleteSapn2").show();
	                		}
	             	       if(event.ty==1)
	             	       {
	             	    	   jQuery("#insert_kecheng").attr("value","1");
	             	    	   jQuery("#insert_richeng").attr("value","2");
	             	    	   $('input:radio[name=I]')[1].checked = true;
	             	    	   jQuery("#real_d_text").text("删除课程");
	             	       }
	             	      if(event.ty==2)
	             	       {
	             	    	  jQuery("#insert_kecheng").attr("value","1");
	             	    	  jQuery("#insert_richeng").attr("value","2");
	             	    	  $('input:radio[name=I]')[0].checked = true;
	             	    	   jQuery("#real_d_text").text("删除事项");
	             	       }
	             	       
	             	      jQuery("#titleInput").val(event.tit);
	             	      jQuery(".popup_SS_II").text(event.tit);
	             	      jQuery("#contentArea").val(event.con.replace(/<[^>]+>/g,""));
	             	      jQuery("#bTime").val(getDateStr(event.bt));
	             	      jQuery("#eTime").val(getDateStr(event.et));
	             	      
	             	      var lpt =event.rt;
	             	      if(typeof(lpt)!="undefined")
	             	      {
	             	    	  var lp=lpt.lp;
	             	    	  var dv=lpt.dv;
	             	    	  var edt=lpt.edt;
	             	    	  var edv=lpt.edv;
	             	    	  
	             	    	 jQuery("#lpSelect").val(lp);
	             	    	 if(lp==1)
	             	    	  {
	             	    		 jQuery("#otherDIV").hide();
	             	    		 
	             	    		 if(dv==0)
	             	    		 {
	             	    			 jQuery(".popup_text_XJ_DY_II").attr("checked",true);
	             	    		 }else
	             	    		 {
	             	    			 jQuery(".popup_text_XJ_DY_III").attr("checked",true);
	             	    			 jQuery("#dayTypeInput").val(dv);
	             	    		 }
	             	    		 if(edt==0)
	             	    		 {
	             	    			 jQuery(".popup_text_XJ_DY_V_I").attr("checked",true);
	             	    		 }
	             	    		 if(edt==1)
	             	    		 {
	             	    			 jQuery(".popup_text_XJ_DY_V_II").attr("checked",true);
	             	    			 jQuery("#dayCishu").val(edv);
	             	    		 }
	             	    		 if(edt==2)
	             	    		 {
	             	    			 jQuery(".popup_text_XJ_DY_V_IIII").attr("checked",true);
	             	    			 jQuery("#dayEndInput").val(getDateStr(edv));
	             	    		 }
	             	    		 
	             	    		 jQuery("#dayDIV").show();
	             	    	  }
	             	    	  else //周月年
	             	    	  {
                                 jQuery("#dayDIV").hide();
	             	    	
	             	    		 if(edt==0)
	             	    		 {
	             	    			 jQuery(".popup_text_XJ_YE_IIII").attr("checked",true);
	             	    		 }
	             	    		 if(edt==1)
	             	    		 {
	             	    			 jQuery(".popup_text_XJ_YE_II").attr("checked",true);
	             	    			 jQuery("#otherCishu").val(edv);
	             	    		 }
	             	    		 if(edt==2)
	             	    		 {
	             	    			 jQuery(".popup_text_XJ_YE_III").attr("checked",true);
	             	    			 jQuery("#otherEndInput").val(getDateStr(edv));
	             	    		 }
	             	    		 
	             	    		 jQuery("#otherDIV").show();
	             	    	  }
	             	   
	             	      }
	             	      else
	             	      {
	             	    	  jQuery("#lpSelect").val(-1);
	             	    	  jQuery("#otherDIV,#dayDIV").hide();
	             	      }
	             	      
	                }else
	                {
	                 alert(res.message);
	                }
	             }
	         });
		}
	}
	
	
	
	function insert()
	{
		
		var type=jQuery("input[name='I']:checked").val();
		var title=jQuery("#titleInput").val();
		var content=jQuery("#contentArea").val();
		
		 var beginTime;
		 var endTime;
		 try
		 {
			    if(!jQuery("#bTime").val() || !jQuery("#eTime").val())
			    {
			    	 alert("请正确选择开始时间");
			    	 return;
			    }
	
		         beginTime=getTime(jQuery("#bTime").val());
		   
		         endTime=getTime(jQuery("#eTime").val());
		         
		         if(beginTime>=endTime)
		        	 {
		        	     alert("请正确选择时间");
				         return;
		        	 }
		        
		 }catch(x)
		 {
		        alert("请正确选择时间");
		        return;
		 }
		 
		 var lp=jQuery("#lpSelect").val();
		 var dv=-1;
		 var edt;
		 var edv;
		 if(parseInt(lp)>0) //循环
		 {
			 if(parseInt(lp)==1) //天威循环力度
		     {
				 
				 var dayType=jQuery("input[name='day']:checked").val();
				 
				 
				 if(dayType==0)
				 {
					 dv=0;
				 }
				 else
				 {
					 try
					 {
						 dv=parseInt(jQuery("#dayTypeInput").val());
						 if(dv<0)
						 {
						  
						  alert("参数错误");
						  return;
						 }
						
					 }catch(x){
						 alert("参数错误");
						 return;
					 }
				 }
				 
				 edt=jQuery("input[name='V']:checked").val();
				 if(edt==0)
				 {
				  edv=0;
				 }
				 if(edt==1)
				 {
					 try
					 {
						 edv=parseInt(jQuery("#dayCishu").val());
						 
						 if(edv<0)
							 {
							  
							  alert("参数错误");
							  return;
							 }
					 }catch(x){
						 alert("参数错误");
						 return;
					 }
				 }
				 if(edt==2)
				 {
					 edv= getTime(jQuery("#dayEndInput").val());
				 }
		     }
			 else //其他
			 {
				 edt=jQuery("input[name='year']:checked").val();
				 if(edt==0)
				 {
				  edv=0;
				 }
				 if(edt==1)
				 {
					 try
					 {
						 edv=parseInt(jQuery("#otherCishu").val());
					 }catch(x){
						 alert("参数错误");
						 return;
					 }
				 }
				 if(edt==2)
				 {
					 edv= getTime(jQuery("#otherEndInput").val());
				 }
		     }
			 
			 if(isNaN(edv))
			 {
				   alert("请正确选择循环结束时间");
			       return;
			 }
			 
			 
			 if( edt<0 || edv<0 )
			 {
				  alert("参数错误");
			      return;
			 }
		 }
		
		
		 
		 
		// String type,String title,String content,String beginTime,String endTime,
        // String lp,String dv,String edt,String ed
		 var data={
	                "type":type,
	                "title":title,
	                "content":content,
	                "beginTime":beginTime,
	                "endTime":endTime,
	                "lp":lp,
	                "dv":dv,
	                "edt":edt,
	                "edv":edv,
	                "operId":operId
	           };
		 
		 $.ajax({
             url: '/calendar/add.do',
             type: 'POST',
             contentType: 'application/json',
             data: data,
             contentType:"application/x-www-form-urlencoded; charset=utf-8",
             success: function (res) 
             {
                if(res.code=="200")
                {
                	   flushData();
             	       hideAll();
                }else
                {
                 alert(res.message);
                }
             }
         });
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
