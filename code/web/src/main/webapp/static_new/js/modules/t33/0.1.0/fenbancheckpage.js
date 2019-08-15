define('fenbancheckpage',['jquery','doT','common'],function(require,exports,module){
	 var fenbancheckpage = {};
     require('jquery');
     require('doT');
     common = require('common');
	
	 fenbancheckpage.init=function(){
		 getjiaoji();
		 loadData();
	 }
	 
	 
	 /**
	  * 得到知识面
	  */
	 loadData= function()
	 {
		 var url="/t33/fenban/data.do";
		 common.getData(url,{"cid":jQuery("#idTag").val()},function(rep){
			 if(rep.code=="200")
			 {
				 
				 
				 var idtg=jQuery("#idTag").val();
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
						   
						  
						  // html+='<td><a target="_blank" href="/t33/fenban/tiaoban.do?userId='+obj.studentIdStr+'&idtag='+idtg+'">调班</a></td>';
						   
						    html+='<td><a target="_blank" href="/t33/tiaobanpage.do?userId='+obj.studentIdStr+'&idtag='+idtg+'">调班</a></td>';
						   html+='</tr>';
						   
						   
						   jQuery("#student_tr").append(html);
						   
						   }catch(x)
						   {
						   }
					   }
					  
			 }
		 });
	 }
	 
	 
	 
	 
	 
	 
	 
	 function getjiaoji()
	 {
		 var url="/t33/fenban/jiaoji.do";
		 common.getData(url,{"idtag":jQuery("#idTag").val()},function(rep){
			 if(rep.code=="200")
			 {
				 jQuery("#s1,#s2,#s3,#u1,#u2,#u3").empty();
				 if(rep.message.length==3)
				 {
					 var obj1=rep.message[0];
					 jQuery("#s1").text(obj1.subjectName);
					 jQuery("#u1").html(getusers(obj1));
					 
                     var obj2=rep.message[1];
					 jQuery("#s2").text(obj2.subjectName);
					 jQuery("#u2").html(getusers(obj2));
					 
                     var obj3=rep.message[2];
                 
					 jQuery("#s3").text(obj3.subjectName);
					 jQuery("#u3").html(getusers(obj3));
					 
				 } 
				 
				 
				 $('.bg').show();
		    	 $('.gl-popup').show();
			 }
		 });
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
	 
	 
	 
	 function getusers(obj)
	 {
		 var ht="";
		 for(var i=0;i<obj.userList.length;i++)
		 {
			 ht+='<em>'+obj.userList[i].name+'</em>';
		 } 
		 
		 return ht;
	 }
     module.exports=fenbancheckpage;
});
