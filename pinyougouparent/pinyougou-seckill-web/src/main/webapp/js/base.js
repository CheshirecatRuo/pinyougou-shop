var app=angular.module('pinyougou',[]);

app.config(['$locationProvider', function ($locationProvider){
    $locationProvider.html5Mode(true);
}]);

app.filter('trustHtml', ['$sce', function ($sce){
        return function (data)
        {
            return $sce.trustAsHtml(data);
        }
    }]);