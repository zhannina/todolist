package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private EditText newTodoEditText;
    private Button addButton;
    private List<ToDoElement> todoItems;
    private ArrayAdapter<ToDoElement> adapter;
    private ListView myListView;
    private String element;
    private ToDoElement todo;
    final DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateListView();

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addTask(v);

            }
        });

        myListView.setAdapter(adapter);
        registerForContextMenu(myListView);

    }

    public void addTask(View v){
        String todoText = newTodoEditText.getText().toString();
        Log.d("Tasks", todoText);
        if (todoText.isEmpty() || todoText.equalsIgnoreCase(" ")) {
            Toast.makeText(this, "Enter the task description!", Toast.LENGTH_SHORT).show();
            Log.d("insideIf", todoText);
        } else {
            todo = new ToDoElement(todoText);
            long id = db.addToDo(todo);
            todo.setID((int)id);
            newTodoEditText.setText("");
            adapter.add(todo);
            adapter.notifyDataSetChanged();
        }
    }

    private class MyAdapter extends ArrayAdapter<ToDoElement> {
        Context context;
        List<ToDoElement> taskList = new ArrayList<ToDoElement>();
        int layoutResourceId;

        public MyAdapter(Context context, int layoutResourceId,
                         List<ToDoElement> objects) {
            super(context, layoutResourceId, objects);
            this.layoutResourceId = layoutResourceId;
            this.taskList = objects;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.item, parent, false);
            TextView item=(TextView)rowView.findViewById(R.id.list_item_text_view);
            ToDoElement current=taskList.get(position);
            item.setText(current.toString());
            return rowView;
        }
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.to_do_list_view){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ToDoElement listItem = (ToDoElement) myListView.getAdapter().getItem(info.position);
        int listItemId = listItem.getID();
        switch (item.getItemId()) {
            case R.id.edit:
                // edit the name of the list element
                Intent i = new Intent(getBaseContext(), UpdateListElement.class);
                i.putExtra("ElementID", listItemId);
                startActivity(i);
                Toast.makeText(this, "Edit selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                db.deleteToDo(listItem);
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                updateListView();
                adapter.notifyDataSetChanged();
                break;

            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);

    }

    public void updateListView() {
        myListView = (ListView) findViewById(R.id.to_do_list_view);
        newTodoEditText = (EditText)findViewById(R.id.new_to_do_edit_text);
        addButton = (Button)findViewById(R.id.add_button);

//        todoItems = new ArrayList<String>();
        final List<ToDoElement> todoItems = db.getAllToDos();

        adapter = new ArrayAdapter<>(this, R.layout.item, todoItems);
        myListView.setAdapter(adapter);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


