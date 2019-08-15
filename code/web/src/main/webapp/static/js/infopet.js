$(function() {
    getstudentInfo();
    $('#experience').hover(function() {
        $('.change-name').show();
    }, function() {
        $('.change-name').hide();
    });

    $('.change-name').hover(function() {
        $('.change-name').show();
    }, function() {
        $('.change-name').hide();
    });

    $('.change-name').on('click', function(e) {
        stopPropagation(e);
        if (!$('.input-name').hasClass('exist')) {
            $('.input-name').val($('.pet-name').text()).show();
            $('.input-name').focus();
            $('.input-name').addClass('exist');
            $('.pet-name').hide();
        }
    });
    $('.input-name').bind('click', function(e) {
        stopPropagation(e);
    });
    $('.input-name').keydown(function(e) {
        if (e.which == 13) changeName();
    });
    $(document).on('click', function(event) {
        if ($('.input-name').hasClass('exist')) {
            event.preventDefault();
            changeName();
        } else {
            stopPropagation(event);
        }
    });

    $('#experience, #user-exp').on('click', function(event) {
        if (!$('.input-name').hasClass('exist')) {
            window.location = "/petbag";
        }
    });
});

function stopPropagation(e) {
    if (e.stopPropagation)
        e.stopPropagation();
    else
        e.cancelBubble = true;
}

function changeName() {
    if ($('.input-name').val() == '') {
        alert('名字不能为空！');
        $('.input-name').focus();
    } else if ($('.input-name').val().getLength() > 20) {
        alert('长度限制20个字符！');
        $('.input-name').focus();
    } else {
        $('.input-name').hide();
        $.ajax({
            url: '/reverse/updatePetName.action',
            type: 'post',
            dataType: 'json',
            data: {
                petname: $('.input-name').val()
            },
            success: function() {
                $('.pet-name').text($('.input-name').val());
                $('.input-name').removeClass('exist');
            },
            error: function() {
                alert('改名失败');
                console.log('updatePetName error');
            },
            complete: function() {
                $('.pet-name').show();
            }
        });
    }
}

function getstudentInfo() {
    var ret = [];

    return ret;
}