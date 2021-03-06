package com.epicodus.gamestat.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.epicodus.gamestat.R;
import com.epicodus.gamestat.adapters.GameListAdapter;
import com.epicodus.gamestat.model.Game;
import com.epicodus.gamestat.services.GiantBombService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GameSearchActivity extends AppCompatActivity {
    public static final String TAG = GameSearchActivity.class.getSimpleName();

    @Bind(R.id.gameSearchlistView) RecyclerView mRecyclerView;
    private GameListAdapter mAdapter;

    private ProgressDialog mAuthProgressDialog;

    private ArrayList<Game> mGames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String gameTitle = intent.getStringExtra("gameTitle");
        createAuthProgressDialog();
        getGames(gameTitle);

    }

    private void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Loading Games...");
        mAuthProgressDialog.setCancelable(false);
    }

    private void getGames(String query) {
        final GiantBombService giantBombService = new GiantBombService();

        mAuthProgressDialog.show();

        giantBombService.findGames(query, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mGames = giantBombService.SearchedGameResults(response);

                GameSearchActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mAuthProgressDialog.dismiss();

                        mAdapter = new GameListAdapter(getApplicationContext(), mGames);

                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(GameSearchActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);

                    }
                });
            }
        });
    }
}