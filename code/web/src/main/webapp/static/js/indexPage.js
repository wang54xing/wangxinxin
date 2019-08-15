var currentPageID = 0;
var sub_site = document.domain.substring(0, document.domain.indexOf('.'));
switch (sub_site) {
    case 'chuzhou':
        break;
    case 'maanshan':
        break;
    case 'tongling':
        break;
    default:
        sub_site = 'ah';
}

//var sTimer;
function pauseSlides() {

    /*
        var slideTimes=[6000,4000,4000,4000];

        var slideNo=jQuery('#slider').data('nivo:vars').currentSlide;


        if(slideTimes[slideNo]<=2000) {
            jQuery('#slider .nivo-directionNav a').css('visibility','hidden');
        }
        else {
            jQuery('#slider .nivo-directionNav a').css('visibility','visible');
        };


        if(slideTimes[slideNo]==0) return;

        var sTimer = setTimeout(function() {
            jQuery('#slider .nivo-nextNav').trigger('click');
        }, slideTimes[slideNo]);
        */
    var slideNo = jQuery('#slider').data('nivo:vars').currentSlide;
    if (slideNo == 0) {
        $('#slider').data('nivo:vars').stop = true;
        setTimeout("$('#slider').data('nivo:vars').stop = false;", 3000);
    }

};
$(window).load(function() {
    //    getNewCourseVideo();
    //    getCourseVideo(0);
    //    getSchoolList('14');
    //    getFreeCourseVideo();
    // getCarouselImage();

    //    cutImage($('.course-video-img'), 166, 99);
    //    cutImage($('.newvideo-img'), 110, 66);
    //    cutImage($('.free-img'), 187, 111);


    $('#slider').fadeIn().nivoSlider({
        pauseOnHover: true,
        pauseTime: 4000,
        effect: 'fade',
        directionNav: false, // Next & Prev navigation
        controlNav: true, // 1,2,3... navigation
        controlNavThumbs: false,
        afterLoad: function() {
            pauseSlides();
        },
        afterChange: function() {
            pauseSlides();
        }
    });

    $('.M_subject').on('click', function() {
        var subjectId = $(this).attr('data-subjectId');
        $('.jingjiang-select').find('.selected').removeClass('selected');
        $(this).addClass('selected');
        $('.courseVideo-container.selected').removeClass('selected');
        $('.courseVideo-container[data-subjectId="' + subjectId + '"').addClass('selected');
        /*if (subjectId == 70) {
            getPromotionVideo();
        } else {
            getCourseVideo(subjectId);
        }*/
    });

    $('.M_area').on('click', function() {
        var provinceId = $(this).attr('data-provinceId');
        $('.area-select').find('.selected').removeClass('selected');
        $(this).addClass('selected');
        $('.college-next').removeClass('disabled');
        $('.college-prev').attr('data-prevpage', 0);
        $('.college-next').attr('data-nextpage', 2);
        $('.college-inline').empty();
        getSchoolList(provinceId);
    });

    $('.btn-next').on('click', function() {
        if (!$(this).hasClass('disabled')) {
            var area = $(this).attr('data-area');
            var prevBtn = $(this).siblings('.btn-prev');
            var nextBtn = $(this);
            var nextPage = nextBtn.attr('data-nextpage');
            var prevPage = prevBtn.attr('data-prevpage');
            nextBtn.attr('data-nextpage', (parseInt(nextPage) + 1));
            prevBtn.attr('data-prevpage', (parseInt(prevPage) + 1));
            runSwitchPage(area, nextPage);
            /*$('.college-inline').animate({
                scrollLeft : '+=200'
            });*/
        }
    });

    $('.btn-prev').on('click', function() {
        var area = $(this).attr('data-area');
        var prevBtn = $(this);
        var nextBtn = $(this).siblings('.btn-next');
        var nextPage = nextBtn.attr('data-nextpage');
        var prevPage = prevBtn.attr('data-prevpage');
        if (prevPage != 0) {
            if (nextBtn.hasClass('disabled')) nextBtn.removeClass('disabled');
            nextBtn.attr('data-nextpage', (parseInt(nextPage) - 1));
            prevBtn.attr('data-prevpage', (parseInt(prevPage) - 1));
            runSwitchPage(area, prevPage);
        }
    });

    $('.nivo-control').eq(0).text('20分钟直击高考 全站视频免费看');
    $('.nivo-control').eq(1).text('高考名师 豪华阵容');
    $('.nivo-control').eq(2).text('高考题库 在线模拟');
});

