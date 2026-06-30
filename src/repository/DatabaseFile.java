//Persistencia
package repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseFile {
    
    @SuppressWarnings("unchecked")
    public static <T> void salvarDados(String nomeArquivo, List<T> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados no arquivo " + nomeArquivo + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> carregarDados(String nomeArquivo) {
        File arquivo = new File(nomeArquivo);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados do arquivo " + nomeArquivo + ". Criando nova lista.");
            return new ArrayList<>();
        }
    }
}
