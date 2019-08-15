/**
 * Created by Tony on 2015/1/7.
 */

function showContent(type) {
    $('#content_right>div').hide();
    $('#' + type + '_section').removeClass('hidden').show();
}

var app = angular.module('index', ['ngAnimate', 'ui.bootstrap', 'k6kt.filter'])
    .controller('IndexCtrl', ['$scope', 'QuestionService', function ($scope, questionService) {
        $.extend($scope, {
            show: 'normal',
            unread: 0,
            showContent: function (type) {
                $scope.show = type;
            },
            getUnread: function () {
                questionService.getUnread().then(function (count) {
                    $scope.unread = count;
                });
            }
        });
        //$scope.getUnread();
    }])
    .directive('stopEvent', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attr) {
                element.bind('click', function (e) {
                    e.stopPropagation();
                });
            }
        };
    });