define('userBasic',['jquery','doT','common'],function(require,exports,module){
	 var userBasic = {};
     require('jquery');
     require('doT');
     common = require('common');
   
     var countdown = 60;
     
     
     
     function settime(val) {
         if (countdown == 0) {
             val.attr("disabled", false);
             val.text("点击获取");
             countdown = 60;
             return true;
         } else {
             val.attr("disabled", true);
             val.text("重新发送(" + countdown + ")");
             countdown--;
         }
         setTimeout(function() {
             settime(val)
         },1000)
     }
     
     
     
     getVerificationCode= function (){
         var phone = $('#mobileNUmber').val();
         var verifyCode = $('#verifyCode').val();
         if(verifyCode == ''){
             alert("验证码不能为空");
             return false;
         }
         {
             changeImg();
             common.getData("/mall/users/messages.do", {mobile:phone, verifyCode:verifyCode}, function(resp){
            	 
            	 
            	 if(resp.code=="200")
            		 {
		               var  cacheKeyId = resp.cacheKeyId;
		                 $('#cacheKeyId_tr').show();
		                 jQuery("#cacheKeyId").val(cacheKeyId);
		                 
		                 
		                 settime(jQuery(".account-C"));
            		 }
            	 else
            		 {
            		  alert(resp.message);
            		 }
             })
             return true;
         }
         return false;
     }

    changeImg= function (){
        var src = "/verify/verifyCode.do";
        var src=chgUrl(src);
        $("#imgObj").attr("src", src);
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
   
     
     

     sendInfos= function (){
    	 
    	 var userLoginName=jQuery("#userName").val();
    	 var mobile=jQuery("#mobileNUmber").val();
    	 var valiCode=jQuery("#valiCode").val();
    	 var cacheKeyId=jQuery("#cacheKeyId").val();
    	 var email=jQuery("#email").val();
    	 var sex=$("input[name='sex']:checked").val();
    	 
    	 
    	 if(!jQuery.trim(userLoginName))
    	 {
    		 alert("请输入用户登录名");
    		 return;
    	 }	 
    	 if(!sex)
    		 {
    		 sex=-1;
    		 }
    	 
    	 
    	 
         var requestData = {};
         requestData.userLoginName = userLoginName;
         requestData.mobile = mobile;
         requestData.valiCode = valiCode;
         requestData.cacheKeyId = cacheKeyId;
         requestData.email = email;
         requestData.sex = sex;
         common.getPostData('/user/update/basic.do', requestData, function(resp){
             if(resp.code != 200){
                 alert(resp.message);
             }
             else
             {
            	 alert("修改成功");
             }
         })
     }

     
     
     function checkPhoneNumber(phone){
  
     
         var pattern = /^1[34578]\d{9}$/;
         if(!pattern.test(phone)) {
            alert("手机号码错误");
            return false;
         } 
     }
     

	
     userBasic.init=function(){
		 
	 }
	
     module.exports=userBasic;
});
