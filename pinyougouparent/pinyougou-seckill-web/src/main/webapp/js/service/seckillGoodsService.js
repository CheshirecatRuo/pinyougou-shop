app.service("seckillGoodsService", function ($http){

    this.list = function ()
    {
        return $http.get("/seckill/goods/list.do");
    }

    this.findOne = function (id)
    {
        return $http.get("/seckill/goods/findOne.do?id=" + id);
    }

    this.add = function (id)
    {
        return $http.get("/seckill/order/add.do?id=" + id);
    }
});