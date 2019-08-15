var customCandidateArr = new Array("list");
var elects = [];
template.config('escape', false);

template.helper('day', function(date){
	return date.substring(0, 10);
});
Object.defineProperty(Array.prototype, 'indexOf', {
	enumerable: false,
	value: function (val) {
		for (var i = 0; i < this.length; i++) {
			if (this[i] == val) {
				return i;
			}
		}
		return -1;
	}
});
/*$(function(){
	$('#customCandidateCheck').parent().siblings('label').find.c(function(){
		if($('#customCandidateCheck').prop('checked')){
			$('#customCandidateCheck').trigger('click');
		}
	});
});*/
function signup(obj){
	var $bm = $(obj).parents('.tp-con-people').siblings('.tp-bm-box');
	if($bm.is(':hidden')){
		$(obj).html('取消');
		$bm.show();
	}else{
		$(obj).html('报名');
		$bm.hide();
	}
}
function launchVote(){
	if(checkvote()) {
        var voteCatogery = $.map($('.view-catogery:checked'), function (checkbox) {
            return $(checkbox).val();
        });
		var elect = {
			'name': $('#inputEmail3').val(),
			'description': $('#descriptforvote').val().replace(/\n/g, '<br>'),
			'classIds': voteCatogery,
			parentEligible: false,
			studentEligible: false,
			teacherEligible: false,
			'parentVotable': $('#parentVotable').prop('checked'),//投票角色
			'studentVotable': $('#studentVotable').prop('checked'),
			'teacherVotable': $('#teacherVotable').prop('checked'),
			'startDate': $('#starttimeforvote').val().replace(/-/g, ''),
			'endDate': $('#closetimeforvote').val().replace(/-/g, ''),
			'ballotCount': $('#ballotCount').val()
		};
		customCandidateArr = new Array();
        if($('#customCandidateCheck').prop('checked')){
			$('#custom-container').children('span').each(function () {
				customCandidateArr.push($(this).attr('userid'));
			});
			elect.candidateArr = customCandidateArr;
        }else{
			elect.parentEligible = $('#parentEligible').prop('checked');
			elect.studentEligible = $('#studentEligible').prop('checked');
			elect.teacherEligible = $('#teacherEligible').prop('checked');
        }
		if(!elect.parentVotable&&!elect.studentVotable&&!elect.teacherVotable)
		{
			alert("请选择投票角色");
			return;
		}
		if(elect.startDate=="")
		{
			alert("请选择投票开始日期");
			return;
		}
        $.ajax({
            url: '/elect/addElect.do',
            type: 'post',
			data: elect,
            traditional: true,
            success: function (response) {
                $('.tp-publish>form :text').val('');
                $('.tp-publish>form textarea').val('');
                $('.tp-publish>form :checked').removeAttr('checked');
                $('.custom-candidate').slideUp();
                getVoteList(0, 5);
				if (response.score) {
					scoreManager(response.scoreMsg, response.score);
				}
                $('.eligibleCheck').prop('checked',false).attr('disabled',false);
            },
            error: function () {
            }
        });
    }
}
function checkvote(){
	var title = $('#inputEmail3').val();
    if(title.length==0)
    {alert('请输入标题！');
    return false;}
	 else if(title.length > 20){
		alert('投票标题不能超过20个字!');
		return false;
	}
	if ($('#voteCatogery :checked').length == 0) {
		alert('请选择范围!');
		return false;
	}
    var description = $("#descriptforvote").val().replace(/\n/g, '<br>');
    if(description.getLength()>1000){
        alert('说明文字不能超过1000个字符！');
        return false;
    }

    if(!$('#closetimeforvote').val())
    {
        alert('请选择结束日期！');
        return false;
    }
    return true;
}
function voteDetail(id){
	$('.vote-container').load('/WEB-INF/pages/vote/votedetail.jsp',function(response,status,xhr){
		alert(status);
	});
}
function getVoteList(page,size,firstPage) {
	$('.tp-con.tp-publish').show();
    $('.elect-list-container').html('<div class="hardloadingt"></div>');
    $.getJSON('/elect/elects.do', {
        'page':page,
		'size': size
    }, function (response) {
		$.each(response.content, function(i, elect){
			wrapElectData(elect);
		});

		elects = response.content;
		
        var html = template('electList', response);
		
		$('.elect-list-container').html(html);
      //重设分页导航
        resetVotePage(response.totalPages,firstPage);
        $('#example').show();
		renderTools();
    });
}

