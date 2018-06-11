package com.denghao.tabindicatorview;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.List;

/**
 * @author yudneghao
 * @date 2018/6/5
 */

public class TabIndicator extends View {
  private static final String TAG = "WrapContentView";
  private int desiredWidth;
  private int desiredHeight;
  private int padding;
  private List<String> tabText;
  private int position;
  private int selectColor;
  private int unSelectColor;
  /**
   * 0从左边向右 1 从右边向左
   */
  private int orientation = 1;
  private int beforeSelect;
  private int currentCoordinates = -1;
  private int currentStart = -1;
  private int normalCircleX = -1;
  private int endCircleX = -1;
  private onTabSelectListener mTabSelectListener;

  public TabIndicator(Context context) {
    this(context, null);
  }

  public TabIndicator(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TabIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public TabIndicator setData(int selectColor, int unSelectColor, List<String> tabText) {
    this.selectColor = selectColor;
    this.unSelectColor = unSelectColor;
    this.tabText = tabText;
    for (int i = 0; i < tabText.size(); i++) {
      String currentS = tabText.get(i);
      Paint paint = getTextPaint(getResources().getColor(selectColor), -1);
      Rect stepRect = new Rect();
      paint.getTextBounds(currentS, 0, currentS.length(), stepRect);
      desiredWidth += stepRect.width() + dip2px(padding);
      desiredHeight = stepRect.height();
    }
    invalidate();
    return this;
  }

  /**
   * 设置字间距
   */
  public TabIndicator setPadding(int padding) {
    this.padding = padding;
    invalidate();
    return this;
  }

  /**
   * 设置选中项 默认是0
   */
  public TabIndicator setSelect(int position) {
    this.position = position;
    invalidate();
    return this;
  }

  private Paint getTextPaint( int color, float with) {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setAntiAlias(true);
    paint.setColor(color);
    paint.setTextSize(dip2px(15));
    if (with != -1) {
      paint.setStrokeWidth(dip2px(with));
    }
    return paint;
  }

  public static int dip2px(float dipValue) {
    float scale = Resources.getSystem().getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5F);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int desiredWidth = this.desiredWidth;
    int desiredHeight = dip2px(53);

    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

    int width;
    int height;
    if (widthMode == MeasureSpec.EXACTLY) {
      width = widthSize;
    } else if (widthMode == MeasureSpec.AT_MOST) {
      width = Math.min(desiredWidth, widthSize);
    } else {
      width = desiredWidth;
    }
    if (heightMode == MeasureSpec.EXACTLY) {
      height = heightSize;
    } else if (heightMode == MeasureSpec.AT_MOST) {
      height = Math.min(desiredHeight, heightSize);
    } else {
      height = desiredHeight;
    }
    setMeasuredDimension(width, height);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawText(canvas, position);
    drawIndicator(canvas, position);
  }

  private void drawIndicator(Canvas canvas, int position) {
    if (tabText.size() > 1) {
      Paint unSelect = getTextPaint(getResources().getColor(unSelectColor), 3);
      Paint select = getTextPaint(getResources().getColor(selectColor), 3);
      Rect stepRect = new Rect();
      int y = desiredHeight + dip2px(21);
      String tempStart = "";
      String tempEnd = "";
      RectF rectF = new RectF();
      for (int i = 0; i < tabText.size(); i++) {
        String currentString = tabText.get(i);
        tempStart += currentString;
        tempEnd += currentString;
        int startX;
        int endX;
        if (i == 0) {
          unSelect.getTextBounds(tempStart, 0, tempStart.length(), stepRect);
          startX = 0;
          endX = stepRect.width() + dip2px(padding);
        } else {
          unSelect.getTextBounds(
              tempStart.substring(0, tempStart.length() - currentString.length()), 0,
              tempStart.length() - currentString.length(), stepRect);
          startX = stepRect.width() + i * dip2px(padding);
          unSelect.getTextBounds(tempEnd, 0, tempEnd.length(), stepRect);
          endX = stepRect.width() + (i + 1) * dip2px(padding);
        }
        if (i == position) {
          if (i != tabText.size() - 1) {
            if (orientation == 0) {
              rectF.set(endX - dip2px(padding) - dip2px(15), y,
                  currentCoordinates == -1 ? endX - dip2px(padding) : currentCoordinates,
                  y + dip2px(3));
              canvas.drawRoundRect(rectF, dip2px(3), dip2px(3), select);
            } else if (orientation == 1) {
              rectF.set(currentCoordinates == -1 ? endX - dip2px(padding) - dip2px(15)
                  : currentCoordinates, y, endX - dip2px(padding), y + dip2px(3));
              canvas.drawRoundRect(rectF, dip2px(3), dip2px(3), select);
            }

            canvas.drawCircle(normalCircleX == -1 ? endX + dip2px(3.5f) : normalCircleX,
                dip2px(1.5f) + y, dip2px(1.5f), unSelect);
          } else if (position == tabText.size() - 1) {
            rectF.set(startX, y, currentStart == -1 ? startX + dip2px(15) : currentStart,
                y + dip2px(3));
            canvas.drawRoundRect(rectF, dip2px(3), dip2px(3), select);
            canvas.drawCircle(
                endCircleX == -1 ? startX - dip2px(padding) - dip2px(3.5f) : endCircleX,
                dip2px(1.5f) + y, dip2px(1.5f), unSelect);
          }
        }
      }
    }
  }

