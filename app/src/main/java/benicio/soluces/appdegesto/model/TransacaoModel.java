package benicio.soluces.appdegesto.model;

public class TransacaoModel {
    String data, categoria, metodoPagamento;
    int tipo;
    Double valor;

    public TransacaoModel(String data, String categoria, String metodoPagamento, int tipo, Double valor) {
        this.data = data;
        this.categoria = categoria;
        this.metodoPagamento = metodoPagamento;
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
