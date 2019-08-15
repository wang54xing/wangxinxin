


$(function(){
	$(window).load(function(){
		var width = $(window).width();//浏览器宽度
		var bwidth = (width - 1200)/2;//中间内容距离浏览器一边的距离
		var bbwidth =bbwidth-50
		var liwidth = $('.roll-info>ul>li').length*1200;//所有li的总长度
		var left = $('.roll-info-list-b1').parent().offset().left;//距离左边的距离
		$(".roll-info").css('width',width)
		$(".roll-info>ul").css('width',liwidth+bwidth-50);
		$('.roll-info>ul').css('margin-left',-(left+1200-50));
		$('.roll-info-list-b1').parent().css('margin-left',bbwidth);
		$('.roll-info-list').parent().css('margin-left',bwidth-50)
		$('.roll-info-list-b1').click(function(){
			$(this).parent().css('left',1200+bwidth+50)
			$('.roll-info-list').parent().css('left',1200+bwidth+50)
		})
		$('.roll-info-list').parent().click(function(){
			$(this).css('left',100)
			$('.roll-info-list-b1').parent().css('left',100)/*
			$('.roll-info-list-b1').parent().css('margin-left',bbwidth);
		    $(this).css('margin-left',bwidth-50)*/
		})
	})
	/*$(window).resize(function(){
		var width = $(window).width();//浏览器宽度
		var bwidth = (width - 1200)/2;//中间内容距离浏览器一边的距离
		var bbwidth =bbwidth-50
		var liwidth = $('.roll-info>ul>li').length*1200;//所有li的总长度
		var left = $('.roll-info-list-b1').parent().offset().left;//距离左边的距离
		$(".roll-info").css('width',width)
		$(".roll-info>ul").css('width',liwidth+bwidth-50);
		$('.roll-info>ul').css('margin-left',-(left+1200-50));
		$('.roll-info-list-b1').parent().css('margin-left',bbwidth);
		$('.roll-info-list').parent().css('margin-left',bwidth-50)
		$('.roll-info-list-b1').click(function(){
			$(this).parent().css('left',1200+bwidth+50)
			$('.roll-info-list').parent().css('left',1200+bwidth+50)
		})
		$('.roll-info-list').parent().click(function(){
			$(this).css('left',100)
			$('.roll-info-list-b1').parent().css('left',100)*//*
			$('.roll-info-list-b1').parent().css('margin-left',bbwidth);
		    $(this).css('margin-left',bwidth-50)*//*
		})
	})*/
	
})

/*$(function(){
	$(window).load(function(){
		var width = $(window).width();
		var bwidth = (width - 1200)/2;
		var liwidth = $('.roll-info>ul>li').length*1200;
		var left = $('.roll-info-list-b1').parent().offset().left;
		$(".roll-info").css('width',width)
		$(".roll-info>ul").css('width',liwidth);
		$('.roll-info>ul').css('margin-left',-(left+1200-bwidth ));
		$(".roll-info-list-b1").parent().css('left',)
	})
	$(window).resize(function(){
		var width = $(window).width();
		var bwidth = (width - 1200)/2;
		var liwidth = $('.roll-info>ul>li').length*1200;
		var left = $('.roll-info-list-b1').parent().offset().left;
		$(".roll-info").css('width',width)
		$(".roll-info>ul").css('width',liwidth);
		$('.roll-info>ul').css('margin-left',-(left+1200-bwidth ));
	})
	
})*/