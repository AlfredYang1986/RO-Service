/**
 * Created by liwei on 2017/6/14.
 */

/**
 **********************************************************
 * js实现账号登入
 **********************************************************
 */
$(function () {
    $('#myprofile').click(function () {
        myProfile();
    })
});


var login = function () {
    try{
        var dataMap = JSON.stringify({
            "user_name" : $("#name").val(),
            "pwd" : $("#password").val()
        });

        AjaxData("/auth/password", dataMap, "POST", function(data){
            if(data.status == "error"){
                $("#password").focus();
                PetAlert(getMessageByName('login account and password do not match'));
            }else{
                login_im($("#name").val(),$("#password").val(),data.result);
            }
        }, function(e){
            throw new PetException(e.name);
        });
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}

/**
 **********************************************************
 * js实现账号登出清楚cookie和session
 **********************************************************
 */
var logout = function () {
    try{
        window.location="/"
    } catch(err) {
        PetAlert(getMessageByName('logout failed'));
    }
}

/**
 **********************************************************
 * js实现个人资料展示
 **********************************************************
 */
var myProfile = function () {
    try{
        $("#myprofile-form").modal('show');
        $('#user_form')[0].reset();
        $('#company').text($.cookie('company'));
        $('#department').text($.cookie('department'));
    } catch(err) {
        PetAlert(getMessageByName('the user information request failed'));
    }
}

/**
 **********************************************************
 * js实现登录验证
 * @returns {boolean}
 **********************************************************
 */
var loginauthentication = function () {
    var flag = true
    var user_name = $("#name").val()
    console.info(user_name)
    if(user_name == ""){
        $.tooltip('请输入账号');
        $("#name").focus();
        flag = false
    }else{
        var pwd = $("#password").val()
        if(pwd == ""){
            $.tooltip('请输入密码');
            $("#password").focus();
            flag = false
        }
    }
    return flag
}

/**
 **********************************************************
 * js实现清除所有cookie函数
 **********************************************************
 */
var clearAllCookie = function() {
    var cookie = document.cookie.split(';');

    for (var i = 0; i < cookie.length; i++) {

        var chip = cookie[i],
            entry = chip.split("="),
            name = entry[0];

        document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    }
}

/**
 **********************************************************
 * js实现金额‘千位分隔符’且保留整数的思路及代码（+优化版）
 * @param num 整数|小数
 * @returns {string}
 **********************************************************
 */
var thousandBitSeparatorParseInt = function(num) {
    return thousandBitSeparator(parseInt(num))
}

/**
 **********************************************************
 * js实现金额‘千位分隔符’的思路及代码（+优化版）
 * @param num 整数|小数
 * @returns {string}
 **********************************************************
 */
var thousandBitSeparator = function(num) {
    var decimal = String(num).split('.')[1] || '';//小数部分
    var tempArr = [];
    var revNumArr = String(num).split('.')[0].split("").reverse();//倒序
    for (i in revNumArr){
        tempArr.push(revNumArr[i]);
        if((i+1)%3 === 0 && i != revNumArr.length-1){
            tempArr.push(',');
        }
    }
    var zs = tempArr.reverse().join('');//整数部分
    return decimal?zs+'.'+decimal:zs;
}

/**
 **********************************************************
 * js实现将千分位数还原正常
 * @param str   千位分隔符字符串
 * @returns {Number}
 * *********************************************************
 */
var reductionNumber = function (str) {
    var arr = str.split(",")
    var newstr = ""
    for(var i in arr){
        newstr += arr[i]
    }
    return parseFloat(newstr)
}

/**
 **********************************************************
 * js实现小数保留位数
 * @param float 小数
 * @param fixed 保留几位
 * @returns {string}
 **********************************************************
 */
var floattoFixedN = function (float,fixed) {
    return parseFloat(float).toFixed(fixed)
}

/**
 **********************************************************
 * js实现将小数转换为百分数
 **********************************************************
 */
var doubleToPercentage = function (num) {
    return (parseFloat(num)*100).toFixed(1)+"%"
}

/**
 **********************************************************
 * js实现时间戳转换yyyy-MM-dd hh:mm:ss
 **********************************************************
 */
var formatDateTime = function(timeStamp) {
    var date = new Date();
    //date.setTime(timeStamp * 1000);
    date.setTime(timeStamp);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    var minute = date.getMinutes();
    var second = date.getSeconds();
    minute = minute < 10 ? ('0' + minute) : minute;
    second = second < 10 ? ('0' + second) : second;
    return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;
};