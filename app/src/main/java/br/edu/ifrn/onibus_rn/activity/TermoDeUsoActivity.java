package br.edu.ifrn.onibus_rn.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

import br.edu.ifrn.onibus_rn.R;
import mehdi.sakout.aboutpage.AboutPage;

public class TermoDeUsoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termo_de_uso);
    }

    public void textConfirmar(View view) {
        startActivity(new Intent(this, PrincipalActivity.class));
    }
}