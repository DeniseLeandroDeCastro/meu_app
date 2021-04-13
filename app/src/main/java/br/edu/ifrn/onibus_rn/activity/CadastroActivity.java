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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.edu.ifrn.onibus_rn.R;
import br.edu.ifrn.onibus_rn.config.ConfiguracaoFirebase;
import br.edu.ifrn.onibus_rn.helper.Base64Custom;
import br.edu.ifrn.onibus_rn.helper.UsuarioFirebase;
import br.edu.ifrn.onibus_rn.model.Usuario;

public class CadastroActivity extends AppCompatActivity {
    private EditText campoNome, campoEmail, campoSenha, campoLogradouro,
                     campoNumero, campoComplemento, campoTelefone,
                     campoBairro, campoCep, campoCidade, campoEstado;

    private Button botaoCadastrar;
    //Atributo usado para cadastrar usuário no banco de dados do Firebase
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Retorna um objeto que permite alterar a ActionBar
        getSupportActionBar().setTitle("Cadastro");


        /**
         * Para recuperar o conteúdo informado pelo usuário em cada campo
         * é preciso reconhecer cada campo pelo seu id
         */
        campoNome       = findViewById(R.id.editNome);
        campoEmail      = findViewById(R.id.editEmail);
        campoSenha      = findViewById(R.id.editSenha);
        campoLogradouro = findViewById(R.id.editLogradouro);
        campoNumero     = findViewById(R.id.editNumero);
        campoComplemento= findViewById(R.id.editComplemento);
        campoTelefone   = findViewById(R.id.editTelefone);
        campoBairro     = findViewById(R.id.editBairro);
        campoCep        = findViewById(R.id.editCep);
        campoCidade     = findViewById(R.id.editCidade);
        campoEstado     = findViewById(R.id.editEstado);
        botaoCadastrar  = findViewById(R.id.buttonCadastrar);
        /**
         * Configurando o evento de clique
         * do botão cadastrar
         */
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recuperar texto digitado pelo usuário nos campos
                String textoNome        = campoNome.getText().toString();
                String textoEmail       = campoEmail.getText().toString();
                String textoSenha       = campoSenha.getText().toString();
                String textoLogradouro  = campoLogradouro.getText().toString();
                String textoNumero      = campoNumero.getText().toString();
                String textoComplemento = campoComplemento.getText().toString();
                String textoTelefone    = campoTelefone.getText().toString();
                String textoBairro      = campoBairro.getText().toString();
                String textoCep         = campoCep.getText().toString();
                String textoCidade      = campoCidade.getText().toString();
                String textoEstado      = campoEstado.getText().toString();


                //Validar se os campos foram preenchidos
                if(!textoNome.isEmpty()) { //Verifica se o campo nome não está vazio
                    if(!textoEmail.isEmpty()) { //Verifica se o campo email não está vazio
                        if(!textoSenha.isEmpty()) { //Verifica se o campo senha não está vazio
                            if( !textoLogradouro.isEmpty()) { //Verifica se o campo logradouro não está vazio
                                if( !textoNumero.isEmpty()) { //Verifica se o campo Número não está vazio
                                    if( !textoComplemento.isEmpty()) { //Verifica se o campo Complemento não está vazio
                                        if( !textoTelefone.isEmpty()) { //Verifica se o campo Telefone não está vazio
                                            if( !textoBairro.isEmpty()) { //Verifica se o campo Bairro não está vazio
                                                if( !textoCep.isEmpty()) { //Verifica se o campo Cep não está vazio
                                                    if( !textoCidade.isEmpty()) { //Verifica se o campo Cidade não está vazio
                                                        if( !textoEstado.isEmpty()) { //Verifica se o campo Estado não está vazio

                                                            usuario = new Usuario();
                                                            usuario.setNome(textoNome);
                                                            usuario.setEmail(textoEmail);
                                                            usuario.setSenha(textoSenha);
                                                            usuario.setLogradouro(textoLogradouro);
                                                            usuario.setNumero(textoNumero);
                                                            usuario.setComplemento(textoComplemento);
                                                            usuario.setTelefone(textoTelefone);
                                                            usuario.setBairro(textoBairro);
                                                            usuario.setCep(textoCep);
                                                            usuario.setCidade(textoCidade);
                                                            usuario.setEstado(textoEstado);
                                                            cadastrarUsuario();

                                                        } else {
                                                            Toast.makeText(CadastroActivity.this,
                                                                    "Preencha o Estado!",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }

                                                    } else {
                                                        Toast.makeText(CadastroActivity.this,
                                                                "Preencha a Cidade!",
                                                                Toast.LENGTH_SHORT).show();
                                                    }

                                                } else {
                                                    Toast.makeText(CadastroActivity.this,
                                                            "Preencha o Cep!",
                                                            Toast.LENGTH_SHORT).show();
                                                }

                                            } else {
                                                Toast.makeText(CadastroActivity.this,
                                                        "Preencha o Bairro!",
                                                        Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(CadastroActivity.this,
                                                    "Preencha o telefone!",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(CadastroActivity.this,
                                                "Preencha o complemento!",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(CadastroActivity.this,
                                            "Preencha o número!",
                                            Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(CadastroActivity.this,
                                        "Preencha o endereço!",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(CadastroActivity.this,
                                    "Preencha a senha!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CadastroActivity.this,
                                "Preencha o email!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroActivity.this,
                            "Preencha o nome!",
                            Toast.LENGTH_SHORT).show();

                    //Limpar texto
                    botaoCadastrar.setText("");
                }
            }
        });
    }
    /**
     * Configurando o clique no texto termo de uso
     * OnClick
     */
    public void textTermoDeUso (View view) {
        startActivity(new Intent(this, TermoDeUsoActivity.class));
    }
    /**
     * Método para cadastrar o usuário
     */
    public void cadastrarUsuario() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) { //Verifica se o cadastro deu certo
                    Toast.makeText(CadastroActivity.this,
                            "Sucesso ao cadastrar usuário!",
                            Toast.LENGTH_SHORT).show();
                    UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());
                    finish();

                    //Para salvar os dados do usuário
                    try {
                        String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                        usuario.setId(idUsuario);
                        usuario.salvar();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String excecao = "";
                    try {
                        throw  task.getException();
                    } catch( FirebaseAuthWeakPasswordException e ) { //Exceção de senha fraca
                        excecao = "Digite uma senha mais forte!";
                    } catch( FirebaseAuthInvalidCredentialsException e ) { //Formato de email inválido
                        excecao = "Por favor, digite um e-mail válido!";
                    } catch( FirebaseAuthUserCollisionException e ) { //Usuário já cadastrado
                        excecao = "Usuário(a) já existe!";
                    } catch( Exception e ) { //Erro genérico
                        excecao = "Erro ao cadastrar usuário(a)! " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}