function scoreManager(msg, score){
	score = (score > 0)? '+'+score:score;
	var msgShow = '<div id="msgScore" style="position: fixed;border: 4px solid #c9c9c9;height: 20px;width: 300px;overflow:hidden;top: 40%;left: 50%;margin-left:-150px;text-align: center;background: #fff;padding: 30px;color: #565656;font: 600 14px \'Microsoft YaHei\';z-index:100;"><p>'+msg+'! <strong style="color:#f80;">'+score+'</strong>经验值'+'</p></div>';
	$('body').prepend(msgShow);
	$('#msgScore').fadeOut(4000,function(){
		$('#msgScore').remove();
	});
}