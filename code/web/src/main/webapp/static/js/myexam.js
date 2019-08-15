/**
 * Created by NiuXin on 14-6-17.
 * 翻转课堂试卷
 */

//教师部分
//新建试卷部分
var newPaperId = null;
var currentPage = null;

function uploadPaperWord(form) {
    newPaperId = null;
    $('#newexam-buttons').hide();
    $('#exampaper-preview').html('');
    form.action = '/reverse/wordUpload';
    if ($("input[name='word']").val() == '') {
        MessageBox('请选择试卷文档。', -1);
        return;
    }
    MessageBox('试卷上传中...', 0);
    fileUpload(form, function (content) {
        try {
            var s = $(content).text();
            var json = $.parseJSON(s);
            if (json.result) {
                newPaperId = json.paperId;
                var html = '';
                for (var i in json.data) {
                    var problem = json.data[i];
                    html += problem.proposition;
                    html += '<br/><p><b>解析：</b></p>';
                    html += problem.answer;
                    html += '<br/>'
                }
                $('#exampaper-preview').html(html);
                ClosePromptDialog();
                $('#newexam-buttons').slideDown();
            } else {
                MessageBox('试卷解析出错。', -1)
            }
        } catch (err) {
            MessageBox('服务器响应请求出错。', -1);
        }
    });
}

function uploadFileChanged(input_file) {
	var filename = input_file.value;
	var doctype = filename.substring(filename.indexOf('.'));
    if (doctype.indexOf('.xls') > -1 || doctype.indexOf('.doc') > -1) {
		$('#upfile-name').val(filename);
	    uploadPaperWord(input_file.form);
	}else{
        MessageBox('请检查文件格式，只支持.xls、.xlsx、.doc、docx格式的模板。', -1);
		$('input[name="word"]').remove();
		$('form').prepend('<input onchange="uploadFileChanged(this)" id="paperUpload" name="word" type="file" class="new-input" style="width:0px;height:0px">');
	}
}

function createExamPaper() {
    if (newPaperId == null) {
        return;
    }
    var paper_name = $.trim($('#new-paper-name').val());
    if (paper_name == '') {
        MessageBox('请输入试卷名称。', -1);
        return;
    }
    var class_sel = $('input[name="new-paper-class"]:checked').map(function () {
        return this.value
    }).get().join(',');
    if (class_sel.length <= 0) {
        MessageBox('请选择班级。', -1);
        return;
    }
    MessageBox('保存中，请稍后...', 0);
    $.ajax({
        url: '/reverse/toIssuePaper.action',
        type: 'post',
        data: {
            'papername': paper_name,
            'classId': class_sel,
            'result': true,
            'paperId': newPaperId
        },
        success: function (data) {
            if (data.result) {
                MessageBox('保存成功。', 1);
                cancelCreateExamPaper();
                if (data.score) {
                    scoreManager(data.scoreMsg, data.score);
                }
                showExamList();
            } else {
                MessageBox('保存失败。', -1);
            }
        },
        error: function (e) {
            MessageBox('服务器响应请求出错。', -1);
        }
    })
}

function showExamList() {
    getTeacherExamPaper(1, 8);
    $('.exam_list').show();
    $('.newexam_form').hide();
    $('.myexam_bar>span').text('上传考卷');
    $('#example').show();
}

function cancelCreateExamPaper() {
    newPaperId = null;
    $('#new-paper-name').val('');
    $("input[name='new-paper-class']:checked").removeAttr('checked');
    var control = $("input[type='file'][name='word']")[0];
    control.form.reset();
    $('#upfile-name').val('点击此处选择试卷文档');
    $('#newexam-buttons').hide();
    $('#exampaper-preview').html('');
}

