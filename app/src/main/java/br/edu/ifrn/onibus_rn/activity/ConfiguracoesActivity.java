package br.edu.ifrn.onibus_rn.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import br.edu.ifrn.onibus_rn.R;
import br.edu.ifrn.onibus_rn.config.ConfiguracaoFirebase;
import br.edu.ifrn.onibus_rn.helper.Base64Custom;
import br.edu.ifrn.onibus_rn.helper.Permissao;
import br.edu.ifrn.onibus_rn.helper.UsuarioFirebase;
import br.edu.ifrn.onibus_rn.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracoesActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private ImageButton imageButtonCamera,imageButtonGaleria;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private CircleImageView circleImageViewPerfil;
    private StorageReference storageReference;
    private String identificadorUsuario;
    private EditText editNomeUsuario;
    private ImageView imageAtualizarNome;
    private Usuario usuarioLogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        //Configura????es iniciais
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        //Validar permiss??es
        Permissao.validarPermissoes(permissoesNecessarias, this, 1);

        imageButtonCamera = findViewById(R.id.imageButtonCamera);
        imageButtonGaleria = findViewById(R.id.imageButtonGaleria);
        circleImageViewPerfil = findViewById(R.id.circleImageViewFotoPerfil);
        editNomeUsuario = findViewById(R.id.editNomeUsuario);
        imageAtualizarNome = findViewById(R.id.imageAtualizarNome);

        //Criar uma nova toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Configura????es");
        setSupportActionBar(toolbar);

        //Retorna um objeto que permite alterar a ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperar dados do usu??rio
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        Uri url = usuario.getPhotoUrl();

        if(url != null) {
            Glide.with(ConfiguracoesActivity.this)
                    .load(url)
                    .into(circleImageViewPerfil);
        } else {
            circleImageViewPerfil.setImageResource(R.drawable.padrao);
        }

        editNomeUsuario.setText(usuario.getDisplayName());

        //Configurando o clique dos bot??es de c??mera e de galeria
        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager())!=null) {
                    startActivityForResult(i, SELECAO_CAMERA);
                }
            }
        });
        imageButtonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//Permite escolher foto em um local espec??fico
                if(i.resolveActivity(getPackageManager())!=null) {
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });
        imageAtualizarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editNomeUsuario.getText().toString();
                boolean retorno = UsuarioFirebase.atualizarNomeUsuario(nome);
                if(retorno) {
                    usuarioLogado.setNome(nome);
                    usuarioLogado.atualizar();

                    Toast.makeText(ConfiguracoesActivity.this,
                            "Nome alterado com sucesso!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Bitmap imagem = null;
            try {
                switch(requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }
                if(imagem != null) {
                    circleImageViewPerfil.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Salvar a imagem no firebase
                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            .child(identificadorUsuario + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfiguracoesActivity.this,
                                    "Erro ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ConfiguracoesActivity.this,
                                    "Sucesso ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();

                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizaFotoUsuario(url);
                                }
                            });
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //M??todo para atualizar a foto do usu??rio
    public void atualizaFotoUsuario(Uri url) {
        boolean retorno = UsuarioFirebase.atualizarFotoUsuario(url);
        if(retorno) {
            usuarioLogado.setFoto(url.toString());
            usuarioLogado.atualizar();

            Toast.makeText(ConfiguracoesActivity.this,
                    "Sua foto foi alterada!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int permissaoResultado : grantResults) {
            if(permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }
    }
    private void alertaValidacaoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permiss??es Negadas!");
        builder.setMessage("Para utilizar o app ?? necess??rio aceitar as permiss??es");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}