function runSwitchPage(area, page) {
    switch (area) {
        case 'newvideo':
            {
                // $('.newvideo-container').empty();
                getNewCourseVideo(page);
                break;
            }
        case 'college':
            {
                // $('.college-inline').empty();
                var provinceId = $('.M_area.selected').attr('data-provinceId');
                getSchoolList(provinceId, page);
                break;
            }
        case 'teacher':
            {
                // $('.slider-bx').empty();
                getTeacherList(page);
                break;
            }
    }
}

// function getTeacherList(pageNumber) {
//     var page = pageNumber || 1;
//     $.ajax({
//         url: 'getTeacherList.action',
//         type: 'get',
//         dataType: 'json',
//         data: {
//             pageSize: 4,
//             page: page
//         },
//         success: function(data) {
//             var rows = data.rows;
//             $('.slider-bx').empty();
//             var content = '';
//             var text = '';
//             var contentHead = '<ul class="bxslider">';
//             var contentEnd = '</ul>';
//             var textHead = '<div id="bx-pager">';
//             var textEnd = '</div>';
//             for (var i = 0; i < rows.length; i++) {
//                 content += '<li><a href="/teacher/' + rows[i].id + '">';
//                 content += '<img src="/upload/pic/teacher/' + rows[i].avatarUrl + '" alt=""';
//                 content += 'title="' + rows[i].teacherName + ',' + rows[i].schoolName + '<br>' + rows[i].introduce + '">';
//                 content += '</a></li>';
//                 text += '<a data-slide-index="' + i + '" href="">';
//                 text += '<img src="/upload/pic/teacher/' + rows[i].imageUrl + '" /></a>';
//             }
//             content = contentHead + content + contentEnd;
//             text = textHead + text + textEnd;
//             $('.slider-bx').append(content).append(text);

//             var slider = $('.bxslider').bxSlider({
//                 pagerCustom: '#bx-pager',
//                 mode: 'fade',
//                 auto: true,
//                 captions: true
//             });
//             slider.startAuto();

//             var totalPage = checkPage(data.total, 4);
//             if ((totalPage + 1) == $('.teacher-next').attr('data-nextpage')) {
//                 $('.teacher-next').addClass('disabled');
//             }
//         },
//         error: function() {
//             console.log('getTeacherList ERROR');
//         }
//     });
// }

function checkPage(total, pageSize) {
    var totalPage;
    if (total % pageSize != 0) {
        totalPage = parseInt(total / pageSize) + 1;
    } else {
        totalPage = total / pageSize;
    }
    return totalPage;
}

function getNewCourseVideo(pageNumber) {
    var page = pageNumber || 1;
    $.ajax({
        url: 'getNewCoursePackageList.action',
        type: 'get',
        dataType: 'json',
        data: {
            pageSize: 5,
            page: page
        },
        success: function(data) {
            if (data.end) {
                $('.newvideo-next').addClass('disabled');
            } else {
                var rows = data.rows;
                $('.newvideo-container').empty();
                for (var i = 0; i < rows.length; i++) {
                    var content = '';
                    content += '<a class="newvideo" href="/video/' + rows[i].courses[0].videos[0].id + '/' + rows[i].id + '">';
                    content += '<div><div class="newvideo-img-container"><img class="newvideo-img" src="' + rows[i].imageUrl + '" alt="' + rows[i].packageName + '"></div>';
                    content += '<div>' + rows[i].packageName + '</div>';
                    if (rows[i].teachers.length > 1) {
                        content += '<span>' + rows[i].teachers[0].teacherName + '等';
                    } else {
                        content += '<span>' + rows[i].teachers[0].teacherName;
                    }
                    content += '&nbsp;&nbsp;' + rows[i].teachers[0].subjectName + '</span>';
                    content += '</div><div class="newvideo-date">更新时间：' + rows[i].refreshTime + '</div></a>';
                    $('.newvideo-container').append(content);
                }

                var totalPage = checkPage(data.total, 5);
                if ((totalPage + 1) == $('.newvideo-next').attr('data-nextpage')) {
                    $('.newvideo-next').addClass('disabled');
                }
                cutImage($('.newvideo-img'), 110, 66);
            }
        },
        error: function() {
            console.log('getNewCourseVideo error');
        }
    });
}

