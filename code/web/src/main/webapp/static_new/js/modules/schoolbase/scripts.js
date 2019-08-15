'use strict';

(function ($) {

    $(function () {

        $('input[name="chart-state"]').on('click', function () {
            $('#edit-panel, .orgchart').toggleClass('view-state');
            if ($(this).val() === 'edit') {
                $('.orgchart').find('tr').removeClass('hidden')
                    .find('td').removeClass('hidden')
                    .find('.node').removeClass('slide-up slide-down slide-right slide-left');
            } else {
                $('#btn-reset').trigger('click');
            }
        });

        $('input[name="node-type"]').on('click', function () {
            var $this = $(this);
            if ($this.val() === 'parent') {
                $('#edit-panel').addClass('edit-parent-node');
                $('#new-nodelist').children(':gt(0)').remove();
            } else {
                $('#edit-panel').removeClass('edit-parent-node');
            }
        });

        $('#btn-add-input').on('click', function () {
            $('#new-nodelist').append('<li><input type="text" class="new-node"></li>');
        });

        $('#btn-remove-input').on('click', function () {
            var inputs = $('#new-nodelist').children('li');
            if (inputs.length > 1) {
                inputs.last().remove();
            }
        });


        $('#btn-reset').on('click', function () {
            $('.orgchart').trigger('click');
            $('#new-nodelist').find('input:first').val('').parent().siblings().remove();
            $('#node-type-panel').find('input').prop('checked', false);
        });

    });

})(jQuery);