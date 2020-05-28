package com.example.cvtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MenuOpcoes extends AppCompatActivity {

    void definirLinhas(){
        Button bt_finalizar = (Button) findViewById(R.id.Finalizar);
        bt_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(MenuOpcoes.this,MainActivity.class);
                inte.putExtra("teste","msg de teste");
                startActivity(inte);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_menu_opcoes);
        Intent in = getIntent();
        Bundle b = in.getBundleExtra("coordPontos");
        int qtdLinhas = b.getInt("qtdLinhas");
        double p1x = b.getDouble("Ponto1x");
        double p1y = b.getDouble("Ponto1y");
        Toast.makeText(getApplicationContext(), "nPontos=" + Integer.toString(qtdLinhas) + ", (" + Double.toString(p1x) + "," + Double.toString(p1y) + ")", Toast.LENGTH_SHORT).show();
        definirLinhas();//Observa o comportamento do botão
    }
}
