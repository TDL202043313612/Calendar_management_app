/**
 * WebSocket全局处理
 */
import io from 'socket.io-client';
import {MessageBox} from 'element-ui';
import store from '@/store';
import { getUserId, removeToken } from '@/utils/auth'

let socket = null;

/**
 * 初始化websocket
 */
export function webSocketInit() {
    // socketIo连接的服务器信息，就是我们后端配置的信息
    socket = getSocket();
    // socket.emit("push_event", {userId: userIdStr});//向后端推送数据
    socket.on('connect', function () {
        console.log('websocket 连接成功');
    });

    /**
     * 监听后台的 push_system_message_event 事件
     */
    socket.on('push_system_message_event', function (data) {
        MessageBox.alert(data.content, '系统实时消息通知：', {
                confirmButtonText: '确定',
            }
        ).then(() => {
        }).catch(() => {
        });
    });

    /**
     * 监听后台的 push_online_count_event 事件
     */
    socket.on('push_online_count_event', function (data) {
        // console.log("后台值onlineCount = "+data.onlineCount);
        //将在线人数设置到 store
        store.commit('SET_ONLINE_USER_COUNT', data.onlineCount);
    });

    /**
     * 监听后台的 push_loginout_event 事件(被管理员强迫下线)
     */
    socket.on('push_loginout_event', function (data) {
        //被强迫下线
        if(socket != null){
            socket.emit("user_login_out", {userId: getUserId()});//向后端推送数据
        }
        removeToken();
        if(data.userId){
            //提示框
            MessageBox.alert('您已被强制下线', '完犊子啦', {
                    confirmButtonText: '确定',
                    type: 'error',
                }
            ).then(() => {
                location.href = '/';
            }).catch(() => {
                location.href = '/';
            });
        }
    });
};

/**
 * 下线操作
 */
export function webSocketDisconnect() {
    if(socket != null){
        socket.emit("user_login_out", {userId: getUserId()});//向后端推送数据
    }
};

/**
 * 获取socket对象
 */
export function getSocket() {
    if(socket){
      return socket;
    }else{
      let opts = {
        query: 'userId=' + getUserId(),
      };
      // socket = io.connect('http://127.0.0.1:8017', opts); //本地服务器IP地址
      // socket = io.connect('http://124.222.59.129:8038', opts); //腾讯云222服务器
      socket = io.connect('http://10.85.246.210:8038', opts); //张总服务器
      return socket;
    }
};
