app.service("userService", function ($http) {

    //注册功能
    this.reg = function (entity, identifyingCode)
    {
        return $http.post("/user/add.do?code="+identifyingCode, entity);
    }

    this.createIdentifyingCode = function (phone)
    {
        return $http.get("/user/createIdentifyingCode.do?phone=" + phone);
    }
})