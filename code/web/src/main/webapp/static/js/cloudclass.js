//-----------------------------------  浏览器兼容性代码   解决  IE10中  console  未定义错误！
if (!window.console || !console.firebug)

{
	var names = [ "log", "debug", "info", "warn", "error", "assert", "dir",
			"dirxml", "group", "groupEnd", "time", "timeEnd", "count", "trace",
			"profile", "profileEnd" ];

	window.console = {};
	for (var i = 0; i < names.length; ++i)
		window.console[names[i]] = function() {
		}
}

// ------------------------------------

var newCourseclass = '.inside-dialog';
var currentCourseId = 0;
$(function() {
	var $selectSubjectList = $('.subject-ul');
	var $selectGradeList = $('.grade-ul');
	var $knowledgeList = $('.knowledge-ul');

	$.ajax({
		url : '/cloud/infos1.do',
		type : 'POST',
		dataType : 'json',
		data : {},
		success : function(data) {
			showdata(data);
		},
		error : function() {
			console.log('selCloudList error');
		}
	});

	$('body').on('click', '.retrieval-detail>img', function() {
		var content = $(this).parent().attr('data-status');
		$(this).parent().remove();
		removeActive(content);
	});

	$('.retrieval-search-form').keydown(function(event) {

		if (event.which == 13) {
			getSubjectCourseInfo($('.retrieval-search-form').val(), 1, 12);
		}
	});

	$('.retrieval-search-button').on('click', function() {
		getSubjectCourseInfo($('.retrieval-search-form').val(), 1, 12);
	});

	$('body').on('click', '.knowledge-down', function() {
		$('.retrieval-knowledge').css('height', 'auto');
		$(this).html('收起<i class="fa fa-angle-up"></i>');
		$(this).removeClass('knowledge-down').addClass('knowledge-up');
	});
	$('body').on('click', '.knowledge-up', function() {
		$('.retrieval-knowledge').css('height', '39px');
		$(this).html('更多<i class="fa fa-angle-down"></i>');
		$(this).removeClass('knowledge-up').addClass('knowledge-down');
	});

	// 演示js
	$('.retrieval-search')
			.change(
					function() {
						// $('.retrieval-search').val();
						var status = (window.location.href
								.indexOf('/cloudClass') > 0) ? $(
								'.sty-container .sty_span_actv').attr('sty-id')
								: $('.retrieval-search').val();
						$('.retrieval-detail>img').click();
						$('.knowledge-ul>li').show();
						$('.knowledge-up').click();
						$('.knowledge-down').show();
						// changeEducation(status);
						getCloudList(status);
					});
	function getCloudList(status) {
		if (status != 1) {
			$('.retrieval-knowledge').hide();
		} else {
			$('.retrieval-knowledge').show();
		}
		$
				.ajax({
					url : '/cloud/infos1.do',
					type : 'POST',
					dataType : 'json',
					data : {
						"schoolType" : status
					},
					success : function(data) {
						showdata(data);
						if ($('#knowledge-ul').height() > 30) {
							$('#knowledge-ul')
									.before(
											'<div class="knowledge-down more-knowledge">更多<i class="fa fa-angle-down"></i></div>');
						} else {
							$('.more-knowledge').remove();
						}
					},
					error : function() {
						console.log('selCloudList error');
					}
				});
		getSubjectCourseInfo('', 1, 12);
	}
	// 小学 初中 高中
	$('.sty_span', '.sty-container').click(
			function() {
				$(this).addClass('sty_span_actv').siblings().removeClass(
						'sty_span_actv');
				$('.select-course').find('li').removeClass('active');
				getCloudList($(this).attr('sty-id'));
				if ($('.more-knowledge').hasClass('knowledge-up')) {
					$('.more-knowledge').trigger('click');
				}
			});
	
	
	

	//setTimeout("dialogTrigger()",1000)

	
	
	jQuery(document).ready(function($) {
		
		
		
		  $(".btn-vo .btn-no").click(function(){
			  $(".wind-push-lesson").fadeOut(); 
			  $(".bg").fadeOut(); 
			  jQuery("#backlist").empty();
	          jQuery("#lesson_count").text("0");
	          
	          var teacherTree = $.fn.zTree.getZTreeObj("teacherDirUl1");
	          teacherTree.checkAllNodes(false);
		  });
		  $('.push-lesson').click(function(){ 
			  $(".wind-push-lesson").fadeIn();
			  $(".bg").fadeIn(); 
			  }); 
	  	  $('.wind-top span').click(function(){
	  		$(".wind-push-lesson").fadeOut(); 
	  		$(".bg").fadeOut();
	  		
	  		 var teacherTree = $.fn.zTree.getZTreeObj("teacherDirUl1");
	         teacherTree.checkAllNodes(false);
	  		
	  		});
	  	  
	  	  
		
	});

	function showdata(data) {
		var subjectList = data.subject;
		var gradeList = data.grade;
		var pointList = data.type;
		$selectSubjectList.empty();
		$selectGradeList.empty();
		$knowledgeList.empty();
		$('.retrieval-grade').show();
		for (var i = 0; i < subjectList.length; i++) {
			var content = '';
			content += "<li data-subjectId='" + subjectList[i].id + "'>"
					+ subjectList[i].value + "</li>";
			// 大学风采
			if (subjectList[i].subjectName == '大学风采') {
				content += '<img src="/img/star.gif" class="gif-star"/>';
			}
			$selectSubjectList.append(content);
		}

		if (gradeList.length == 0) {
			$('.retrieval-grade').hide();
		} else {
			for (var i = 0; i < gradeList.length; i++) {
				var content = '';
				content += "<li data-status='" + gradeList[i].id + "'>"
						+ gradeList[i].value + "</li>";
				$selectGradeList.append(content);
			}
		}
		if (pointList.length > 0) {
			for (var i = 0; i < pointList.length; i++) {
				var content = '';
				content += "<li data-pointId='" + pointList[i].id + "'>"
						+ pointList[i].name + "</li>";
				$knowledgeList.append(content);
			}
			$('.retrieval-knowledge').show();
		} else {
			$('.retrieval-knowledge').hide();
		}

		var $selectSubjectLi = $('.subject-ul>li');
		var $selectGradeLi = $('.grade-ul>li');
		var $knowledgeLi = $('.knowledge-ul>li');

		$selectSubjectLi.on('click', function() {
			var subject = $(this).text();
			selectKnowledge(subject);
			changeSearchBar($(this), 'subject');
		});

		$selectGradeLi.on('click', function() {
			changeSearchBar($(this), 'grade');
		});

		$knowledgeLi.on('click', function() {
			changeSearchBar($(this), 'knowledge');
		});
	}
	// 演示js
	$('#y_courses .btn-try').click(function() {
		tryPlayYCourse();
	});

	// changeEducation("3");
	getSubjectCourseInfo('', 1, 12);
	$('#example').bootstrapPaginator({
		currentPage : 1,
		totalPages : 1,
		itemTexts : function(type, page, current) {
			switch (type) {
			case "first":
				return "首页";
			case "prev":
				return "<";
			case "next":
				return ">";
			case "last":
				return "末页" + page;
			case "page":
				return page;
			}
		},
		onPageClicked : function(e, originalEvent, type, page) {
		}
	});

	$.fn.zTree.init($('#teacherDirUl'), {
		async : {
			enable : true,
			url : '/dir/find.do?type=BACK_UP'
		},
		data : {
			simpleData : {
				enable : true,
				pIdKey : 'parentId',
				rootPId : 0
			}
		},
		callback : {
			onRename : addDir
		},
		check : {
			enable : true,
			chkboxType : {
				"Y" : "",
				"N" : ""
			}
		},
		view : {
			selectedMulti : true,
			addHoverDom : renderTreeNewButton,
			removeHoverDom : removeTreeNewButton
		}
	});

	$.fn.zTree.init($('#teacherDirUl1'), {
		async : {
			enable : true,
			url : '/dir/find.do?type=BACK_UP'
		},
		data : {
			simpleData : {
				enable : true,
				pIdKey : 'parentId',
				rootPId : 0
			}
		},
		callback : {
			onRename : addDir,
			onCheck: treeonCheck
		},
		check : {
			enable : true,
			chkboxType : {
				"Y" : "",
				"N" : ""
			}
		},
		view : {
			selectedMulti : true,
			addHoverDom : renderTreeNewButton,
			removeHoverDom : removeTreeNewButton
		}
	});

	/*
	 * $.fn.zTree.init($("#schoolDirUl"), { async: { enable: true, url:
	 * '/directory/getSchoolDirTree', autoParam: ['id']
	 */
	/*
	 * , dataFilter : function(treeId, parent, data){ filterDirNodes(data);
	 * return data; }
	 */
	/*
	 * }, callback: { onAsyncSuccess: null, onClick: clickTreeNode }, check: {
	 * enable: true, chkboxType: { "Y": "", "N": "" } }, view: { selectedMulti:
	 * true } });
	 */
});



