function validateForm(option,errorMsg)
 {
	  var msgIndex=0;
	  errorMsg.msg= typeof errorMsg.msg=='undefined'?[]:errorMsg.msg;
	  errorMsg.con= typeof errorMsg.con=='undefined'?[]:errorMsg.con;
	  $(errorMsg.con).text('');
	  
	  for(var name in option)
	  {
	      var target=$("#"+name);
	      if(target.length>0)
    	  {
		      var msg=typeof errorMsg.msg[msgIndex]=='undefined'?'':errorMsg.msg[msgIndex];
		      var con=typeof errorMsg.con[msgIndex]=='undefined'?errorMsg.con[0]:errorMsg.con[msgIndex];
		      var value='';
		      if(target.val()==null || typeof target.val()=='undefined')
	    	  {
	    	     value='';
	    	  }
		      else
	    	  {
	    	    value=target.val();
	    	  }

		      if(option[name].required)
	    	  {
	    	      if(value.length==0)
		    	  {
	    	    	  if(msg.length==0)
	    			  {
		    			  var prefix= target.prev().text();
		    			  msg=prefix+"不可为空！";
	    			  }
	    	    	  else{
	    	    		  msg+="不可为空！";
	    	    	  }
	    	    	 shErMsg(msg,con);
		    	     return false;
		    	  }
	    	  }
		      
		      if(option[name].maxLength)
	    	  {
		    	  if(value.getLength()>option[name].maxLength)
	    		  {
		    		  var fixed=msg;
		    		  if(msg.length==0)
	    			  {
		    			  var prefix= target.prev().text();
		    			  msg=prefix+"不能长于"+option[name].maxLength+"位！";
	    			  }
		    		  else{
		    			  msg+="不能长于"+option[name].maxLength+"位！";
		    		  }
		    		  
		    		  if(option[name].bCode)
    				  {
	    				  msg= fixed +"是"+option[name].maxLength+"位以内字母数字下划线，或者"+
	    				        option[name].maxLength/2+"位以内的汉字！";
    				  }
		    		  
		    		  shErMsg(msg,con);
		    		  return false;
	    		  }
	    	  }
		      
		      if(option[name].minLength)
	    	  {
		    	  if(value.getLength()<option[name].minLength)
	    		  {
		    		  if(msg.length==0)
	    			  {
		    			  var prefix= target.prev().text();
		    			  msg=prefix+"不能短于"+option[name].minLength+"位！";
	    			  }
		    		  else{
		    			  msg+="不能短于"+option[name].minLength+"位！";
		    		  }
		    		  shErMsg(msg,con);
		    		  return false;
	    		  }
	    	  }
		      
		      if(option[name].email)
	    	  {
	    	      if(value.length>0)
		    	  {
		    	      if(!/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(value))
	    	    	  {
		    	    	  if(msg.length==0)
		    			  {
		    	    		  msg="请输入正确的邮箱格式！";
		    			  }
		    	    	  else
	    	    		  {
		    	    		  msg+="格式不正确！";
	    	    		  }
		    	    	  shErMsg(msg,con);
			    		  return false;
	    	    	  }
		    	  }
	    	  }
		      
		      if(option[name].diff)
	    	  {
		    	  if(value==target.data('val'))
	    		  {
		    		  if(msg.length==0)
	    			  {
		    			  var prefix= target.prev().text();
		    			  msg="请确保"+prefix+"不同于初始值！";
	    			  }
		    		  shErMsg(msg,con);
		    		  return false;
	    		  }
	    	  }
		      
		      if(option[name].confirm)
	    	  {
		       
	    	     if(value!=$("#"+ option[name].confirm).val())
		    	 {   
	    	    	 if(msg.length==0)
	    			 {
		    			  msg="确认密码与新密码不一致！";
	    			 }
	    	    	 else{
	    	    		
	    	    		 msg+="与新密码不一致！";
	    	    	 }
	    	    	  shErMsg(msg,con);
		    		  return false;
		    	 }
	    	    
	    	    	 
	    	    
	    	  }
          }
	      msgIndex++;
	  }
	  return true;
 }
