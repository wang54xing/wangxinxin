var currentUserId;
var blogType = 1;
function submitBlog() {
    var flist = '';
    var i = 0;
    var arrayObj = [];
    if ($(".blog-img").length <= 9) {
        $(".blog-img").each(function () {
            flist += $(this).attr('src') + ',';
            arrayObj[i] = $(this).attr('src');
            i++;
            $(this).parent().remove();
        });
    } else {
        alert('上传图片不可超过九张！');
        return;
    }

    var comment_content = $.trim($("#blog_content").val());
    var comment_placehd = $("#blog_content").attr('placeholder');
    if (comment_content == '' && comment_placehd != '' && comment_placehd != '说点什么吧') {
        comment_content = comment_placehd;
        $("#blog_content").val(comment_placehd)
    }
    var arr = document.getElementById("theme");
    var themetype;
    if (arr.checked) {
        themetype = 1;
    } else {
        themetype = 0;
    }
    var content = $("#blog_content").val().replace(/\n/g, '<br>');
    if (comment_content == '') {
        alert("请输入评论内容。");
        return;
    }
    if (content.getLength() > 1000) {
        alert('文字最多发1000个字符！');
        return;
    }

    $('#blogsubmitloading').show();

    $.ajax({
        url: "/homeschool/publicBlog.do",
        type: "post",
        dataType: "json",
        async: true,
        traditional:true,
        data: {
            'blogcontent': content,
            'filenameAry':arrayObj,
            'top': themetype,
            'blogtype': blogType
        },
        success: function (data) {
            $("#blog_content").val('');
            $('#theme').attr('checked', false);
            ret = data;
            $('.order-blue:nth(0)').click();
            if (data.score) {
                scoreManager(data.scoreMsg, data.score);
            }
        },
        complete: function () {
            $('#blogsubmitloading').hide();
            $('#blog_content').attr('placeholder', '说点什么吧');
        }
    });
    $('#blog_content').attr('rows', '2');
}

/*  查询blog 最新1 最热2  ***************/

function getFriendBlogInfo(t, page, size) {
    $('#blog-list').html('');
    $("#blog-list").addClass('hardloadingb');
    //var order = $("#order").val();
    $.ajax({
        url: "/homeschool/selFriendBlogInfo.do",
        type: "post",
        dataType: "json",
        async: true,
        data: {
            hottype: t,
            //seachtype: order,
            page: page,
            pageSize: size,
            blogtype: blogType,
            seachtype: 1

        },
        success: function (data) {
            ret = data;
            showFriendBlogInfo(data);
            var total = data.rows.length > 0 ? data.total : 0;
            var to = Math.ceil(total / size);
            totalPages = to == 0 ? 1 : to;
            if (page == 1) {
                resetPaginatorBlog(totalPages);
            }
            if (data.rows.length > 0) {
                $('#example').show();
            } else {
                $('#example').hide();
            }
        },
        complete: function () {
            $("#blog-list").removeClass('hardloadingb');
            $('.contentImg-container').each(function () {
                //                ypxxutility.homepage.picswitch(this, {
                //                    width: window.screen.availWidth * 0.7,
                //                    height: window.screen.availHeight * 0.7
                //                });
                ypxxutility.homepage.picswitch(this, {
                    width: 600,
                    height: 400
                });
            });
            setNotifyNum();

            $('.comment-btn').on('click', function () {
                if ($(this).hasClass('active')) {
                    $(this).removeClass('active');
                    $(this).parents('.info-detail').find('.content-all').slideUp(400);
                } else {
                    $(this).addClass('active');
                    if ($(this).parents('.info-detail').find('.content-all').length == 0) {
                        var uid = $(this).parents('.info-detail').attr('userid');
                        $(this).parents('.info-detail').append('<div class="content-all" userid="' + uid + '"></div>');
                    }
                    getFriendReplyInfo($(this).attr('blogid'), 2, $(this).parents('.info-detail').find('.content-all'));
                    $(this).parents('.info-detail').find('.content-all').slideDown(400);
                }
            });

            $('.blog-delete-btn').on('click', function () {
                var blogId = $(this).attr('blogid');
                if (confirm("确定要删除该条微博吗？")) {
                    deleteBlog(blogId, $(this));
                }
            });
        }
    });
}

