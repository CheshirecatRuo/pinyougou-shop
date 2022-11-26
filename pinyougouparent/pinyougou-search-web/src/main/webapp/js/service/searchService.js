app.service("searchService", function ($http){

    this.search = function (entity)
    {
        return $http.post("/item/search.do", entity);
    }
});