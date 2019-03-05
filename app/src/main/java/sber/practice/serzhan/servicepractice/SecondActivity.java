package sber.practice.serzhan.servicepractice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private Button stopServiceButton;
    private TextView text;
//    private MyService mBoundService;
    private Messenger mService;
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initViews();
        initListeners();
        init();
    }

    private void initViews() {
        stopServiceButton = findViewById(R.id.stop_button);
        text = findViewById(R.id.textView);
    }

    private void initListeners() {
        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unBindService();
            }
        });
    }

    private void init() {
        bindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unBindService();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            mBoundService = ((MyService.LocalBinder)service).getService();
            mService = new Messenger(service);
            Message msg = Message.obtain(null, MyService.MSG_REGISTER_CLIENT);
            msg.replyTo = mMessenger;
            try {
                mService.send(msg);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v("SecondActivity", "disconnected");
        }
    };

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            text.setText(msg.obj.toString());
        }
    }

    public void bindService() {
        bindService(MyService.newIntent(SecondActivity.this),
                    mServiceConnection,
                    Context.BIND_AUTO_CREATE);
    }

    public void unBindService() {
        Message msg = Message.obtain(null,
                MyService.MSG_UNREGISTER_CLIENT);
        msg.replyTo = mMessenger;
        try{
            mService.send(msg);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        unbindService(mServiceConnection);
    }

    public static final Intent newIntent(Context context) {
        return new Intent(context, SecondActivity.class);
    }
}
