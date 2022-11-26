 //控制层 
app.controller('itemCatController' ,function($scope,$controller,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承

	//定义实体
	$scope.entity = {"typeId":"", "name":"", "parentId":0};
	//select2变量
	$scope.typeId = null;

	//读取列表数据绑定到表单中
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.list;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体
	$scope.typeTemplateList = [];
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity = response;
			}
		);
		typeTemplateService.findTypeTemplateList().success(function (response) {
			$scope.typeTemplateList = {data:response};
		});
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象
		if ($scope.entity_3 != null)
		{
			$scope.entity.parentId = $scope.entity_3.id;
		}
		else if($scope.entity_2 != null)
		{
			$scope.entity.parentId = $scope.entity_2.id;
		}
		else
		{
			$scope.entity.parentId = $scope.entity_1.id;
		}
		$scope.entity.typeId = $scope.typeId.id;
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询
					if ($scope.entity_3 != null)
					{
						$scope.findByParentId($scope.entity_3.id);
					}
					else if($scope.entity_2 != null)
					{
						$scope.findByParentId($scope.entity_2.id);
					}
					else
					{
						$scope.findByParentId($scope.entity_1.id);
					}
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.delete=function(){
		//获取选中的复选框			
		itemCatService.delete( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.list;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//根据父ID查询所有子分类
	$scope.findByParentId = function (id) {
		itemCatService.findByParentId(id).success(function (response) {
			$scope.list = response;
		});
	}

	//定义参数，记录当前分类处于级数
	$scope.grand = 1;

	//定义方法，每次调用级数加一
	$scope.loadChild = function (itemCat) {
		$scope.grand++;
		//当前分类属于第二级分类时，把itemCat赋值给第二级变量
		if ($scope.grand == 2)
		{
			$scope.entity_2 = itemCat;
		}
		//当前分类属于第三级分类时，把itemCat赋值给第三级变量
		else if ($scope.grand == 3)
		{
			$scope.entity_3 = itemCat;
		}
	}

	//定义3个变量，用于存储面包屑内容
	$scope.entity_1 = {"name" : "顶级分类","id" : 0};
	$scope.entity_2 = null;
	$scope.entity_3 = null;

});
