package com.example.mylistview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Test {
    //添加文字
    private Bitmap addTextBitmap(Bitmap bitmapSrc, String text){
        int srcWidth = bitmapSrc.getWidth();
        int srcHeight = bitmapSrc.getHeight();
        //计算text所需要的height
        int textSize = 12;
        int padding = 5;
        int textLinePadding = 1;
        //每行的文字(纵向)
        int perLineWords = (srcWidth - 2 * padding) / textSize;
        //行数
        int lineNum = text.length() / perLineWords;
        lineNum = text.length() % perLineWords == 0 ? lineNum : lineNum + 1;
        //文字总高度
        int textTotalHeight = lineNum * (textSize + textLinePadding) + 2 * padding;

        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight + textTotalHeight,Bitmap.Config.ARGB_8888);

        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bitmapSrc, 0, 0, null);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLACK);
            paint.setTextSize(textSize);
            /*
           //Typeface.MONOSPACE等宽字体类型 Typeface.BOLD粗体
           String familyName = "仿宋";
           Typeface font = Typeface.create(familyName, Typeface.BOLD);
           paint.setTypeface(font);*/

            String lineText;
            int startY = srcHeight + textSize;
            int start, end;
            for (int i = 0; i < lineNum; i++) {
                start = i * perLineWords;
                end = start + perLineWords;
                lineText = text.substring(start, end > text.length() ? text.length() : end);
                canvas.drawText(lineText, (srcWidth - perLineWords * textSize) / 2, startY, paint);
                startY += textSize + textLinePadding;
            }
            canvas.save();
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }
}
