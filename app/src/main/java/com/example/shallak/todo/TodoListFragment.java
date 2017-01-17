package com.example.shallak.todo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.shallak.todo.model.Todo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.shallak.todo.Utils.SUID.id;


public class TodoListFragment extends Fragment {


    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    @BindView(R.id.lvItems) ListView lvItems;
    @BindView(R.id.etNewItem) EditText etNewItem;
    private Realm realm;

    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static TodoListFragment newInstance(int page, String title) {
        TodoListFragment fragmentFirst = new TodoListFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_todo_list, container, false);
        ButterKnife.bind(this,view);
        realm = Realm.getDefaultInstance();


        items = new ArrayList<>();

        itemsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(itemsAdapter);
        for (Todo todo : realm.where(Todo.class).findAll()) {
            items.add(todo.getText());
        }
        setupListViewListener();
        return view;
    }

    public void onAddItem(View view) {
        final String itemText= etNewItem.getText().toString();
        if(!itemText.equals("")){
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


}