function treeonCheck()
{
	if (currentCourseId == null) {
		return;
	}
	var teacherTree = $.fn.zTree.getZTreeObj("teacherDirUl1");
	var selectedNodes = teacherTree.getCheckedNodes(true);
	
	var selectIds="";
	 for (var i = 0; i < selectedNodes.length; i++) {
		 selectIds += selectedNodes[i].id+",";
     }
	 
	 
		 
	
		$.ajax({
			url : "/lesson/dir/backups.do",
			type : "post",
			data : {
				"ids" : selectIds,
			},
			success : function(response) {
				
				jQuery("#backlist").empty();
				
				if(response.message && response.message.length>0)
				{	
				 
					
						for(var i=0;i<response.message.length;i++)
							{
								try{
								  var html='';
								     html+=' <div id="'+response.message[i].idStr+'" class="video-s">';
								     
								     if(response.message[i].name)
								     {
								    	   html+='<img src="'+response.message[i].name+'" width="147px" height="117px">';
								     }
								     else
								     {
								    	   html+=' <img src="../../../img/K6KT/txt_vo.png" width="147px" height="117px">';
								     }
								     html+=' <div class="video-tran">';
								     html+=' <span>'+response.message[i].value+'</span>';
								     html+=' </div>';
								     html+=' <div class="gou" ></div>';
								     html+=' <div id="a_'+response.message[i].idStr+'"  class="gou-b" ></div>';
								     html+=' <div id="b_'+response.message[i].idStr+'" class="bg-video" onclick="selectLesson(\''+response.message[i].idStr+'\')"></div>';
								     html+=' </div>';
								     jQuery("#backlist").append(html);
								}catch(ex)
								{
									
								}
							}
						
				}
                $(".video-s .bg-video").click(function(){
                    var id=$(this).attr("flag");
                    $("#a_"+id).toggle();
                });

                $('.video-s .gou').click(function() {
                    $(this).toggleClass('gou-b');
                    $(this).toggleClass('disblock');
                    $(this).next().toggleClass('disblock');
                });
				jQuery("#lesson_count").text(jQuery("#backlist").find(".video-s").length);
				
			},
			error : function(e) {
				
			}
		})
	
}


