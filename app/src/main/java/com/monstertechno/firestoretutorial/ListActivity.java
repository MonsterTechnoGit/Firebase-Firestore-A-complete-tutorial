package com.monstertechno.firestoretutorial;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListActivity extends AppCompatActivity {

    RecyclerView list;

    private FirestoreRecyclerAdapter<ListModel,ListViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        list = findViewById(R.id.list_firebase);
        list.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Query query = firebaseFirestore.collection("post")
                .orderBy("postValue",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ListModel> option = new FirestoreRecyclerOptions.Builder<ListModel>()
                .setQuery(query,ListModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ListModel, ListViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull ListViewHolder holder, int position, @NonNull ListModel model) {
                holder.textView.setText(model.getPostValue());
            }

            @NonNull
            @Override
            public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
                return new ListViewHolder(view);

            }
        };

        list.setAdapter(adapter);

    }

    private class ListViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public ListViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textValue);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null){
            adapter.stopListening();
        }
    }
}
