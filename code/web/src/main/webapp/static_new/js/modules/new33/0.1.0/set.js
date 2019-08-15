/**
 * Created by albin on 2018/3/7.
 */
define('set', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var set = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var pkxq = "";
    var pkc = "";
    set.init = function () {
        $("#term").change(function () {
        	if($("#term").val() == 0){
        		return;
        	}
            set.updateDefaultTerm();
        });
        
        $("body").on("click", "#swqd", function () {
       	 	var par = {};
       	 	par.id = $(this).attr("ids");
       	 	par.desc = $("#swType").val();
       	 	par.xqid = pkxq;
       	 	if(!par.xqid || par.xqid == "" || par.xqid == null){
       	 		layer.msg("请设置排课学期");
       	 		return;
       	 	}
       	 	Common.getData('/n33_set/addSWTypeDto.do', par, function (rep) {
       	 		$("#swType").val("");
       	 		$("#swqd").attr("ids","*");
       	 		$('.bg').hide();
       	 		$('.sw-popup').hide();
       	 		set.getSwTypeList();
       	 	});
        })
        
        $("body").on("click", "#SWList .opts", function () {
            var flag = getXqIdIsFaBu();
            if(flag){
                layer.alert("当前学期已经发布,不允许删除数据!");
                return;
            }
            var par = {};
            par.id = $(this).attr("ids");
            layer.confirm('该事务类型可能存在事务，您确认删除吗？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                Common.getData('/n33_set/removeSWType.do', par, function (rep) {
                    layer.msg(rep.message);
                    set.getSwTypeList();
                });
            },function () {
            });
         });
        
        $("body").on("click", "#opts", function () {
      	  var par = {};
      	  par.id = $(this).attr("ids");
            layer.confirm('确认删除？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
          	  Common.getData('/n33_set/deleteManager.do', par, function (rep) {
            		layer.msg(rep.message);
            		set.getAllManagerList();
                });
            },function () {
            }); 
       });
        
        $("body").on("click", ".ad i", function(){
        	$(".ad i").removeClass("ad-cur");
    		$(this).toggleClass('ad-cur');
    	})
        
        $("body").on("click", ".opt", function () {
        	$("#swqd").attr("ids",$(this).attr("ids"));
        	$("#swType").val($(this).attr("type"));
        	$('.bg').show();
    	 	$('.sw-popup').show();
        });
        
        $("body").on("click", "#mnqd", function () {
        	$('.ssw-popup').hide();
       	 	$('.addn-popup').show();
       	 	$(".ad i").removeClass("ad-cur");
       	 	$(".ad i").attr("ids","*");
       	 	set.getManagerList();
        });
        
        $("body").on("click", "#tj", function () {
        	$('.bg').hide();
        	$('.addn-popup').hide();
        	var tr=	$(".ad-cur").parent().parent();
        	if($(tr).attr("userId") == undefined){
        		layer.msg("请选中管理员");
        		$('.addn-popup').show();
        		set.getManagerList();
        	}else{
        		set.addManager();
           	 	set.getAllManagerList();
        	}
        });
        $('body').on('click','.close',function(){
            $('.bg').hide();
            $('.sw-popup').hide();
            $('.newpk-popup').hide();
            $('.ssw-popup').hide();
        })
        $("body").on("click", ".sel2", function () {
        	set.setIsZouBan($(this));
        });
        $("body").on("click", ".sel3", function () {
        	set.setIsZouBan($(this));
        });
        $("body").on("click", ".sel5", function () {
        	set.setIsZouBan($(this));
        });
        $("#term2").change(function () {
            if($("#term").val() == 0){
                return;
            }
            set.getPaikeTimesByTermId();
            set.getGradeList();
            $("#paikeci input[name='current_paike']").eq($("#paikeci input[name='current_paike']").length-1).prop("checked",true);
            set.updatePaiKeTerm();
            set.getRequireTime();
        });
        $("#savePaikeci").click(function () {
			if($("#savePaikeci").attr("pid")&&$("#savePaikeci").attr("pid")!=""){
                set.updatePaikeTime();
			}
			else{
                set.addPaikeTime();
			}
        });
        // 删除
        $("body").on("click","#paikeci .del",function () {
        	var pid = $(this).attr("pid");
        	if($(this).parent().parent().find("input[name='current_paike']").is(":checked")){
        	    layer.alert("选中的排课不能删除");
                return;
            }
            layer.confirm('您确认删除吗？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                set.removePaiKeTime(pid);
            },function () {
            });

        });
        $('#all').click(function(){
            if($(this).is(':checked')){
                $('#types input').prop('checked',true)
            }else{
                $('#types input').prop('checked',false)
            }
        })
        $("body").on("click","#paikeci .edit",function () {
            $('.bg').show();
            $('.newpk-popup').show();
            $("#description").val($(this).attr("desc"));
            var grades = $(this).attr("grades");
            $("#grade input").each(function () {
				if(grades.indexOf($(this).val())>=0){
					$(this).prop("checked",true);
				}
				else{
                    $(this).prop("checked",false);
				}
            });
            $("#savePaikeci").attr("pid",$(this).attr("pid"));
        });
        $("body").on("click","#paikeci input[name='current_paike']",function () {
            set.updatePaiKeTerm();
        });
        $("#start_year").change(function(){
            var next = (Number)($(this).val())+1;
            $("#end_year").val(next);
            set.getTermByYear();
        });
        $("#save_term").click(function(){
            set.saveTerm();

        });
        $("body").on("click","#term3 .edit",function () {

            $("#start_year").val($(this).attr("year")).change();
            $('.bg').show();
            $('.xj-popup').show();
        });
        $("#synData").click(function () {
            var pid = $(this).attr("pid");
            layer.confirm('同步数据会删除当前次数据，是否同步？', {
                btn: ['确定', '取消'] ,//按钮
            }, function () {
                layer.closeAll();
                // setTimeout(function () {
                //     synPaiKeData(pid);
                // },1000)
                synPaiKeData(pid);

            },function () {
            });
        });
        $('body').on('click','.tbb',function(){
            $("#synData").attr("pid",$(this).attr("pid"));
            getTimesList($(this).attr("pid"));
            Common.getData('/n33_syndata/getSynRecord.do', {"ciId":$(this).attr("pid")}, function (rep) {
                if(rep.list){
                    $("#types input").each(function () {
                       if(jQuery.inArray(Number($(this).val()), rep.list)>=0){
                            $(this).prop("checked",true);
                       }
                       else{
                           $(this).prop("checked",false);
                       }
                    });
                }
                else{
                    $("#types input").each(function () {
                        $(this).prop("checked",false);
                    })
                }
                if(rep.old){
                    $("#synSource").val(rep.old);
                }
            });
            $('.bg').show();
            $('.tbb-popup').show();
        })

        $('body').on("click",'#qd',function () {
            var start = $("#time1").val();
            var end = $('#time2').val();
            set.addOrUpdateRequireTime(start,end);
            set.getRequireTime();
        });

        $("body").on("click","#allow",function () {
            set.updateTurnOff(1);
        });

        // $("body").on("click","#tdk",function () {
        //     set.updateTurnOff(2);
        // });

        $("#gradeTurnOff").change(function () {
            set.getTurnOff(1);
            set.getDESCForNoCourse();
            // set.getTurnOff(2);
        });

        $('body').on('click','#descQD',function () {
            if($(this).text() == "编辑"){
                $('#desc').attr("disabled",false);
                $(this).text("确定");
            }else{
                $(this).text("编辑");
                $('#desc').attr("disabled",true);
                set.saveDESCForNoCourse();
            }
        });

        $('body').on('change','#kbType',function () {
            set.getDESCForNoCourse();
        })

        $("body").on("change","#afreshSet",function(){
            console.log("11");
            if( $(this).prop('checked')){
                set.updateAcClassToTurnOff(1,1);
            }else{
                set.updateAcClassToTurnOff(1,0);
            }
        })

        set.getTermList();
        set.getYearList();
        set.getDefaultTerm();
        set.getSwTypeList();
        set.getAllManagerList();
        set.getAllZouBanSubject();
        set.getPaikeTimesByTermId();
        set.getGradeList();
        set.initRome();
        set.initStudyYear();
        set.getTermByYear();
        set.getRequireTime();
        set.getTurnOff(1);
        set.getTurnOff(2);
        set.getDESCForNoCourse();

    }
    set.updateAcClassToTurnOff = function(type,acClass){
        var par = {};
        par.ciId = pkc;
        par.gradeId = $("#gradeTurnOff").val();
        par.type = type;
        par.acClass = acClass;
        /*@RequestParam String ciId,
         @RequestParam String gradeId,@RequestParam Integer type,@RequestParam Integer acClass*/
        Common.getData('/n33_set/updateAcClassToTurnOff.do', par, function (rep) {

        });

    }
    
    set.setIsZouBan = function (tr) {
    	var par = {};
    	par.xqid = pkc;
    	par.subId = tr.attr("subId");
    	par.gid = tr.attr("gid");
    	if(tr.is(":checked")){
    		par.isZouBan = 1;
    	}else{
    		par.isZouBan = 0;
    	}
    	Common.getData('/n33_set/setIsZouBan.do', par, function (rep) {
    		set.getAllZouBanSubject();
    		set.getDefaultTerm();
    	});
	}
    
    set.getAllZouBanSubject = function () {
    	var par = {};
    	if(pkc != "" && pkc != null && pkc != undefined){
    		par.xqid = pkc;
        	Common.getData('/n33_set/getAllZouBanSubject.do', par, function (rep) {
        		Common.render({tmpl: $('#subjectList'), data: rep.message, context: '#subList', overwrite: 1});
        		$.each(rep.message, function (i, obj) {
                    $("#subList tr[subId" + obj.subjectId + "]").attr("subId",obj.subjectId);
                    
                })
        		
        	});
    	}
	}
    
    set.getAllManagerList = function () {
    	Common.getData('/n33_set/getAllManagerList.do', {}, function (rep) {
    		Common.render({tmpl: $('#manageList'), data: rep.message, context: '#maList', overwrite: 1});
    		
    	});
    }
    
    set.addManager = function () {
    	var dto = {};
    	var tr=	$(".ad-cur").parent().parent();
    	dto.id = $(tr).attr("ids");
    	dto.userId = $(tr).attr("userId");
    	dto.userName = $(tr).attr("userName");
    	dto.contactWay = $(tr).attr("contactWay");
    	Common.getPostBodyData('/n33_set/addManager.do', dto, function (rep) {
    		$("#mname").val("");
        });
    	
    }
    
    set.getManagerList = function () {
    	var par = {};
    	par.userName = $("#mname").val();
    	Common.getData('/n33_set/getManagerList.do', par, function (rep) {
    		Common.render({tmpl: $('#managerList'), data: rep.message, context: '#manaList', overwrite: 1});
    		if(rep.message.length == 0){
    			layer.msg("暂无对应的职工可添加");
    			$('.ssw-popup').show();
    			$('.addn-popup').hide();
    		}
    		$.each(rep.message, function (i, obj) {
                $("#manaList tr[userId=" + obj.userId + "]").attr("userId", obj.userId);
                $("#manaList tr[userId=" + obj.userId + "]").attr("userName", obj.userName);
                $("#manaList tr[userId=" + obj.userId + "]").attr("contactWay",obj.contactWay);
            })
    	});
    }
    
    set.getSwTypeList = function () {
    	var par = {};
    	par.xqid = pkxq;
    	if(!par.xqid || par.xqid == "" || par.xqid == null){
    		Common.render({tmpl: $('#SWLXList'), data: [], context: '#SWList', overwrite: 1});
    		return;
    	}
    	Common.getData('/new33isolateMange/getSwTypeListByXqid.do', par, function (rep) {
    		Common.render({tmpl: $('#SWLXList'), data: rep.message, context: '#SWList', overwrite: 1});
    	
    	});
    }
    
    set.getDefaultTerm = function () {
    	Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
        		$("#term option[value=" + rep.message.xqid + "]").attr("selected","selected");
        		deXqid = rep.message.xqid;
        		$("#defaultTerm").text(rep.message.paikeciname);

        		if(rep.message.paikexq){
                    $("#term2 option[value=" + rep.message.paikexq + "]").attr("selected","selected");
                    pkxq = rep.message.paikexq;
                    pkc = rep.message.paikeci;
                }
    	});
    }
    
    set.updateDefaultTerm = function(){
    	var dto = {};
    	dto.xqid = $("#term").val();
    	Common.getPostBodyData('/n33_set/updateDefaultTerm.do', dto, function (rep) {
    		set.getDefaultTerm();
    		set.getAllZouBanSubject();
        });
    }
    
    set.getTermList = function () {
        Common.getData('/new33isolateMange/getTermList.do', {}, function (rep) {
            if (rep.code == 200) {
                Common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term', overwrite: 1});
                Common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term2', overwrite: 1});
                Common.render({tmpl: $('#term2_temp'), data: rep.message, context: '#term3', overwrite: 1});
                /*$("#term").append("<option value='"+'0'+"' selected = 'selected'>"+'请选择:'+"</option>"); */
            }
        });
    }
    set.getYearList = function () {
        Common.getData('/new33isolateMange/getYearList.do', {}, function (rep) {
            if (rep.code == 200) {
                Common.render({tmpl: $('#term2_temp'), data: rep.message, context: '#term3', overwrite: 1});
            }
        });
    }
    set.getPaikeTimesByTermId = function(){
    	var termId = $("#term2").val();
        Common.getData('/n33_set/getPaikeTimesByTermId.do', {"termId":termId}, function (rep) {
            if (rep.code == 200) {
                Common.render({tmpl: $('#paikeci_templ'), data: rep.message, context: '#paikeci', overwrite: 1});
            }
            $("#paikeci input[name='current_paike']").each(function () {
                if($(this).val()==pkc){
                    $(this).prop("checked",true);
                }

            })
        });
	}
    set.getGradeList = function () {
        Common.getData('/new33isolateMange/getGradListOrigin.do', {}, function (rep) {
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade', overwrite: 1});
            Common.render({tmpl: $('#gradeTurnOff_temp'), data: rep.message, context: '#gradeTurnOff', overwrite: 1});
        })
    }
    set.addPaikeTime = function(){
        var termId = $("#term2").val();
        var dto = {};
        var maxsn = 0;
        $("#paikeci tr").each(function () {
			if(Number($(this).attr("sn"))>maxsn){
                maxsn = Number($(this).attr("sn"));
			}
        });
        dto.serialNumber = maxsn+1;
        dto.description = $("#description").val();
        dto.gradeIds = [];
        $("#grade input").each(function () {
        	if($(this).is(":checked")){
                dto.gradeIds.push($(this).val());
			}
        });
        Common.getPostBodyData('/n33_set/addPaikeTime.do?termId='+termId, dto, function (rep) {
            if (rep.code == 200) {
                $('.bg').hide();
                $('.newpk-popup').hide();
                layer.msg("添加成功");
            }
            set.getPaikeTimesByTermId();
        });
    }
    set.updatePaikeTime = function(){
        var termId = $("#term2").val();
        var timeId = $("#savePaikeci").attr("pid")
        var dto = {};
        dto.description = $("#description").val();
        dto.gradeIds = [];
        $("#grade input").each(function () {
            if($(this).is(":checked")){
                dto.gradeIds.push($(this).val());
            }

        });
        Common.getPostBodyData('/n33_set/updatePaikeTime.do?termId='+termId+"&timeId="+timeId, dto, function (rep) {
            if (rep.code == 200) {
                $('.bg').hide();
                $('.newpk-popup').hide();
                layer.msg("保存成功");
            }
            set.getPaikeTimesByTermId();
        });
    }
    set.removePaiKeTime = function (timeId) {
        var termId = $("#term2").val();
        Common.getData('/n33_set/removePaiKeTime.do?termId='+termId+"&timeId="+timeId, {}, function (rep) {
            layer.msg("删除成功");
        	set.getPaikeTimesByTermId();
        })
    }
    set.updatePaiKeTerm = function () {
        var paikeTermId = $("#term2").val();
        var paikeciId = null;
        $("#paikeci input[name='current_paike']").each(function () {
            if($(this).is(":checked")){
                paikeciId = $(this).val();
            }
        });
        if(paikeciId==null){
            return;
        }
        Common.getData('/n33_set/updatePaiKeTerm.do?paikeTermId='+paikeTermId+"&paikeciId="+paikeciId, {}, function (rep) {
            set.getDefaultTerm();
            set.getPaikeTimesByTermId();
        })
    }
    set.initRome = function(){
        t1 = rome(dt1,{time:false});
        t2 = rome(dt2,{time:false});
        t3 = rome(dt3,{time:false});
        t4 = rome(dt4,{time:false});
    }
    set.initStudyYear = function(){
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
    set.saveTerm = function(){
        var year = $("#start_year").val();
        var start = $("#dt1").val();
        var end = $("#dt2").val();
        var start2 = $("#dt3").val();
        var end2 = $("#dt4").val();
        var yearName = $("#yearName").val();
        if(set.verify(start,end,start2,end2)==false){
            return false;
        }
        Common.getData('/n33_jxz/saveTerm.do', {"year":year,"start":start,"end":end,"start2":start2,"end2":end2,"yearName":yearName}, function (rep) {
            if(rep.code==200){
                layer.msg("保存成功");
                $('.xj-popup').hide();
                $('.bg').hide();
                // set.getListByXq($("#term").val());
                set.getYearList();
            }
        });
    }
    set.getListByXq = function(xqid){
        Common.getData('/n33_jxz/getListByXq.do', {"xqid":xqid}, function (rep) {
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week',overwrite:1});
        });
    }

    set.getTermByYear = function(){
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
                    $("#yearName").val(rep[0].sy);
                }
            }
            else if(rep.length == 0){
                set.initRome();
                $("#dt1").val("");
                $("#dt2").val("");
                $("#dt3").val("");
                $("#dt4").val("");
            }
        });
    }
    set.getTermByYear2 = function(year){
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

        });
    }

    set.getRequireTime = function () {
        Common.getData('/teaRules/getRequireTime.do', {"xqid":pkxq}, function (rep) {
            $("#time1").val(rep.message.start);
            $("#time2").val(rep.message.end);
        });
    }

    set.addOrUpdateRequireTime = function (start,end) {
        if(start == "" || end == ""){
            layer.alert("日期不能为空");
        }else if(start>end){
            layer.alert("开始时间不能晚于结束时间");
        }else{
            var par = {};
            par.start = start;
            par.end = end;
            par.xqid = pkxq;
            Common.getData('/teaRules/addOrUpdateRequireTime.do', par, function (rep) {
                if(rep.code==200){
                    layer.msg("设置成功");
                }else{
                    layer.msg("服务器正忙");
                }
            });
        }
    }

    // 校验日期
    set.verify = function(start1,end1,start2,end2){
        if(start1==""||end1==""||start2==""||end2==""){
            layer.alert("日期不能为空");
            return false;
        }
        if(start1>end1||start2>end2||end1>start2){
            layer.alert("开始时间不能晚于结束时间");
            return false;
        }
    }

    set.updateTurnOff = function (type) {
        var par = {};
        par.ciId = pkc;
        par.gradeId = $("#gradeTurnOff").val();
        if (type==1) {
            if($("#allow").is(":checked")){
                par.status = 0;
            }else{
                par.status = 1;
            }
        } else {
            if($("#tdk").is(":checked")){
                par.status = 0;
            }else{
                par.status = 1;
            }
        }

        par.type = type;
        Common.getData('/n33_set/updateTurnOff.do', par, function (rep) {
            if(rep.code == 200){
                layer.msg(rep.message);
            }
        });
    }

    set.saveDESCForNoCourse = function () {
        var par = {};
        par.ciId = pkc;
        par.gradeId = $("#gradeTurnOff").val();
        par.desc = $('#desc').val();
        par.type = 3;
        par.kbType = $('#kbType').val();
        Common.getData('/n33_set/saveDESCForNoCourse.do', par, function (rep) {
            if(rep.code == 200){
                layer.msg("设置成功");
            }
        });
    }

    set.getDESCForNoCourse = function () {
        var par = {};
        par.ciId = pkc;
        par.gradeId = $("#gradeTurnOff").val();
        par.type = 3;par.kbType = $('#kbType').val();
        Common.getData('/n33_set/getDESCForNoCourse.do', par, function (rep) {
            $('#desc').val(rep.message)
        });
    }



    set.getTurnOff = function (type) {
        if (type==1) {
            $("#allow").prop("checked", false);
        } else {
            $("#tdk").prop("checked",false);
        }
        var par = {};
        par.ciId = pkc;
        par.gradeId = $("#gradeTurnOff").val();
        par.type = type;
        Common.getPostData('/n33_set/getTurnOff.do', par, function (rep) {
            if(rep.message.status == 0){
                if (type==1) {
                    $("#allow").prop("checked",true);
                } else {
                    $("#tdk").prop("checked",true);
                }
            }
            if(type==1){
                if(rep.message.acClass==1){
                    $("#afreshSet").prop('checked','true');
                }else{
                    $("#afreshSet").removeAttr("checked");
                }
            }
        });
    }


    function synPaiKeData(newCiId){
        var arr = [];
        $("#types input").each(function () {
            if($(this).is(":checked")){
                arr.push($(this).val());
            }
        })

        var oldCiId = $("#synSource").val();
        if(oldCiId==null){
           layer.alert("请选择一个数据源");
           return;
        }
        var newCiId = newCiId;
        $('.bg').show();
        $(".jhh").show();
        $.ajax({
            type: "post",
            data:JSON.stringify(arr),
            url: '/n33_syndata/synAllData.do?oldCiId='+oldCiId+"&newCiId="+newCiId,
            async: true,
            cache : false,
            contentType: 'application/json',
            success: function(rep){
                if(rep.code==500){
                    $(".load-popup").hide();
                    $('.bg').hide();
                    layer.alert("同步出现异常");
                }
            }
        });
        $('.tbb-popup').hide();
        c = setInterval(getStatus,1);

    }

    function getStatus(){
        $.ajax({
            type: "GET",
            url: '/n33_syndata/getStatus.do',
            async: false,
            cache : false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function(rep){
                if(rep.end==1){
                    $('.bg').hide();
                    $(".jhh").hide();
                    window.clearInterval(c);
                    layer.msg("同步成功");
                }
                $(".jhhSp").html(rep.text);
            }
        });
    }
    function getTimesList(ciId){
        Common.getData('/new33isolateMange/getTermPaikeTimes.do', {}, function (rep) {
            if(rep.code==200){
                var arr = [];
                for(var i in rep.message){
                    if(rep.message[i].ciId!=ciId){
                        arr.push(rep.message[i]);
                    }
                }
                Common.render({tmpl: $('#synSource_temp'), data: arr, context: '#synSource',overwrite:1});

            }
        });
    }

    function getXqIdIsFaBu() {
        var par = {};
        var flag = null;
        par.xqid = pkxq;
        Common.getData('/new33isolateMange/getXqIdIsFaBu.do', par, function (rep) {
            flag = rep.message;
        });
        return flag;
    }
    module.exports = set;
})