//kindeditor设置
var editorOption = {
	width : '100%',
	showRemote : false,
	allowImageUpload : true,
	uploadJson : '/imgUpload',
	items : [ 'source', '|', 'wordpaste', '|', 'fontname', 'fontsize', '|',
			'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
			'removeformat', '|', 'justifyleft', 'justifycenter',
			'justifyright', 'insertorderedlist', 'insertunorderedlist', '|',
			'emoticons', 'image', 'flash', 'media', 'link', 'unlink' ]
};

/**
 * 时间对象的格式化;
 */
Date.prototype.format = function(format) {
    /*
	 * eg:format="YYYY-MM-dd hh:mm:ss";
	 */
    var o = {
        "M+" :this.getMonth() + 1, // month
        "d+" :this.getDate(), // day
        "h+" :this.getHours(), // hour
        "m+" :this.getMinutes(), // minute
        "s+" :this.getSeconds(), // second
        "q+" :Math.floor((this.getMonth() + 3) / 3), // quarter
        "S" :this.getMilliseconds()
    // millisecond
    };

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
                .substr(4 - RegExp.$1.length));
    }

    for ( var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                    : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};

// 格式化时间yyyy-MM-dd
  function formatterDate(value){

      if(typeof(value)=='undefined' || $.trim(value)==''){
          return "";
      }  

      return new Date(value).format("yyyy-MM-dd"); 
}

// 格式化时间yyyy-MM-dd hh:mm:ss
  function formatterDateTime(value){

       if(typeof(value)=='undefined' || $.trim(value)==''){
          return "";
       }

     return new Date(value).format("yyyy-MM-dd hh:mm:ss"); 
  }
  
  
  
  // 转大写金额
  function convertMoney(numberValue){
         var numberValue=new String(Math.round(numberValue*100)); // 数字金额
         var chineseValue=""; // 转换后的汉字金额
         var String1 = "零壹贰叁肆伍陆柒捌玖"; // 汉字数字
         var String2 = "万仟佰拾亿仟佰拾万仟佰拾元角分"; // 对应单位
         var len=numberValue.length; // numberValue 的字符串长度
         var Ch1; // 数字的汉语读法
         var Ch2; // 数字位的汉字读法
         var nZero=0; // 用来计算连续的零值的个数
         var String3; // 指定位置的数值
         
         if(len>15){
            alert("超出计算范围");
            return "";
         }
         
         if (numberValue==0){
            chineseValue = "零元整";
            return chineseValue;
         }
         
         String2 = String2.substr(String2.length-len, len); // 取出对应位数的STRING2的值
         
         for(var i=0; i<len; i++){
        	 
             String3 = parseInt(numberValue.substr(i, 1),10); // 取出需转换的某一位的值
             if ( i != (len - 3) && i != (len - 7) && i != (len - 11) && i !=(len - 15) ){
                if ( String3 == 0 ){
                    Ch1 = "";
                    Ch2 = "";
                    nZero = nZero + 1;
                }else if ( String3 != 0 && nZero != 0 ){
                           Ch1 = "零" + String1.substr(String3, 1);
                           Ch2 = String2.substr(i, 1);
                           nZero = 0;
                 }else{
                           Ch1 = String1.substr(String3, 1);
                           Ch2 = String2.substr(i, 1);
                           nZero = 0;
                  }
            }else{ // 该位是万亿，亿，万，元位等关键位
                   if( String3 != 0 && nZero != 0 ){
                   Ch1 = "零" + String1.substr(String3, 1);
                   Ch2 = String2.substr(i, 1);
                   nZero = 0;
                   }else if ( String3 != 0 && nZero == 0 ){
                          Ch1 = String1.substr(String3, 1);
                          Ch2 = String2.substr(i, 1);
                          nZero = 0;
                    }else if( String3 == 0 && nZero >= 3 ){
                             Ch1 = "";
                             Ch2 = "";
                             nZero = nZero + 1;
                          }else{
                             Ch1 = "";
                             Ch2 = String2.substr(i, 1);
                             nZero = nZero + 1;
                          }
                   
                   if( i == (len - 11) || i == (len - 3)){ // 如果该位是亿位或元位，则必须写上
                       Ch2 = String2.substr(i, 1);
                    }
                }
             
                 chineseValue = chineseValue + Ch1 + Ch2;
          }
         
            if ( String3 == 0 ){ // 最后一位（分）为0时，加上“整”
                   chineseValue = chineseValue + "整";
            }
            
            return chineseValue;
  }
  
  //两端去空格函数
  String.prototype.trim = function() {
  	return this.replace(/(^\s*)|(\s*$)/g, "");
  };