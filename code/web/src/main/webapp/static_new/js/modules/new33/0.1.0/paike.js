function allowDrop(ev){
    ev.preventDefault();
}

var srcdiv = null;
function drag(ev,divdom){
    getConflictedSettledJXB($(divdom).attr('jxbId'),$(divdom).attr('clsrmId'));
    srcdiv=divdom;
    ev.dataTransfer.setData("text/html",divdom.innerHTML);
}

function drop(ev,divdom){
    ev.preventDefault();
    if(srcdiv != divdom){
        saveKeBiaoInfo($(divdom).attr('ykbid'),$(srcdiv).attr('jxbid'),$(srcdiv).attr('ykbid'));
        srcdiv.innerHTML = divdom.innerHTML;
        divdom.innerHTML=ev.dataTransfer.getData("text/html");

    }
}

// function removed(divdom){
//     divdom.parentNode.removeChild(divdom)
// }

//可排教学班
function getkpjxb() {
    var par = {};
    var gradeId = "";
    $('.gaozhong label').each(function (i) {
        if ($(this).hasClass("active")) {
            gradeId = $(this).attr('ids');
        }
    })
    par.gradeId = gradeId;
    par.subjectId = $('#subjectId').val();
    par.type = $('#jxbType').val();
    par.termId = $("#defaultTerm").attr("ids");
    $.ajax({
        url: "/paike/getkpjxb.do",
        type: "get",
        dataType: "json",
        async: true,
        data: par,
        success: function(rep) {
            if (rep.code == '200') {
                $('.kpjxblist').html('');
                var str = "";
                if (rep.message!=null && rep.message.length!=0) {
                    for (var i=0;i<rep.message.length;i++) {
                        var row = rep.message[i];
                        if (row.ypksCount!=row.zksCount) {
                            str += '<li  draggable="true" ondrop="drop(event,this)" ondragover="allowDrop(event)" ondragstart="drag(event, this)" ' +
                                'jxbId="' + row.id + '" clsrmId="' + row.classroomId + '" ypksCnt="' + row.ypksCount + '" zksCnt="' + row.zksCount + '"><div class="ls"><p><em class="vxk">' + row.name + '</em>' +
                                '<em class="vrs">(' + row.studentCount + ')</em></p><p class="vjs" js="ls">' + row.teacherName + '</p><p class="vbq">'+row.remarks+'</p></div>' +
                                '<span>' + row.ypksCount + '/' + row.zksCount + '</span></li>';
                        }
                    }
                    $('.kpjxblist').html(str);
                }

            }
        },
        error: function() {
            console.log('saveKeBiaoInfo error');
        }
    });
}

//教学班放入格子里
function saveKeBiaoInfo(ykbId,jxbId,oykbId) {
    var data = {};
    data.ykbId = ykbId;
    data.jxbId = jxbId;
    data.oykbId = oykbId;
    $.ajax({
        url: "/paike/saveKeBiaoInfo.do",
        type: "get",
        dataType: "json",
        async: true,
        data: data,
        success: function(rep) {
            if (rep.code == '200') {
                layer.msg("放入成功！");
                // getkpjxb();
                // repair();
                seajs.use('pkTable', function (pkTable) {
                    pkTable.getkpjxb($('#subjectId').val());
                    pkTable.getKeBiaoList(2);
                });
            }
        },
        error: function() {
            console.log('saveKeBiaoInfo error');
        }
    });
}

function repair() {
    $('.ke .gray').each(function(i) {
        $(this).removeClass("gray");
        $(this).attr('draggable',true);
        $(this).attr('ondrop',"drop(event,this)");
        $(this).attr('ondragover',"allowDrop(event)");
        $(this).attr('ondragstart',"drag(event, this)");
    })
    $('.ke .red').each(function(i) {
        $(this).removeClass("red");
        $(this).attr('draggable',true);
        $(this).attr('ondrop',"drop(event,this)");
        $(this).attr('ondragover',"allowDrop(event)");
        $(this).attr('ondragstart',"drag(event, this)");
        $(this).text("");
    })
}

//冲突格子
function getConflictedSettledJXB(jxbId,classroomId) {
    var data = {};
    data.jxbId = jxbId;
    data.termId = $("#defaultTerm").attr("ids");
    $.ajax({
        url: "/paike/getConflictedSettledJXB.do",
        type: "get",
        dataType: "json",
        async: true,
        data: data,
        success: function(rep) {
            if (rep.code == '200') {
                var sws = rep.message.swcts;
                if (sws!=null && sws.length!=0) {
                    for (var i=0;i<sws.length;i++) {
                        var dt = "idx"+(sws[i].y-1)+(sws[i].x-1);
                        // $("."+dt).attr('draggable',false);
                        $("."+dt).removeAttr('draggable');
                        $("."+dt).removeAttr('ondrop');
                        $("."+dt).removeAttr('ondragover');
                        $("."+dt).removeAttr('ondragstart');
                        $("."+dt).addClass("red");
                        $("."+dt).text(sws[i].desc);
                    }
                }
                if (rep.message.jxbcts!=null &&rep.message.jxbcts.length!=0) {
                    for (var j=0;j<rep.message.jxbcts.length;j++) {
                        var dt = "idx"+(rep.message.jxbcts[j].x)+(rep.message.jxbcts[j].y);
                        $("."+rep.message.jxbcts[j].classroomId+" ."+dt).removeAttr('draggable');
                        $("."+rep.message.jxbcts[j].classroomId+" ."+dt).removeAttr('ondrop');
                        $("."+rep.message.jxbcts[j].classroomId+" ."+dt).removeAttr('ondragover');
                        $("."+rep.message.jxbcts[j].classroomId+" ."+dt).removeAttr('ondragstart');
                        if (!$("."+rep.message.jxbcts[j].classroomId+" ."+dt).hasClass('red')) {
                            $("."+rep.message.jxbcts[j].classroomId+" ."+dt).addClass("gray");
                        }
                    }
                }

            }
        },
        error: function() {
            console.log('getConflictedSettledJXB error');
        }
    });
}

