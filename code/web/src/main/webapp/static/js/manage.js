$(function() {
    var $manageList = $('.manage-select>li');

    initHeight();
    getSubjectList();
    getSchoolInfo();
    $('.subject-list').show();
    $('.class-right-title-bar').hide();
    $('.right-title-bar').hide();
    window.onresize = function() {
        initHeight();
    }
    
    $('.manage-tianjia').each(function(i){
        		if(i==0){
                    $(this).addClass('manage-hover');
                    $('.interest-class-add-student').attr('tid', 1);
                    $('.loading-img').fadeIn('fast');
                }
                $(this).click(function(){
                    $('.manage-tianjia').removeClass('manage-hover');
                    $(this).addClass('manage-hover');
                    $('.header').nextAll().remove();
                    $('.loading-img').fadeIn('fast');
                     if(i==0) {
                     	 $('.interest-class-student-list').show();
                     	 $('.second-interest-class-student-list').hide();
                     	 $('.interest-class-add-student').attr('tid', 1);
                     } else {
                     	 $('.interest-class-student-list').hide();
                     	 $('.second-interest-class-student-list').show();
                     	 $('.interest-class-add-student').attr('tid', 2);
                     }
                    
                    
                    
                });
        	});

    // 各个列表的修改项
    $('body').on('click', '.list-edit', function() {
        var belong = $(this).parents('.list-container').attr('belong');
        selectEditModal(belong, $(this));
    });
    // 各个列表的删除项
    $('body').on('click', '.list-delete', function() {
        var belong = $(this).parents('.list-container').attr('belong');
        selectDeleteForm(belong, $(this));
    });

    // 关闭模态框
    $('.close-modal').on('click', function() {
        hideAlert();
    });

    // 导航栏切换

    $manageList.on('click', function() {
        menuChange(this);
    });

    // 升级
    $('.title-update').on('click', function() {
        showAlert();
        $('.update').show();
        $('.edit-commit-btn').on('click', function() {
            resetEditContainer();
            $('.update-success').show();
            $('.edit-commit-btn').on('click', function() {
                hideAlert();
            });
        });
    });

    // 修改默认密码
    $('.title-resetpassword').on('click', function() {
        showAlert();
        $('.edit-password').show();
        setPassword();
    });

    // 修改学校信息
    $('.title-edit').on('click', function() {
        showAlert();
        editSchoolInfo();

    });

    // 导入数据
    $('.upload-user-info').change(function(event) {
        $('#upload-form').submit();
    });

    // checkbox全选/反选
    $('body').on('change', '.edit-select-all', function() {
        if ($(this).is(':checked')) {
            $('.edit-subject-selectgrade input:checkbox').prop('checked', true);
            $('.edit-subject-selectgrade-interest input:checkbox').prop('checked', true);
        } else {
            $('.edit-subject-selectgrade input:checkbox').prop('checked', false);
            $('.edit-subject-selectgrade-interest input:checkbox').prop('checked', false);
        }
    });

    $('body').on('change', '.edit-subject-selectgrade input:checkbox', function() {
        if (!$(this).is(':checked')) {
            $('.edit-select-all').prop('checked', false);
        }
    });

    // 双击年级进入班级
    $('body').on('click', '.grade-list .list-content', function() {
        var gid = $(this).parent().attr('gid');
        $('.list-container').hide();
        $('.class-list .list-title-grade').text($(this).find('span').eq(0).text());
        $('.class-list .list-add-class').attr('gid', gid);
        getClassList(gid);
        $('.class-list').show();
    });

    // 双击班级进入班级详情
    $('body').on('click', '.class-list .list-content', function() {
        var cid = $(this).parent().attr('cid');
        var gid = $(this).parent().attr('gid');
        $('.class-add-teacher,.class-add-student').attr('cid', cid);
        $('.list-container').hide();
        $('.pre-class-list .list-title-class').text($(this).find('span').eq(0).text());
        $('.list-title-grade-pre').text($('.list-title-grade').text());
        $('.list-title-grade-pre').click(function() {
            $('.list-container').hide();
            getClassList(gid);
            $('.class-list').show();
            $('.list-title-grade-pre').unbind();
        });
        getClassUserList(cid);
        $('.pre-class-list').show();
    });
    
     // 双击兴趣班进入学生列表
    $('body').on('click', '.interest-list .list-content', function() {
    	var cid = $(this).parent().attr('cid');
    	$('.interest-class-add-student').attr('cid', cid);
    	$('.interest-list').hide();
    	getInterestClassUserList(cid);
    	$('.interest-class-list').show();
    });

    // 添加老师
    $('.list-add-teacher').on('click', function() {
        $('.edit-teacher .edit-title-bar').find('div').text('添加老师');
        $('.edit-teacher').find('input:text').val('');
        $('.edit-teacher-username').attr('disabled',false);
        $("#ismanage").attr("checked",false);
        var target4 = $('.user-techuser');
        target4.empty();
        showEditModal($('.edit-teacher'));
        addTeacher();
    });

    //添加年级
    $('.list-add-grade').on('click', function() {
        $('.edit-grade .edit-title-bar').find('div').text('添加年级');
        $('.edit-grade').find('input').val('');
        $.ajax({
            url: '/reverse/showGradeInfo.action',
            type: 'post',
            dataType: 'json',
            data: {},
            success: function(data) {
                var row = data.GradeIdList;
                if (row!=null && row.length!=0) {
                	$('.edit-grade-value').empty();
                    for (var i = 0; i < row.length; i++) {
                        var content = '';
                        content += '<option value=' + row[i].status + '>' + row[i].description + '</option>';
                        $('.edit-grade-value').append(content);
                    }	
                    getSelectTeacherList($('.edit-grade-teacher'), function() {
                        $('.edit-grade-teacher').select2("destroy");
                        initSelect($('.edit-grade-teacher'));
                        showEditModal($('.edit-grade'));
                    });
                } else {
                	alert("请先选择学校学段！");
                	showAlert();
                	editSchoolInfo();
                }
            },
            error: function() {
                console.log('showGradeInfo error');
            }
        });
        addGradeInfo();
    });

    // 添加班级
    $('.list-add-class').on('click', function() {
        var gid = $(this).attr('gid');
        $('.edit-class .edit-title-bar').find('div').text('添加班级');
        $('.edit-class').find('input').val('');
        $('.edit-class-grade').text($('.class-list .list-title-grade').text());
        getSelectTeacherList($('.edit-class-teacher'), function() {
            $('.edit-class-teacher').select2("destroy");
            initSelect($('.edit-class-teacher'));
            showEditModal($('.edit-class'));
        });
        addClassInfo(gid);
    });

    // 添加科目
    $('.list-add-subject').on('click', function() {
        $.ajax({
            url: '/reverse/selSchoolGradeInfo.action',
            type: 'post',
            dataType: 'json',
            data: {},
            success: function(data) {
                var row = data.total;
                if (row > 0) {
                $('.edit-subject .edit-title-bar').find('div').text('添加年级');
                $('.edit-subject').find('input').val('');
                getSelectRadioGradeList('subject');
                showEditModal($('.edit-subject'));
                addSubject();
                } else {
                   alert("请先新建年级！");
                }
            },
            error: function() {
                console.log('error');
            }
        });
    });

    //打开关闭选课
    $('body').on('click','.open-close-class', function() {
    	var element = this;
    	var classId = $(this).parent().attr('cid');
        $.ajax({
            url: '/reverse/openCloseClass.action',
            type: 'post',
            dataType: 'json',
            data: {classid:classId},
            success: function(data) {
               if(data.resultCode==1) {
               	alert("打开关闭选课失败");
               } else {
               		if(data.opentype==1){
               			$(element).text("关闭选课");
               		} else {
               			$(element).text("打开选课");
               		}
               }
               
            },
            error: function() {
                console.log('error');
            }
        });
    });
    

    // 班级添加老师
    $('.class-add-teacher').on('click', function() {
        var cid = $(this).attr('cid');
        $('.add-teacher .edit-title-bar').find('div').text('添加老师');
        $('.add-teacher').find('input').val('');
       
        getSelectTeacherList($('.add-class-teacher'), function() {
            getSelectClassSubjectList(cid);
            $('.add-class-teacher').select2("destroy");
            initSelect($('.add-class-teacher'));
            $('.reset-password-form').hide();
            showEditModal($('.add-teacher'));
        });
        addClassTeacher(cid);
    });

    // 班级添加学生
    $('.class-add-student').on('click', function() {
        var cid = $(this).attr('cid');
        $('.edit-student .edit-title-bar').find('div').text('添加学生');
        $('.edit-student').find('input:text').val('');
        $('.edit-student').find('input:text').attr('disabled', false);
        $('.edit-error').remove();
        $('.reset-password-form').hide();
        var target2 = $('.user-stuuser');
        target2.empty();
        var target3 = $('.user-paruser');
        target3.empty();
        showEditModal($('.edit-student'));
        addClassStudent(cid);
    });

    // 添加拓展课
    $('.list-add-interest').on('click', function() {
    	$('.course-type').show();
        $('.edit-interest .edit-title-bar').find('div').text('添加拓展课');
        $('.edit-interest').find('input').val('');
        $('.edit-interest').find('.select-time-lesson >li').removeClass('selected');
        $('.edit-interest').find('.edit-error').remove();
        $('.add-interest-teacher').select2("destroy");
        getSelectSubjectList();
        getSelectRadioGradeList('interest');
        getSelectTeacherList($('.add-interest-teacher'), function() {
            initSelect($('.add-interest-teacher'));
            showEditModal($('.edit-interest'));
        });
        addInterestClass();
    });
    
    // 拓展课添加学生
    $('.interest-class-add-student').on('click', function() {
        var cid = $(this).attr('cid');
        var tid = $(this).attr('tid');
        $('.add-student .edit-title-bar').find('div').text('添加学生');
        $('.add-student').find('input').val('');
       
        getSelectStudentList($('.add-class-student'), function() {
            getSelectClassSubjectList(cid);
            $('.add-class-student').select2("destroy");
            initSelect($('.add-class-student'));
            $('.reset-password-form').hide();
            showEditModal($('.add-student'));
        });
        addInterestClassStudent(cid,tid);
    });

    // 添加德育评分项
    $('.list-add-moral').on('click', function() {
        $('.edit-moral .edit-title-bar').find('div').text('添加评分项');
        $('.edit-moral').find('input:text').val('');
        showEditModal($('.edit-moral'));
        addMoral();
    });

    // 检查学生用户名重复
    $('.edit-student-name').keyup(function() {
        var target = $(this);
        var target2 = $('.user-stuuser');
        var target3 = $('.user-paruser');
        if ($.trim(target.val()) != '') {
            $.ajax({
                url: '/reverse/checkUserName.action',
                type: 'post',
                dataType: 'json',
                data: {
                    userName: $.trim(target.val()),
                    number: 1
                },
                success: function(data) {
                	if ($('.edit-student .edit-title-bar').find('div').text()=="添加学生") {
	                	if (data.resultCode == 1) {
	                		target.next().remove();
	                        target.after('<span class="edit-error">用户名已存在！</span>');
	                	} else {
	                		 target.next().remove();
		                	
		                		target2.empty();
		                		target3.empty();
		                		var content = '';
		                        content += '<span style="padding-bottom: 5px;">生成学生用户名&nbsp;&nbsp;&nbsp;</span><span style="width: 150px;" id="studentname">' + data.username  + '</span>';
		                        content += '<span>密码&nbsp;&nbsp;' + data.password + '</span>';
		                        target2.append(content);
		                        var content2 = '';
		                        content2 += '<span>生成家长用户名&nbsp;&nbsp;&nbsp;</span><span style="width: 150px;" id="parentname">' + data.parusername + '</span>';
		                        content2 += '<span>密码&nbsp;&nbsp;' + data.password + '</span>';
		                        target3.append(content2);
	                	}
                	}
                },
                error: function() {
                }
            });
        }
    });
    
 // 检查老师用户名重复
    $('.edit-teacher-name').keyup(function() {
        var target = $(this);
        var target4 = $('.user-techuser');
        if ($.trim(target.val()) != '') {
            $.ajax({
                url: '/reverse/checkUserName.action',
                type: 'post',
                dataType: 'json',
                data: {
                    userName: $.trim(target.val())
                },
                success: function(data) {
                	if ($('.edit-teacher .edit-title-bar').find('div').text()=="添加老师") {
	                	if (data.resultCode == 1) {
	                		target.next().remove();
	                        target.after('<span class="edit-error">用户名已存在！</span>');
	                	} else {
	                		 target.next().remove();
	 	                		target4.empty();
	 	                		var content = '';
	 	                        content += '<span style="padding-bottom: 5px;">生成用户名&nbsp;&nbsp;&nbsp;</span><span style="width: 100px;" id="teachername">' + data.username  + '</span>';
	 	                        content += '<span>密码&nbsp;&nbsp;' + data.password + '</span>';
	 	                        target4.append(content);
	                	}
                	}
                },
                error: function() {
                }
            });
        }
    });

    //调换班级
    $('body').on('click', '.list-change-class', function() {
        var stid = $(this).parent().attr('stid');
        var cid = $(this).parent().attr('cid');
        var stname = $(this).parent().find('span').eq(0).text();
        $('.change-stu-name').text(stname);
        //getSelectClassGradeList();
        showEditModal($('.change-class'));
        changeClass(stid, cid, $(this));
    });
    $('.change-class-grade').on('change', function() {
        var gid = $(this).val();
        getSelectClassList(gid);
    });

    // 选择上课时间
    $('.select-time-lesson >li').on('click', function() {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
        } else {
            $(this).addClass('selected');
        }
    });
    
    // 开始新学期
    $('.start-new-year').on('click', function() {
        if (confirm('确定要开始新学期吗？')) {
        	  $.ajax({
                  url: '/grade/update.do',
                  type: 'get',
                  contentType: 'application/json',
                  success: function (res) {
                  	alert('开始新学期成功了！');
                  }
              });  
        }
    });
});

