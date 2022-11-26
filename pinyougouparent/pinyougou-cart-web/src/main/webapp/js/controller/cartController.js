app.controller("cartController", function($scope, cartService){

    $scope.findCartList = function ()
    {
        cartService.findCartList().success(function (response){
            $scope.cartList = response;
            sum($scope.cartList);
        })
    }

    $scope.add = function (itemId, num){
        cartService.add(itemId, num).success(function (response){
            if (response.success)
            {
                $scope.findCartList();
            }
            else
            {
                alert(response.message);
            }

        })
    }

    sum = function(cartList)
    {
        //定义一个参数，用于存储总价格和数量
        $scope.totalValue = {totalNum: 0, totalValue: 0.0};

        for (var i = 0; i < cartList.length; i++) {
            var cart = cartList[i];

            var itemList = cart.orderItemList;

            for (var j = 0; j < itemList.length; j++) {
                $scope.totalValue.totalNum += itemList[j].num;
                $scope.totalValue.totalValue += itemList[j].totalFee;

            }
            
        }
    }

    $scope.addressList = function ()
    {
        cartService.addressList().success(function (response){
            $scope.addressList = response;
            for (var i = 0; i < $scope.addressList.length; i++) {
                if ($scope.addressList[i].isDefault == "1")
                {
                    $scope.address = angular.copy($scope.addressList[i]);
                    break;
                }
            }
        });
    }

    $scope.selectAddress = function (address)
    {
        $scope.address = address;
    }

    $scope.order = {paymentType:"1"} //1在线支付 2-货到付款

    $scope.selectPayType = function (type)
    {
        $scope.order.paymentType = type;
    }

    $scope.addOrder = function ()
    {
        $scope.order.receiverAreaName = $scope.address.address;
        $scope.order.receiverMobile = $scope.address.mobile;
        $scope.order.receiver = $scope.address.contact;

        cartService.addOrder($scope.order).success(function (response){
            if (response.success)
            {
                alert("恭喜下单成功!");
                location.href = "/pay.html";
            }
            else
            {
                alert("下单不成功");
            }
        });
    }
})