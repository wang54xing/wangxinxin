/**
 * Created by albin on 2017/7/25.
 */

define('baseWeek', ['jquery', 'doT', 'common','Rome', 'pagination', 'layer'], function (require, exports, module) {
    var baseWeek = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    /**全局变量**/
    var t1;
    var t2;
    var t3;
    var t4;
    baseWeek.init=function(){
    	baseWeek.getTermList();
    	baseWeek.initRome();
    	baseWeek.initStudyYear();
    	baseWeek.getTermByYear();
    	baseWeek.getListByXq($("#term").val());
    	// 学期设置
    	$('.btn-xqsz').click(function(){
			$('.wind-xqsz,.bg').fadeIn();
		})
		$('.wind .d1 em,.wind .dbtn .btn-no').click(function(){
			$('.wind,.bg').fadeOut();
		})
		$('.cou-x em').click(function(){
			$('.reset').show();
		});
		$('.d-sty em').click(function(){
			$(this).addClass('cur').siblings().removeClass('cur');
		});
		$('.vonav span').click(function(){
			$(this).addClass('active').siblings('span').removeClass('active')
		})
        $("#start_year").change(function(){
        	var next = (Number)($(this).val())+1;
        	$("#end_year").val(next);
        	baseWeek.getTermByYear();
        });
		$("#save_term").click(function(){
			baseWeek.saveTerm();
			$('.wind,.bg').fadeOut();
		});
        $("#term").change(function(){
        	baseWeek.getListByXq($(this).val());
        });

    }
    baseWeek.getTermList = function(){
    	Common.getData('/new33isolateMange/getTermList.do', {}, function (rep) {
    		  if(rep.code==200){
    			Common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term',overwrite:1});
    		  }
    	  });
    }
    baseWeek.initRome = function(){
    	t1 = rome(dt1,{time:false});
        t2 = rome(dt2,{time:false});
        t3 = rome(dt3,{time:false});
        t4 = rome(dt4,{time:false});
	 }
    baseWeek.initStudyYear = function(){
    	var date = new Date()
    	var year = date.getFullYear();
    	var arr1 = [];
    	var arr2 = [];
    	for(var i=0;i<5;i++){
    		arr1.push({"val":year-(5-i)});
    		arr2.push({"val":year-(4-i)});
    	}
    	arr1.push({"val":year});
    	for(var i=0;i<5;i++){
    		arr1.push({"val":year+i});
    		arr2.push({"val":year+1+i});
    	}
    	Common.render({tmpl: $('#start_year_temp'), data: arr1, context: '#start_year',overwrite:1});
    	Common.render({tmpl: $('#end_year_temp'), data: arr2, context: '#end_year',overwrite:1});
    }
    baseWeek.saveTerm = function(){
    	var year = $("#start_year").val();
    	var start = $("#dt1").val();
    	var end = $("#dt2").val();
    	var start2 = $("#dt3").val();
    	var end2 = $("#dt4").val();
    	if(baseWeek.verify(start,end,start2,end2)==false){
    		return false;
    	}
    	Common.getData('/n33_jxz/saveTerm.do', {"year":year,"start":start,"end":end,"start2":start2,"end2":end2}, function (rep) {
  		  if(rep.code==200){
  			layer.msg("保存成功");
              baseWeek.getListByXq($("#term").val());
  		  }
  	  	});
    }
    baseWeek.getListByXq = function(xqid){
    	Common.getData('/n33_jxz/getListByXq.do', {"xqid":xqid}, function (rep) {
            if(rep.length==0){
                $("#none_png").show();
                $("#content").hide();
            }
            else{
                $("#none_png").hide();
                $("#content").show();
            }
    		Common.render({tmpl: $('#week_temp'), data: rep, context: '#week',overwrite:1});
    	 });
    }
    baseWeek.getTermByYear = function(){
    	var year = $("#start_year").val();
    	Common.getData('/n33_jxz/getTermByYear.do', {"year":year}, function (rep) {
    		if(rep&&rep.length!=0){
    			for(var i in rep){
      			  if(rep[i].ir==0){// 上学期
      				  $("#dt1").val(rep[i].start);
      				  $("#dt2").val(rep[i].end);
      				  t1.setValue(rep[i].start);
      				  t2.setValue(rep[i].end);
      			  }
      			  else if(rep[i].ir==1){
      				  $("#dt3").val(rep[i].start);
    				  $("#dt4").val(rep[i].end);
      				  t3.setValue(rep[i].start);
    				  t4.setValue(rep[i].end);
      			  }
      		  }
    		}
    		else if(rep.length == 0){
    			  baseWeek.initRome();
    			  $("#dt1").val("");
				  $("#dt2").val("");
				  $("#dt3").val("");
				  $("#dt4").val("");
    		}
    	});
    }
    // 校验日期
    baseWeek.verify = function(start1,end1,start2,end2){
    	if(start1==""||end1==""||start2==""||end2==""){
    		layer.alert("日期不能为空");
    		return false;
    	}
    	if(start1>end1||start2>end2||end1>start2){
    		layer.alert("开始时间不能晚于结束时间");
    		return false;
    	}
    }
    module.exports = baseWeek;
})