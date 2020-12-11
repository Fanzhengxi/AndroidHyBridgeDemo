var callbacks=new Array();
//1.jsCallAndroid
function jsCallAndroid(obj,method,params,callback){
    //保存callback
    var port=callbacks.length;
    callbacks[port]=callback;
    //组合出符合规则的URL,并传递给java层
    var url='JSBridge://'+obj+':'+port+'/'+method+'?'+JSON.stringify(params);
    window.prompt(url);


}
//2.onAndroidFinished
function onAndroidFinished(port,jsonObj){
//从callback取出对应的回调函数
    var callback=callbacks[port];
    callback(jsonObj);
    //从callbacks中删除callback
    delete callbacks[port];

}