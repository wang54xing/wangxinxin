var isFirstLoad = true;
var selectedContact = new Array();//新建群组的已选择联系人
var lastRecord = new Array();
$(function(){
	//点击聊天框及聊天相关框,禁止冒泡
	$(".chatRight").click(function(event){
        event.stopPropagation();
		return false;
    });
	//除了聊天框及聊天相关框的div,聊天框消失
	$('div').not('.chatRight').click(function(event){
		if($(this).closest('.center_main_popup_main').length > 0){
			event.stopPropagation();
			return;
		}else if($(this).closest('#quit-confirm-dialog').length > 0){
			event.stopPropagation();
			return;
		}else if($('#customCandidate').length > 0 && !$('#customCandidate').is(':hidden')){
			return;
		}else if(!$('#bg').is(':hidden')){
			//event.stopPropagation();
			return;
		}else if($(this).closest('.chatRight').length < 1){
			$('.chatRight').hide();
		}
	})

});

function listToggle(obj){
	var curtStatus  = $(obj).prop('className');
	if(curtStatus.indexOf('fa-caret-right') > 0){
		$(obj).removeClass('fa fa-caret-right').addClass('fa fa-caret-down');
		$(obj).parent().siblings('ul').slideDown();
	}else{
		$(obj).removeClass('fa fa-caret-down').addClass('fa fa-caret-right');
		$(obj).parent().siblings('ul').slideUp();
	}
}


function newGroup(){
	$('#bg').show();
	$('#selected-contact-count').html(0);
	selectedContact = new Array();
	$('#newGroup').show();
	if(isFirstLoad){
		GetContact();//新建群组
	} else {
		var html = template('contact-group-template', dataList);
        $('.new_center_main_popup_main_LT ul').html(html);
	}
	$('#select-group-list').html('');
}
function managerGroup(flag,roomId){
	$('#bg').show();
	if(isFirstLoad){
		GetContact();//成员管理
	}
	selectedContact = new Array();
	$.ajax({
		url:'/groupchat/isGroupMain.do',
		data:{roomid:roomId},
		dataType: "json",
		type:'post',
		success:function(data){
			if(data.isgroupmain){
				$('#select-group-list-manager').html('');
				for(var i = 0; i < data.groupmemberList.length; i++){
					var temp = data.groupmemberList[i];
					selectedContact.push(temp.userid);
					if(!temp.isgroupmain){
						$('#select-group-list-manager').append('<li user-id="'+ temp.userid +'"><div class="new_center_main_popup_main_RT_III"><img src="'+ temp.maxImageURL +'"><div>'+ temp.userName +'</div><span onclick="removeFromSelect(this)">x</span></div></li>');
					}else{
						curGroupMainId = parseInt(temp.userid);
					}
				}
				$('#selected-contact-count-manager').html(data.groupmemberList.length-1);
				
				$('#groupName','#managerGroup').val(curRoomName);
				//标识已添加到"已选择联系人"
				$('.contact-selected').remove();
				$('#select-group-list-manager li').each(function(){
					var userid = $(this).attr('user-id');
					var $contactli = $('.group-list').find('li[user-id="'+ userid +'"] div');
					$contactli.append('<span class="contact-selected" style="left: 370px;top: 28px;"><i class="fa fa-check-square fa-lg"></i></span>');
				});
				$('#managerGroup').show();
			} else {
				$('#group-member-list-view').html('');
				for(var i = 0; i < data.groupmemberList.length; i++){
					var temp = data.groupmemberList[i];
					selectedContact.push(temp.userid);
					if(!temp.isgroupmain){
						$('#group-member-list-view').append('<li user-id="'+ temp.userid +'"><div class="new_center_main_popup_main_SUB_II"><img src="'+ temp.maxImageURL +'"><span>'+ temp.userName +'</span></div></li>');
					}else{
						$('#manager-info span').html(temp.userName);
						$('#manager-info img').attr('src',temp.maxImageURL);
					}
				}
				$('#selected-contact-count').html(data.groupmemberList.length);
				$('#groupMemberView').show();
			}
		}
	});
//	if(flag){//如果是群组,管理成员
//		$('#groupName','#managerGroup').val(curRoomName);
//		//标识已添加到"已选择联系人"
//		$('.contact-selected').remove();
//		$('#select-group-list-manager li').each(function(){
//			var userid = $(this).attr('user-id');
//			var $contactli = $('.group-list').find('li[user-id="'+ userid +'"] div');
//			$contactli.append('<span class="contact-selected" style="left: 370px;top: 28px;"><i class="fa fa-check-square fa-lg"></i></span>');
//		});
//		$('#managerGroup').show();
//	}else{//不是群组,查看群组成员
//		$('#groupMemberView').show();
//	}
}
var userList;
var dataList;
function GetContact() {
    $.getJSON('/user/getAddressBookPc.do', function(data) {
    	isFirstLoad = false;
    	data.nb_role = nb_role;
		data.nb_role2 = nb_role2;
		data.nb_role3 = nb_role3;
    	var html = template('contact-group-template', data);
        $('.new_center_main_popup_main_LT ul').html(html);
      
        if (data) {
        	dataList = data;
        	userList = data.userList;
        	
        }
    });
}
function addToCharGroup(obj,userid,userImg,userName){
	if($.inArray(userid,selectedContact) < 0){
		selectedContact.push(userid);
		var list = '<li user-id="'+ userid +'"><div class="new_center_main_popup_main_RT_III"><img src="'+ userImg +'"><div>'+ userName +'</div><span onclick="removeFromSelect(this)">x</span></div></li>';

		if(!$('#newGroup').is(':hidden')){
			$('#select-group-list').append(list);
			$('#selected-contact-count').html($('#select-group-list').children('li').length);
		}else{
			$('#select-group-list-manager').append(list);
			$('#selected-contact-count-manager').html($('#select-group-list-manager').children('li').length);
		}
		$(obj).append('<span class="contact-selected" style="left: 370px;top: 28px;"><i class="fa fa-check-square fa-lg"></i></span>');
	}
}