//教师试卷列表
function getTeacherExamPaper(page,size) {
    $('.exam_list').html('<div class="hardloadingt" style="min-height: 40px"></div>');
    $.getJSON('/reverse/teaPaperList.action', {
        'classId': currentClassId,
        'page':page,
		'pageSize': size
    }, function (response) {
        $('.exam_list').empty();
        for (var i in response.data) {
            var paper = response.data[i];

            var out_div = $('<div style="position:relative;" />');
            $('<img src="/img/class.png"/>').appendTo(out_div);

            var div1 = '<div style="width:50%"><p style="margin-bottom:5px;"><a onclick="showExamDetail(' + paper.id + ', \'' + paper.name + '\')" class="exam_title">' + paper.name + '</a></p>\
            <p style="margin-top:5px"><span style="margin-right:10px;">' + paper.nickName + '</span>\
            <span>于 ' + paper.createTime.replace('T', ' ') + ' 发表</span><span style="margin-left:10px;"></span></p></div>';
            $(div1).appendTo(out_div);

            var div2 = '<div style="width:40%"><p style="margin-bottom:5px;">答题人数</p>' +
                '<p style="margin-top:5px;margin-left:6px">' + paper.doneCount + '/' + paper.studentCount +
                '<button class="exam_delete" onclick="delExamPaper(' + paper.id + ')">删除</button></p></div>';
            $(div2).appendTo(out_div);

            out_div.appendTo($('.exam_list'));
        }
        //重设分页导航
        resetPage(response,page,size);
    })
}

function showExamDetail(eid, paperName) {
    $('.exam_list').hide();
    $('.viewexam-container').addClass('hardloadingt');
    $('.viewexam-container').empty().show();
    $('.correctexam-container').hide();

    $('.myexam_bar > span:first-child').html('<a onclick="$(\'.tab_button\')[4].click()">上传考卷</a> > <a onclick="showExamDetail(' + eid + ', \'' + paperName + '\')">' + paperName + '</a>');
    $('#example').hide();
    $.getJSON('/reverse/teaPaperDetial.action', {
        'paperId': eid
    }, function (response) {
        currentPaperData = response.data;
        $('.viewexam-container').removeClass('hardloadingt');
        var html = template('viewexam', response);
        $('.viewexam-container').html(html);
    });
}

function delExamPaper(eid) {
    ConfirmDialog('确定删除该试卷？', 'doDelExamPaper(' + eid + ')');
}

function doDelExamPaper(eid) {
	page = parseInt($('#example ul li.active').children('a').html());
    MessageBox('删除中...', 0);
    $.getJSON('/reverse/deletePaper.action', {
        'paperId': eid
    }, function (data) {
        if (data.data) {
            if (data.score) {
                scoreManager(data.scoreMsg, data.score);
            }
            MessageBox('删除成功。', 1);
            getTeacherExamPaper(page,8);
        } else {
            MessageBox('删除失败。', -1);
        }
    });
}

function showCorrecting(index) {
    $('.myexam_bar > span:first-child').contents().eq(3).remove();
    var path = ' > 题目' + (index + 1);
    $('.myexam_bar > span:first-child').append(path);

    $('.viewexam-container').hide();
    $('.correctexam-container').addClass('hardloadingt');
    $('.correctexam-container').empty().show();
    //$('.correctexam-container').html('<div class="hardloadingb" style="min-height: 50px"></div>').show();
    $.getJSON('/reverse/teaCorrectView.action', {
        'questionId': currentPaperData[index].id
    }, function (response) {
        $('.correctexam-container').removeClass('hardloadingt');
        response['index'] = index;
        if (index < currentPaperData.length - 1) {
            response['hasNext'] = true;
        }
        var html = template('correctexam', response);
        $('.correctexam-container').html(html);
    });
}

function correctAnswer(input_radio) {
    console.log(input_radio.name);
    $.get('/reverse/teaDoCorrect.action', {
        'answeritemsId': input_radio.name,
        'result': input_radio.value
    });
}


