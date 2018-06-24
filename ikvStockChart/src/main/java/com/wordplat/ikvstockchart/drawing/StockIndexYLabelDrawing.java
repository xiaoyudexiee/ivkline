/*
 * Copyright (C) 2017 WordPlat Open Source Project
 *
 *      https://wordplat.com/InteractiveKLineView/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wordplat.ikvstockchart.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;

import com.wordplat.ikvstockchart.align.HightLightStyle;
import com.wordplat.ikvstockchart.align.YLabelAlign;
import com.wordplat.ikvstockchart.compat.ViewUtils;
import com.wordplat.ikvstockchart.entry.SizeColor;
import com.wordplat.ikvstockchart.render.AbstractRender;

import java.text.DecimalFormat;

/**
 * <p>StockIndexYLabelDrawing</p>
 * <p>Date: 2017/3/28</p>
 *
 * @author afon
 */

public class StockIndexYLabelDrawing implements IDrawing {

    private Paint yLabelPaint; // Y 轴标签的画笔
    private final Paint.FontMetrics fontMetrics = new Paint.FontMetrics(); // 用于 labelPaint 计算文字位置
    private final DecimalFormat decimalFormatter = new DecimalFormat("0.00");
    private final DecimalFormat threeDecimalFormatter = new DecimalFormat("0.000");

    private final RectF indexRect = new RectF();

    private YLabelAlign yLabelAlign; // Y 轴标签对齐方向
    private Context context;

    @Override
    public void onInit(RectF contentRect, AbstractRender render, Context context) {
        final SizeColor sizeColor = render.getSizeColor();
        this.context =context;
        if (yLabelPaint == null) {
            yLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            yLabelPaint.setTextSize(sizeColor.getYLabelSize());
        }
        yLabelPaint.setColor(sizeColor.getYLabelColor());
        yLabelPaint.getFontMetrics(fontMetrics);
        yLabelAlign = sizeColor.getYLabelAlign();
        if (yLabelAlign == YLabelAlign.RIGHT) {
            yLabelPaint.setTextAlign(sizeColor.getHightLightStyle() == HightLightStyle.OUT?Paint.Align.LEFT: Paint.Align.RIGHT);
        }else if(yLabelAlign == YLabelAlign.LEFT){
            yLabelPaint.setTextAlign(sizeColor.getHightLightStyle() == HightLightStyle.OUT?Paint.Align.RIGHT: Paint.Align.LEFT);
        }

        indexRect.set(contentRect);
    }

    @Override
    public void computePoint(int minIndex, int maxIndex, int currentIndex) {

    }

    @Override
    public void onComputeOver(Canvas canvas, int minIndex, int maxIndex, float minY, float maxY) {
        //调节附图的YLabel偏移量
        float labelX = yLabelAlign == YLabelAlign.LEFT ? indexRect.left + ViewUtils.dpTopx(context, 5) : indexRect.right - ViewUtils.dpTopx(context, 5);
//        float labelX = yLabelAlign == YLabelAlign.LEFT ? ((maxY >10000)?indexRect.left - ViewUtils.dpTopx(context,35) :indexRect.left - ViewUtils.dpTopx(context,5)): ((maxY >10000)?indexRect.right + ViewUtils.dpTopx(context,2):indexRect.right + ViewUtils.dpTopx(context,2));
        String textMaxY = maxY >10000?decimalFormatter.format(maxY/10000)+"万":(maxY>0.01?decimalFormatter.format(maxY):threeDecimalFormatter.format(maxY));
        String textMinY = minY >10000?decimalFormatter.format(minY/10000)+"万":(minY>0.01?decimalFormatter.format(minY):threeDecimalFormatter.format(minY));
        if (TextUtils.equals(textMaxY,"NaN")||TextUtils.equals(textMinY,"NaN")){
            return;
        }
        canvas.drawText(
                textMaxY,
                labelX,
                indexRect.top - fontMetrics.top,
                yLabelPaint);

        canvas.drawText(
                textMinY,
                labelX,
                indexRect.bottom - fontMetrics.bottom,
                yLabelPaint);
    }

    @Override
    public void onDrawOver(Canvas canvas) {

    }
}
