/*  share js for flipped class room */
function checkCourseTimeSpan(timeOb){
    var timeF=$(timeOb).closest("div").find(".fc_setting input:nth(0)").val();
    var timeL=$(timeOb).closest("div").find(".fc_setting input:nth(1)").val();
     if(timeF.length==0)
	{
     	if(timeL.length!=0)
		{
     		 var now=new Date();
      	     var h=new Date(timeL);
      	     if(now>h){
      	    	 alert("不在课程时间范围内！");
      	     }
      	     else{
      	    	 window.open($(timeOb).attr("url"));
      	     }
		}
     	else
   		{
     		window.open($(timeOb).attr("url"));
   		}
	}
     else if(timeL.length==0)
	{
	     if(timeF.length!=0)
		{
   		 var now=new Date();
    	     var q=new Date(timeF);
    	     if(now<q){
    	    	 alert("不在课程时间范围内！");
    	     }
    	     else{
    	    	 window.open($(timeOb).attr("url"));
    	     }
		}
	      	else
   		{
	      		window.open($(timeOb).attr("url"));
   		}
	}
     else
	{
     	 var now=new Date();
	     var q=new Date(timeF);
	     var h=new Date(timeL);
	     if(now>q && now<h)
    	 {
	    	 window.open($(timeOb).attr("url"));
    	 }
	     else
    	 {
    	    alert("不在课程时间范围内！");
    	 }
	}
}

function getAdaptTimeFromNow(time) {
    var lasttime=new Date().getTime()-new Date((time != null ? time:'')).getTime();
    lasttime/=1000;
    var timeStr="";
    if(lasttime/(3600*24)>1)
    {
        timeStr+= Math.floor(lasttime/(3600*24))+"天前";
    }
    else if(lasttime/3600>1)
    {
        timeStr+= Math.floor(lasttime/3600)+"小时前";
    }
    else if(lasttime/60>1)
    {
        timeStr+= Math.floor(lasttime/60)+"分钟前";
    }
    else
    {
        if(timeStr>0)
        {
            timeStr+= Math.floor(lasttime/60)+"秒前";
        }
        else
        {
            timeStr+="刚刚";
        }
    }
    return timeStr;
}

function getTimeFromNow(time) {
	var dataTime;
	if(time!=null){
		var year=time.substring(0,4);
		var month=time.substring(5,7);
		var day=time.substring(8,10);
		var hour=time.substring(11,13);
		var minute=time.substring(14,16);
		var second=time.substring(17,19);
		dataTime=new Date(year,month,day,hour,minute,second);
	}
	if(dataTime==null){
		dataTime=new Date('');
	}

	var lasttime=new Date().getTime()-dataTime.getTime();
	lasttime/=1000;
	var timeStr="";
	if(lasttime/(3600*24)>1)
	{
		timeStr+= Math.floor(lasttime/(3600*24))+"天前";
	}
	else if(lasttime/3600>1)
	{
		timeStr+= Math.floor(lasttime/3600)+"小时前";
	}
	else if(lasttime/60>1)
	{
		timeStr+= Math.floor(lasttime/60)+"分钟前";
	}
	else
	{
		if(timeStr>0)
		{
			timeStr+= Math.floor(lasttime/60)+"秒前";
		}
		else
		{
			timeStr+="刚刚";
		}
	}
	return timeStr;
}
