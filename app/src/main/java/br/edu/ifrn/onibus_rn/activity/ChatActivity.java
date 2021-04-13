package br.edu.ifrn.onibus_rn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import br.edu.ifrn.onibus_rn.R;
import br.edu.ifrn.onibus_rn.config.ConfiguracaoFirebase;
import br.edu.ifrn.onibus_rn.fragment.ContatosFragment;
import br.edu.ifrn.onibus_rn.fragment.ConversasFragment;
import br.edu.ifrn.onibus_rn.fragment.SobreFragment;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //Criar uma nova toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Chat");
        setSupportActionBar(toolbar);

        //Configurar abas
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Conversas", ConversasFragment.class)
                        .add("Contatos", ContatosFragment.class)
                        .add("Sobre", SobreFragment.class)
                        .create()
        );
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewPagerTab);
        viewPagerTab.setViewPager(viewPager);
    }
    //Criar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Recuperar os itens selecionados
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSair:
                deslogarUsuario();
                finish();
                break;
            case R.id.menuConfiguracoes:
                abrirConfiguracoes();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //Método do item de menu sair
    public void deslogarUsuario() {
        try {
            autenticacao.signOut();
            startActivity(new Intent(this, LoginActivity.class));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //Método para abrir a página de configurações
    public void abrirConfiguracoes() {
        Intent intent = new Intent(ChatActivity.this, ConfiguracoesActivity.class);
        startActivity(intent);
    }
}