'use strict';
define(['jquery','doT','easing','common','select2',"layer"],function(require,exports,module){
    var classManage = {},
        Common = require('common');
    require('layer');
    require('select2');
    var classManageData = {};
    var classroomMap = new Map();
    var classroomInfoMap = new Map();
    var gradeDate = [];
    classManage.init = function(){
        classManageData.page = 1;
        classManageData.pageSize = 10;

        //-------------------------班級--------------------------------------------------------------------------------
        classManage.getGradeList();
        classManage.getClassInfoByGrade($('#classGrades').val());
        //添加班级
        $(".step-4 .newAdd").click(function(){
            $('#cname').text($('#classGrades').find("option:selected").text());
            if (gradeDate!=null && gradeDate.length!=0) {
                for (var i=0;i<gradeDate.length;i++) {
                    if ($('#classGrades').val()==gradeDate[i].id) {
                        $('#classDate').val(gradeDate[i].date);
                        break;
                    }
                }
            }
            $(".newadd-alert,.bg").show();
        });
        $('.sure-class').click(function() {
            classManageData.admissionDate = $('#classDate').val();
            classManageData.classCount = $('#count').val();
            classManageData.gradeId = $('#classGrades').val();
            if ($('#count').val()==0 || $('#count').val()=='') {
                layer.alert("新增班级数大于0！");
                return;
            }
            classManage.addClass();
        });
        //取消
        $(".btn-esc,.alert-r").click(function(){
            $(".alert-top").parent().hide();
            $(".bg").hide();
        })

        $('#classGrades').click(function() {
            classManage.getClassInfoByGrade($('#classGrades').val());
        });
        $('#classGrades2').click(function() {
            classManage.getClassesByGrade($('#classGrades2').val());
        });
        //班级管理编辑导航栏切换
        classManage.tabCheck("class-manage","edit-nav","edit-main","div","edit-cur");
        //返回班级管理首页
        $(".class-manage .path-root").click(function(){
            classManage.getClassInfoByGrade($('#classGrades').val());
            $(".class-manage .edit-con").hide();
            $(".class-manage .show-con").show();
        })
        //更换班主任
        $(".tea-change").click(function(){
            classManage.getSelectTeacherList($('.edit-class-teacher'),function() {
                //年级组长控制
                var childs=$('.edit-class-teacher').children();
                for(var i=0;i<childs.length;i++){
                    if(childs[i].value==$('#masterId').val()){
                        childs[i].selected='selected';
                    }
                }
                $('.edit-class-teacher').select2("destroy");
                initSelect($('.edit-class-teacher'));
            });
            $('#orgTeacherId').val($('#masterId').val());
            $(".teachange-alert,.bg").show();
            $('.teachange-alert').attr('ed-type', 1);
        })
        
        // 弹窗编辑班主任
        $('body').on('click', '.ed-master', function() {
        	var masterId = $(this).parent().attr('mid')
        	classManage.getSelectTeacherList($('.edit-class-teacher'),function() {
                //年级组长控制
                var childs=$('.edit-class-teacher').children();
                for(var i=0;i<childs.length;i++){
                    if(childs[i].value == masterId){
                        childs[i].selected='selected';
                    }
                }
                $('.edit-class-teacher').select2("destroy");
                initSelect($('.edit-class-teacher'));
            });
            $('#orgTeacherId').val(masterId);
            $(".teachange-alert,.bg").show();
            $('.teachange-alert').attr('ed-type', 2);
            $('.teachange-alert').attr('cid', $(this).parent().attr('cid'));
        })
        
        //添加班委
        $(".add-bw").click(function(){
            $("#roleList option:first").prop("selected", 'selected');
            $('#remark').val("");
            $('.myList2 li').removeClass("li-select");
            $(".addbw-alert,.bg").show();
        });
        //添加班委
        $(".sure-roleUser").click(function(){
            var uids = "";
            var bol = false;
            $('.myList2 .li-select').each(function(i) {
                uids += $(this).attr('uid') + ',';
                bol = true;
            });
            if (!bol) {
                layer.alert("请选择添加对象！");
                return;
            }
            classManageData.userIds = uids;
            classManageData.roleId = $('#roleList').val();
            classManageData.remark = $('#remark').val();
            classManageData.classId = $('#classId').val();
            classManage.addStudentRole();

        });
        //学生调班
        $(".member-head .class-edit").click(function(){
            classManage.getGradeList();
            classManage.getClassesByGrade($('#classGrades2').val());
            $(".bg,.classedit-alert").show();
        });
        $('#teacherName').blur(function() {
            if (('#teacherName').val()!='') {
                classManageData.userName = $('#teacherName').val();
                classManage.checkUserName();
            }
        });
        //添加班主任
        $('.sure-leader').click(function() {
        	
        	var type = $('.teachange-alert').attr('ed-type');
        	
        	classManageData.orgTeacherId = $('#orgTeacherId').val();
        	var tid = $('.edit-class-teacher').select2("val");
        	if (tid=='0') {
        		tid=='';
        	}
        	classManageData.teacherId = tid;
        	
        	if (type == "1") {
        		classManageData.classId = $('#classId').val();
        		classManage.addMaster($('#classId').val(), 1);
        	} else if(type == "2") {
        		var cid = $('.teachange-alert').attr('cid');
        		classManageData.classId = $('.teachange-alert').attr('cid');
        		var masterName = $('.edit-class-teacher option[value=' + tid + ']').text();
        		classManage.addMaster(cid, 2, masterName);
        	}
        });
        //班级管理 添加教师
        $('.btn-addT').click(function(){
            classManage.getGradeTeacher($('#gradeId').val());
            $('#className2').text($('#className').val());
            $('.addT-alert2, .bg').fadeIn();
        })
        $('.btn-add').click(function(){
            var userArg = "";
            var subjectArg = "";
            $('.checkGt').each(function() {
                if ($(this).is(':checked')) {
                    userArg += $(this).attr('uid') +",";
                    subjectArg += $(this).attr('suid') +",";
                }
            });
            if (userArg!=''&& subjectArg!='') {
                classManageData.id = $('#classId').val();
                classManageData.userIds = userArg;
                classManageData.subjectIds = subjectArg;
                classManage.addClassTeacher();
            }
        })
        $('#gseach2').click(function() {
            classManage.getGradeTeacher($('#gradeId').val());
        });
        //班级删除老师
        $('.btn-delT').click(function(){
            var ids = "";
            var bol = false;
            $('.classTL .li-select').each(function(i) {
                ids += $(this).attr('ctid') + ',';
                bol = true;
            });
            if (!bol) {
                layer.alert("请选择删除对象！");
                return;
            }
            classManageData.ids = ids;
            layer.confirm('确认删除', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                layer.msg('删除中。。。', {time: 1000});
                classManage.delTeacher();
            }, function () {
            });

        })
        $('.sure-classBase').click(function() {
            if ($('#className').val()=='') {
                layer.alert("班级名称不为空！");
            }
            classManageData.className = $('#className').val();
            classManageData.classSize = $('#classCount').val();
            classManageData.admissionDate = $('#intake').val();
            classManageData.classNumber = $('#index').val();
            classManageData.introduce = $('#slogan').val();
            classManageData.id = $('#classId').val();
            classManageData.roomId = $('#clazz option:selected').attr('cid');
            classManage.updateClass();
        });
        //删除班级学生
        $(".class-del").click(function(){
            var uids = "";
            var bol = false;
            $('.myList .li-select').each(function(i) {
                uids += $(this).attr('uid') + ',';
                bol = true;
            });
            if (!bol) {
                layer.alert("请选择删除对象！");
                return;
            }
            classManageData.userIds = uids;
            classManageData.classId = $('#classId').val();
            classManage.deleteStudent();
        });

        $('.sure-move').click(function() {
            var uids = "";
            var bol = false;
            $('.myList .li-select').each(function(i) {
                uids += $(this).attr('uid') + ',';
                bol = true;
            });
            if (!bol) {
                layer.alert("请选择调班对象！");
                return;
            }
            classManageData.userIds = uids;
            classManageData.classId = $('.classList2').val();
            classManageData.orgClassId = $('#classId').val();
            classManage.moveStudent();
        });

        classManage.getSchoolLoop();

        $('body').on('change', '#loop', function() {
        	var loopId = $(this).find('option:selected').attr('lid');
        	var roomDatas = classroomMap.get(loopId);
        	Common.render({
        		tmpl : "#clazzTmpl",
        		context : "#clazz",
        		data : roomDatas,
        		overwrite : 1
        	});
        })
        
        $('#loop').change();
        
        $('body').on('change', '#loop2', function() {
        	var loopId = $(this).find('option:selected').attr('lid');
        	var roomDatas = classroomMap.get(loopId);
        	Common.render({
        		tmpl : "#clazzTmpl",
        		context : "#clazz2",
        		data : roomDatas,
        		overwrite : 1
        	});
        })
        
        $('body').on('click', '.ed-room', function() {
        	var roomId = $(this).parent().attr('rid');
        	var clazzId = $(this).parent().attr('clazzid');
            if (roomId!="undefined"&&roomId!='') {
            	var info = classroomInfoMap.get(roomId);
            	if (info) {
            		var loopId = info.loopId;
            		$('#loop2 option[lid=' + loopId + ']').prop('selected', true);
            		$('#loop2').change();
            		$('#clazz2 option[cid=' + roomId + ']').prop('selected', true);
            	}
            } else{
                var loopId = $('#loop2').find('option:selected').attr('lid');;
                var roomDatas = classroomMap.get(loopId);
                Common.render({
                    tmpl : "#clazzTmpl",
                    context : "#clazz2",
                    data : roomDatas,
                    overwrite : 1
                });
            }
            $('.edit-room').show();
            $('.edit-room').attr('clazzid', clazzId);
            $('.bg').show();
        })
        
        $('.ed-room-sure').on('click', function() {
        	var roomId = $('#clazz2 option:selected').attr('cid');
        	if (!roomId) {
        		layer.msg('请选择教室！');
        		return;
        	} 
        	var id = $('.edit-room').attr('clazzid');
        	var name = $('#clazz2 option:selected').val();
        	setRoom(id, roomId, name);
        });	
        
    }
    //-----------------------通用--------------------------------------------------------------------------------------------------------------------
    //导航栏切换
    classManage.tabCheck = function(pars,par,wrap,child,className){
        var $li=$("."+pars+" ."+par).find("li");
        var $tab=$("."+pars+" ."+wrap).children(child);
        var len=$li.length;
        $li.click(function(){
            var $index=$(this).index();
            $(this).addClass(className).siblings().removeClass(className);
            $tab.hide();
            $tab.eq($index).show();
        })
    }
    
    function setRoom(id, roomId, name) {
    	var args = {
    		id : id,
    		roomId : roomId
    	}
    	Common.getData('/schoolClass/upRoom.do', args, function(resp) {
    		if (resp && resp.code == '200') {
    			$('.edit-room').hide();
    			$('.bg').hide();
    			var $parent = $('.classList td[clazzid=' + id + ']');
    			$parent.find('span').text(name);
    			$parent.attr('rid', roomId);
    		}
    	})
    }
    
    classManage.getSchoolLoop = function() {
    	Common.getData('/clsroom/getSchoolLoopList.do?page=1&pageSize=100', {}, function(resp) {
    		if (resp && resp.code == '200') {
    			Common.render({
    				tmpl : "#loopTmpl",
    				context : "#loop,#loop2",
    				data : resp.message,
    				overwrite : 1
    			});
    			$.each(resp.message, function(i, item) {
    				var loopId = item.id;
    				var args = {
    					page : 1,
    					pageSize : 100,
    					loopId : loopId
    				}
    				Common.getData('/clsroom/getClassRoomList.do', args, function(resp) {
    					if (resp && resp.code == '200') {
    						classroomMap.set(loopId, resp.message.rows);
    						$.each(resp.message.rows, function(i, item) {
    			        		classroomInfoMap.set(item.id, item);
    			        	});
    					}
    				})
    			});
    		}
    	})
    }
    
    classManage.addClassTeacher = function() {
        Common.getData('/schoolGrade/addGradeTeacher.do', classManageData,function(rep){
            if (rep.code=='200') {
                $(".addteacher-alert,.bg").hide();
                classManage.getClass($('#classId').val());
            }
        });
    }
    classManage.addMaster = function(id, type, name) {
        Common.getData('/schoolClass/addMaster.do', classManageData,function(rep){
            if (rep.code=='200') {
            	if (type == 1) { // 编辑页面
            		classManage.getClass(id);
            	} else if (type == 2) {
            		$('.classList td[cid=' + id + '] span').text(name);
            	}
                $('.teachange-alert,.bg').hide();
            } else {
                layer.alert(rep.message);
            }
        });
    }
    classManage.delTeacher = function() {
        Common.getData('/schoolClass/delTeacher.do', classManageData,function(rep){
            if (rep.code=='200') {
                classManage.getClass($('#classId').val());
            } else {
                layer.alert("添加班委失败！");
            }
        });
    }
    classManage.addStudentRole = function() {
        Common.getData('/schoolClass/addStudentRole.do', classManageData,function(rep){
            if (rep.code=='200') {
                $(".bg,.addbw-alert").hide();
                classManage.getClass($('#classId').val());
            } else {
                layer.alert("添加班委失败！");
            }
        });
    }
    classManage.moveStudent = function() {
        Common.getData('/schoolClass/moveStudents.do', classManageData,function(rep){
            if (rep.code=='200') {
                $(".bg,.classedit-alert").hide();
                classManage.getClass($('#classId').val());
            } else {
                layer.alert("调班失败！");
            }
        });
    }
    classManage.deleteStudent = function() {
        if (confirm('确认删除！')) {
            Common.getData('/schoolClass/deleteStudents.do', classManageData, function (rep) {
                if (rep.code == '200') {
                    classManage.getClass($('#classId').val());
                } else {
                    layer.alert("删除失败！");
                }
            });
        }
    }

    classManage.checkUserName = function() {
        Common.getData('/shoolbase/checkUserName.do', classManageData,function(rep){
            if (rep.code=='200') {
                if (rep.message!='') {
                    $('#teacherId').val(rep.message);
                } else {
                    layer.alert("用户名不存在！");
                    $('#teacherName').val('');
                }
            } else {
                layer.alert(rep.message);
            }
        });
    }

    //-------------年级管理--------------------------------------------------------------------------------------------------------------------------

    classManage.getGradeList = function() {
        Common.getData('/schoolGrade/selGradeList.do', classManageData,function(rep){
            $('#classGrades').html('');
            Common.render({tmpl:$('#classGrades_templ'),data:rep,context:'#classGrades'});
            gradeDate = [];
            if (rep.message!=null &&rep.message.length!=0) {
                for (var i=0;i<rep.message.length;i++) {
                    gradeDate.push({
                        id:rep.message[i].id,
                        date:rep.message[i].admissionDate
                    });
                }
            }

            $('#classGrades2').html('');
            Common.render({tmpl:$('#classGrades2_templ'),data:rep,context:'#classGrades2'});

        });
    }
    classManage.getGradeTeacher = function(id,name) {
        classManageData.id = id;
        classManageData.keyword = $('#gtkeyword').val();
        classManageData.page = 0;
        classManageData.pageSize = 0;
        Common.getData('/schoolGrade/getGradeTeacher.do', classManageData,function(rep){
            if (rep.code=='200') {
                $('.gradeTeacher').html('');
                Common.render({tmpl:$('#gradeTeacher_templ'),data:rep,context:'.gradeTeacher'});
            }
        });
    }
    //-------------------班级-----------------------------------------------------------------------------------------------------------------------------------

    classManage.getClass = function(id) {
        classManageData.id = id;
        Common.getPostData('/schoolClass/selClass.do', classManageData,function(rep){
            if (rep.code == '200') {
                var obj = eval('(' + rep.message + ')');
                //if (index==1) {
                    $('#classId').val(id);
                    $('#masterId').val(obj.message.classDto.masterId);
                    $('#className').val(obj.message.classDto.className);
                    $('#classCount').val(obj.message.classDto.classSize);
                    $('#intake').val(obj.message.classDto.admissionDate);
                    $('#index').val(obj.message.classDto.classNumber);
                    $('#slogan').val(obj.message.classDto.introduce);
                    
                    var roomId = obj.message.classDto.roomId;
                    if (roomId) {
                    	var info = classroomInfoMap.get(roomId);
                    	if (info) {
                    		var loopId = info.loopId;
                    		$('#loop option[lid=' + loopId + ']').prop('selected', true);
                    		$('#loop').change();
                    		$('#clazz option[cid=' + roomId + ']').prop('selected', true);
                    	}
                    }
                    
                $('#masterName').text(obj.message.classDto.masterName);
                $('#masterImage').attr("src",obj.message.classDto.masterImage);
                $('#masterName2').text(obj.message.classDto.masterName);
                $('#masterImage2').attr("src",obj.message.classDto.masterImage);
                $('#gradeId').val(obj.message.classDto.gradeId);
                //} else {
                    $('#myList-nav').empty();
                    $('#myList2-nav').empty();
                    $('#myList').html('');
                    Common.render({tmpl:$('#myList_templ'),data:obj.message.classDto,context:'#myList'});
                    $('#myList2').html('');
                    Common.render({tmpl:$('#myList2_templ'),data:obj.message.classDto,context:'#myList2'});
                    $('.classTL').html('');
                    Common.render({tmpl:$('#classTL_templ'),data:obj,context:'.classTL'});
                    $('#roleList').html('');
                    Common.render({tmpl:$('#roleList_templ'),data:obj.message,context:'#roleList'});
                var obj2 = eval('(' + obj.message.stuRole + ')');
                    $('#userRoleList').html('');
                    Common.render({tmpl:$('#userRoleList_templ'),data:obj2,context:'#userRoleList'});

                //$('.table-edit').click(function() {
                //    classManage.selStudentRole($(this).attr("rid"));
                //});
                $('.table-del').click(function() {
                    if (confirm('确认删除！')) {
                        classManageData.id = $(this).attr("tid");
                        classManageData.userId = $(this).attr("uid");
                        classManage.delStudentRole();
                    }
                });

                $(".classTL li").click(function(){
                    $(this).toggleClass("li-select");
                });
                $('.myList li').click(function(){
                    $(this).toggleClass("li-select");
                });
                $('.myList2 li').click(function(){
                    $(this).toggleClass("li-select");
                });

                $('#myList').listnav({
                    includeOther: true,
                    noMatchText: '',
                    prefixes: ['the', 'a']
                });
                $('#myList2').listnav({
                    includeOther: true,
                    noMatchText: '',
                    prefixes: ['the', 'a']
                });
                //}
                $('.edit-con').show();
                $('.show-con').hide();
            }
        });
    }
    //classManage.selStudentRole = function(id) {
    //    Common.getData('/schoolClass/selStudentRole.do', classManageData,function(rep){
    //
    //    });
    //}
    classManage.delStudentRole = function() {
        Common.getData('/schoolClass/delStudentRole.do', classManageData,function(rep){
            if (rep.code=='200') {
                classManage.getClass($('#classId').val());
            } else {
                layer.alert(rep.message);
            }
        });
    }
    classManage.getClassInfoByGrade = function(id) {
        classManageData.gradeId = id;
        Common.getData('/schoolClass/getClassInfoByGrade.do', classManageData,function(rep){
            if (rep.code=='200') {
                var obj = eval('(' + rep.message + ')');
                    $('.classList').html('');
                    Common.render({tmpl:$('#classList_templ'),data:obj,context:'.classList'});
                    $('#classCnt').text(obj.message.length);
                    $('.table-edit').click(function() {
                        classManage.getClass($(this).attr('cid'));
                    });
                //查看任课老师
                $(".tab-lf").hover(function(){
                    $(this).children(".hide-inf").show();
                },function(){
                    $(this).children(".hide-inf").hide();
                });
                $('.addIndex').click(function () {
                    var index = $(this).attr('idx');
                    if (index==0) {
                        layer.alert("0不可以调节");
                        return;
                    }
                    classManage.updateIndex($(this).attr('cid'),1);
                });
                $('.minIndex').click(function () {
                    var index = $(this).attr('idx');
                    if (index==0) {
                        layer.alert("0不可以调节！");
                        return;
                    } else if (index==1) {
                        layer.alert("已经在第一位了！");
                        return;
                    }
                    classManage.updateIndex($(this).attr('cid'),2);
                })
                    $('.table-del').click(function() {
                        if (confirm('确认删除！')) {
                            classManage.delClassInfo($(this).attr('cid'));
                        }
                    });

            } else {
                layer.alert(rep.message);
            }
        });
    }
    classManage.updateIndex = function(id,type) {
        var data = {};
        data.id = id;
        data.type = type;
        Common.getData('/schoolClass/updateIndex.do', data,function(rep){
            if (rep.code=='200') {
                layer.msg("调节成功！");
                classManage.getClassInfoByGrade($('#classGrades').val());
            } else {
                layer.alert("序号调节失败！");
            }
        });
    }
    classManage.getClassesByGrade = function(id) {
        classManageData.gradeId = id;
        Common.getData('/schoolClass/getClassesByGrade.do', classManageData,function(rep){
            if (rep.code=='200') {
                    $('.classList2').html('');
                    Common.render({tmpl:$('#classList2_templ'),data:rep,context:'.classList2'});
            } else {
                layer.alert(rep.message);
            }
        });
    }
    classManage.addClass = function() {
        Common.getData('/schoolClass/addClass.do', classManageData,function(rep){
            if (rep.code=='200') {
                $(".newadd-alert,.bg").hide();
                classManage.getClassInfoByGrade($('#classGrades').val());
            } else {
                layer.alert(rep.message);
            }
        });
    }

    classManage.updateClass = function() {
        Common.getData('/schoolClass/updateClass.do', classManageData,function(rep){
            if (rep.code=='200') {
                layer.alert("保存成功！");
            } else {
                layer.alert(rep.message);
            }
        });
    }
    classManage.delClassInfo = function(id) {
        classManageData.id = id;
        Common.getData('/schoolClass/delClass.do', classManageData,function(rep){
            if (rep.code=='200') {
                classManage.getClassInfoByGrade($('#classGrades').val());
            } else {
                layer.alert(rep.message);
            }
        });
    }
    classManage.getSelectTeacherList = function(target,callback) {
        target.empty();
        target.append('<option value="0">请选择...</option>');
        Common.getData('/teacher/selTeachersBySchoolId.do', classManageData,function(rep){
            var obj = eval('(' + rep.message + ')');
            for (var i = 0; i < obj.message.length; i++) {
                var content = '';
                content += '<option value=' + obj.message[i].userId + '>' + obj.message[i].userName + '</option>';
                target.append(content);
            }
            callback();
        });

    }
// 初始化select2
    function initSelect(target) {
        target.select2({
            width: '270px',
            containerCss: {
                'margin-left': '10px',
                'font-family': 'sans-serif'
            },
            dropdownCss: {
                'font-size': '14px',
                'font-family': 'sans-serif'
            }
        });
    }

    classManage.init();
});