$(function(){
	$('#loading').show();
	$.ajax({
		url:"/questions/QuestionCensusCount.action",
		type:"post",
		dataType:"json",
		success:function(data){
			$(".mytest-do-number").append(data.doneQuestionCount);
			$(".mytest-correct-number").append(data.doneQuestionRightCount);
		}
	});
	$.ajax({
		url:"/questions/getSubject.action",
		type:"post",
		dataType:"json",
		success:function(data){
			var list = data.data;
			$.each(list,function(i,e){
				subject(e.subjectId,e.subjectName);
			});
		},
		complete:function(){
			$('#loading').fadeOut(1000);
		}
	});
});

function subject(subjectId,SubjectName){
	$.ajax({
		url:"/questions/questionCensus.action",
		async: false,
		data:"subject="+subjectId,
		type:"post",
		dataType:"json",
		success:function(data){
			var value = '';
			value = point(subjectId,SubjectName);
			$(".table-top").append("<tr class='tr-mytest-subject'><td style='position:relative;'><img src='/img/open.png' class='subject-open  "+subjectId+"' onclick='isShow("+subjectId+")'><span>"+SubjectName+"</span></td><td>"+data.totalCount+"</td><td>"+data.right+"</td></tr>"+value);
		}
	});
}

function point(subjectId,SubjectName){
	var value = '';	
	$.ajax({
			url:"/questions/getPaperPoint.action",
			data:"subject="+subjectId,
			async: false,
			type:"post",
			dataType:"json",
			success:function(data){
				var list = data.returnData;
				$.each(list,function(i,e){
					value += pointCentus(subjectId,e,SubjectName);
				});
			}
	});
	return value;
}

function pointCentus(subjectId,pointName,SubjectName){
	var value = '';
	$.ajax({
		url:"/questions/questionCensus.action",
		async: false,
		data:"subject="+subjectId+"&examPoint="+pointName,
		type:"post",
		dataType:"json",
		success:function(data){
			value = "<tr class='mytest-subject-detail "+subjectId+"trs' style='display: none' value='true'><td style='text-indent:60px;'><span>"+pointName+"</span></td><td>"+data.totalCount+"</td><td>"+data.right+"</td></tr>";
		}
	});
	return value;
}

function isShow(subjectId){
	var flag = $("."+subjectId+"trs").attr('value');
	if("true"==flag){
		$("."+subjectId).attr('src','/img/subject-close.png');
		$("."+subjectId+"trs").attr('value','false');
		$("."+subjectId+"trs").css('display' ,'');
	}else{
		$("."+subjectId).attr('src','/img/open.png');
		$("."+subjectId+"trs").attr('value','true');
		$("."+subjectId+"trs").css('display' ,'none');
	}
}