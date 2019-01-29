package mpet.project2018.air.mpet.notifications;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import mpet.project2018.air.database.entities.Ljubimac;
import mpet.project2018.air.database.entities.Ljubimac_Table;
import mpet.project2018.air.database.entities.Skeniranje;
import mpet.project2018.air.database.entities.Skeniranje_Table;
import mpet.project2018.air.mpet.R;
import mpet.project2018.air.mpet.fragments.NotificationDetails;

public class NotificationsRecycleViewAdapter extends RecyclerView.Adapter<NotificationsRecycleViewAdapter.SkeniranjeHolder> {

    List<Skeniranje> listSken = new ArrayList<>();

    Integer idSkeniranja = 0;

    public NotificationsRecycleViewAdapter(List<Skeniranje> listSken) {
        this.listSken = listSken;
    }

    private static List<Integer> listaID = new ArrayList<>();


    @Override
    public SkeniranjeHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.one_row_notification_rview, parent, false);
        return new SkeniranjeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkeniranjeHolder holder, int i) {

        String imeLjubimca = "";

        String datumVrijemeSkeniranja = "";

        Ljubimac ljubimac = new SQLite().select().from(Ljubimac.class).where(Ljubimac_Table.kartica_id_kartice.is(listSken.get(listSken.size()-(i+1)).getKartica().getId_kartice())).querySingle();

        try {
            imeLjubimca = ljubimac.getIme();
        } catch (Exception e) {

        }

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy.");

        String parsedDate = "";

        try {
            parsedDate = format.format(listSken.get(listSken.size()-(i+1)).getDatum());
        } catch (Exception e) {

        }

        datumVrijemeSkeniranja = listSken.get(listSken.size()-(i+1)).getVrijeme() + "   " + parsedDate;

        holder.detaljiObavijesti.setText(imeLjubimca + " je bio/la skeniran/a!");

        holder.datumSkeniranja.setText(datumVrijemeSkeniranja);

        Integer redniBroj = listSken.size()-i;

        holder.redniBroj.setText(redniBroj.toString());

        idSkeniranja = listSken.get(listSken.size()-(i+1)).getId_skeniranja();

        holder.IDSkeniranja = idSkeniranja.toString();

        try {

            if (Integer.parseInt(listSken.get(listSken.size()-(i+1)).getProcitano()) < 1) {
                holder.itemView.setBackgroundColor(Color.GRAY);
            }

        }

        catch(Exception e){
            //
        }

    }

    @Override
    public int getItemCount() {
        return listSken.size();
    }


    public class SkeniranjeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView detaljiObavijesti;
        TextView datumSkeniranja;
        TextView redniBroj;
        String IDSkeniranja = "";

        public SkeniranjeHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setBackgroundColor(Color.parseColor("#ebebe4"));
            itemView.setOnClickListener(this);
            detaljiObavijesti = itemView.findViewById(R.id.detaljiPojedineObavijesti);
            datumSkeniranja = itemView.findViewById(R.id.datumVrijemeSkeniranja);
            redniBroj = itemView.findViewById(R.id.redniBrojObavijesti);
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("idSkena", IDSkeniranja);
            FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            NotificationDetails fragmentObavijestiDetaljno = new NotificationDetails();
            fragmentObavijestiDetaljno.setArguments(bundle);
            fragmentTransaction.replace(R.id.mainFrame, fragmentObavijestiDetaljno);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            Skeniranje skeniranje = new Skeniranje();
            skeniranje = new SQLite().select().from(Skeniranje.class).where(Skeniranje_Table.id_skeniranja.is(Integer.parseInt(IDSkeniranja))).querySingle();
            skeniranje.setProcitano("1");
            skeniranje.save();

        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
