package git.starstudio.eventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Loser on 2017/3/26.
 * 这是优先级的代码
 */


public class MainActivity extends AppCompatActivity {
    private int i = 0;
    private TextView mTextView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);
        mButton = (Button) findViewById(R.id.button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setText("");
                EventBus.getDefault().post(new MessageEvent("测试"));
            }
        });
    }

    @Subscribe(priority = 2,threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        mTextView.append(event.message+"========>priority = 2   " + i++ +"\n");
    }

    @Subscribe(priority = 1)
    public void onEvent(MessageEvent event) {
        mTextView.append(event.message+"========>priority = 1   " + i++ +"\n");
        EventBus.getDefault().cancelEventDelivery(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent2(MessageEvent event) {
        mTextView.append(event.message+"========>priority = 0   " + i++ +"\n");
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
