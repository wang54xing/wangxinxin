/**
 * Created by NiuXin on 14-6-24.
 * 在线测评
 */

//选择题目部分
function getSubject(){
    $.getJSON('/questions/getSubject.action', function(response){
        $('#subject-list div:gt(0)').remove();
        var temp = $('<div class="radio col-xs-1 subjectRadio"><label class="test-label"></label></div>');
        for(var i in response.data) {
            var sub = response.data[i];
            var sel_div = temp.clone();
            if(sub.subjectId>=31 && sub.subjectId<=39){
            sel_div.children('label').html('<input onchange="chooseSubject(this.value)" type="radio" name="subject" value="'+ sub.subjectId +'"/>' + sub.subjectName);
            sel_div.appendTo($('#subject-list'));
        	}
        }
        var first_subject = $('input[name="subject"]:first');
        //first_subject.attr('checked', 'checked');
        first_subject.click();
        //chooseSubject(first_subject.val());
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
            for(var i in response.data) {
                var q_type = response.data[i];
                var q_type_label = template.clone();
                q_type_label.html('<input onchange="submitTestPara()" type="checkbox" name="question-type" value="'+ q_type +'">' + q_type);
                q_type_label.appendTo($('#type-list'));
            }
            submitTestPara();
            $('#selection-table tr:gt(0)').show();
        },
        error: function(e) {
            console.log(e);
        }
    });
}

var total_question = -1;

function submitTestPara() {
    var subjectId = $('input[name="subject"]:checked').val();
    var points = $('input[name="point"]:checked').map(function(){return this.value;}).get().join('#');
    var q_type = $('input[name="question-type"]:checked').map(function(){return this.value;}).get().join(',');
    var old_exam = $('input[name="old-exam"]:checked').map(function(){return this.value;}).get().join(',');
    var isDone = $('input[name="have-done"]:checked').val();

    $('#sel-result').html('');
    $('#sel-result').addClass('hardloadings');
    $.ajax({
        url:'/questions/getCount.action',
        type:'post',
        data:{
            'subject': subjectId,
            'examPoint': points,
            'type': q_type,
            'materials': old_exam,
            'isDone': isDone
        },
        success: function(response) {
            $('#sel-result').removeClass('hardloadings');
            $('#sel-result').html('当前选中了'+ response.totalCount +'题，其中' + response.doneCount + '题已做。');
            total_question = response.totalCount;
        },
        error: function(e) {
            console.log(e.responseText);
        }
    });
}

function startTest() {
	$.ajax({
        url:'/questions/isQuestionBuy.action',
        async:false,
        type:'post',
        dataType:"json",
        success: function(response){
        	if(response.isQuestionBuy==false){
        		$(".show-pay-type").css("display","");
        	}else{
        		 if(total_question > 0) {
        		        location.href = '/doexam';
        		 }else{
        		        MessageBox('所选试题为空。', -1);
        		 }
        	}
        },
        error: function(e) {
            console.log(e.responseText);
        }
	});
   
}

//答题部分

