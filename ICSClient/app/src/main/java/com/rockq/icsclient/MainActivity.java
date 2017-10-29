package com.rockq.icsclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rockq.icsclient.action.DeviceInfoList;
import com.rockq.icsclient.action.ICSInfoList;
import com.rockq.icsclient.bean.UserAlarmInformation;
import com.rockq.icsclient.bean.UserChannelValue;
import com.rockq.icsclient.bean.UserIcsCharacteristic;
import com.rockq.icsclient.bean.UserSwingCardRecord;
import com.rockq.icsclient.constant.VariableAsReceiveMessageServer;
import com.rockq.icsclient.domain.Device;
import com.rockq.icsclient.domain.ICS;
import com.rockq.icsclient.domain.VerifyCode;
import com.rockq.icsclient.eventBus.ReceiverDataEvent;
import com.rockq.icsclient.eventBus.UpdateEvent;
import com.rockq.icsclient.global.ReceiveServerGlobal;
import com.rockq.icsclient.messageHandler.MessageDisposeDao;
import com.rockq.icsclient.messageSend.SendMessageQueueService;
import com.rockq.icsclient.utils.AppLog;
import com.rockq.icsclient.utils.GetTime;
import com.rockq.icsclient.utils.HexConversion;
import com.rockq.icsclient.utils.InstructCreate;
import com.rockq.icsclient.utils.ListForReceiveMessage;
import com.rockq.icsclient.utils.ListForUserAlarmInformationCache;
import com.rockq.icsclient.utils.ListForUserChannelValueCache;
import com.rockq.icsclient.utils.ListForUserIcsCharacteristic;
import com.rockq.icsclient.utils.ListForUserSwingCardRecordCache;
import com.rockq.icsclient.utils.WebUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView mtvMessage;
    private EditText mEtMessage;
    private EditText mEtPort;
    private Context mContext;
    private boolean mSentAddressPort;
    private boolean mReceiveMessage;
    private InputStream inStream;
    private DataOutputStream outStream;
    private String targetIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mtvMessage = findViewById(R.id.tv_message);
        mEtPort = findViewById(R.id.et_port);
        mEtMessage = findViewById(R.id.et_message);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onSubcribe(UpdateEvent event) {
        if (event != null) {
            mtvMessage.setText(String.format(Locale.CHINA, "%s\n    [%s]%s", mEtMessage.getText(), new SimpleDateFormat("yy-MM-dd dd:mm:ss", Locale.CHINA).format(new Date()), event.getMsg()));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecevierData(ReceiverDataEvent event) {
        if (event != null) {
            // 消息获取后读取消息
            if (ListForReceiveMessage.getInstance().getListSize() < 1)
                return;
            // 处理消息队列最前一条消息
            MessageDisposeDao messageDisposeDao = new MessageDisposeDao();
            for (int i = 0; i < ListForReceiveMessage.getInstance().getListSize(); i++) {
                messageDisposeDao.disposeMessage((String) ListForReceiveMessage.getInstance().getReceiveMessage(), (String) ListForReceiveMessage.getInstance().getReceiveMessage());
            }
            //

            StringBuilder str = new StringBuilder();
            if (ICSInfoList.getInstance().getListSize() > 0) {
                List<ICS> icsInfo = ICSInfoList.getInstance().getICSInfo();
                AppLog.d("收到数据---->" + icsInfo.toString());
                str.append(icsInfo.toString()).append("\n");
            }
            if (DeviceInfoList.getInstance().getDeviceSize() > 0) {
                List<Device> devices = DeviceInfoList.getInstance().getDevices();
                str.append(devices.toString()).append("\n");
            }
            if (VerifyCode.verifyResult == 1) {
                str.append("验证通过").append("\n");
            } else if (VerifyCode.verifyResult == 0) {
                str.append("验证失败").append("\n");
            }
            if (!TextUtils.isEmpty(str.toString()))
                mtvMessage.setText(str.toString());
            //消息数据对应
            //解析后的消息显示
            //消息界面显示
        }
    }


    public void createPort(View view) {
        if (mSentAddressPort)
            return;
        mSentAddressPort = true;
        new Thread(new udpAsIp(WebUtils.getByeIpAddress(mContext))).start();

    }

    public void stopSendIp(View view) {
        mSentAddressPort = false;
    }

    public void receiveMessage(View view) {
        if (mReceiveMessage)
            return;
        mReceiveMessage = true;
        try {
            server = new ServerSocket(8553);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new GetLogTask().execute();
//        server = new ServerSocket(8553);
//        Socket client = server.accept();
//        new Thread(new SocketServer(client)).start();


    }

    public void stopReceive(View view) {
        mReceiveMessage = false;
    }

    public void sendMessage(View view) {
        SendMessageQueueService sendMessageQueueService = new SendMessageQueueService();
        sendMessageQueueService.addSendNews("192.168.3.145", InstructCreate.sendICSGetDeviceCode1());
        sendMessageQueueService.send();
    }

    /**
     * 得到连接ip
     */
    public String targetIp(Socket client) {
        InetAddress ip = client.getInetAddress();
        return ip.toString().substring(1);
    }

    /**
     * 数据接收
     *
     * @return
     */
    public InputStream receiveInputStream(Socket client, InputStream in_stream) {
        try {
            return in_stream = client.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数据发送
     */
    public void upgradeDataOutput(String outPut) {
        outPut = outPut.toUpperCase();
        // Socket DataOutput。write发送编码默认为ascll，故转为字节数组发送16进制报文
        byte instruction[] = HexConversion.hexStringToByteArray(outPut);
        AppLog.d(Arrays.toString(instruction));
        try {
            outStream.write(instruction);
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空存储接收数据byte数组
     */
    public void cleanReceiveMessageByte(byte[] messageByte) {
        for (int i = 0; i < VariableAsReceiveMessageServer.receiveByteSize; i++) {
            messageByte[i] = 0;
        }
    }

    private ServerSocket server = null;
    private Socket client = null;


    @SuppressLint("StaticFieldLeak")
    public class GetLogTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... param) {
            try {
                Socket client = server.accept();
                // 获得连接端ip
                AppLog.d("打开次数----" + in++);
                targetIP = targetIp(client);
                AppLog.d("targetIp" + targetIP);
                try {
                    outStream = new DataOutputStream(client.getOutputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 接收完毕且数据完整，则flag为false；否则为true
                boolean flag = true;
                while (flag) {
                    try {
                        inStream = receiveInputStream(client, inStream);
                        // 接收数据完整性判断，通过接收字节长度receiveSize
                        int receiveSize = inStream.read(receiveByte);
                        if (receiveSize == ReceiveServerGlobal.ICS_SEND_SIZE) {
                            // 暂定返回ics序列号+功能码字符串...
                            upgradeDataOutput(
                                    // 保存连接ip及对发送收数据
                                    ListForReceiveMessage.getInstance().addReceiveMessage(
                                            targetIP, receiveByte));
                            AppLog.d(GetTime.nowTime() + " 返回成功，接收关闭指令。。。。。。");
                            // 接收关闭指令
                            if (returnClose(client, inStream, judgeByte) == 1) {
                                flag = false;
                                AppLog.d("客户端:" + client + "数据传输成功！");
                                AppLog.d(GetTime.nowTime() + " 接收关闭指令。。。。。。");
                            } else {
                                // 异常返回FFFFFFFF字符串...
                                upgradeDataOutput("FFFF");
                                flag = false;
                                System.err.println("客户端:" + client + "本次数据传输异常！");
                            }
                        } else {
                            // 异常返回FFFFFFFF字符串...
                            upgradeDataOutput("FFFF");
                            flag = false;
                            System.err.println("客户端:" + client + "本次数据传输异常！");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // 清空存储接收数据数组
                cleanReceiveMessageByte(receiveByte);
                cleanJudgeMessageByte(judgeByte);
                // 关闭连接
                AppLog.d("开始关闭------->");
                try {
                    client.shutdownOutput();
                    client.close();
                    inStream.close();
                    outStream.close();
                    AppLog.d("关闭结束-->：" + client);
                    AppLog.d("关闭次数----" + out++);
                    mHandler.sendEmptyMessage(1);

                } catch (IOException e) {
                    e.printStackTrace();
                    AppLog.d("关闭异常-->：" + client);
                    mHandler.sendEmptyMessage(1);

                }
//                server = new ServerSocket(8553);
//                while (true) {
//                    try {
//                if (server != null) {
//                Socket client = server.accept();

//                AppLog.d("-----------------------------------------------------------------------------------------");
//                AppLog.d("客户端连接成功：" + client);
//                new Thread(new SocketServer(client)).start();

//                } else {
//                    Thread.sleep(1000);
//                }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }

            } catch (Exception ex) {
                ex.printStackTrace();
                AppLog.d("程序异常-->：" + client);
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
//            new GetLogTask().execute();

        }
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1)
                new GetLogTask().execute();
        }
    };

    private int in = 0;
    private int out = 0;
    private int outEx = 0;
    private byte receiveByte[] = new byte[VariableAsReceiveMessageServer.receiveByteSize];


    private byte judgeByte[] = new byte[ReceiveServerGlobal.JUDGE_BYTEARRAY_SIZE];

    /**
     * 清空关闭接收数据byte数组
     */
    public void cleanJudgeMessageByte(byte[] judgeByte) {
        for (int i = 0; i < ReceiveServerGlobal.JUDGE_BYTEARRAY_SIZE; i++) {
            judgeByte[i] = 0;
        }
    }

    /**
     * 数据接收
     *
     * @return 确认关闭信息
     */
    public int returnClose(Socket client, InputStream in_stream, byte[] judgeByte) {
        try {
            client.getInputStream().read(judgeByte);
            AppLog.d("收到关闭指令集：----" + Arrays.toString(judgeByte));
            if (HexConversion.byteArrayToStringHex(judgeByte).equals(ReceiveServerGlobal.JUDGE_MESSAGE_SIGN)) {
                return 1;
            } else {
                return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public class udpAsIp implements Runnable {
        private DatagramSocket socket;
        private DatagramPacket packet;
        private byte[] protocol = {(byte) 95, (byte) 00, (byte) 00, (byte) 00};
        private byte[] data = {(byte) 95, (byte) 00, (byte) 00, (byte) 00, (byte) 192, (byte) 168, (byte) 3, (byte) 182,};

        public udpAsIp() {
        }

        public udpAsIp(byte[] data) {
            if (data != null && data.length != 0) {
                byte[] bytes = Arrays.copyOf(protocol, protocol.length + data.length);
                System.arraycopy(data, 0, bytes, protocol.length, data.length);
                AppLog.d(Arrays.toString(bytes));
                this.data = bytes;
            }
        }

        public void run() {
            try {
                socket = new DatagramSocket();
                socket.setBroadcast(true);
                packet = new DatagramPacket(data, data.length, InetAddress.getByName("192.168.3.255"), 8333);
                AppLog.d("UdpServiceThread Startup Successful！");
                socket.send(packet);
                AppLog.d("地址发先送中…………");
                while (mSentAddressPort) {
                    Thread.sleep(30000);
                    socket.send(packet);
                    AppLog.d("地址发先送中…………");
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
    //    /**
//     * 接收消息处理
//     */
//    public class SocketServer implements Runnable {
//        private Socket client = null;
//        private byte receiveByte[] = new byte[VariableAsReceiveMessageServer.receiveByteSize];
//
//        /**
//         * 传入客户端client对象
//         */
//        public SocketServer(Socket client) {
//            this.client = client;
//        }
//
//        public void run() {
//            // 获得连接端ip
//            AppLog.d("打开次数----" + in++);
//            targetIP = targetIp(client);
//            AppLog.d("targetIp" + targetIP);
//            sendMessage();
//            try {
//                outStream = new DataOutputStream(client.getOutputStream());
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            // 接收完毕且数据完整，则flag为false；否则为true
//            boolean flag = true;
////            try {
////                DataInputStream input = new DataInputStream(client.getInputStream());
////                byte[] b = new byte[10000];
////                while (true) {
////                    int length = input.read(b);
////                    String Msg = new String(b, 0, length);
////                    Log.v("data", Msg);
////                }
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//
////            while (flag) {
////                try {
////                    inStream = client.getInputStream();
////                    // 接收数据完整性判断，通过接收字节长度receiveSize
////                    int receiveSize = inStream.read(receiveByte);
////
//////                    if (receiveSize != -1) {
//////                        upgradeDataOutput(
//////                                // 保存连接ip及对发送收数据
//////                                ListForReceiveMessage.getInstance().addReceiveMessage(targetIP, receiveByte)
//////                        );
//////
//////                        flag = false;
//////                        AppLog.d("客户端:" + client + "数据传输成功！");
//////                    } else {
//////                        // 暂定返回空字符串...
//////                        upgradeDataOutput("FFFF");
//////                        flag = false;
//////                        System.err.println("客户端:" + client + "本次数据传输异常！");
//////                    }
////                    if (receiveSize == VariableAsReceiveMessageServer.targetSize) {
////                        // 暂定返回ics序列号+功能码字符串...
////                        upgradeDataOutput(
////                                // 保存连接ip及对发送收数据
////                                ListForReceiveMessage.getInstance().addReceiveMessage(targetIP, receiveByte)
////                        );
////                        flag = false;
////                        AppLog.d("客户端:" + client + "数据传输成功！");
////                    } else {
////                        // 暂定返回空字符串...
////                        upgradeDataOutput("FFFF");
////                        flag = false;
////                        System.err.println("客户端:" + client + "本次数据传输异常！");
////                    }
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
//            while (flag) {
//                try {
//                    inStream = receiveInputStream(client, inStream);
//                    // 接收数据完整性判断，通过接收字节长度receiveSize
//                    int receiveSize = inStream.read(receiveByte);
//                    if (receiveSize == ReceiveServerGlobal.ICS_SEND_SIZE) {
//                        // 暂定返回ics序列号+功能码字符串...
//                        upgradeDataOutput(
//                                // 保存连接ip及对发送收数据
//                                ListForReceiveMessage.getInstance().addReceiveMessage(
//                                        targetIP, receiveByte));
//                        AppLog.d(GetTime.nowTime() + " 返回成功，接收关闭指令。。。。。。");
//                        // 接收关闭指令
//                        if (returnClose(client, inStream, judgeByte) == 1) {
//                            flag = false;
//                            AppLog.d("客户端:" + client + "数据传输成功！");
//                            AppLog.d(GetTime.nowTime() + " 接收关闭指令。。。。。。");
//                        } else {
//                            // 异常返回FFFFFFFF字符串...
//                            upgradeDataOutput("FFFF");
//                            flag = false;
//                            System.err.println("客户端:" + client + "本次数据传输异常！");
//                        }
//                    } else {
//                        // 异常返回FFFFFFFF字符串...
//                        upgradeDataOutput("FFFF");
//                        flag = false;
//                        System.err.println("客户端:" + client + "本次数据传输异常！");
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            // 清空存储接收数据数组
//            cleanReceiveMessageByte(receiveByte);
//            cleanJudgeMessageByte(judgeByte);
//            // 关闭连接
//            try {
//                client.shutdownOutput();
//                client.close();
//                inStream.close();
//                outStream.close();
//                AppLog.d("连接关闭：" + client);
////                new Thread(new SocketServer(client = server.accept())).start();
//                AppLog.d("关闭次数----" + out++);
////                AppLog.d(client.isClosed() ? "关闭成功" : "关闭失败");
//                if (client.isClosed()) {
//                    Socket client = server.accept();
////                    AppLog.d("-----------------------------------------------------------------------------------------");
////                    AppLog.d("客户端连接成功：" + client);
//                    new Thread(new SocketServer(client)).start();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
////                new Thread(new SocketServer(client)).start();
//                AppLog.d("关闭异常次数----" + outEx++);
//                Socket client = null;
//                try {
////                    client = server.accept();
////                    AppLog.d("-----------------------------------------------------------------------------------------");
////                    AppLog.d("客户端连接成功：" + client);
//                    new Thread(new SocketServer(server.accept())).start();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//
//            }
//        }
//    }

}
