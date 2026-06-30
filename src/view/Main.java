package view;

import service.HotelService;
import model.Cliente;
import model.Quarto;

import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        HotelService hotelService = new HotelService();
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        System.out.println("=========================================");
        System.out.println("   SISTEMA DE GERENCIAMENTO HOTELEIRO    ");
        System.out.println("=========================================");

        while (opcao != 0) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Registar Cliente");
            System.out.println("2. Entrada de Hóspede (Check-In)");
            System.out.println("3. Saída de Hóspede (Check-Out)");
            System.out.println("4. Mostrar Quartos Disponíveis");
            System.out.println("5. Painel Exclusivo dos Donos do Hotel");
            System.out.println("0. Sair");
            System.out.print("Introduza a sua opção: ");
            
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introduza um dígito numérico válido.");
                continue;
            }

            switch (opcao) {
                case 1:
                    System.out.print("Nome Completo: ");
                    String nome = scanner.nextLine();
                    System.out.print("Documento de Identidade (NIF/CPF): ");
                    String cpf = scanner.nextLine();
                    System.out.print("Telemóvel/Telefone: ");
                    String telefone = scanner.nextLine();
                    
                    if (hotelService.cadastrarCliente(nome, cpf, telefone)) {
                        System.out.println("Sucesso: Cliente registado na base de dados.");
                    } else {
                        System.out.println("Aviso: Falha. Documento já existente.");
                    }
                    break;

                case 2:
                    System.out.print("Introduza o ID/NIF do Cliente: ");
                    String idCli = scanner.nextLine();
                    
                    System.out.println("\nQuartos Desocupados:");
                    List<Quarto> livres = hotelService.listarQuartosDisponiveis();
                    if (livres.isEmpty()) {
                        System.out.println("Não há quartos vagos.");
                        break;
                    }
                    livres.forEach(System.out.println);
                    
                    System.out.print("\nNúmero do quarto para alojar: ");
                    int nQuartoIn = Integer.parseInt(scanner.nextLine());
                    if (hotelService.realizarCheckIn(idCli, nQuartoIn)) {
                        System.out.println("Check-In concluído com sucesso!");
                    }
                    break;

                case 3:
                    System.out.print("Introduza o número do quarto para libertar: ");
                    int nQuartoOut = Integer.parseInt(scanner.nextLine());
                    if (hotelService.realizarCheckOut(nQuartoOut)) {
                        System.out.println("Sucesso! O quarto foi esvaziado e reposicionado como livre.");
                    }
                    break;

                case 4:
                    System.out.println("\n=== LISTA DE QUARTOS VAGOS ===");
                    hotelService.listarQuartosDisponiveis().forEach(System.out.println);
                    break;

                case 5:
                    // Sistema de barreira de acesso para o Proprietário do Hotel
                    System.out.print("Introduza a palavra-passe do Proprietário: ");
                    String pass = scanner.nextLine();
                    if ("admin123".equals(pass)) {
                        hotelService.exibirRelatorioGerencial();
                        System.out.println("\nDeseja ver dados analíticos detalhados? [1] Clientes [2] Quartos [Outra tecla] Menu");
                        String subOp = scanner.nextLine();
                        if ("1".equals(subOp)) hotelService.listarTodosClientes().forEach(System.out.println);
                        if ("2".equals(subOp)) hotelService.listarTodosQuartos().forEach(System.out.println);
                    } else {
                        System.out.println("Acesso Recusado! Palavra-passe incorreta.");
                    }
                    break;

                case 0:
                    System.out.println("Encerrando a aplicação hoteleira...");
                    break;
            }
        }
        scanner.close();
    }
}
