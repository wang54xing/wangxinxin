define('fenbanzuhe',['jquery','doT','common'],function(require,exports,module){
	 var fenbanzuhe = {};
     require('jquery');
     require('doT');
     common = require('common');
     
     var selectClass=[];
     var cnClass=jQuery("#jyjj").val();
     var sId="";
	 var operObj;
	 fenbanzuhe.init=function(){
		 $('body').on('click','.class-btn',function(){
			    selectClass=[];
				var ht='<div class="com-tab mt30 w1200"><table><thead><tr>';
	    		var nu = $('.inp2').val();
	    		var ths="";
	    		var tds="";
	    		for(var i =1; i <= nu; i ++){
	    			ths+='<th><em>教室'+(i+1)+'</em></th>';
	    			tds+='<td><em class="chat edit">更改</em></td>';
	    		}
	    		ht+=ths;
	    		ht+='</tr></thead><tbody><tr>';
	    		ht+=tds;
	    		ht+='</tr></tbody></table></div>';
	    		
	    		$('.com').append(ht);
	    	});
	    	
	    	
	    	
	    	
	    	
	    	$('body').on('click','.edit',function(){
	    	   operObj=jQuery(this);	
    		   $(".gl-popup").show();
    		   $('.bg').show();
    	    })
    
    	
		
		 var id= jQuery(jQuery("#subjectss").find("li").get(0)).attr("id");
		 showClass(id);
		 
		 jQuery("#subjectss").find("li").click(function(){
			 var subid=jQuery(this).attr("id");
			 showClass(subid);
		 });
		 
		 
		 jQuery(".qdaad").click(function(){
			 doselectClass();
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
			 var thisId=subid+"_"+i;
			 var isCan=isCanSelect(thisId);
			 if(isCan)
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
		 } 
		 jQuery("#class_count").empty();
		 jQuery("#class_count").html(hts);
		 
		 
		 jQuery("#class_count").find("li").click(function(){
			 jQuery("#class_count").find(".li-cur").removeClass("li-cur");
			 jQuery(this).addClass("li-cur");
			 sId=jQuery(this).attr("id");
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
	 
	 
	 function doselectClass()
	 {
		 if(!sId)
		 {
			 alert("请选择课程");
			 return;
		 } 
		 /**
		 var succ=true;
		 for(var i=0;i<selectClass.length;i++)
		 {
			 if(sId==selectClass[i])
			 {
				 alert("该课程已经选择");
				 return;
			 } 
			 var str=sId+"_"+selectClass[i];
			 if(cnClass.indexOf(str)>=0 )
			 {
				 alert("有冲突");
				 return;
			 }
		 } 
		 **/
		 
		 var isCan=isCanSelect(sId);
		 if(!isCan)
		 {
			 alert("不能选择");
			 return ;
		 }
		 
		 var ss=jQuery("#"+sId).text();
		 
		 operObj.removeClass("edit");
		 operObj.text(ss);
		 selectClass.push(sId);
		 
	  	 jQuery(".gl-popup").hide();
	  	 jQuery('.bg').hide();
		
	 }
	 
	 
	 /**
	  * 检查是否是冲突
	  */
	 function isCanSelect(sId)
	 {
		 if(selectClass.length==0)
		 {
			 return true;
		 } 
		 
		 
		 for(var i=0;i<selectClass.length;i++)
		 {
			 if(sId==selectClass[i])
			 {
				 return false;
			 } 
			 var str=sId+"_"+selectClass[i];
			 if(cnClass.indexOf(str)>=0 )
			 {
				 return false;
			 }
		 }
		 
		 return true;
	 }
	 
     module.exports=fenbanzuhe;
});
