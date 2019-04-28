package com.gramin.sakhala.gramintracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.gramin.sakhala.gramintracker.R;
import com.gramin.sakhala.gramintracker.adapter.DivisionAdapter;
import com.gramin.sakhala.gramintracker.callback.DivisionCallback;
import com.gramin.sakhala.gramintracker.dto.Division;
import com.gramin.sakhala.gramintracker.dto.DivisionData;
import com.gramin.sakhala.gramintracker.dto.DivisionHeader;
import com.gramin.sakhala.gramintracker.dto.DivisionList;

import java.util.ArrayList;
import java.util.List;

public class DivisionActivity extends AppCompatActivity implements DivisionCallback {

    RecyclerView recyclerView;
    DivisionAdapter divisionAdapter;
    String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if(intent != null) {
            title = (String)intent.getExtras().get("title");
        }
        setContentView(R.layout.division_activity);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        divisionAdapter = new DivisionAdapter(this, new DivisionList().getDivisionData(), this);
        recyclerView.setAdapter(divisionAdapter);
        divisionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(DivisionData divisionData) {
        if(divisionData instanceof Division){
            if(title.equals(getString(R.string.deparmanet_str))) {
                Intent intent = new Intent(DivisionActivity.this, MainActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("division_name", ((Division) divisionData).getName());
                startActivity(intent);
            }else{
                Intent intent = new Intent(DivisionActivity.this, MainActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("division_name", ((Division) divisionData).getName());
                startActivity(intent);
            }
        }
    }
}
