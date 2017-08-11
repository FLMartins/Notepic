package br.com.woobe.notepic.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import br.com.woobe.notepic.R;
import br.com.woobe.notepic.activity.MainActivity;
import br.com.woobe.notepic.util.Constants;

public class MenuFragment extends Fragment {

    private MainActivity activity;

    private TextView itemDisciplina, itemTutorial, itemAvalie, itemContato, itemSobre, itemPremium;

    public MenuFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ImageView viewSair = (ImageView) view.findViewById(R.id.btn_sair);
        viewSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.closeActualFragment();
            }
        });

        itemDisciplina = (TextView) view.findViewById(R.id.item_menu_disciplina);
        itemDisciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisciplinesFragment fragment = new DisciplinesFragment();
                fragment.setActivity(activity);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment, Constants.Fragments.FRAGMENT_NEW_DISCIPLINE)
                        .addToBackStack(null)
                        .commit();
            }
        });
        itemTutorial = (TextView) view.findViewById(R.id.item_menu_tutorial);
        itemAvalie = (TextView) view.findViewById(R.id.item_menu_avalie);
        itemContato = (TextView) view.findViewById(R.id.item_menu_contato);
        itemSobre = (TextView) view.findViewById(R.id.item_menu_sobre);
        itemPremium = (TextView) view.findViewById(R.id.item_menu_premium);

        return view;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
}
