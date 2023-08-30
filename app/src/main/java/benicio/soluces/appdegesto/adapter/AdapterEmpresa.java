package benicio.soluces.appdegesto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import benicio.soluces.appdegesto.R;
import benicio.soluces.appdegesto.model.EmpresaModel;

public class AdapterEmpresa extends RecyclerView.Adapter<AdapterEmpresa.MyViewHolder>{
    List<EmpresaModel> lista;
    Context c;

    public AdapterEmpresa(List<EmpresaModel> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.empresa_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EmpresaModel empresaModel = lista.get(position);
        holder.nome_empresa.setText(empresaModel.getLogin());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView nome_empresa;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome_empresa = itemView.findViewById(R.id.nome_empresa_text);
        }
    }
}
