 //控制层 
app.controller('typeTemplateController' ,function($scope,$controller,typeTemplateService, brandService, specificationService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		typeTemplateService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	} ;
	
	//分页
	$scope.findPage=function(page,rows){			
		typeTemplateService.findPage(page,rows).success(
			function(response){
				$scope.list=response.list;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};
	
	//查询实体 
	$scope.findOne=function(id){				
		typeTemplateService.findOne(id).success(
			function(response){
				$scope.entity= response;
				//品牌转JSON
				$scope.entity.brandIds = angular.fromJson($scope.entity.brandIds);
				//规格转JSON
				$scope.entity.specIds = angular.fromJson($scope.entity.specIds);
				//扩展属性转JSON
				$scope.entity.customAttributeItems = angular.fromJson($scope.entity.customAttributeItems);
			}
		);				
	};
	
	//保存 
	$scope.save=function(){
		//alert(JSON.stringify($scope.entity))
		var serviceObject;//服务层对象
		if($scope.entity.id!=null){//如果有ID
			serviceObject=typeTemplateService.update( $scope.entity ); //修改
		}else{
			serviceObject=typeTemplateService.add( $scope.entity  );//增加
		}
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}
		);

	};
	
	 
	//批量删除 
	$scope.delete=function(){
		//获取选中的复选框			
		typeTemplateService.delete( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	};
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		typeTemplateService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.list;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};

	//准备select2的数据
	// $scope.brandList = {
	// 	data: [
	// 		{id:1,text:'华为'},
	// 		{id:2,text:'小米'}
	// 		]
	// 	// 其他配置略，可以去看看内置配置中的ajax配置
	// };

	//方法一：使用pojo添加getText()方法
	//缺点：如果有id、text之外的属性，如firstChar会参与格式化并存到数据库中
	$scope.brandList = [];
	$scope.findBrandList = function () {
		brandService.findAll().success(function (response) {
			$scope.brandList = {data:response};
		});
	};
	//方法二：使用Mapper封装id和text类，并返回List<Map>对象
	$scope.specificationList = [];
	$scope.findSpecificationList = function () {
		specificationService.findSpecificationList().success(function (response) {
			$scope.specificationList = {data:response};
		});
	};

	//创建集合，存储扩展属性
	$scope.entity = {customAttributeItems:[],specIds:[],brandIds:[]};

	//增加扩展属性
	$scope.addTableRow = function () {
		$scope.entity.customAttributeItems.push({});
	};

	//删除扩展属性
	$scope.deleteTableRow = function (index) {
		$scope.entity.customAttributeItems.splice(index, 1);
	}

	//将JSON数据拼接成一个字符串
	$scope.json2Str = function (jsonStr, key) {
		var json = angular.fromJson(jsonStr);
		var result = "";
		for (var i = 0; i < json.length; i++) {
			if(i !=0)
			{
				result += ",";
			}
			result += json[i][key];
		}
		return result;
	}
});	
