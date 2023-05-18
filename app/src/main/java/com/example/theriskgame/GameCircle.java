package com.example.theriskgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Paint;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.theriskgame.R;

import java.util.ArrayList;

public class GameCircle extends AppCompatActivity {

    RelativeLayout relativeLayout;
    Paint paint;
    View view;
    Path path2;
    Bitmap bitmap;
    Canvas canvas;
    Button button;
    TextView res;
    Button button_validate;
    int score;
    public ArrayList<DrawingClass> DrawingClassArrayList = new ArrayList<DrawingClass>();

    int seconds =15;

    private Runnable chronometer = new Runnable() {

        @Override
        public void run() {
            TextView chronometre = (TextView) findViewById(R.id.chrnonometre);
            while (seconds >= -3) {
                try {
                    String chronometer_string = String.valueOf(seconds);

                    Thread.sleep(1000);
                    //Log.d("chrono : ", chronometer_string);
                    chronometre.setText(chronometer_string);


                } catch (Exception e) {

                }
                seconds -= 1;
                if (seconds == -1) {
                    //todo renvoyé l'attribut score.
                    Log.d("Score renvoie : ", String.valueOf(score));

                    Intent intent_pont = new Intent(GameCircle.this, Pont.class);
                    intent_pont.putExtra("Points", score);
                    String concated = "\n\n T'as fais " + score + " points, pas mal... \n Bon maintenant j'ai besoin que tu sois concentré, tu devras appuyer le plus précisément possible sur 7.77 !!! Prêt ?? Let's gooooo" ;
                    intent_pont.putExtra("Text", concated);
                    intent_pont.putExtra("Next", SeptSeconde.class);
                    startActivity(intent_pont);
                    break;



                }
            }

            ;
        }

        ;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_game);

        Thread chrono = new Thread(chronometer);
        chrono.start();

        relativeLayout = (RelativeLayout) findViewById(R.id.l2);

        button = (Button)findViewById(R.id.button_sup);
        button_validate = (Button)findViewById(R.id.button_ok);
        res = (TextView)findViewById(R.id.result);
        view = new com.example.theriskgame.GameCircle.SketchSheetView(com.example.theriskgame.GameCircle.this);

        paint = new Paint();

        path2 = new Path();
        score = 100;
        relativeLayout.addView(view, new LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));

        paint.setDither(true);

        paint.setColor(Color.parseColor("#000000"));

        paint.setStyle(Paint.Style.STROKE);

        paint.setStrokeJoin(Paint.Join.ROUND);

        paint.setStrokeCap(Paint.Cap.ROUND);

        paint.setStrokeWidth(2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                path2.reset();
                DrawingClassArrayList = new ArrayList<DrawingClass>();

            }
        });

        button_validate.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   if (DrawingClassArrayList.size() < 20) {
                                                       Log.d("Pas assez", "nombre de point" + DrawingClassArrayList.size());
                                                   } else {
                                                       double centerX = 0;
                                                       double centerY = 0 ;

                                                       for (int i =0 ; i < DrawingClassArrayList.size() ; i++){
                                                           Log.d("Tous les points", "X ?" + DrawingClassArrayList.get(i).getX());
                                                           Log.d("Tous les points", "Y ?" + DrawingClassArrayList.get(i).getY());
                                                           centerX += DrawingClassArrayList.get(i).getX();
                                                           centerY += DrawingClassArrayList.get(i).getY();
                                                       }
                                                       centerX = centerX / DrawingClassArrayList.size();
                                                       centerY = centerY / DrawingClassArrayList.size();

                                                       Log.d("Centre du rond en X ", "" + centerX);
                                                       Log.d("Centre du rond en Y", "" + centerY);

                                                       double dist = 0;
                                                       for (int i =0 ; i < DrawingClassArrayList.size() ; i++){
                                                           dist += Math.pow(DrawingClassArrayList.get(i).getX() - centerX, 2) + Math.pow(DrawingClassArrayList.get(i).getY() - centerY, 2);
                                                       }

                                                       dist = Math.pow(dist / DrawingClassArrayList.size(), 0.5);
                                                       Log.d("dist moyenne du cercle", "" + dist);
                                                       if (dist > 60){
                                                       double erreur = 0;
                                                       for (int i =0 ; i < DrawingClassArrayList.size() ; i++){

                                                           double tmp = Math.pow(DrawingClassArrayList.get(i).getX() - centerX, 2) + Math.pow(DrawingClassArrayList.get(i).getY() - centerY,2);
                                                           erreur += Math.abs(Math.pow(tmp, 0.5) - dist) / dist;
                                                       }

                                                       Log.d("erreur", "" + erreur);
                                                       int tmp = (int) ((int) 100 * erreur / DrawingClassArrayList.size());
                                                       Log.d("erreur", "" + tmp);
                                                       score = Math.min(tmp, score);
                                                       res.setText("Erreur :" + score);
                                                   }}
                                               }
                                           }
        );

    }
    public class SketchSheetView extends View {

        public SketchSheetView(Context context) {

            super(context);

            bitmap = Bitmap.createBitmap(820, 480, Bitmap.Config.ARGB_4444);

            canvas = new Canvas(bitmap);

            this.setBackgroundColor(Color.WHITE);
        }



        @Override
        public boolean onTouchEvent(MotionEvent event) {

            com.example.theriskgame.GameCircle.DrawingClass pathWithPaint = new com.example.theriskgame.GameCircle.DrawingClass();

            canvas.drawPath(path2, paint);

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                double x = event.getX();
                double y = event.getY();
                Log.d("TAGgfdgfd", "Position (" + x + "," + y + ")");
                path2.moveTo(event.getX(), event.getY());

                path2.lineTo(event.getX(), event.getY());
            }
            else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                double x = event.getX();
                double y = event.getY();
                Log.d("TAG", "Position (" + x + "," + y + ")");

                path2.lineTo(event.getX(), event.getY());

                pathWithPaint.setX(event.getX());

                pathWithPaint.setY(event.getY());

                pathWithPaint.setPath(path2);

                pathWithPaint.setPaint(paint);

                DrawingClassArrayList.add(pathWithPaint);
            }

            invalidate();
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (DrawingClassArrayList.size() > 0) {

                canvas.drawPath(
                        DrawingClassArrayList.get(DrawingClassArrayList.size() - 1).getPath(),

                        DrawingClassArrayList.get(DrawingClassArrayList.size() - 1).getPaint());
            }
        }
    }

    public class DrawingClass {

        Path DrawingClassPath;
        Paint DrawingClassPaint;


        double x;
        double y;


        public double getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }


        public Path getPath() {
            return DrawingClassPath;
        }

        public void setPath(Path path) {
            this.DrawingClassPath = path;
        }


        public Paint getPaint() {
            return DrawingClassPaint;
        }

        public void setPaint(Paint paint) {
            this.DrawingClassPaint = paint;
        }
    }

}



