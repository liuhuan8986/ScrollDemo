package lh.scrolldemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private AutoLinearLayout rootView;
    private AutoLinearLayout rootView2;
    private AutoLinearLayout rootView3;
    private ScrollView scrollView;
    private ScrollView scrollView2;
    private ScrollView scrollView3;
    private Button start;
    private ImageView img;

    boolean isStop = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = (AutoLinearLayout) findViewById(R.id.rootView);
        rootView2 = (AutoLinearLayout) findViewById(R.id.rootView2);
        rootView3 = (AutoLinearLayout) findViewById(R.id.rootView3);
        scrollView = (ScrollView) findViewById(R.id.scrollView1);
        scrollView2 = (ScrollView) findViewById(R.id.scrollView2);
        scrollView3 = (ScrollView) findViewById(R.id.scrollView3);
        start = (Button) findViewById(R.id.start);
        img = (ImageView) findViewById(R.id.img);
        int height = getResources().getDisplayMetrics().heightPixels;
        Log.i(TAG,"height:"+height);

        rootView.setScrollFinishListener(new AutoLinearLayout.ScrollFinishListener() {
            @Override
            public void onFinish(View parent) {
                Toast.makeText(MainActivity.this, "完成1111", Toast.LENGTH_SHORT).show();
                View view = View.inflate(MainActivity.this,R.layout.view,null);
                if(!isStop){
                    rootView.start(view);
                }

            }
        });

        rootView2.setScrollFinishListener(new AutoLinearLayout.ScrollFinishListener() {
            @Override
            public void onFinish(View parent) {
                Toast.makeText(MainActivity.this, "完成222", Toast.LENGTH_SHORT).show();
                View view = View.inflate(MainActivity.this,R.layout.view,null);
                if(!isStop){
                    rootView.start(view);
                }
            }
        });

        rootView3.setScrollFinishListener(new AutoLinearLayout.ScrollFinishListener() {
            @Override
            public void onFinish(View parent) {
                Toast.makeText(MainActivity.this, "图文完成", Toast.LENGTH_SHORT).show();
                View view = View.inflate(MainActivity.this,R.layout.view,null);
                if(!isStop){
                    rootView.start(view);
                }
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.setPraentHeight(scrollView.getMeasuredHeight());
                TextView textView3 = new TextView(MainActivity.this);
                textView3.setText("这里是MyLinearLayout内容小于 scrollView高度");
                textView3.setBackgroundColor(Color.RED);
                rootView.start(textView3);


                rootView2.setPraentHeight(scrollView2.getMeasuredHeight());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1000);
                TextView textView4 = new TextView(MainActivity.this);
                textView4.setText("这里是 MyLinearLayout 的内容大于 scrollView高度，你没看到 整个黑色的TextView吗，TextView 的高度 1000啊，");
                textView4.setBackgroundColor(Color.BLACK);
                textView4.setTextColor(Color.WHITE);
                textView4.setLayoutParams(lp);
                rootView2.start(textView4);


                rootView3.setPraentHeight(scrollView3.getMeasuredHeight());

                View view = View.inflate(MainActivity.this,R.layout.view,null);
                rootView3.start(view);

            }
        });



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isStop = true;
        rootView.cancelAnimator();
        rootView2.cancelAnimator();
        rootView3.cancelAnimator();
    }
}
