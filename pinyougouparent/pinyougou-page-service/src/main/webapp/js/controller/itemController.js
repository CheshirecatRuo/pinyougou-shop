app.controller("itemController", function ($scope,$http){
    $scope.specList = {};

    $scope.addCart = function ()
    {
        alert("购买的Item的ID是" + $scope.sku.id);

        //$http发送请求执行购物车的增加
        $http.get("http://localhost:9106/cart/add.do?itemId=" + $scope.sku.id + "&num=" + $scope.num, {"withCredentials":true}).success(function (response){
            if (response.success)
            {
                location.href = "http://localhost:9106/cart.html";
            }
            else
            {
                alert(response.message);
            }
        });
    }
    $scope.num = 1;
    $scope.addNum = function (num)
    {
        $scope.num = parseInt($scope. num) + parseInt(num);
        if ($scope.num <= 0)
        {
            $scope.num = 1;
        }
    }

    $scope.selectSpec = function (key, value)
    {
        $scope.specList[key] = value;
        for (var i = 0; i < itemList.length; i++) {
            //获取第i个的spec
            var currentSpec = angular.fromJson(itemList[i].spec);
            if (angular.equals($scope.specList, currentSpec))
            {
                $scope.sku = angular.copy(itemList[i]);
                return
            }
        }
        $scope.sku = {"id" : 0, title: "该商品已下架！", "price": 0, "spec": []};
    }

    $scope.isSelectedSpec = function (key, value){
        if ($scope.specList[key] == value)
        {
            return "selected";
        }
    }

    $scope.sku = {};

    $scope.loadDefaultSku = function ()
    {
        $scope.specList = angular.fromJson(itemList[0].spec);

        $scope.sku = angular.copy(itemList[0]);
    }
})