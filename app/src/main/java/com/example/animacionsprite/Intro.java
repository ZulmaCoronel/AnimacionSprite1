package com.example.animacionsprite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class Intro extends View {

    Bitmap corgi,texto;
    CountDownTimer timer,siguiente;

    int resulusionx,resulusiony;
    float y=0, y1=1300;
    boolean movimiento;

    public Intro(Context context) {
        super(context);
        Resolucion();

        y1=(float) (resulusiony/1.4768);

        corgi = BitmapFactory.decodeResource(getResources(),R.drawable.gato);
        corgi=escalado(corgi,(resulusiony/5), true);

        texto = BitmapFactory.decodeResource(getResources(),R.drawable.letras);
        texto=escalado(texto,(resulusiony/2), true);

        movimiento = true;
        timer = new CountDownTimer(5000,1) {

            @Override
            public void onTick(long l) {
                //Velocidad con la que aparecen las imagenes en el intro
                if(movimiento ==true) {
                    y += 5;
                    y1 -=5;
                }
                else
                {
                    siguiente.start();
                    timer.cancel();
                }
                if (y >= ((float) (resulusiony/1.4768- corgi.getHeight()) / 2)) {
                    movimiento = false;
                }

                invalidate(); //Vuelve a ejecutar el onDraw
            }
            @Override
            public void onFinish()
            {
                timer.start();
            }
        };
        timer.start();

        //tiempo que tarda en pasar a la siguiente pantalla
        siguiente = new CountDownTimer(3000,1) {
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish()
            {
                if(movimiento == false) {
                    Intent opcion = new Intent(getContext(), MainActivity.class);
                    getContext().startActivity(opcion);
                }
            }
        };
    }
    public void onDraw(Canvas c){
        Paint p = new Paint();
        c.drawBitmap(corgi,(float) (resulusionx/3),y,p);
        c.drawBitmap(texto,(float) (resulusionx/21.6),y1,p);
    }
    public void Resolucion() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        resulusionx = size.x;
        resulusiony = size.y;
        System.out.println("RESOLUCION "+resulusionx+","+resulusiony);

    }
    public static Bitmap escalado(Bitmap imgentrada, float tamanio,  boolean filtro) {
        float ratio = Math.min((float) tamanio / imgentrada.getWidth(), (float) tamanio / imgentrada.getHeight());
        int width = Math.round((float) ratio * imgentrada.getWidth());
        int height = Math.round((float) ratio * imgentrada.getHeight());
        Bitmap nuevaImagen = Bitmap.createScaledBitmap(imgentrada, width, height, filtro);
        return nuevaImagen;
    }


}