function renderTools() {
	//上传图片
	$('.imgforvote').fileupload({
		url: '/elect/upload.do',
		paramName: 'file',
		done: function (e, response) {
			var $imgUl = $(this).closest('.tp-bm-box').find('.vote-img-container ul');
			$imgUl.find('.tip-msg').remove();
			if ($imgUl.find('.candidate-img').length < 9) {
				$imgUl.append('<li><a class="fancybox" href="' + response.result[0] + '" data-fancybox-group="vote" title="预览"><img class="candidate-img" src="' + response.result[0] + '"></img></a><i class="fa fa-times blog-img-delete"></i></li>');
				$('.fancybox').fancybox();
			}
		},
		progressall:function(e, data){
			var $imgUl = $(this).closest('.tp-bm-box').find('.vote-img-container ul')
			if($imgUl.find('.tip-msg').length == 0){
				$imgUl.prepend('<li class="tip-msg">正在上传...</li>');
			}
			
		}
	});
	//上传视频
	$('.videoforvote').fileupload({
		url: '/commonupload/video.do',
		paramName: 'Filedata',
		formData: {'type': 'elect'},
		done: function (e, response) {
			$(this).closest('.tp-bm-box').find('.vote-vedio-container ul').html('<li data-id="' + response.result.videoInfo.id + '"><img class="candidate-vedio-cover" src="' + response.result.videoInfo.imageUrl + '"></img><img src="/img/play.png" class="video-play-icon"/><i class="fa fa-times blog-img-delete"></i></li>');
		},
		progressall:function(e, data){
			$(this).closest('.tp-bm-box').find('.vote-vedio-container ul').html('正在上传...');
		}
		
	});
}

function wrapElectData(elect) {
	elect.voted = 0;
	if (elect.candidates) {
		$.each(elect.candidates, function (index, candidate) {
			if (candidate.ballots != null && $.inArray(userId, candidate.ballots) >= 0) {
				candidate.voted = true;
				elect.voted++;
			}
			if (candidate.id == userId) {
				elect.signed = true;
			}
		});
		elect.candidates = elect.candidates.sort(function (a, b) {
			var ab = a.ballots ? a.ballots.length : 0,
				bb = b.ballots ? b.ballots.length : 0;
			if (ab != bb) {
				return bb - ab;
			}
			return new Date(a.signTime) - new Date(b.signTime);
		});
		//计算百分比
		if (!elect.voting) {
			elect.totalBallotCount = 0;
			$.each(elect.candidates, function (index, candidate) {
				if (candidate.ballots != null) {
					elect.totalBallotCount += candidate.ballots.length;
				} else {
					candidate.percent = 0;
				}

			});
			$.each(elect.candidates, function (index, candidate) {
				if (candidate.ballots != null) {
					candidate.percent = candidate.ballots.length * 100 / elect.totalBallotCount;
				}
			});
		}
	}
	if (elect.ballotCount == null) {
		elect.ballotCount = 1;
	}
	if (elect.voted < elect.ballotCount && elect.candidates && elect.candidates.length > elect.voted) {
		//有余票且有多余候选人可投票
		elect.votable = true;
	}
	
	//参选权限
	eligibleRight(elect);
	//投票权限
	votableRight(elect);
	//判断是否开始投票
	var nowDate = new Date();
	var formatDate = nowDate.getFullYear() +'/'+ ((nowDate.getMonth()+1) < 10 ? '0'+(nowDate.getMonth()+1):(nowDate.getMonth()+1)) +'/'+nowDate.getDate();
	if(elect.startDate){
		if(elect.startDate <= formatDate){
			elect.begin = true;
		}else{
			elect.begin = false;
		}
	}else{
		elect.begin = true;
	}
}
//添加参选条件
function eligibleRight(elect){
	elect.eligRight = false;


    if(isTeacher(nb_role)&&elect['teacherEligible']){
        elect.eligRight = true;
    }
    if(isStudent(nb_role)&&elect['studentEligible']){
        elect.eligRight = true;
    }
    if(isHeadMaster(nb_role)&&elect['teacherEligible']){
        elect.eligRight = true;
    }
    if(isParent(nb_role)&&elect['parentEligible']){
        elect.eligRight = true;
    }

	/*
	var eligible_role = new Array();
	if(elect.parentEligible){
		eligible_role.push(4);
	}
	if(elect.studentEligible ){
		eligible_role.push(0);
	}
	if(elect.teacherEligible ){
		eligible_role.push(1,2);
	}	
	if(elect.candidates != null && elect.candidates.length > 0){
		$.grep(elect.candidates,function(n){
			if(n.id == userId)
				elect.eligRight = true;
		});
	}else{
		$.grep(eligible_role,function(n){
			if(n == nb_role)
				elect.eligRight = true;
		});
	 }*/
}
//投票权限
function votableRight(elect){
	elect.voteRight = false;

    if(isTeacher(nb_role)&&elect.teacherVotable ){
        elect.voteRight = true;
    }
    if(isStudent(nb_role)&&elect.studentVotable ){
        elect.voteRight = true;
    }
    if(isHeadMaster(nb_role)&&elect.teacherVotable ){
        elect.voteRight = true;
    }
    if(isParent(nb_role)&&elect.parentVotable){
        elect.voteRight = true;
    }
	//票用完
	if(elect.voteRight){
		elect.enable = false;
		if(elect.voted == elect.ballotCount){
			elect.enable = false;
		}
	}
}

