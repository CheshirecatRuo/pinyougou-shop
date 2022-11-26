app.controller("seckillGoodsController", function($scope, $location, $interval, seckillGoodsService){

    $scope.list = function ()
    {
        seckillGoodsService.list().success(function (response){
            $scope.list = response;
        });
    }

    $scope.findOne = function ()
    {
        var id = $location.search()["id"];

        if (!isNaN(id))
        {
            seckillGoodsService.findOne(id).success(function (response){
                $scope.item = response;
                var num = new Date($scope.item.endTime).getTime() - new Date().getTime();
                var time = $interval(function (){
                    num -= 1000;
                    if (num <= 0)
                    {
                        $interval.cancel(time);
                        $scope.timeStr = "已结束";
                    }
                    $scope.timeStr = datetimeinfo(num);
                }, 1000);
            });
        }
    }

    $scope.add = function()
    {
        var id = $location.search()["id"];

        if (!isNaN(id)) {
            //增加订单
            seckillGoodsService.add(id).success(function (response) {
                if (response.success)
                {
                    //跳转到支付页面
                    alert("下单成功，即将跳转到支付页面");
                    location.href = "/pay.html";
                }
                else
                {
                    if (response.message == "403")
                    {
                        alert("请登录!");
                        location.href = "/login/loading.do";
                    }
                    else
                    {
                        alert(response.message);
                    }
                }
            });
        }
    }

    /*
*
* 该方法的作用用于换算毫秒时间
*               换算成：有多少天、有多少时、有多少分钟、有多少秒
* */
    datetimeinfo=function (num) {
        var second = 1000;          //1秒有多少毫秒
        var minute = second*60;     //1分钟有多少毫秒
        var hour =minute*60;        //1小时有多少毫秒
        var day = hour*24;          //1天有多少毫秒

        //天
        var days = Math.floor(num/day);

        //小时
        var hours =Math.floor(num%day/hour);

        //分钟   天的秒数+小时的秒数 = Math.floor( num/minute*60 )
        var minutes =Math.floor( (num%hour)/minute );

        //秒
        var seconds = Math.floor( (num%minute)/second );
        return days+"天"+hours+"小时"+minutes+'分钟'+seconds+'秒';
    }
});