function pushToMultiBackups()
{
	 var ids="";
	 

	 jQuery("#backlist").find("div").each(function(){
		 
		 if(jQuery(this).attr("class")=="gou-b disblock" && jQuery(this).is(":visible"))
		 {
			 ids+=jQuery(this).attr("id")+",";
		 }
	 });
	 
	 
	 if(!ids)
		{
			 alert("请选择您的备课空间");
			 return ;
		}
	 
	 
		$.ajax({
			url : "/cloud/push/buckups.do",
			type : "post",
			data : {
				 "lessonId":currentCourseId,
				 "ids" : ids,
			},
			success : function(response) {
				
                   if(response.code=="200")
                   {
                 	   $(".wind-push-lesson").fadeOut();
                       $(".bg").fadeOut();
                	   alert("推送已经成功");
                   }else
                	{
                	    alert(response.message);
                	}
			},
			error : function(e) {
				
			}
		})
	
}




function selectLesson(id)
{
	 jQuery("#a_"+id).toggle();
}


function clickTreeNode(event, treeId, node) {
	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	treeObj.cancelSelectedNode();
	treeObj.checkNode(node, !node.getCheckStatus().checked, false);
}

function submitPushDir() {
	if (currentCourseId == null) {
		MessageBox("尚未选择课程！", -1);
	}
	var teacherTree = $.fn.zTree.getZTreeObj("teacherDirUl");
	// var schoolTree = $.fn.zTree.getZTreeObj("schoolDirUl");
	var selectedNodes = teacherTree.getCheckedNodes(true);
	// selectedNodes = selectedNodes.concat(schoolTree.getCheckedNodes(true));
	if (selectedNodes.length > 0) {
		MessageBox("推送中...", 0);
		var destDirs = selectedNodes.map(function(node) {
			return node.id;
		}).join(',');
		$
				.ajax({
					url : "/cloud/push.do",
					type : "post",
					data : {
						"lessonId" : currentCourseId,
						"dirIds" : destDirs
					},
					success : function(response) {
						if (getCookie('pushclass') == "pushT") {
							MessageBox('推送完成', 1);
						} else {
							MessageBox(
									'推送完成',
									2,
									'push',
									'老师您好，您还需要返回 <strong class="msg-orange">备课空间</strong>，点击刚才新建的该课程，选择工具栏上的<strong class="msg-orange">推送到班级</strong>之后，学生们才能在他们自己的<strong class="msg-orange">班级课程</strong>里看到这个视频哦~~');
						}
						pushDialog.close();
					},
					error : function(e) {
						MessageBox('推送失败!', -1);
					}
				})
	} else {
		MessageBox("请选择至少一个文件夹进行推送！", -1);
	}

	return false;
}