function showFriendBlogInfo(data) {
    var target = $("#blog-list");
    var html = '';
    var userId = $('#currentId').val();
    for (var i in data.rows) {
        var blog = data.rows[i];
        var zanimg = blog.iszan == 0 ? '/img/star.png' : '/img/zaned.png';
        //var position;
        //if (blog.role == 0) {
        //    position = '学生';
        //} else if (blog.role == 1) {
        //    position = '老师';
        //} else if (blog.role == 2 || blog.role == 9) {
        //    position = '校领导';
        //} else if (blog.role==4) {
        //	position = '家长';
        //}
        var theme = '';
        if (blog.top == 1) {
            theme = '<span class="info-role color-red">主题帖</span>'
        }
        html += '<div class="info-container">';

        html += '<img class="info-user-img" src="' + blog.userimage + '" role="' + blog.role + '" target-id="' + blog.userid + '">'
        html += '<div class="info-detail" userid="' + blog.userid + '"><div class="info-user-name">' + blog.username + '<span class="info-role">' + blog.roleDescription + '</span>' + theme;
        var content = blog.blogcontent != null ? blog.blogcontent : '';
        
        html += '</div><div>' + '<div class="info-content ' + (content.length > 100 ? 'cracked' : '') + '">' + ( content.length > 100 ? content.substring(0, 105) + '...<a class="zhankai" onclick="showHideContent(this,\'' + content + '\')">展开</a>' : content) + '</div><div class="contentImg-container">';
        if (blog.filenameAry!=null) {
            for (var j = 0; j < blog.filenameAry.length; j++) {
                var imgsrc = blog.filenameAry[j];
                html += '<img class="content-img" title="点击查看大图" src="' + imgsrc + '">';
            }
        }
        var client = blog.clienttype == null ? 'PC' : blog.clienttype;
        var update = '';
        if (blog.updatetime && blog.updatetime != blog.createtime) {
            update = '<span style="margin-left:12px;">最新评论于' + getAdaptTimeFromNow(blog.updatetime) + '</span>';
        }
        html += '</div><div class="info-controllBar"><div class="fleft" style="width:90px">&nbsp;' + getAdaptTimeFromNow(blog.createtime) + '</div><div class="fleft">来自' + client + '</div>' + update;
        html += '<div class="fright comment-btn" blogid="' + blog.id + '"><span>评论</span><span>（' + blog.mreply + '）</span></div><div class="fright" style="margin:0px 7px 0px 3px;">|</div><div class="fright like-status ' + (blog.iszan == 0 ? '' : 'active') + '" onclick="zanBlog(\'' + blog.id + '\',\'\',this)"><i class="fa fa-thumbs-o-up"></i><span>（' + blog.zancount + '）</span></div>';
        if ((userId == blog.userid) || (blog.isdelete == 1)) {
            html += '<img class="blog-delete-btn" blogid="' + blog.id + '" src="/img/dustbin.png" title="删除此贴">';
        }
        html += '</div></div></div></div>';
    }
    target.html(html);
}
function replierReply(obj){
	var p = $(obj).parents('.replier-container');
    p.find('.reply-replier-all').slideDown(400);
}
function deleteBlog(blogId, obj) {
    $.ajax({
        url: '/homeschool/delteMicroBlog.do',
        type: 'post',
        dataType: 'json',
        data: {
            id: blogId
        },
        success: function (data) {
            if (data) {
                obj.parents('.info-container').remove();
            } else {
                alert('删除失败');
            }
        },
        error: function () {
            console.log('deleteMicroBlogInfo error');
        }
    });
}
/*  获取一条blog的评论 *************************/
function getOneBlogInfo(id) {
    $('.mycomment-container').hide();
    $('.one_blog_container').show();
    $.ajax({
        url: "/homeschool/selOneBlogInfo.do",
        type: "post",
        dataType: "json",
        async: true,
        data: {
            'blogid': id
        },
        success: function (data) {
            ret = data;
            showOneBlog(data);
            $('#example').hide();
        },
        complete: function () {
            $('.contentImg-container').each(function () {
                // ypxxutility.homepage.picswitch(this, {
                //     width: window.screen.availWidth * 0.7,
                //     height: window.screen.availHeight * 0.7
                // });
                ypxxutility.homepage.picswitch(this, {
                    width: 600,
                    height: 400
                });
            });

            $('.comment-btn').each(function () {
                if ($(this).parents('.info-detail').find('.content-all').length == 0) {
                    var uid = $(this).parents('.info-detail').attr('userid');
                    $(this).parents('.info-detail').append('<div class="content-all" userid="' + uid + '"></div>');
                    getFriendReplyInfo($(this).attr('blogid'), 2, $(this).parents('.info-detail').find('.content-all'));
                }
                $(this).bind('click', function () {
                    if ($(this).hasClass('active')) {
                        $(this).removeClass('active');
                        $(this).parents('.info-detail').find('.content-all').slideUp(400);
                    } else {
                        $(this).addClass('active');
                        getFriendReplyInfo($(this).attr('blogid'), 2, $(this).parents('.info-detail').find('.content-all'));
                        $(this).parents('.info-detail').find('.content-all').slideDown(400);
                    }
                });

            });
        }
    });
}
function showHideContent(button, content) {
    var $button = $(button),
        textDiv = $button.closest(".info-content");
    $button.detach();
    if (textDiv.is(".cracked")) {
        textDiv.removeClass("cracked").text(content);
        $button.text("收起").appendTo(textDiv);
    }
    else {
        textDiv.addClass("cracked").text(content.substring(0, 105) + '...');
        $button.text("展开").appendTo(textDiv);
    }
};


