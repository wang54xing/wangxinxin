define('fenban',['jquery','doT','common'],function(require,exports,module){
	 var fenban = {};
     require('jquery');
     require('doT');
     common = require('common');
	 /**
	  * 得到知识面
	  */
	 loadData= function(cid)
	 {
		 var url="/t33/fenban/data1.do";
		 common.getData(url,{"cid":cid},function(rep){
			 if(rep.code=="200")
			 {
				 jQuery("#student_tr").empty();
					   
					   for(var i=0;i<rep.message.length;i++)
					   {
						   
						   try
						   {
						   var obj=rep.message[i];
						   var html='<tr>';
						   html+='<td>'+obj.name+'</td>';
						   html+='<td>'+obj.classPair.value+'</td>';
						   
						   var zuhe=getZuhe(obj);
						   html+='<td>'+zuhe+'</td>';
						 	//物理 	化学 	生物 	历史 	地理 	政治
						   if(zuhe.indexOf("物理")>=0)
						   {
							   html+='<td>已选</td>';
						   } 
						   else
						   {
							   html+='<td></td>';
						   }
						   
						   
						   if(zuhe.indexOf("化学")>=0)
						   {
							   html+='<td>已选</td>';
						   } 
						   else
						   {
							   html+='<td></td>';
						   }
						   
						   if(zuhe.indexOf("生物")>=0)
						   {
							   html+='<td>已选</td>';
						   } 
						   else
						   {
							   html+='<td></td>';
						   }
						   
						   
						   if(zuhe.indexOf("历史")>=0)
						   {
							   html+='<td>已选</td>';
						   } 
						   else
						   {
							   html+='<td></td>';
						   }
						   
						   
						   if(zuhe.indexOf("地理")>=0)
						   {
							   html+='<td>已选</td>';
						   } 
						   else
						   {
							   html+='<td></td>';
						   }
						   
						   if(zuhe.indexOf("政治")>=0)
						   {
							   html+='<td>已选</td>';
						   } 
						   else
						   {
							   html+='<td></td>';
						   }
						    html+='</tr>';
						   
						   
						   jQuery("#student_tr").append(html);
						   
						   }catch(x)
						   {
							   
						   }
					   }
					  
			 }
		 });
	 }
	 
	 fenban.init=function(){
		 
		 var id= jQuery(jQuery(".clearfix").find("li").get(0)).attr("id");
		 showClass(id);
		 
		 jQuery(".clearfix").find("li").click(function(){
			 
			 var subid=jQuery(this).attr("id");
			 showClass(subid);
		 });
	 }
	 
	 
	 function showClass(subId)
	 {
		 
		 
		 jQuery(".clearfix").find(".li-cur").removeClass("li-cur");
		 
		 jQuery("#"+subId).addClass("li-cur");
		 
		 
		 var wuliCount=jQuery("#"+subId+"_c").val();
		 classCount(subId,wuliCount);
	 }
	 
	 
	 function classCount(subid,count)
	 {
		 var subName=jQuery("#"+subid).text();
		 var hts="";
		 for(var i=0;i<count;i++)
		 {
			 if(i==0)
			 {
				 hts+='<li id="'+(subid+"_"+i)+'" class="li-cur">'+(subName+(i+1))+'</li>';
			 }
			 else
			 {
				 hts+='<li id="'+(subid+"_"+i)+'">'+(subName+(i+1))+'</li>';
			 }
		 } 
		 jQuery("#class_count").empty();
		 jQuery("#class_count").html(hts);
		 
		 
		 jQuery("#class_count").find("li").click(function(){
			 
			 jQuery("#class_count").find(".li-cur").removeClass("li-cur");
			 jQuery(this).addClass("li-cur");
			 var cid=jQuery(this).attr("id");
			 loadData(cid);
		 });
		 
		 loadData(subid+"_0");
		 
	 }
	
	 
	 function getZuhe(obj)
	 {
		 var s="";
		 for(var j=0;j<obj.subjects.length;j++)
		 {
			 s+=obj.subjects[j].value;
		 } 
		 return s;
	 }
	 
	 
     module.exports=fenban;
});