function shErMsg(m,c)
{
	 if(typeof c=='undefined' || c.length==0)
	 {
	    alert(m);
	 }
	 else
	 {
		 $(c).css("color",'red').text(m);
	 }
}

// 验证fulaan全国通用版
function isFulaan()
{
	if(document.domain.match('www'))
	{
	   return true;
	}
	return false;
}

//  jquery extend

   $.fn.number=function(bAbs){
	   bAbs = typeof bAbs=='undefined'?true:bAbs;
	   var rawValue='';
	   $(this).keydown(function(e){
		      rawValue=$(this).val();
			  if (/msie/.test(navigator.userAgent.toLowerCase())) { 
			   if ( ((e.keyCode > 47) && (e.keyCode < 58)) || ((e.keyCode > 95) && (e.keyCode < 106)) || (e.keyCode == 8) ){ 
			        return true;
			   }
			   else if(!bAbs && (e.keyCode == 189))
			   {
				   if(rawValue.match('-'))
		    	   {
			    	   e.preventDefault();
				        return false;
		    	   }
				   else if(GetCursorPsn(this)>0)
	    		   {
			    	   e.preventDefault();
				        return false;
	    		   }
			       else
		    	   {
			    	   return true;
		    	   }
			   }
			   else {
				    e.preventDefault();
			        return false;
			   }
			  } else {
			   if ( ((e.which > 47) && (e.which < 58)) || ((e.which > 95) && (e.which < 106))|| (e.which == 8) || (e.keyCode == 17) ) {
				   return true; 
			   } 
			   else if(!bAbs && (e.keyCode == 189))
			   {
			       if(rawValue.lastIndexOf('-')>0)
		    	   {
			    	    e.preventDefault();
				        return false;
		    	   }
			       else
		    	   {
			    	   return true;
		    	   }
			   }
			   else if(e.keyCode==229)
			   {
				   return true;
			   }
			   else {
				   e.preventDefault();
			       return false;
			   }
			  }
			 }).bind('keyup',function(e){

			 }).bind('input',function(e){
				/* this.value = this.value.replace(/\D+/g, "");*/
				 if(!bAbs)
				 {
					 var reg=new RegExp('^-?\\d+$');
					 if(reg.test(this.value))
					 {
						 if(this.value.lastIndexOf('-')>0)
			    		 {
							 if(this.value.indexOf('-')==0)
							 {
								 this.value ='-'+ this.value.replace(/\D+/g, "");
							 }
							 else
							 {
								 this.value =this.value.replace(/\D+/g, "");
							 }
			    		 }
					 }
					 else
					 {
						  if(this.value.indexOf('-')==0)
						  {
							  this.value ='-'+ this.value.replace(/\D+/g, "");
						  }
						  else
						 {
							 this.value =this.value.replace(/\D+/g, "");
						 }
					 }
				 }
				 else
				 {
				   this.value = this.value.replace(/\D+/g, "");
				 }
				
				 rawValue= this.value;
			 }).focus(function() {
			   this.style.imeMode='disabled'; 
		});
   };
   
   function GetCursorPsn(txb) 
   { 
	   if(document.selection)
	   {
	      var slct = document.selection; 
	      var rng = slct.createRange(); 
	      txb.select(); 
	      rng.setEndPoint("StartToStart", slct.createRange()); 
	      var psn = rng.text.length; 
	      rng.collapse(false); 
	      rng.select(); 
	      return psn; 
	   }
	   else
	   {
	      return txb.selectionStart; 
	   }
   } 

//
String.prototype.getLength = function() {
    var realLength = 0, len = this.length, charCode = -1;
    for (var i = 0; i < len; i++) {
        charCode = this.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128) realLength += 1;
        else realLength += 2;
    }
    return realLength;
};

String.prototype.replaceOnIndex = function(index,char) {
    len = this.length;
    var reStr='';
    for (var i = 0; i < len; i++) {
           if(i==index)
    	   {
    	      reStr+=char;
    	   }
           else
    	   {
        	   reStr+=this[i];
    	   }
    }
    return reStr;
};