/*********   activate  start       */
function initActivateUser(returnUrl) {
		$("#dialog-activate .title a").unbind("click");
		$("#dialog-activate .dialog-submit a.blue-btn").unbind("click");
		$("#dialog-activate .title a").bind("click",function(){
			closeActivate(returnUrl);
		});
		
		$("#dialog-activate .dialog-submit a.blue-btn").bind("click",function() {
					var check = $("#commit_tip");
					if (check.prop('checked')) {
						activateUser(returnUrl);
					} else {
						$("#dialog-activate .error").text("请先阅读服务条款！");
					}
				});
		
		$("#email").blur(function(){
			var emailValue=$(this).val();
			var email=this;
			 if(/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(emailValue))
			 {
				$.ajax({
					url : "/user/initializeEmail.action",
					type : "get",
					data : {
						email: emailValue
					},
					success : function(data) {
						if(data.status!="ok")
						{
							$('#dialog-activate .error').text(data.errorMessage);
							$(email).css("border","1px solid red");
						}
						else if(data.status=="ok")
						{
							$(email).css("border","1px solid #cccccc");
							$('#dialog-activate .error').text("");
						}
						else{
							
						}
					}
				});
			 }
			 else
			 {
				 $('#dialog-activate .error').text("请输入正确的邮箱格式！");
			 }
		});
		
		/* $.ajax({
			url : "/selectUserName.action",
			type : "get",
			data : {},
			success : function(data) {
				if (data.result) {
					$("#userName").val(data.result.userName);
					$("#userName").data("val", data.result.userName);
					$("#userSex").val(data.result.sex);
				}
			}
		}); */
	}
	
	function closeActivateWindow(returnUrl) {
		$("#fubg").hide();
		$("#dialog-activate").hide();
		varcpage = window.location.href;
		alert('恭喜您，激活账号获得500兰贝。');
		if (!currentPageID) {
			if(typeof returnUrl!="undefined")
			{
				window.location.href = returnUrl;
			}
			else
			{
				window.location.href = "/main";
			}
		} else {
			if(typeof returnUrl!="undefined")
			{
				window.location.href = returnUrl;
			}
			else
			{
			  window.location.reload();
			}
		} 
	}
	
	function activateUser(returnUrl) {
		var option = {
			userName : {
				required : true,
				maxLength: 20,
				bCode: true
			},
			nPassword : {
				required : true,
				maxLength: 20,
				minLength : 6
			},
			confirm_password : {
				required : true,
				minLength : 6,
				confirm : 'nPassword'
			},
			email : {
				required : true,
				maxLength: 255,
				email : true
			},
			pwdProtectA : {
				required : true,
				maxLength:40,
				bCode: true
			}
		};

		if (validateForm(option,{con:$('#dialog-activate .error'),msg:['设置用户名']})) {
			$.ajax({
				url : "/user/doInitializeUser.action",
				type : "post",
				dataType : "json",
				async : true,
				data : {
					'username' : $('#userName').val(),
					'sex' : $('#userSex').val(),
					'password' : $('#nPassword').val(),
					'email' : $('#email').val(),
					'pwdProtectQ' : $('#pwdProtectQ').val(),
					'pwdProtectA' : $('#pwdProtectA').val()
				},
				success : function(data) {
					if (data.status == "ok") {
						closeActivateWindow(returnUrl);
					} else {
						$(".error").text(data ? data.errorMessage : "");
					}
				}		
			});
		}
	}
	
