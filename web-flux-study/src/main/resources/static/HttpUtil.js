var httpUtil = {};
httpUtil.dealAjaxRequest4SimpleParam = function(serverURL,simpleJsonData){//异步操作
    var defer = $.Deferred();
    var option = {
        url:serverURL,
        type: 'POST',
        timeout : 100000, //超时时间设置，单位毫秒
        data:simpleJsonData,
        dataType:'json',
        error: function (err) {
            defer.reject(err) ;
        },
        success:function (result) {
            defer.resolve(result);
        }
    };
    $.ajax(option); //发送ajax请
    return defer.promise() ;
}
//使用contentType:'application/json'后，
// data必须json字符串，且后台必须使用@RequestBody接收数据
httpUtil.dealAjaxRequest4JSObj = function(serverURL,jsObjData){//异步操作
    var defer = $.Deferred();
    var option = {
        contentType:'application/json' ,
        url:serverURL,
        type: 'POST',
        timeout : 100000, //超时时间设置，单位毫秒
        data:JSON.stringify(jsObjData),
        dataType:'json',
        error: function (err) {
            defer.reject(err) ;
        },
        success:function (result) {
            defer.resolve(result);
        }
    };
    $.ajax(option); //发送ajax请
    return defer.promise() ;
}