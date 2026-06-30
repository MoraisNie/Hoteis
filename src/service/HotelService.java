//Regra de negocios
package service;

import model.Cliente;
import model.Quarto;
import repository.DatabaseFile;

import java.util.List;
import java.util.ArrayList;

public class HotelService {
    private List<Cliente> clientes;
    private List<Quarto> quartos;
    
    private final String ARQUIVO_CLIENTES = "clientes.dat";
    private final String ARQUIVO_QUARTOS = "quartos.dat";

    public HotelService() {
        this.clientes = DatabaseFile.carregarDados(ARQUIVO_CLIENTES);
        this.quartos = DatabaseFile.carregarDados(ARQUIVO_QUARTOS);
        
        // Inicialização padrão de quartos se a base estiver vazia
        if (quartos.isEmpty()) {
            quartos.add(new Quarto(101, "Single Standard", 50.0));
            quartos.add(new Quarto(102, "Single Standard", 50.0));
            quartos.add(new Quarto(201, "Double Deluxe", 85.0));
            quartos.add(new Quarto(202, "Double Deluxe", 85.0));
            quartos.add(new Quarto(301, "Master Suite VIP", 150.0));
            DatabaseFile.salvarDados(ARQUIVO_QUARTOS, quartos);
        }
    }

    // Registo de Clientes
    public boolean cadastrarCliente(String nome, String cpf, String telefone) {
        if (buscarClientePorCpf(cpf) != null) {
            return false; // Cliente já existe
        }
        clientes.add(new Cliente(nome, cpf, telefone));
        DatabaseFile.salvarDados(ARQUIVO_CLIENTES, clientes);
        return true;
    }

    public Cliente buscarClientePorCpf(String cpf) {
        return clientes.stream().filter(c -> c.getCpf().equalsIgnoreCase(cpf)).findFirst().orElse(null);
    }

    // Gestão de Check-in (Entrada)
    public boolean realizarCheckIn(String cpfCliente, int numeroQuarto) {
        Cliente cliente = buscarClientePorCpf(cpfCliente);
        if (cliente == null) {
            System.out.println("Erro: Cliente não registado no sistema. Registe o cliente primeiro.");
            return false;
        }

        Quarto quarto = buscarQuartoPorNumero(numeroQuarto);
        if (quarto == null) {
            System.out.println("Erro: Quarto não encontrado.");
            return false;
        }

        if (quarto.isOcupado()) {
            System.out.println("Erro: O Quarto já se encontra ocupado.");
            return false;
        }

        quarto.setOcupado(true);
        quarto.setCpfCliente(cpfCliente);
        DatabaseFile.salvarDados(ARQUIVO_QUARTOS, quartos);
        return true;
    }

    // Gestão de Check-out (Saída) - Disponibiliza o quarto automaticamente
    public boolean realizarCheckOut(int numeroQuarto) {
        Quarto quarto = buscarQuartoPorNumero(numeroQuarto);
        if (quarto == null || !quarto.isOcupado()) {
            System.out.println("Erro: Quarto não está ocupado ou não existe.");
            return false;
        }

        // Processo automático de libertação e limpeza simulada
        System.out.println("Processando Check-out do quarto " + numeroQuarto);
        System.out.println("Quarto desocupado pelo cliente CPF: " + quarto.getCpfCliente());
        
        quarto.setOcupado(false);
        quarto.setCpfCliente(""); // Remove vínculo de ocupação instantaneamente
        
        DatabaseFile.salvarDados(ARQUIVO_QUARTOS, quartos);
        return true;
    }

    public Quarto buscarQuartoPorNumero(int numero) {
        return quartos.stream().filter(q -> q.getNumero() == numero).findFirst().orElse(null);
    }

    // Relatórios de Acesso para os Donos de Hotéis (Dashboard Financeiro/Ocupação)
    public List<Quarto> listarTodosQuartos() {
        return new ArrayList<>(quartos);
    }

    public List<Quarto> listarQuartosDisponiveis() {
        List<Quarto> disponiveis = new ArrayList<>();
        for (Quarto q : quartos) {
            if (!q.isOcupado()) {
                disponiveis.add(q);
            }
        }
        return disponiveis;
    }

    public List<Cliente> listarTodosClientes() {
        return new ArrayList<>(clientes);
    }

    public void exibirRelatorioGerencial() {
        System.out.println("\n=== RELATÓRIO GERENCIAL (Acesso Reservado aos Donos) ===");
        long totalQuartos = quartos.size();
        long ocupados = quartos.stream().filter(Quarto::isOcupado).count();
        long disponiveis = totalQuartos - ocupados;
        double taxaOcupacao = ((double) ocupados / totalQuartos) * 100;
        double faturamentoPotencialDiario = quartos.stream().filter(Quarto::isOcupado).mapToDouble(Quarto::getPrecoDiaria).sum();

        System.out.println("Total de Quartos Regulamentados: " + totalQuartos);
        System.out.println("Quartos Ocupados neste momento: " + ocupados);
        System.out.println("Quartos Disponíveis no Sistema: " + disponiveis);
        System.out.printf("Taxa de Ocupação Atual: %.2f%%\n", taxaOcupacao);
        System.out.printf("Faturamento Ativo Corrente: %.2f€ / dia\n", faturamentoPotencialDiario);
        System.out.println("=========================================================");
    }
}
