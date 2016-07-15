package com.epicodus.gamestat.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.epicodus.gamestat.Constants;
import com.epicodus.gamestat.R;
import com.epicodus.gamestat.adapters.FirebaseGameViewHolder;
import com.epicodus.gamestat.model.Game;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SavedGameListActivity extends AppCompatActivity {
    private DatabaseReference mRestaurantReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;

    @Bind(R.id.allGamelistView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_all_search);
        ButterKnife.bind(this);

        mRestaurantReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_GAMES);
        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter() {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Game, FirebaseGameViewHolder>
                (Game.class, R.layout.game_list_item, FirebaseGameViewHolder.class,
                        mRestaurantReference) {

            @Override
            protected void populateViewHolder(FirebaseGameViewHolder viewHolder,
                Game model, int position) {
                viewHolder.bindGame(model);
            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }
}