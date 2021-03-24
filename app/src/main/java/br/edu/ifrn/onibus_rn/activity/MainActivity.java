package br.edu.ifrn.onibus_rn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import br.edu.ifrn.onibus_rn.R;
import br.edu.ifrn.onibus_rn.config.ConfiguracaoFirebase;

public class MainActivity extends IntroActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //Escondendo os botões de navegação do slide
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        /**
         * Adicionando os slides
         */
        //Slide Faça seu cadastro
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.background_light)
                .fragment(R.layout.intro_1)
                .build());
        //Slide Faça login
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.background_light)
                .fragment(R.layout.intro_2)
                .build());
        //Slide Tire suas dúvidas
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.background_light)
                .fragment(R.layout.intro_3)
                .build());
        //Slide Envie um chamado
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.background_light)
                .fragment(R.layout.intro_4)
                //.canGoForward(false) //Não é possível avançar p/outros slides
                .build());
        //Slide intro_cadastro
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.background_light)
                .fragment(R.layout.intro_cadastro)
                .build());
    }
    //Método do ciclo de vida de uma activity
    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    /**
     * Método para acessar a tela de login
     * através do texto: Já tenho um conta
     */
    public void btEntrar(View view) {
        startActivity(new Intent(this, LoginActivity.class));

    }

    /**
     * Método para acessar a tela de cadastro
     * quando clicar no botão cadastre-se
     */
    public void btCadastrar(View view) {

        startActivity(new Intent(this, CadastroActivity.class));
    }
    /**
     * Método para verificar se o
     * usuário está logado
     */
    public void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //autenticacao.signOut(); //Desloga o usuário
        if( autenticacao.getCurrentUser() != null ) { //Verifica se existe um usuário logado
            abrirTelaPrincipal();
        }
    }
    /**
     * Método que irá abrir a tela principal
     */
    public void abrirTelaPrincipal() {
        startActivity(new Intent(this, PrincipalActivity.class));
    }
}