//获取题目
function getQuestion() {
    $.ajax({
        url:'/questions/getPaperNew.action',
        type:'get',
        cache: false,
        success: function(response){
        	if(response.data == null) {
                ConfirmDialog('已完成所有试题！是否查看我的错题集？', "location.href='/user/myQuestionBook'");
                return;
            }

            $('#q-no').html(response.data.tid);
            $('#q-res').html(response.data.unit);
            $('#q-diff').html(response.data.difficulty);
            $('#q-type').html(response.data.type);
            $('#q-point').html(response.data.examPoint);
            $('#q-score').html(response.data.value);

            $('.question-title').html(response.data.proposition);
            $('.analysis').html(response.data.answer);
            var selections = $('ul.select-answer').empty();
            questionId = response.data.id;
            isChoiceQuestion = response.data.isSelect == 'true';
            if(isChoiceQuestion) {
                $('.correct-answer').html(response.data.objectiveQuestionsAnswer);
                for (var i = 0; i < response.data.groupNumber; i++) {
                    var sel = "<li class='clear'>";
                    sel += '<span>' + (i + 1) + '.</span>';
                    sel += '<label class="checkbox-inline"><input name="' + i + '" type="checkbox" value="A"> A</label>';
                    sel += '<label class="checkbox-inline"><input name="' + i + '" type="checkbox" value="B"> B</label>';
                    sel += '<label class="checkbox-inline"><input name="' + i + '" type="checkbox" value="C"> C</label>';
                    sel += '<label class="checkbox-inline"><input name="' + i + '" type="checkbox" value="D"> D</label>';
                    sel += '</li>';
                    selections.append(sel);
                }
            } else {
                $('#text-answer-div').show();
            }

            if(response.data.isJoinWrongCollection == 'true') {
                $('.add-btn').hide();
            } else {
                $('.add-btn').show();
            }
            $('.exam-complete').fadeIn('fast');
            $('.question-profile').fadeIn('fast');
        },
        error: function(e) {
            console.log(e.responseText);
//            MessageBox('服务器错误！', -1);
        }
    });
}

//完成答题
function completeQuestion() {
    $('.exam-complete').hide();
    var ans_ul = $('.select-answer');
    var $yourAnswer = $('.your-answer').empty();
    if(isChoiceQuestion) {
        var submit_answer = [];
        for (var i = 0; i < ans_ul.find('li').length; i++) {
            var selectAnswer = [];
            ans_ul.find('li').eq(i).find('input').each(function () {
                if ($(this).is(':checked')) {
                    selectAnswer.push(this.value);
                }
            });
            submit_answer.push(selectAnswer.join(''));
            $yourAnswer.append('<div>' + (i + 1) + '. ' + selectAnswer.join(' ') + '</div>');
        }
        //提交答案
        $.get('/questions/doQuestion.action', {'questionId': questionId, 'answer': submit_answer.join(',')});

        $('.correct-container').show();
        //$('.your-container').show();
    }
    else {
        $('.correct-container').hide();
        //$('.your-container').hide();
        var text_answer = $.trim($('#text-answer').val());
        $yourAnswer.html(text_answer.replace(/\n/g,'<br/>'));
    }
    $('.answer-container').slideDown('slow');
}

//重置答题页面
function resetQuestionPage() {
    $('.answer-container').fadeOut('fast');
    $('.exam-complete').hide();
    $('.question-title').html('');
    $('.question-profile').hide();
    $('.analysis').html('');
    $('.correct-answer').html('');
    $('ul.select-answer').empty();
    $('#text-answer').val('');
    $('#text-answer-div').hide();
    //$('.add-btn').off('click');
}

//下一题
function nextQuestion() {
    resetQuestionPage();
    getQuestion();
}

//加入错题集
function addQuestionBook() {
    $.ajax({
        url: '/questions/addWrongCollection',
        data: {'questionId':questionId},
        success: function(response){
            if(response.result) {
                MessageBox('已加入错题集。', 1);
                $('.add-btn').hide();
            }
            else {
                MessageBox('加入失败。', -1);
            }
        },
        error: function(e) {
            console.log(e.responseText);
            MessageBox('服务器响应请求出错，请稍后再试。', -1);
        }
    });
}

months = 12;//全局变量统计包月时长默认为一年
paytypes = 'lanbei';//全局变量统计支付方式默认是蓝贝
questionPriceByMonth = 0;//全局变量试题包月单位

$(function(){
	$.ajax({
		url:"/questions/getQuestionPerMonthPrice.action",
		async:false,
		type:"post",
		dataType:"json",
		success:function(data){
			questionPriceByMonth = data.result;
		}
	});
	var money = getMoney(months,questionPriceByMonth);
	$(".pay-money").html(money+"元");
});
//计算试题包月价格
function money(month,Timetype){
	if('year'==Timetype){
		$('.payhyear').removeClass('selected');
		$('.payyear').addClass('selected');
		$('.pay-input-month').val('');
	}else if('hyear'==Timetype){
		$('.payyear').removeClass('selected');
		$('.payhyear').addClass('selected');
		$('.pay-input-month').val('');
	}else{
		var myregex = new RegExp("^[0-9]*[1-9][0-9]*$");
		if(month!='' && !myregex.test(month)){
			alert("请输入大于0的整数！");
			return;
		}
		if(''==month){
			$('.payyear').addClass('selected');
			month = 12;
		}
	}
	months = month;
	var money = getMoney(month,questionPriceByMonth);
	$(".pay-money").html(money+"元");
}

