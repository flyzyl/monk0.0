package cn.andye.fragmenttabhost;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

//饼状统计图，带有标注线，都可以自行设定其多种参数选项
public class PieChartView extends View {

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private float pieChartCircleRadius = 100;//饼图半径
    private float textBottom;
    private float mTextSize = 14;// 记录文字大小
    private RectF pieChartCircleRectF = new RectF();    //饼图所占矩形区域（不包括文字）
    private List<PieceDataHolder> pieceDataHolders = new ArrayList<>();    // 饼状图信息列表
    private float markerLineLength = 30f;//标记线长度

    public PieChartView(Context context) {
        super(context);
        init(null, 0);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // 加载属性
        final TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.PieChartView, defStyle, 0);
        pieChartCircleRadius = a.getDimension(R.styleable.PieChartView_circleRadius,
                pieChartCircleRadius);
        mTextSize = a.getDimension(R.styleable.PieChartView_textSize, mTextSize)/getResources().getDisplayMetrics().density;
        a.recycle();
        mTextPaint = new TextPaint(); // 设置默认的TextPaint对象
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        invalidateTextPaintAndMeasurements();        // 从属性更新TextPaint和文本度量
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getContext().getResources().getDisplayMetrics()));
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.descent - fontMetrics.ascent;
        textBottom = fontMetrics.bottom;
    }

    //设置饼状图的半径
    public void setPieChartCircleRadius(int pieChartCircleRadius) {
        this.pieChartCircleRadius = pieChartCircleRadius;
        invalidate();
    }

    //设置标记线的长度
    public void setMarkerLineLength(int markerLineLength) {
        this.markerLineLength = markerLineLength;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPieChartCircleRectF();
        drawAllSectors(canvas);
    }

    private void drawAllSectors(Canvas canvas) {
        float sum = 1440;
        for (PieceDataHolder pieceDataHolder : pieceDataHolders) {
            float startAngel =pieceDataHolder.ang;
            float sweepAngel = pieceDataHolder.value / sum * 360;
            drawSector(canvas, pieceDataHolder.color, startAngel, sweepAngel);
            if(pieceDataHolder.value!=1440&pieceDataHolder.value!=3){
                drawMarkerLineAndText(canvas, pieceDataHolder.color, startAngel + sweepAngel / 2, pieceDataHolder.marker);
            }

        }
    }

    private void initPieChartCircleRectF() {
        pieChartCircleRectF.left = getWidth() / 2 - pieChartCircleRadius;
        pieChartCircleRectF.top = getHeight() / 2 - pieChartCircleRadius;
        pieChartCircleRectF.right = pieChartCircleRectF.left + pieChartCircleRadius * 2;
        pieChartCircleRectF.bottom = pieChartCircleRectF.top + pieChartCircleRadius * 2;
    }

    //获取示例维属性(sp)
    public float getTextSize() {
        return mTextSize;
    }

    /**设置视图的文本维度属性值。在PieChartView视图中，此维度是字体大小。
     * @param textSize 要使用的文本维度属性值(sp)*/
    public void setTextSize(float textSize) {
        mTextSize = textSize;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * 设置饼状图要显示的数据
     * @param data 列表数据
     */
    public void setData(List<PieceDataHolder> data) {
        if (data != null) {
            pieceDataHolders.clear();
            pieceDataHolders.addAll(data);
        }
        invalidate();
    }

    /**
     * 绘制扇形
     *
     * @param canvas     画布
     * @param color      要绘制扇形的颜色
     * @param startAngle 起始角度
     * @param sweepAngle 结束角度
     */
    protected void drawSector(Canvas canvas, String color, float startAngle, float sweepAngle) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(color));
        canvas.drawArc(pieChartCircleRectF, startAngle, sweepAngle, true, paint);
    }

    /**
     * 绘制标注线和标记文字
     * @param canvas      画布
     * @param color       标记的颜色
     * @param rotateAngel 标记线和水平相差旋转的角度
     */
    protected void drawMarkerLineAndText(Canvas canvas, String color, float rotateAngel, String text) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(color));
        Path path = new Path();
        path.close();
        path.moveTo(getWidth() / 2, getHeight() / 2);
        final float x = (float) (getWidth() / 2 + (markerLineLength + pieChartCircleRadius) * Math.cos(Math.toRadians(rotateAngel)));
        final float y = (float) (getHeight() / 2 + (markerLineLength + pieChartCircleRadius) * Math.sin(Math.toRadians(rotateAngel)));
        path.lineTo(x, y);
        float landLineX;
        if (270f > rotateAngel && rotateAngel > 90f) {
            landLineX = x - 20;
        } else {
            landLineX = x + 20;
        }
        path.lineTo(landLineX, y);
        canvas.drawPath(path, paint);
        mTextPaint.setColor(Color.parseColor("#ffffff"));
        if (270f > rotateAngel && rotateAngel > 90f) {
            float textWidth = mTextPaint.measureText(text);
            canvas.drawText(text, landLineX - textWidth, y + mTextHeight / 2 - textBottom, mTextPaint);

        } else {
            canvas.drawText(text, landLineX, y + mTextHeight / 2 - textBottom, mTextPaint);
        }
    }

    //饼状图每块的信息持有者
    public static final class PieceDataHolder {
        private float ang;//每块扇形的值的起始角度
        private float value;//每块扇形的值的大小
        private String color;//扇形的颜色
        private String marker;//每块的标记

        public PieceDataHolder(float ang,float value, String color, String marker) {
            this.ang=ang;
            this.value = value;
            this.color = color;
            this.marker = marker;
        }
    }
}
