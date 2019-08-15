<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<link rel="stylesheet" type="text/css" href="/static/easymob/easymob-webim1.0/css/webim.css" />
<!-- <link rel="stylesheet" type="text/css" href="/business/easymob-webim1.0/css/bootstrap.css" /> -->
<script type="text/javascript">
	var curUserId = userId;
	var curChatUserId = null;
	var isGroupMain = false;//是否群组
	var curRoomId = null;
	var curRoomName = null;
	var msgCardDivId = "chat01";
	var talkToDivId = "talkTo";
	var talkInputId = "talkInputId";
	var fileInputId = "fileInput";
	var bothRoster = [];
	var toRoster = [];
	var unknowContact = {};
	var maxWidth = 200;
	var listRoom = [];
	var listRoomId = [];
	var reSendCount = 5;

	var curContentTypeFlag = null;
	var groupFlagMark = "group&-";
	var groupQuering = false;
	var textSending = false;

	window.URL = window.URL || window.webkitURL || window.mozURL
			|| window.msURL;
	var getLoginInfo = function() {
		return {
			isLogin : false
		};
	};
	var showLoginUI = function() {
		$('#loginmodal').modal('show');
		$('#username').focus();
	};
	var hiddenLoginUI = function() {
		$('#loginmodal').modal('hide');
	};
	var showWaitLoginedUI = function() {
		//$('#waitLoginmodal').modal('show');
		$('#waitLoginmodal').show();
	};
	var hiddenWaitLoginedUI = function() {
		//$('#waitLoginmodal').modal('hide');
		$('#waitLoginmodal').hide();
		$('#bg').hide();
	};
	var showChatUI = function() {
		$('#content').css({
			"display" : "block"
		});
		var login_userEle = document.getElementById("login_user").children[0];
		login_userEle.innerHTML = '群组交流';
		//login_userEle.setAttribute("title", curUserId);
	};
	//登录之前不显示web对话框
	var hiddenChatUI = function() {
		$('#content').css({
			"display" : "none"
		});
		document.getElementById(talkInputId).value = "";
	};
	//定义消息编辑文本域的快捷键，enter和ctrl+enter为发送，alt+enter为换行
	//控制提交频率
	$(function() {
// 		 $('#show-panel-switch').animate({right:'2px'});
// 		 $('#show-panel-switch').html('<i class="fa fa-angle-double-left"></i>');
// 		 $('#leftcontact').animate({right: '-185px'});
		getChatGroupList();
		$("textarea").keydown(function(event) {

			if (event.altKey && event.keyCode == 13) {
				e = $(this).val();
				$(this).val(e + '\n');
			} else if (event.ctrlKey && event.keyCode == 13) {
				//e = $(this).val();
				//$(this).val(e + '<br>');
				event.returnValue = false;
				sendText();
				return false;
			} else if (event.keyCode == 13) {
				event.returnValue = false;
				sendText();
				return false;
			}

		});
		$('body').on('click','li.offline',function(){
			if($('.chatRight').is(':hidden')){
				showContactChatDiv($(this).attr('id'));
				$('.chatRight').show();
			}
		});
		//发送文件的模态窗口
		$('#fileModal').on('hidden.bs.modal', function(e) {
			document.getElementById("fileSend").disabled = false;
			document.getElementById("cancelfileSend").disabled = false;

			if(!Easemob.im.Helper.isCanUploadFileAsync) return;
			var ele = document.getElementById(fileInputId);
			ele.value = "";
			if (!window.addEventListener) {
				ele.outerHTML = ele.outerHTML;
			}
		});
	});
	
	//处理连接时函数,主要是登录成功后对页面元素做处理
	var handleOpen = function(conn) {
		//从连接中获取到当前的登录人注册帐号名
		curUserId = conn.context.userId;
		//获取当前登录人的联系人列表
		conn.getRoster({
			success : function(roster) {
				// 页面处理
				hiddenWaitLoginedUI();
				showChatUI();
				createContactlistUL();
				
				var curroster;
				for ( var i in roster) {
					var ros = roster[i];
					//both为双方互为好友，要显示的联系人,from我是对方的单向好友
					if (ros.subscription == 'both'
							|| ros.subscription == 'from') {
						bothRoster.push(ros);
					} else if (ros.subscription == 'to') {
						//to表明了联系人是我的单向好友
						toRoster.push(ros);
					}
				}
				/* if (bothRoster.length > 0) {
					curroster = bothRoster[0];
					buildContactDiv("contractlist", bothRoster);//联系人列表页面处理
					if (curroster)
						setCurrentContact(curroster.name);//页面处理将第一个联系人作为当前聊天div
				} */
				conn.setPresence();
				//chooseContactDivClick($('#contractlist li')[0]);
				// $('#contractlist li:first-child').trigger('click');
				// $('.chatRight').hide();
				chooseContactDivClick($('#contractlist li')[0]);

				//获取当前登录人的群组列表
// 				conn.listRooms({
// 					success : function(rooms) {
// 						if (rooms) {
// 							var listRoom = rooms;
// 							buildListRoomDiv("contractlist", rooms);//群组列表页面处理
// 							//获取联系人列表
// 							GetContact();
// 						}
// 					},
// 					error : function(e) {

// 					}
// 				});
			}
		});

	};

	//连接中断时的处理，主要是对页面进行处理
	var handleClosed = function() {
		curUserId = null;
		curChatUserId = null;
		curRoomId = null;
		bothRoster = [];
		toRoster = [];
		listRoom = [];
		listRoomId = [];
		hiddenChatUI();
		clearContactUI("contractlist", msgCardDivId);
		unknowContact = {};
		showLoginUI();
		groupQuering = false;
		textSending = false;
	};
	//easemobwebim-sdk中收到联系人订阅请求的处理方法，具体的type值所对应的值请参考xmpp协议规范
	var handlePresence = function(e) {
		//（发送者希望订阅接收者的出席信息），即别人申请加你为好友
		if (e.type == 'subscribe') {
			if (e.status) {
				if (e.status.indexOf('resp:true') > -1) {
					agreeAddFriend(e.from);
					return;
				}
			}
			var subscribeMessage = e.from + "请求加你为好友。\n验证消息：" + e.status;
			showNewNotice(subscribeMessage);
			$('#confirm-block-footer-confirmButton').click(function() {
				//同意好友请求
				agreeAddFriend(e.from);//e.from用户名
				//反向添加对方好友
				conn.subscribe({
					to : e.from,
					message : "[resp:true]"
				});
				$('#confirm-block-div-modal').modal('hide');
			});
			$('#confirm-block-footer-cancelButton').click(function() {
				rejectAddFriend(e.from);//拒绝加为好友
				$('#confirm-block-div-modal').modal('hide');
			});
			return;
		}
		//(发送者允许接收者接收他们的出席信息)，即别人同意你加他为好友
		if (e.type == 'subscribed') {
			toRoster.push({
				name : e.from,
				jid : e.fromJid,
				subscription : "to"
			});
			return;
		}
		//（发送者取消订阅另一个实体的出席信息）,即删除现有好友
		if (e.type == 'unsubscribe') {
			//单向删除自己的好友信息，具体使用时请结合具体业务进行处理
			delFriend(e.from);
			return;
		}
		//（订阅者的请求被拒绝或以前的订阅被取消），即对方单向的删除了好友
		if (e.type == 'unsubscribed') {
			delFriend(e.from);
			return;
		}
	};
	//easemobwebim-sdk中处理出席状态操作
	var handleRoster = function(rosterMsg) {
		for ( var i = 0; i < rosterMsg.length; i++) {
			var contact = rosterMsg[i];
			if (contact.ask && contact.ask == 'subscribe') {
				continue;
			}
			if (contact.subscription == 'to') {
				toRoster.push({
					name : contact.name,
					jid : contact.jid,
					subscription : "to"
				});
			}
			//app端删除好友后web端要同时判断状态from做删除对方的操作
			if (contact.subscription == 'from') {
				toRoster.push({
					name : contact.name,
					jid : contact.jid,
					subscription : "from"
				});
			}
			if (contact.subscription == 'both') {
				var isexist = contains(bothRoster, contact);
				if (!isexist) {
					var newcontact = document.getElementById("contactlistUL");
					var lielem = document.createElement("li");
					lielem.setAttribute("id", contact.name);
					lielem.setAttribute("class", "offline");
					lielem.setAttribute("className", "offline");
					lielem.onclick = function() {
						chooseContactDivClick(this);
					};
					/*var imgelem = document.createElement("img");
					imgelem.setAttribute("src", "/img/youjian_2.png");
					lielem.appendChild(imgelem);*/
					$(lielem).append('<i class="fa fa-wechat fa-2x"></i>');
					var spanelem = document.createElement("span");

					spanelem.innerHTML = contact.name;
					lielem.appendChild(spanelem);

					newcontact.appendChild(lielem);
					bothRoster.push(contact);
				}
			}
			if (contact.subscription == 'remove') {
				var isexist = contains(bothRoster, contact);
				if (isexist) {
					removeFriendDomElement(contact.name);
				}
			}
		}
	};
	//异常情况下的处理方法
	var handleError = function(e) {
		if (curUserId == null) {
			hiddenWaitLoginedUI();
			//alert(e.msg + ",请重新登录");
			showLoginUI();
		} else {
			var msg = e.msg;
			if (e.type == EASEMOB_IM_CONNCTION_SERVER_CLOSE_ERROR) {
				if (msg == "") {
					alert("服务器断开连接,可能是因为在别处登录");
				} else {
					alert("服务器断开连接");
				}
			} else {
				alert(msg);
			}
		}
	};
	//判断要操作的联系人和当前联系人列表的关系
	var contains = function(roster, contact) {
		var i = roster.length;
		while (i--) {
			if (roster[i].name === contact.name) {
				return true;
			}
		}
		return false;
	};
	
	Object.defineProperty(Array.prototype, 'indexOf', {
		enumerable: false,
		value: function(val) {
			for ( var i = 0; i < this.length; i++) {
				if (this[i].name == val.name)
					return i;
			}
			return -1;
		}
	});
	
	Object.defineProperty(Array.prototype, 'remove', {
		enumerable: false,
		value: function(val) {
			var index = this.indexOf(val);
			if (index > -1) {
				this.splice(index, 1);
			}
		}
	});

	var logout = function() {
		conn.close();
	};

	//设置当前显示的聊天窗口div，如果有联系人则默认选中联系人中的第一个联系人，如没有联系人则当前div为null-nouser
	var setCurrentContact = function(defaultUserId) {
		showContactChatDiv(defaultUserId);
		if (curChatUserId != null) {
			hiddenContactChatDiv(curChatUserId);
		} else {
			$('#null-nouser').css({
				"display" : "none"
			});
		}
		curChatUserId = defaultUserId;
	};

	var createContactlistUL = function() {
		var contactListUL = document.getElementById("contactlistUL");
		if (contactListUL != null) {
			return;
		}
		var uielem = document.createElement("ul");
		$(uielem).attr({
			"id" : "contactlistUL",
			"class" : "chat03_content_ul"
		});
		var contactlist = document.getElementById("contractlist");
		contactlist.appendChild(uielem);
	};

	//构造联系人列表
	var buildContactDiv = function(contactlistDivId, roster) {
		var uielem = document.getElementById("contactlistUL");
		var cache = {};
		for (i = 0; i < roster.length; i++) {
			if (!(roster[i].subscription == 'both' || roster[i].subscription == 'from')) {
				continue;
			}
			var jid = roster[i].jid;
			var userName = jid.substring(jid.indexOf("_") + 1).split("@")[0];
			if (userName in cache) {
				continue;
			}
			cache[userName] = true;
			var lielem = document.createElement("li");
			$(lielem).attr({
				'id' : userName,
				'class' : 'offline',
				'className' : 'offline',
				'chat' : 'chat',
				'displayName' : userName
			});
			lielem.onclick = function() {
				chooseContactDivClick(this);
			};
			/* var imgelem = document.createElement("img");
			imgelem.setAttribute("src", "/img/youjian_2.png");
			lielem.appendChild(imgelem); */
			$(lielem).append('<i class="fa fa-wechat fa-2x"></i>');

			var spanelem = document.createElement("span");
			spanelem.innerHTML = userName;
			lielem.appendChild(spanelem);

			uielem.appendChild(lielem);
		}
		var contactlist = document.getElementById(contactlistDivId);
		var children = contactlist.children;
		if (children.length > 0) {
			contactlist.removeChild(children[0]);
		}
		contactlist.appendChild(uielem);
	};

	//构造群组列表
	buildListRoomDiv = function(contactlistDivId, rooms) {
		var uielem = document.getElementById("contactlistUL");
		uielem.innerHTML = '';
		var cache = {};
		for (i = 0; i < rooms.length; i++) {
			var roomsName = rooms[i].name;
			var roomId = rooms[i].roomId;
			listRoomId.push(roomId);
			if (roomId in cache) {
				continue;
			}
			cache[roomId] = true;
			var lielem = document.createElement("li");
			$(lielem).attr({
				'id' : groupFlagMark + roomId,
				'class' : 'offline',
				'className' : 'offline',
				'type' : 'groupchat',
				'displayName' : roomsName,
				'roomId' : roomId,
				'joined' : 'false'
			});
			$(lielem).click(function() {
				chooseContactDivClick(this);
			});
			//$(lielem).append('<img src="/img/youjian_2.png"/>');
			$(lielem).append('<i class="fa fa-wechat fa-2x"></i>');
			$(lielem).append('<span class="WW">' + roomsName + '<span>');

			uielem.appendChild(lielem);
		}
		var contactlist = document.getElementById(contactlistDivId);
		var children = contactlist.children;
		if (children.length > 0) {
			contactlist.removeChild(children[0]);
		}
		contactlist.appendChild(uielem);
	};

	//选择联系人的处理
	var getContactLi = function(chatUserId) {
		return document.getElementById(chatUserId);
	};

	//构造当前聊天记录的窗口div
	var getContactChatDiv = function(chatUserId) {
		return document.getElementById(curUserId + "-" + chatUserId);
	};

	//如果当前没有某一个联系人的聊天窗口div就新建一个
	var createContactChatDiv = function(chatUserId) {
		var msgContentDivId = curUserId + "-" + chatUserId;
		var newContent = document.createElement("div");
		newContent.setAttribute("id", msgContentDivId);
		newContent.setAttribute("class", "chat01_content");
		newContent.setAttribute("className", "chat01_content");
		newContent.setAttribute("style", "display:none");
		newContent.setAttribute("style", "overflow: auto !important");
		return newContent;
	};

	//显示当前选中联系人的聊天窗口div，并将该联系人在联系人列表中背景色置为蓝色
	var showContactChatDiv = function(chatUserId) {
		var contentDiv = getContactChatDiv(chatUserId);
		if (contentDiv == null) {
			contentDiv = createContactChatDiv(chatUserId);
			//如果未登录成功
			/*if (curUserId == null) {

			}*/
			var chatRoomId = parseInt(chatUserId.split('&-')[1]);
			if(curUserId && chatRoomId && lastRecord[chatRoomId]){
				$(contentDiv).html(template('chat-latest-list-template',{data:lastRecord[chatRoomId].groupchatList}));	
				
				if(lastRecord[chatRoomId].groupchatList && lastRecord[chatRoomId].groupchatList.length > 0){
					$(contentDiv).append('<div style="color: #aaa;"><span>------</span><span>以上是历史消息</span>------<span></span></div>');
					var curChatId = $(contentDiv).children().first().attr('chat-id');
					if(lastRecord[chatRoomId].flag){
						$(contentDiv).prepend('<span class="chat-viewmore-his" onclick="viewMoreRecords(0,30,this,'+ curChatId+')"><img src="/img/time.gif"><span>查看更多消息</span></span>');
					}
				}				
			}
			var talkDiv = document.getElementById('null-nouser');
			$(talkDiv).after(contentDiv);
		}
		//在滚动条底部
		$(contentDiv).scrollTop (10000);
		contentDiv.style.display = "block";
		var contactLi = document.getElementById(chatUserId);
		if (contactLi == null) {
			return;
		}
		contactLi.style.backgroundColor = "#f1faf2";
		$(contactLi).find('.notreadMsg-count').html('');
		//$('.notreadMsg-count').html('');
		var dispalyTitle = null;//聊天窗口显示当前对话人名称
		if (chatUserId.indexOf(groupFlagMark) >= 0) {
			dispalyTitle = "群组" + $(contactLi).attr('displayname') + "聊天中";
			curRoomId = $(contactLi).attr('roomid');
			curRoomName = $(contactLi).attr('displayname');
			$("#roomMemberImg").css('display', 'block');
		} else {
			dispalyTitle = "与" + chatUserId + "聊天中";
			$("#roomMemberImg").css('display', 'none');
		}

		document.getElementById(talkToDivId).children[0].innerHTML = dispalyTitle;
	};
	//对上一个联系人的聊天窗口div做隐藏处理，并将联系人列表中选择的联系人背景色置空
	var hiddenContactChatDiv = function(chatUserId) {
		var contactLi = document.getElementById(chatUserId);
		if (contactLi) {
			contactLi.style.backgroundColor = "";
		}
		var contentDiv = getContactChatDiv(chatUserId);
		if (contentDiv) {
			contentDiv.style.display = "none";

		}

	};
	//切换联系人聊天窗口div
	var chooseContactDivClick = function(li) {
		var chatUserId = li.id;
		if(curUserId){
			$('div[id^="null-group&-"]').remove();
			showContactChatDiv(chatUserId);
		}
		if ($(li).attr("type") == 'groupchat'
				&& ('true' != $(li).attr("joined"))) {
			conn.join({
				roomId : $(li).attr("roomId")
			});
			$(li).attr("joined", "true");
		}
		if (chatUserId != curChatUserId) {
			if (curChatUserId == null) {
				showContactChatDiv(chatUserId);
			} else {
				showContactChatDiv(chatUserId);
				hiddenContactChatDiv(curChatUserId);
			}
			curChatUserId = chatUserId;
		}
		$.ajax({
			url:'/groupchat/updateStatus.do',
			data:{
				roomid:$(li).attr("roomId")
			},
			type:'post',
			success:function(data){
			}
		});
		//对默认的null-nouser div进行处理,走的这里说明联系人列表肯定不为空所以对默认的聊天div进行处理
		$('#null-nouser').css({
			"display" : "none"
		});
		//判断是否群组
		handleData($(li).attr("roomId"));
		//切换历史聊天记录
		if($('#chat-history-list').attr('list-data-id') != curRoomId){
			getChatHistoryRecords(0,30);
		}
	};

	var clearContactUI = function(contactInfoDiv, contactChatDiv) {
		document.getElementById(talkToDivId).children[0].innerHTML = "";
		var contactlist = document.getElementById(contactInfoDiv);
		var children = contactlist.children;
		if (children.length > 0) {
			contactlist.removeChild(children[0]);
		}
		var chatRootDiv = document.getElementById(contactChatDiv);
		var children = chatRootDiv.children;
		for ( var i = children.length - 1; i > 1; i--) {
			chatRootDiv.removeChild(children[i]);
		}
		$('#null-nouser').css({
			"display" : "block"
		});
	};
	var emotionFlag = false;
	var showEmotionDialog = function() {
		if (emotionFlag) {
			$('#wl_faces_box').css({
				"display" : "block"
			});
			return;
		}
		;
		emotionFlag = true;
		// Easemob.im.Helper.EmotionPicData设置表情的json数组
		var sjson = Easemob.im.Helper.EmotionPicData;
		for ( var key in sjson) {
			var emotionImgContent = document.createElement("img");
			emotionImgContent.setAttribute("id", key);
			emotionImgContent.setAttribute("src", sjson[key]);
			emotionImgContent.setAttribute("style", "cursor:pointer;");
			emotionImgContent.onclick = function() {
				selectEmotionImg(this);
			};
			var emotionLi = document.createElement("li");
			emotionLi.appendChild(emotionImgContent);
			document.getElementById("emotionUL").appendChild(emotionLi);
		}
		$('#wl_faces_box').css({
			"display" : "block"
		});
	};
	//表情选择div的关闭方法
	var turnoffFaces_box = function() {
		$("#wl_faces_box").fadeOut("slow");
	};
	var selectEmotionImg = function(selImg) {
		var txt = document.getElementById(talkInputId);
		txt.value = txt.value + selImg.id;
		txt.focus();
	};
	var showSendPic = function() {
		$('#fileModal').modal('toggle');
		$('#sendfiletype').val('pic');
		$('#send-file-warning').html("");
	};
	var showSendAudio = function() {
		$('#fileModal').modal('toggle');
		$('#sendfiletype').val('audio');
		$('#send-file-warning').html("");
	};

	var _interval;
	var sendText = function() {
// 		if (textSending) {
// 			return;
// 		}
		//未登录成功,增加waiting样式
		if (curUserId == null) {
			if($('#send_text').next('img').length == 0){
				$('#send_text').after('<img src="/img/loading4.gif">');
			}
			//尝试重发
			if(reSendCount > 0){
				_interval = setInterval(resendMsg,2500);
				function resendMsg(){
					sendText();
				reSendCount --;
				}
			}else{
				clearInterval(_interval);
			}
			return;
		}else{
			$('#send_text').next('img').remove();
			//删除登录前的聊天框
			$('div[id^="null-group&-"]').remove();
			clearInterval(_interval);
		}
		textSending = true;

		var msgInput = document.getElementById(talkInputId);
		var msg = msgInput.value;

		if (msg == null || msg.length == 0) {
			return;
		}
		$.ajax({
			url:'/groupchat/send.do',
			data:{
				chatContent:msg,
				roomid:curRoomId
			},
			type:'post',
			success:function(data){
			}
		});
		var to = curChatUserId;
		if (to == null) {
			return;
		}
		var options = {
			to : to,
			msg : msg,
			type : "chat",
			ext:{"attr1":nickname,"attr2":imageUrl}
		};
		// 群组消息和个人消息的判断分支
		if (curChatUserId.indexOf(groupFlagMark) >= 0) {
			options.type = 'groupchat';
			options.to = curRoomId;
		}
		//easemobwebim-sdk发送文本消息的方法 to为发送给谁，meg为文本消息对象
		conn.sendTextMessage(options);

		//当前登录人发送的信息在聊天窗口中原样显示
		var msgtext = msg.replace(/\n/g, '<br>');
		appendMsg(curUserId, to, msgtext);
		turnoffFaces_box();
		msgInput.value = "";
		msgInput.focus();

// 		setTimeout(function() {
// 			textSending = false;
// 		}, 1000);
	};

	var pictype = {
		"jpg" : true,
		"gif" : true,
		"png" : true,
		"bmp" : true
	};
	var sendFile = function() {
		var type = $("#sendfiletype").val();
		if (type == 'pic') {
			sendPic();
		} else {
			sendAudio();
		}
	};
	//发送图片消息时调用方法
	var sendPic = function() {
		var to = curChatUserId;
		if (to == null) {
			return;
		}
		// Easemob.im.Helper.getFileUrl为easemobwebim-sdk获取发送文件对象的方法，fileInputId为 input 标签的id值
		var fileObj = Easemob.im.Helper.getFileUrl(fileInputId);
		if (fileObj.url == null || fileObj.url == '') {
			$('#send-file-warning')
					.html("<font color='#FF0000'>请选择发送图片</font>");
			return;
		}
		var filetype = fileObj.filetype;
		var filename = fileObj.filename;
		if (filetype in pictype) {
			document.getElementById("fileSend").disabled = true;
			document.getElementById("cancelfileSend").disabled = true;
			var opt = {
				type : 'chat',
				fileInputId : fileInputId,
				to : to,
				onFileUploadError : function(error) {
					$('#fileModal').modal('hide');
					var messageContent = error.msg + ",发送图片文件失败:" + filename;
					appendMsg(curUserId, to, messageContent);
				},
				onFileUploadComplete : function(data) {
					$('#fileModal').modal('hide');
					var file = document.getElementById(fileInputId);
					if (file && file.files) {
						var objUrl = getObjectURL(file.files[0]);
						if (objUrl) {
							var img = document.createElement("img");
							img.src = objUrl;
							img.width = maxWidth;
							$.ajax({
								url:'/groupchat/send.do',
								data:{
									chatContent:objUrl,
									roomid:curRoomId
								},
								type:'post',
								success:function(data){
								}
							});
						}
					}
					appendMsg(curUserId, to, {
						data : [ {
							type : 'pic',
							filename : filename,
							data : img
						} ]
					});
				}
			};

			if (curChatUserId.indexOf(groupFlagMark) >= 0) {
				opt.type = 'groupchat';
				opt.to = curRoomId;
			}
			conn.sendPicture(opt);
			return;
		}
		$('#send-file-warning').html(
				"<font color='#FF0000'>不支持此图片类型" + filetype + "</font>");
	};
	var audtype = {
		"mp3" : true,
		"wma" : true,
		"wav" : true,
		"amr" : true,
		"avi" : true
	};
	//发送音频消息时调用的方法
	var sendAudio = function() {
		var to = curChatUserId;
		if (to == null) {
			return;
		}
		//利用easemobwebim-sdk提供的方法来构造一个file对象
		var fileObj = Easemob.im.Helper.getFileUrl(fileInputId);
		if (fileObj.url == null || fileObj.url == '') {
			$('#send-file-warning')
					.html("<font color='#FF0000'>请选择发送音频</font>");
			return;
		}
		var filetype = fileObj.filetype;
		var filename = fileObj.filename;
		if (filetype in audtype) {
			document.getElementById("fileSend").disabled = true;
			document.getElementById("cancelfileSend").disabled = true;
			var opt = {
				type : "chat",
				fileInputId : fileInputId,
				to : to,//发给谁
				onFileUploadError : function(error) {
					$('#fileModal').modal('hide');
					var messageContent = error.msg + ",发送音频失败:" + filename;
					appendMsg(curUserId, to, messageContent);
				},
				onFileUploadComplete : function(data) {
					var messageContent = "发送音频" + filename;
					$('#fileModal').modal('hide');
					appendMsg(curUserId, to, messageContent);
				}
			};
			//构造完opt对象后调用easemobwebim-sdk中发送音频的方法
			if (curChatUserId.indexOf(groupFlagMark) >= 0) {
				opt.type = 'groupchat';
				opt.to = curRoomId;
			}
			conn.sendAudio(opt);
			return;
		}
		$('#send-file-warning').html(
				"<font color='#FF0000'>不支持此音频类型" + filetype + "</font>");
	};
	//easemobwebim-sdk收到文本消息的回调方法的实现
	var handleTextMessage = function(message) {
		//清空登录前打开的聊天框
		$('div[id^="null-group&-"]').remove();
		var from = message.from;//消息的发送者
		var mestype = message.type;//消息发送的类型是群组消息还是个人消息
		var messageContent = message.data;//文本消息体
		//TODO  根据消息体的to值去定位那个群组的聊天记录
		var room = message.to;
		if (mestype == 'groupchat') {
			
			appendMsg(message.from, message.to, messageContent, mestype);
		} else {
			appendMsg(from, from, messageContent);
		}
	};
	//easemobwebim-sdk收到表情消息的回调方法的实现，message为表情符号和文本的消息对象，文本和表情符号sdk中做了
	//统一的处理，不需要用户自己区别字符是文本还是表情符号。
	var handleEmotion = function(message) {
		var from = message.from;
		var room = message.to;
		var mestype = message.type;//消息发送的类型是群组消息还是个人消息
		if (mestype == 'groupchat') {
			appendMsg(message.from, message.to, message, mestype);
		} else {
			appendMsg(from, from, message);
		}

	};
	//easemobwebim-sdk收到图片消息的回调方法的实现
	var handlePictureMessage = function(message) {
		var filename = message.filename;//文件名称，带文件扩展名
		var from = message.from;//文件的发送者
		var mestype = message.type;//消息发送的类型是群组消息还是个人消息
		var contactDivId = from;
		if (mestype == 'groupchat') {
			contactDivId = groupFlagMark + message.to;
		}
		var options = message;
		// 图片消息下载成功后的处理逻辑
		options.onFileDownloadComplete = function(response, xhr) {
			var objectURL = window.URL.createObjectURL(response);
			img = document.createElement("img");
			img.onload = function(e) {
				img.onload = null;
				window.URL.revokeObjectURL(img.src);
			};
			img.onerror = function() {
				img.onerror = null;
				if (typeof FileReader == 'undefined') {
					img.alter = "当前浏览器不支持blob方式";
					return;
				}
				img.onerror = function() {
					img.alter = "当前浏览器不支持blob方式";
				};
				var reader = new FileReader();
				reader.onload = function(event) {
					img.src = this.result;
				};
				reader.readAsDataURL(response);
			}
			img.src = objectURL;
			var pic_real_width = options.width;

			if (pic_real_width == 0) {
				$("<img/>").attr("src", objectURL).load(function() {
					pic_real_width = this.width;
					if (pic_real_width > maxWidth) {
						img.width = maxWidth;
					} else {
						img.width = pic_real_width;
					}
					appendMsg(from, contactDivId, {
						data : [ {
							type : 'pic',
							filename : filename,
							data : img
						} ]
					});

				});
			} else {
				if (pic_real_width > maxWidth) {
					img.width = maxWidth;
				} else {
					img.width = pic_real_width;
				}
				appendMsg(from, contactDivId, {
					data : [ {
						type : 'pic',
						filename : filename,
						data : img
					} ]
				});
			}
		};
		options.onFileDownloadError = function(e) {
			appendMsg(from, contactDivId, e.msg + ",下载图片" + filename + "失败");
		};
		//easemobwebim-sdk包装的下载文件对象的统一处理方法。
		Easemob.im.Helper.download(options);
	};
	//easemobwebim-sdk收到音频消息回调方法的实现
	var handleAudioMessage = function(message) {
		var filename = message.filename;
		var filetype = message.filetype;
		var from = message.from;

		var mestype = message.type;//消息发送的类型是群组消息还是个人消息
		var contactDivId = from;
		if (mestype == 'groupchat') {
			contactDivId = groupFlagMark + message.to;
		}
		var options = message;
		options.onFileDownloadComplete = function(response, xhr) {
			//amr 不处理播放，提供下载
			var index = filename.lastIndexOf("\.");
			if (index > 0) {
				var fileType = filename.substring(index, filename.length);
				if (".amr" == fileType.toLowerCase()) {
					var spans = "不支持的音频格式:" + filename;
					var reader = new FileReader();
					reader.onload = function(event) {
						if (navigator.userAgent.indexOf("Trident") == -1) {
							spans = spans
									+ ",请<a download='"+filename+"' href='"+event.target.result+"'>&nbsp;&nbsp;下载&nbsp;&nbsp;</a>播放";
						}
						appendMsg(from, contactDivId, spans);
					}
					reader.readAsDataURL(response);
					return;
				}
			}
			var objectURL = window.URL.createObjectURL(response);
			var audio = document.createElement("audio");
			if (("src" in audio) && ("controls" in audio)) {
				audio.onload = function() {
					audio.onload = null;
					window.URL.revokeObjectURL(audio.src);
				};
				audio.onerror = function() {
					audio.onerror = null;
					appendMsg(from, contactDivId, "当前浏览器不支持播放此音频:" + filename);
				};
				audio.controls = "controls";
				audio.src = objectURL;
				appendMsg(from, contactDivId, {
					data : [ {
						type : 'audio',
						filename : filename,
						data : audio
					} ]
				});
				audio.play();
				return;
			}
		};
		options.onFileDownloadError = function(e) {
			appendMsg(from, contactDivId, e.msg + ",下载音频" + filename + "失败");
		};
		Easemob.im.Helper.download(options);
	};

	var handleLocationMessage = function(message) {
		var from = message.from;
		var addr = message.addr;
		var ele = appendMsg(from, from, addr);
		return ele;
	};

	//显示聊天记录的统一处理方法
	var appendMsg = function(who, contact, message, chattype) {
		//who的头像
		if (who == null) {
			return;
		}
		
		var contactUL = document.getElementById("contactlistUL");
		if (contactUL.children.length == 0) {
			return null;
		}
		var cid = contact;
		if (contact.indexOf("group&-")!=-1) {
			cid = contact.replace("group&-","");
		}

		var contactDivId = contact;
		if (chattype) {
			contactDivId = groupFlagMark + contact;
		}
		var contactLi = getContactLi(contactDivId);
		if (contactLi == null) {
			if (unknowContact[contact]) {
				return;
			}
			//alert("陌生人" + who + "的消息,忽略");
			unknowContact[contact] = true;
			return null;
		}

		// 消息体 {isemotion:true;body:[{type:txt,msg:ssss}{type:emotion,msg:imgdata}]}
		var localMsg = null;
		if (typeof message == 'string') {
			localMsg = Easemob.im.Helper.parseTextMessage(message);
			localMsg = localMsg.body;
		} else {
			localMsg = message.data;
		}
		var date = new Date();
		var time = date.toLocaleTimeString();

		//根据who获取头像
		var myimg = $.grep(myhash[cid],function(value,index){
			if(value.userid == who){
				return value.maxImageURL;
			}
		});
		if (contact==curRoomId && !$(".chatRight").is(':hidden')) {
				$.ajax({
					url:'/groupchat/updateStatus.do',
					data:{
						roomid:curRoomId
					},
					type:'post',
					success:function(data){
					}
				});
			}
		/* var mypic = '';
		var myname = '';
		if(myimg != null && myimg.length > 0){
			mypic = myimg[0].maxImageURL;
			myname = myimg[0].userName;
		} */
		//"<p1 class='chat-time'>" + time + "<b></b><br/></p1>" ,
		var headstr = ["<p1 class='chat-time'>" + time + "<b></b><br/></p1>" , "<p2><img class='chat-img' src='" + myimg[0].maxImageURL + "'/>   <span></span>" + "   </p2>","<p4 class=\"chat-name\"> "+ myimg[0].userName +"</p4>"];
		var header = $(headstr.join(''));

		var lineDiv = document.createElement("div");
		for ( var i = 0; i < header.length; i++) {
			var ele = header[i];
			lineDiv.appendChild(ele);
		}
		var messageContent = localMsg;
		for ( var i = 0; i < messageContent.length; i++) {
			var msg = messageContent[i];
			var type = msg.type;
			var data = msg.data;
			if (type == "emotion") {
				var eletext = "<p3><img src='" + data + "'/></p3>";
				var ele = $(eletext);
				for ( var j = 0; j < ele.length; j++) {
					lineDiv.appendChild(ele[j]);
				}
			} else if (type == "pic") {
				var filename = msg.filename;
				var fileele = $("<p3>" + filename + "</p3><br>");
				for ( var j = 0; j < fileele.length; j++) {
					lineDiv.appendChild(fileele[j]);
				}
				lineDiv.appendChild(data);
			} else if (type == 'audio') {
				var filename = msg.filename;
				var fileele = $("<p3>" + filename + "</p3><br>");
				for ( var j = 0; j < fileele.length; j++) {
					lineDiv.appendChild(fileele[j]);
				}
				lineDiv.appendChild(data);
			} else {
				var eletext = "<p3>" + data + "</p3>";
				var ele = $(eletext);
				ele[0].setAttribute("class", "chat-content-p3");
				ele[0].setAttribute("className", "chat-content-p3");
				/*if (curUserId == who) {
					ele[0].style.backgroundColor = "#EBEBEB";
				}*/
				for ( var j = 0; j < ele.length; j++) {
					lineDiv.appendChild(ele[j]);
				}
			}
		}
		if (curChatUserId.indexOf(contact) < 0 || $('.chatRight').is(':hidden')) {
			//contactLi.style.backgroundColor = "green";
			var $notreadDom = $(contactLi).children('.notreadMsg-count');
			var notreadCount = $notreadDom.html() != '' ? ($notreadDom.html().indexOf('+') < 0 ? parseInt($notreadDom.html()):100):0;
			if(notreadCount< 100){
				$notreadDom.html(notreadCount+1);
			}else{
				$notreadDom.html(notreadCount+'<b>+</b>');
			}
		}
		$('#contactlistUL').prepend(contactLi);
		var msgContentDiv = getContactChatDiv(contactDivId);
		if (curUserId == who) {
			lineDiv.style.textAlign = "right";
			$(lineDiv).addClass('msg-info-right');
			$(lineDiv).find('p2 img').addClass('chat-img-right');
			$(lineDiv).find('p3').addClass('chat-content-p3-right');
			$(lineDiv).find('p4').addClass('chat-name-right');
		} else {
			lineDiv.style.textAlign = "left";
			$(lineDiv).addClass('msg-info-left');
			$(lineDiv).find('p2 img').addClass('chat-img-left');
			$(lineDiv).find('p3').addClass('chat-content-p3-left');
			$(lineDiv).find('p4').addClass('chat-name-left');
		}

		
		var create = false;
		if (msgContentDiv == null) {
			msgContentDiv = createContactChatDiv(contactDivId);
			create = true;
		}
		msgContentDiv.appendChild(lineDiv);
		if (create) {
			//document.getElementById(msgCardDivId).appendChild(msgContentDiv);
			$(msgContentDiv).hide();
			$('#null-nouser').after(msgContentDiv);
		}
		msgContentDiv.scrollTop = msgContentDiv.scrollHeight;
		return lineDiv;

	};

	var showAddFriend = function() {
		$('#addFridentModal').modal('toggle');
		$('#addfridentId').val('好友账号');//输入好友账号
		$('#add-frident-warning').html("");
	};

	//添加输入框鼠标焦点进入时清空输入框中的内容
	var clearInputValue = function(inputId) {
		$('#' + inputId).val('');
	};

	var showDelFriend = function() {
		$('#delFridentModal').modal('toggle');
		$('#delfridentId').val('好友账号');//输入好友账号
		$('#del-frident-warning').html("");
	};

	//消息通知操作时条用的方法
	var showNewNotice = function(message) {
		$('#confirm-block-div-modal').modal('toggle');
		$('#confirm-block-footer-body').html(message);
	};

	var showWarning = function(message) {
		$('#notice-block-div').modal('toggle');
		$('#notice-block-body').html(message);
	};

	//主动添加好友操作的实现方法
	var startAddFriend = function() {
		var user = $('#addfridentId').val();
		if (user == '') {
			$('#add-frident-warning').html(
					"<font color='#FF0000'> 请输入好友名称</font>");
			return;
		}
		if (bothRoster)
			for ( var i = 0; i < bothRoster.length; i++) {
				if (bothRoster[i].name == user) {
					$('#add-frident-warning').html(
							"<font color='#FF0000'> 已是您的好友</font>");
					return;
				}
			}
		//发送添加好友请求
		var date = new Date().toLocaleTimeString();
		conn.subscribe({
			to : user,
			message : "加个好友呗-" + date
		});
		$('#addFridentModal').modal('hide');
		return;
	};

	//回调方法执行时同意添加好友操作的实现方法
	var agreeAddFriend = function(user) {
		conn.subscribed({
			to : user,
			message : "[resp:true]"
		});
	};
	//拒绝添加好友的方法处理
	var rejectAddFriend = function(user) {
		var date = new Date().toLocaleTimeString();
		conn.unsubscribed({
			to : user,
			message : date
		});
	};

	//直接调用删除操作时的调用方法
	var directDelFriend = function() {
		var user = $('#delfridentId').val();
		if (validateFriend(user, bothRoster)) {
			conn.removeRoster({
				to : user,
				success : function() {
					conn.unsubscribed({
						to : user
					});
					//删除操作成功时隐藏掉dialog
					$('#delFridentModal').modal('hide');
				},
				error : function() {
					$('#del-frident-warning').html(
							"<font color='#FF0000'>删除联系人失败!</font>");
				}
			});
		} else {
			$('#del-frident-warning').html(
					"<font color='#FF0000'>该用户不是你的好友!</font>");
		}
	};
	//判断要删除的好友是否在当前好友列表中
	var validateFriend = function(optionuser, bothRoster) {
		for ( var deluser in bothRoster) {
			if (optionuser == bothRoster[deluser].name) {
				return true;
			}
		}
		return false;
	};

	//回调方法执行时删除好友操作的方法处理
	var delFriend = function(user) {
		conn.removeRoster({
			to : user,
			groups : [ 'default' ],
			success : function() {
				conn.unsubscribed({
					to : user
				});
			}
		});
	};
	var removeFriendDomElement = function(userToDel, local) {
		var contactToDel;
		if (bothRoster.length > 0) {
			for ( var i = 0; i < bothRoster.length; i++) {
				if (bothRoster[i].name == userToDel) {
					contactToDel = bothRoster[i];
					break;
				}
			}
		}
		if (contactToDel) {
			bothRoster.remove(contactToDel);
		}
		// 隐藏删除好友窗口
		if (local) {
			$('#delFridentModal').modal('hide');
		}
		//删除通讯录
		$('#' + userToDel).remove();
		//删除聊天
		var chatDivId = curUserId + "-" + userToDel;
		var chatDiv = $('#' + chatDivId);
		if (chatDiv) {
			chatDiv.remove();
		}
		if (curChatUserId != userToDel) {
			return;
		} else {
			var displayName = '';
			//将第一个联系人作为当前聊天div
			if (bothRoster.length > 0) {
				curChatUserId = bothRoster[0].name;
				$('#' + curChatUserId).css({
					"background-color" : "blue"
				});
				var currentDiv = getContactChatDiv(curChatUserId)
						|| createContactChatDiv(curChatUserId);
				document.getElementById(msgCardDivId).appendChild(currentDiv);
				$(currentDiv).css({
					"display" : "block"
				});
				displayName = '与' + curChatUserId + '聊天中';
			} else {
				$('#null-nouser').css({
					"display" : "block"
				});
				displayName = '';
			}
			$('#talkTo').html('<a href="#">' + displayName + '</a>');
		}
	};

	//清除聊天记录
	var clearCurrentChat = function clearCurrentChat() {
		var currentDiv = getContactChatDiv(curChatUserId)
				|| createContactChatDiv(curChatUserId);
		currentDiv.innerHTML = "";
	};

	//显示成员列表
	var showRoomMember = function showRoomMember() {
		if (groupQuering) {
			return;
		}
		groupQuering = true;
		queryOccupants(curRoomId);
	};

	//根据roomId查询room成员列表
	var queryOccupants = function queryOccupants(roomId) {
		var occupants = [];
		conn.queryRoomInfo({
			roomId : roomId,
			success : function(occs) {
				if (occs) {
					for ( var i = 0; i < occs.length; i++) {
						occupants.push(occs[i]);
					}
				}
				conn.queryRoomMember({
					roomId : roomId,
					success : function(members) {
						if (members) {
							for ( var i = 0; i < members.length; i++) {
								occupants.push(members[i]);
							}
						}
						showRoomMemberList(occupants);
						groupQuering = false;
					},
					error : function() {
						groupQuering = false;
					}
				});
			},
			error : function() {
				groupQuering = false;
			}
		});
	};

	var showRoomMemberList = function showRoomMemberList(occupants) {
		var list = $('#room-member-list')[0];
		var childs = list.childNodes;
		for ( var i = childs.length - 1; i >= 0; i--) {
			list.removeChild(childs.item(i));
		}
		for (i = 0; i < occupants.length; i++) {
			var jid = occupants[i].jid;
			var userName = jid.substring(jid.indexOf("_") + 1).split("@")[0];
			var txt = $("<p></p>").text(userName);
			$('#room-member-list').append(txt);
		}
		$('#option-room-div-modal').modal('toggle');
	};

	var getObjectURL = function getObjectURL(file) {
		var url = null;
		if (window.createObjectURL != undefined) { // basic
			url = window.createObjectURL(file);
		} else if (window.URL != undefined) { // mozilla(firefox)
			url = window.URL.createObjectURL(file);
		} else if (window.webkitURL != undefined) { // webkit or chrome
			url = window.webkitURL.createObjectURL(file);
		}
		return url;
	};