/***  register start */
	
	function tryRegister(b) {
		if(b){
			cloBg();
		}
        _czc.push(["_trackEvent",'全站顶部导航','点击','注册','1','a']);
		/*alert('本地学生可通过教育局获取用户卡，用户卡直接登录享受更多优惠，免费观看本地老师视频。');*/
		window.location='/user/register';
	}
	
	/* function showRegister()
	 {
		 $("#fubg").css({
				display : "block"
			});
		$("#dialog-register").show();
		initRegisterUser();
	 }
	
	function initRegisterUser() {
		$('#dialog-register input').val('');
		$("#dialog-register .title a").unbind("click");
		$("#dialog-register .dialog-submit a.blue-btn").unbind("click");
		$("#dialog-register .title a").bind("click",function(){
			closeRegister();
		});
		
		$("#dialog-register .dialog-submit a.blue-btn").bind("click",function() {
					var check = $("#commit_tip_r");
					if (check.prop('checked')) {
						registerUser();
					} else {
						$("#dialog-register .error").text("请先阅读服务条款！");
					}
		});
		

		$("#register_city").unbind("change");
		
         $("#register_city").bind("change",function(){
        	 $('#register_school').html('');
        	 getSchoolForRegister($(this).val());
		});

         getCityForRegister();*/
         
     	/*$("#email_r").blur(function(){
			var emailValue=$(this).val();
			var email=this;
			 if(/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(emailValue))
			 {
				$.ajax({
					url : "/user/initializeEmail.action",
					type : "get",
					data : {
						email: emailValue
					},
					success : function(data) {
						if(data.status!="ok")
						{
							$('#dialog-register .error').text(data.errorMessage);
							$(email).css("border","1px solid red");
						}
						else if(data.status=="ok")
						{
							$(email).css("border","1px solid #cccccc");
							$('#dialog-register .error').text("");
						}
						else{
							
						}
					}
				});
			 }
			 else
			 {
				 $('#dialog-register .error').text("请输入正确的邮箱格式！");
			 }
		});
	}*/
	
	/*function registerUser()
	{
		var option = {
				userName_r : {
					required : true,
					maxLength: 20,
					bCode: true
				},
				nPassword_r : {
					required : true,
					maxLength: 20,
					minLength : 6
				},
				confirm_password_r : {
					required : true,
					minLength : 6,
					confirm : 'nPassword_r'
				},
				register_city:{
					required : true
				},
				register_school:{
					required : true
				},
				email_r : {
					required : true,
					maxLength: 255,
					email : true
				},
				pwdProtectA_r : {
					required : true,
					maxLength:40,
					bCode: true
				}
			};
		
		if (validateForm(option,{con:$('#dialog-register .error'),msg:['用户名','','','城市','学校']})) {
			$.ajax({
				url : "/user/registerUser.action",
				type : "post",
				dataType : "json",
				async : true,
				data : {
					'username' : $('#userName_r').val(),
					'password' : $('#nPassword_r').val(),
					'email' : $('#email_r').val(),
					'activationKey': $('#invite_code').val(),
					'geoId': $('#register_city').val(),
					'schoolId': $('#register_school').val(),
					'gradeId': $('#register_grade').val(),
					'pwdProtectQ' : $('#pwdProtectQ_r').val(),
					'pwdProtectA' : $('#pwdProtectA_r').val()
				},
				success : function(data) {
					if (data.status == "ok") {
						if($('#invite_code').val()=='fulaan')
						{
							alert('恭喜您获得200兰贝！（1兰贝=1人民币）');
							closeRegister(true);
						}
						else
						{
							alert('恭喜您获得100兰贝！（1兰贝=1人民币）');
							closeRegister(true);
						}
					} else {
						$("#dialog-register .error").text(data ? data.errorMessage : "");
					}
				}		
			});
		}
	}

	function closeRegister(b) {
		$("#fubg").hide();
		$("#dialog-register").hide();
		if(b){
		$.ajax({
			url : "login.action",
			type : "post",
			dataType : "json",
			data : {
				'userName' : $("#userName_r").val(),
				'password' : $("#nPassword_r").val(),
				'inJson' : true
			},
			success : function(data) {
				if (data.status == "ok") {
					if (!data.userInitialized) {
						cloBg();
						if(typeof data.geo =='undefined')
						{
						    showActivate();
						}
						else
						{
							var prefix= document.domain.substring(0,document.domain.indexOf('.'));
						 	var url="http://" + document.domain.replace(prefix,data.geo);
						 	url+='/main';
						    showActivate(url);
						}
					} else {
						if(typeof data.geo =='undefined')
						{
						    window.location = "/main";
						}
						else
						{
							 var prefix= document.domain.substring(0,document.domain.indexOf('.'));
						 	 var url="http://" + document.domain.replace(prefix,data.geo);
                             url+='/main';
							 window.location=url;
							  if(suffix.length>0 && suffix!='main')
							 {
								 window.location= url;
							 }
							 else if(suffix.length==0)
							 {
								 window.location= url+"main";
							 }
							 else
							 {
							 
							 } 
						}
					}
				} 
			}
		});
	  }
	}
	
	// 
	function getCityForRegister()
	{
		if($('#register_city').children().length<=1)
	    {
			$.ajax({
				url : "/user/getCity.action",
				type : "post",
				dataType : "json",
				async : true,
				data : {
	               'provinceId':3
				},
				success : function(data) {
				    	if(data)
			    		{
				    		for(var i in data)
			    			{
				    			if(typeof data[i].geoId!='undefined' && data[i].geoId!=null)
			    				{
			    				  $('#register_city').append('<option value="'+ data[i].geoId +'">'+ data[i].city +'</option>');
			    				}
			    			}
				    		
				    		 getSchoolForRegister(data[0].geoId);
			    		}
				}		
			});
	   }
	}
	
	//
	
	function getSchoolForRegister(cid)
	{
		if($('#register_school').children().length<=1)
	    {
			$.ajax({
				url : "/user/getSchool.action",
				type : "post",
				dataType : "json",
				async : true,
				data : {
	               'cityId':cid
				},
				success : function(data) {
					    $('#register_school').append('<option value="0">请选择学校</option>');
				    	if(data)
			    		{
				    		for(var i in data)
			    			{
				    			if(typeof data[i].schoolId!='undefined' && data[i].schoolId!=null)
			    				{
				    			   $('#register_school').append('<option value="'+ data[i].schoolId +'">'+ data[i].schoolName +'</option>');
			    				} 
			    			}
			    		}
				    	$('#register_school').append('<option value="0">其他</option>');
				}		
			});
	   }
	}
	*/