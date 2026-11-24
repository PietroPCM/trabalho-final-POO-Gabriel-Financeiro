package br.com.pietro.financeiro.orcamento.model;

public class AnaliseOrcamento {

    private double limite;
    private double totalDespesas;
    private double percentualUsado;
    private String estrategia;
    private String situacao;
    private String mensagem;

    public AnaliseOrcamento() {
    }

    public AnaliseOrcamento(double limite, double totalDespesas, double percentualUsado,
                            String estrategia, String situacao, String mensagem) {
        this.limite = limite;
        this.totalDespesas = totalDespesas;
        this.percentualUsado = percentualUsado;
        this.estrategia = estrategia;
        this.situacao = situacao;
        this.mensagem = mensagem;
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public double getTotalDespesas() {
        return totalDespesas;
    }

    public void setTotalDespesas(double totalDespesas) {
        this.totalDespesas = totalDespesas;
    }

    public double getPercentualUsado() {
        return percentualUsado;
    }

    public void setPercentualUsado(double percentualUsado) {
        this.percentualUsado = percentualUsado;
    }

    public String getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(String estrategia) {
        this.estrategia = estrategia;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