var currentpage = 1;
if($('li.active a').html() != null && $('li.active a').html() != ''){
	currentpage = parseInt($('li.active a').html());
}
function resetVotePage(totalPages,firstPage) {
    if (totalPages <= 0) {
        totalPages = 1;
    }
    if(firstPage){
    	currentpage = 1;
    }
    if(currentpage >= totalPages){
		currentpage = totalPages;
	}

    $('#example').bootstrapPaginator("setOptions", {
        currentPage: currentpage,
        totalPages: totalPages,
        itemTexts: function (type, page, current) {  
            switch (type) {  
                case "first":  
                    return "首页";  
                case "prev":  
                    return "<";  
                case "next":  
                    return ">";  
                case "last":  
                    return "末页"+page;  
                case "page":  
                    return  page;  
            }                 
        },
        onPageClicked: function(e, originalEvent, type, page) {
        	currentpage = page;
        	getVoteList(page-1, 5);
        }
    });
}

/**
 * 报名
 * @param btn
 */
function runForElect(btn){
	var $elect = $(btn).closest('.elect');
	var $bm = $(btn).closest('.tp-bm-box');
	var manifesto = $bm.find('textarea.tp-form-control').val();
	if (manifesto && $.trim(manifesto) != '') {
		$.ajax({
			url: '/elect/runForElect.do',
			type: 'POST',
			data: {
				electId: $elect.attr('elect-id'),
				manifesto: manifesto,
				picUrls: $.map($bm.find('.candidate-img'), function (img) {
					return $(img).attr('src');
				}),
				videoId: $bm.find('.vote-vedio-container ul li').attr('data-id'),
				voiceUrl: $bm.find('.vote-audio-container ul p a').attr('url')
			},
			traditional: true,
			success: function (elect) {
				$elect.find('.tp-bm-box').hide();
				$elect.find('.tp-p-detail').find('.tp-p-detail-btn').html('<button class="tp-btn tp-cancel" type="button" onclick="cancleSignup(\'' + elect.elect.id + '\',this)">退出参选</button>');
				wrapElectData(elect.elect);
				$elect.find('.tp-p-detail>ul').html(template('candidate-list-template', elect.elect));
				if ($('.tp-con.tp-publish').length == 0 || $('.tp-con.tp-publish').is(':hidden')) {//在详细页面
					viewElect($elect.attr('elect-id'));
				} else {
					var candidateCount = $elect.find('.tp-p-detail>ul li').length;
					if (candidateCount > 9) {
						$('.clearfix.brief').after('<a class="expand-candidate-btn" onclick="expandCandidates(this)">更多' + candidateCount + '名候选人↓</a>');
					}
				}
				if (elect.score) {
					scoreManager(elect.scoreMsg, elect.score);
				}
			}
		});
	} else {
		alert('请输入竞选宣言');
	}
}


/**
 * 投票
 * @param btn
 */
