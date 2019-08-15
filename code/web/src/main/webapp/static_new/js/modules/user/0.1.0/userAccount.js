define('userAccount',['jquery','doT','common'],function(require,exports,module){
	 var userAccount = {};
     require('jquery');
     require('doT');
     common = require('common');
      

     var un="";
     var uty=-1;
     var phone="";
     var cacheKeyId = "";
     
     
     
     firstCheck=function()
     {
		 var url="/user/check/first.do";
		 common.getData(url,{"username":jQuery("#userName").val(),"vCode":jQuery("#vCode").val()},function(rep){
			 if(rep.code=="200")
			 {
				 un=jQuery("#userName").val();
				 uty=rep.message;
				 jQuery("#step_1").hide();
				 jQuery("#step_2").show();
				 if(uty!=3)
				 {
					 jQuery("#type_"+uty).show();
				 }else
				 {
					 jQuery("#user_i").text(jQuery("#userName").val());
					 jQuery(".bg,.bound-popup").show();
				 }
				 changeImg();
			 }
			 else
			{
				  alert(rep.message);
			}
		 });
     }
     
     
     secondCheck=function()
     {
    	 if(uty=="0" || uty=="1")
    	 {
    		 var url="/class/checkName.do";
    		 common.getData(url,{"userName":un,"studentName":jQuery("#studentName_"+uty).val(),"teacherName":jQuery("#teacherName_"+uty).val()},function(rep){
    			 if(rep.code=="200")
    			 {
    				
    				 jQuery("#step_2").hide();
    				 jQuery("#step_3").show();
    				
    			 }
    			 else
    			 {
    				  alert(rep.message);
    			 }
    		 });
    	 }	 
    	 
    	 
    	 if(uty=="2" )
    	 {
    		 var url="/myschool/checkName.do";
    		 common.getData(url,{"userName":un,"checkName":jQuery("#workmate").val()},function(rep){
    			 if(rep.code=="200")
    			 {
    				
    				 jQuery("#step_2").hide();
    				 jQuery("#step_3").show();
    				
    			 }
    			 else
    			 {
    				  alert(rep.message);
    			 }
    		 });
    	 }	 
     }
     
    
     
     thirdCheck=function()
     {
    	 var data={
    			  "userName":un,
    			  "mobile": phone,
    			  "valiCode":jQuery("#checkCode").val(),
    			  "cacheKeyId":cacheKeyId,
    			  "email":jQuery("#email").val()
    			 
    	         };
    	 
    		 var url="/user/update/basic2.do";
    		 common.getData(url,data,function(rep){
    			 if(rep.code=="200")
    			 {
    				 jQuery("#step_3").hide();
    				 changeImg();
    				 jQuery("#step_4").show();
    			 }
    			 else
    			 {
    				  alert(rep.message);
    			 }
    		 });
     }
     
     
     
     
     
     sendEmail =function()
     {
    	 var data={
   			  "email":jQuery("#email").val()
   	         };
   	 
   		 var url="/user/email.do";
   		 common.getData(url,data,function(rep){
   			 if(rep.code=="200")
   			 {
   				alert("邮件已发送，请查收");
   			 }
   			 else
   			 {
   				  alert(rep.message);
   			 }
   		 });
     }
     
     fourCheck=function()
     {
    	 var data={
   			  "username":un,
   			  "pwd":jQuery("#newPwd").val(),
   			  "pwdAgain":jQuery("#newPwdAgain").val(),
   			  "vCode":jQuery("#vode1").val()
   	         };
   	 
   		 var url="/user/resetPwd.do";
   		 common.getData(url,data,function(rep){
   			 if(rep.code=="200")
   			 {
   				 jQuery("#step_4").hide();
   				 jQuery("#step_5").show();
   			 }
   			 else
   			 {
   				  alert(rep.message);
   			 }
   		 });
     }
     
     
     getVerificationCode= function (obj){
          phone = $('#mobileNUmber').val();
		 var verifyCode = $('#vCode2').val();
		 if(verifyCode == ''){
			 alert("验证码不能为空");
			 return false;
		 }

             common.getData("/mall/users/messages.do", {mobile:phone, verifyCode:verifyCode}, function(resp){

            	 if(resp.code=="200")
            	 {
					 settime(obj);
            		 cacheKeyId = resp.cacheKeyId;
            	 }
            	 else
            		 {
            		 alert(resp.message);
            		 }
             })
             return true;
     }

    var countdown=60;
    settime = function (obj) {
        if (countdown == 0) {
            obj.removeAttribute("disabled");
            obj.value="免费获取验证码";
            countdown = 60;
            return;
        } else {
            obj.setAttribute("disabled", true);
            obj.value="重新发送(" + countdown + ")";
            countdown--;
        }
        setTimeout(function() {
                settime(obj) }
            ,1000)
    }

        checkPwd=function()
     {
    	 var pwd=jQuery("#newPwd").val();
    	 var pwdAgain=jQuery("#newPwdAgain").val();
    	 
    	 if(pwd!=pwdAgain)
    	 {
    		 alert("两次输入不一致");
    		 return;
    	 }	 
     }
	 
	 changeImg= function (){
		  var src = "/verify/verifyCode.do";
		  var src=chgUrl(src);
	        var imgSrc = $("#imgObj");
	        imgSrc.attr("src",src);
	        
	        var imgSrc1 = $("#imgObj1");
	        imgSrc1.attr("src",src);
		 $("#imgObj2").attr("src", src);
	    }
	 
	 function chgUrl(url){
	        var timestamp = (new Date()).valueOf();
	        url = url.substring(0,21);
	        if((url.indexOf("&")>=0)){
	            url = url + "×tamp=" + timestamp;
	        }else{
	            url = url + "?timestamp=" + timestamp;
	        }
	        return url;
	  }
	
	 userAccount.init=function(){
		 changeImg();
	 }
	
	 
	 hide=function()
	 {
		 jQuery(".bg,.bound-popup").hide();
		 window.location.href="/user/findPwd.do";
	 }
	 module.exports=userAccount;
});
