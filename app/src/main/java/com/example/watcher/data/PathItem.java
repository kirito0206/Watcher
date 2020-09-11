package com.example.watcher.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.view.MotionEvent;

import com.example.watcher.ui.MyApplication;
import com.example.watcher.Retrofit.RetrofitUtil;

public class PathItem {

    private Path mPath;
    private int mDrawColor;
    private Region mRegion;
    public String name;

    public PathItem(Path path,String name) {
        this.mPath = path;
        this.name = name;

        RectF rectF = new RectF();
        // 计算path的边界, exact参数无所谓，该方法不再使用这个参数
        mPath.computeBounds(rectF, true);

        mRegion = new Region();
        // 得到path和其最大矩形范围的交集区域
        mRegion.setPath(mPath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
    }

    /**
     * 设置区块绘制颜色
     */
    public void setDrawColor(int drawColor) {
        this.mDrawColor = drawColor;
    }

    public void drawItem(Canvas canvas, Paint paint, boolean isSelect) {
        if (isSelect) {
            // 选中状态

            paint.clearShadowLayer();
            paint.setStrokeWidth(1);

            // 绘制填充
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(mDrawColor);
            canvas.drawPath(mPath, paint);

            // 绘制描边
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setColor(Color.BLACK);
            canvas.drawPath(mPath, paint);

            Paint textPaint = new Paint();          // 创建画笔
            textPaint.setColor(Color.BLACK);        // 设置颜色
            textPaint.setStyle(Paint.Style.FILL);   // 设置样式
            textPaint.setTextSize(20f);
            textPaint.setFakeBoldText(true);
            if (!name.equals("")){
                int num = 0;
                canvas.drawText(name,20, MyApplication.dp2px(150),textPaint);
                if (RetrofitUtil.getInstance().getProvinceDetail(name) != null)
                    num = RetrofitUtil.getInstance().getProvinceDetail(name).getTotal().getConfirm() - RetrofitUtil.getInstance().getProvinceDetail(name).getTotal().getDead() - RetrofitUtil.getInstance().getProvinceDetail(name).getTotal().getHeal();
                else if (RetrofitUtil.getInstance().getCountryDetail(name) != null)
                    num = RetrofitUtil.getInstance().getCountryDetail(name).getTotal().getConfirm() - RetrofitUtil.getInstance().getCountryDetail(name).getTotal().getDead() - RetrofitUtil.getInstance().getCountryDetail(name).getTotal().getHeal();
                canvas.drawText("现有确诊："+num,20,MyApplication.dp2px(160),textPaint);
            }
        } else {
            // 普通状态

            paint.setStrokeWidth(2);

            // 绘制底色+阴影
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(8, 0, 0, 0xffffff);
            canvas.drawPath(mPath, paint);

            // 绘制填充
            paint.clearShadowLayer();
            paint.setColor(mDrawColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(mPath, paint);
        }
    }

    public boolean isTouch(int x, int y, MotionEvent event) {

        boolean isTouch = mRegion.contains(x, y);

        if (isTouch) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 按下
                    break;
                case MotionEvent.ACTION_MOVE:
                    // 滑动
                    break;
                case MotionEvent.ACTION_UP:
                    // 抬起
                    break;
                default:
                    break;
            }
        }

        return isTouch;
    }

}