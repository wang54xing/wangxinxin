$(function () {
    //添加教材弹窗
    $(".btn-esc,.alert-r").on("click", function () {
        $(this).parent().parent().fadeOut();
        $(".bg").fadeOut();
    })
    //编辑
    $("body").on("click", ".detail-top .edit-btn", function () {
        if ($(this).text() === "编辑") {
            $(".add-chapter").show();
            $(this).text("保存");
        } else {
            $(".add-chapter").hide();
            $(this).text("编辑");
        }
    })
    $("body").on("mouseover", ".textbooks-detail>dt,.textbooks-detail>dd>div", function () {
        if ($(".detail-top .edit-btn").text() === "编辑") {
            return
        } else {
            $(this).find(".chapter-operate").show();
        }
    })
    $("body").on("mouseout", ".textbooks-detail>dt,.textbooks-detail>dd", function () {
        $(this).find(".chapter-operate").hide();
    })
    //添加出版社
    $("body").on("click",".add-publish",function(){
        $(".addpublisher-alert,.bg").fadeIn();
        $("#itemName").val("")
    })
})