package com.wordplat.ikvstockchart.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;

import com.wordplat.ikvstockchart.entry.Entry;
import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.entry.SizeColor;
import com.wordplat.ikvstockchart.render.AbstractRender;

public class FenshiDrawing implements IDrawing {


    private Paint fenshiPaint;
    private Paint ma10Paint;
    private Paint ma20Paint;

    private final RectF candleRect = new RectF(); // K 线图显示区域
    private AbstractRender render;

    // 计算 MA(5, 10, 20) 线条坐标用的
    private float[] ma5Buffer = new float[4];
    private float[] pointBuffer = new float[2];
    private Path path;


    @Override
    public void onInit(RectF contentRect, AbstractRender render, Context context) {
        this.render = render;
        final SizeColor sizeColor = render.getSizeColor();
        if (fenshiPaint == null) {
            fenshiPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            fenshiPaint.setStyle(Paint.Style.STROKE);
        }

        if (ma10Paint  == null){
            ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        ma10Paint.setColor(sizeColor.getMa10Color());
        ma10Paint.setStyle(Paint.Style.FILL);
        ma10Paint.setAlpha(15);
        ma10Paint.setStrokeWidth(2);
        path = new Path();
        path.moveTo(  candleRect.left, candleRect.bottom);
        fenshiPaint.setStrokeWidth(sizeColor.getMaLineSize());

        fenshiPaint.setColor(sizeColor.getMa5Color());

        candleRect.set(contentRect);
    }


    @Override
    public void computePoint(int minIndex, int maxIndex, int currentIndex) {
        final int count = (maxIndex - minIndex) * 4;
        if (ma5Buffer.length < count) {
            ma5Buffer = new float[count];
        }

        final EntrySet entrySet = render.getEntrySet();
        final Entry entry = entrySet.getEntryList().get(currentIndex);
        final int i = currentIndex - minIndex;

        if (currentIndex < maxIndex - 1) {
            ma5Buffer[i * 4 + 0] = currentIndex + 0.5f;
            ma5Buffer[i * 4 + 1] = entry.getClose();
            ma5Buffer[i * 4 + 2] = currentIndex + 1 + 0.5f;
            ma5Buffer[i * 4 + 3] = entrySet.getEntryList().get(currentIndex + 1).getClose();
        }


    }

    @Override
    public void onComputeOver(Canvas canvas, int minIndex, int maxIndex, float minY, float maxY) {
        canvas.save();
        canvas.clipRect(candleRect);
        render.mapPoints(ma5Buffer);


        final int count = (maxIndex - minIndex) * 4;
        if (count > 0) {
            // 使用 drawLines 方法比依次调用 drawLine 方法要快
            canvas.drawLines(ma5Buffer, 0, count, fenshiPaint);
        }
        /*for (int i = minIndex; i < maxIndex - 1; i++) {
            Entry entry = entrySet.getEntryList().get(i);

            candleRectBuffer[0] = i ;
            candleRectBuffer[2] = i + 1;

            candleRectBuffer[1] = entry.getClose();
            candleRectBuffer[3] = entrySet.getEntryList().get(i + 1).getClose();
            render.mapPoints(candleRectBuffer);


            path.lineTo( candleRectBuffer[0], candleRectBuffer[1]);
            if (i == maxIndex -2){
                path.lineTo(candleRectBuffer[2] ,
                        candleRect.bottom);
            }

        }*/
        Log.d("FenshiDrawing","buffer : "+ ma5Buffer[0]+"  " +ma5Buffer[1]+"    "+ma5Buffer[2]+"    "+ma5Buffer[3]);

        // 计算高亮坐标
        if (render.isHighlight()) {
            final EntrySet entrySet = render.getEntrySet();
            final int lastEntryIndex = entrySet.getEntryList().size()-1;
            final float[] highlightPoint = render.getHighlightPoint();
            pointBuffer[0] = highlightPoint[0];
            render.invertMapPoints(pointBuffer);
            final int highlightIndex = pointBuffer[0] < 0 ? 0 : (int) pointBuffer[0];
            final int i = highlightIndex - minIndex;
            try {
                highlightPoint[0] = highlightIndex < lastEntryIndex ?
                        ma5Buffer[i * 4 ] : ma5Buffer[lastEntryIndex * 4 + 2];
                highlightPoint[1] = highlightIndex < lastEntryIndex ?
                        ma5Buffer[i * 4 + 1] : ma5Buffer[lastEntryIndex * 4 + 3];
                entrySet.setHighlightIndex(highlightIndex);
            }catch (Exception e){
                Log.d("Fenshi "," Exception : " + e);
            }

        }


        canvas.restore();
    }

    @Override
    public void onDrawOver(Canvas canvas) {

    }
}