var polyv_player = null;

function selectKnowledge(subject) {
	switch (subject) {
	case "语文": {
		/* $('#knowledge-ul>li').hide(); */
		$('.chinese').show();
		break;
	}
	case "数学": {
		/* $('#knowledge-ul>li').hide(); */
		$('.math').show();
		break;
	}
	case "英语": {
		/* $('#knowledge-ul>li').hide(); */
		$('.english').show();
		break;
	}
	case "物理": {
		/* $('#knowledge-ul>li').hide(); */
		$('.physics').show();
		break;
	}
	case "化学": {
		/* $('#knowledge-ul>li').hide(); */
		$('.chemistry').show();
		break;
	}
	case "生物": {
		/* $('#knowledge-ul>li').hide(); */
		$('.biology').show();
		break;
	}
	case "政治": {
		/* $('#knowledge-ul>li').hide(); */
		$('.politics').show();
		break;
	}
	case "历史": {
		/* $('#knowledge-ul>li').hide(); */
		$('.history').show();
		break;
	}
	case "地理": {
		/* $('#knowledge-ul>li').hide(); */
		$('.geography').show();
		break;
	}
	case "其他": {
		/* $('#knowledge-ul>li').hide(); */
		$('.etc').show();
		break;
	}
	case "科学": {
		/* $('#knowledge-ul>li').hide(); */
		$('.science').show();
		break;
	}
	case "音美体": {
		/* $('#knowledge-ul>li').hide(); */
		$('.emt').show();
		break;
	}
	}
	checkHight();
}

function checkHight() {
	var obj = document.getElementById("knowledge-ul");
	$('.knowledge-up').click();
	if (obj.offsetHeight < 40) {
		$('.knowledge-down').hide();
	} else {
		$('.knowledge-down').show();
	}
}

/*
 * function changeEducation(status) { switch (status) { case "1": {
 * $('.retrieval-grade').show(); $('.xiaoxue').show(); $('.chuzhong').hide();
 * $('.low').show(); $('.middle,.high').hide();
 * $('.retrieval-knowledge').find('#knowledge-ul').attr('id', '');
 * $('.retrieval-knowledge>ul').hide(); $('.primary-school').show().attr('id',
 * 'knowledge-ul'); $('.knowledge-title').text('模块'); break; } case "2": {
 * $('.retrieval-grade').show(); $('.xiaoxue').hide();
 * $('.chuzhong,.middle').show(); $('.high,.low').hide();
 * $('.retrieval-knowledge').find('#knowledge-ul').attr('id', '');
 * $('.retrieval-knowledge>ul').hide(); $('.middle-school').show().attr('id',
 * 'knowledge-ul'); $('.knowledge-title').text('知识点'); break; } case "3": {
 * $('.retrieval-grade,.low').hide(); $('.middle,.high').show();
 * $('.retrieval-knowledge').find('#knowledge-ul').attr('id', '');
 * $('.retrieval-knowledge>ul').hide(); $('.high-school').show().attr('id',
 * 'knowledge-ul'); $('.knowledge-title').text('知识点'); break; } } }
 */

