define('t33',['jquery','doT','common'],function(require,exports,module){
	 var t33 = {};
     require('jquery');
     require('doT');
     common = require('common');
     
     
     

	 /**
	  * 得到知识面
	  */
	 loadPage= function()
	 {
		 var url="/t33/load.do";
		 common.getData(url,{},function(rep){
			 if(rep.code=="200")
			 {
					   common.render({tmpl: $('#subject_script'), data: rep.message.subjectList, context: '#subject_tr'});
					   
					   for(var i=0;i<rep.message.studentList.length;i++)
					   {
						   
						   try
						   {
						   var obj=rep.message.studentList[i];
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
	 
	 t33.init=function(){
		 loadPage();
		 
	    	
	     jQuery("#cancel").click(function(){
	    		$('#myForm').hide();
	     }); 
		 
		 
		 jQuery("#mySet").click(function(){
	    		$('#myForm').show();
	     }); 
	    	
	    	
		 jQuery("#se_school").change(function(){
			 
			 
			 var schoolId=jQuery("#se_school").val();
			 
			 if(schoolId=="59533a22b0fbeb15307a7566")//蚌埠
			 {
				 var gs='<option value="59533a22b0fbeb15307a7568">高一</option>';
				 jQuery("#se_grade").html(gs);
			 }
			 if(schoolId=="58bf9e9eb0fbeb67fcd550ba")//晋元
			 {
				 var gs='<option value="58bf9e9fb0fbeb67fcd550be">高二</option><option value="58bf9e9fb0fbeb67fcd550de">高三</option>';
				 jQuery("#se_grade").html(gs);
			 }
			 
		 });
		 
		 
		 
		 jQuery(".qd").click(function(){
			 
			 
			 common.getData("/t33/set.do",{"schoolId":jQuery("#se_school").val(),"gradeId":jQuery("#se_grade").val(),"persion":jQuery("#persionCount").val()},function(rep){});
			 
			 location.href="/t33/load/page.do?id="+new Date().getTime();
			 
		 });
		 
		 jQuery("#createClass").click(function(){
			 
					jQuery("#huaxue").val(2);
					jQuery("#shengwu").val(2);
					jQuery("#lishi").val(2);
					jQuery("#dili").val(2);
					jQuery("#zhengzhi").val(2);
					jQuery("#wuli").val(2);
					
					jQuery("#tag").val(jQuery("input[name='ra']:checked").val() );
			
					jQuery("#fenbanForm").submit();
			 
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
	 
	 
     module.exports=t33;
});
