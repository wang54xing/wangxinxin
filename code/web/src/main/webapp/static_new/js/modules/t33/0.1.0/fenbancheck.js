define('fenbancheck',['jquery','doT','common'],function(require,exports,module){
	 var fenbancheck = {};
     require('jquery');
     require('doT');
     common = require('common');
     

	
	 fenbancheck.init=function(){
		 
		var id= jQuery(jQuery("#compareUI").find("li").get(0)).attr("id");
		 showClass(id);
		 
		 jQuery("#compareUI").find("li").click(function(){
			 
			 
			 var cId= jQuery(this).attr("id");
			 showClass(cId);
			 
			 
		 });
		 
	 }
	 
	 
	 function showClass(subId)
	 {
		 jQuery("#container").empty();
		 
		 jQuery("#compareUI").find(".li-cur").removeClass("li-cur");
		 
		 jQuery("#"+subId).addClass("li-cur");
		 
		 
		 jQuery("#compareUI").find("li").each(function(){
			 
			var cId= jQuery(this).attr("id");
			if(subId!=cId)
			{
				showClassDetail(subId,cId);
			}
		 });
		 
		 
		 
		 
		 $('.chat').click(function(){
			 
			    var cId= jQuery(this).attr("id");
			     
			    
			    getjiaoji(cId);
	    		
	     })
		 
		
	 }
	 
	 
	 function showClassDetail(sub,another)
	 {
		 var subName=jQuery("#"+sub).text();
		 var anotherName=jQuery("#"+another).text();
		 
		 var subCount=jQuery("#"+sub+"_c").val();
		 var anotherCount=jQuery("#"+another+"_c").val();
		 
		 var htmls='<div><p class="list-p"><em>'+subName+'</em>/<em>'+anotherName+'</em></p><div class="tab">';
		 htmls+='<table>';
		 
		 var headStr=createHead(subName,subCount);
		 htmls+=headStr;
		 htmls+='<tbody>';

		 for(var j=0;j<anotherCount;j++)
	     {
			 var thisRow=createRow(another,anotherName,j,sub,subCount);
			 htmls+=thisRow;
	     } 
		 htmls+='</tbody></table></div></div>';
		 jQuery("#container").append(htmls);
	 }
	 
	 
	 
	 function createHead(subName,count)
	 {
		 var hs='<thead><tr><th><em></em></th>';
		 for(var i=0;i<count;i++)
		 {
			 hs+='<th><em>'+subName+(i+1)+'</em></th>';
		 } 
		 hs+='</tr></thead>';
		 return hs;
	 }
	 
	 var jyjj="";
	 function createRow(another,anotherName,index,sub,subcount)
	 {
		 if(!jyjj)
		 {
			 jyjj=jQuery("#jyjj").val();
		 }
		 var rowStr='<tr>';
		 rowStr+='<td>'+anotherName+(index+1)+'</td>';
		 for(var i=0;i<subcount;i++)
		 {
			var thisId=another+"_"+index+"_"+sub+"_"+i;
			if(jyjj.indexOf(thisId)>=0)
			{
			 var coun=jQuery("#"+thisId).val();
			 rowStr+='<td><em class="chat" id="'+thisId+'">'+coun+'</em></td>';
			}
			else
			{
				 rowStr+='<td></td>';
			}
		 } 
		 rowStr+='</tr>';
		 return rowStr;
	 }
	 
	 
	 
	 function getjiaoji(id)
	 {
		 
		 /**
		 var url="/t33/fenban/jiaoji.do";
		 common.getData(url,{"idtag":id},function(rep){
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
		 **/
		 
		 var p='/t33/fenban/jiaoji/page.do?idtag='+id;
		 location.href=p;
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
     module.exports=fenbancheck;
});