function removeActive(str) {
	if (str == 'subject') {
		$('.subject-ul').find('.active').removeClass('active');
		$('.knowledge-ul>li').show();
		$('.knowledge').remove();
		$('.knowledge-ul').find('.active').removeClass('active');
		checkHight();
	} else if (str == 'grade') {
		$('.grade-ul').find('.active').removeClass('active');
	} else {
		$('.knowledge-ul').find('.active').removeClass('active');
	}
	refreshCloud();
	getSubjectCourseInfo('', 1, 12);
}

function changeSearchBar(element, str) {

	if (!element.hasClass('active')) {
		element.parent().find('.active').removeClass('active');
		element.addClass('active');
		if ($('.retrieval-search-bar').prevAll('.' + str).length == 0) {
			// $('.retrieval-search-bar').before('<div class="retrieval-detail '
			// + str + '" data-status=' + str + '><span>' + element.text() +
			// '</span><img src="/img/error-grey.png"></div>');
		} else {
			$('.retrieval-search-bar').prevAll('.' + str).find('span').text(
					element.text());
		}
		if (str == 'subject') {
			$('.knowledge').remove();
			$('.knowledge-ul').find('.active').removeClass('active');
			refreshCloud();
		}
		getSubjectCourseInfo('', 1, 12);
	}
}

function refreshCloud() {
	var $knowledgeList = $('.knowledge-ul');
	// $('.retrieval-search').val();
	var status = (window.location.href.indexOf('/cloud/cloudLesson') > 0) ? $(
			'.sty-container .sty_span_actv').attr('sty-id') : $(
			'.retrieval-search').val();
	var subjectId = $('.subject-ul .active').attr('data-subjectId') || null;
	if (subjectId == 53 || subjectId == 47 || subjectId == 51
			|| subjectId == 52) {
		$('.retrieval-grade').hide();
	} else {
		$('.retrieval-grade').show();
	}
	$
			.ajax({
				url : '/cloud/infos1.do',
				type : 'POST',
				dataType : 'json',
				data : {
					"schoolType" : status,
					"subject" : subjectId
				},
				success : function(data) {
					var pointList = data.type;
					$knowledgeList.empty();
					if (status == 1) {
						$('.retrieval-knowledge').show();
					}
					if (pointList.length == 0) {
						$('.retrieval-knowledge').hide();
					} else {
						for (var i = 0; i < pointList.length; i++) {
							var content = '';
							content += "<li data-pointId='" + pointList[i].id
									+ "'>" + pointList[i].name + "</li>";
							$knowledgeList.append(content);
						}
					}
					if ($('#knowledge-ul').height() > 30) {
						$('#knowledge-ul')
								.before(
										'<div class="knowledge-down more-knowledge">更多<i class="fa fa-angle-down"></i></div>');
					} else {
						$('.more-knowledge').remove();
					}
					var $knowledgeLi = $('.knowledge-ul>li');
					$knowledgeLi.on('click', function() {
						changeSearchBar($(this), 'knowledge');
					});
					checkHight();
				},
				error : function() {
					console.log('selCloudList error');
				}
			});
}

// function getTeacherInfo() {
// var ret = [];
// $.ajax({
// url: "/reverse/getTeacherInfo.action",
// type: "get",
// dataType: "json",
// async: false,
// data: {},
// success: function(data) {
// if (data) {
// ret = data;
// console.log(data);
// var tname = data.role == 1 ? '老师' : '校领导';
// $(".info p:first").html(data.userName + '<span style="font-size:14px;">' +
// tname + '</span><a href="/editphoto"><img src="/img/edit.png"/></a>'
// +'<label id="user-help" style="margin-left: 30px;"
// onclick="gotoHelpPage()">用户手册</label>');
// $(".info p span:nth(1)").text(data.sex == 1 ? "男" : "女");
// if ('${session.currentUser.getRole()}' == 1) {
// $(".info p span:nth(0)").text("教师");
// } else if ('${session.currentUser.getRole()}' == 2) {
// $(".info p span:nth(0)").text("校领导");
// } else {
//
// }
//
// if ('${session.currentUser.getRole()}' == 1) {
// $(".info p span:nth(2)").text(data.subjectName + "老师");
// } else if ('${session.currentUser.getRole()}' == 2) {
// $(".info p span:nth(2)").text("校领导");
// } else {
//
// }
//
//
// $(".popu b:nth(0)").text(data.studentNum);
// $(".popu b:nth(1)").text(data.courseNum);
// /* $("#teacher_info >img").attr('src','/upload/pic/user/'+ data.imageUrl); */
// /* $(".popu b:nth(2)").text(data.viewNum); */
// }
// },
// complete: function() {}
// });
// return ret;
// }

