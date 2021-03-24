package br.edu.ifrn.onibus_rn.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import br.edu.ifrn.onibus_rn.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class SobreFragment extends Fragment {

    public SobreFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String descricao = "O aplicativo SafeBus é um projeto que visa auxiliar o cidadão morador do Rio Grande do Norte " +
                "no combate aos casos de assaltos ocorridos dentro do transporte público. " +
                "Apresentado como Trabalho de Conclusão de Curso " +
                "da aluna Denise Maria Leandro de Castro, do Curso Superior de Tecnologia em Sistemas para Internet " +
                "do Instituto Federal de Educação, Ciência e Tecnologia do Rio Grande do Norte, " +
                "campus Parnamirim.";

        Element versao = new Element();
        versao.setTitle("Versão 1.0");

        return new AboutPage(getContext())
                .setImage(R.drawable.ifrn)
                .setDescription(descricao)

                .addGroup("Entre em contato")
                .addEmail("atendimento@safebus.com.br", "Envie um e-mail")
                .addWebsite("https://www.google.com/", "Acesse nosso site")

                .addGroup("Redes Sociais")
                .addFacebook("deniseleandrodecastro", "Facebook")
                .addInstagram("denisemarialeandro", "Instagram")
                .addTwitter("DeniseLCastro", "Twitter")
                .addGitHub("deniseleandrodecastro", "GitHub")
                .addPlayStore("com.facebook.katana", "Download App")
                .addItem(versao)
                .create();

        //return inflater.inflate(R.layout.fragment_sobre, container, false);
    }
}
