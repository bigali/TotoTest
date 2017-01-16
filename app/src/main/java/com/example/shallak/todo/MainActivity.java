package com.example.shallak.todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.shallak.todo.model.Todo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.shallak.todo.Utils.SUID.id;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    @BindView(R.id.lvItems) ListView lvItems;
    @BindView(R.id.etNewItem) EditText etNewItem;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Realm.init(this);

        realm = Realm.getDefaultInstance();


        items = new ArrayList<>();

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(itemsAdapter);
        for (Todo todo : realm.where(Todo.class).findAll()) {
            items.add(todo.getText());
        }
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        final RealmResults<Todo> results = realm.where(Todo.class).findAll();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Todo todo = results.get(position);
                                todo.deleteFromRealm();
                            }
                        });
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    public void onAddItem(View view) {
        final String itemText= etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Todo todo = realm.createObject(Todo.class);
                todo.setId(id());
                todo.setText(itemText);
            }
        });
        etNewItem.setText("");

    }
}