function gotoHelpPage() {
	location.href = "/business/reverse/user/userHelp.jsp";
}

function getSubjectCourseInfo(word, page, size) {

	if (word != null) {
		getSubjectCourseInfo.searchTerm = word;
	}

	$('#y_courses').addClass('hardloadingb');
	$('#y_courses').html('');
	var subjectId = $('.subject-ul .active').attr('data-subjectId') || null;
	var schoolLevel = (window.location.href.indexOf('/cloudLesson') > 0) ? $(
			'.sty-container .sty_span_actv').attr('sty-id') : $(
			'.retrieval-search').val();// 云课程页面 其他页面
	var gradeId = $('.grade-ul .active').attr('data-status') || null;
	var knowledgePointId = $('#knowledge-ul .active').attr('data-pointId')
			|| null;
	// var gradeId = $('.grade-ul .active').attr('cid') || null;
	// $.ajax({
	// url: "/reverse/selSubjectCourseInfo.action",
	// type: "post",
	// dataType: "json",
	// async: true,
	// data: {
	// subjectName: sname,
	// gradeId: gradeId,
	// word: word,
	// page: page,
	// pageSize: size
	// },
	// success: function(data) {
	// showSubjectCourseInfo(data);
	// var total = data.rows.length > 0 ? data.total : 0;
	// var to = Math.ceil(total / size);
	// totalPages = to == 0 ? 1 : to;
	// if (page == 1) {
	// resetPaginator(totalPages);
	// }
	// },
	// error: function(e) {},
	// complete: function() {
	// $('#y_courses').removeClass('hardloadingb');
	// $('.btn-use').click(function() {
	// $('#new-course-name').val($(this).closest('div').find('div').text());
	// });
	// }
	// });

	$('#y_courses').load('/cloud/courseList.do', {
		"grade" : gradeId,
		"schoolType" : schoolLevel,
		"subject" : subjectId,
		"classTypeId" : knowledgePointId,
		"searchName" : getSubjectCourseInfo.searchTerm,
		"page" : page,
		"limit" : size
	}, function(response, status) {
		$('#y_courses').removeClass('hardloadingb');
	});

	
	setTimeout("dialogTrigger()",1000)
	
	
	
	
	/*
	 * $.ajax({ url: "/reverse/selSubjectCourseInfo.action", type: "post",
	 * dataType: "json", async: true, data: { "gradeId": gradeId, "schoolLevel":
	 * schoolLevel, "subjectId": subjectId, "knowledgePointId":
	 * knowledgePointId, "word": word, "page": page, "pageSize": size },
	 * success: function(data) { if(data.rows.length > 0){
	 * showSubjectCourseInfo(data); }else{ $('#y_courses').append('<span
	 * id="nodata-msg"
	 * style="position:absolute;top:50%;left:50%;">Oh~NO,目前没有符合条件的课程</span>'); }
	 * var total = data.rows.length > 0 ? data.total : 0; var to =
	 * Math.ceil(total / size); totalPages = to == 0 ? 1 : to; if (page == 1) {
	 * resetPaginator(totalPages); } }, error: function(e) {
	 * console.log('selSubjectCourseInfo error'); }, complete: function() {
	 * $('#y_courses').removeClass('hardloadingb'); } });
	 */
	/*
	 * $('.btn-use').click(function() {
	 * $('#new-course-name').val($(this).closest('div').find('div').text()); });
	 */
	/*
	 * } });
	 */
}

function resetPaginator(totalPages) {
	if (totalPages <= 0) {
		totalPages = 1;
	}
	$('#example').bootstrapPaginator("setOptions", {
		currentPage : 1,
		totalPages : totalPages,
		itemTexts : function(type, page, current) {
			switch (type) {
			case "first":
				return "首页";
			case "prev":
				return "<";
			case "next":
				return ">";
			case "last":
				return "末页" + page;
			case "page":
				return page;
			}
		},
		onPageClicked : function(e, originalEvent, type, page) {
			getSubjectCourseInfo(null, page, 12);
		}
	});
}

