/**
 * Created by albin on 2017/7/25.
 */

define('stuselect', ['jquery', 'doT', 'common','Rome', 'pagination', 'layer'], function (require, exports, module) {
    var stuselect = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    common = require('common');

    var paikeci = "";
    var flag = true;
    stuselect.init=function(){
    	stuselect.getSubjectGroup();
        getDefaultTerm ();
    	$("body").on("click","#subject span",function(){
    		$(this).addClass('com-cur').siblings().removeClass('com-cur');
    	});
    	$(".submit").click(function(){
    		if(flag){
                stuselect.stuSelect();
			}else{
    			layer.alert("不在选科时间范围内");
			}
    	});
	}
    stuselect.getSubjectGroup = function(){
    	common.getData('/new33school/set/getCurrentSubjectGroup.do', {}, function (rep) {
    		if(rep.relist){
    			common.render({tmpl: $('#subject_temp'), data: rep.relist, context: '#subject',overwrite:1});
    		}
    		if(rep.isOnTime == 1){
				layer.alert("不在选科时间范围内");
				flag = false;
				$(".submit").hide();
			}
    		if(rep.nolist&&rep.nolist.length!=0){
    			var str = "";
        		for(var i in rep.nolist){
        			str+=rep.nolist[i].name+"/"
        		}
        		$("#cannot_select").html("注："+str+"组合不可选");
    		}
    		if(rep.xqid){
    			$("body").attr("xqid",rep.xqid);
    		}
    		if(rep.select){
				$("#subject span[name='"+rep.select+"']").addClass('ck').siblings().removeClass('ck');
			}
    	});
    }
    stuselect.stuSelect = function(){
    	var sub1 =$("#subject span[class='com-cur']").attr("sub1");
    	var sub2 =$("#subject span[class='com-cur']").attr("sub2");
    	var sub3 =$("#subject span[class='com-cur']").attr("sub3");
    	var name = $("#subject span[class='com-cur']").attr("name");
    	if(!sub1){
    		layer.alert("请选择一种组合");
    		return;
    	}
        var flag = getCiIdIsFaBu();
        if(flag){
            layer.alert("课表已发布,不允许修改");
            return;
        }
    	common.getData('/new33school/set/studentSelectSubjectGroup.do', {"xqid":$("body").attr("xqid"),"sub1":sub1,"sub2":sub2,"sub3":sub3,"name":name}, function (rep) {
    		if(rep.code==200){
                stuselect.getSubjectGroup();
    			layer.msg(rep.message);
    		}
    	})
    }
    function getDefaultTerm (){
        common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            paikeci = rep.message.paikeci
        });
    }
    function getCiIdIsFaBu() {
        var flag = null;
        common.getData('/new33isolateMange/getCiIdIsFaBu.do', {"ciId":paikeci}, function (rep) {
            flag = rep.message;
        });
        return flag;
    }
    module.exports = stuselect;
})