/**
 * Created by Tony on 2015/1/7.
 */
angular.module('k6kt.filter', [])
    .filter('questionType', [function () {
        return function (q) {
            return q > 0 & q<10000 ? '单选' : q < 0 ? '多选' : q>10000? '打分' : '问答';
        };
    }])
    .filter('percent', [function () {
        return function (val) {
            return val * 100 + '%';
        }
    }])
    .filter('limit', ['$filter', function ($filter) {
        return function (list) {
            return list.expanded ? list : $filter('limitTo')(list, 2);
        };
    }])
    .filter('deadLine', [function () {
        return function (fullDateStr) {
            var date = moment(fullDateStr, 'YYYYMMDD HH:mm:ss');
            if (!date.isValid()) {
                return '';
            }
            days = date.diff(moment(), 'days');
            if (days > 0 && days <= 3) {
                return days + '天后截止';
            }
            if (days == 0) {
                return '今天截止';
            }
            return date.format('YYYY/MM/DD');
        }
    }])
    .filter('removeHtml', [function () {
        return function (str) {
            return (str || '').replace(/<[^>]+>/g, "");
        }
    }])
    .filter('formatDate',[function(){
        return function(str, format){
            //str.replace(/(.{6})/,"$1/")
            return moment(str, 'YYYYMMDD HH:mm:ss').format(format);
        };
    }])
    .filter('subContent',[function(){
        return function(str,count){
           if(str.length > count){
               return str.substring(0,count) + '...';
           }else{
               return str;
           }
        };
    }]);