function showOneBlog(data) {
    var target = $(".one_blog");
    var html = '';

    var blog = data.data;
    html += '<div class="info-container"><img class="info-user-img" src="' + blog.userimage + '" target-id="' + blog.userid + '" role="' + blog.role + '">';
    html += '<div class="info-detail" userid="' + blog.userid + '"><div class="info-user-name">' + blog.username;
    html += '</div><div>' + '<span id="contentText" style="display:inline;" class=' + (blog.blogcontent.length > 100 ? 'info-content_hidden' : 'info-content') + '>' + ( blog.blogcontent.length > 100 ? blog.blogcontent.substring(0, 105) : blog.blogcontent) + '</span>' + (blog.blogcontent.length > 100 ? '<span class="shenglue" style="display: inline;">...</span><button class="zhankai" type="button" onclick="showHideContent(this,\'' + blog.blogcontent + '\')">展开</button>' : '') + '</div><div class="contentImg-container">';
    if (blog.filenameAry!=null) {
        for (var j = 0; j < blog.filenameAry.length; j++) {
            var imgsrc = blog.filenameAry[j];
            html += '<img class="content-img" title="点击查看大图" src="' + imgsrc + '">';
        }
    }
    var zanimg = blog.iszan == 0 ? '/img/star.png' : '/img/zaned.png';
    var canZan = blog.iszan == 0 ? 'onclick="zanBlog(\'' + blog.id + '\',\'\',this)"' : '';
    var client = blog.clienttype == null ? 'PC' : blog.clienttype;
    html += '</div><div class="info-controllBar"><div class="fleft" style="width:100%;">&nbsp;&nbsp;' + getAdaptTimeFromNow(blog.createtime) + '<div class="fleft">来自' + client + '</div>';
    html += '<div class="fright comment-btn" blogid="' + blog.id + '"><span>评论</span><span>（' + blog.mreply + '）</span></div><div class="fright" style="margin:0px 7px 0px 3px;">|</div><div class="fright like-status ' + (blog.iszan == 0 ? '' : 'active') + '" ' + canZan + '><i class="fa fa-thumbs-o-up"></i><span>（' + blog.zancount + '）</span></div>';
    html += ' </div></div></div></div>';

    target.html(html);
}


