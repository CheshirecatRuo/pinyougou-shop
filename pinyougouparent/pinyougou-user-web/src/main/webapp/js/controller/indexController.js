app.controller("indexController", function ($scope, loginService){

    $scope.showUsername = function ()
    {
        loginService.showUsername().success(function (response){
            $scope.username = response;
        });
    }
})