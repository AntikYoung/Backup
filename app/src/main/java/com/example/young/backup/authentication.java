package com.example.young.backup;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class authentication extends FragmentActivity {

    //TextPassPopWindow mpop;
    private PassValitationPopwindow mPopup;
    private  NewFragment newFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
//				mPop = new TextPassPopWindow(MainActivity.this, itemsOnClick);
//				  //显示窗口
//				mPop.showAtLocation(MainActivity.this.findViewById(R.id.main),
//						Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置

                new PassValitationPopwindow(MainActivity.this,1,findViewById(R.id.main),new OnInputNumberCodeCallback() {

                    @Override
                    public void onSuccess() {

                    }
                });

            }
        });

        if (newFragment == null)
        {
            newFragment = new NewFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_fragment, newFragment).commit(); // 将左菜单默认VIEW替换为左菜单Fragment


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.backup:

            case R.id.restore:

            case R.id.authentication:

            case R.id.back:
        }
    }

}
