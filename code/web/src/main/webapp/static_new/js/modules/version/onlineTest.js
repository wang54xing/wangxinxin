$(function(){
    $("body").on("click",".alert-tab ul li",function(){
        $(this).addClass("active").siblings().removeClass("active");
    })
    //关闭弹窗
    $(".btn-esc,.alert-r").on("click",function(){
        $(this).parent().parent().fadeOut();
        $(".bg").fadeOut();
    })
    $("body").on("click",".object-select",function(){
        $(".add-alert,.bg").fadeIn();
    })
    //批改
    $("body").on("click",".table-total .table-modify",function(){
        $(".table-total").hide();
        $(".path-info,.table-single").show();
    })
    //返回
    $("body").on("click",".test-list .back-prev",function(){
        $(this).parent().hide();
        $(".table-single").hide();
        $(".table-total").show();
    })
    //导航栏切换
    function tabCheck(par,wrap,child,className){
		var $li=$("."+par).find("li");
	    var $tab=$("."+wrap).children(child);
	    var len=$li.length;
	    $li.click(function(){
	        var $index=$(this).index();
	        $(this).addClass(className).siblings().removeClass(className);
	        $tab.hide();
	        $tab.eq($index).show();
	    })

	}
	//答题卡
	tabCheck("card-top","answer-type","div","active");
    //题目标记
    $("body").on("click",".stu-detail-main .iconfont",function(e){
        e.stopPropagation();
        $(this).toggleClass("active");
    })
    //全局设置
    $("body").on("click",".global-set",function(){
        $(this).children(".set-detail").toggle();
    })
    $(".global-set .set-detail").on("click",function(e){
        e.stopPropagation();
    })
    $(".set-detail .save-btn").on("click",function(e){
        e.stopPropagation();
        $(".set-detail").hide();
    })
    $("body").on("click",".table-single .table-modify",function(){
        $(".content .top-operate,.list-con").hide();
        $(".top-fix,.detail-con").show();
    })
    //返回
    $("body").on("click",".paper-detail .back-prev",function(){
        $(".top-fix,.detail-con").hide();
        $(".content .top-operate,.list-con").show();
    })

    $("body").on("click",".card-main>dl",function(){
        $(this).addClass("active").siblings().removeClass("active");
    })
})