//学生部分
//试卷列表
function getStudentExamPaper(page,size) {
    $('.exam_list').html('<div class="hardloadingt"></div>');
    var currentClassId = $('.class_selected').attr('cid');
    $.getJSON('/reverse/stuPaperList.action', {
        classid: currentClassId,
        'page':page,
		'pageSize': size
    }, function (response) {
        template.helper('stateFormat', function (state) {
            switch (state) {
                case '0':
                    return '未答题';
                case '1':
                    return '已答题';
                case '2':
                    return '批改完成';
                default:
                    return '';
            }
        });
        var html = template('stu-exam-temp', response);
        $('.exam_list').html(html);
      //重设分页导航
        resetPage(response,page,size);
    });
}

function showStudentExamPaper(eid) {
    $('.answer-sheet').empty().addClass('hardloadingt').show();
    $('.exam_list').hide();
    $('#example').hide();
    
    $.getJSON('/reverse/preDoPaper.action', {
        'paperId': eid
    }, function (response) {
        $('.answer-sheet').removeClass('hardloadingt');
        var html = '';
        var j = 1;
        for (var i in response.data) {
            var question = response.data[i];
            html += '<div class="ques-container" style="overflow: visible;margin-bottom: 30px"><div style="font-size:14px;font-weight:bold;border-bottom:1px #ccc dashed;height:25px ">题目' + (j++) + '</div>' +
                '<div style="margin: 10px 0;">【题目】</div><div class="ques-content" style="position: relative;overflow: visible">';
            html += question.proposition + '</div>';
            html += '<div style="margin-top: 20px"><div style="margin-bottom: 10px;">【答题】</div>';
            if (question.type == '选择题') {
                html += '<div><input onchange="checkboxChanged(this, ' + eid + ')" type="checkbox" name="' + question.id + '" value="A">A<input onchange="checkboxChanged(this, ' + eid + ')" type="checkbox" name="' + question.id + '" value="B">B' +
                    '<input onchange="checkboxChanged(this, ' + eid + ')" type="checkbox" name="' + question.id + '" value="C">C<input onchange="checkboxChanged(this, ' + eid + ')" type="checkbox" name="' + question.id + '" value="D">D</div>';
            } else {
                html += '<form name="' + question.id + '" method="post" action="/reverse/doPaperText.action">'
                html += '<div><textarea onblur="textareaBlur(this)" name="answer"></textarea></div><div style="position: relative; margin-top: 20px">' +
                    '<span name="' + question.id + '" class="submit-button" style="text-align: center;width:80px; line-height: 30px">上传图片</span><input onchange="inputFileChanged(this)" name="question" type="file"/>' +
                    '</div><input type="hidden" name="questionId" value="' + question.id + '" />' +
                    '<input type="hidden" name="paperId" value="' + eid + '" /></form>';
            }
            html += '</div></div>';
        }
        html += '<button id="submit-paper-btn" style="margin-top: 10px" class="commit-btn" onclick="submitPaper(' + eid + ')">交卷</button>'
        $('.answer-sheet').html(html);
        
        currentPage = $('#example ul li.active a').html();
    });
}

function textareaBlur(textarea) {
    fileUpload(textarea.form, function (content) {
    });
}

function inputFileChanged(input_file) {
    var id = input_file.form.name;
    $('span[name="' + id + '"]').html('<img src="/img/loading4.gif" style="vertical-align: middle"/> 上传中');
    fileUpload(input_file.form, function (content) {
        $('span[name="' + id + '"]').html('重新上传');
        input_file.removeAttribute('disabled');
    });
    input_file.setAttribute('disabled', 'disabled');
}

function checkboxChanged(checkbox, eid) {
    var id = checkbox.name;
    var value = $('input[type="checkbox"][name="' + id + '"]:checked').map(function () {
        return this.value
    }).get().join('');
    $.get('/reverse/doPaper.action', {
        'questionId': id,
        'answer': value,
        'paperId': eid
    });
}

function submitPaper(eid) {
    ConfirmDialog('确认交卷？', 'doSubmitPaper(' + eid + ')');
}

