/**
 * Created by yan on 2015/6/4.
 */
/* 同学列表排行  ****************************/
function getStudentRank(async, cid, classtype) {
    var ret = [];
    async = typeof async == 'undefined' ? false : true;
    $.ajax({
        url: "/user/studenttopfive",
        type: "get",
        dataType: "json",
        async: async,
        data: {
//                    'classId': cid,
//                    'classtype': classtype
        },
        success: function (data) {
            ret = data;
            showStudentRank(data);
        },
        complete: function () {
        }
    });
    return ret;
}

function showStudentRank(data) {
    var target = $('#fc_stu_rank');
    var html = '';
    $('#version_student_sort').empty();
    if(data)
    {
        jQuery(".homepage-left-bottom").show();
        for (var i = 0;i< data.length;i++) {
            var r = data[i];
            var html='<li>';
            html+='<img src="'+r.imgUrl+'">';
            html+='<div>';
            html+='<span class="homepage-left-bottom-I ellipsis">'+r.userName+'</span>&nbsp';
            html+='<span>经验值</span>';
            html+='<span class="homepage-left-bottom-II">'+r.experienceValue+'</span>';
            html+='</div>';
            html+='</li>';
            jQuery("#version_student_sort").append(html);
        }
    }
}