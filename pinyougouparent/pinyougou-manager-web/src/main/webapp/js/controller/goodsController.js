//控制层
app.controller("goodsController",function($scope,$http,$controller,goodsService,itemCatService){

	$controller('baseController',{$scope:$scope});//继承

	$scope.searchEntity={};//定义搜索对象

	$scope.status = ["未审核", "审核通过", "审核不通过", "关闭"];

	$scope.itemCatShowList = {};

	$scope.updateStatus = function (status)
	{
		goodsService.updateStatus($scope.selectIds, status).success(function (response){
			if (response.success)
			{
				$scope.reloadList();
				$scope.selectIds = {};
			}
			else
			{
				alert(response.message);
			}
		});
	}

	$scope.getItemCatShowList = function (){
		return itemCatService.findAll().success(function (response){
			for (var i = 0; i < response.length; i++) {
				var key = response[i].id;
				var value = response[i].name;

				$scope.itemCatShowList[key] = value;
			}
		});
	}


	//分页
	$scope.findPage=function(page,rows){
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.list;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}
		);
	}

	//查询实体
	$scope.findOne=function(id){
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;
			}
		);
	}

	//保存
	$scope.save=function(){
		var serviceObject;//服务层对象
		//文本编辑器对象.html()获取文本编辑器内容
		$scope.entity.goodsDesc.introduction = editor.html();
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加
		}
		serviceObject.success(
			function(response) {
				if (response.success) {
					//判断执行状态
					if (response.success) {
						//重新加载新的数据
						$scope.reloadList();
					} else {
						alert(response.message);
					}
				}
			}
		);
	}

	//批量删除
	$scope.delete=function(){
		//获取选中的复选框
		goodsService.delete( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}
			}
		);
	}

	//搜索
	$scope.search=function(page,rows){
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.list;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}
		);
	}
});	
