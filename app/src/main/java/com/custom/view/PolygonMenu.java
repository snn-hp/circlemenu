package com.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.modle.Coordinate;
import com.example.shanhukeji.circlemenu.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2017/7/7 . on 下午 12:05
 * gfh
 */
public class PolygonMenu extends View {
    private int mHeight = 0;//控件的高度
    private int mWidth = 0;//控件的宽度
    private String[] mIndexStr = null;
    private int defaultUnit = 100;//单位间隔
    private int firstRadius = 300;
    private int textSize = 0;
    private int textColor = 0;
    private int lineColor = 0;
    private int rectColor = 0;
    private int[] initValue = {2, 0, 3, 1, 0};
    private Paint rectPain;//绘制线条paint
    private Paint textPain;
    private Paint solidPain;//绘制属性矩形paint
    private Paint blackLinePain;//绘制属性矩形黑线
    private Paint redLinePain;//绘制属性矩形红色虚线
    private Paint grayCirclePaint;//绘制灰色圆形paint
    private Paint blackCirclePaint;//绘制黑色圆形paint
    private int textWidth;
    private int textHeight;
    private static final int LINE_SPACE = 10;
    private Map<Integer, Coordinate> coordinateMap = new LinkedHashMap<>();
    //灰色外圆半径
    private static final int GRAY_CIRCLE_RADIUS = 100;
    //黑色内圆半径
    private static final int BLACK_CIRCLE_RADIUS = 90;
    //最外圆半径
    private  int big_radius = 400;
    //数量集合
    private List<String> numberList = new ArrayList<>();

    public PolygonMenu(Context context) {
        this(context, null);
    }

