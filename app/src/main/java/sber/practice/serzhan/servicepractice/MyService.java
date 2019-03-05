package sber.practice.serzhan.servicepractice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {

    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_SET_VALUE = 3;
    private List<Messenger> mClients = new ArrayList<>();
    private Messenger mMessenger = new Messenger(new IncomingHandler());
//    private IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
        sendToClients();
    }

    private void sendToClients() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        Message msg = new Message();
                        msg.obj = System.currentTimeMillis();
                        for (Messenger client : mClients) {
                            try {
                                client.send(msg);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

//    public class LocalBinder extends Binder {
//        MyService getService(){
//            return MyService.this;
//        }
//    }

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_SET_VALUE:
                    break;
            }
        }
    }

    public static final Intent newIntent(Context context) {
        return new Intent(context, MyService.class);
    }
}