function vote(btn){
	var candidateId = $(btn).closest('.candidate').attr('user-id');
	$.ajax({
		url: '/elect/vote.do',
		type: 'POST',
		data: {
			electId: $(btn).closest('.elect').attr('elect-id'),
			candidateId: $(btn).closest('.candidate').attr('user-id')
		},
		dataType: 'json',
		success: function(elect){
			var temp = $.grep(elect.elect.candidates,function(n,i){
				if(n.id == candidateId)
					return n;
			});
			$(btn).closest('dl').find('.tp-piao-count').html(temp[0].ballots.length + '票');
			$(btn).closest('dt').hide().after('<dt class="tp-done">已投</dt>');
			var $candidateList = $(btn).closest('.clearfix');
			resetVoteBtn(elect.elect.ballotCount,$candidateList.find('.tp-done').length,$candidateList);
            if (elect.score) {
                scoreManager(elect.scoreMsg, elect.score);
            }
		}
	});
}
//根据每人票数和已投票数,重置按钮
function resetVoteBtn(ballotCount,voted,$content){
	if(ballotCount <= voted){
		$content.find('.tp-tpbtn').hide();
	}
}
function viewElect(electId,candidateid){
	$.getJSON('/elect/viewElect.do',{
		'electId':electId
	}, function (elect) {
		wrapElectData(elect);
		var html = template('viewelect', elect);
		$('.tp-con.tp-publish').hide();
		$('.elect-list-container').html(html);
		$('#example').hide();
		if(candidateid){
			location.href = '#tp-intro-'+candidateid;
		}else{
			location.href = '#vote-container';
		}
		renderTools();
		$('.fancybox').fancybox();
	});
}
function signup(obj){
	var $bm = $(obj).parents('.tp-con-people').siblings('.tp-bm-box');
	if($bm.is(':hidden')){
		$(obj).html('取消');
		$bm.slideDown();
	}else{
		$(obj).html('报名');
		$bm.slideUp();
	}
}

function chooseVideo(obj){
	$(obj).parent().siblings('.size-zero').find('.videoforvote').trigger('click');
}

function showRecordflash(obj,electId)
{	
	var $flash = $(obj).parent('.tpz').siblings('#recordflash');
	if($flash.is(':hidden')){
		var mc = new FlashObject("/static/plugins/audiorecorder.swf", "recorderApp", "350px", "23px", "8");
	    mc.setAttribute("id","recorderApp");
	    mc.setAttribute("name","recorderApp");

	    mc.addVariable("uploadAction","/elect/upload.do");
	    mc.write("myContent-"+electId);
	    $flash.show();
	    $flash.find('.sanjiao').show();
	    $flash.find("div[id^=myContent]").show();
	}else{
		$flash.hide();
	}
    
}

var isFlash = false;

function getVideoType(url) {
	if (url.indexOf('polyv.net') > -1) {
		return "POLYV";
	}
	if (url.endWith('.swf')) {
		return 'FLASH';
	}
	return 'HLS';
}

