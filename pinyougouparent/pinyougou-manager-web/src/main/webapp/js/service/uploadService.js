//文件上传
app.service("uploadService", function ($http) {

    //文件上传方法
    this.uploadFile = function () {


        //H5支持的表单打包，FormData对象可以组装一组用XMLHttpRequest发送请求的键值对，可以用于发送表单数据
        //默认当前js所在页面的表单对象,若有多个可以使用有参构造方法，参数：form对象（使用document.querySelector("name")）
        var formData = new FormData();

        //FormData表单添加数据
        //file.file[0]表示当前js所在页面表单中type="file"第一个文件
        formData.append("file", file.files[0]);

        //提交请求
        return $http({
            method : "POST",
            url : "/upload.do",
            data : formData,
            headers : {"Content-Type" : undefined},  //定义表单enctype="multipart/form-data"
            transformRequest : angular.identity  //表单序列化
        });
    }
});