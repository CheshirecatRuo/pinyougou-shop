app.controller("userController", function($scope, userService){

    //注册方法
    $scope.reg = function()
    {
        userService.reg($scope.entity, $scope.identifyingCode).success(function(response)
        {
            alert(response.message);
        })
    }

    $scope.entity= { "phone":""}
    $scope.createIdentifyingCode = function()
    {
        if ($scope.entity.phone == undefined || $scope.entity.phone == null)
        {
            alert("请输入手机号");
            return;
        }
        userService.createIdentifyingCode($scope.entity.phone).success(function (response){
            alert(response.message);
        });
    }
})