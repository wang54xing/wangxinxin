/**
 * Created by NiuXin on 14-6-23.
 */

var page_size = 20, current_page = 1, total_pages = 1;
function getMyPromotionalInfo(page, callback) {
    $.getJSON('/user/getInviteInfo.action', {'page': page, 'pageSize': page_size}, callback);
}

function displayPromotionalList(data) {
    $('.extension-link').html(data.inviteURL);
    var inviteList = data.inviteList;
    $('#promotional-table tr:gt(0)').remove();
    for(var i in inviteList.rows) {
        var row = inviteList.rows[i];
        var tr = '<tr>';
        tr += '<td>' + row.inviteUser + '</td>';
        tr += '<td>' + row.inviteTime + '</td>';
        tr += '<td>' + row.rewardFlag + '</td>';
        tr += '</tr>'
        $('#promotional-table').append(tr);
    }
}

$(function(){
    getMyPromotionalInfo(0, function(data) {
        total_pages = Math.ceil(data.inviteList.total / page_size);
        var _totalPage = total_pages >= 1? total_pages: 1;
        var options = {
            currentPage: 1,
            totalPages: _totalPage,
            onPageClicked: function(e,originalEvent,type,page){
                getMyPromotionalInfo(page, displayPromotionalList);
            }
        }
        $('#pages #page-numbers-div').bootstrapPaginator(options);
        displayPromotionalList(data);
        $('.hardloadingb').hide();
        $('.extension-container').show();
    });
})