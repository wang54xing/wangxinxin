$(function() {
    var $commitBtn = $('#commit-btn');
    var $testType = $("input[name='testtype']");
    var $select = $('#select');
    var $selectSubject = $('.select-subject');

    $commitBtn.on('click', function() {
        for (var i = 0; i < $testType.length; i++) {
            if ($testType.eq(i).is(':checked')) {
                console.log($testType.eq(i).val());
            }
        }
    });

    $select.on('click', function() {
        if ($select.is(':checked')) {
            $select.parents('tr').nextAll('tr').find('input').prop('checked', true);
        } else {
            $select.parents('tr').nextAll('tr').find('input').prop('checked', false);
        }
    });

    $selectSubject.on('click',function() {
        if(!$(this).is(':checked')) {
            $select.prop('checked', false);
        }
    });
});