 //控制层 
app.controller('contentController' ,function($scope, contentService){

	$scope.content = {};

    //根据categoryId查询广告
	$scope.findByCategoryId=function(categoryId){
		contentService.findByCategoryId(categoryId).success(
			function(response){
				$scope.content[categoryId] = response;
			}			
		);
	}

	$scope.search = function ()
	{
		location.href = "http://localhost:9104#?keyword=" + $scope.keyword;
	}

});	
