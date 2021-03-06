package com.xpf.p2p.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xpf.p2p.R;
import com.xpf.p2p.common.BaseActivity;
import com.xpf.p2p.fragment.HomeFragment2;
import com.xpf.p2p.fragment.InvestFragment;
import com.xpf.p2p.fragment.MeFragment;
import com.xpf.p2p.fragment.MoreFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_invest)
    RadioButton rbInvest;
    @BindView(R.id.rb_assets)
    RadioButton rbAssets;
    @BindView(R.id.rb_more)
    RadioButton rbMore;
    @BindView(R.id.rg_main)
    RadioGroup rgMain;

    private HomeFragment2 homeFragment;
    private InvestFragment investFragment;
    private MeFragment meFragment;
    private MoreFragment moreFragment;
    private FragmentTransaction transaction;
    private static final int MESSAGE_BACK = 1;

    private boolean isFlag = true;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_BACK:
                    isFlag = true; // 在2s时,恢复isFlag的变量值
                    break;
            }
        }
    };

    @Override
    protected void initData() {
        // 默认选中“首页”
        rgMain.check(R.id.rb_home); // 选中按键
        setSelect(0); // 选中视图
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.iv_back, R.id.iv_setting, R.id.rb_home, R.id.rb_invest, R.id.rb_assets, R.id.rb_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.iv_setting:
                ivSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                    }
                });
                break;
            case R.id.rb_home:
                setSelect(0);
                break;
            case R.id.rb_invest:
                setSelect(1);
                break;
            case R.id.rb_assets:
                setSelect(2);
                break;
            case R.id.rb_more:
                setSelect(3);
                break;
        }
    }

    /**
     * 提供相应的fragment的显示
     * 如何在activity中动态加载fragment
     */
    private void setSelect(int i) {

        transaction = this.getSupportFragmentManager().beginTransaction();

        hideFragment();

        if (i == 0) {
            tvTitle.setText("首页");
            ivBack.setVisibility(View.GONE);
            ivSetting.setVisibility(View.GONE);
            if (homeFragment == null) {
                homeFragment = new HomeFragment2();
                transaction.add(R.id.fl_main, homeFragment);
            }
            transaction.show(homeFragment);
        } else if (i == 1) {
            tvTitle.setText("投资");
            ivSetting.setVisibility(View.GONE);
            ivBack.setVisibility(View.GONE);
            if (investFragment == null) {
                investFragment = new InvestFragment();
                transaction.add(R.id.fl_main, investFragment);
            }
            transaction.show(investFragment);
        } else if (i == 2) {
            tvTitle.setText("我的资产");
            ivSetting.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.GONE);
            if (meFragment == null) {
                meFragment = new MeFragment();
                transaction.add(R.id.fl_main, meFragment);
            }
            transaction.show(meFragment);
        } else if (i == 3) {
            tvTitle.setText("更多");
            ivSetting.setVisibility(View.GONE);
            ivBack.setVisibility(View.GONE);
            if (moreFragment == null) {
                moreFragment = new MoreFragment();
                transaction.add(R.id.fl_main, moreFragment);
            }
            transaction.show(moreFragment);
        }
        transaction.commit();// 提交事务
    }

    // 隐藏所有的Fragment的显示
    private void hideFragment() {

        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (investFragment != null) {
            transaction.hide(investFragment);
        }
        if (meFragment != null) {
            transaction.hide(meFragment);
        }
        if (moreFragment != null) {
            transaction.hide(moreFragment);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && isFlag) {
            isFlag = false;
            Toast.makeText(MainActivity.this, "再点击一次返回键退出应用", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(MESSAGE_BACK, 2000);
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 保证在activity退出前,移除所有未被执行的消息和回调方法,避免出现内存泄漏!
        handler.removeCallbacksAndMessages(null);
    }
}
