/**
 * Created by qiangm on 2015/8/20.
 */
'use strict';
define(['jquery','doT','easing','common','zTreeCore','zTreeExcheck','zTreeExedit',"layer"],function (require, exports, module) {
    /**
     *初始化参数
     */
    var zTreeObj;
    var homework = {},
        Common = require('common');
    require('zTreeCore');
    require('zTreeExcheck');
    require('zTreeExedit');
    require('layer');
    var homeworkData = {};

    var setting = {
        callback: {
            onCheck: zTreeOnClick
        },
        check:{
            enable:true,
            chkStyle:"checkbox"
        }
    };
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * notice.init()
     */
    homework.init = function () {
        homework.initTree();
        $('.t-II').addClass('homework-li');
        $('.t-I').removeClass('homework-li');
        $('.saveUser').click(function () {
            homework.selectStud();
        })
        $('.arrange-btn').click(function(){
            $('.bg').show();
            $('.arrange-popup').show();
        })
        $('.popup-qx,.popup-top i').click(function(){
            $('.bg').hide();
            $('.arrange-popup').hide();
        })
    };
    function zTreeOnClick(event, treeId, treeNode) {
        var nodes=zTreeObj.getCheckedNodes(true);
        var ids="";

        for(var i=0;i<nodes.length;i++){
            if(nodes[i].id)
            {
                ids+=nodes[i].id + ",";
            }
        }
        $("#classes").val(ids);
        homework.getStudent(ids);
    };
    homework.selectByPinyin = function()
    {
        $("#myList-nav").html("");
        $('#myList').listnav({
            includeOther: true,
            noMatchText: '',
            prefixes: ['the', 'a']
        });
    }
    homework.initTree = function() {
        var zNodes=[];
        $.ajax({
            url: "/exam2/gradeclass.do",
            type: "post",
            dataType: "json",
            success: function (data) {

                for(var i=0;i<data.message.length;i++)
                {
                    var parentNode={};
                    var obj=data.message[i];
                    parentNode.id=obj.parent.id;
                    parentNode.name=obj.parent.name;

                    var children=[];
                    for(var j=0;j<obj.children.length;j++)
                    {
                        var childObj=obj.children[j];
                        var childNode={};
                        childNode.id=childObj.id;
                        childNode.name=childObj.name;
                        children.push(childNode);

                        if(j==0)
                        {
                            // getStudent(childObj.id);
                        }
                    }
                    parentNode.children=children;
                    zNodes.push(parentNode);
                }
                zTreeObj= $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            }
        });
    }
    homework.selectStud = function()
    {
        var studs="";
        var names="";
        var i=0;
        $("#myList").find(".selected").each(function(){
            if($(this).hasClass("selected"))
            {
                i++;
                studs+=$(this).attr("id")+",";
                if(i<5)
                {
                    names+=$("#span_"+jQuery(this).attr("id")).text()+",";
                }
            }
        });
        names+="等"+i+"人";
        $("#users").val(studs);
        $("#stuendsNames").val(names);
        $(".arrange-popup,.bg").hide();
    }
    
    homework.getStudent = function(cid) {
        $.ajax({
            url: "/class/students.do?classIds="+cid,
            type: "post",
            dataType: "json",
            success: function (data) {
                var html="";
                for(var i=0;i<data.length;i++)
                {
                    var obj=data[i];
                    html+='<li id="'+obj.id+'" class="selected">';
                    html+='<img src="'+obj.value1+'">';
                    html+='<span id="span_'+obj.id+'" class="female">'+obj.value+'</span>';
                    html+='</li>';
                }

                $("#myList").html(html);
                $('.member-list li').click(function(){
                    $(this).toggleClass('selected');
                })
                homework.selectByPinyin();
            }
        });
    }
    /*上传附件开始*/
    var files = [];
    /*上传附件结束*/

    $(document).ready(function () {
        //上传附件
        require.async("widget",function(widget) {
            require.async("fileupload", function (fileupload) {
                $('#file_attach').fileupload({
                    url: '/commonupload/upload.do?type=1',
                    start: function (e) {
                        $('#fileuploadLoading').show();
                    },
                    done: function (e, data) {
                        var info = data.result.message[0];
                        files.push({name: info.fileName, "path": info.path});

                        $("#fileListShow").empty();
                        Common.render({
                            tmpl: $('#fileListJs'),
                            data: {data: files},
                            context: '#fileListShow'
                        });

                        $('#fileListShow').find('a').each(function(index, item){
                            var href = $(item).attr('href');
                            var fileKey = href.substring(href.lastIndexOf('/') + 1);
                            var fileName = $(this).attr("fn");
                            var fileIndex = href.substring(href.lastIndexOf('.')+1);
                            if (fileIndex=='m3u8') {
                                href = '/commonupload/m3u8ToMp4DownLoad.do?filePath='+href+'&fileName='+fileName;
                            } else {
                                href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName;
                            }
                            $(item).attr('href', href)
                        });
                        $('.remove').click(function() {
                            var path = $(this).attr("did");
                            for (var i = 0; i < files.length; i++) {
                                if (files[i].path == path) {
                                    files.splice(i, 1);
                                    break;
                                }
                            }
                            $(this).parent().remove();

                        });
                    },
                    fail: function (e, data) {
                    },
                    always: function (e, data) {
                        $('#fileuploadLoading').hide();
                    }
                });
            });
        });
    });


    homework.init();
});