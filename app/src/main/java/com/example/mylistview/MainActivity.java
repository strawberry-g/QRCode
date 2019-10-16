package com.example.mylistview;

import android.graphics.*;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.graphics.ZebraImageFactory;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private EditText editText;
    private Button create;
    private Button print;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*listView = findViewById(R.id.lv);
        MyListAdapter myListAdapter = new MyListAdapter(MainActivity.this);
        listView.setAdapter(myListAdapter);*/

        editText  = findViewById(R.id.edit);
        print = findViewById(R.id.print);
        create = findViewById(R.id.create);
        imageView = findViewById(R.id.image);

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                Bitmap bitmap = generateBitmap(content,180,180);
                Bitmap textBitmap = addText("测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试",180,180);
                Bitmap result = mixtureBitmap(bitmap,textBitmap);
                doPrintQRCode(result);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                Bitmap bitmap = generateBitmap(content,180,180);
                Bitmap textBitmap = addText("测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试",180,180);
                Bitmap result = mixtureBitmap(bitmap,textBitmap);
                imageView.setImageBitmap(result);
            }
        });
    }

    private void doPrintQRCode(final Bitmap bitmap) {
        // 开启一个子线程
        new Thread() {
            public void run() {
                TcpConnection connection = new TcpConnection("192.168.1.17", TcpConnection.DEFAULT_ZPL_TCP_PORT);
                try {
                    connection.open();
                    if (connection.isConnected()) {
                        try {
                            ZebraPrinter printer = ZebraPrinterFactory.getInstance(PrinterLanguage.ZPL, connection);
                            Looper.prepare();
                            /*
                            //打印图片的另一种方法
                            String str = "^XA\n^FO170,30\n^XGR:IMAGE.GRF,1,1^FS^XZ";
                            printer.storeImage("R:IMAGE.GRF", ZebraImageFactory.getImage(bitmap), 360, 360);
                            connection.write(str.getBytes());
                            */
                            printer.printImage(ZebraImageFactory.getImage(bitmap), 0, 20, 720, 360, false);
                            //确保数据已进入打印机
                            Thread.sleep(500);
                            //释放资源
                            connection.close();
                            Looper.myLooper().quit();
                        } catch (Exception ex) {
                            connection.close();
                            Thread.sleep(5000);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 生成二维码图片
     * @param content 生成二维码的文字
     * @param width   二维码宽度
     * @param height  二维码高度
     */
    @Nullable
    private Bitmap generateBitmap(String content, int width, int height) {
        if (content.isEmpty()){
            return null;
        }
        if (width < 0 || height < 0){
            return null;
        }
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        //1、设置二维码的相关配置
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //内容所使用编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //设置二维码四周白色区域的大小
        hints.put(EncodeHintType.MARGIN, 1);
        //设置二维码的容错性
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        try {//构建二维码图片,QR_CODE 一种矩阵二维码
            //2、将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            //3、创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                    if (encode.get(j, i)) {
                        //黑色色块像素设置
                        pixels[i * width + j] = 0xFF000000;
                    } else {
                        //白色色块像素设置
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            //4、创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels,0,width,0,0,width,height);
            //Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888)
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    //添加文字
    private Bitmap addTextBitmap(Bitmap bitmapSrc,String text){
        int srcWidth = bitmapSrc.getWidth();
        int srcHeight = bitmapSrc.getHeight();
        //计算text所需要的height
        int textSize = 12;
        int padding = 2;
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
            String lineText;
            for (int i = 0, startY = srcHeight + textSize, start, end; i < lineNum; i++) {
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

    //文字bitmap
    public Bitmap addText(String text,int width,int height){
        int textSize = 12;
        int padding = 3;
        int textLinePadding = 3;
        int perLineWords = (width - 2 * padding) / textSize;
        int lineNum = text.length() / perLineWords;
        lineNum = text.length() % perLineWords == 0 ? lineNum : lineNum + 1;

        Bitmap textBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);

       try {
           Canvas canvas = new Canvas(textBitmap);
           canvas.drawColor(Color.WHITE);

           Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
           paint.setColor(Color.BLACK);
           paint.setTextSize(textSize);

           int startX = (width - textSize * perLineWords) / 2;
           int startY = height / 3;
           String lineText;
           int start,end;

           for (int i = 0; i < lineNum; i++){
               start = i * perLineWords;
               end = start + perLineWords;
               lineText = text.substring(start, end > text.length() ? text.length() : end);
               canvas.drawText(lineText,startX,startY,paint);
               startY = startY + textSize + textLinePadding;
           }
           canvas.save();
           canvas.restore();
       }catch (Exception e){
           e.printStackTrace();
       }
        return textBitmap;
    }

    //合并两个bitmap
    public Bitmap mixtureBitmap(Bitmap first,Bitmap second){
        int marginPadding = 8;
        Bitmap newBitmap = Bitmap.createBitmap(first.getWidth() + second.getWidth() + marginPadding,
                first.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cv = new Canvas(newBitmap);
        cv.drawColor(Color.WHITE);
        cv.drawBitmap(first, marginPadding, 0, null);
        cv.drawBitmap(second, first.getWidth() + marginPadding,0, null);
        cv.save();
        cv.restore();

        return newBitmap;
    }
}
