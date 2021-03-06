package br.edu.ifrn.onibus_rn.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference firebase;
    private static StorageReference storage;

    /**
     * Método que retorna
     * a instancia do FirebaseDatabase
     */
    public static DatabaseReference getFirebaseDatabase() {
        if(firebase == null) {
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }

    /**
     * Método que retorna a
     * instancia do FirebaseAuth
     */
    public static FirebaseAuth getFirebaseAutenticacao() {
        if(autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
    //Para salvar no storage
    public static StorageReference getFirebaseStorage() {
        if(storage == null) {
            storage = FirebaseStorage
                    .getInstance()
                    .getReference();
        }
        return storage;
    }
}
