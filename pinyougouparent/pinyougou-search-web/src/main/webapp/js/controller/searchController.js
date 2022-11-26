
app.controller("searchController", function($scope, $location, searchService){

    $scope.resultMap = {brandList:[]};

    $scope.loadKeyword = function ()
    {
       var keyword =  $location.search()['keyword'];

       if (keyword != null)
       {
           $scope.searchMap.keyword = keyword;
       }

       //搜索
        $scope.search();
    }

    $scope.search = function ()
    {
        searchService.search($scope.searchMap).success(function (response){
            $scope.resultMap = response;

            //$scope.keywordLoadBrand();

            $scope.pageHandler(response.totalElements, $scope.searchMap.pageNum);
        });
    }

    $scope.searchMap = {"keyword" : "", "category" : "", "brand": "", spec:{}, "price":"", "pageNum" : 1, "size" : 10, "sort": "", "sortField" : "item_price"};

    $scope.addItemSearch = function (key, value)
    {
        if (key == "category" || key == "brand" || key == "price") {
            $scope.searchMap[key] = value;
        }
        else
        {
            $scope.searchMap.spec[key] = value;
        }

        //搜索实现
        $scope.search();
    }

    $scope.removeItemSearch = function (key)
    {
        if (key == "category" || key == "brand" || key == "price") {
            $scope.searchMap[key] = "";
        }
        else
        {
            //$scope.searchMap.spec[key] = "";
            //将数据从map结构中移除
            delete $scope.searchMap.spec[key];
        }

        //搜索实现
        $scope.search();
    }

    //分页定义
    $scope.page={
        size:10,        //每页显示多少条
        total:0,        //总共有多少条记录
        pageNum:1,      //当前页
        offset:1,       //偏移量
        lpage:1,        //起始页
        rpage:1,        //结束页
        totalPage:1,    //总页数
        pages:[],       //页码
        nextPage:1,     //下一页
        prePage:1,       //上一页
        hasPre:0,       //是否有上页
        hasNext:0       //是否有下页
    }

    $scope.pageHandler = function (total, pageNum)
    {
        $scope.page.pageNum = pageNum;
        var totalPage = total % $scope.page.size == 0 ? total / $scope.page.size : parseInt((total / $scope.page.size) + 1);
        $scope.page.totalPage = totalPage;
        var offset = $scope.page.offset;
        var lpage = $scope.page.lpage;
        var rpage = $scope.page.rpage;

        if ((pageNum - offset) > 0)
        {
            lpage = pageNum - offset;
            rpage = pageNum + offset;
        }
        else if ((pageNum - offset) <= 0)
        {
            lpage = 1;
            rpage = pageNum + offset + Math.abs(pageNum - offset) + 1;
        }

        if (rpage > totalPage)
        {
            lpage = lpage - (rpage - totalPage);
            rpage = totalPage;
        }

        if (lpage <= 0)
        {
            lpage = 1;
        }

        $scope.page.pages=[];
        for (var i = lpage; i <= rpage; i++)
        {
            $scope.page.pages.push(i);
        }

        if ((pageNum - 1) >= 1)
        {
            $scope.page.prePage = (pageNum - 1);
            $scope.page.hasPre = 1;
        }
        else
        {
            $scope.page.hasPre = 0;
        }

        if (pageNum < totalPage)
        {
            $scope.page.nextPage = pageNum + 1;
            $scope.page.hasNext = 1;
        }
        else
        {
            $scope.page.hasNext = 0;
        }
    }

    $scope.pageSearch = function (pageNum)
    {
        if (!isNaN(pageNum))
        {
            $scope.searchMap.pageNum = parseInt(pageNum);
        }
        else
        {
            $scope.searchMap.pageNum = 1;
        }

        if ($scope.searchMap.pageNum > $scope.page.totalPage)
        {
            $scope.searchMap.pageNum = $scope.page.totalPage
        }

        $scope.search();
    }

    $scope.sortSearch = function (sort, sortField)
    {
        $scope.searchMap.sort = sort;
        $scope.searchMap.sortField = sortField;

        $scope.search();
    }



    $scope.keywordLoadBrand = function ()
    {
        if ($scope.resultMap.brandList != null) {
            for (var i = 0; i < $scope.resultMap.brandList.length; i++) {
                var brandName = $scope.resultMap.brandList[i].text;
                var index = $scope.searchMap.keyword.indexOf(brandName);
                if (index >= 0)
                {
                    $scope.searchMap.brand = brandName;
                    return true;
                }
            }
        }
    }

    return false;
})