function showSubjectCourseInfo(data) {
	var target = $('#y_courses');
	if (data.rows) {
		var html = "";
		for ( var i in data.rows) {
			var co = data.rows[i];
			/* var img = getImage(co.subjectName); */
			// html += '<div class="col-xs-3 subject-container"><img
			// class="subject-img" src="' + cimg + '">';
			html += '<div class="col-xs-3 subject-container"><div class="subject-img-container"><img class="subject-img" src="'
					+ co.imageURL + '"></div>';/*
												 * html += '<div
												 * class="context">' +
												 * co.coursename + '</div><a
												 * class="btn btn-default
												 * btn-try"
												 * onclick="tryPlayYCourse($(this));"
												 * vurl="' + co.path + '"
												 * vid="'+co.id+'">试看</a>';
												 * html += '<a class="btn
												 * btn-primary btn-use"
												 * onclick="currentCourseId=' +
												 * co.id +
												 * ';showDialog(newCourseclass);">推送</a></div>';
												 */
		}
	}
	target.html(html);
}

function getImage(subjectName) {
	var url = '';
	switch (subjectName) {
	case '语文': {
		url = '/img/course/chinese.png';
		break;
	}
	case '数学': {
		url = '/img/course/math.png';
		break;
	}
	case '英语': {
		url = '/img/course/english.png';
		break;
	}
	case '物理': {
		url = '/img/course/physics.png';
		break;
	}
	case '化学': {
		url = '/img/course/chemistry.png';
		break;
	}
	case '生物': {
		url = '/img/course/biology.png';
		break;
	}
	case '地理': {
		url = '/img/course/geography.png';
		break;
	}
	case '历史': {
		url = '/img/course/history.png';
		break;
	}
	case '政治': {
		url = '/img/course/politics.png';
		break;
	}
	case '音美体': {
		url = '/img/course/art.png';
		break;
	}
	case '科学': {
		url = '/img/course/others.png';
		break;
	}
	case '其他': {
		url = '/img/course/others.png';
		break;
	}
	}
	return url;
}
var video_id;

var watch_timer = null, watchedTime = 0, videoLength = 0;
var isFlash = false;

function cloudResPlay(id) {
	var obj = jQuery("#" + id);
	increase(id, 0);
	tryPlayYCourse(obj);
}

function increase(id, type) {
	$.ajax({
		url : "/cloudres/increase.do",
		type : "post",
		data : {
			"id" : id,
			"type" : type
		},
		success : function(response) {
			if (type == 0) {
				var n = jQuery("#v_" + id).text();
				jQuery("#v_" + id).text(parseInt(n) + 1)
			}
			if (type == 1) {
				var n = jQuery("#p_" + id).text();
				jQuery("#p_" + id).text(parseInt(n) + 1)
			}
		},
		error : function(e) {
		}
	})
}

function tryPlayYCourse(ob) {
	var url = ob.attr('vurl');
	video_id = ob.attr('vid');
	videoLength = (ob.attr('video-length') / 1000 || 0);

	var videoType = getVideoType(url);
	switch (videoType) {
	case "FLASH":
		$('#sewise-div').hide();
		$('#player_div').show();
		$('#YCourse_player').addClass('flash-container');
		$("#player_div").addClass('flash-player-div');
		$("#player_div").html('<div id="swfobject_replace"></div>');
		var attributes = {
			id : 'flashViewer'
		};

		var params = {
			allowfullscreen : 'true',
			menu : 'false',
			wmode : 'transparent'
		};
		swfobject.embedSWF(url, "swfobject_replace", "100%", "100%", "9.0.0",
				'', {}, params, attributes);
		break;
	case "POLYV":
		$('#sewise-div').hide();
		$('#player_div').show();
		var _start = url.lastIndexOf('/') + 1, _end = url.lastIndexOf('.');
		var polyv_vid = url.substring(_start, _end);
		var player = polyvObject('#player_div').videoPlayer({
			'width' : '100%',
			'height' : '100%',
			'vid' : polyv_vid
		});
		break;
	case "HLS":
		$('#player_div').hide();
		$('#sewise-div').show();
		try {
			SewisePlayer.toPlay(url, "云课程", 0, true);
		} catch (e) {
			playerReady.URL = url;
			isFlash = true;
		}
		break;
	}

	$('#YCourse_player').fadeIn('fast');
	$('.dialog-bg').fadeIn('fast');
	vedioSize();
	if (!isStudent) {
		return;
	}

	switch (videoType) {
	case "FLASH":
		if (bowser.ios || bowser.android && typeof swfobject == 'undefined') {
		} else {
			clearTimeout(watch_timer);
			watch_timer = setTimeout(function cloudCourseScore() {
				$.ajax({
					url : '/experience/studentScoreLog.do',
					type : 'POST',
					dataType : 'json',
					async : false,
					data : {
						'relateId' : video_id,
						'scoretype' : 0
					},
					success : function(data) {
						if (data.resultcode == 0) {
							scoreManager(data.desc, data.score);
						}
					},
					error : function() {
					}
				});

			}, 1000);
		}
		break;
	case "POLYV":
		watchedTime = 0;
		if (bowser.ios || bowser.android && typeof swfobject == 'undefined') {
			var video = $('#player_div video')[0];
			video.addEventListener('play', s2j_onPlayStart);
			video.addEventListener("pause", s2j_onVideoPause);
			video.addEventListener('ended', s2j_onPlayOver);
			video.play();
		}
		break;
	case "HLS":
		watchedTime = 0;
		break;
	}
}

