package mpet.project2018.air.mpet.petsAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mpet.project2018.air.mpet.R;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class PetsAdapter extends
        RecyclerView.Adapter<PetsAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View petView = inflater.inflate(R.layout.pet_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(petView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        // Get the data model based on position
        PetModel pet = mPets.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.ime;
        textView.setText(pet.getName());
        ImageView img = viewHolder.slika;
        img.setImageBitmap(pet.getImage());
        Button button=viewHolder.gumb;
        if(pet.getPetCard()=="0"){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ako nema pridruženu karticu
                }
            });
        }
        else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ako ima pridruženu karticu
                }
            });
        }

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final AlertDialog alertDialog = new AlertDialog.Builder(viewHolder.itemView.getContext()).create();
                alertDialog.setTitle("Zaista želiti obrisati?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OBRIŠI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO brisanje ljubimca
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Odustani", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

                return true;
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO uređivanje ljubimca
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ime;
        public ImageView slika;
        public Button gumb;

        public ViewHolder(View itemView) {
            super(itemView);

            ime = (TextView) itemView.findViewById(R.id.pet_name);
            slika = (ImageView) itemView.findViewById(R.id.pet_picture);
            gumb=(Button) itemView.findViewById(R.id.petItemGumb);
        }
    }
    private List<PetModel> mPets;

    public PetsAdapter(List<PetModel> pets) {
        mPets = pets;
    }

    /*****/

}