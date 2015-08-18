package com.example.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class UpdateListElement extends ActionBarActivity {

    private String elementName;
    private int elementId;
    private EditText toEdit;
    private Button saveBtn;
    private Button cancelBtn;
    private ToDoElement element;
    private DatabaseHandler dbhandler;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_list_element);

        dbhandler = new DatabaseHandler(this);
        element = new ToDoElement();
        elementName = " ";

        Bundle extras = getIntent().getExtras();

        if(extras !=null)
        {
            elementId = extras.getInt("ElementID");
        }

        element = dbhandler.getToDo(elementId);
        elementName = element.getToDo();

//        Log.d("passedElement", elementId+"");
//        Log.d("dbhandler", dbhandler.getToDo(elementId)+"");

        Log.d("passedElement", element+"");
        Log.d("elementName", elementName+"");

        toEdit = (EditText) findViewById(R.id.textToEdit);
        toEdit.setText(elementName);

        saveBtn = (Button) findViewById(R.id.saveBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                elementName = toEdit.getText().toString();
                element.setToDo(elementName);
                dbhandler.updateToDo(element);
                i.putExtra("UpdatedElementID", element.getID());
                startActivity(i);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("OldElementID", element.getID());
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_list_element, menu);
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
