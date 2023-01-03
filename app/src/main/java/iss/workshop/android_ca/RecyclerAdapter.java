package iss.workshop.android_ca;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    // YT added
    // !! change line 89 in mainActivity cuz constructor changed
    private final RecyclerViewInterface recyclerViewInterface;

    private ArrayList<Uri> uriArrayList; // Uri of 20 jpg images

    // YT Added interface
    public RecyclerAdapter(ArrayList<Uri> uriArrayList, RecyclerViewInterface rvi) {
        this.uriArrayList = uriArrayList;
        recyclerViewInterface = rvi;
    }

    // Original constructor
//    public RecyclerAdapter(ArrayList<Uri> uriArrayList) {
//        this.uriArrayList = uriArrayList;
//    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.puzzel_image,parent,false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    // called by RecyclerView to display data at the indicated position
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
//            uriArrayList.set(position,Uri.parse("https://cdn-icons-png.flaticon.com/512/59/59836.png"));
        holder.puzzle_imageView.setImageURI(uriArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView puzzle_imageView;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            // puzzle_imageView holds each image. child of itemView
            puzzle_imageView = itemView.findViewById(R.id.puzzle_imageView);

            puzzle_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    recyclerViewInterface.onImageClick(position, view);
                }
            });
        }
    }




}
