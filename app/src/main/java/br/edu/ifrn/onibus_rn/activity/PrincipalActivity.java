package br.edu.ifrn.onibus_rn.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.edu.ifrn.onibus_rn.R;
import br.edu.ifrn.onibus_rn.config.ConfiguracaoFirebase;

public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }
    /**
     * Método para exibir os menus na toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /**
     * Método para abrir a tela de chat
     */
    public void abrirChat(View view) {
        startActivity(new Intent(this, ChatActivity.class));
    }

    /**
     * Método para abrir uma mensagem de alerta
     * para o usuário solicitando autorização
     * para o monitoramento do celular
     */
    public void abrirDialog(View view) {
        //Instanciar AlertDialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        //Configurar título e mensagem
        dialog.setTitle("Atenção:");
        dialog.setMessage("Ao acionar este serviço, seu telefone passa a ser" +
                " monitorado até a conclusão do atendimento. Para confirmar, " +
                " clique em 'OK', para cancelar, clique em 'AGORA NÃO'.");

        //Configurar cancelamento
        dialog.setCancelable(false);

        //Configurar ícone
        dialog.setIcon(R.drawable.ic_celular);

        //Configurar ações para sim e não
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(
                        getApplicationContext(),
                        "Você concordou com o monitoramento do celular," +
                                " enviando o chamado agora. ",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
        dialog.setNegativeButton("AGORA NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(
                        getApplicationContext(),
                        "Você clicou em Agora Não. " +
                                "Caso seja necessário, " +
                                "você poderá acionar este serviço a qualquer momento.",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        //Criar e exibir AlertDialog
        dialog.create();
        dialog.show();
    }

    //Método para enviar email através do FloatingActionButton
    public void enviarEmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"atendimento@safebus.com.br"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contato pelo app");
        intent.putExtra(Intent.EXTRA_TEXT, "Mensagem automática");
        //Tipo utilizado para o envio de email
        //intent.setType("message/rfc822"); //Abre direto no GMail
        intent.setType("text/plain"); //Oferece opção do GMail e de outro app de envio de mensagens
        //A partir de uma intent escolhe os apps que podem atender àquela intent
        startActivity(Intent.createChooser(intent, "Compartilhar"));
    }

    //Método para acessar a página termo de uso
    public void paginaTermoDeUso(View view) {
        startActivity(new Intent(this, TermoDeUsoActivity.class));
    }

    //Recuperar os itens selecionados
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSair:
                deslogarUsuario();
                finish();
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
}