function menuChange(button) {
    var target = $(button).attr('data-target');
    var type = $(button).attr('data-type') || null;
    $(button).parent().find('.active').removeClass('active');
    $(button).siblings().removeAttr('style');
    $(button).addClass('active');
    $('.page-paginator').hide();
    $('.class-right-title-bar').hide();
    if ($(button).text() == '管理校园') {
        $('.right-title-bar').show();
    } else {
        $('.right-title-bar').hide();
    }
    getIndexPage(target, type);
}

function showmange(option) {
	if (option=="老师") {
		$('.ismanageclass').show();
	} else {
		$('.ismanageclass').hide();
	}
}

function showEditModal(target) {
    $('.modal-bg').fadeIn();
    $('.edit-container').fadeIn();
    $('.edit-info-container').hide();
    target.fadeIn();
}

function resetEditContainer() {
    $('.edit-info-container').hide();
}

function showAlert() {
    $('.modal-bg').fadeIn();
    $('.edit-container').fadeIn();
    resetEditContainer();
}

function hideAlert() {
    $('.modal-bg').fadeOut();
    $('.edit-container').fadeOut();
    $('.edit-commit-btn').unbind();
    $('.select-teacher-list').empty().hide();
}

function getIndexPage(target, type) {
    if (type == 'angular') {
        $('.manage-right-container').hide();
        $('#angular-right-container').show();
    } else {
        $('.manage-right-container .list-container').hide();
        $('#angular-right-container').hide();
        $('.manage-right-container').show();
        $('.' + target + '-list').show();
        switch (target) {
            case 'teacher':
                getTeacherList();
                break;
            case 'subject':
                getSubjectList();
                break;
            case 'grade':
                getGradeList();
                break;
            case 'moral':
                getMoralList();
                break;
            case 'interest':
            	$('.class-right-title-bar').hide();
                getInterestGradeList();
                break;
        }
    }
}
var teacherList;
function getTeacherList(page) {
    var tpage = page || 1;
    $.ajax({
        url: '/reverse/selSchoolAllTeacher.action',
        type: 'post',
        dataType: 'json',
        data: {
            page: tpage,
            keyword: $('#teacher-name-search').val()
        },
        success: function(data) {
        	var row = data.rows;
            teacherList = row;
            var target = $('.teacher-list .list-ul');
            var option = {
                total: data.total,
                pagesize: data.pageSize,
                currentpage: data.page,
                operate: function(totalPage) {
                    $('.page-index span').each(function() {
                        $(this).attr('onclick', 'getTeacherList(' + $(this).text() + ');')
                    });
                    $('.first-page').attr('onclick', 'getTeacherList(1);');
                    $('.last-page').attr('onclick', 'getTeacherList(' + totalPage + ');');
                }
            }
            target.empty();
            for (var i = 0; i < row.length; i++) {
                var content = '';
                content += '<li tid=' + row[i].id + '><div class="list-content"><span>' + row[i].jobnumber + ' ' + row[i].teacherName + '</span></div>';
                content += '<i class="fa fa-pencil list-edit"></i><i class="fa fa-trash-o list-delete"></i></li>';
                target.append(content);
            }
            initPaginator(option);
        },
        error: function() {
            console.log('selSchoolAllTeacher error');
        }
    });
}