    public PolygonMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolygonMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PolygonMenu, defStyleAttr, 0);
            for (int i = 0; i < ta.getIndexCount(); i++) {
                int attr = ta.getIndex(i);
                if (attr == R.styleable.PolygonMenu_firstR) {
                    firstRadius = ta.getDimensionPixelSize(attr, DensityUtil.dip2px(context, 20));
                } else if (attr == R.styleable.PolygonMenu_unitR) {
                    defaultUnit = ta.getDimensionPixelSize(attr, DensityUtil.dip2px(context, 20));
                } else if (attr == R.styleable.PolygonMenu_textSize) {
                    textSize = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
                } else if (attr == R.styleable.PolygonMenu_textColor) {
                    textColor = ta.getColor(attr, Color.BLACK);
                } else if (attr == R.styleable.PolygonMenu_lineColor) {
                    lineColor = ta.getColor(attr, Color.BLACK);
                } else if (attr == R.styleable.PolygonMenu_rectColor) {
                    rectColor = ta.getColor(attr, Color.BLACK);
                } else if (attr == R.styleable.PolygonMenu_attrs) {
                    String ar = ta.getString(attr);
                    if (TextUtils.isEmpty(ar)) {
                        mIndexStr =getResources().getStringArray(R.array.appraisal_status);
                    }
                } else if (attr == R.styleable.PolygonMenu_datas) {
                    String dr = ta.getString(attr);
                    if (TextUtils.isEmpty(dr)) {
                        initValue = new int[]{2, 0, 3, 1, 0};
                    } else {
                        String[] dar = dr.split(",");
                        initValue = new int[dar.length];
                        for (int index = 0; index < dar.length; index++) {
                            initValue[index] = Integer.parseInt(dar[index]);
                        }
                    }
                }else if(attr == R.styleable.PolygonMenu_big_radius){
                        big_radius=ta.getDimensionPixelSize(attr, DensityUtil.dip2px(context, 200));
                }
            }
            ta.recycle();
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int wMode = MeasureSpec.getMode(widthMeasureSpec);
            int wSize = MeasureSpec.getSize(widthMeasureSpec);
            if (wMode != MeasureSpec.EXACTLY) {
                wSize = defaultUnit * 2 * initValue.length + textWidth * 3 + firstRadius * 2;
            }
            int hMode = MeasureSpec.getMode(heightMeasureSpec);
            int hSize = MeasureSpec.getSize(heightMeasureSpec);
            if (hMode != MeasureSpec.EXACTLY) {
                hSize = defaultUnit * 2 * initValue.length + textHeight * 2 + firstRadius * 2;
            }
            setMeasuredDimension(wSize, hSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        rectPain = new Paint();
        rectPain.setAntiAlias(true);
        rectPain.setStyle(Paint.Style.FILL);
        rectPain.setStrokeWidth(3);
        rectPain.setColor(lineColor);
        textPain = new Paint();
        textPain.setAntiAlias(true);
        textPain.setTextSize(textSize);
        textPain.setColor(textColor);
        solidPain = new Paint();
        solidPain.setAntiAlias(true);
        solidPain.setStyle(Paint.Style.FILL);
        solidPain.setColor(rectColor);

        Rect rect = new Rect();
        textPain.getTextBounds(mIndexStr[0], 0, mIndexStr[0].length(), rect);
        textWidth = rect.width();
        textHeight = rect.height();
        /**黑线paint*/
        blackLinePain = new Paint();
        blackLinePain.setAntiAlias(true);
        blackLinePain.setStyle(Paint.Style.STROKE);
        blackLinePain.setStrokeWidth(5);
        blackLinePain.setColor(Color.BLACK);

        /**红色虚线*/
        DashPathEffect pathEffect = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);
        redLinePain = new Paint();
        redLinePain.setAntiAlias(true);
        redLinePain.setStyle(Paint.Style.STROKE);
        redLinePain.setStrokeWidth(2);
        redLinePain.setColor(Color.RED);
        redLinePain.setPathEffect(pathEffect);
        /**红色圆形paint*/
        grayCirclePaint = new Paint();
        grayCirclePaint.setAntiAlias(true);
        grayCirclePaint.setStyle(Paint.Style.FILL);
        grayCirclePaint.setColor(Color.GRAY);
        /**黑色圆形*/
        blackCirclePaint = new Paint();
        blackCirclePaint.setAntiAlias(true);
        blackCirclePaint.setStyle(Paint.Style.FILL);
        blackCirclePaint.setColor(Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //将画布坐标系移动到view的中心
        canvas.translate(mWidth / 2, mHeight / 2);
        drawRect(canvas);
    }


    /**
     * 绘制多边形
     */
    private void drawRect(Canvas canvas) {
        Path path_rect = new Path();//绘制多边形的路径
        Path path_line = new Path();//绘制圆心与顶点的连线
        Path path_sloid = new Path();//绘制属性值的路径
        Path path_black_line = new Path();//绘制悬浮黑线
        Path path_red_line = new Path();//绘制悬浮红线
        for (int j = 0; j < mIndexStr.length; j++) {
            Coordinate coordinate = new Coordinate();
            int angle = j * 360 / mIndexStr.length;//我们的原则是第一个点在x轴正半轴
            // 每一个点对应的角度
            if (initValue.length % 2 != 0) {
                angle += 360 / initValue.length + 90;//如果是边数是奇数的情况,如果是偶数边，就没有必要进行偏移，

            }
            double radain = Math.PI * angle / 180;
            float x = (float) (Math.cos(radain) * big_radius);
            float y = (float) (Math.sin(radain) * big_radius);
            coordinate.setX(x);
            coordinate.setY(y);
            coordinateMap.put(j, coordinate);
            float back_line_x = (float) (Math.cos(radain) * (big_radius + LINE_SPACE));
            float back_line_y = (float) (Math.sin(radain) * (big_radius + LINE_SPACE));
            float red_line_x = (float) (Math.cos(radain) * (big_radius + 2 * LINE_SPACE));
            float red_line_y = (float) (Math.sin(radain) * (big_radius + 2 * LINE_SPACE));
            if (j == 0) {
                path_rect.moveTo(x, y);
                path_black_line.moveTo(back_line_x, back_line_y);
                path_red_line.moveTo(red_line_x, red_line_y);
            } else {
                path_rect.lineTo(x, y);
                path_black_line.lineTo(back_line_x, back_line_y);
                path_red_line.lineTo(red_line_x, red_line_y);
            }
            //最后一个多边形，画上中心与顶点的连线
            path_line.lineTo(x, y);
            canvas.drawPath(path_line, rectPain);
            path_line.reset();
            int radus2 = firstRadius + initValue[j] * defaultUnit;
            float x2 = (float) (Math.cos(radain) * radus2);
            float y2 = (float) (Math.sin(radain) * radus2);
            if (j == 0) {
                path_sloid.moveTo(x2, y2);
            } else {
                path_sloid.lineTo(x2, y2);
            }

        }
        path_rect.close();
        canvas.drawPath(path_rect, rectPain);
        path_rect.reset();
        path_sloid.close();
        canvas.drawPath(path_sloid, solidPain);

        path_black_line.close();
        canvas.drawPath(path_black_line, blackLinePain);
        path_black_line.reset();

        path_red_line.close();
        canvas.drawPath(path_red_line, redLinePain);
        path_red_line.reset();
        /**绘制多边形对角圆*/
        int loopCount = 0;
        for (Map.Entry<Integer, Coordinate> entry : coordinateMap.entrySet()) {
            Coordinate coordinate = entry.getValue();
            canvas.drawCircle(coordinate.getX(), coordinate.getY(), GRAY_CIRCLE_RADIUS, grayCirclePaint);
            canvas.drawCircle(coordinate.getX(), coordinate.getY(), BLACK_CIRCLE_RADIUS, blackCirclePaint);
            setTextFormat(coordinate, GRAY_CIRCLE_RADIUS, canvas, mIndexStr[loopCount], numberList, loopCount);
            loopCount++;
        }
    }

    /**
     * 获取圆的内切方形
     */
    private void setTextFormat(Coordinate coordinate, int radius, Canvas canvas, String numberStr, List<String> numberList, int position) {
        float x = coordinate.getX();
        float y = coordinate.getY();
        Rect rect = new Rect((int) (x - radius), (int) (y - radius), (int) (x + radius), (int) (y + radius));//画一个矩形
        Paint rectPaint = new Paint();
        rectPaint.setColor(Color.TRANSPARENT);
        rectPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, rectPaint);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(textSize);
        textPaint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

        int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式
        float line_space=getContext().getResources().getDimension(R.dimen.text_line_space);
        if (null != numberList && numberList.size() > position) {
            canvas.drawText(numberList.get(position), rect.centerX(), baseLineY-line_space, textPaint);
        }
        //两行不同textsize文字居中显示，将下行文字下移
        canvas.drawText(numberStr, rect.centerX(), baseLineY + line_space, textPaint);

    }

    /**
     * 提供多边形各个点的坐标
     */
    public Map<Integer, Coordinate> getAllCoordinates() {
        return coordinateMap;
    }
   /**
    * 更新服务器数据
    * */
    public void setData(List<String> numberList){
         if(null!=numberList){
             this.numberList.clear();
             this.numberList.addAll(numberList);
         }
    }
    /**后续点击事件需要在此处理*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