/* 获取blog的评论   1 我的评论 2 单条评论   **********************/
function getFriendReplyInfo(id, type, blogDom, page, size) {
    page = typeof page == 'undefined' ? 0 : page;
    size = typeof size == 'undefined' ? 0 : size;
    $.ajax({
        url: "/homeschool/friendReplyInfo.do",
        type: "post",
        dataType: "json",
        async: true,
        data: {
            blogid: id,
            type: type,
            page: page,
            pageSize: size
        },
        success: function (data) {
            ret = data;
            if (type == 1) {
                showFriendReplyInfoOfMy(data);
                var total = data.rows.length > 0 ? data.total : 0;
                var to = Math.ceil(total / size);
                totalPages = to == 0 ? 1 : to;
                if (page == 1) {
                    resetPaginatorReplyMy(totalPages);
                }
                if (data.rows.length > 0) {
                    $('#example').show();
                } else {
                    $('#example').hide();
                }
            } else if (type == 2) {
                showFriendReplyInfoOfBlog(data, blogDom, id);
            } else {

            }

        },
        complete: function () {
            setNotifyNum();
            if (type == 2) {
            	$(blogDom).slideDown(400);
            } else if (type == 1) {
                top.location = '#mycomments';
                $('.mycomment-reply').bind('click', function () {
                    $(this).closest('div').next().slideDown(400);
                });

                /*$('.mycomment-reply-btn').bind('click',function(){
                 replyBlog($(this).attr('blogid'),1,$(this).prev(),'','','');
                 });*/

                $('.mycomment-delete').bind('click', function () {
                    if (confirm('确认删除此条评论！')) {
                        var target = $(this).parents('.mycomment-reply-form');
                        var cid = $(this).attr('commentid');
                        var bid = $(this).attr('blogid');
                        $.ajax({
                            url: "/homeschool/delteMicroBlog.do",
                            type: "post",
                            dataType: "json",
                            async: true,
                            data: {
                                'id': cid,
                                'blogid':bid
                            },
                            success: function (data) {
                                ret = data;
                                target.remove();
                            },
                            complete: function () {
                            }
                        });
                    }

                });
            } else {

            }
        }
    });
}

function deleteBlogReplyByCommentIdAndBlogId(node, commentId, blogId) {

    if (confirm('确认删除此条评论！')) {
        $.ajax({
            url: "/homeschool/delteMicroBlog.do",
            type: "post",
            dataType: "json",
            async: true,
            data: {
                id: commentId,
                blogid:blogId
            },
            success: function (data) {
                if (data.result) {
                    alert("删除评论成功！");
                    var remove = $(node).parent().parent().parent();
                    var span = remove.parent().parent().prev().find(".comment-btn").find("span").last();
                    var str = $(span).html();
                    var number = str.substring(1, str.length - 1);
                    $(span).html("(" + (number - 1) + ")");
                    remove.remove();
                    return;
                } else {
                    alert("删除评论失败！");
                }

            },
            complete: function () {
            }
        });
    }
}

