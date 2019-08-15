 function manageMsgUI(finder)
 {
    	var self=this;
    	this.scrollUI= function(){return $(finder);}
    	
    	this.latestMsgId= function(){ 
    		   var an= this.scrollUI().find('li.answer:first');
    		   if(an.length>0)
   			   {
   			      return an.attr('index');
   			   }
    		   else
   			   {
   			      return this.scrollUI().find('li.question:first').attr('index');
   			   }
    		  
    		}
    	this.oldestMsgId= function(){ 
    		var an= this.scrollUI().find('li.answer:last');
 		   if(an.length>0)
		   {
		      return an.attr('index');
		   }
 		   else
		   {
		       return this.scrollUI().find('li.question:last').attr('index');
		   }
       }
    	
    	 this.addTemplate=function (){
    			return	$("<li><img src='img/fudan-head1.png' align='absmiddle' /></li>");
    	    }
    	 
    	 this.addTeacherStudent=function(msgs){

     		 for(var index in msgs)
     		{
     			 var UI=this.scrollUI();

   				 UI.append(this.addTemplate().attr('index',msgs[index].user.id).append(msgs[index].user.schoolName+ "&nbsp;&nbsp;"+ msgs[index].user.userName));
     		}
    	 }
    		
    	 this.addMsg=function(msgs,bsingle)
     	{
     		 for(var index in msgs)
     		{
     			 var UI=this.scrollUI();
     			 if(bsingle)
   				 {
     				if(msgs[index])
   					{
 				        UI.append(this.addTemplate().attr('index',msgs[index].id).append(msgs[index].content));
   					}
   				 }
     			 else
     		    {
 	  			 var answer= this.addTemplate()[0].attr('index',msgs[index].id).append(msgs[index].content);
 	  			 var question= null;
 	  			 if(msgs[index].parentInfo)
 				 {
 			       question= this.addTemplate()[1].attr('index',msgs[index].parentInfo.id).append(msgs[index].parentInfo.content);
 			       UI.append(question);
  			       UI.append(answer);
 				 }
     		   }
     	    }
     	}
     	
     	this.insertMsg=function(msgs,bsingle)
     	{
     		if(msgs.length>0)
     		{
	     		 for(var index in msgs)
	      		{
	      			var UI=this.scrollUI();
	      			if(bsingle)
	  				 {
	    				if(msgs[index])
	  					{
					       this.addTemplate().attr('index',msgs[index].id).append(msgs[index].content).insertBefore(UI.find("li:first"));	   
	  					}
	  				 }
	    			 else
	    		    {
	      			 var answer= this.addTemplate()[0].attr('index',msgs[index].id).append(msgs[index].content);
	 	  			 var question= null;
	 	  			 if(msgs[index].parentInfo)
	 				 {
	 			       question= this.addTemplate()[1].attr('index',msgs[index].parentInfo.id).append(msgs[index].parentInfo.content);
	 			       answer.insertBefore(UI.find("li:first"));
		  			   question.insertBefore(answer);
	 				 }
	    		   }
	    
	      	    }
     	  }
      }
   }
 
 function getSchoolInfo(id)
 {
	 var ret=[];
 	$.ajax({
 		url:"getSchoolInfo.action",
 		type:"get",
 		dataType:"json",
 		async:false,
 		data:{
 			    'schoolId': id,
 		     },
 		success:function(data){
 			ret=data;
 		}
 	});
 	return ret;
 }
 
 
 function addEvent(el, type, callback, useCapture){
     if(el.dispatchEvent){//w3c方式优先
         el.addEventListener( type, callback, !!useCapture  );
     }else {
         el.attachEvent( "on"+type, callback );
     }
     return callback;//返回callback方便卸载时用
 }
 
 var wheel = function(obj,callback){
     var wheelType = "mousewheel"
     try{
         document.createEvent("MouseScrollEvents")
         wheelType = "DOMMouseScroll"
     }catch(e){}
     addEvent(obj, wheelType,function(event){
         if ("wheelDelta" in event){//统一为±120，其中正数表示为向上滚动，负数表示向下滚动
             var delta = event.wheelDelta
             //opera 9x系列的滚动方向与IE保持一致，10后修正
             if( window.opera && opera.version() < 10 )
                 delta = -delta;
             //由于事件对象的原有属性是只读，我们只能通过添加一个私有属性delta来解决兼容问题
             event.delta = Math.round(delta) /120; //修正safari的浮点 bug
         }else if( "detail" in event ){
             event.wheelDelta = -event.detail * 40//为FF添加更大众化的wheelDelta
             event.delta = event.wheelDelta /120  //添加私有的delta
         }
         callback.call(obj,event);//修正IE的this指向
     });
 }