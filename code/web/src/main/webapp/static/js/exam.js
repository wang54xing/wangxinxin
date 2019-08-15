$(function() {
    var $completeBtn = $('.exam-complete');
    var $answerContainer = $('.answer-container');
    var $inputAnswer = $('.input-answer').length == 0 ? null : $('.input-answer');
    var $yourAnswer = $('.your-answer');
    var $selectList = $('.select-answer').length == 0 ? null : $('.select-answer');

    $completeBtn.on('click', function() {
        $completeBtn.remove();
        if ($inputAnswer) {
            $yourAnswer.html($inputAnswer.val().replace(/\n/g, '<br/>'));
        }
        if ($selectList) {
            for (var i = 0; i < $selectList.find('li').length; i++) {
                var seletAnswer = '';
                $selectList.find('li').eq(i).find('input').each(function() {
                    if ($(this).is(':checked')) {
                        seletAnswer += $(this).val()+' ';
                    }
                });
                $yourAnswer.append('<div>' + (i + 1) + '. ' + seletAnswer + '</div>');
            }
        }
        $answerContainer.slideDown('slow');
    });
});