function showFriendReplyInfoOfBlog(data, blogDom, blogId) {
    var nodeUserId =$(blogDom).attr('userid');
    if(currentUserId==null || currentUserId==undefined){
        $.ajax({
            url: "/homeschool/currentUser.do",
            type: "post",
            dataType: "json",
            async: false,
            data: {
            },
            success: function(data) {
                currentUserId=data.id;
            },
            complete: function() {}
        });
    }


    var target = typeof blogDom == 'undefined' ? '' : $(blogDom);
    if (target.find('.content-container').length == 0) {
        var containerHtml = "";
        containerHtml += '<div class="content-container-border1"></div><div class="content-container-border2"></div>';
        containerHtml += '<div class="content-container"><textarea type="text" class="content-reply"></textarea><button class="reply-btn reply-blog" userid="' + target.attr('userid') + '" onclick="replyBlog(\''+ blogId +'\', 1, this, \''+ target.attr('userid') +'\',\'\')">评论</button></div>';
        target.html(containerHtml);
    }
    target.find('.card-more').remove();
    if (data.page * data.pageSize < data.total) {
        /**
         * 显示更多
         */
        target.append('<a onclick="getFriendReplyInfo(' + blogId + ', 2, $(this).closest(\'.content-all\'),' + (data.page + 1) + ',' + data.pageSize + ' )" class="card-more"><span class="more-txt" style="margin-left: 150px;">后面还有' + (data.total - data.page * data.pageSize) + '条评论，点击查看>></span></a>');
    }
    var replyContainer = target.find('.content-container');
    var html = '';
    for (var i = 0; i <= data.rows.length - 1; i++) {
        var rp = data.rows[i];
        var bid = target.prev().find('.comment-btn').attr('blogid');
        var zanimg = rp.iszan == 0 ? '/img/zan_new.png' : '/img/zaned_new.png';


        var canZan = rp.iszan == 0 ? 'onclick="zanBlog(\'' + bid + '\',' + rp.id + ',this)"' : '';
        var huifu = rp.replytype ==3 ? ':@' + rp.busername : '';
        html += '<div class="replier-container"><img class="replier-img" src="' + rp.userimage + '" target-id="'+rp.userid+'" role="'+rp.role+'">';
        html += '<div><a class="replier-name" href="javascript:;" >' + rp.username + huifu + '</a><span>:</span>';
        html += '<span class="reply-word">' + rp.blogcontent + '</span><span>(' + getAdaptTimeFromNow(rp.createtime) + ')</span>';
        html += '<div><div class="fright reply-text"><span class="replier-reply" onclick="replierReply(this)">回复</span></div>';
        html += '<div class="fright" style="margin:0px 7px 0px 7px;color:#d0d0d0;">|</div>';


        //添加删除评论的 按钮 <img class="blog-delete-btn" blogid="485" src="/img/dustbin.png" title="删除此贴">
        if(rp.userid==currentUserId || nodeUserId==currentUserId){
//            html+='<div class="fright"><img class="blog-delete-btn" onclick="deleteBlogReplyByCommentIdAndBlogId(this,'+rp.id+','+blogId+')" src="/img/dustbin.png" title="删除此评论"></div>'
            html += '<div class="fright like-status ' + (rp.iszan == 0 ? '' : 'active') + '"' + canZan + '><i class="fa fa-thumbs-o-up"></i><span>（' + rp.zancount + '）</span></div>' +
                '<img class="blog-delete-btn" onclick="deleteBlogReplyByCommentIdAndBlogId(this,\''+rp.id+'\',\''+blogId+'\')" src="/img/dustbin.png" title="删除此评论"></div>';
        }else {
            html += '<div class="fright like-status ' + (rp.iszan == 0 ? '' : 'active') + '"' + canZan + '><i class="fa fa-thumbs-o-up"></i><span>（' + rp.zancount + '）</span></div></div>';
        }

        html += '<div class="reply-replier-all"><div class="reply-replier-border1"></div><div class="reply-replier-border2"></div>';
        html += '<div class="reply-replier-container"><textarea type="text" class="reply-replier-input" placeholder="回复@~' + rp.username + '~:"></textarea>';

        html += '<button class="reply-btn reply-reply" userid="' + rp.userid + '" commentid="' + rp.id + '" onclick="replyBlog(\''+ blogId +'\', 3, this, \'' + rp.userid + '\', \'' + rp.id + '\')">评论</button></div></div></div></div>';
    }
    if (data.page == 1) {
        replyContainer.find('.replier-container').remove();
    }
    replyContainer.append(html);
}

function showFriendReplyInfoOfMy(data) {
    var target = $('.mycomment-main-container');
    var html = '';
    for (var i in data.rows) {
        var mrp = data.rows[i];
        var rAttach = '';
        var prefix = '评论我的微博 ';
        if (mrp.commentid > 0) {
            rAttach = ':回复@' + mrp.bnickname;
            prefix = '回复我的评论 ';
        }
        html += '<div class="mycomment-reply-form"><img class="mycomment-user-img" src="' + mrp.userimage + '" target-id="' + mrp.userid + '" role="' + mrp.role + '">';
        html += '<div class="mycomment-content-border1"></div><div class="mycomment-content-border2"></div><div class="mycomment-content">';
        html += '<a class="replier-name" href="javascript:;">' + mrp.username + rAttach + '</a><span> : </span><span>' + mrp.blogcontent + '</span>';
        html += '<p>' + prefix + '<a class="weibo-link" href="javascript:getOneBlogInfo(\'' + mrp.blogid + '\');">"' + (mrp.commentid == 0 ? (mrp.blogcontent.length == 0 ? '分享图片' : mrp.blogcontent) : mrp.bcommentcontent) + '"</a></p><p class="release-time">' + mrp.blogtime + '</p>';
        html += '<div class="fright"><a class="mycomment-delete" commentid="' + mrp.id + '" blogid="' + mrp.replyid + '">删除</a><a class="mycomment-reply">回复</a></div>';
        html += '<div class="mycomment-reply-container"><input type="text" class="mycomment-reply-input" placeholder="回复@' + mrp.username + ':"><button class="mycomment-reply-btn" onclick="replyBlog(\'' + mrp.replyid + '\',3,this' + ',\'' + mrp.userid + '\',\'' + mrp.id + '\',' + null + ')">评论</button></div>';
        html += '</div></div></div>';
    }
    target.html(html);
}

