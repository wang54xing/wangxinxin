<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

    <link rel="stylesheet" href="/static_new/css/new33/course.css">
	<script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <style type="text/css">
		.right-pos{
			top: -140px;
		}
    </style>
	<script>
		$('.t-down').click(function(){
			$('.td-over').scrollTop();
			$('.td-over').css('margin-top',-91)
		})
		$('.t-top').click(function(){
			$('.td-over').css('margin-top',0)
		})
	</script>

<body>

<div class="right-pos">
    <div id="t-top" class="t-top">
        <span></span>
    </div>
	<div class="ov-hi">
		<div class="td-over">
			<a href="/newisolatepage/classpage.do" class="jss1">教室</a>
			<a href="/newisolatepage/courseRangePage.do" class="va1">课表</a>
			<a href="/newisolatepage/baseWeek.do" class="jzz1">教学周</a>
            <a href="/newisolatepage/classTime.do"  class="ve1">学科&课时</a>
			<a href="/newisolatepage/baseTeacher.do"class="vd1">教师</a>
			<a href="/newisolatepage/baseAffair.do" class="vs1">事务</a>
            <!-- <a href="/newisolatepage/baseGuiZe.do" class="gz1">排课规则</a> -->
		</div>
	</div>
    <div id="t-down" class="t-down">
        <span></span>
    </div>
</div>
</body>

<script>

</script>
</html>
