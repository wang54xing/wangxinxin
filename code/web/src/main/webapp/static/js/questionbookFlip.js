/**
 * Created by NiuXin on 14-6-26.
 * 错题集
 */

//全选框
function selAllChanged(check_all) {
    if(check_all.checked) {
        $('input[name="question-del"]').prop('checked', true);
    } else {
        $('input[name="question-del"]').prop('checked', false);
    }
}

//错题集列表
function listQuestionBook() {
    $('#selection-table tr:gt(0)').hide();
    $('#question-table tr:gt(0)').remove();
    $.getJSON('/questions/getWrongQuestionByParam', fillQuestions);
}

//显示题目
function fillQuestions(response) {
    $('#question-table tr:gt(0)').remove();
    for(var i in response.data) {
        var question = response.data[i];
        var tr = '<tr><td><input type="checkbox" name="question-del" value="' + question.id + '"/></td>';
        tr += '<td>' + question.questionId + '</td>';
        tr += '<td>' + question.subject + '</td>';
        tr += '<td>' + question.paperPoint + '</td>';
        tr += '<td>' + question.type + '</td>';
        tr += '<td>' + question.difficult + '</td>';
        tr += '<td>' + question.resource + '</td></tr>';
        $('#question-table').append(tr);
    }
}

//获取科目
function getSubject(){
    $.getJSON('/questions/getSubject.action', function(response){
        $('#subject-list div:gt(0)').remove();
        var temp = $('<div class="radio col-xs-1 subjectRadio"><label class="test-label"></label></div>');
        for(var i in response.data) {
            var sub = response.data[i];
            var sel_div = temp.clone();
            if(sub.subjectId>=31 && sub.subjectId<=44) {
                sel_div.children('label').html('<input onchange="chooseSubject(this.value)" type="radio" name="subject" value="' + sub.subjectId + '"/>' + sub.subjectName);
                sel_div.appendTo($('#subject-list'));
            }
        }
    })
}

function chooseSubject(subjectId) {
    var loading = true;
    $.getJSON('/questions/getPaperPoint.action', {'subject': subjectId}, function(response) {
        $('#point-list').empty();
        var template = $('<label class="checkbox-inline col-xs-2 test-label"></label>');
        for(var i in response.returnData) {
            var point = response.returnData[i];
            var point_label = template.clone();
            point_label.html('<input onchange="choosePaperPoint()" type="checkbox" name="point" value="'+ point +'">' + point);
            point_label.appendTo($('#point-list'));
        }
        if(loading) {
            loading = false;
        }
        else {
            submitTestPara();
            $('#selection-table tr:gt(0)').show();
        }
    });

    $.getJSON('/questions/getTypeBySubject.action', {'subjectId': subjectId}, function(response) {
        $('#type-list').empty();
        var template = $('<label class="checkbox-inline col-xs-2 test-label test-type"></label>');
        for(var i in response.data) {
            var q_type = response.data[i];
            var q_type_label = template.clone();
            q_type_label.html('<input onchange="submitTestPara()" type="checkbox" name="question-type" value="'+ q_type +'">' + q_type);
            q_type_label.appendTo($('#type-list'));
        }
        if(loading) {
            loading = false;
        }
        else {
            submitTestPara();
            $('#selection-table tr:gt(0)').show();
        }
    });

}

function choosePaperPoint()
{
    var subjectId = $('input[name="subject"]:checked').val();
    var points = $('input[name="point"]:checked').map(function(){return this.value;}).get().join('#');
    $.ajax({
        url: '/questions/getTypeBySubject.action',
        type: 'post',
        dataType: 'json',
        data: {'subjectId': subjectId, 'examPoint': points},
        success: function(response) {
            $('#type-list').empty();
            var template = $('<label class="checkbox-inline col-xs-2 test-label test-type"></label>');
            for (var i in response.data) {
                var q_type = response.data[i];
                var q_type_label = template.clone();
                q_type_label.html('<input onchange="submitTestPara()" type="checkbox" name="question-type" value="' + q_type + '">' + q_type);
                q_type_label.appendTo($('#type-list'));
            }
            submitTestPara();
            $('#selection-table tr:gt(0)').show();
        },
        error:function(e) {
            console.log(e);
        }
    });
}


function submitTestPara() {
    var subjectId = $('input[name="subject"]:checked').val();
    var _data = {};
    if(subjectId != '0') {
        var points = $('input[name="point"]:checked').map(function(){return this.value;}).get().join('#');
        var q_type = $('input[name="question-type"]:checked').map(function(){return this.value;}).get().join(',');
        var old_exam = $('input[name="old-exam"]:checked').map(function(){return this.value;}).get().join(',');
        _data = {
            'subject': subjectId,
            'examPoint': points,
            'type': q_type,
            'materials': old_exam
        };
    }

    $('#sel-result').html('');
    $('#sel-result').addClass('hardloadings');
    $.ajax({
        url:'/questions/getWrongQuestionByParam',
        type:'post',
        data:_data,
        success: fillQuestions,
        error: function(e) {
            console.log(e.responseText);
        }
    });
}

//批量删除错题集
function removeFromQuestionBook() {
    var qid = $('input[name="question-del"]:checked').map(function(){return this.value;}).get().join(',');
    if(qid) {
        ConfirmDialog('确定从错题集中移除选定题目？', 'doRemoveFromQuestionBook()');
    }
}

function doRemoveFromQuestionBook() {
    var qid = $('input[name="question-del"]:checked').map(function(){return this.value;}).get().join(',');
    if(qid) {
        MessageBox('移除中...', 0);
        $.ajax({
            url: '/questions/delWrongCollection',
            data: {'wrongCollectionId':qid},
            success: function(response){
                if(response.data) {
                    MessageBox('已从错题集中移除。', 1);
                    submitTestPara();
                }
                else {
                    MessageBox('移除失败。', -1);
                }
            },
            error: function(e) {
                console.log(e.responseText);
                MessageBox('服务器响应请求出错，请稍后再试。', -1);
            }
        });
    }
}

//开始答题
function startTest() {
    var qid = $('input[name="question-del"]:checked').map(function(){return this.value;}).get().join(',');
    if(qid) {
        $('#commit-btn').prop('disabled', true);
        $('#commit-btn').html('等待开始...');
        $('#commit-btn').css('cursor', 'wait');
        $.ajax({
            url: '/questions/getWrongQuestionToDo',
            data: {'questionIds': qid},
            success: function(response) {
                $('#commit-btn').prop('disabled', false);
                $('#commit-btn').css('cursor', 'auto');
                location.href = '/flippedDoexam';
            },
            error: function(e) {
                console.log(e.responseText);
                MessageBox('服务器响应请求出错。', -1);
                $('#commit-btn').prop('disabled', false);
                $('#commit-btn').html('开始答题');
                $('#commit-btn').css('cursor', 'auto');
            }
        });
    }else{
        MessageBox('所选试题为空。', -1);
    }
}