package br.com.woobe.notepic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.com.woobe.notepic.R;
import br.com.woobe.notepic.model.Disciplina;

/**
 * Created by willian alfeu on 17/01/2017.
 */
public class DisciplinaAdapter extends BaseAdapter {

    private List<Disciplina> disciplinas;

    public DisciplinaAdapter(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    @Override
    public int getCount() {
        return disciplinas.size();
    }

    @Override
    public Object getItem(int position) {
        return disciplinas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return disciplinas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Disciplina disciplina = disciplinas.get(position);
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_discipline, null);
            holder = new ViewHolder(view);
            convertView = view;
        }
        holder.nome.setText(disciplina.getNome());
        holder.btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        convertView.setTag(holder);
        return convertView;
    }

    class ViewHolder {
        TextView nome;
        ImageView btnDeletar;
        ImageView btnExcluir;

        public ViewHolder(View view) {
            nome = (TextView) view.findViewById(R.id.nome);
            btnDeletar = (ImageView) view.findViewById(R.id.editar);
            btnExcluir = (ImageView) view.findViewById(R.id.excluir);
        }
    }
}
