package com.wordplat.ikvstockchart.drawing;

import android.graphics.Canvas;
import android.util.Log;

import com.wordplat.ikvstockchart.marker.IMarkerView;

/**
 * <p>KLineVolumeHighlightDrawing K线成交量的高亮绘制</p>
 * <p>Date: 2017/6/28</p>
 *
 * @author afon
 */

public class KLineVolumeHighlightDrawing extends HighlightDrawing {

    private boolean mIsY;

    public KLineVolumeHighlightDrawing(boolean isY){
        mIsY = isY;
    }

    @Override
    public void onDrawOver(Canvas canvas) {
        // 绘制高亮 成交量的高亮线条不需要垂直移动
        if (render.isHighlight()) {
            final float[] highlightPoint = render.getHighlightPoint();

            canvas.save();
            canvas.clipRect(contentRect);

            if (markerViewList.size() > 0) {
                for (IMarkerView markerView : markerViewList) {
                    markerView.onDrawMarkerView(canvas,
                            highlightPoint[0],
                            highlightPoint[1]);
                }
            }

            Log.d("KLineHighlight","highlightPoint[1]:"+highlightPoint[1]+"\thighlightPoint[0]:"+highlightPoint[0]);
            canvas.drawLine(highlightPoint[0], contentRect.top, highlightPoint[0], contentRect.bottom, highlightPaint);
            if (mIsY) {
                canvas.drawLine(contentRect.left, highlightPoint[1], contentRect.right, highlightPoint[1], highlightPaint);
            }

            canvas.restore();
        }
    }
}
