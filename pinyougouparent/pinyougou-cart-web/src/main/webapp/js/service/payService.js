app.service("payService", function ($http){
   this. createNative = function ()
   {
      return $http.get("/pay/createNative.do");
   }

   this.queryPayStatus = function(outTradeNo)
   {
      return $http.get("/pay/queryPayStatus.do?outTradeNo="+ outTradeNo);
   }
});