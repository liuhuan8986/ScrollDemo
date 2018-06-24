package lh.scrolldemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

/**
 * Created by liuhuan on 2018/6/23.
 *  第一点： 这个控件只能有一个子控件
 *  第二点： 这给容器得和ScrollView 配合使用，被ScrollView包裹住
 *  第三点，使用的时候得先 调用 setPraentHeight 把ScrollView得高度传进来在 再调用 start()方法
 */

public class AutoLinearLayout extends LinearLayout {

    public AutoLinearLayout(Context context) {
        super(context);
    }

    public AutoLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * 容器内容的高度
     */
    public int contentHeight= 0;
    public int praentHeight=0;
    /**
     * 是否滚动完成
     */
    public boolean mFinish = true;
    private int preValue;
    /**
     * 滚动完成得监听
     */
    public ScrollFinishListener scrollFinishListener;

    private ValueAnimator animator;
    public int getPraentHeight() {
        return praentHeight;
    }

    public void setPraentHeight(int praentHeight) {
        this.praentHeight = praentHeight;
    }

    public ScrollFinishListener getScrollFinishListener() {
        return scrollFinishListener;
    }

    public void setScrollFinishListener(ScrollFinishListener scrollFinishListener) {
        this.scrollFinishListener = scrollFinishListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
         int paddingTop = getPaddingTop();
         int paddingBottom = getPaddingBottom();
        contentHeight = 0;
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount();i++){
                View child = getChildAt(i);
                if(child != null && child.getVisibility() != View.GONE){
                    //先测量子View
                    measureChild(child,widthMeasureSpec,heightMeasureSpec);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                    //把每个子View所占的高度累加起来 包括marginmTop 和 marginmBottom
                    contentHeight+=child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;
                }
            }
            contentHeight += paddingTop+paddingBottom;
        }
        Log.i("praentHeight:",""+praentHeight);
        Log.i("conentHeight:",""+contentHeight);
        Log.i("getScrollY:",""+getScrollY());
        if(contentHeight < praentHeight){
            setMeasuredDimension(widthMeasureSpec,praentHeight);
        }else{
            setMeasuredDimension(widthMeasureSpec,contentHeight);
        }
    }


    public int getContentHeight() {
        return contentHeight;
    }


    public void start(View view){
        if(!mFinish){
            return;
        }
        view.setVisibility(View.INVISIBLE);
        addView(view);
        post(new Runnable() {
           @Override
           public void run() {
               reset();
                animator = ValueAnimator.ofInt(0,getPraentHeight()+getContentHeight());;
               //这里算一个比例  比如要滚动 1000px 需要20秒 为基数
               int time =0;
               if(getContentHeight() > getPraentHeight()){
                   time = (int) ((getContentHeight())/1000f * 20f);//单位秒
               }else{
                   time = (int) ((getPraentHeight())/1000f * 20f);//单位秒
               }


               Log.i("动态的动画时间",time+"");
               animator.setDuration(time*1000);
               //设置匀速
               animator.setInterpolator(new LinearInterpolator());

               animator.addListener(new AnimatorListenerAdapter() {
                   @Override
                   public void onAnimationEnd(Animator animation) {
                       super.onAnimationEnd(animation);
                       //结束之后,把所有子控件移除
                       scrollTo(0,0);
                       removeAllViews();
                       preValue = 0;
                       mFinish  = true;
                       if(scrollFinishListener!=null){
                           scrollFinishListener.onFinish(AutoLinearLayout.this);
                       }

                       Log.i("End_getContentHeight",":"+getContentHeight());

                   }

                   @Override
                   public void onAnimationStart(Animator animation) {
                       super.onAnimationStart(animation);
                       if(getChildCount() > 0){
                           View  child = getChildAt(0);
                           child.setVisibility(View.VISIBLE);
                           mFinish = false;
                       }
                   }
               });
               animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                   @Override
                   public void onAnimationUpdate(ValueAnimator animation) {
                       int curValue = (int)animation.getAnimatedValue();
                       Log.i("Update",":"+getScrollY());
                       scrollBy(0,curValue-preValue);
                       preValue = curValue;
                   }
               });
               animator.start();
           }
       });
    }
    /**
     * 恢复位置 让 子控件从底部出来,即让子View 先滚动到 底部
     */
   private void  reset(){
       if(getChildCount() > 0){
           scrollTo(0,-getPraentHeight() );
       }
   }

   public interface ScrollFinishListener{
       void onFinish(View parent);
    }

    public void cancelAnimator(){
        if(animator.isRunning()){
            animator.end();
        }
    }

}
