package com.joanmanera.simondice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bStart, bBestScores;
    private TextView tvPuntuacion;
    private AppCompatButton bVerde, bRojo, bAmarillo, bAzul;
    private LinearLayout llPuntuacion, llListaPuntuaciones;
    private ArrayList<Integer> listaColores;
    private ArrayList<Integer> listaColoresJugador;
    private ArrayList<Integer> puntuaciones;
    private Jugar juego;
    private int clicks;
    private int beep01, beep02, beep03, beep04;
    private SoundPool soundPool;
    private ListView listaPuntuaciones;
    private ImageButton ibBack;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaColores = new ArrayList<>();
        cargarSonidos();
        iniciarBotones();
        puntuaciones = new ArrayList<>();
        cargarPreferencias();

    }

    private void iniciarBotones(){
        bStart = findViewById(R.id.bStart);
        bBestScores = findViewById(R.id.bBestScores);
        llPuntuacion = findViewById(R.id.llPuntuacion);
        llListaPuntuaciones = findViewById(R.id.llMejoresPuntuaciones);
        tvPuntuacion = findViewById(R.id.tvPuntuacion);
        ibBack = findViewById(R.id.ibBack);
        listaPuntuaciones = findViewById(R.id.lvBestScores);
        llListaPuntuaciones.setVisibility(View.INVISIBLE);

        bVerde = findViewById(R.id.bVerde);
        bRojo = findViewById(R.id.bRojo);
        bAmarillo = findViewById(R.id.bAmarillo);
        bAzul = findViewById(R.id.bAzul);

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_pressed}, // pressed
                new int[]{android.R.attr.state_enabled} // enabled
        };

        int[] colors;
        colors = new int[]{
                ContextCompat.getColor(MainActivity.this, R.color.verdeClick),
                ContextCompat.getColor(MainActivity.this, R.color.verde)
        };
        bVerde.setSupportBackgroundTintList(new ColorStateList(states, colors));

        colors = new int[]{
                ContextCompat.getColor(MainActivity.this, R.color.rojoClick),
                ContextCompat.getColor(MainActivity.this, R.color.rojo)
        };
        bRojo.setSupportBackgroundTintList(new ColorStateList(states, colors));

        colors = new int[]{
                ContextCompat.getColor(MainActivity.this, R.color.amarilloClick),
                ContextCompat.getColor(MainActivity.this, R.color.amarillo)
        };
        bAmarillo.setSupportBackgroundTintList(new ColorStateList(states, colors));

        colors = new int[]{
                ContextCompat.getColor(MainActivity.this, R.color.azulClick),
                ContextCompat.getColor(MainActivity.this, R.color.azul)
        };
        bAzul.setSupportBackgroundTintList(new ColorStateList(states, colors));


        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llPuntuacion.setVisibility(View.INVISIBLE);
                iniciarJuego();
            }
        });
        bBestScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0 ; i < puntuaciones.size() ; i++){
                    int puntuacion = puntuaciones.get(i);
                    if(puntuacion == 0){
                        puntuaciones.remove(i);
                    }
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, puntuaciones);
                listaPuntuaciones.setAdapter(arrayAdapter);
                llListaPuntuaciones.setVisibility(View.VISIBLE);
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llListaPuntuaciones.setVisibility(View.INVISIBLE);
            }
        });

        bVerde.setOnClickListener(this);
        bAzul.setOnClickListener(this);
        bAmarillo.setOnClickListener(this);
        bRojo.setOnClickListener(this);
        botonesClickable(false);

    }

    private void cargarPreferencias() {
        SharedPreferences sharedPreferences = getSharedPreferences("puntuaciones", MODE_PRIVATE);


        for(int i = 0 ; i < 10 ; i++){
            int puntuacion = sharedPreferences.getInt("puntuaciones", 0);
            puntuaciones.add(puntuacion);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void cargarSonidos(){
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(audioAttributes)
                .build();
        beep01 = soundPool.load(this, R.raw.beep01, 1);
        beep02 = soundPool.load(this, R.raw.beep02, 1);
        beep03 = soundPool.load(this, R.raw.beep03, 1);
        beep04 = soundPool.load(this, R.raw.beep04, 1);
    }

    private void iniciarJuego(){
        clicks = 0;
        listaColores = new ArrayList<>();
        juego = new Jugar();
        juego.execute();
    }

    @Override
    public void onClick(View v) {
        clicks++;
        switch (v.getId()){
            case R.id.bVerde:
                listaColoresJugador.add(1);
                soundPool.play(beep01, 1, 1, 1, 0, 1);
                break;
            case R.id.bRojo:
                listaColoresJugador.add(2);
                soundPool.play(beep02, 1, 1, 1, 0, 1);
                break;
            case R.id.bAmarillo:
                listaColoresJugador.add(3);
                soundPool.play(beep03, 1, 1, 1, 0, 1);
                break;
            case R.id.bAzul:
                listaColoresJugador.add(4);
                soundPool.play(beep04, 1, 1, 1, 0, 1);
                break;
        }
        comprobarJugada();
    }

    private void comprobarJugada(){
       if(listaColoresJugador.get(clicks -1) != listaColores.get(clicks -1)){
           llPuntuacion.setVisibility(View.VISIBLE);
           juego.cancel(true);
           botonesClickable(false);
           tvPuntuacion.setText(String.valueOf(listaColores.size()-1));
           puntuaciones.add(listaColores.size()-1);
       } else if(clicks >= listaColores.size()){
           juego = new Jugar();
           juego.execute();
       }
    }

    private void botonesClickable(boolean isClickable){
        bAmarillo.setClickable(isClickable);
        bAzul.setClickable(isClickable);
        bRojo.setClickable(isClickable);
        bVerde.setClickable(isClickable);
    }

    private class Jugar extends AsyncTask<Void, Integer, Boolean>{

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            bAmarillo.setPressed(false);
            bAzul.setPressed(false);
            bRojo.setPressed(false);
            bVerde.setPressed(false);
            botonesClickable(false);


            clicks = 0;
            listaColoresJugador = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0 ; i < listaColores.size() ; i++){
                try{
                    Thread.sleep(400);
                    publishProgress(listaColores.get(i));
                    Thread.sleep(400);
                    publishProgress(listaColores.get(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            listaColores.add(getRandom());
            try{
                Thread.sleep(400);
                publishProgress(listaColores.get(listaColores.size()-1));
                Thread.sleep(400);
                publishProgress(listaColores.get(listaColores.size()-1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            switch (values[0]){
                case 1:
                    if(bVerde.isPressed()){
                        bVerde.setPressed(false);
                    } else {
                        soundPool.play(beep01, 1, 1, 1, 0, 1);
                        bVerde.setPressed(true);
                    }
                    break;
                case 2:
                    if(bRojo.isPressed()){
                        bRojo.setPressed(false);
                    } else {
                        soundPool.play(beep02, 1, 1, 1, 0, 1);
                        bRojo.setPressed(true);
                    }
                    break;
                case 3:
                    if(bAmarillo.isPressed()){
                        bAmarillo.setPressed(false);
                    } else {
                        soundPool.play(beep03, 1, 1, 1, 0, 1);
                        bAmarillo.setPressed(true);
                    }
                    break;
                case 4:
                    if(bAzul.isPressed()){
                        bAzul.setPressed(false);
                    } else {
                        soundPool.play(beep04, 1, 1, 1, 0, 1);
                        bAzul.setPressed(true);
                    }
                    break;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Toast.makeText(MainActivity.this, "Go", Toast.LENGTH_LONG).show();
            botonesClickable(true);
        }

    }

    private int getRandom(){
        Random r = new Random();
        return r.nextInt(4) + 1;
    }
}
