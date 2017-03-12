package com.idisfkj.loadingcirclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.idisfkj.loadingcirclerview.common.LoadingCirclerView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements LoadingCirclerView.PeriodFinishListener {

    private LoadingCirclerView mLoadingCircleView;
    private ImageView mLoadingImage;
    private AnimationSet mSetAnimation1;
    private AnimationSet mSetAnimation2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setupAnimation();
        show();
    }

    private void initView() {
        mLoadingCircleView = (LoadingCirclerView) findViewById(R.id.loading_circle_view);
        mLoadingImage = (ImageView) findViewById(R.id.loading_image_view);
    }

    private void setupAnimation() {
        //设置动画圆弧的宽度
        mLoadingCircleView.setCircleWidth(5);
        //设置UI更新的递减量
        mLoadingCircleView.setDrawIntervalTimeTDecrement(10);
        //设置画圆弧的百分比增量
        mLoadingCircleView.setCirclerPercentIncremental(10);
        //设置画完一次圆弧的周期性监听
        mLoadingCircleView.setPeriodFinishListener(this);
        mSetAnimation1 = new AnimationSet(true);

        //放大透明
        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimation1.setDuration(550);
        mSetAnimation1.addAnimation(alphaAnimation1);

        ScaleAnimation scaleAnimation2 = new ScaleAnimation(1.05f, 1.0f, 1.05f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation2.setDuration(550);
        mSetAnimation1.addAnimation(scaleAnimation2);
        mSetAnimation1.setFillAfter(true);

        //缩放透明
        mSetAnimation2 = new AnimationSet(true);
        AlphaAnimation alphaAnimation2 = new AlphaAnimation(1.0f, 0.2f);
        alphaAnimation2.setDuration(100);
        ScaleAnimation scaleAnimation3 = new ScaleAnimation(1.0f, 0.6f, 1.0f, 0.6f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation3.setDuration(100);
        mSetAnimation2.addAnimation(alphaAnimation2);
        mSetAnimation2.addAnimation(scaleAnimation3);
        mSetAnimation2.setFillAfter(true);

        scaleAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //4:一个周期动画结束后，执行缩放透明，返回到初始化状态，
                // 这里延迟650毫秒，因为固定圆形显示时间为1200毫秒 需再减去前面的放大透明动画的执行时间550毫秒
                mSetAnimation2.setStartOffset(650);
                mLoadingImage.startAnimation(mSetAnimation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void show() {
        //1.重置，开启动画
        mLoadingImage.setVisibility(VISIBLE);
        mLoadingCircleView.setVisibility(VISIBLE);
        mLoadingCircleView.setReset();
        mLoadingCircleView.startAnimation();
        mLoadingCircleView.invalidate();

        //2.缩放动画推辞200毫秒执行
        mLoadingImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadingImage.startAnimation(mSetAnimation2);
            }
        }, 200);
    }

    public void dismiss() {
        mLoadingCircleView.stopAnimation();
        mLoadingCircleView.setVisibility(GONE);
        mLoadingImage.setVisibility(GONE);
        //清理动画，防止View无法隐藏
        //因为View的类型动画是对View的镜像进行动画，并没有改变View的状态
        if (mLoadingImage.getAnimation() != null) {
            mLoadingImage.clearAnimation();
        }
    }

    @Override
    public void onFinish() {
        //3.圆形画完后立刻执行放大透明动画
        mLoadingImage.setVisibility(VISIBLE);
        mLoadingImage.startAnimation(mSetAnimation1);
    }

}
