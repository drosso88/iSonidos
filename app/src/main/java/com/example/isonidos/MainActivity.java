package com.example.isonidos;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.VideoView;

import java.lang.reflect.Field;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    HashMap<String, String>listaSonidos=new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int numeroLinea=0;
        LinearLayout auxiliar = creaLineaBotones(numeroLinea);
        LinearLayout principal = (LinearLayout) findViewById(R.id.botones);
        principal.addView(auxiliar);
        Field[] listaCanciones = R.raw.class.getFields();
        int columnas =5;
        for (int i=0; i<listaCanciones.length;i++){
            //creamos el boton x codigo y lo añadimos a la pantalla ppal
            Button b = creaBoton (i, listaCanciones);
            //añadimos el boton al layout
            auxiliar.addView(b);
            if(i % columnas == columnas-1){
                auxiliar = creaLineaBotones(i);
                principal.addView(auxiliar);
            }
                    listaSonidos.put(b.getTag().toString(),b.getText().toString());
            b.setText(acortaEtiquetaBoton((b.getText().toString())));
        }
    }

    private LinearLayout creaLineaBotones(int numeroLinea){
        LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        parametros.weight =1;
        LinearLayout linea = new LinearLayout(this);
        linea.setOrientation(LinearLayout.HORIZONTAL);
        linea.setLayoutParams(parametros);
        linea.setId(numeroLinea);
        return linea;
    }
    private Button creaBoton(int i, Field[] listaCanciones){
        LinearLayout.LayoutParams parametrosBotones= new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        parametrosBotones.weight=1;
        parametrosBotones.setMargins(5,5,5,5);
        parametrosBotones.gravity = Gravity.CENTER_HORIZONTAL;
        //boton x codigo
        Button b = new Button(this);
        b.setLayoutParams(parametrosBotones);
        b.setText(listaCanciones[i].getName());
        b.setTextColor(Color.MAGENTA);
        b.setTextSize(10);
        b.setBackgroundColor(Color.CYAN);
        b.setAllCaps(true);//para tener las letras o mayusculas o minusculas
        //nombres para las canciones
        int id = this.getResources().getIdentifier(listaCanciones[i].getName(),"raw", this.getPackageName());
        String nombreLargo = listaCanciones[i].getName();

        //distinguimos los videos x colores
        if(nombreLargo!=null && nombreLargo.substring(0,2).contains("v_")){
            b.setBackgroundColor(Color.rgb(255,140,0));
        }
        b.setTag(id);
        b.setId(i+50);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sonido(view);

            }
        });
        return b;
    }

    public void sonido (View view){
        //identificamos el boton pulsado
        Button b =(Button) findViewById(view.getId());
        String nombre = listaSonidos.get(view.getTag().toString());
        //para video
        if(nombre.substring(0,2).contains("v_")) {
            VideoView videoView = (VideoView) findViewById(R.id.videoView);
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + view.getTag());
            videoView.setVideoURI(uri);
            videoView.start();
        }else {
            //para sonido
            MediaPlayer m = new MediaPlayer();
            m= MediaPlayer.create(this, (int) findViewById(view.getId()).getTag());
            m.start();
            m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                    if(mediaPlayer!=null){
                        mediaPlayer.release();
                    }

                }
            });
    }

    }

private String acortaEtiquetaBoton(String s){
    if(s.substring(0,2).contains("v_")){
        s=s.substring(s.indexOf('_')+1);
    }
    if (s.contains("_")) {s= s.substring(s.indexOf('_')); }
    s.replace('_', ' ');
    return s;

    }
}
