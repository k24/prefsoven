package com.github.k24.prefsovensample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.k24.prefsoven.PrefsStoreOven;
import com.github.k24.prefsoven.store.Pid;
import com.github.k24.prefsovensample.viewmodel.LastUpdated;
import com.github.k24.prefsovensample.viewmodel.Memo;
import com.github.k24.prefsovensample.prefs.MemoStore;

/**
 * Created by k24 on 2016/01/17.
 */
public class PrefsEditActivity extends AppCompatActivity {

    private EditText subjectEdit;
    private EditText bodyEdit;
    private Pid pid;
    private Memo memo;

    public static void startActivity(Activity source, int requestCode, Pid.Index index) {
        if (index == null) {
            Toast.makeText(source, "Something wrong!", Toast.LENGTH_SHORT).show();
            return;
        }
        source.startActivityForResult(new Intent(source, PrefsEditActivity.class).putExtra(Pid.Index.class.getName(), index), requestCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Pid.Index index = (Pid.Index) getIntent().getSerializableExtra(Pid.Index.class.getName());

        MemoStore memoStore = App.memoStore();
        PrefsStoreOven.ControlPanel controlPanel = memoStore.getControlPanel();
        pid = controlPanel.pid(index);
        if (pid == null) {
            Toast.makeText(this, "Something wrong!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        toolbar.setTitle(LastUpdated.formatDatetime(pid.createdAtMillis()));
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        memo = memoStore.cook(pid, Memo.class);
        subjectEdit = (EditText) findViewById(R.id.subject);
        subjectEdit.setText(memo.subject);
        bodyEdit = (EditText) findViewById(R.id.body);
        bodyEdit.setText(memo.body);
    }

    @Override
    public void onBackPressed() {
        saveIfNeeded();
        super.onBackPressed();
    }

    private void saveIfNeeded() {
        if (pid == null || subjectEdit.length() == 0) {
            // Nothing to do
            return;
        }

        memo.subject = subjectEdit.getText().toString();
        memo.body = bodyEdit.getText().toString();
        App.memoStore().getControlPanel().preheat(pid, memo);
        App.lastUpdatedOven().getControlPanel().preheat(LastUpdated.newInstance(memo));
        setResult(RESULT_OK, new Intent().putExtra(Pid.Index.class.getName(), pid.index()));
    }

    public static Pid getPidFromResult(Intent data) {
        return App.memoStore().getControlPanel().pid((Pid.Index) data.getSerializableExtra(Pid.Index.class.getName()));
    }
}
