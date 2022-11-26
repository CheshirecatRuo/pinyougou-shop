app.controller("payController", function($scope, $location, payService){

    $scope.createNative = function()
    {
        payService.createNative().success(function(response){

            $scope.outTradeNo = response.out_trade_no;
            $scope.totalFee = (response.total_fee / 100);
            $scope.codeUrl = response.code_url;

            console.log(document.getElementById("payImg"));
            var qr = window.qr = new QRious({
                element: document.getElementById("payImg"),
                size: 300,
                value: $scope.codeUrl,
                level: 'M'
            });

            payService.queryPayStatus($scope.outTradeNo).success(function(response){
                if(response.success)
                {
                    location.href="/paysuccess.html?money=" + $scope.totalFee;
                }
                else
                {
                    if(response.message == "timeout")
                    {
                         //$scope.createNative();
                         location.href="/payfail.html";
                    }
                    else
                    {
                        location.href="/payfail.html";
                    }
                }


            });
        });
    }

    $scope.getMoney = function()
    {
            return $location.search()["money"];
    }
})