/*  回复blog  commenttype:1(回复blog) :2(回复评论)  ************/
function replyBlog(id, type, replyDom, userid, commentid, blogDom) {
	if(!blogDom){
		blogDom = $(replyDom).closest('.content-all');
	}
    if ($(replyDom).prev().val().length == 0 || $.trim($(replyDom).prev().val()) == '') {
        alert("评论不可为空！");
        return;
    }
    var postData = {
        id: id,
        replytype: type,
        blogcontent: $(replyDom).prev().val(),
        userid: userid,
        replyid: commentid
    };
    //if (type == 2) {
    //    postData.commentid = commentid;
    //}

    $.ajax({
        url: "/homeschool/replyComment.do",
        type: "post",
        dataType: "json",
        async: true,
        data: postData,
        success: function (data) {
            ret = data;

            if (blogDom && blogDom.length > 0) {
                $(replyDom).parents('.content-all').prev().find('.comment-btn span:nth(1)').text('（' + data.replyCount + '）');
                getFriendReplyInfo(id, 2, blogDom);
                $(replyDom).siblings('.content-reply').val('');
            } else {
                /*var p=$(replyDom).parents('.reply-replier-all');
                 if(p)
                 {
                 p.slideUp(400)
                 }
                 $(replyDom).closest('div').slideUp(400);*/
                $(replyDom).closest('div').slideUp(400);
            }
            if (data.score) {
                scoreManager(data.scoreMsg, data.score);
            }
        },
        complete: function () {
        }
    });
}


/******  赞  **************************/

function zanBlog(bid, cid, dom) {
    cid = typeof cid == 'undefined' ? '' : cid;
    if (cid!='') {
        bid = cid;
    }
    $.ajax({
        url: "/homeschool/isBlogZan.do",
        type: "post",
        dataType: "json",
        async: true,
        data: {
            blogid: bid
        },
        success: function (data) {
            if (data.flag) {
                $(dom).addClass("active");
                $(dom).find('span').text('（' + data.zanCount + '）');
                if (data.score) {
                    scoreManager(data.scoreMsg, data.score);
                }
            }
        },
        complete: function () {
        }
    });
}

function resetPaginatorBlog(totalPages) {
    //$('#example').bootstrapPaginator("setOptions", {
    //    currentPage: 1,
    //    totalPages: totalPages,
    //    itemTexts: function (type, page, current) {
    //        switch (type) {
    //            case "first":
    //                return "首页";
    //            case "prev":
    //                return "<";
    //            case "next":
    //                return ">";
    //            case "last":
    //                return "末页" + page;
    //            case "page":
    //                return  page;
    //        }
    //    },
    //    onPageClicked: function (e, originalEvent, type, page) {
    //        currentPage = page;
    //        getFriendBlogInfo($('.order-bar .active').attr('index'), page, 12);
    //    }
    //});
}

function resetPaginatorReplyMy(totalPages) {
    $('#example').bootstrapPaginator("setOptions", {
        currentPage: 1,
        totalPages: totalPages,
        itemTexts: function (type, page, current) {
            switch (type) {
                case "first":
                    return "首页";
                case "prev":
                    return "<";
                case "next":
                    return ">";
                case "last":
                    return "末页" + page;
                case "page":
                    return  page;
            }
        },
        onPageClicked: function (e, originalEvent, type, page) {
            currentPage = page;
            getFriendReplyInfo('', 1, '', page, 10);
        }
    });
}


/*  未读个数重设  *****************************/

