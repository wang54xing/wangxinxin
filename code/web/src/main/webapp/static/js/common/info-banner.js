/**
 * Created by Niu Xin on 14-10-14.
 */

function changePetName() {
    if(!$('.input-name').hasClass('exist')) {
        $('.input-name').val($('.pet-name').text()).show();
        $('.input-name').focus();
        $('.input-name').addClass('exist');
        $('.input-name').val($.trim($('.input-name').val()));
        $('.pet-name').hide();
    }
}

function petNameInputKeyDown(e) {
    if(e.which==13)
        doChangePetName();
}

function petNameInputBlur(event) {
    if($('.input-name').hasClass('exist')) {
        doChangePetName();
    }
}

function doChangePetName() {
    if($('.input-name').val() == '' || $('.input-name').val() == $('.pet-name').text()) {
        $('.input-name').hide();
        $('.input-name').removeClass('exist');
        $('.input-name').val($('.pet-name').text());
        $('.pet-name').show();
    }else if($('.input-name').val().length > 20) {
        alert('长度限制20个字符！');
        $('.input-name').focus();
    }else{
        $('.input-name').hide();
        var id = $('#petid').val();
        if(!id){
        	$.ajax({
                url:'/reverse/updatePetName.action',
                type:'post',
                dataType:'json',
                data:{
                    petname: $('.input-name').val()
                },
                success:function() {
                    $('.pet-name').text($('.input-name').val());
                    $('.input-name').removeClass('exist');
                },
                error:function() {
                    alert('改名失败。');
                },
                complete:function() {
                    $('.pet-name').show();
                }
            });
        }else{
        	$.ajax({
        		url:'/pet/updatePetName.do',
        		data:{'id':id,'petname':$('.input-name').val()},
        		success:function(){
        			$('.pet-name').text($('.input-name').val());
                    $('.input-name').removeClass('exist');
        		},
        		error:function() {
                    alert('改名失败。');
                },
                complete:function() {
                    $('.pet-name').show();
                }
        	});
        }
        
    }
}
var isRefresh = true;
$(function(){
	var prev = 1;
	function show(){
		var msgId = generateRandom();
		while(prev == msgId){
			msgId = generateRandom();
		}
		if(prev != msgId){
			prev = msgId;
			var msg = $('#petmsg-list').children('li[data-id="'+msgId+'"]').html();
			$('#talkmsg').html(msg);
		}
		
	}

//	$('#choosenPet').mouseover(function(){
//		if($('#pettalk_area').is(':hidden') && isRefresh){
//			$('#pettalk_area').show();
//			show();
//		}
//	});
	
	$('#msgclose').click(function(){
		$('#pettalk_area').hide();
		isRefresh = false;
	});
	//
//	if(schoolId == 467){
//	var cookie = getCookie('newyear');
//	if( cookie != '2015'){
//        $('#bg').after('<div id="aiguo" class="bg" style="display: none;background-color: rgba(0,0,0,0.5);opacity: 1;position: relative;"><i onclick="$(this).parent().hide();" class="fa fa-times-circle fa-lg" style="color: #fff;cursor: pointer;z-index:1000;"></i><img src="/img/ag215.gif" style="width:600px;height:300px;"></div>');
//		$('#aiguo').show();
//		var winH = -$('#aiguo img').height()*0.5;
//		var winW = -$('#aiguo img').width()*0.5;
//		$('#aiguo img').css({'position':'fixed','top':'50%','left':'50%'});
//		$('#aiguo img').css({'margin-left':winW,'margin-top':winH});
//		var imgpst = $('#aiguo img').position();
//		$('#aiguo i').css({'position':'absolute','top':imgpst.top-160,'left':imgpst.left+290});
//		//写入cookie
//		setCookie('newyear','2015','30*24*60*60*1000');
//	}
//		
//	}
	
});
function generateRandom(){ 
    var rand = parseInt(Math.random()*98); 
    return rand;
}

function getPetInfo(){
    $.ajax({
        url:'/pet/selectedPet.do',
        success:function(data){
            var petinfo = data.petInfo;
            if(petinfo != null){
                $('#choosenPet').find('img').attr('src',petinfo.petimage);
                $('#petid').val(petinfo.id);
                $('.pet-name').html(petinfo.petname);
                $('.pet-name').attr('title',petinfo.petname);
                /* $('#choosenPet').parent('a').attr('href','/petbag'); */
            }else{
                $('#choosenPet').find('img').attr('src','/img/egg.png');
            }
        },
        error:function(){
        }

    });
}
function lookDialog(){
	MessageBox('', 3, 'gotolook', ' 老师们还在为不会制作课程而烦恼吗？K6KT录课神器上线啦，全网首创一站式课程制作，可同步智能推送给学生。老师们可在K6KT中“我的微课”页面, 顶部点击“录制神器”图标录制微课，老师们赶紧试试吧，同学们都在期待老师的课程呢!');
}
function gotoVotePage(){
    showContent('discuss');
	$('.my_vote').trigger('click');
}
function showBoneRule(){
	$('#ad_bones_content').show();
	var d = dialog({
        title: '亲，该页面仅老师可见哦~',
        content: $('#ad_bones_content'),
        width: 700,
        height: 498,
        cancelValue: '确定',
        cancel: function () {
        	$('#ad_bones_content').hide();
        	d.close();
            return false;
        }
    });
	$('.ui-dialog-title').css('text-align','center');
    d.showModal();
}