</script>
	<!-- <div id="loginmodal" class="modal hide fade in" role="dialog"
		aria-hidden="true" data-backdrop="static">
		<div class="modal-header">
			<h3>用户登录</h3>
		</div>
		<div class="modal-body">
			<table>
				<tr>
					<td width="65%"><label for="username">用户名:</label> <input
						type="text" name="username" value="" id="username" tabindex="1" />
						<label for="password">密码:</label> <input type="password" value=""
						name="password" id="password" tabindex="2" /></td>
				</tr>
			</table>
		</div>
		<div class="modal-footer">
			<button class="flatbtn-blu" onclick="login()" tabindex="3">登录</button>
		</div>
	</div> -->

	<%--<div id="waitLoginmodal" class="modal hide leftcontact" data-backdrop="static" style="display: none;">--%>
		<%--<div style="margin: 130% 10px;text-align: center;">--%>
			<%--<img src="/img/loading.gif" style="display: block;margin: auto;"/>--%>
			<%--<span style="display: block;margin-top:20px;">拼命登陆中...</span>--%>
		<%--</div>--%>
	<%--</div>--%>
	<div class="content" id="content" style="display: none;height: 0;">
		<%--<div id="show-panel-switch" onclick="showSwitch()"><i class="fa fa-angle-double-right"></i></div>--%>

		<!-- 聊天页面 -->
		<!-- <div style="height: 78px;" id="righttop"></div> -->


		<div id="addFridentModal" class="modal hide fade" role="dialog"
			aria-hidden="true" data-backdrop="static">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h3>添加好友</h3>
			</div>
			<div class="modal-body">
				<input id="addfridentId" onfocus='clearInputValue("addfridentId")' />
				<div id="add-frident-warning"></div>
			</div>
			<div class="modal-footer">
				<button id="addFridend" class="btn btn-primary"
					onclick="startAddFriend()">添加</button>
				<button id="cancelAddFridend" class="btn" data-dismiss="modal">取消</button>
			</div>
		</div>

		<div id="delFridentModal" class="modal hide fade" role="dialog"
			aria-hidden="true" data-backdrop="static">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h3>删除好友</h3>
			</div>
			<div class="modal-body">
				<input id="delfridentId" onfocus='clearInputValue("delfridentId")' />
				<div id="del-frident-warning"></div>
			</div>
			<div class="modal-footer">
				<button id="delFridend" class="btn btn-primary"
					onclick="directDelFriend()">删除</button>
				<button id="canceldelFridend" class="btn" data-dismiss="modal">取消</button>
			</div>
		</div>

		<!-- 一般消息通知 -->
		<div id="notice-block-div" class="modal fade hide">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
			<div class="modal-body">
				<h4>Warning!</h4>
				<div id="notice-block-body"></div>
			</div>
		</div>

		<!-- 确认消息通知 -->
		<div id="confirm-block-div-modal" class="modal fade hide"
			role="dialog" aria-hidden="true" data-backdrop="static">
			<div class="modal-header">
				<h3>订阅通知</h3>
			</div>
			<div class="modal-body">
				<div id="confirm-block-footer-body"></div>
			</div>
			<div class="modal-footer">
				<button id="confirm-block-footer-confirmButton"
					class="btn btn-primary">同意</button>
				<button id="confirm-block-footer-cancelButton" class="btn"
					data-dismiss="modal">拒绝</button>
			</div>
		</div>

		<!-- 群组成员操作界面 -->
		<div id="option-room-div-modal" class="alert modal fade hide"
			role="dialog" aria-hidden="true" data-backdrop="static">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<div class="modal-header">
				<h3>群组成员</h3>
			</div>
			<div class="modal-body">
				<div id="room-member-list" style="height: 100px; overflow-y: auto"></div>
			</div>
		</div>

	</div>

