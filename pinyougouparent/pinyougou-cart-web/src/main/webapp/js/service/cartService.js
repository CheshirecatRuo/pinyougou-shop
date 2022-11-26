app.service("cartService", function ($http){

    this.findCartList = function ()
    {
        return $http.get("/cart/list.do");
    }

    this.add = function(itemId, num)
    {
        return $http.get("/cart/add.do?itemId=" + itemId + "&num=" + num);
    }

    this.addressList = function()
    {
        return $http.get("/address/list.do");
    }

    this.addOrder = function (order)
    {
        return $http.post("/order/add.do", order);
    }
})