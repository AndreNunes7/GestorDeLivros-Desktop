package com.mycompany.firebaseproject;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LivrosDAL {
    private static final Logger LOGGER = Logger.getLogger(LivrosDAL.class.getName());
    private static Firestore db;

    static {
        inicializaFirebase();
    }

    private static void inicializaFirebase() {
        try {
            FileInputStream serviceAccount = new FileInputStream("arquivo.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao inicializar o Firebase", e);
        }
    }

    public static void insereLivro(Livro livro) {
        try {
            checkFirestoreInitialized();
            Map<String, Object> livroData = createLivroMap(livro);
            ApiFuture<WriteResult> future = db.collection("livros").document().set(livroData);
            LOGGER.info("Livro inserido com sucesso em: " + future.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.SEVERE, "Erro ao inserir livro", e);
        }
    }

    public static void consultaLivro(Livro livro) {
        try {
            checkFirestoreInitialized();
            ApiFuture<QuerySnapshot> future = db.collection("livros")
                    .whereEqualTo("titulo", livro.getTitulo())
                    .get();
            QuerySnapshot querySnapshot = future.get();
            if (!querySnapshot.getDocuments().isEmpty()) {
                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                updateLivroFromDocument(livro, document);
                LOGGER.info("Livro encontrado.");
            } else {
                LOGGER.info("Livro não localizado!");
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.SEVERE, "Erro ao consultar livro", e);
        }
    }

    public static void deletaLivro(Livro livro) {
        try {
            checkFirestoreInitialized();
            ApiFuture<QuerySnapshot> future = db.collection("livros")
                    .whereEqualTo("titulo", livro.getTitulo())
                    .get();
            QuerySnapshot querySnapshot = future.get();
            if (!querySnapshot.getDocuments().isEmpty()) {
                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                ApiFuture<WriteResult> writeResult = document.getReference().delete();
                LOGGER.info("Livro deletado em: " + writeResult.get().getUpdateTime());
            } else {
                LOGGER.info("Nenhum livro encontrado para deletar.");
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.SEVERE, "Erro ao deletar livro", e);
        }
    }

    public static void alteraLivro(Livro livro) {
        try {
            checkFirestoreInitialized();
            Map<String, Object> livroData = createLivroMap(livro);
            ApiFuture<QuerySnapshot> future = db.collection("livros")
                    .whereEqualTo("titulo", livro.getTitulo())
                    .get();
            QuerySnapshot querySnapshot = future.get();
            if (!querySnapshot.getDocuments().isEmpty()) {
                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                ApiFuture<WriteResult> updateFuture = document.getReference().update(livroData);
                LOGGER.info("Livro atualizado com sucesso em: " + updateFuture.get().getUpdateTime());
            } else {
                LOGGER.info("Nenhum livro encontrado para atualizar.");
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar livro", e);
        }
    }

    private static void checkFirestoreInitialized() {
        if (db == null) {
            throw new IllegalStateException("Firestore não está inicializado.");
        }
    }

    private static Map<String, Object> createLivroMap(Livro livro) {
        Map<String, Object> livroData = new HashMap<>();
        livroData.put("titulo", livro.getTitulo());
        livroData.put("autor", livro.getAutor());
        livroData.put("editora", livro.getEditora());
        livroData.put("anoEdicao", livro.getAnoEdicao());
        livroData.put("localizacao", livro.getLocalizacao());
        return livroData;
    }

    private static void updateLivroFromDocument(Livro livro, DocumentSnapshot document) {
        livro.setAutor(document.getString("autor"));
        livro.setEditora(document.getString("editora"));
        livro.setAnoEdicao(document.getString("anoEdicao"));
        livro.setLocalizacao(document.getString("localizacao"));
    }
}