function setNotifyNum() {
    $.ajax({
        url: "/homeschool/getNoticeCount.do",
        type: "post",
        dataType: "json",
        async: true,
        data: {

        },
        success: function (data) {
            ret = data;
            $(".my_friend >*").hide();
            if (data.blogType == 1) {
                if ($(".my_friend span.notity").length == 0) {
                    $('.my_friend').append('<span id="notify_myfriend" class="notity">' + data.blogCount + '</span>')
                } else {
                    $(".my_friend span.notity").text(data.blogCount).show();
                }
            } else if (data.blogType == 2) {
                if ($(".my_friend span.sn-radio").length == 0) {
                    $('.my_friend').append('<b class="sn-radio"></b>')
                } else {
                    $(".my_friend b.sn-radio").show();
                }
            } else {
                //$(".my_friend span").hide();
            }
            $('.my_friend').each(function () {
                if ($(this).hasClass('chosen')) {
                    $(this).find('span').removeClass('notity_b');
                } else {
                    $(this).find('span').addClass('notity_b');
                }
            });
        },
        complete: function () {
        }
    });
}

/*  未读个数  *****************************/

function getNotifyNum(id, classtype) {
    $.ajax({
        url: "/reverse/selMesgNotViewCount.action",
        type: "post",
        dataType: "json",
        async: true,
        data: {
            'classId': id,
            'classtype': classtype
        },
        success: function (data) {
            ret = data;
            if (data.noticeNum && data.noticeNum > 0) {
                if ($(".my_notice span").length == 0) {
                    $(".my_notice").append('<span id="notify_notice" class="notity">' + data.noticeNum + '</span>');
                } else {
                    $(".my_notice span").text(data.noticeNum).show();
                }
            } else {
                $(".my_notice span").hide();
            }

            if (data.taskNum && data.taskNum > 0) {
                if ($(".my_homework span").length == 0) {
                    $(".my_homework").append('<span id="notify_homework" class="notity">' + data.taskNum + '</span>');
                } else {
                    $(".my_homework span").text(data.taskNum).show();
                }
            } else {
                $(".my_homework span").hide();
            }

            $(".my_friend >*").hide();
            if (data.blogType == 1) {
                if ($(".my_friend span.notity").length == 0) {
                    $('.my_friend').append('<span id="notify_myfriend" class="notity">' + data.blogCount + '</span>')
                } else {
                    $(".my_friend span.notity").text(data.blogCount).show();
                }
            } else if (data.blogType == 2) {
                if ($(".my_friend span.sn-radio").length == 0) {
                    $('.my_friend').append('<b class="sn-radio"></b>')
                } else {
                    $(".my_friend b.sn-radio").show();
                }
            } else {
                //$(".my_friend span").hide();
            }
            $('.tab_button').each(function () {
                if ($(this).hasClass('chosen')) {
                    $(this).find('span').removeClass('notity_b');
                } else {
                    $(this).find('span').addClass('notity_b');
                }
            });
        },
        complete: function () {
        }
    });
}

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