function doSubmitPaper(eid) {
    MessageBox('交卷中...', 0);
    $.getJSON('/reverse/submitPaper.action', {
        'paperId': eid
    }, function (data) {
        if (data.result) {
            MessageBox('试卷已提交。', 1);
            $('#submit_section').hide();
            $('.newexam_container').show();
            $('.exam_list').show();
            $('#discuss_list').hide();
            $('.answer-sheet').hide();
            getStudentExamPaper(currentPage,8);
            $('#example').show();
            if (data.score) {
                scoreManager(data.desc,data.score);
            }
        } else {
            MessageBox('请先答题再提交。', -1);
        }
    });
}

//查看已经提交的试卷
function showPaperHaveDone(eid) {
    $('.answer-sheet').empty().addClass('hardloadingt').show();
    $('.exam_list').hide();
    $('#example').hide();
    $.getJSON('/reverse/preDoPaper.action', {
        'paperId': eid
    }, function (response) {
        $('.answer-sheet').removeClass('hardloadingt');
        var html = '';
        var j = 1;
        for (var i in response.data) {
            var question = response.data[i];
            html += '<div class="ques-container" style="overflow: visible;margin-bottom: 30px"><div style="font-size:14px;font-weight:bold;border-bottom:1px #ccc dashed;height:25px ">题目' + (j++) + '</div>' +
                '<div style="margin: 10px 0;">【题目】</div><div class="ques-content" style="position: relative;overflow: visible">';
            html += question.proposition + '</div>';
            html += '<div style="margin-top: 20px;margin-bottom: 10px;">【我的解答】</div>';
            if (question.myanswer != null) {
                html += '<div style="word-wrap: break-word">' + question.myanswer + '</div>';
            } else {
                html += '<div style="color: red">未作答</div>';
            }
            if (question.imgUrl) {
                html += '<div><img src="' + question.imgUrl + '"/></div>';
            }
            html += '</div>';
        }
        $('.answer-sheet').html(html);
        //$('.answer-sheet').html(html.replace(/null/gm, ' '));
    });
}

//查看教师批改后的试卷
function showPaperCorrected(eid) {
    $('.answer-sheet').empty().addClass('hardloadingt').show();
    $('.exam_list').hide();
    $('#example').hide();
    $.getJSON('/reverse/preDoPaper.action', {
        'paperId': eid
    }, function (response) {
        $('.answer-sheet').removeClass('hardloadingt');
        var html = '';
        var j = 1;
        for (var i in response.data) {
            var question = response.data[i];
            html += '<div class="ques-container" style="overflow: visible;margin-bottom: 30px"><div style="font-size:14px;font-weight:bold;border-bottom:1px #ccc dashed;height:25px ">题目' + (j++) + '</div>' +
                '<div style="margin: 10px 0;">【题目】</div><div class="ques-content" style="position: relative;overflow: visible">';
            html += question.proposition + '</div>';

            html += '<div style="margin-top: 20px;margin-bottom: 10px;">【我的解答】</div>';
            if (question.myanswer != null) {
                html += '<div style="word-wrap: break-word">' + question.myanswer + '</div>';
            } else {
                html += '<div style="color: red">未作答</div>';
            }
            if (question.imgUrl) {
                html += '<div><img src="' + question.imgUrl + '"/></div>';
            }
            html += '<div style="margin-top: 20px;margin-bottom: 10px;">【答案】</div>';
            html += '<div style="overflow: visible">' + question.answer + '</div>';
            if (question.istrue != null) {
                html += '<div style="margin-top: 20px;margin-bottom: 10px">【教师批改】</div>';
                if (question.istrue == "true") {
                    html += '<div style="overflow: visible; color: green">正确</div>';
                } else {
                    html += '<div style="overflow: visible; color: red">错误</div>';
                }
            }
            html += '</div>';
        }
        $('.answer-sheet').html(html);
        //$('.answer-sheet').html(html.replace(/null/gm, ' '));
    });
}

function resetPage(response, page,size){
	var total= response.total >0?response.total:0;
	var to=Math.ceil(total/size);
	totalPages = to==0?1:to;
	if(page==1)
		 {
		   resetPaginator(totalPages);
		 }else {
  	   resetPaginatorCurrentPage(page,totalPages);
	}
}