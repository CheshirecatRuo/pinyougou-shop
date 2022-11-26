app.service("loginService", function ($http){
    this.showUsername = function ()
    {
        return $http.get("/user/name.do");
    }
})