$(function () {
    if (getQueryString('index')==6) {
        blogType = 1;
    } else if (getQueryString('index')==7) {
        blogType = 2;
    }
    /* 上传blog图片 */
/*
    $('#image-upload').uploadify({
        'formData': {'type': 'blog'},
        'buttonClass': 'image-upload',
        'swf': "/plugins/uploadify/uploadify.swf",
        'uploader': '/upload.do',
        'method': 'post',
        'buttonText': '上传图片',
        'fileTypeDesc': '图像文件',
        'fileSizeLimit': '5MB',
        'fileTypeExts': '*.png; *.jpg; *.gif; *.bmp',
        'multi': true,
        'onUploadSuccess': function (file, response, result) {
            try {
                var json = $.parseJSON(response);
                if (json.result) {
                    $('#img-container ul').append('<li><img class="blog-img" src="' + json.path + '"><i class="fa fa-times blog-img-delete"></i></li>');
                    $('#blog_content').attr('placeholder','分享图片');
                }
            }catch (err) {
            }
        },
        'onUploadStart': function() {
            $('#blogpicuploadLoading').show();
        },
        'onUploadComplete': function() {
            $('#blogpicuploadLoading').hide();
        },
        'onUploadError': function (file, errorCode, errorMsg, errorString) {
        }
    });
*/

    //$('#file_attach').fileupload({
    //    url: '/reverse/addAttachment.action',
    //    start: function(e) {
    //        $('#fileuploadLoading').show();
    //    },
    //    done: function(e, data) {
    //        if (data.dataType == 'iframe ') {
    //            var response = $( 'pre', data.result ).text();
    //        } else {
    //            var response = data.result;
    //        }
    //        try {
    //            var ob = $.parseJSON(response);
    //            if (ob.uploadType == 0) {
    //                alert("附件上传失败！");
    //            }
    //            var uf = $('<p style="padding:5px 0;cursor:pointer;" class="uploadedFiles" realname="' + ob.realname + '">' + data.files[0].name + '</p>').hover(function () {
    //                if ($(this).find('img').length == 0) {
    //                    $(this).append("<a style=\"margin-left:20px;\" onclick=\"$(this).closest('p').remove();\"><img src=\"/img/dustbin.png\" /></a>");
    //
    //                } else {
    //                    $(this).find('img').show();
    //                }
    //            }, function () {
    //                $(this).find('img').hide();
    //            });
    //            $("#attachment-container").append(uf);
    //        } catch (err) {
    //        }
    //    },
    //    fail: function (e, data) {
    //
    //    },
    //    always: function (e, data) {
    //        $('#fileuploadLoading').hide();
    //    }
    //});

    /*  上传附件   ****************************************
    $("#faf input").bind('change', function() {
        if ($(this).val().length > 0) {
            $('#fileuploadLoading').show();
            fileUpload($('#faf')[0], function(content) {
                if (content) {
                    var ob = $.parseJSON($(content).text());
                    if (ob.uploadType == 0) {
                        alert("附件上传失败！");
                    }
                    // $('#file_attach').attr('realname',ob.realname);
                    var uf = $('<p style="margin-left:50px;padding:5px 0;cursor:pointer;" class="uploadedFiles" realname="' + ob.realname + '">' + $('#file_attach').val().match(/[^\\]*$/)[0] + '</p>').hover(function() {
                        if ($(this).find('img').length == 0) {
                            $(this).append("<a  style=\"margin-left:20px;\" onclick=\"$(this).closest('p').remove();\"><img src=\"/img/dustbin.png\" /></a>");

                        } else {
                            $(this).find('img').show();
                        }
                    }, function() {
                        $(this).find('img').hide();
                    });
                    $(".label-upload").append(uf);
                }
                $('#fileuploadLoading').hide();
            });
        }
    });
    $('.upload-img').bind('change', function () {
        var fileList = $(this)[0].files;
        if (fileList.length <= 9) {
            for (var i = 0; i < fileList.length; i++) {
                var f = fileList[i].name;
                var n = f.split('.');
                var rule = /png|jpg|gif|bmp|PNG|JPG|GIF|BMP/;
                if (rule.test(n[n.length - 1])) {
                    if (f.length > 0) {
                        $('#blogpicuploadLoading').show();
                        var flag = true;//为了解决ie浏览器调用执行两次fileUpload里面的方法
                        fileUpload($('#blog_pic')[0], function (content) {
                            if (flag) {
                                if (content) {
                                    var ob = $.parseJSON($(content).text());
                                    if (ob.uploadType == 0) {
                                        alert("图片上传失败！");
                                    } else {
                                        for (var j = 0; j < ob.realname.length; j++) {
                                            $('#img-container ul').append('<li><img class="blog-img" src="' + ob.realname[j] + '"><i class="fa fa-times blog-img-delete"></i></li>');
                                            $('#blog_content').attr('placeholder', '分享图片');
                                        }
                                    }
                                }
                                flag = false;
                            }
                            $('#blogpicuploadLoading').hide();
                        });
                    }
                } else {
                    alert('必须上传图片文件！');
                }
            }
        } else {
            alert('上传图片不能超过9张哦');
        }
    });*/

    //点击收到评论
    $('.receive_btn').on('click', function () {
        $('.my_friend span').remove();
        $('.mycomment-container').show();
        $('.friend-container').hide();
        $('.one_blog_container').hide();
        getFriendReplyInfo('', 1, '', 1, 10);
    });

    // blog 最新最热切换
    $('.order-blue').each(function (i) {
        $(this).bind('click', function () {
            $('.order-blue').removeClass('active');
            $(this).addClass('active');
            getFriendBlogInfo(i + 1, 1, 12);
        });
    });
    //年级筛选
    $('.select-order').change(function () {
        getFriendBlogInfo($('.order-bar .active').attr('index'), 1, 12);
    });
    // 删除上传图片
    $('body').on('click', '.blog-img-delete', function () {
        $(this).parent().remove();
    });
});