//具体计算价格
function getMoney(month,price){
	if(month!=''){
		return (parseFloat(month)*parseFloat(price)).toFixed(2);
	}else{
		return 0.0;
	}
}

//选择输入月份时则取消一年和半年的选中标志
function removeselected(){
	$('.payyear').removeClass('selected');
	$('.payhyear').removeClass('selected');
}

//选择支付方式
function selectPayType(type){
	if('apliy'==type){
		$('.checklanbei').attr('checked',false);
		$('.checkyue').attr('checked',false);
		$('.pay-ali').addClass('selected');
		paytypes = "Alipay";
	}else{
		$('.pay-ali').removeClass('selected');
	}
}

//试题支付
function buyQuestion(){
	if(''==paytypes){
		alert("请选择一种支付方式!");
		return;
	}
	var orderId = '';
	paytypes = '';
	var ptyps = $(":checkbox[id='checkType']:checked");
	$.each(ptyps,function(i,t){
		paytypes +=$(t).attr("name");
	});
	if(''==paytypes){
		paytypes = "Alipay";
	}
	if('Alipay'==paytypes){
		orderId = getOrderId(months);
		var f = document.getElementById('online-pay-form');
	    f.action = "/questions/questionBuyByMonth.action";
	    f.payType.value = paytypes;
	    f.month.value = months;
	    f.orderId.value = orderId;
	    f.submit();
	    $(".show-pay-type").css("display","none");
	    $(".paying-complete").attr("onclick","checkorder("+orderId+")");
	    $(".paying-modal-container").css("display","");
	}else{
		orderId = getOrderId(months);
		$.ajax({
			url:"/questions/questionBuyByMonth.action",
			data:"month="+months+"&payType="+paytypes+"&orderId="+orderId,
			type:"post",
			dataType:"json",
			success:function(data){
				if(data.result){
					$(".show-pay-type").css("display","none");
					$(".pay-deadline-time").html(data.endTime);
					$(".pay-start").attr('onclick','startTest()');
					$(".qustion-pay-success").css("display","");
				}else{
					alert(data.resultText);
				}
			}
		});
	}
}

//生成订单编号
function getOrderId(month){
	var orderId = '';
	$.ajax({
		url:"/questions/getOrderId.action",
		data:"month="+month,
		type:"post",
		async:false,
		dataType:"json",
		success:function(data){
			orderId = data.result;
			if(orderId==''){
				alert("订单生成失败!");
				return;
			}
		},
		error: function(e){
			MessageBox('服务器响应请求失败，请稍后再试。', -1);
		}
	});
	return orderId;
}

//支付遇到问题
function questionCanNotPay(){
	$(".show-pay-type").css("display","");
    $(".paying-modal-container").css("display","none");
}

function checkorder(order){
	$.ajax({
		url:"/questions/chechOrder.action",
		data:"order="+order,
		type:"post",
		dataType:"json",
		success:function(data){
			if(data.result){
				$(".pay-deadline-time").html(data.endTime);
				$(".pay-start").attr('onclick','startTest()');
				$(".paying-modal-container").css("display","none");
				$(".qustion-pay-success").css("display","");
			}else{	
				$(".paying-modal-container").css("display","none");
				$(".pay-failed-container").css("display","");
			}
		},
		error: function(e){
			MessageBox('服务器响应请求失败，请稍后再试。', -1);
		}
	});
}

//重新支付
function retryPay(){
	$(".pay-failed-container").css("display","none");
	$(".show-pay-type").css("display","");
}
