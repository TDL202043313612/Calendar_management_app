// 根据id、class、tag获取第一个匹配到的值
function getDom(selector, type = 'id') {
  let getDomBy = {
    id(selector) {
      return document.getElementById(selector);
    },
    className(selector) {
      return document.getElementsByClassName(selector)[0];
    },
    tagName(selector) {
      return document.getElementsByTagName(selector)[0];
    }
  };
  return getDomBy[type](selector);
}

const _charStr = 'abacdefghjklmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ0123456789';

/**
 * 随机生成字符串
 * @param len 指定生成字符串长度
 */
function getRandomString(len) {
  let min = 0, max = _charStr.length-1, _str = '';
  //判断是否指定长度，否则默认长度为15
  len = len || 15;
  //循环生成字符串
  for(var i = 0, index; i < len; i++){
    index = RandomIndex(min, max, i);
    _str += _charStr[index];
  }
  return _str;
}

/**
 * 随机生成索引
 * @param min 最小值
 * @param max 最大值
 * @param i 当前获取位置
 */
function RandomIndex(min, max, i){
  let index = Math.floor(Math.random()*(max-min+1)+min),
    numStart = _charStr.length - 10;
  //如果字符串第一位是数字，则递归重新获取
  if(i==0&&index>=numStart){
    index = RandomIndex(min, max, i);
  }
  //返回最终索引值
  return index;
}

// echarts的title样式
const defaultEchartsOpt = {
  title: {
    text: "",
    left: "center",
    top: 20,
    textStyle: {
      color: "#666"
    }
  }
}

export default {
  getDom,
  defaultEchartsOpt,
  getRandomString
}

/**
 * 按指定格式-格式化时间 如：new Date().Format("yyyy-MM-dd hh:mm:ss");
 * hasweek用来表示是否显示星期
 */
Date.prototype.Format = function (fmt, hasweek) {

  var weekday = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
  var o = {
    "M+": this.getMonth() + 1,
    "d+": this.getDate(),
    "h+": this.getHours(),
    "m+": this.getMinutes(),
    "s+": this.getSeconds(),
    "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
    "S": this.getMilliseconds() // 毫秒
  };
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));

  return fmt + (hasweek ? "&nbsp;&nbsp;&nbsp;&nbsp;" + weekday[this.getDay()] : "");
};