function getSchoolList(provinceId, pageNumber) {
    var page = pageNumber || 1;
    $.ajax({
        url: 'getSchoolList.action',
        type: 'get',
        dataType: 'json',
        data: {
            pageSize: 4,
            page: page,
            provinceId: provinceId
        },
        success: function(data) {
            if (data.end) {
                $('.college-next').addClass('disabled');
            } else {
                var rows = data.rows;
                $('.college-inline').empty();
                for (var i = 0; i < rows.length; i++) {
                    var content = '';
                    content += "<a class='college-info' href=/university/" + rows[i].videoId + ">";
                    content += "<img class='college-img' src='/upload/pic/school/" + rows[i].imageUrl + "' alt='" + rows[i].schoolName + "'>";
                    content += "<div class='college-name'>" + rows[i].schoolName + "</div>";
                    content += "<div class='college-ename'>" + rows[i].englishName + "</div></a>";
                    $('.college-inline').append(content);
                }

                var totalPage = checkPage(data.total, 4);
                if ((totalPage + 1) == $('.college-next').attr('data-nextpage')) {
                    $('.college-next').addClass('disabled');
                }
            }
        },
        error: function() {
            console.log('getSchoolList error');
        }
    });
}

function getPromotionVideo() {
    $.ajax({
        url: 'listCoursesPackageDiscount.action',
        type: 'get',
        dataType: 'json',
        data: {
            pageSize: 12
        },
        success: function(data) {
            showDiscountCourseVideo(data);
        },
        error: function() {
            console.log('getPromotionVideo error');
        }
    });
}

function getCourseVideo(subjectId) {
    $.ajax({
        url: 'getHomepageCourseList.action',
        type: 'get',
        dataType: 'json',
        data: {
            subject: subjectId
        },
        success: function(data) {
            showCourseVideo(data);
        },
        error: function() {
            console.log('getCourseVideo error');
        }
    });
}

function showCourseVideo(data) {
    $('.courseVideo-container').empty();
    var rows = data.rows;
    for (var i = 0; i < rows.length; i++) {
        var content = '';
        if (i % 4 == 0)
            content += '<div class="course-inline">';
        content += "<a target='_blank' class='course-video' href='" + rows[i].url + "'>";
        content += "<div class='course-video-img-container'><img class='course-video-img' src='" + rows[i].imageurl + "'></div>";
        content += "<div class='course-video-name'>" + rows[i].coursename + "</div><div class='course-video-teacher'>";
        content += '<span>' + rows[i].teachername + '</span>';
        content += '<span>&nbsp;&nbsp;' + rows[i].subjectname + '</span>';
        content += "</div></a>";
        if (i % 4 == 0)
            $('.courseVideo-container').append(content);
        else
            $('.course-inline').eq(parseInt(i / 4)).append(content);
    }
    cutImage($('.course-video-img'), 166, 99);
}

function showDiscountCourseVideo(data) {
    $('.courseVideo-container').empty();
    var rows = data.rows;
    for (var i = 0; i < rows.length; i++) {
        var content = '';
        if (rows[i].courses[0].videos.length < 1) continue;
        if (i % 4 == 0) content += '<div class="course-inline">';
        content += "<a target='_blank' class='course-video' href='/video/" + rows[i].courses[0].videos[0].id + '/' + rows[i].courses[0].id + "'>";
        content += "<div class='course-video-img-container'><img class='course-video-img' src='" + rows[i].imageUrl + "'></div>";
        content += "<div class='course-video-name'>" + rows[i].courses[0].courseName + "</div><div class='course-video-teacher'>";
        content += '<span>' + rows[i].teachers[0].teacherName + '</span>';
        content += '<span>&nbsp;&nbsp;' + rows[i].teachers[0].subjectName + '</span>';
        content += "</div></a>";
        if (i % 4 == 3) content += '</div>';
        if (i % 4 == 0) $('.courseVideo-container').append(content);
        else $('.course-inline').eq(parseInt(i / 4)).append(content);
    }
    cutImage($('.course-video-img'), 166, 99);
}

