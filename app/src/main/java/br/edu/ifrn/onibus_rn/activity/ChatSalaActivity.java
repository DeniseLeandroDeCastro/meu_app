package br.edu.ifrn.onibus_rn.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.edu.ifrn.onibus_rn.R;
import br.edu.ifrn.onibus_rn.adapter.MensagensAdapter;
import br.edu.ifrn.onibus_rn.config.ConfiguracaoFirebase;
import br.edu.ifrn.onibus_rn.helper.Base64Custom;
import br.edu.ifrn.onibus_rn.helper.UsuarioFirebase;
import br.edu.ifrn.onibus_rn.model.Mensagem;
import br.edu.ifrn.onibus_rn.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;


public class ChatSalaActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private EditText editMensagem;
    private ImageView imageCamera;
    private Usuario usuarioDestinatario;
    private Usuario usuarioRemetente;
    private DatabaseReference database;
    private StorageReference storage;
    private DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;

    //Identificadores usu??rios remetente e destinat??riio
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;

    private RecyclerView recyclerMensagens;
    private MensagensAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<>();

    private static final int SELECAO_CAMERA = 100;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_sala);

        //Configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configuracoes iniciais
        textViewNome        = findViewById(R.id.textViewNomeChat);
        circleImageViewFoto = findViewById(R.id.circleImageFotoChat);
        editMensagem        = findViewById(R.id.editMensagem);
        recyclerMensagens   = findViewById(R.id.recyclerMensagens);
        imageCamera         = findViewById(R.id.imageCamera);

        //recupera dados do usuario remetente
        idUsuarioRemetente = UsuarioFirebase.getIdentificadorUsuario();
        usuarioRemetente   = UsuarioFirebase.getDadosUsuarioLogado();

        //Recuperar dados do usu??rio destinatario
        Bundle bundle = getIntent().getExtras();
        if ( bundle !=  null ){

            usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");
            textViewNome.setText( usuarioDestinatario.getNome() );

            String foto = usuarioDestinatario.getFoto();
            if ( foto != null ){
                Uri url = Uri.parse(usuarioDestinatario.getFoto());
                Glide.with(ChatSalaActivity.this)
                        .load(url)
                        .into( circleImageViewFoto );
            }else {
                circleImageViewFoto.setImageResource(R.drawable.padrao);
            }

            //recuperar dados usuario destinatario
            idUsuarioDestinatario = Base64Custom.codificarBase64( usuarioDestinatario.getEmail() );
        }

        //Configura????o adapter
        adapter = new MensagensAdapter(mensagens, getApplicationContext() );

        //Configura????o recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager( layoutManager );
        recyclerMensagens.setHasFixedSize( true );
        recyclerMensagens.setAdapter( adapter );

        database = ConfiguracaoFirebase.getFirebaseDatabase();
        storage = ConfiguracaoFirebase.getFirebaseStorage();
        mensagensRef = database.child("mensagens")
                .child( idUsuarioRemetente )
                .child( idUsuarioDestinatario );

        //Evento de clique na c??mera
        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if( i.resolveActivity( getPackageManager() ) !=null ) {
                    startActivityForResult( i, SELECAO_CAMERA );
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK ) {
            Bitmap imagem = null;
            try {
                switch(requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                }
                if( imagem != null ){
                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Criar nome da imagem
                    String nomeImagem = UUID.randomUUID().toString();

                    //Configurar refer??ncia do firebase
                    final StorageReference imagemRef = storage.child( "imagens" )
                            .child( "fotos" )
                            .child( idUsuarioRemetente )
                            .child( nomeImagem );

                    UploadTask uploadTask = imagemRef.putBytes( dadosImagem );
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d( "Erro", "Erro ao fazer upload" );
                            Toast.makeText( ChatSalaActivity.this,
                                    "Erro ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imagemRef.getDownloadUrl().addOnCompleteListener(
                                    new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            Uri url = task.getResult();

                                            Mensagem mensagem = new Mensagem();
                                            mensagem.setIdUsuario( idUsuarioRemetente );
                                            mensagem.setMensagem( "imagem.jpeg" );
                                            mensagem.setImagem( toString() );

                                            //Salvar mensagem remetente
                                            salvarMensagem( idUsuarioRemetente, idUsuarioDestinatario, mensagem );

                                            //Salvar mensagem destinat??rio
                                            salvarMensagem( idUsuarioDestinatario, idUsuarioRemetente, mensagem );

                                            Toast.makeText( ChatSalaActivity.this,
                                                    "Sucesso ao enviar imagem",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                        }
                    });
                }
            } catch( Exception e ) {
                e.printStackTrace();
            }
        }
    }

    public void enviarMensagem(View view){

        String textoMensagem = editMensagem.getText().toString();

        if ( !textoMensagem.isEmpty() ){

            Mensagem mensagem = new Mensagem();
            mensagem.setIdUsuario( idUsuarioRemetente );
            mensagem.setMensagem( textoMensagem );

            //Salvar mensagem para o remetente
            salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

            //Salvar mensagem para o destinatario
            salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

        }else {
            Toast.makeText(ChatSalaActivity.this,
                    "Digite uma mensagem para enviar!",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem msg){

        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference mensagemRef = database.child("mensagens");

        mensagemRef.child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(msg);

        //Limpar texto
        editMensagem.setText("");

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener( childEventListenerMensagens );
    }

    private void recuperarMensagens(){

        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Mensagem mensagem = dataSnapshot.getValue( Mensagem.class );
                mensagens.add( mensagem );
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}