  private void drawText(Canvas canvas, int position) {
    int start = 0;
    Paint select;
    for (int i = 0; i < tabText.size(); i++) {
      if (i != position) {
        select = getTextPaint(getResources().getColor(unSelectColor), -1);
      } else {
        select = getTextPaint(getResources().getColor(selectColor), -1);
      }
      String s = tabText.get(i);
      canvas.drawText(s, start, desiredHeight + dip2px(15), select);
      start += getTextRect(s).width() + dip2px(padding);
    }
  }

  private Rect getTextRect(String s) {
    Paint select = getTextPaint(getResources().getColor(unSelectColor), -1);
    Rect stepRect = new Rect();
    select.getTextBounds(s, 0, s.length(), stepRect);
    return stepRect;
  }

  @SuppressLint("ClickableViewAccessibility") @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        float x = event.getX();
        //获取文字显示区域的范围
        String tempStart = "";
        String tempEnd = "";
        for (int i = 0; i < tabText.size(); i++) {
          Paint paint = getTextPaint(getResources().getColor(selectColor), -1);
          Rect stepRect = new Rect();
          if (i == 0) {
            tempStart += tabText.get(i);
          } else {
            String currentS = tabText.get(i);
            tempStart += currentS;
            tempStart = tempStart.substring(0, tempStart.length() - currentS.length());
          }
          tempEnd += tabText.get(i);
          int startX;
          int endX;
          if (i == 0) {
            paint.getTextBounds(tempStart, 0, tempStart.length(), stepRect);
            startX = 0;
            endX = stepRect.width() + dip2px(padding);
          } else {
            paint.getTextBounds(tempStart, 0, tempStart.length(), stepRect);
            startX = stepRect.width() + i * dip2px(padding);
            paint.getTextBounds(tempEnd, 0, tempEnd.length(), stepRect);
            endX = stepRect.width() + (i + 1) * dip2px(padding);
          }
          if (x >= startX && x <= endX) {
            position = i;
            //orientation = beforeSelect > position ? 1 : 0;
            if (beforeSelect != position) {
              beforeSelect = position;
              setAnimators(position);
            }
            if (mTabSelectListener != null) {
              mTabSelectListener.onSelect(i, startX, endX);
            }
            break;
          }
        }
        break;

      default:
    }
    return super.onTouchEvent(event);
  }

  public void setAnimators(int position) {

    if (tabText.size() > 1) {
      final Paint unSelect = getTextPaint(getResources().getColor(unSelectColor), 3);
      Rect stepRect = new Rect();
      String tempStart = "";
      String tempEnd = "";
      for (int i = 0; i < tabText.size(); i++) {
        String currentString = tabText.get(i);
        tempStart += currentString;
        tempEnd += currentString;
        int startX;
        final int endX;
        if (i == 0) {
          unSelect.getTextBounds(tempStart, 0, tempStart.length(), stepRect);
          startX = 0;
          endX = stepRect.width() + dip2px(padding);
        } else {
          unSelect.getTextBounds(
              tempStart.substring(0, tempStart.length() - currentString.length()), 0,
              tempStart.length() - currentString.length(), stepRect);
          startX = stepRect.width() + i * dip2px(padding);
          unSelect.getTextBounds(tempEnd, 0, tempEnd.length(), stepRect);
          endX = stepRect.width() + (i + 1) * dip2px(padding);
        }
        if (i == position) {
          if (i != tabText.size() - 1) {
            ValueAnimator animator = null;
            if (orientation == 0) {
              animator =
                  ValueAnimator.ofInt(endX - dip2px(padding) - dip2px(15), endX - dip2px(padding));
            } else if (orientation == 1) {
              animator =
                  ValueAnimator.ofInt(endX - dip2px(padding), endX - dip2px(padding) - dip2px(15));
            }

            assert animator != null;
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
              @Override public void onAnimationUpdate(ValueAnimator animation) {
                currentCoordinates = (int) animation.getAnimatedValue();
                invalidate();
              }
            });
            animator.setDuration(1000);
            animator.start();
            ValueAnimator circleAnimator =
                ValueAnimator.ofInt(endX - dip2px(padding) - dip2px(3.5f), endX + dip2px(3.5f));
            circleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
              @Override public void onAnimationUpdate(ValueAnimator animation) {
                normalCircleX = (int) animation.getAnimatedValue();
                invalidate();
              }
            });
            circleAnimator.setDuration(1000);
            circleAnimator.start();
          } else if (position == tabText.size() - 1) {
            ValueAnimator animator = ValueAnimator.ofInt(startX, startX + dip2px(15));
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
              @Override public void onAnimationUpdate(ValueAnimator animation) {
                currentStart = (int) animation.getAnimatedValue();
                invalidate();
              }
            });
            animator.setDuration(1000);
            animator.start();
            ValueAnimator circleAnimator =
                ValueAnimator.ofInt(startX, startX - dip2px(3.5f) - dip2px(padding));
            circleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
              @Override public void onAnimationUpdate(ValueAnimator animation) {
                endCircleX = (int) animation.getAnimatedValue();
                invalidate();
              }
            });
            circleAnimator.setDuration(1000);
            circleAnimator.start();
          }
        }
      }
    }
  }

  public interface onTabSelectListener {
    void onSelect(int position, int start, int end);
  }

  public void setTabSelectListener(onTabSelectListener tabSelectListener) {
    mTabSelectListener = tabSelectListener;
  }
}
