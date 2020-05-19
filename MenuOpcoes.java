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
        definirLinhas();//Observa o comportamento do botão
    }
}