function removeFromSelect(obj){
	var removeUserid = $(obj).closest('li').attr('user-id');
	selectedContact.splice($.inArray(removeUserid,selectedContact),1);
	$(obj).closest('li').remove();
	$('.group-list').find('li[user-id="'+ removeUserid +'"]').find('.contact-selected').remove();
	if(!$('#newGroup').is(':hidden')){
		$('#selected-contact-count').html($('#select-group-list').children('li').length);
	}else{
		$('#selected-contact-count-manager').html($('#select-group-list-manager').children('li').length);
	}
}
function newChartGroup(obj){
	if(checkNewGroup()){
		$(obj).after('<img src="/img/loading4.gif" style="position: absolute;left: 577px;top: 20px;">');
		selectedContact = new Array();
		$('#select-group-list li').each(function(){
			selectedContact.push($(this).attr('user-id'));
		});
		if(selectedContact == null || selectedContact.length == 0){
			alert('至少选择一个联系人哦，否则只能和自己聊天~~');
			return false;
		}
		
		$.ajax({
			url:'/groupchat/addGroup.do',
			data:{
				'groupname':$('#groupName').val().replace(/\n/g, ''),
				'useridAry':selectedContact
			},
			type:'post',
			traditional: true, 
			success:function(data){
				if (data.score) {
					scoreManager(data.scoreMsg, data.score);
				}
				$(obj).next('img').remove();
				$('#bg').hide();
				$('#newGroup').hide();
				$('#groupName').val('');
				$('#select-group-list').html('');
				selectedContact = new Array();
				//getChatGroupList();
				/*if(conn){
					handleOpen(conn);
				}*/
				getChatGroupList();
			}
		});
	}
}
function checkNewGroup(){
	var groupname = '';
	if($('#managerGroup').is(':hidden')){
		groupname = $('#groupName','#newGroup').val();
	}else{
		groupname = $('#groupName','#managerGroup').val();
	}
	if(groupname == null || groupname == ''){
		alert('群组名称不能为空~');
		return false;
	}
	if(groupname.length > 10){
		alert('群组名称太长了不好记,须在10个字符以内哦~');
		return false;
	}
	return true;
}
function getChatGroupList(){
	$.ajax({
		url:'/groupchat/selGroup.do',
		type:'get',
		dataType:'json',
		success: function(data){
			//showChatUI();
			createContactlistUL();
			$('#contactlistUL').html('');
			for(var i in data){
				hashroom(data[i].roomid);
				$('#contactlistUL').append('<li onclick="chooseContactDivClick(this)" id="group&amp;-'+data[i].roomid + '" class="offline" classname="offline" type="groupchat" displayname="'+data[i].groupname+'" roomid="'+data[i].roomid + '" joined="false"><i class="fa fa-wechat fa-2x"></i><span>'+data[i].groupname+'</span><span class="notreadMsg-count"></span></span></li>');
			}
			//获取每个roomid 的历史聊天记录
			getLatestChatRecords();
		}
		
	});
}

