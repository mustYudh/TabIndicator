package com.denghao.tabindicatorview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author yudneghao
 * @date 2018/6/11
 */

public class ArcView extends View {
  private Paint mPaint;
  private PointF mStartPoint;
  private PointF mEndPoint;
  private PointF mControlPoint;
  private int arcHeight = 92;
  private String bgColor = "#2DE1D9";
  private Path path;

  public ArcView(Context context) {
    this(context, null);
  }

  public ArcView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public void setViewData(String bgColor, int mArcHeight) {
    this.arcHeight = mArcHeight;
    this.bgColor = bgColor;
    invalidate();
  }

  private void init() {
    mPaint = new Paint();
    mPaint.setColor(Color.parseColor(bgColor));
    mPaint.setAntiAlias(true);
    mPaint.setStyle(Paint.Style.FILL);
    mStartPoint = new PointF(0, 0);
    mEndPoint = new PointF(0, 0);
    mControlPoint = new PointF(0, 0);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    path = new Path();
    path.reset();
    path.moveTo(0, 0);
    path.addRect(0, 0, w, h - arcHeight, Path.Direction.CW);
    mStartPoint.x = 0;
    mStartPoint.y = h - arcHeight;
    mEndPoint.x = w;
    mEndPoint.y = h - arcHeight;
    mControlPoint.x = w / 2;
    mControlPoint.y = h + arcHeight;

    invalidate();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    path.moveTo(mStartPoint.x, mStartPoint.y);
    path.quadTo(mControlPoint.x, mControlPoint.y, mEndPoint.x, mEndPoint.y);
    canvas.drawPath(path, mPaint);
  }
}
