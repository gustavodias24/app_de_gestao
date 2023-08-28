package benicio.soluces.appdegesto.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import benicio.soluces.appdegesto.R;
import benicio.soluces.appdegesto.model.TransacaoModel;

public class AdapterTransacao extends RecyclerView.Adapter<AdapterTransacao.MyViewHolder> {

    List<TransacaoModel> lista;
    Context c;

    public AdapterTransacao(List<TransacaoModel> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transacao_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TransacaoModel transacao = lista.get(position);

        holder.data.setText("Data: "+ transacao.getData());
        holder.categoria.setText("Categoria: "+ transacao.getCategoria());

        if ( transacao.getTipo() == 0){
            holder.valor.setTextColor(Color.GREEN);
            holder.valor.setText("Receita: " + transacao.getValor());
        }else if ( transacao.getTipo() == 1 ){
            holder.valor.setTextColor(Color.RED);
            holder.valor.setText("Despesa: " + transacao.getValor());
        }else if ( transacao.getTipo() == 2){
            holder.valor.setTextColor(Color.BLUE);
            holder.valor.setText("Transferêcnia: " + transacao.getValor());
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView data, categoria, valor;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            data = itemView.findViewById(R.id.data_text);
            categoria = itemView.findViewById(R.id.categoria_text);
            valor = itemView.findViewById(R.id.valor_text);
        }
    }
}