function hashroom(roomId) {
	$.ajax({
		url: '/groupchat/isGroupMain.do',
		data: {roomid: roomId},
		dataType: "json",
		type: 'post',
		success: function (data) {
			if (data.groupmemberList) {
				if (myhash[roomId] == null) {
					myhash[roomId] = data.groupmemberList;
				}
			}
		}
	});
}
function getLatestChatRecords(){
	$.ajax({

		url:'/groupchat/selChatList.do',
		/*data:{
			'roomid':roomid
		},*/
		type:'post',
		dataType:'json',
		success: function(data){
			if(data){
				for (var i in data) {
					var localMsg = null;
					if (data[i]!=null) {
						if (data[i].groupchatList!=null && data[i].groupchatList.size!=0) {
							var chatdata = data[i].groupchatList;
							for (var j in chatdata) {
								localMsg = Easemob.im.Helper.parseTextMessage(chatdata[j].chatContent);
								localMsg = localMsg.body;
								chatdata[j].chatContentList = localMsg;
							}
						}
						//在列表绑定未读个数
						var notviewcount = data[i].notviewcount < 100 ? (data[i].notviewcount > 0 ? data[i].notviewcount:''): '99<b>+</b>';
						$('li[roomid="'+ data[i].roomid +'"] .notreadMsg-count').html(notviewcount);
					}
					lastRecord[data[i].roomid] = data[i];
				};
			}
		},error:function(){

		}
	});
}
function switchChatPanel(){
	var panelWidth = $('.center_main_chat').width();
	$('.center_main_chat').animate({left:panelWidth});
}
function openChatPanel(){
	$('#bg').show();
	$('#chating-panel').show();
}
var myhash = new Array();
function handleData(roomId){
	$.ajax({
		url:'/groupchat/isGroupMain.do',
		data:{roomid:roomId},
		dataType: "json",
		type:'post',
		success:function(data){
//			selectedContact = new Array();
			
			if(data.groupmemberList){
				var $btn1 = $('.ctb04');
				var $btn2 = $('.ctb05');
				if (myhash[roomId]==null) {
					myhash[roomId] = data.groupmemberList;
				}
				$('#group-member-list').html('');
				//成员列表
				for(var i = 0; i < data.groupmemberList.length; i++){
					var temp = data.groupmemberList[i];
//					selectedContact.push(temp.userid);
					if(!temp.isgroupmain){
						var item_temp = '<li user-id="'+ temp.userid +'"><div class="chat-member-item notgroupmain"><img src="'+ temp.maxImageURL +'"><span class="chat-member-name">'+ temp.userName +'</span></div></li>';
						//当前用户排第一
						if(temp.userid == userId){
							if($('#group-member-list .groupmain').length > 0){
								$('#group-member-list .groupmain').after(item_temp);
							}else{
								$('#group-member-list').prepend(item_temp);
							}
						}else{
							$('#group-member-list').append(item_temp);
						}
					}else{
						
						//群主第一
						$('#group-member-list').prepend('<li user-id="'+ temp.userid +'" class="groupmain"><div class="chat-member-item"><img src="/img/user_female.gif" title="群组" style="height: 16px;width: 16px;"/><img src="'+ temp.maxImageURL +'"><span class="chat-member-name">'+ temp.userName +'</span></div></li>');
					}
				}
				$('#member-count').html(data.groupmemberList.length);

				//群主管理成员
				if(data.isgroupmain){
					isGroupMain = true;
					$btn1.attr('onclick',"managerGroup(true,"+"\'"+roomId+"\'"+")");
					$btn1.find('span').html('成员管理');
					$btn2.attr('onclick',"dissolveGroup(1)");
					$btn2.find('span').html('解散群组');
					
//					$('#select-group-list-manager').html('');
//					for(var i = 0; i < data.groupmemberList.length; i++){
//						var temp = data.groupmemberList[i];
//						selectedContact.push(temp.userid);
//						if(!temp.isgroupmain){
//							$('#select-group-list-manager').append('<li user-id="'+ temp.userid +'"><div class="new_center_main_popup_main_RT_III"><img src="'+ temp.maxImageURL +'"><div>'+ temp.userName +'</div><span onclick="removeFromSelect(this)">x</span></div></li>');
//						}else{
//							curGroupMainId = parseInt(temp.userid);
//						}
//					}
//					$('#selected-contact-count-manager').html(data.groupmemberList.length-1);
				}//非群主查看成员
				else{
					isGroupMain = false;
					$btn1.attr('onclick',"managerGroup(false,"+"\'"+roomId+"\'"+")");
					$btn1.find('span').html('群组成员');
					$btn2.attr('onclick',"dissolveGroup(2)");
					$btn2.find('span').html('退出群组');
					
//					$('#group-member-list-view').html('');
//					for(var i = 0; i < data.groupmemberList.length; i++){
//						var temp = data.groupmemberList[i];
//						selectedContact.push(temp.userid);
//						if(!temp.isgroupmain){
//							$('#group-member-list-view').append('<li user-id="'+ temp.userid +'"><div class="new_center_main_popup_main_SUB_II"><img src="'+ temp.maxImageURL +'"><span>'+ temp.userName +'</span></div></li>');
//						}else{
//							$('#manager-info span').html(temp.userName);
//							$('#manager-info img').attr('src',temp.maxImageURL);
//						}
//					}
//					$('#selected-contact-count').html(data.groupmemberList.length);
				}
			}
		}
	});
}
function updateGroup(obj){
	if(checkNewGroup()){
		selectedContact = new Array();
		$(obj).after('<img src="/img/loading4.gif" style="position: absolute;left: 577px;top: 20px;">');
		$('#select-group-list-manager li').each(function(){
			selectedContact.push($(this).attr('user-id'));
		});
		if(selectedContact == null || selectedContact.length == 0){
			alert('至少选择一个联系人哦，否则只能和自己聊天~~');
			return false;
		}
		$.ajax({
			url:'/groupchat/updateGroup.do',
			data:{
				groupname:$('#groupName','#managerGroup').val().replace(/\n/g, ''),
				roomid:curRoomId,
				'useridAry':selectedContact
			},
			type:'post',
			traditional:true,
			success:function(){
				$('#bg').hide();
				$('#managerGroup').hide();
				$(obj).next('img').remove();
				/*if(conn){
					handleOpen(conn);
				}*/
				getChatGroupList();
			},error:function(){
				alert('服务器错误!');
			}
		});
	}
}
//type == 1 解散群 ; type == 2 退出群
function dissolveGroup(type){
	if(type == 1){
		$('#dialog-msg','#quit-confirm-dialog').html("确定要解散这个群?");
		$('#confirmQuit','#quit-confirm-dialog').attr('onclick','confirmDissolve(1)');
	}else{
		$('#dialog-msg','#quit-confirm-dialog').html("确定要退出这个群?");
		$('#confirmQuit','#quit-confirm-dialog').attr('onclick','confirmDissolve(2)');
	}
	$('#quit-confirm-dialog div').show();
	$('#quit-confirm-dialog').show();
	$('#bg').show();
}
function confirmDissolve(type){
	$.ajax({
		url:'/groupchat/deleteGroup.do',
		data:{
			roomid:curRoomId,
			type:type
		},
		type:'post',
		success:function(){
			$('.chatRight').hide();
			$('.center_main_popup_main_XL').toggle();
			$('#quit-confirm-dialog').hide();
			$('#bg').hide();
			/*if(conn){
				handleOpen(conn);
			}*/
			getChatGroupList();
		}
	});
}
 function showfilelist(page,size) {
	 $.ajax({
			url:'/groupchat/selFileList.do',
			data:{
				roomid:curRoomId,
				page:page,
				size: size
			},
			type:'post',
			dataType:'json',
			success:function(data){
				$('#bg').show();
				if(data.content){
					$.each(data.content,function(index,content){
						if (content.uploadUserid == userId || isGroupMain){
							content.deleteable = true;
						}else{
							content.deleteable = false;
						}
					});
				}
				if(page == 0){
					$('#chat-file-list').html(template('chat-file-list-template', {data:data.content}));
				}else{
					$('#chat-file-list').append(template('chat-file-list-template', {data:data.content}));
				}
				$('#chat-file-list').next('span').remove();
				if(data.number*data.size + data.numberOfElements < data.totalElements){
					$('#chat-file-list').after('<span style="width: 100%;text-align: center;cursor: pointer;" onclick="showfilelist('+ (page+1) +',8)">加载更多<i class="fa fa-angle-double-down"></i></span>');
				}
				$('#file-total').html(data.totalElements);
			}
		});
	 $('#group-file-list').show();
	 renderTool();
 }

 //上传文件到群组
 function renderTool(){
	 $('.upload-file').fileupload({
			url: '/groupchat/upload.do',
			paramName: 'file',
			formData:{roomid:curRoomId},
			done: function (e, response) {
				MessageBox("上传成功!", 1);
				$('#chat-file-list').html('');
				showfilelist(0,8);
				//发送消息到群,通知who上传了what文件
				if(conn){
					if(response.files && response.files.length > 0){
						for(var i = 0; i < response.files.length; i ++){
							var options = {
								to : curRoomId,
								msg : nb_username+'上传了'+response.files[i].name,
								type : "groupchat"
							};
							
							//easemobwebim-sdk发送文本消息的方法 to为发送给谁，meg为文本消息对象
							conn.sendTextMessage(options);
						}
					}
					
				}
			},
			progressall:function(e, data){
				MessageBox('上传中', 0);
			}
		});
 }
 //下载群组中的文件
 function downloadfile(fileId,path,obj){
	 window.open(path);
	 $.ajax({
			url:'/groupchat/download.do',
			data:{
				fileid:fileId
			},
			type:'post',
			success:function(data){
				//if(data.flag){
					/*$(obj).removeAttr('onclick').html('<a href="file:///'+ data.path +'" target="_blanck">打开</a>');
					var $downloatCount = $(obj).parent().prev().find('.downloadtimes');
					$downloatCount.html(parseInt($downloatCount.html()) + 1);
					$(obj).parent().next().remove();*/
					//window.location.href = data.filepath;
				//}
			}
	});
 }
 //删除文件
 function deletefile(fileId,obj){
	$.ajax({
		url:'/groupchat/deletefile.do',
		data:{
			id:fileId
		},
		type:'post',
		success:function(data){
			$(obj).closest('li').remove();
			$('#file-total').html(parseInt($('#file-total').html()) -1);
		}
	});
 }
 //群组聊天列表显示/隐藏
 function showSwitch(){
	 var _left = $('#leftcontact').position().left;
	 var winWidth = $(window).width();
	 var _right = winWidth - _left;
	 if(_right > 20){
		 $('#show-panel-switch').animate({right:'2px'});
		 $('#show-panel-switch').html('<i class="fa fa-angle-double-left"></i>');
		 $('#leftcontact').animate({right: '-185px'});
	 }else{
		 $('#show-panel-switch').animate({right:'190px'});
		 $('#show-panel-switch').html('<i class="fa fa-angle-double-right"></i>');
		 $('#leftcontact').animate({right: '0px'});
	 } 
 }
 //添加整组联系人到群组
 function addAllToGroup(obj){
	 $(obj).parent().next('.group-list').children('li').each(function(){
		 $(this).children('div').trigger('click');
	 });
 /*    $(obj).parent().siblings("ul").fadeToggle();
     if($(obj).attr("src")=="/img/jiahao_2.png"){
        $(obj).attr("src","/img/jihao_2.png")
     }else{
         $(obj).attr("src","/img/jiahao_2.png")
     }*/
 }
 
 function seachkey(keyword) {
	 //var keyword = $('#seachkeyword').val();
	 var seachList = new Array();
	 var j = 0;
	 if (keyword!='') {
		 for (var i = 0;i<userList.length;i++) {
		 		var name = userList[i].nickName;
		 		if (name.toLowerCase().indexOf(keyword.toLowerCase())>-1) {
		 			seachList[j] = userList[i];
		 			j++;
		 		}
		 	}
		 	$('.new_center_main_popup_main_LT ul').html(template('contact-group-seach-template', {seachList:seachList})); 
	 } else {
		 $('.new_center_main_popup_main_LT ul').html(template('contact-group-template', dataList));
	 }	
 }
 //获取历史消息
 var hisCurPage = 0;//当前页面
 var hisTotalPages = 0;
 function getChatHistoryRecords(page,size,obj,chatid){
 	if($(obj).hasClass('cursor-notallow')){
 		return;
 	}
 	if(!curRoomId){
 		return;
 	}
 	$.ajax({
 		url:'/groupchat/selHistoryChatList.do',
 		data:{
 			'roomid':curRoomId,
 			'page':page,
			'size': size
 		},
 		type:'post',
 		dataType:'json',
 		success: function(data){
 			var tempDate = null;
 			if(data.content){
 				$('#history-count').html(data.totalElements);
 				hisTotalPages = data.totalPages;
 				hisCurPage = data.number;
 				$.each(data.content,function(i,value){
 	 				var thisDate = value.sendDate.substring(0,10);
 	 				if(!tempDate){
 	 					tempDate = thisDate;
 	 					value.border = true;
 	 					value.borderdate = tempDate.replace(new RegExp("/", 'g'), "-");
 	 				}
 	 				if(tempDate < thisDate){//
 	 					tempDate = thisDate;
 	 					value.border = true;
 	 					value.borderdate = tempDate.replace(new RegExp("/", 'g'), "-");
					}
 	 			});
 	 			$('#his-page-numb').html(hisCurPage+1);
 	 			$('.chat-hist-btns > i').removeClass('cursor-notallow');
				if (hisTotalPages==1) {
					$('.fa-angle-double-right,.fa-angle-right').addClass('cursor-notallow');
					$('.fa-angle-double-left,.fa-angle-left').addClass('cursor-notallow');
				} else {
					if(hisCurPage == 0){
						$('.fa-angle-double-right,.fa-angle-right').addClass('cursor-notallow');
					}else if(hisCurPage == hisTotalPages-1){
						$('.fa-angle-double-left,.fa-angle-left').addClass('cursor-notallow');
					}
				}

 	 			
 				var content = data.content;
 				if (content && content.length>0) {
 					for (var i in content) {
							localMsg = Easemob.im.Helper.parseTextMessage(content[i].chatContent);
							localMsg = localMsg.body;
							content[i].chatContentList = localMsg;
 					}
 				}
 				$('#chat-history-list').html(template('chat-history-list-template',{data:content}));
 				var e=document.getElementById("chat-history-list");
				e.scrollTop=e.scrollHeight;
 			}else{
 				$('#chat-history-list').html('无历史记录');
 			}
 			$('#chat-history-list').attr('list-data-id',curRoomId);
 		},error: function(){

 		}
 	});
 }

 function viewMoreRecords(page,size,obj,chatid){
 	if(!curRoomId){
 		return;
 	}
 	$.ajax({
 		url:'/groupchat/selHistoryChatList.do',
 		data:{
 			'roomid':curRoomId,
 			'chatid':chatid,
 			'page':page,
			'size': size
 		},
 		type:'post',
 		dataType:'json',
 		success: function(data){
 			var content = data.content;
				if (content && content.length>0) {
					for (var i in content) {
						localMsg = Easemob.im.Helper.parseTextMessage(content[i].chatContent);
					localMsg = localMsg.body;
					content[i].chatContentList = localMsg;
					}
				}
 			$(obj).after(template('chat-latest-list-template',{data:content}));
 			if(data.totalPages > data.number+1){
 				var curChatId = $(obj).next().attr('chat-id');
 				$(obj).attr('onclick','viewMoreRecords(0,30,this,'+ curChatId+')');
 			}else{
				$(obj).attr('onclick','');
				$(obj).html('<span>没有更多消息了~</span>');
 			}
 			
 		},error: function(){

 		}
 	});
 }

 function viewChatHis(obj){
 	$(obj).toggleClass('chat02_title_btn_active');
 	$('#member-list-container').toggle();
 	$('#history-list-container').toggle();
 	$('#chat-history-list').html('');
 	getChatHistoryRecords(0,30);
 }
 //首次点开一个room时显示当前聊天消息和部分历史消息
 function getLastRecord(){

 }