package mpet.project2018.air.database;

import android.content.Context;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Klasa koja služi za inicijalizaciju lokalne baze podataka, DBflow
 */
@Database(name = MainDatabase.NAME, version = MainDatabase.VERSION)
public class MainDatabase {

    public static final String NAME = "mPetDB";
    public static final int VERSION = 1;

    /**
     * metoda koja inicijalizira lokalnu bazu podatak prema gore navedenim atributima imena i verzije
     * @param context kontekst aplikacije, aktivnosti
     */
    public static void initializeDatabase(Context context)
    {
        FlowManager.init(new FlowConfig.Builder(context).build());
    }

    }

