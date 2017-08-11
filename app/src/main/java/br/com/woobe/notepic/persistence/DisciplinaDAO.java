package br.com.woobe.notepic.persistence;

import android.content.Context;

import java.util.List;
import java.util.UUID;

import br.com.woobe.notepic.model.Disciplina;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by willian alfeu on 17/01/2017.
 */
public class DisciplinaDAO {

    private Realm realm;

    public DisciplinaDAO(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public boolean hasDisciplines() {
        long count = realm.where(Disciplina.class).count();
        return count > 0;
    }

    public List<Disciplina> getAll() {
        RealmResults<Disciplina> results = realm.where(Disciplina.class).findAll();
        return results.subList(0, results.size());
    }

    public void save(Disciplina d){
        UUID uuid = UUID.randomUUID();
        d.setId(uuid.getMostSignificantBits());
        realm.beginTransaction();
        Disciplina realmObject = realm.copyToRealmOrUpdate(d);
        realm.commitTransaction();
    }

}
