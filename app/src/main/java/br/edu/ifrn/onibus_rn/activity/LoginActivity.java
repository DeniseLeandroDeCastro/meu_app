package br.edu.ifrn.onibus_rn.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.edu.ifrn.onibus_rn.R;
import br.edu.ifrn.onibus_rn.config.ConfiguracaoFirebase;
import br.edu.ifrn.onibus_rn.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button botaoEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Retorna um objeto que permite alterar a ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        campoEmail  = findViewById(R.id.edtEmail);
        campoSenha  = findViewById(R.id.edtSenha);
        botaoEntrar = findViewById(R.id.buttonEntrar);
        /**
         * Configurar evento de clique do
         * botão entrar
         */
        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoEmail   = campoEmail.getText().toString();
                String textoSenha   = campoSenha.getText().toString();

                if(!textoEmail.isEmpty()) { //Verifica se o campo email não está vazio
                    if(!textoSenha.isEmpty()) { //Verifica se o campo senha não está vazio
                        usuario = new Usuario();
                        usuario.setEmail(textoEmail);
                        usuario.setSenha(textoSenha);
                        validarLogin();

                        //Limpar texto
                        botaoEntrar.setText("");
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Preencha a senha!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Preencha o email!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * Método para enviar o usuário
     * para fazer o cadastro
     */
    public void textNaoTemConta(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }
    /**
     * Método para validar o login
     */
    public void validarLogin() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful() ) {
                    abrirTelaPrincipal(); //Após o login, o usuário é direcionado p/tela principal
                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch( FirebaseAuthInvalidUserException e ) {
                        excecao = "Usuário(a) não está cadastrado(a)!";
                    } catch( FirebaseAuthInvalidCredentialsException e ) {
                        excecao = "E-mail e senha não correspondem ao usuário cadastrado!";
                    } catch( Exception e ) { //Erro genérico
                        excecao = "Erro ao cadastrar usuário(a)! " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * Método que irá abrir a tela principal
     */
    public void abrirTelaPrincipal() {
        startActivity(new Intent(this, PrincipalActivity.class));

    }
}