function playerReady(name) {
	if (isFlash) {
		SewisePlayer.toPlay(playerReady.URL, "云课程", 0, true);
	}
}

function swfObjectCallback(e) {
	if (e.success) {
		// alert("The embed was successful!");
	} else {
		// alert("The embed failed!");
	}
}

function closeCloudView() {
	$('#YCourse_player').fadeOut('fast', function() {
		$('#YCourse_player').removeClass('flash-container');
		$("#player_div").removeClass('flash-player-div');
		$("#player_div").empty();
	});
	$('.dialog-bg').fadeOut('fast');
	clearInterval(watch_timer);
	watchedTime = 0;
	try {
		SewisePlayer.doStop();
	} catch (e) {
	}
}

function addCourseClass(list) {
	var content = "<ul class='add-course-class'>";
	for (var i = 0; i < list.length; i++) {
		content += "<li><input type='checkbox' data-id='" + list[i].classtype
				+ "," + list[i].id + "'>" + list[i].classname + "</li>";
	}
	content += "</ul>";
	return content;
}

function getVideoType(url) {
	if (url.indexOf('polyv.net') > -1) {
		return "POLYV";
	}
	if (url.endWith('.swf')) {
		return 'FLASH';
	}
	return 'HLS';
}

function s2j_onPlayOver() {
	clearInterval(watch_timer);
}

function s2j_onPlayStart() {
	if (!isStudent) {
		return;
	}
	clearInterval(watch_timer);
	watch_timer = setInterval(watchTimeCheck, 1000);
}

function s2j_onVideoPause() {
	clearInterval(watch_timer);
}

var onStop = s2j_onPlayOver;
var onStart = s2j_onPlayStart;
var onPause = s2j_onVideoPause;

function watchTimeCheck() {
	watchedTime++;
	if (watchedTime > videoLength * 0.85) {
		$.getJSON("/experience/studentScoreLog.do", {
			'relateId' : video_id,
			'scoretype' : 0
		}, function(data) {
			if (data.resultcode == 0) {
				scoreManager(data.desc, data.score);
			}
		});
	}
}


function dialogTrigger()
{

	/*$('.col-xs-4 .push-f').click(function(){
		$(this).toggleClass('btn-use-open');
		$(this).children(".push-select").slideToggle();
	});*/
	$('.col-xs-4 .push-f').on("click",function(){
		$(this).toggleClass('btn-use-open');
		$(this).children(".push-select").slideToggle();
	});
	
    $('.col-xs-4').mouseleave(function(){
        $(this).children().children(".push-select").slideUp();
    });
}

var pushDialog = null;

function openPushDialog() {
	if (pushDialog == null) {
		pushDialog = dialog({
			title : '推送课程至备课空间成为新课程',
			content : $('#teacherDirUl')[0],
			width : 486,
			height : 219,
			okValue : '确定',
			ok : submitPushDir,
			cancelValue : '取消',
			cancel : function() {
				pushDialog.close();
				return false;
			}
		});
	}
	pushDialog.showModal();
}

var pushDialog1 = null;
function openPushDialog1() {
	$(".wind-push-lesson").fadeIn();
	$(".bg").fadeIn();
}