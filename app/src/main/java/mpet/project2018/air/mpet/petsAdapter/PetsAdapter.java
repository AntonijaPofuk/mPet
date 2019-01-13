package mpet.project2018.air.mpet.petsAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mpet.project2018.air.mpet.R;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // Get the data model based on position
        PetModel pet = mPets.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.ime;
        textView.setText(pet.getName());
        ImageView img = viewHolder.slika;
        img.setImageBitmap(pet.getImage());
    }

    @Override
    public int getItemCount() {
        return mPets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ime;
        public ImageView slika;

        public ViewHolder(View itemView) {
            super(itemView);

            ime = (TextView) itemView.findViewById(R.id.pet_name);
            slika = (ImageView) itemView.findViewById(R.id.pet_picture);
        }
    }
    private List<PetModel> mPets;

    public PetsAdapter(List<PetModel> pets) {
        mPets = pets;
    }

    /*****/

}