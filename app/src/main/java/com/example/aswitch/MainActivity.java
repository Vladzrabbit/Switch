
/*
 * MainActivity- в этом файле осуществляется открытие меню, основные действия по стабилизации экрана.
 *
 * Version 1.1
 *
 * 08.07 -работа надпередачей контекста
 */
package com.example.aswitch;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity  {

    public TextView textView;
    public SensorManager sensorManager;
    public Sensor sensorLinAccel;
    public Timer timer;
    public TextView tvText;
    EditText editText;
    Button btnLoad;
    final String SAVED_TEXT = "saved_text";
    SharedPreferences sPref;
    private AppSettings settings;
    public SettingActivity setAct;
    public float velocity_str=0.2f; // ВНИМАНИЕ КОСТЫЛЬ!!! Эти значения должны прийти из вкладки Setting Activity
    public float Lowpass_str=0.85f;// ВНИМАНИЕ КОСТЫЛЬ!!! Эти значения должны прийти из вкладки Setting Activity
    public float Position_str=0.1f;// ВНИМАНИЕ КОСТЫЛЬ!!! Эти значения должны прийти из вкладки Setting Activity
    public float Velocity_am_str=10000;// ВНИМАНИЕ КОСТЫЛЬ!!! Эти значения должны прийти из вкладки Setting Activity
    public  PDFView pdfView;


    private final float[] tempAcc = new float[3];
    private final float[] acc = new float[3];
    private final float[] velocity = new float[3];
    private final float[] position = new float[3];
    private long timestamp = 0;
    private boolean accListenerRegistered = false;
    private int x = 0, y = 0;

    private IBinder flinger = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        PDFView pdfView=(PDFView) findViewById(R.id.pdfView);
        pdfView.fromAsset("file.pdf").load();//ткрываю файл в PDF


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorLinAccel = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        textView = (TextView)findViewById(R.id.textView);//тестоовое окно с Пушкиным , которое ясобираюсь стабилизировать
        tvText = (TextView)findViewById(R.id.tvText);

        settings = AppSettings.getAppSettings(getApplicationContext());//перетаскиваю контекст ,пока не работает

        final Switch sw = (Switch) findViewById(R.id.monitored_switch);//Создал свитч на вкл выкл функции
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str1;
                if (sw.isChecked()) {//Состояние свитча
                    str1 = sw.getTextOn().toString();//если вкл
                } else {
                    str1 = sw.getTextOff().toString();//если выкл
                } Toast.makeText(getApplicationContext(),  str1+ " \n" ,Toast.LENGTH_SHORT).show();//вывести наэкран нужнуюстроку

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//создаю обьект меню
        getMenuInflater().inflate(R.menu.menu_main, menu);//привязываю обьект к меню
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {// создаю строчки в меню
        // получим идентификатор выбранного пункта меню
        int id = item.getItemId();


        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.about://Создал 2 страницы , туда куда переносится , если нажать соответствующие пукты меню
                Intent int1=new Intent("com.example.aswitch.SecondActivity"); // обьект в котором хранится ссылка на 2ую страницу "о приложении"
                startActivity(int1);// запускаю
                return true;
            case R.id.action_settings:
                Intent int2=new Intent("com.example.aswitch.SettingActivity");// обьект в котором хранится ссылка на 2ую страницу " настройки"
                startActivity(int2);
                return true;
            default:
                return super.onOptionsItemSelected(item);//хз,но помоему ничего не происходит
        }

    }



    float[] valuesLinAccel = new float[3];

    String format(float values[]) {
        return String.format("%1$.1f\t\t%2$.1f\t\t%3$.1f", values[0], values[1],
                values[2]);
    }



    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
        timer.cancel();
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener, sensorLinAccel,
                SensorManager.SENSOR_DELAY_NORMAL);
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        };
        timer.schedule(task, 0, 400);
    }


    SensorEventListener listener = new SensorEventListener() {
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
                for (int i = 0; i < 3; i++) {
                    valuesLinAccel[i] = event.values[i];
                    if (timestamp != 0)
                    {
                        tempAcc[0] = Utils.rangeValue(event.values[0], -Constants.MAX_ACC, Constants.MAX_ACC);
                        tempAcc[1] = Utils.rangeValue(event.values[1], -Constants.MAX_ACC, Constants.MAX_ACC);
                        tempAcc[2] = Utils.rangeValue(event.values[2], -Constants.MAX_ACC, Constants.MAX_ACC);

                        Utils.lowPassFilter(tempAcc, acc, getLowPassAlpha());//Костыль!

                        float dt = (event.timestamp - timestamp) * Constants.NS2S;

                        for(int index = 0; index < 3; ++index)
                        {
                            velocity[index] += acc[index] * dt - getVelocityFriction() * velocity[index];//Костыль!
                            velocity[index] = Utils.fixNanOrInfinite(velocity[index]);

                            position[index] += velocity[index] * getVelocityAmpl() * dt - getPositionFriction() * position[index];//Костыль!
                            position[index] = Utils.rangeValue(position[index], -Constants.MAX_POS_SHIFT, Constants.MAX_POS_SHIFT);
                        }
                    }
                    else
                    {
                        velocity[0] = velocity[1] = velocity[2] = 0f;
                        position[0] = position[1] = position[2] = 0f;

                        acc[0] = Utils.rangeValue(event.values[0], -Constants.MAX_ACC, Constants.MAX_ACC);
                        acc[1] = Utils.rangeValue(event.values[1], -Constants.MAX_ACC, Constants.MAX_ACC);
                        acc[2] = Utils.rangeValue(event.values[2], -Constants.MAX_ACC, Constants.MAX_ACC);
                    }

                    timestamp = event.timestamp;
                    //textView .setTranslationX(-position[0]);//двигаем изображения
                    //textView .setTranslationY(position[1]);
                    //pdfView.setTranslationX(-position[0]);
                    //pdfView.setTranslationY(position[1]);

                }
    }
    };


    public float getVelocityFriction() //ВНИМАНИЕ!!! КОСТЫЛЬ!!! Дублирует функционал из Setting Activity
    {
        return  velocity_str;
    }
    public float getPositionFriction() //ВНИМАНИЕ!!! КОСТЫЛЬ!!! Дублирует функционал из Setting Activity
    {
        return  Position_str;
    }
    public float getLowPassAlpha() //ВНИМАНИЕ!!! КОСТЫЛЬ!!! Дублирует функционал из Setting Activity
    {
        return  Lowpass_str;
    }
    public float getVelocityAmpl() //ВНИМАНИЕ!!! КОСТЫЛЬ!!! Дублирует функционал из Setting Activity
    {
        return  Velocity_am_str;
    }


}


