package git.starstudio.eventbus;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Loser on 2017/3/26.
 * 这时测试删除粘性事件的demo
 */

public class StickyEvent extends AppCompatActivity {

    private TextView mTextView;
    private Button mButton;
    private ImageView mImageView;
    private Button mButton2;
    private Button mButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sticky);

        mTextView = (TextView) findViewById(R.id.textView);
        mButton = (Button) findViewById(R.id.button);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new MessageEvent("Hello everyone!"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText("");
                    }
                },1000);
                EventBus.getDefault().unregister(StickyEvent.this);
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().register(StickyEvent.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText("");
                    }
                },1000);
            }
        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageEvent stickyEvent = EventBus.getDefault().removeStickyEvent(MessageEvent.class);
                if (stickyEvent != null){
                    mButton2.setEnabled(false);
                    Toast.makeText(StickyEvent.this, "删除成功~~!", Toast.LENGTH_SHORT).show();
                }
                //先注销之前注册的EventBus
                EventBus.getDefault().unregister(StickyEvent.this);
                //此时紧接着注册一个EventBus，看一下是否还有之前的粘性事件触发
                EventBus.getDefault().register(StickyEvent.this);
            }
        });
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
        mTextView.setText(event.message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //注销EventBus
        EventBus.getDefault().unregister(this);
    }
}
