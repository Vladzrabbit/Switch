package com.example.aswitch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class SettingActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {


    public static final String APP_PREFERENCES = "mysettings";// имя файла шде будут храниться данные
    public static final String VELOCITY_FRICTION_KEY= "velocity_friction";// имя коунтера
    public static final String LOW_PASS_ALPHA_KEY = "low_pass_alpha";// имя коунтера
    public static final String POSITION_FRICTION_KEY = "position_friction";// имя коунтера
    public static final String VELOCITY_AMPL_KEY = "velocity_ampl";// имя коунтера
    public SharedPreferences mSettings;//отвечает за работу с настройками
    public TextView velocity;// Показывает значения счетчиков
    public TextView Lowpass;
    public TextView Position;
    public TextView Velocity_am;
    public Integer velocity_str;// переменная в которых хранятся значения
    public Integer Lowpass_str;
    public Integer Position_str;
    public Integer Velocity_am_str;
    public SeekBar velocity_s;// Полоса прокрутки
    public SeekBar LowPass_s;
    public SeekBar Position_s;
    public SeekBar velocity_am_s;

    EditText etText;
    Button btnSave;

    SharedPreferences sPref;

    public static final String SAVED_TEXT = "saved_text";
    public static final String APP_PREF= "mysettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);//создание класса совсеми настройкамии передача туда данных

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

         velocity_s = (SeekBar)findViewById(R.id.velocity_s);
        velocity_s.setOnSeekBarChangeListener(this);

        velocity = (TextView)findViewById(R.id.velocity_t);

         LowPass_s = (SeekBar)findViewById(R.id.LowPass_s);
        LowPass_s.setOnSeekBarChangeListener(this);

        Lowpass = (TextView)findViewById(R.id.LowPass_t);

         Position_s = (SeekBar)findViewById(R.id.Position_s);
        Position_s.setOnSeekBarChangeListener(this);

        Position = (TextView)findViewById(R.id.Position_t);

         velocity_am_s = (SeekBar)findViewById(R.id.velocity_am_s);
        velocity_am_s.setOnSeekBarChangeListener(this);

        Velocity_am = (TextView)findViewById(R.id.velocity_am_t);

        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        etText = (EditText) findViewById(R.id.etText);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener((View.OnClickListener) this);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {//изменение значений сверху строки для наглядности
        if (seekBar.getId() == R.id.velocity_s)
        {
            velocity.setText("velocity friction=" + String.valueOf(seekBar.getProgress()));
        }
        else if (seekBar.getId() == R.id.LowPass_s)
        {
            Lowpass.setText("Low pass filter alpha="+String.valueOf(seekBar.getProgress()));
        }
        else if (seekBar.getId() == R.id.Position_s)
        {
            Position.setText("Position friction="+String.valueOf(seekBar.getProgress()));
        }
        else if (seekBar.getId() == R.id.velocity_am_s)
        {
            Velocity_am.setText("Velocity amplification="+String.valueOf(seekBar.getProgress()));
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { // по завершению , если отпустить кнопку , данные запичываются в переменные
        if (seekBar.getId() == R.id.velocity_s)
        {
            velocity_str=seekBar.getProgress();
        }
        else if (seekBar.getId() == R.id.LowPass_s)
        {
            Lowpass_str=seekBar.getProgress();
        }
        else if (seekBar.getId() == R.id.Position_s)
        {
            Position_str=seekBar.getProgress();
        }
        else if (seekBar.getId() == R.id.velocity_am_s)
        {
            Velocity_am_str=seekBar.getProgress();
        }
    }


    @Override
    protected void onResume() {// метод позволяющий менять значения в прямомэфире
        super.onResume();

        if (mSettings.contains(VELOCITY_FRICTION_KEY)) {//Может дать ошибку !!!
            load(mSettings);
            // Выводим на экран данные из настроек
            velocity.setText("velocity friction="+String.valueOf(velocity_str));// выводим текст по запуску приложения
            velocity_s.setProgress(velocity_str);// устанавливаем значение по запуску приложения
            Lowpass.setText("Low pass filter alpha="+String.valueOf(Lowpass_str));
            LowPass_s.setProgress(Lowpass_str);
            Position.setText("Position friction="+String.valueOf(Position_str));
            Position_s.setProgress(Position_str);
            Velocity_am.setText("Velocity amplification="+String.valueOf(Velocity_am_str));
            velocity_am_s.setProgress(Velocity_am_str);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Запоминаем данные
    save(mSettings);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                save(mSettings);
                break;
            default:
                break;
        }
    }


    void load(SharedPreferences mSettings){
        // Получаем число из настроек
        velocity_str = mSettings.getInt(VELOCITY_FRICTION_KEY, 0);// присваиваем значение из счетчиков их файла
        Lowpass_str=mSettings.getInt(LOW_PASS_ALPHA_KEY, 0);
        Position_str=mSettings.getInt(POSITION_FRICTION_KEY, 0);
        Velocity_am_str=mSettings.getInt(VELOCITY_AMPL_KEY , 0);
    }
    public void save(SharedPreferences mSettings)
    {
        SharedPreferences.Editor editor = mSettings.edit();
        save(editor);
        editor.commit();
    }

    public void saveDeferred(SharedPreferences mSettings)
    {
        SharedPreferences.Editor editor = mSettings.edit();
        save(editor);
        editor.apply();
    }

    public void save(SharedPreferences.Editor editor)
    {
        editor.putInt(VELOCITY_FRICTION_KEY, velocity_str);
        editor.putInt(LOW_PASS_ALPHA_KEY, Lowpass_str);
        editor.putInt(POSITION_FRICTION_KEY, Position_str);
        editor.putInt(VELOCITY_AMPL_KEY , Velocity_am_str);
    }

    /*
    void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, etText.getText().toString());//получить тескт который ввел пользователь
        ed.apply();
        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }
    */
    public int getVelocity()
    {
        return  velocity_str;
    }

    public void setVelocity(Integer velocity_str)
    {
        this. velocity_str=  velocity_str;
    }

}

