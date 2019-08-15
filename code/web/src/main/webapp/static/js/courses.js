$(function() {
//    var subject = $('.course-ul li.active a').text();
    $('.new-course-img').hover(function() {
        $(this).children('div').show();
    }, function() {
        $(this).children('div').hide();
    });
    /*$('#subjectName').text(subject);

     document.title = '高中' + subject + '_高考' + subject + '视频学习教程';*/
});


function getRecentOrder() {
    $.ajax({
        url: '/listLastOrders.action',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {

        },
        error: function() {
            console.log('getRecentOrder error');
        }
    });
}