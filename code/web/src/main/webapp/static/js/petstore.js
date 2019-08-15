function addControlBar($selector){
	
	$selector.append('<a href="/petbag"><span id="petbag" class="class_item">宠物背包</span></a>');
    $selector.append('<a href="/petCenter"><span id="petcenter" class="class_item">宠物中心</span></a>');
    $selector.append('<a href="/studentScoreList"><span id="scoreList" class="class_item">历史积分</span></a>');
}

$(function(){
    
    var petimg = $('#left_petimg').attr('src');
    if(petimg == ''){
    	var headimg = $('#choosenPet').find('img').attr('src');
    	if(headimg.indexOf('tree') > 0){//提示领取宠物
    		$('#left_petimg').hide();
    		$('#havenopet').show();
    	}else{//显示蛋
    		$('#left_petimg').attr('src','/img/bighatch.jpg');
    	}
    	$('#r_sorry').show();
    	$('.petName').hide();
    }else{
    	$('.petName').show();
    }
    
});

function renameDisplay(obj){
	var $next = $(obj).next('.reviseName');
	if($next.attr('style') == 'display: inline-block;'){
		$next.hide();
	}else{
		$next.show();
		$(obj).next('.reviseName').find('input').focus();
	}
}

function rename(obj,petid){
	var petname = $(obj).prev('input').val();
	$.ajax({
		url:'/pet/updatePetName.do',
		data:{'id':petid,'petname':petname},
		success:function(){
			$(obj).parent().hide();
			$(obj).parent().siblings('em').html('昵称:'+petname);
			$('input[value="'+ petid +'"]').parent('dl').find('em').html(petname);
			var choosePetid = $('#choosePet').val();
			if(petid == choosePetid){
				$('.pet-name').html(petname);
			}
			
		},
		error:function(){
			
		}
	});
}