function getFreeCourseVideo() {
    $.ajax({
        url: 'listCoursesPackageFree.action',
        type: 'get',
        dataType: 'json',
        data: {},
        success: function(data) {
            $('.free-container').empty();
            var rows = data.rows;
            for (var i = 0; i < rows.length; i++) {
                var content = '';
                if (rows[i].courses[0].videos.length < 1) continue;
                content += '<a target="_blank" class="free-course" href="/video/' + rows[i].courses[0].videos[0].id + '/' + rows[i].courses[0].id + '">';
                content += '<div class="free-img-container"><img class="free-img" src="' + rows[i].imageUrl + '" alt="' + rows[i].courses[0].courseName + '"></div>';
                content += '<div class="free-title">' + rows[i].courses[0].courseName + '</div>';
                content += '<div class="free-teacher">' + rows[i].teachers[0].subjectName + ' ' + rows[i].teachers[0].teacherName + '</div></a>';
                $('.free-container').append(content);
            }
            cutImage($('.free-img'), 187, 111);
        },
        error: function() {
            console.log('getFreeCourseVideo error');
        }
    });
}

function cutImage(obj, width, height) {
    for (var i = 0; i < obj.length; i++) {
        var imgSrc = obj.eq(i).attr('src');
        loadImage(obj.eq(i), imgSrc, function(position, target) {
            var tp = (height - width / (position.iwidth / position.iheight)) / 2 + 'px';
            target.css({
                marginTop: tp,
                'width': width
            });
        });
    }
}

function loadImage(obj, imgsrc, callback) {
    var img = new Image();
    img.src = imgsrc;
    var position = {};

    img.onload = function() {
        position = {
            iwidth: img.width,
            iheight: img.height
        }
        callback(position, obj);
    }
}
// function getSchoolListByHot() {
//     var ret = [];
//     $("#univ-show").addClass('hardloadingm');
//     $.ajax({
//         url: "listLevelSchoolByHot.action",
//         type: "get",
//         dataType: "json",
//         async: true,
//         data: {

//         },
//         success: function(data) {
//             ret = data;
//             showSchools(data);
//         }
//     });
//     return ret;
// }

// function showSchools(videos) {
//     // show html
//     var target = $("#univ-show");
//     target.empty();
//     var html = "";
//     var rows = videos.rows;
//     for (var i = 0; i < rows.length; i++) {
//         var sc = rows[i];
//         html += '<div><a href="/university/' + sc.videoId + '">';
//         html += '<img src="upload/pic/school/' + sc.imageUrl + '" class="simg" style="width:201px;height:143px">';
//         // html+='<img src="/img/bjdx.jpg" class="simg">';
//         html += '</a><h4 style="margin:5px 0;">' + sc.schoolName + '</h4>';
//         //html+='<div><img src="/img/view-times.jpg"><span>8888次</span></div></div>';
//         html += '</div>';
//     }
//     target.append(html);
//     $("#univ-show").removeClass('hardloadingm');
// }

// function GotoSpeechPage() {
//     setCookie('clsubjectId', 45);
//     location.href = "/courses";
// }
function getPlayer(movieName) {
    if (navigator.appName.indexOf("Microsoft") != -1) {
        var reObj = window[movieName];
        try {
            if (reObj.length > 0) {
                return reObj[0];
            } else {
                return reObj;
            }
        } catch (e) {}
        return document[movieName];
    } else {
        return document[movieName];
    }
}

function showvideo() {
    $('#tinymask').show();
    $('#tinybox').css({
        left: ($(window).width() - $('#tinybox').width()) / 2,
        top: ($(window).height() - $('#tinybox').height()) / 2
    });
    $('#tinybox').show();
    var player = getPlayer('polyvplayer1a9bb5bed53becd8b73ad7248608ac85_1');
    if (player != undefined && player.j2s_resumeVideo != undefined) {
        player.j2s_resumeVideo();
    }
}

function hideme() {
    $('#tinymask').hide();
    $('#tinybox').hide();
    var player = getPlayer('polyvplayer1a9bb5bed53becd8b73ad7248608ac85_1');
    if (player != undefined && player.j2s_stopVideo != undefined) {
        player.j2s_stopVideo();
    }
}