(function () {
    'use strict';

    /**
     * add directives
     */
    angular.module('angular-echarts', [])
        .directive('eChart', [function () {
            return {
                restrict: 'EA',
                scope: {
                    option: '='
                },
                controller: function ($parse, $attrs, $scope) {
                    this.setChartObj = function (chartObj) {
                        $parse($attrs.chartObj).assign($scope.$parent, chartObj);
                    };
                },
                link: function (scope, element, attrs, ctrl) {
                    var options = scope.option || {};
                    var dom = element[0], chart;

                    function setOptions() {
                        if (!options) {
                            return;
                        }
                        if (!chart) {
                            chart = echarts.init(dom);
                            ctrl.setChartObj(chart);
                        }
                        if (options.forceClear) {
                            chart.clear();
                        }
                        if (options.series.length) {
                            chart.setOption(options, options.notMerge);
                            chart.resize();
                        } else {
                            element.text('\u6CA1\u6709\u6570\u636E');
                        }
                    }

                    // update when charts config changes
                    scope.$watch(function () {
                        return scope.option;
                    }, function (value) {
                        if (value) {
                            options = scope.option;
                            setOptions();
                        }
                    });
                }
            };
        }]);
})();