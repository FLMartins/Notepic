package br.com.woobe.notepic.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import br.com.woobe.notepic.R;
import br.com.woobe.notepic.activity.MainActivity;
import br.com.woobe.notepic.adapter.DisciplinaAdapter;
import br.com.woobe.notepic.model.Disciplina;
import br.com.woobe.notepic.util.Constants;
import br.com.woobe.notepic.util.Log;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class DisciplinesFragment extends Fragment {

    private MainActivity activity;

    private ListView listView;
    private LinearLayout frameNoResults;
    private List<Disciplina> disciplinas;
    private RealmResults<Disciplina> realmResult;

    public DisciplinesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disciplines, container, false);
        frameNoResults = (LinearLayout) view.findViewById(R.id.frame_no_result);
        listView = (ListView) view.findViewById(R.id.list_disciplines);
        Button btnNova = (Button) view.findViewById(R.id.btn_nova);
        btnNova.setOnClickListener(new NewDisciplineListener());

        Button btnNovaTopo = (Button) view.findViewById(R.id.btn_nova_topo);
        btnNovaTopo.setOnClickListener(new NewDisciplineListener());

        ImageView btnVoltar = (ImageView) view.findViewById(R.id.btn_voltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        Realm.init(getActivity());
        Realm realm = Realm.getDefaultInstance();

        realmResult = realm.where(Disciplina.class).findAllAsync();
        RealmChangeListener<RealmResults<Disciplina>> callback = new RealmChangeListener<RealmResults<Disciplina>>() {
            @Override
            public void onChange(RealmResults<Disciplina> element) {
                if (realmResult.isLoaded()) {
                    disciplinas = element.subList(0, element.size());
                    Log.d(disciplinas.toString());
                    if (disciplinas.size() > 0) {
                        frameNoResults.setVisibility(View.GONE);
                        DisciplinaAdapter adapter = new DisciplinaAdapter(disciplinas);
                        listView.setAdapter(adapter);
                    } else {
                        frameNoResults.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
        realmResult.addChangeListener(callback);

        return view;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onStop() {
        super.onStop();
        realmResult.removeChangeListeners();
    }

    class NewDisciplineListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            DisciplineEditFragment fragment = new DisciplineEditFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.Fragments.DISCIPLINES_LIST_SIZE, disciplinas.size());
            fragment.setArguments(bundle);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, Constants.Fragments.FRAGMENT_NEW_DISCIPLINE)
                    .addToBackStack(null)
                    .commit();

        }
    }
}
