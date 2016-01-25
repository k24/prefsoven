package com.github.k24.prefsovensample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.github.k24.prefsoven.store.Pid;
import com.github.k24.prefsovensample.viewmodel.LastUpdated;
import com.github.k24.prefsovensample.prefs.LastUpdatedOven;
import com.github.k24.prefsovensample.viewmodel.Memo;
import com.github.k24.prefsovensample.prefs.MemoStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView memoList;
    private List<Map<String, Object>> data;
    private SimpleAdapter adapter;
    private boolean paused;
    private Pid modifiedPid;
    private List<Pid> pids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Header
        final LastUpdatedOven lastUpdatedOven = App.lastUpdatedOven();
        updateHeader(lastUpdatedOven);

        // List
        final MemoStore memoStore = App.memoStore();
        String[] from = {memoStore.subject().name(), memoStore.body().name()};
        int[] to = {android.R.id.text1, android.R.id.text2};
        data = new ArrayList<>();
        addAll(memoStore);
        adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, from, to);
        memoList = (ListView) findViewById(R.id.list);
        memoList.setAdapter(adapter);
        memoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PrefsEditActivity.startActivity(MainActivity.this, 0, pids.get(position).index());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Memo memo = new Memo();
                memo.subject = "New " + (memoStore.getControlPanel().pids().size() + 1);
                memo.body = "Edit this!";
                Pid pid = memoStore.getControlPanel().preheat(memo);
                data.add(pid.valuesAsMapWithName());
                adapter.notifyDataSetChanged();
                pids.add(pid);

                lastUpdatedOven.getControlPanel().preheat(LastUpdated.newInstance(memo));
                updateHeader(lastUpdatedOven);
            }
        });
    }

    private void addAll(MemoStore memoStore) {
        pids = new ArrayList<>(memoStore.getControlPanel().pids());
        Collections.sort(pids, Pid.comparator());
        for (Pid pid : pids) {
            data.add(pid.valuesAsMapWithName());
        }
    }

    void updateHeader(LastUpdatedOven lastUpdatedOven) {
        TextView summary = (TextView) findViewById(R.id.summary);
        TextView lastUpdatedAt = (TextView) findViewById(R.id.last_updated_at);
        if (lastUpdatedOven.summary().exists()) {
            summary.setText(lastUpdatedOven.summary().get());
            lastUpdatedAt.setText(LastUpdated.formatDatetime(lastUpdatedOven.updatedAt().get()));
        } else {
            summary.setText("Add new memo!");
            lastUpdatedAt.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            App.memoStore().getControlPanel().clear();
            data.clear();
            pids.clear();
            adapter.notifyDataSetChanged();

            LastUpdatedOven lastUpdatedOven = App.lastUpdatedOven();
            lastUpdatedOven.getControlPanel().clear();
            updateHeader(lastUpdatedOven);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        paused = false;
        if (modifiedPid != null) {
            updateList(modifiedPid);
            modifiedPid = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isFinishing()) return;
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Pid pid = PrefsEditActivity.getPidFromResult(data);
                if (paused) {
                    modifiedPid = pid;
                } else {
                    updateList(pid);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateList(Pid pid) {
        // Tired... lol
        data.clear();
        addAll(App.memoStore());
        adapter.notifyDataSetChanged();
        updateHeader(App.lastUpdatedOven());
    }
}
