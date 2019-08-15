

//关闭弹窗
function dgHide() {
    $('.dialog').hide()
    $('.bg').hide();
    $('.z-popup4').hide();
}

//点击标签名
// $(document).on('click','.tag_show',function () {
//     $('.dialog-tag').show()
// })

//修改列名
// $(document).on('click','.col-edit',function () {
//     $('.dialog-name').show()
// })

// //清空该列
// $(document).on('click','.col-clear',function () {
//     $('.dialog-clear').show();
//     console.log($(this).parent().index())
//     var index=$(this).parent().index()
//     $('#qd_clear').click(function () {
//         dgHide();
//         $.each($('#main_table tbody tr'),function (i,item) {
//             var td= $(item).find('td').eq(index);
//             td.find('.jxb').removeClass('init').find('.name').text('选择教学班');
//             td.find('.teacher').removeClass('init').find('.name').text('选择老师');
//             td.find('.house').removeClass('init').find('.name').text('选择教室')
//         })
//     })
// })

//
// function activeTable(that,name){//选中相同变色
//     if(that.hasClass('active')){
//         $.each($('#main_table .jxb-zh-item'),function (i,item) {
//             $(item).removeClass('active');
//         })
//     }else{
//         $.each($('#main_table .jxb-zh-item'),function (i,item) {
//             $(item).removeClass('active');
//             if ($(item).find('.name').text() == name) {
//                 $(item).addClass('active');
//             }
//         })
//     }
// }


//选择教学班
// $(document).on('click','.jxb-zh-item.jxb',function () {
//     var name=$(this).find('.name').text();
//     var that=$(this);
//     if(name!='选择教学班'){
//         activeTable(that,name)
//     }
// })


// $(document).on('click','.jxb-zh-item.jxb .del',function (e) {//删除教学班名称
//     e.stopPropagation();
//     var that=$(this).parent().parent();
//     if(that.hasClass('init')){
//         that.removeClass('init').find('.name').text('选择教学班');
//         that.siblings('.teacher').removeClass('init').find('.name').text('选择老师');
//         that.siblings('.house').removeClass('init').find('.name').text('选择教室');
//         $.each($('#main_table .jxb-zh-item'),function (i,item) {
//             $(item).removeClass('active');
//         })
//     }
// })




//选择老师
// $(document).on('click','.jxb-zh-item.teacher',function () {
//     var name=$(this).find('.name').text();
//     var that=$(this);
//     if(name!='选择老师'){
//         activeTable(that,name)
//     }
// })
// $(document).on('click','.jxb-zh-item.teacher .cs',function (e) {
//     e.stopPropagation();
//     $('.dialog-teacher').show()
// })
// $(document).on('click','.jxb-zh-item.teacher .del',function (e) {//删除老师
//     e.stopPropagation();
//     var that=$(this).parent().parent();
//     if(that.hasClass('init')){
//         that.removeClass('init').find('.name').text('选择老师');
//         that.siblings('.house').removeClass('init').find('.name').text('选择教室');
//         $.each($('#main_table .jxb-zh-item'),function (i,item) {
//             $(item).removeClass('active');
//         })
//     }
// })

//选择教室
// $(document).on('click','.jxb-zh-item.house',function () {
//     var name=$(this).find('.name').text();
//     var that=$(this);
//     if(name!='选择教室'){
//         activeTable(that,name)
//     }
// })
// $(document).on('click','.jxb-zh-item.house .cs',function (e) {
//     e.stopPropagation();
//     $('.dialog-house').show()
// })
// $(document).on('click','.jxb-zh-item.house .del',function (e) {//删除教室
//     e.stopPropagation();
//     var that=$(this).parent().parent();
//     if(that.hasClass('init')){
//         that.removeClass('init').find('.name').text('选择教室');
//         $.each($('#main_table .jxb-zh-item'),function (i,item) {
//             $(item).removeClass('active');
//         })
//     }
// })



//新建组合
// $('#newZh').click(function () {
//     $.each($('#main_table thead tr'),function (i,item) {
//         var t='<td width="280">'+
//                 '<span class="name">等级-时段组合1</span>'+
//                 '<a class="modify col-edit"><i class="fa fa-pencil"></i></a>'+
//                 '<a class="clear col-clear"><i class="fa fa-trash"></i></a>'+
//                 '</td>';
//         $(item).append(t);
//     })
//
//     $.each($('#main_table tbody tr'),function (i,item) {
//         var t='<td width="280">'+
//         '<div class="jxb-zh-item jxb">'+
//         '<span class="name">选择教学班</span>'+
//         '<div class="gp">'+
//         '<a class="cs"><i class="fa fa-pencil"></i></a>'+
//         '<a class="del"><i class="fa fa-trash"></i></a>'+
//         '</div>'+
//         '</div>'+
//         '<div class="jxb-zh-item teacher">'+
//         '<span class="name">选择老师</span>'+
//         '<div class="gp">'+
//         '<a class="cs"><i class="fa fa-pencil"></i></a>'+
//         '<a class="del"><i class="fa fa-trash"></i></a>'+
//         '</div>'+
//         '</div>'+
//         '<div class="jxb-zh-item house">'+
//         '<span class="name">选择教室</span>'+
//         '<div class="gp">'+
//         '<a class="cs"><i class="fa fa-pencil"></i></a>'+
//         '<a class="del"><i class="fa fa-trash"></i></a>'+
//         '</div>'+
//         '</div>'+
//         '</td>';
//         $(item).append(t);
//     })
//
// })

//删除组合
// $('#delZh').click(function () {
//     $('.dialog-del').show()
// })



//滚动监听
var a=0,b=0;
$('.jxb-zh-group').scroll(function(){
    var sl=this.scrollLeft,
        st=this.scrollTop;

    if(sl!=a){//监听左右滚动
        a=sl;
        $('.left-table').css('z-index',3);
        $('.top-table').css('z-index',2);
        $('.top-table').css('left','-'+sl+'px');
    }
    if(st!=b){//监听上下滚动
        b=st;
        $('.left-table').css('z-index',2);
        $('.top-table').css('z-index',3);
        $('.left-table').css('top','-'+st+'px');
    }

});