function selectEditModal(belong, source) {
    switch (belong) {
        case 'grade':
            var gradeId = source.parent().attr('gid');
            $('.edit-grade .edit-title-bar').find('div').text('编辑年级');
            getSelectTeacherList($('.edit-grade-teacher'), function() {
                getSelectGradeList();
                $('.edit-grade-teacher').select2("destroy");
                initSelect($('.edit-grade-teacher'));
                showEditGradeInfo(gradeId);
                showEditModal($('.edit-grade'));
            });
            editGrade(gradeId);
            break;
        case 'class':
            var classId = source.parent().attr('cid');
            $('.edit-class .edit-title-bar').find('div').text('编辑班级');
            getSelectTeacherList($('.edit-class-teacher'), function() {
                $('.edit-class-teacher').select2("destroy");
                initSelect($('.edit-class-teacher'));
                showEditClassInfo(classId);
                showEditModal($('.edit-class'));
            });
            editClass(classId);
            break;
        case 'pre-class':
            if (source.parents('.list-ul').hasClass('class-teacher-list')) {
                var teacherid = source.parent().attr('tid');
                var cid = source.parent().attr('cid');
                $('.add-teacher .edit-title-bar').find('div').text('编辑老师');
                getSelectTeacherList($('.add-class-teacher'), function() {
                    getSelectClassSubjectList(cid);
                    $('.add-class-teacher').select2("destroy");
                    initSelect($('.add-class-teacher'));
                    showEditClassTeacherInfo(teacherid);
                    showEditModal($('.add-teacher'));
                });
                editClassTeacher(teacherid, cid);
            } else {
                var studentid = source.parent().attr('stid');
                $('.edit-student .edit-title-bar').find('div').text('编辑学生');
                $('.edit-error').remove();
                $('.reset-password-form').show();
                $('.reset-password-form img').hide();
                showEditClassStudentInfo(studentid);
                var target2 = $('.user-stuuser');
                target2.empty();
                var target3 = $('.user-paruser');
                target3.empty();
                showEditModal($('.edit-student'));
                editClassStudentInfo(studentid, source);
            }
            break;
        case 'subject':
            var sid = source.parent().attr('sid');
            $('.edit-subject .edit-title-bar').find('div').text('编辑年级');
            getSelectRadioGradeList('subject');
            showEditSubject(sid);
            showEditModal($('.edit-subject'));
            editSubject(sid, source);
            break;
        case 'teacher':
            var teacherId = source.parent().attr('tid');
            $('.edit-teacher .edit-title-bar').find('div').text('编辑老师');
            $('.edit-teacher').find('input').val('');
            $('.ismanageclass').hide();
            $("#ismanage").attr("checked",false);
            showEditTeacher(teacherId);
            $('.reset-password-form').show();
            $('.reset-password-form img').hide();
            showEditModal($('.edit-teacher'));
            editTeacher(teacherId, source);
            break;
        case 'moral':
            var moralId = source.parent().attr('mid');
            $('.edit-moral .edit-title-bar').find('div').text('编辑评分项');
            $('.edit-moral-name').val(source.parent().find('span').eq(0).text());
            showEditModal($('.edit-moral'));
            editMoral(moralId, source);
            break;
        case 'interest':
            var classId = source.parent().attr('cid');
        	$('.course-type').hide();
            $('.edit-interest .edit-title-bar').find('div').text('编辑拓展课');
            $('.edit-interest').find('input').val('');
            $('.edit-interest').find('.select-time-lesson >li').removeClass('selected');
            $('.edit-interest').find('.edit-error').remove();
            $('.add-interest-teacher').select2("destroy");
            getSelectSubjectList();
            getSelectRadioGradeList('interest');
            getSelectTeacherList($('.add-interest-teacher'), function() {
                initSelect($('.add-interest-teacher'));
                showEditInterestInfo(classId);
                showEditModal($('.edit-interest'));
            });
            updateInterestClass(classId);
            break;
        default:
            break;
    }
}

function selectDeleteForm(belong, source) {
    switch (belong) {
        case 'grade':
            var gradeId = source.parent().attr('gid');
            deleteGrade(gradeId, source);
            break;
        case 'class':
            var classId = source.parent().attr('cid');
            deleteClassInfo(classId, source);
            break;
        case 'pre-class':
            if (source.parents('.list-ul').hasClass('class-teacher-list')) {
                var teacherid = source.parent().attr('tid');
                deleteClassTeacher(teacherid, source);
            } else {
                var studentid = source.parent().attr('stid');
                deleteClassStudent(studentid, source);
            }
            break;
        case 'subject':
            var sid = source.parent().attr('sid');
            deleteSubject(sid, source);
            break;
        case 'teacher':
            var teacherId = source.parent().attr('tid');
            deleteTeacher(teacherId, source);
            break;
        case 'moral':
            var moralId = source.parent().attr('mid');
            deleteMoral(moralId, source);
            break;
        case 'interest':
            var classId = source.parent().attr('cid');
            deleteInterestClass(classId, source);
            break;
        case 'interest-class':
        var classId = source.parent().attr('cid');
        var tid = source.parent().attr('did');
        var userid = source.parent().attr('tid');
        deleteInterestClassUser(classId,tid,userid,source);
        break;
    }
}

function getSchoolInfo() {
    $.ajax({
        url: '/reverse/selSchoolInfo.action',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
            var schoolInfo = data.schoolInfo;
            var content = [];
            $('.title-school-year').text(schoolInfo.schoolYear);
            $('.title-school-name').text(schoolInfo.schoolName);
            if (schoolInfo.isPrimary == 1) content.push('小学');
            if (schoolInfo.isMiddle == 1) content.push('初中');
            if (schoolInfo.isHigh == 1) content.push('高中');
            $('.title-school-level').text(content);
        },
        error: function() {
            console.log('selSchoolInfo error');
        }
    });
}

