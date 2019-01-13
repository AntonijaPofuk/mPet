package mpet.project2018.air.mpet.petsAdapter;

import android.graphics.Bitmap;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import mpet.project2018.air.database.entities.Ljubimac;
import mpet.project2018.air.database.entities.Ljubimac_Table;

public class PetModel {
    private int petId;
    private String petName;
    private Bitmap petImage;

    public PetModel(int id, String n, Bitmap b) {
        petId=id;
        petName = n;
        petImage = b;
    }

    public String getName() {
        return petName;
    }

    public Bitmap getImage() {
        return petImage;
    }


    public static ArrayList<PetModel> createPetsList(int idVlasnika) {
        ArrayList<PetModel> ljubimci = new ArrayList<PetModel>();
        List<Ljubimac> lj= new SQLite().select().from(Ljubimac.class).where(Ljubimac_Table.korisnik_id_korisnika.is(idVlasnika)).queryList();
        for (Ljubimac item:lj) {
            int id=item.getId_ljubimca();
            String ime=item.getIme();
            Bitmap slika=item.getSlika();
            ljubimci.add(new PetModel(id,ime, slika));
        }

        return ljubimci;
    }
}
