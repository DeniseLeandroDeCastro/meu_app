package br.edu.ifrn.onibus_rn.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import br.edu.ifrn.onibus_rn.config.ConfiguracaoFirebase;
import br.edu.ifrn.onibus_rn.helper.UsuarioFirebase;

public class Usuario {

    private String idUsuario;
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cep;
    private String cidade;
    private String estado;
    private String telefone;
    private String foto;


    public Usuario() {
    }

    public void atualizar() {
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuariosRef = database.child("usuarios")
                .child(identificadorUsuario);

        Map<String, Object> valoresUsuario = converterParaMap();
        usuariosRef.updateChildren(valoresUsuario);
    }
    public Map<String, Object> converterParaMap() {
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("Id", getIdUsuario());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("email", getEmail());
        usuarioMap.put("senha", getSenha());
        usuarioMap.put("logradouro", getLogradouro());
        usuarioMap.put("numero", getNumero());
        usuarioMap.put("complemento", getComplemento());
        usuarioMap.put("bairro", getBairro());
        usuarioMap.put("cep", getCep());
        usuarioMap.put("cidade", getCidade());
        usuarioMap.put("estado", getEstado());
        usuarioMap.put("telefone", getTelefone());
        usuarioMap.put("foto", getFoto());


        return usuarioMap;
    }

    /*Método para salvar o usuário no Firebase*/
    public void salvar() {
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child(this.idUsuario)
                .child(this.nome)
                .child(this.email)
                .child(this.senha)
                .child(this.logradouro)
                .child(this.numero)
                .child(this.complemento)
                .child(this.bairro)
                .child(this.cep)
                .child(this.cidade)
                .child(this.estado)
                .child(this.telefone)
                .child(this.foto)
                .setValue(this);

    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