function editSchoolInfo() {
    var schoolName = $('.title-school-name').text();
    var schoolLevel = $('.title-school-level').text().split(',');
    var inputSchoolName = $('.edit-school .edit-input-group').eq(0).find('input');
    var selectLevel = $('.edit-school .edit-input-group').eq(1).find('input');
    var content = '';
    var typeList = '';
    inputSchoolName.val(schoolName);
    for (var i = 0; i < selectLevel.length; i++) {
        selectLevel.eq(i).prop('checked', false);
        for (var j in schoolLevel) {
            if ($.trim(selectLevel.eq(i).parent().text()) == schoolLevel[j]) {
                selectLevel.eq(i).prop('checked', 'checked');
            }
        }
    }
    $('.edit-school').show();
    $('.edit-commit-btn').on('click', function() {
        for (var i = 0; i < selectLevel.length; i++) {
            if (selectLevel.eq(i).is(':checked')) {
                if (content == '')
                    content += $.trim(selectLevel.eq(i).parent().text());
                else
                    content += ',' + $.trim(selectLevel.eq(i).parent().text());
                typeList += '1,';
            } else {
                typeList += '0,';
            }
        }
        if ($.trim(inputSchoolName.val()) != '' && typeList != '0,0,0,') {
            $.ajax({
                url: '/reverse/updateSchoolInfo.action',
                type: 'post',
                dataType: 'json',
                data: {
                    schoolname: inputSchoolName.val(),
                    schoolTypeList: typeList
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        $('.title-school-name').text(inputSchoolName.val());
                        $('.title-school-level').text(content);
                        alert('修改成功！');
                    } else {
                        alert('修改失败！');
                    }
                },
                error: function() {
                    alert('修改失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请填写完整！');
        }
    });
}

function setPassword() {
    $('.edit-commit-btn').on('click', function() {
        if ($.trim($('.edit-password-input').val()) != '') {
            $.ajax({
                url: '/reverse/reastInitPassword.action',
                type: 'post',
                dataType: 'json',
                data: {
                    password: $('.edit-password-input').val()
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        alert('修改成功！');
                    } else {
                        alert('修改失败！');
                    }
                },
                error: function() {
                    alert('修改失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请输入密码');
        }
    });
}

function showEditTeacher(tid) {
	var target4 = $('.user-techuser');
    target4.empty();
    $.ajax({
        url: '/reverse/selSingleTeacher.action',
        type: 'post',
        dataType: 'json',
        data: {
            teacherid: tid
        },
        success: function(data) {
            if (data.resultCode == 0) {
                console.log(data);
                $('.edit-teacher-username').attr('disabled','disabled');
                $('.edit-teacher-username').val(data.teacherInfo.userName);
                $('.edit-teacher-number').val(data.teacherInfo.jobnumber);
                $('.edit-teacher-name').val(data.teacherInfo.teacherName);
                $('.edit-teacher-permission').val(data.teacherInfo.role);
                $('.reset-teacher-password').attr('onclick', 'resetPsw(' + data.teacherInfo.userId + ',"2")');
                if (data.teacherInfo.role==1) {
                	$('.ismanageclass').show();
                	if (data.teacherInfo.ismanage==1) {
                		document.getElementById("ismanage").checked = true;
                	}
                }
                var content = '';
                content += '<span style="padding-bottom: 5px;">生成用户名&nbsp;&nbsp;&nbsp;</span><span style="width: 150px;" id="teachername">' + data.teacherInfo.userName  + '</span>';
                target4.append(content); 

            }
        },
        error: function() {
            console.log('selSingleTeacher error');
        }
    });
}

function editTeacher(teacherId, target) {
    $('.edit-commit-btn').on('click', function() {
//        if ($.trim($('.edit-teacher-number').val()) == '') {
//            $('.edit-teacher-number').next().remove();
//            $('.edit-teacher-number').after('<span class="edit-error">请输入教职工号!</span>');
//        } else {
//            $('.edit-teacher-number').next().remove();
//        }
        if ($.trim($('.edit-teacher-name').val()) == '') {
            $('.edit-teacher-name').next().remove();
            $('.edit-teacher-name').after('<span class="edit-error">请输入老师姓名!</span>');
        } else {
            $('.edit-teacher-name').next().remove();
        }
        if ($('.edit-teacher').find('.edit-error').length == 0) {
        	var ismanage = 0;
        	if(document.getElementById("ismanage").checked) {
        		ismanage = 1;
        	} else {
        		ismanage = 0;
        	}
            $.ajax({
                url: '/reverse/updateTeacherInfo.action',
                type: 'post',
                dataType: 'json',
                data: {
                    number: $('.edit-teacher-number').val(),
                    teachername: $('.edit-teacher-name').val(),
                    permission: $('.edit-teacher-permission').val(),
                    teacherid: teacherId,
                    ismanage: ismanage
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        var content = $('.edit-teacher-number').val() + ' ' + $('.edit-teacher-name').val();
                        target.parent().find('span').text(content);
                        alert('修改成功！');
                    } else {
                        alert('修改失败');
                    }
                },
                error: function() {
                    alert('修改失败');
                },
                complete: function() {
                    hideAlert();
                }
            });
        }
    });
}

function deleteTeacher(teacherId, target) {
    if (confirm("确定要删除该老师吗？")) {
        $.ajax({
            url: '/reverse/deleteTeacherInfo.action',
            type: 'post',
            dataType: 'json',
            data: {
                teacherid: teacherId
            },
            success: function(data) {
                if (data.resultCode == 0) {
                    alert('删除成功');
                    target.parent().remove();
                    getTeacherList();
                } else {
                    alert('删除失败');
                }
            },
            error: function() {
                console.log('deleteTeacherInfo error');
                alert('删除失败');
            }
        });
    }
}

function addTeacher() {
    $('.edit-commit-btn').on('click', function() {
//        if ($.trim($('.edit-teacher-username').val()) == '') {
//            $('.edit-teacher-username').next().remove();
//            $('.edit-teacher-username').after('<span class="edit-error">请输入老师用户名!</span>');
//        }
        if ($.trim($('.edit-teacher-number').val()) == '') {
            $('.edit-teacher-number').next().remove();
            $('.edit-teacher-number').after('<span class="edit-error">请输入教职工号!</span>');
        } else {
            $('.edit-teacher-number').next().remove();
        }
        if ($.trim($('.edit-teacher-name').val()) == '') {
            $('.edit-teacher-name').next().remove();
            $('.edit-teacher-name').after('<span class="edit-error">请输入老师姓名!</span>');
        } else {
            $('.edit-teacher-name').next().remove();
        }
        if ($('.edit-teacher').find('.edit-error').length == 0) {
        	var ismanage = 0;
        	if(document.getElementById("ismanage").checked) {
        		ismanage = 1;
        	} else {
        		ismanage = 0;
        	}
            $.ajax({
                url: '/reverse/addTeacherInfo.action',
                type: 'post',
                dataType: 'json',
                data: {
                    teusername: $.trim($('#teachername').text()),
                    number: $.trim($('.edit-teacher-number').val()),
                    teachername: $.trim($('.edit-teacher-name').val()),
                    permission: $('.edit-teacher-permission').val(),
                    ismanage:ismanage
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        alert('添加成功！');
                        getTeacherList();
                    } else {
                        alert('添加失败');
                    }
                },
                error: function() {
                    console.log('addTeacyherInfo error');
                    alert('添加失败');
                },
                complete: function() {
                    hideAlert();
                }
            });
        }
    });
}

function getClassList(gid) {
    var target = $('.class-list .list-ul');
    $.ajax({
        url: '/reverse/selClassByGradeId.action',
        type: 'POST',
        dataType: 'json',
        data: {
            gradeid: gid
        },
        success: function(data) {
        	target.empty();
            var classList = data.classList;
            for (var i = 0; i < classList.length; i++) {
                var content = '';
                var teacherName = classList[i].teacherName == null ? '暂无' : classList[i].teacherName;
                content += '<li cid=' + classList[i].id + ' gid=' + gid + '><div class="list-content"><span>' + $('.list-title-grade').text() + classList[i].classname + '</span><span>班主任：</span>';
                content += '<span class="grade-manager-name">' + teacherName + '</span></div><i class="fa fa-pencil list-edit"></i>';
                content += '<i class="fa fa-trash-o list-delete"></i></li>';
                target.append(content);
            }
        },
        error: function() {
            console.log('selClassByGradeId error');
        }
    });
}

function addClassInfo(gid) {
    $('.edit-commit-btn').on('click', function() {
        if ($.trim($('.edit-class-number').val()) != '' && $('.edit-class-teacher').select2("val") != 0) {
            $.ajax({
                url: '/reverse/addClassByGradeId.action',
                type: 'post',
                dataType: 'json',
                data: {
                    classname: $('.edit-class-number').val(),
                    teacherid: $('.edit-class-teacher').select2("val"),
                    gradeid: gid
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        alert('添加成功！');
                        getClassList(gid);
                    } else {
                        alert('添加失败！');
                    }
                },
                error: function() {
                    alert('添加失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请填写完整！');
        }
    });
}

function showEditClassInfo(cid) {
    $.ajax({
        url: '/reverse/selSingleClassInfo.action',
        type: 'post',
        dataType: 'json',
        data: {
            classid: cid
        },
        success: function(data) {
            if (data.resultCode == 0) {
                var classInfo = data.classInfo;
                $('.edit-class-number').val(classInfo.classname);
                $('.edit-class-teacher').select2('val', classInfo.teacherid)
            }
        }
    });
}

function editClass(cid) {
    var gid = $('.list-add-class').attr('gid');
    $('.edit-commit-btn').on('click', function() {
        if ($.trim($('.edit-class-number').val()) != '' && $('.edit-class-teacher').select2('val') != 0) {
            $.ajax({
                url: '/reverse/updateClassByGradeId.action',
                type: 'post',
                dataType: 'json',
                data: {
                    classid: cid,
                    classname: $('.edit-class-number').val(),
                    teacherid: $('.edit-class-teacher').select2('val')
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        alert('修改成功！');
                        getClassList(gid);
                    } else {
                        alert('修改失败！');
                    }
                },
                error: function() {
                    alert('修改失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请填写完整！');
        }
    });
}

function deleteClassInfo(cid, target) {
    $.ajax({
        url: '/reverse/selClassUserInfo.action',
        type: 'post',
        dataType: 'json',
        data: {
            classid: cid
        },
        success: function(data) {
            var teacherList = data.classSubjectList;
            var studentList = data.userList;
            if (teacherList.length==0 && studentList.length==0) {
                if (confirm('确定要删除该班级吗？')) {
                    $.ajax({
                        url: '/reverse/deleteClassByGradeId.action',
                        type: 'post',
                        dataType: 'json',
                        data: {
                            classid: cid
                        },
                        success: function(data) {
                            if (data.resultCode == 0) {
                                alert('删除成功');
                                target.parent().remove();
                            } else {
                                alert('删除失败');
                            }
                        },
                        error: function() {
                            alert('删除失败');
                        }
                    });
                }
            } else {
                alert("请先删除班级下的老师和学生！");
            }
        }
    });
}

function getClassUserList(cid) {
    var targetT = $('.class-teacher-list');
    var targetS = $('.class-student-list');
    targetT.empty();
    targetS.empty();
    $.ajax({
        url: '/reverse/selClassUserInfo.action',
        type: 'post',
        dataType: 'json',
        data: {
            classid: cid
        },
        success: function(data) {
            var teacherList = data.classSubjectList;
            var studentList = data.userList;
            for (var i = 0; i < teacherList.length; i++) {
                var content = '';
                content += '<li tid=' + teacherList[i].id + ' cid=' + cid + '><div class="list-content"><span>' + teacherList[i].teacherName + '</span>';
                content += '<span>' + teacherList[i].subjectName + '</span></div>';
                content += '<i class="fa fa-pencil list-edit"></i><i class="fa fa-trash-o list-delete"></i></li>';
                targetT.append(content);
            }
            for (var i = 0; i < studentList.length; i++) {
                var content = '';
                content += '<li stid=' + studentList[i].id + ' cid=' + cid + '><div class="list-content"><span>' + studentList[i].nickName + '</span></div>';
                content += '<i class="fa fa-pencil list-edit"></i><i class="fa fa-trash-o list-delete"></i>';
                content += '<img class="list-change-class" src="/img/K6KT/changeclass.png" title="学生调换班级"/></li>';
                targetS.append(content);
            }
        }
    });
}

function getInterestClassUserList(cid) {
	$('.manage-tianjia').removeClass('manage-hover');
	$('.first-class').addClass('manage-hover');
	$('.interest-class-student-list').show();
    $('.second-interest-class-student-list').hide();
	 $('.team-sutdent').hide();
	$('.class-right-title-bar').show();
    var targetS = $('.interest-class-student-list');
    var targetC = $('.second-interest-class-student-list');
    targetS.empty();
    targetC.empty();
    $.ajax({
        url: '/reverse/selInterestInfoDetail.action',
        type: 'post',
        dataType: 'json',
        data: {
            classid: cid
        },
        success: function(data) {
            var studentList = data.userList;
            var nextstudentList = data.userList2;
            var classInfo = data.classInfo;
            $('.class-name').text("班级名称：" + classInfo.classname);
            $('.teacher-name').text("老师：" + classInfo.teacherName);
            if (classInfo.coursetype==1) {
            $('.fnum').text(classInfo.actualcount + "/" + classInfo.studentcount);	
            $('.snum').text('');	
            $('.interest-class-student-list').show();
            } else {
            	if (classInfo.firstteam==1){
            		$('.fnum').text(classInfo.actualcount + "/" + classInfo.studentcount);	
            	}
            	if (classInfo.secondteam==1) {
            		$('.snum').text(classInfo.nextcount + "/" + classInfo.studentcount);	
            	}
            }
            if (classInfo.coursetype==2) {
             $('.team-sutdent').show();
            }
            for (var i = 0; i < studentList.length; i++) {
                var content = '';
                content += '<li tid=' + studentList[i].id + ' cid=' + cid + ' pid=' + classInfo.coursetype + ' fid=' + classInfo.firstteam + ' sid=' + classInfo.secondteam + ' did=' + 1 + '><div class="list-content"><span>' + studentList[i].nickName + '</span>';
                content += '<span>行政班：</span><span>' + studentList[i].mainClassName + '</span></div>';
                content += '<i class="fa fa-trash-o list-delete"></i></li>';
                targetS.append(content);
            }
            for (var i=0;i<nextstudentList.length;i++) {
            	var content = '';
                content += '<li tid=' + nextstudentList[i].id + ' cid=' + cid + ' pid=' + classInfo.coursetype + ' fid=' + classInfo.firstteam + ' sid=' + classInfo.secondteam + ' did=' + 2 + '><div class="list-content"><span>' + nextstudentList[i].nickName + '</span>';
                content += '<span>行政班：</span><span>' + nextstudentList[i].mainClassName + '</span></div>';
                content += '<i class="fa fa-trash-o list-delete"></i></li>';
                targetC.append(content);
            }
        }
    });
}

function addClassTeacher(cid) {
    $('.edit-commit-btn').on('click', function() {
        if ($('.add-class-teacher').select2("val") != 0) {
            $.ajax({
                url: '/reverse/addClassTeacherInfo.action',
                type: 'post',
                dataType: 'json',
                data: {
                    classid: cid,
                    teacherid: $('.add-class-teacher').select2("val"),
                    subjectid: $('.add-class-subject').val()
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        alert('添加成功！');
                        getClassUserList(cid);
                    } else {
                        alert('添加失败！');
                    }
                },
                error: function() {
                    alert('添加失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请填写完整！');
        }
    });
}

function addInterestClassStudent(cid,tid) {
    $('.edit-commit-btn').on('click', function() {
        if ($('.add-class-student').select2("val") != 0) {
            $.ajax({
                url: '/reverse/addInterestUserInfo.action',
                type: 'post',
                dataType: 'json',
                data: {
                    classid: cid,
                    userid: $('.add-class-student').select2("val"),
                    type:tid
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        alert('添加成功！');
                        getInterestClassUserList(cid);
                    } else if(data.resultCode == 2){
                        alert(data.mesg);
                    } else{
                    	alert('添加失败！');
                    }
                },
                error: function() {
                    alert('添加失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请填写完整！');
        }
    });
}

function addClassStudent(cid) {
    $('.edit-commit-btn').on('click', function() {
        if ($.trim($('.edit-student-name').val()) == '') {
            $('.edit-student-name').next().remove();
            $('.edit-student-name').after('<span class="edit-error">请输入学生姓名!</span>');
        } else {
            $('.edit-student-name').next().remove();
        }
        if ($('.edit-student').find('.edit-error').length == 0) {
            $.ajax({
                url: '/reverse/addStudentInfo.action',
                type: 'post',
                dataType: 'json',
                data: {
                    classid: cid,
                    stusername: $.trim($('#studentname').text()),
                    stnickname: $.trim($('.edit-student-name').val()),
                    sex: $('.edit-student input:radio:checked').val(),
                    ptusername: $.trim($('#parentname').text())
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        alert('添加成功！');
                        getClassUserList(cid);
                    } else {
                        alert('添加失败！');
                    }
                },
                error: function() {
                    alert('添加失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        }
    });
}

function showEditClassTeacherInfo(tid) {
    $.ajax({
        url: '/reverse/selSingleClassTeacher.action',
        type: 'post',
        dataType: 'json',
        data: {
            classSubjectid: tid
        },
        success: function(data) {
            $('.add-class-teacher').select2('val', data.classSubjectInfo.teacherId);
            $('.add-class-subject').val(data.classSubjectInfo.subjectId);
        }
    });
}

function editClassTeacher(tid, cid) {
    $('.edit-commit-btn').on('click', function() {
        if ($('.add-class-teacher').select2("val") != 0) {
            $.ajax({
                url: '/reverse/updateClassTeacherInfo.action',
                type: 'post',
                dataType: 'json',
                data: {
                    classSubjectid: tid,
                    teacherid: $('.add-class-teacher').select2("val"),
                    subjectid: $('.add-class-subject').val()
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        alert('修改成功！');
                        getClassUserList(cid);
                    } else {
                        alert('修改失败！');
                    }
                },
                error: function() {
                    alert('添加失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请填写完整！');
        }
    });
}

function deleteClassTeacher(tid, source) {
    if (confirm('确定要删除该老师吗？')) {
        $.ajax({
            url: '/reverse/deleteClassTeacherInfo.action',
            type: 'post',
            dataType: 'json',
            data: {
                classSubjectid: tid
            },
            success: function(data) {
                if (data.resultCode == 0) {
                    alert('删除成功');
                    source.parent().remove();
                } else {
                    alert('删除失败');
                }
            },
            error: function() {
                alert('删除失败');
            }
        });
    }
}

function showEditClassStudentInfo(stid) {
	var target2 = $('.user-stuuser');
    target2.empty();
    var target3 = $('.user-paruser');
    target3.empty();
    $.ajax({
        url: '/reverse/selSingleClassStudent.action',
        type: 'post',
        dataType: 'json',
        data: {
            studentid: stid
        },
        success: function(data) {
            var sinfo = data.studentUserInfo;
            var pinfo = data.parentUserInfo;
            $('.edit-student-username').val(sinfo.userName).attr('disabled', 'disabled');
            $('.edit-student-name').val(sinfo.nickName);
            $('.edit-student input:radio').each(function(index, el) {
                if ($(this).val() == sinfo.sex) {
                    $(this).prop('checked', true);
                }
            });
            $('.edit-parent-name').val(pinfo.nickName);
            $('.reset-student-password').attr('onclick', 'resetPsw(' + stid + ',"1")');
            $('.reset-parent-password').attr('onclick', 'resetPsw(' + pinfo.id + ',"0")');
            var content = '';
            content += '<span style="padding-bottom: 5px;">生成学生用户名&nbsp;&nbsp;&nbsp;</span><span style="width: 150px;" id="studentname">' + sinfo.userName  + '</span>';
            target2.append(content);
            var content2 = '';
            content2 += '<span style="padding-bottom: 5px;">生成家长用户名&nbsp;&nbsp;&nbsp;</span><span style="width: 150px;" id="parentname">' + pinfo.userName + '</span>';
            target3.append(content2);
            $('#parentname').attr({
                'pid': pinfo.id
            });
        }
    });
}

function editClassStudentInfo(stid, target) {
    $('.edit-commit-btn').on('click', function() {
        var pid = $('.edit-parent-username').attr('pid');
        if ($.trim($('.edit-student-username').val()) == '') {
            $('.edit-student-username').next().remove();
            $('.edit-student-username').after('<span class="edit-error">请输入学生用户名!</span>');
        }
        if ($.trim($('.edit-student-name').val()) == '') {
            $('.edit-student-name').next().remove();
            $('.edit-student-name').after('<span class="edit-error">请输入学生姓名!</span>');
        } else {
            $('.edit-student-name').next().remove();
        }
        if ($.trim($('.edit-parent-username').val()) == '') {
            $('.edit-parent-username').next().remove();
            $('.edit-parent-username').after('<span class="edit-error">请输入家长用户名!</span>');
        }
        if ($.trim($('.edit-parent-name').val()) == '') {
            $('.edit-parent-name').next().remove();
            $('.edit-parent-name').after('<span class="edit-error">请输入家长姓名!</span>');
        } else {
            $('.edit-parent-name').next().remove();
        }
        if ($('.edit-student').find('.edit-error').length == 0) {
            $.ajax({
                url: '/reverse/updateStudentInfo.action',
                type: 'post',
                dataType: 'json',
                data: {
                    studentid: stid,
                    parentid: pid,
                    stnickname: $.trim($('.edit-student-name').val()),
                    sex: $('.edit-student input:radio:checked').val()
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        alert('修改成功！');
                        target.parent().find('span').eq(0).text($.trim($('.edit-student-name').val()));
                    } else {
                        alert('修改失败！');
                    }
                },
                error: function() {
                    alert('修改失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        }
    });
}

function deleteClassStudent(stid, source) {
    if (confirm('确定要删除该学生吗？')) {
        $.ajax({
            url: '/reverse/deleteStudentInfo.action',
            type: 'post',
            dataType: 'json',
            data: {
                studentid: stid
            },
            success: function(data) {
                if (data.resultCode == 0) {
                    alert('删除成功');
                    source.parent().remove();
                } else {
                    alert('删除失败');
                }
            },
            error: function() {
                alert('删除失败');
            }
        });
    }
}

function resetPsw(id, type) {
    if (confirm('确定要重置密码吗？')) {
        $.ajax({
            url: '/reverse/resetUserPassword.action',
            type: 'post',
            dataType: 'json',
            data: {
                studentid: id
            },
            success: function(data) {
                if (data.resultCode == 0) {
                    if (type == 1) {
                        $('.reset-password-form img').eq(0).show();
                    } else if (type==2) {
                    	 $('.reset-password-form img').eq(2).show();
                    } else {
                        $('.reset-password-form img').eq(1).show();
                    }
                } else {
                    alert('重置失败');
                }
            },
            error: function() {
                alert('重置失败');
            }
        });
    }
}

function changeClass(stid, cid, source) {
    $('.edit-commit-btn').on('click', function() {
        $.ajax({
            url: '/reverse/exchangeClass.action',
            type: 'post',
            dataType: 'json',
            data: {
                classid: cid,
                curtclassid: $('.change-class-class').val(),
                studentid: stid
            },
            success: function(data) {
                if (data.resultCode == 0) {
                    alert('换班成功');
                    source.parent().remove();
                } else {
                    alert('换班失败，请重试！');
                }
            },
            error: function() {
                alert('换班失败，请重试！');
            },
            complete: function() {
                hideAlert();
            }
        });
    });
}

function getSubjectList() {
    var target = $('.subject-list .list-ul');
    target.empty();
    $.ajax({
        url: '/reverse/selSchoolSubject.action',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
        	target.empty();
            var list = data.schoolSubjectList;
            for (var i = 0; i < list.length; i++) {
                var content = '';
                content += '<li sid=' + list[i].id + '><div class="list-content"><span>' + list[i].name + '</span></div>';
                content += '<i class="fa fa-pencil list-edit"></i><i class="fa fa-trash-o list-delete"></i></li>';
                target.append(content);
            }
        },
        error: function() {
            console.log('selSchoolSubject error');
        }
    });
}

function addSubject() {
    $('.edit-commit-btn').on('click', function() {
        var checkList = $('.edit-subject input:checkbox');
        var grList = '';
        checkList.each(function() {
            if ($(this).is(':checked')) {
                if ($(this).attr('gid')) {
                    grList += $(this).attr('gid') + ',';
                }
            }
        });
        if ($.trim($('.edit-subject-name').val()) != '' && grList != '') {
            $.ajax({
                url: '/reverse/addSchoolSubject.action',
                type: 'post',
                dataType: 'json',
                data: {
                    subjectname: $.trim($('.edit-subject-name').val()),
                    gradeArry: grList
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        alert('添加成功！');
                        getSubjectList();
                    } else {
                        alert('添加失败！');
                    }
                },
                error: function() {
                    alert('添加失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请填写完整！');
        }
    });
}

function showEditSubject(sid) {
    $.ajax({
        url: '/reverse/selectSingleSubjectInfo.action',
        sync:false,
        type: 'post',
        dataType: 'json',
        data: {
            subjectid: sid
        },
        success: function(data) {
            var subjectInfo = data.subjectsInfo;
            var gradeList = data.subjectsInfo.gradeSubjectList;
            var checkList = $('.edit-subject input:checkbox');
            $('.edit-subject-name').val(subjectInfo.name);
            for (var i in gradeList) {
                for (var j = 0; j < checkList.length; j++) {
                    if (checkList.eq(j).attr('gid') == gradeList[i].gradeId) {
                        checkList.eq(j).prop('checked', true);
                        //break;
                    }
                }
            }
        }
    });
}

function editSubject(sid, target) {
    $('.edit-commit-btn').on('click', function() {
        var checkList = $('.edit-subject input:checkbox');
        var grList = '';
        checkList.each(function() {
            if ($(this).is(':checked')) {
                if ($(this).attr('gid')) {
                    grList += $(this).attr('gid') + ',';
                }
            }
        });
        if ($.trim($('.edit-subject-name').val()) != '' && grList != '') {
            $.ajax({
                url: '/reverse/updateSchoolSubject.action',
                type: 'post',
                dataType: 'json',
                data: {
                    subjectname: $.trim($('.edit-subject-name').val()),
                    gradeArry: grList,
                    subjectid: sid
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        alert('修改成功！');
                        target.parent().find('span').text($.trim($('.edit-subject-name').val()));
                    } else {
                        alert('修改失败！');
                    }
                },
                error: function() {
                    alert('修改失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请填写完整！');
        }
    });
}

function deleteSubject(sid, target) {
    if (confirm("确定要删除该科目？")) {
        $.ajax({
            url: '/reverse/deleteSchoolSubject.action',
            type: 'post',
            dataType: 'json',
            data: {
                subjectid: sid
            },
            success: function(data) {
                if (data.resultCode == 0) {
                    alert("删除成功");
                    target.parent().remove();
                } else {
                    alert("删除失败");
                }
            },
            error: function() {
                alert("删除失败");
            }
        });
    }
}

function getGradeList() {
    $.ajax({
        url: '/reverse/selSchoolGradeInfo.action',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
            var row = data.rows;
            var target = $('.grade-list>.list-ul');
            target.empty();
            for (var i = 0; i < row.length; i++) {
                var content = '';
                var tname = row[i].teacherName == null ? '暂无' : row[i].teacherName;
                content += '<li gid=' + row[i].id + '><div class="list-content"><span>' + row[i].gradeName + '</span>';
                content += '<span>年级组长：</span>';
                content += '<span class="grade-manager-name">' + tname + '</span></div>';
                content += '<i class="fa fa-pencil list-edit"></i><i class="fa fa-trash-o list-delete"></i></li>';
                target.append(content);
            }
        },
        error: function() {
            console.log('error');
        }
    });
}

function addGradeInfo() {
    $('.edit-commit-btn').on('click', function() {
        if ($.trim($('.edit-grade-name').val()) != '') {
            $.ajax({
                url: '/reverse/addGradeInfo.action',
                type: 'post',
                dataType: 'json',
                data: {
                    teacherid: $(".edit-grade-teacher").select2("val"),
                    currentgradeid: $('.edit-grade-value').val(),
                    gradename: $('.edit-grade-name').val()
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        alert('添加成功！');
                        getGradeList();
                    } else {
                        alert('添加失败！');
                    }
                },
                error: function() {
                    console.log('addGradeInfo error');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请输入年级名称！');
        }
    });
}

function editGrade(gid) {
    $('.edit-commit-btn').on('click', function() {
        if ($.trim($('.edit-grade-name').val()) != '') {
            $.ajax({
                url: '/reverse/updateGradeInfo.action',
                type: 'post',
                dataType: 'json',
                data: {
                    gradeid: gid,
                    teacherid: $(".edit-grade-teacher").select2("val"),
                    currentgradeid: $('.edit-grade-value').val(),
                    gradename: $('.edit-grade-name').val()
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        alert('修改成功！');
                        getGradeList();
                    } else {
                        alert('修改失败！');
                    }
                },
                error: function() {
                    console.log('updateGradeInfo error');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请输入年级名称！');
        }
    });
}

function deleteGrade(gid, target) {
        $.ajax({
        url: '/reverse/selClassByGradeId.action',
        type: 'post',
        dataType: 'json',
        data: {
            gradeid: gid
        },
        success: function(data) {
            var row = data.classList;
            if (row.length>0) {
                alert("请先删除年级下所有班级！");
            } else {
                if (confirm('确定要删除该年级吗？')) {
                    $.ajax({
                        url: '/reverse/deleteGradeInfo.action',
                        type: 'post',
                        dataType: 'json',
                        data: {
                            gradeid: gid
                        },
                        success: function(data) {
                            if (data.resultCode == 0) {
                                alert('删除成功');
                                target.parent().remove();
                            } else {
                                alert('删除失败');
                            }
                        },
                        error: function() {
                            console.log('deleteGradeInfo error');
                            alert('删除失败');
                        }
                    });
                }
            }
        }
    });
}

function showEditGradeInfo(gid) {
    $.ajax({
        url: '/reverse/selectSingleGradeInfo.action',
        type: 'post',
        dataType: 'json',
        data: {
            gradeid: gid
        },
        success: function(data) {
            var info = data.gradesInfo;
            $('.edit-grade-name').val(info.gradeName);
            $('.edit-grade-value').val(info.gradeId);
            $(".edit-grade-teacher").select2("val", info.teacherid);
        },
        error: function() {
            console.log('selectSingleGradeInfo error');
        }
    });
}

function getSelectTeacherList(target, callback) {
    target.empty();
    target.append('<option value="0">请选择...</option>');
    $.ajax({
        url: '/reverse/selSchoolAllTeacher.action',
        type: 'post',
        dataType: 'json',
        data: {
            pageSize: 1000
        },
        success: function(data) {
            var row = data.rows;
            for (var i = 0; i < row.length; i++) {
                var content = '';
                content += '<option value=' + row[i].id + '>' + row[i].jobnumber + ' ' + row[i].teacherName + '</option>';
                target.append(content);
            }
            callback();
        }
    });
}

function getSelectStudentList(target, callback) {
    target.empty();
    target.append('<option value="0">请选择...</option>');
    $.ajax({
        url: '/reverse/selSchoolAllStudent.action',
        type: 'post',
        dataType: 'json',
        data: {
            pageSize: 5000
        },
        success: function(data) {
            var row = data.rows;
            for (var i = 0; i < row.length; i++) {
                var content = '';
                content += '<option value=' + row[i].id + '>' +  ' ' + row[i].nickName + '</option>';
                target.append(content);
            }
            callback();
        }
    });
}

function addMoral() {
    $('.edit-commit-btn').on('click', function() {
        if ($.trim($('.edit-moral-name').val()) != '') {
            $.ajax({
                url: '/reverse/addScoreItems.action',
                type: 'post',
                dataType: 'json',
                data: {
                    scoreName: $.trim($('.edit-moral-name').val())
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        alert('添加成功！');
                        getMoralList();
                    } else {
                        alert('添加失败！');
                    }
                },
                error: function() {
                    alert('添加失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请输入评分项！');
        }
    });
}

function getMoralList(page) {
    var tpage = page || 1;
    $.ajax({
        url: '/reverse/selectScoreItems.action',
        type: 'post',
        dataType: 'json',
        data: {
            pageSize: 10,
            page: tpage
        },
        success: function(data) {
            var row = data.rows;
            var target = $('.moral-list .list-ul');
            target.empty();
            for (var i = 0; i < row.length; i++) {
                var content = '';
                content += '<li mid=' + row[i].id + '><div class="list-content"><span>' + row[i].scoreName + '</span></div>';
                content += '<i class="fa fa-pencil list-edit"></i><i class="fa fa-trash-o list-delete"></i></li>';
                target.append(content);
            }
        }
    });
}

function editMoral(mid, source) {
    $('.edit-commit-btn').on('click', function() {
        if ($.trim($('.edit-moral-name').val()) != '') {
            $.ajax({
                url: '/reverse/updateScoreItems.action',
                type: 'post',
                dataType: 'json',
                data: {
                    scoreName: $.trim($('.edit-moral-name').val()),
                    scoreitemid: mid
                },
                success: function(data) {
                    if (data.resultCode == 0) {
                        alert('修改成功！');
                        source.parent().find('span').eq(0).text($.trim($('.edit-moral-name').val()));
                    } else {
                        alert('修改失败！');
                    }
                },
                error: function() {
                    alert('修改失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请输入评分项！');
        }
    });
}

function deleteMoral(mid, source) {
    if (confirm('确定要删除该评分项吗？')) {
        $.ajax({
            url: '/reverse/deleteScoreItems.action',
            type: 'post',
            dataType: 'json',
            data: {
                scoreitemid: mid
            },
            success: function(data) {
                if (data.resultCode == 0) {
                    alert('删除成功');
                    source.parent().remove();
                } else {
                    alert('删除失败');
                }
            },
            error: function() {
                alert('删除失败');
            }
        });
    }
}

function getInterestGradeList() {
    $.ajax({
        url: '/reverse/selectInterestClass.action',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
            var row = data.classList;
            var target = $('.interest-list>.list-ul');
            target.empty();
            var name = '';
            for (var i = 0; i < row.length; i++) {
            	if (row[i].isopen==1) {
            		name = '关闭选课';
            	} else {
            		name = '打开选课';
            	}
                var content = '';
                var tname = row[i].teacherName == null ? '暂无' : row[i].teacherName;
                content += '<li cid="' + row[i].id + '"><div class="list-content"><span>' + row[i].classname + '</span>';
                content += '<span>老师：</span>';
                content += '<span class="grade-manager-name">' + tname + '</span></div>';
                content += '<i style="position: relative;top:8px;" class="fa fa-pencil list-edit"></i><i style="position: relative;top:8px;" class="fa fa-trash-o list-delete"></i>' +
                    '<span style="font-size: 14px;font-family:Microsoft YaHei ;background-color: #ff830a;color: #ffffff;padding: 1px 10px;border-radius: 4px;margin-left: 5px;cursor: pointer;position: relative;left: 10px;" class="open-close-class">' + name + '</span></li>';
                target.append(content);
            }
        }
    });
}

function addInterestClass() {
    $('.edit-commit-btn').on('click', function() {
        var selectTimeList = $('.select-time-lesson>li');
        var content = '';
        var startTime = $('#opentime').val();
        var closeTime = $('#closetime').val();
        selectTimeList.each(function() {
            if ($(this).hasClass('selected')) {
                content += $(this).attr('val') + ',';
            }
        });
        var cousetype = 1;
        var fteam = 0;
        var steam = 0;
        if (document.getElementsByName("classtype")[1].checked){
        	cousetype = 2;
        	if ($('#fteam').is(':checked')) {
        		fteam = 1;
        	}
        	if ($('#steam').is(':checked')) {
        		steam = 1;
        	}
        	
        }
        var checkList = $('.edit-subject-selectgrade-interest input:checkbox');
        var grList = '';
        checkList.each(function() {
            if ($(this).is(':checked')) {
                if ($(this).attr('gid')) {
                    grList += $(this).attr('gid') + ',';
                }
            }
        });
        if (grList != '' && (cousetype==1 || (cousetype==2&&(fteam==1||steam==1)))) {
	        validateInterestClass();
	        if ($('.edit-interest').find('.edit-error').length == 0) {
	            $.ajax({
	                url: '/reverse/addInterestClassInfo.action',
	                type: 'post',
	                dataType: 'json',
	                data: {
	                    interestClassName: $.trim($('.edit-interest-name').val()),
	                    teacherid: $('.add-interest-teacher').select2('val'),
	                    studentCount: $('.edit-interest-number').val(),
	                    classtime: content,
	                    subjectid: $('.edit-interest .add-class-subject').val(),
	                    opentime: startTime,
	                    closetime: closeTime,
	                    coursetype: cousetype,
						firstteam: fteam,
						secondteam: steam,
						gradeArry: grList
	                },
	                success: function(data) {
	                    if (data.resultCode == 0) {
	                        alert('添加成功！');
	                        getInterestGradeList();
	                    } else {
	                        alert('添加失败！');
	                    }
	                },
	                error: function() {
	                    alert('添加失败！');
	                },
	                complete: function() {
	                    hideAlert();
	                }
	            });
	        }
       } else {
       	   if (cousetype==2 && fteam==0 && steam==0) {
       	   	   alert("课程类型请填完整！");
       	   } else {
       	   	   alert("请填写年级属性！");
       	   }
       
       }
    });
}

function showEditInterestInfo(cid) {
    var selectTimeList = $('.select-time-lesson>li');
    $.ajax({
        url: '/reverse/selSingleInterestClass.action',
        type: 'post',
        dataType: 'json',
        data: {
            classid: cid
        },
        success: function(data) {
            console.log(data);
            var classInfo = data.classInfo;
            var classtimeArray = classInfo.classtime.split(',');
            $('.edit-interest-name').val(classInfo.classname);
            $('.add-interest-teacher').select2('val', classInfo.teacherid);
            $('.add-class-subject').val(classInfo.subjectid);
            $('.edit-interest-number').val(classInfo.studentcount);
            if (classInfo.opentime!=null && classInfo.opentime!='') {
            	 $('#opentime').val(classInfo.opentime.replace(/-/g,'/').replace('T',' '));	
            }
            if (classInfo.closetime!=null && classInfo.closetime!='') {
            	$('#closetime').val(classInfo.closetime.replace(/-/g,'/').replace('T',' '));
           }
            selectTimeList.each(function() {
                var t = $(this).attr('val');
                for (var i = 0; i < classtimeArray.length - 1; i++) {
                    if (classtimeArray[i] == t) {
                        $(this).addClass('selected');
                        break;
                    }
                }
            });
            var gradeList = data.classInfo.gradeArry;
            var gradeAry = gradeList.split(',');
			var checkList = $('.show-grade input:checkbox');
            for (var i in gradeAry) {
                for (var j = 0; j < checkList.length; j++) {
                    if (checkList.eq(j).attr('gid') == gradeAry[i]) {
                        checkList.eq(j).prop('checked', true);
                        break;
                    }
                }
            }
        }
    });
}

function validateInterestClass() {
    if ($.trim($('.edit-interest-name').val()) == '') {
        $('.edit-interest-name').next().remove();
        $('.edit-interest-name').after('<span class="edit-error">请输入班级名称</span>');
    } else {
        $('.edit-interest-name').next().remove();
    }
    if ($('.edit-interest-number').val() == '' || $('.edit-interest-number').val() == 0) {
        $('.edit-interest-number').next().remove();
        $('.edit-interest-number').after('<span class="edit-error">请输入班级人数</span>');
    } else {
        $('.edit-interest-number').next().remove();
    }
    var startTime = new Date($('#opentime').val());
    var closeTime = new Date($('#closetime').val());
    startTime = startTime.getTime();
    closeTime = closeTime.getTime();
    if (startTime > closeTime) {
        $('#closetime').next().next().remove();
        $('#closetime').next().after('<span class="edit-error">请输入正确的开放关闭时间</span>');
    } else {
        $('#closetime').next().next().remove();
    }
}

function updateInterestClass(cid) {
    $('.edit-commit-btn').on('click', function() {
        var selectTimeList = $('.select-time-lesson>li');
        var content = '';
        var startTime = $('#opentime').val();
        var closeTime = $('#closetime').val();
        selectTimeList.each(function() {
            if ($(this).hasClass('selected')) {
                content += $(this).attr('val') + ',';
            }
        });
        var checkList = $('.edit-subject-selectgrade-interest input:checkbox');
        var grList = '';
        checkList.each(function() {
            if ($(this).is(':checked')) {
                if ($(this).attr('gid')) {
                    grList += $(this).attr('gid') + ',';
                }
            }
        });
         if (grList != '') {
	        validateInterestClass();
	        if ($('.edit-interest').find('.edit-error').length == 0) {
	            $.ajax({
	                url: '/reverse/updateInterestClass.action',
	                type: 'post',
	                dataType: 'json',
	                data: {
	                    interestClassName: $.trim($('.edit-interest-name').val()),
	                    teacherid: $('.add-interest-teacher').select2('val'),
	                    studentCount: $('.edit-interest-number').val(),
	                    classtime: content,
	                    subjectid: $('.edit-interest .add-class-subject').val(),
	                    classid: cid,
	                    opentime: startTime,
	                    closetime: closeTime,
	                    	gradeArry: grList
	                },
	                success: function(data) {
	                    if (data.resultCode == 0) {
	                        alert('修改成功！');
	                        getInterestGradeList();
	                    } else {
	                        alert('修改失败！');
	                    }
	                },
	                error: function() {
	                    alert('修改失败！');
	                },
	                complete: function() {
	                    hideAlert();
	                }
	            });
	        }
	   } else {
	   alert("请填写年级属性！");
	   }
    });
}

function deleteInterestClass(cid, target) {
    if (confirm('确定要删除该拓展课吗？')) {
        $.ajax({
            url: '/reverse/deleteInterestClass.action',
            type: 'post',
            dataType: 'json',
            data: {
                classid: cid
            },
            success: function(data) {
                if (data.resultCode == 0) {
                    alert('删除成功');
                    target.parent().remove();
                } else {
                    alert('删除失败');
                }
            },
            error: function() {
                alert('删除失败');
            }
        });
    }
}

function deleteInterestClassUser(cid,tid, userid, target) {
    if (confirm('确定要删除该学生吗？')) {
        $.ajax({
            url: '/reverse/deleteInterestUserInfo.action',
            type: 'post',
            dataType: 'json',
            data: {
                classid: cid,
                studentid: userid,
               	type: tid
            },
            success: function(data) {
                if (data.resultCode == 0) {
                    alert('删除成功');
                    target.parent().remove();
                } else {
                    alert('删除失败');
                }
            },
            error: function() {
                alert('删除失败');
            }
        });
    }
}


function getSelectGradeList() {
    $.ajax({
        url: '/reverse/showGradeInfo.action',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
            var row = data.GradeIdList;
        	$('.edit-grade-value').empty();
            for (var i = 0; i < row.length; i++) {
                var content = '';
                content += '<option value=' + row[i].status + '>' + row[i].description + '</option>';
                $('.edit-grade-value').append(content);
            }	
        },
        error: function() {
            console.log('showGradeInfo error');
        }
    });
}

function getSelectRadioGradeList(blong) {
	var target = $('.edit-subject-selectgrade');
	
		if (blong=="subject") {
		target = $('.edit-subject-selectgrade');
		} else if ("interest") {
		target = $('.edit-subject-selectgrade-interest');
		}
	
    $.ajax({
        url: '/reverse/selSchoolGradeInfo.action',
        async:false,
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
            console.log(data.rows);
            var row = data.rows;
            
            target.empty();
            target.append('<label class="checkbox-inline"><input type="checkbox" class="edit-select-all"> 全部</label>');
            for (var i = 0; i < row.length; i++) {
                var content = '';
                content += '<label class="checkbox-inline"><input type="checkbox" gid=' + row[i].id + '> ' + row[i].gradeName + '</label>';
                target.append(content);
            }
        },
        error: function() {
//            console.log('error');
        }
    });
}

function getSelectSubjectList() {
    var target = $('.add-class-subject');
    target.empty();
    $.ajax({
        url: '/reverse/selSchoolSubject.action',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
            var list = data.schoolSubjectList;
            for (var i = 0; i < list.length; i++) {
                var content = '';
                content += '<option value=' + list[i].id + '>' + list[i].name + '</option>';
                target.append(content);
            }
        },
        error: function() {
            console.log('selSchoolSubject error');
        }
    });
}

function getSelectClassSubjectList(cid) {
    var target = $('.add-class-subject');
    target.empty();
    $.ajax({
        url: '/reverse/selSubjectInfoByClassId.action',
        type: 'post',
        dataType: 'json',
        data: {
            classid: cid
        },
        success: function(data) {
            console.log(data);
            var list = data.subjectList;
            for (var i = 0; i < list.length; i++) {
                var content = '';
                content += '<option value=' + list[i].id + '>' + list[i].name + '</option>';
                target.append(content);
            }
        },
        error: function() {
            console.log('selSchoolSubject error');
        }
    });
}

function getSelectClassGradeList() {
    $.ajax({
        url: '/reverse/selSchoolGradeInfo.action',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
            var row = data.rows;
            var target = $('.change-class-grade');
            target.empty();
            for (var i = 0; i < row.length; i++) {
                var content = '';
                content += '<option value=' + row[i].id + '>' + row[i].gradeName + '</option>';
                target.append(content);
            }
            getSelectClassList(row[0].id);
        }
    });
}

function getSelectClassList(gid) {
    $.ajax({
        url: '/reverse/selClassByGradeId.action',
        type: 'post',
        dataType: 'json',
        data: {
            gradeid: gid
        },
        success: function(data) {
            var row = data.classList;
            var target = $('.change-class-class');
            target.empty();
            for (var i = 0; i < row.length; i++) {
                var content = '';
                content += '<option value=' + row[i].id + '>' + row[i].classname + '</option>';
                target.append(content);
            }
        }
    });
}

// 分页初始化
function initPaginator(option) {
    var totalPage = '';
    $('.page-paginator').show();
    $('.page-index').empty();
    if (option.total % option.pagesize == 0) {
        totalPage = option.total / option.pagesize;
    } else {
        totalPage = parseInt(option.total / option.pagesize) + 1;
    }
    buildPaginator(totalPage, option.currentpage);
    option.operate(totalPage);
}

function buildPaginator(totalPage, currentPage) {
    if (totalPage > 5) {
        if (currentPage < 4) {
            for (var i = 1; i <= 5; i++) {
                if (i == currentPage) {
                    $('.page-index').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index').append('<span>' + i + '</span>');
                }
            }
            $('.page-index').append('<i>···</i>');
        } else if (currentPage >= 4 && currentPage < (totalPage - 2)) {
            $('.page-index').append('<i>···</i>');
            for (var i = currentPage - 2; i <= currentPage + 2; i++) {
                if (i == currentPage) {
                    $('.page-index').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index').append('<span>' + i + '</span>');
                }
            }
            $('.page-index').append('<i>···</i>');
        } else {
            $('.page-index').append('<i>···</i>');
            for (var i = totalPage - 4; i <= totalPage; i++) {
                if (i == currentPage) {
                    $('.page-index').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index').append('<span>' + i + '</span>');
                }
            }
        }
    } else {
        for (var i = 1; i <= totalPage; i++) {
            if (i == currentPage) {
                $('.page-index').append('<span class="active">' + i + '</span>');
            } else {
                $('.page-index').append('<span>' + i + '</span>');
            }
        }
    }
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

// 初始化高度
function initHeight() {
    var h = document.documentElement.clientHeight;
    $('.manage-left-container').css('minHeight', h - 160);
}



function getRadio() {
	if (document.getElementsByName("classtype")[0].checked) {
	$('#fteam').attr("checked", false);
	$('#steam').attr("checked", false);
	} else {
	$('#fteam').attr("checked", true);
	$('#steam').attr("checked", true);
	}
}
