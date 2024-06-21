

export default class CommonConstants {

  //服务器IP地址
  // static readonly REQUEST_HOST: string = '192.168.151.230';
  static readonly REQUEST_HOST: string = '10.21.75.132';
  //服务器端口
  static readonly REQUEST_PORT: string = '8008';

  /**
   * 后台服务器IP和端口
   * 本地的模拟器终究是以一个模拟器的身份去访问电脑的地址，所以本地测试时需要用局域网IP
   */
  static readonly REQUEST_PREFIX: string = 'http://' + CommonConstants.REQUEST_HOST + ':' + CommonConstants.REQUEST_PORT;

  /**
   * 未做功能提示
   */
  static readonly UN_DO_TIPS = '该功能正在开发中，敬请期待！';

  /**
   * Preferences存储用户信息的key
   */
  static readonly LOGIN_USER_ID: string = 'loginUserId';

  /**
   * Preferences存储用户token的key
   */
  static readonly LOGIN_TOKEN: string = 'boboToken';

  /**
   * Preferences global data key
   */
  static readonly H_STORE: string = 'dataStoreBobo';

  /**
   * 分割线颜色
   */
  static readonly LINE = '#ffc8c1c1';

  /**
   * 按钮默认背景颜色
   */
  static readonly DEFAULT_BUTTON_BG_COLOR = '#e8483c';

  /**
   * 按钮统一背景颜色
   */
  static readonly BUTTON_BG_COLOR = '#ff0620bf';
}