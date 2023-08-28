package benicio.soluces.appdegesto.model;

import java.util.ArrayList;
import java.util.List;

public class ListaResumoTransacaoModel {

    double receitas, despesas, saldo;
    List<TransacaoModel> transacoes = new ArrayList<>();

    public double getReceitas() {
        return receitas;
    }

    public void setReceitas(double receitas) {
        this.receitas = receitas;
    }

    public double getDespesas() {
        return despesas;
    }

    public void setDespesas(double despesas) {
        this.despesas = despesas;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public List<TransacaoModel> getLista() {
        return transacoes;
    }

    public void setLista(List<TransacaoModel> transacoes) {
        this.transacoes = transacoes;
    }
}