function playTheMovie(url) {
	var videoSourceType = getVideoType(url);

	$('.dialog-bg').fadeIn('fast');
	var $player_container = $("#elect-player");
	$player_container.fadeIn();

	if (videoSourceType == "POLYV") {
		$('#sewise-div').hide();
		$('#elect-player-div').show();
		var player = polyvObject('#elect-player-div').videoPlayer({
			'width': '800',
			'height': '450',
			'vid': url.match(/.+(?=\.swf)/)[0].replace(/.+\//, '')
		});
	} else {
		$('#elect-player-div').hide();
		$('#sewise-div').show();
		try {
			SewisePlayer.toPlay(url, "投票选举", 0, true);
		} catch (e) {
			playerReady.URL = url;
			isFlash = true;
		}
	}
}

function playerReady(name){
	if(isFlash){
		SewisePlayer.toPlay(playerReady.URL, "云课程", 0, true);
	}
}

function closeElectMovie() {
	$('.dialog-bg').fadeOut('fast');
	var $player_container = $("#elect-player");
	$player_container.fadeOut('fast', function() {
		$('#elect-player-div').empty();
		try {
			SewisePlayer.doStop();
		} catch (e) {
		}
	});
}

//删除竞选报名
function cancleSignup(electid,obj){
	$elect = $(obj).closest('.tp-con-people');
	if(confirm('确定退出参选?')){
		$.ajax({
			url:'/elect/abstain.do',
			data:{'electId':electid},
			type:'get',
			datatype:'json',
			success:function(response){
				if($('#example').is(':hidden')){
					viewElect(electid);
				}else{
					$elect.find('.tp-p-detail-btn').hide();
					//$elect.find('.tp-p-detail-btn').html('已退出');
					if($elect.find('.tp-p-detail ul>li').length >1){
						$.each(elects, function (i, elect) {
							if (elect.id == response.elect.id) {

								if(elect.eligRight){
									$elect.find('.tp-p-detail-btn').show().html('<button class="tp-btn tp-handle" type="button" onclick="signup(this)">报名</button>');
								}

								for (var j in elect.candidates) {
									var candidate = elect.candidates[j];
									if (candidate.id == userId) {
										elect.candidates.splice(j, 1);
										break;
									}
								}

								$elect.find('.tp-p-detail ul').html(template('candidate-list-template', elect));
								return;
							}
						});
					}else{
						$elect.find('.tp-p-detail ul').html('<span class="no-candidates">目前没有候选人哦~~快快报名吧!</span>');
					}
				}
				if (response.score) {
					scoreManager(response.scoreMsg,response.score);
				}

			},error:function(){
				alert('删除失败!');
			}
		});
	}
}

/**
 * 展示候选人
 * @param button
 */
function expandCandidates(button) {
	$(button).siblings(".brief").removeClass("brief");
}

/**
 * 选择投票选举的范围
 * @param checkbox
 */
function chooseElectRange(checkbox) {
	if (checkbox.checked) {
		if (checkbox.value == 'school') {
			//选择了全校
			$('.catogeryitem').removeAttr('checked');
		} else {
			//选择了班级
			$('#voteCatogery :checkbox:not(.catogeryitem)').removeAttr('checked');
		}
	}
}
//显示 隐藏自定义候选人
function displayAddContainer(checked, obj) {
	if (checked) {
		$('.eligibleCheck:checked').prop('checked', false);
		$('.custom-candidate').slideDown();
		if($('#contact-list').find('.contact-list-index').length <= 0){
			GetContactList();
		}
		$('.eligibleCheck').prop('checked',false);
	}else{
		$('.custom-candidate').slideUp();
		$('#customCandidateCheck').prop('checked',false);
	}
	
}

function eligibleChecked(checked) {
	if (checked) {
		$('#customCandidateCheck').prop('checked', false);
		displayAddContainer(false);
	}
}

//删除发起的投票
function deleteElect(electid) {
	if(confirm("确认删除该投票")){
		$.ajax({
			url:'/elect/delete.do',
			data: {'id': electid},
			type:'get',
			datatype:'json',
			success:function(){
				getVoteList(currentpage-1,20);
			},
			error:function(){
				alert('服务器错误!');
			}
				
		});
	}
}
function editMyInfo(obj){
	var thisCt = $(obj).closest('.tp-intro');
	thisCt.find('#editMyInfo').hide();
	thisCt.find('.bm-toos-res').slideUp();
	thisCt.next('.tp-bm-box').slideDown();
	renderTools();
}
function updateMyInfo(btn,location){
	var $elect = $(btn).closest('.elect');
	var $bm = $(btn).closest('.tp-bm-box');
	$.ajax({
		url:'/elect/updateCandidate.do',
		data:{
			'electId': $elect.attr('elect-id'),
			'id':$bm.prev('.tp-intro').attr('candidate-id'),
			'manifesto': $bm.find('textarea.tp-form-control').val(),
			'picUrls': $.map($bm.find('.candidate-img'), function(img){
				return $(img).attr('src');
			}),
			'videoId':$bm.find('.vote-vedio-container ul li').attr('data-id'),
			'voiceUrl':$bm.find('.vote-audio-container ul p a').attr('url')
		},
		traditional:true,
		type:'post',
		dataType:'json',
		success:function(){
			viewElect($elect.attr('elect-id'),location);
		},
		error:function(){
			
		}
	});
}
function confirmCandidate(){
	$('#custom-container').html($('#recipient').children().clone(true));
	closeDialog1('#customCandidate'); $('#contact-div').fadeOut();
}

function deleteCandidate(obj){
	$(obj).prev('span').remove();
	$(obj).remove();
}
function addCandidate(){
	showDialog1('#customCandidate');
	$('#recipient').html($('#custom-container').children().clone(true));
}