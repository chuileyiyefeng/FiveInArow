package my.com.fiveinarow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/19.
 */

public class ChessBoardView extends View {
    Paint paint;
    private int mPanelWidth, panelWidth;
    private int maxline = 10;
    private float mPanelSpace;
    Bitmap whitePanel, blackPanel;
    boolean isWhite = true;
    boolean isDrawBoad;
    private List<Point> whitPoints, blackPoints;

    public ChessBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.parseColor("#000000"));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(4f);
        whitePanel = BitmapFactory.decodeResource(getResources(), R.mipmap.white_panel);
        blackPanel = BitmapFactory.decodeResource(getResources(), R.mipmap.black_panel);
        whitPoints = new ArrayList<>();
        blackPoints = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = Math.min(widthSize, heightSize);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;//棋盘的边长
        mPanelSpace = mPanelWidth * 1.0f / maxline;//每个棋盘间的距离
        float scale = 3 * 1.0f / 4;
        panelWidth = (int) (mPanelSpace * scale);
        whitePanel = Bitmap.createScaledBitmap(whitePanel, panelWidth, panelWidth, false);
        blackPanel = Bitmap.createScaledBitmap(blackPanel, panelWidth, panelWidth, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isDrawBoad)
            drawBoard(canvas);
        drawPanel(canvas);
    }

    //画棋盘的方法
    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float mlineheight = mPanelSpace;
        for (int i = 0; i < maxline; i++) {
            int startx = (int) (mlineheight / 2);
            int endx = (int) (w - mlineheight / 2);
            int y = (int) ((0.5 + i) * mlineheight);
            canvas.drawLine(startx, y, endx, y, paint);
            canvas.drawLine(y, startx, y, endx, paint);
        }
        isDrawBoad = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
//         UP的时候，我们首先根据(x,y)坐标，转化为可落子点的坐标，即类似(0,0),(1,1)这种。
//         然后判断改点没有被任何棋子占据，中间一个mIsWhite变量控制当前棋子的颜色，检查完毕后加入到我们的集合中，最后调用invalidate()触发重绘;
            Point p = new Point((int) (x / mPanelSpace), (int) (y / mPanelSpace));
            if (whitPoints.contains(p) || blackPoints.contains(p)) {
                return false;
            }
            if (isWhite) {
                whitPoints.add(p);
            } else {
                blackPoints.add(p);

            }
            invalidate();
            isWhite = !isWhite;
        }
        return true;
    }

    //画棋子的方法
    private void drawPanel(Canvas canvas) {
        for (int i = 0; i < whitPoints.size(); i++) {
            float scale = 3 * 1.0f / 4;
            Point white = whitPoints.get(i);
            canvas.drawBitmap(whitePanel,
                    (white.x + (1 - scale) / 2) * mPanelSpace,
                    (white.y + (1 - scale) / 2) * mPanelSpace, null);
        }
        for (int i = 0; i < blackPoints.size(); i++) {
            float scale = 3 * 1.0f / 4;
            Point white = blackPoints.get(i);
            canvas.drawBitmap(blackPanel,
                    (white.x + (1 - scale) / 2) * mPanelSpace,
                    (white.y + (1 - scale) / 2) * mPanelSpace, null);
        }
    }
}
