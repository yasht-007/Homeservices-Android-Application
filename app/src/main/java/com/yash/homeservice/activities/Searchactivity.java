package com.yash.homeservice.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.yash.homeservice.R;

import java.util.ArrayList;

public class Searchactivity extends AppCompatActivity {

    private ListView listView;
    private SearchView searchView;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchactivity);
        init();
        searchView.requestFocus();

        list=new ArrayList<>();
        list.add("spa");
        list.add("salon");
        list.add("mehendi");
        list.add("plumber");
        list.add("electrician");
        list.add("carpenter");
        list.add("ac repair");
        list.add("repair maintainence");
        list.add("maid");
        list.add("house painters");
        list.add("pest control");
        list.add("packers and movers");
        list.add("yoga and fitness");
        list.add("web designer");

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (list.contains(query))
                {
                    adapter.getFilter().filter(query);
                    listView.setVisibility(View.VISIBLE);
                        }

                else {
                    Toast.makeText(Searchactivity.this, "no match found", Toast.LENGTH_SHORT).show();
                }
                    return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                listView.setVisibility(View.INVISIBLE);
                return false;
            }
        });

    }
    private void init()
    {
        listView=(ListView) findViewById(R.id.listview);
        searchView=(SearchView) findViewById(R.id.searchView);
    }
}
