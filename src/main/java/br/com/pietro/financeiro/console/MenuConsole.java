package br.com.pietro.financeiro.console;

import br.com.pietro.financeiro.cep.ConsultaCep;
import br.com.pietro.financeiro.cep.model.Endereco;
import br.com.pietro.financeiro.entity.Transacao;
import br.com.pietro.financeiro.orcamento.model.AnaliseOrcamento;
import br.com.pietro.financeiro.service.OrcamentoService;
import br.com.pietro.financeiro.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

// Menu de console para demonstrar o sistema sem precisar do Postman.
// Ele chama os mesmos services usados pelos controllers, entao Strategy,
// Observer e Proxy funcionam exatamente do mesmo jeito. A API continua no ar.
@Component
public class MenuConsole implements CommandLineRunner {

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private OrcamentoService orcamentoService;

    @Autowired
    private ConsultaCep consultaCep;

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) {
        Thread menu = new Thread(this::executarMenu, "menu-console");
        menu.start();
    }

    private void executarMenu() {
        try {
            boolean continuar = true;

            while (continuar) {
                mostrarOpcoes();
                String opcao = ler("Escolha uma opcao: ");

                switch (opcao) {
                    case "1" -> cadastrarTransacao();
                    case "2" -> listarTransacoes();
                    case "3" -> analisarOrcamento();
                    case "4" -> listarAlertas();
                    case "5" -> consultarCep();
                    case "0" -> continuar = false;
                    default -> System.out.println("Opcao invalida.");
                }
            }

            System.out.println("Menu encerrado. A API continua em http://localhost:8080");
        } catch (NoSuchElementException e) {
            // Sem terminal para digitar (ex.: rodando sem console): o menu apenas termina.
        }
    }

    private void mostrarOpcoes() {
        System.out.println();
        System.out.println("========== CONTROLE FINANCEIRO ==========");
        System.out.println("1 - Cadastrar transacao");
        System.out.println("2 - Listar transacoes");
        System.out.println("3 - Analisar orcamento");
        System.out.println("4 - Ver alertas");
        System.out.println("5 - Consultar CEP");
        System.out.println("0 - Sair do menu");
        System.out.println("=========================================");
    }

    private void cadastrarTransacao() {
        Transacao transacao = new Transacao();
        transacao.setDescricao(ler("Descricao: "));

        Double valor = lerNumero("Valor: ");
        if (valor == null) {
            return;
        }
        transacao.setValor(valor);

        String tipo = ler("Tipo (1 - RECEITA, 2 - DESPESA): ");
        if (tipo.equals("1")) {
            transacao.setTipo("RECEITA");
        } else if (tipo.equals("2")) {
            transacao.setTipo("DESPESA");
        } else {
            System.out.println("Tipo invalido. Transacao nao cadastrada.");
            return;
        }

        transacao.setData(LocalDate.now());

        Transacao criada = transacaoService.criar(transacao);
        System.out.println("Transacao cadastrada com ID " + criada.getId() + ".");
    }

    private void listarTransacoes() {
        List<Transacao> transacoes = transacaoService.listar();

        if (transacoes.isEmpty()) {
            System.out.println("Nenhuma transacao cadastrada.");
            return;
        }

        for (Transacao t : transacoes) {
            System.out.printf("#%d  %s  %-8s  %-20s  R$ %.2f%n",
                    t.getId(), t.getData(), t.getTipo(), t.getDescricao(), t.getValor());
        }
    }

    private void analisarOrcamento() {
        Double limite = lerNumero("Limite do orcamento: ");
        if (limite == null) {
            return;
        }

        String opcao = ler("Estrategia (1 - padrao, 2 - conservadora): ");
        String estrategia = opcao.equals("2") ? "conservadora" : "padrao";

        try {
            AnaliseOrcamento analise = orcamentoService.analisar(limite, estrategia);

            System.out.println("----- Resultado da analise -----");
            System.out.printf("Limite:          R$ %.2f%n", analise.getLimite());
            System.out.printf("Total despesas:  R$ %.2f%n", analise.getTotalDespesas());
            System.out.printf("Percentual:      %.1f%%%n", analise.getPercentualUsado());
            System.out.println("Estrategia:      " + analise.getEstrategia());
            System.out.println("Situacao:        " + analise.getSituacao());
            System.out.println("Mensagem:        " + analise.getMensagem());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarAlertas() {
        List<String> alertas = orcamentoService.listarAlertas();

        if (alertas.isEmpty()) {
            System.out.println("Nenhum alerta emitido ainda.");
            return;
        }

        for (int i = 0; i < alertas.size(); i++) {
            System.out.println((i + 1) + ". " + alertas.get(i));
        }
    }

    private void consultarCep() {
        String cep = ler("CEP (somente numeros): ");

        try {
            Endereco endereco = consultaCep.buscar(cep);
            System.out.println(endereco.getRua() + ", " + endereco.getBairro() + " - "
                    + endereco.getCidade() + "/" + endereco.getEstado());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private String ler(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private Double lerNumero(String mensagem) {
        String texto = ler(mensagem).replace(",", ".");

        try {
            return Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            System.out.println("Valor invalido.");
            return null;
        }
    }
}
