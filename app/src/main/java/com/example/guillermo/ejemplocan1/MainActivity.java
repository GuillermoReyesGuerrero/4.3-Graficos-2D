package com.example.guillermo.ejemplocan1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private int[] numeros={1,2,3,4,5,6,7,8,9,10,11,12};
    private Rect rect = new Rect();
    private int radio,handTruncation, hourHandTruncation=0;
    public int alto,ancho = 0;
    private boolean started = false;
    public int horas,minutos,segundos =0;
    TextView mostrarhora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mostrarhora=findViewById(R.id.txthora);

        PapelView papel = new PapelView(this);

        setContentView(papel);

    }

    private class PapelView extends View {

        public PapelView(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // Objeto Paint
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);

            // Pintar el canvas
            canvas.drawPaint(paint);

            // Obtener dimensiones
            int ancho = canvas.getWidth();
            int alto = canvas.getHeight();
            paint.setColor(Color.WHITE);
            paint.setTextSize(30);
            paint.setAntiAlias(true);

            // Texto
            //canvas.drawText("ancho" + ancho + "altura" + alto, 70, 70, paint);

            // Lineas
            //paint.setColor(Color.BLACK);
            //canvas.drawLine(0, 40, ancho, 40, paint);
            //canvas.drawLine(20, 0, 20, alto, paint);

            paint.setTextSize(30);

            int min = Math.min(alto,ancho);
            radio=min/2-50;
            handTruncation = min / 20;
            hourHandTruncation = min / 7;


            //Pintar numeros
            for (int n : numeros) {
                String tmp = String.valueOf(n);
                paint.getTextBounds(tmp, 0, tmp.length(), rect);
                double angle = Math.PI / 6 * (n - 3);
                int x = (int) (ancho / 2 + Math.cos(angle) * radio - rect.width() / 2);
                int y = (int) (alto / 2 + Math.sin(angle) * radio + rect.height() / 2);
                canvas.drawText(tmp, x, y, paint);

            }
            //Pintar circulo centro
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            canvas.drawCircle(ancho / 2, alto / 2, 10, paint);

            //Pintar circulo contorno
            paint.setColor(getResources().getColor(android.R.color.holo_red_dark));

            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(ancho / 2, alto / 2, radio + 50 - 10, paint);

            //Relojcon hilo
            if(!started){
                started=true;
                new Thread() {
                    public void run() {
                        try{
                            for(horas=0;horas<=12;horas++){
                                for(minutos=0;minutos<=60;minutos+=10){
                                    for(segundos=0;segundos<=60;segundos++){
                                        Thread.sleep(30);
                                    }
                                }
                                if(horas==12)horas=0;
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }

                    }
                }.start();
            }

            //Pintar manecillas
            //Calendar c = Calendar.getInstance();
            //float hour = c.get(Calendar.HOUR_OF_DAY);
            //hour = hour > 12 ? hour - 12 : hour;
            //double loc= (hour + c.get(Calendar.MINUTE) / 60) * 5f;
            paint.setColor(Color.WHITE);

            double loc = horas * 5 - (60-minutos)/60;
            double angle = Math.PI * loc / 30 - Math.PI / 2;
            boolean isHour=true;
            int handRadius = isHour ? radio - handTruncation - hourHandTruncation : radio - handTruncation;
            canvas.drawLine(ancho / 2, alto / 2,
                    (float) (ancho / 2 + Math.cos(angle) * handRadius),
                    (float) (alto / 2 + Math.sin(angle) * handRadius),
                    paint);

            //double loc2= c.get(Calendar.MINUTE);
            double loc2=minutos;
            double angle2 = Math.PI * loc2 / 30 - Math.PI / 2;
            boolean isHour2=false;
            int handRadius2 = isHour2 ? radio - handTruncation - hourHandTruncation : radio - handTruncation;
            canvas.drawLine(ancho / 2, alto / 2,
                    (float) (ancho / 2 + Math.cos(angle2) * handRadius2),
                    (float) (alto / 2 + Math.sin(angle2) * handRadius2),
                    paint);

            //double loc3= (c.get(Calendar.SECOND)*10);
            double loc3=segundos;
            double angle3 = Math.PI * loc3 / 30 - Math.PI / 2;
            boolean isHour3=false;
            int handRadius3 = isHour3 ? radio - handTruncation - hourHandTruncation : radio - handTruncation;
            canvas.drawLine(ancho / 2, alto / 2,
                    (float) (ancho / 2 + Math.cos(angle3) * handRadius3),
                    (float) (alto / 2 + Math.sin(angle3) * handRadius3),
                    paint);

            postInvalidateDelayed(500);
            invalidate();

        }
    }
}
