package edu.byui.cs246.l06_06;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Local Variables
    List<String> itemsList;
    ArrayAdapter<String> listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    // PUBLIC FUNCTIONS

    /**
     * Clears the main (only) ListView
     * @param view the calling view
     */
    public void clearList(View view) {
        listAdapter.clear();
    }

    /**
     * Creates the list file
     * @param view the calling view
     */
    public void createListFile(View view) {
        // Get filename from resources file
        Resources res = getResources();
        String filename = res.getString(R.string.filename_list_file);

        // Get file object
        File file = getFile(filename);

        // Write numbers to the file
        writeNumbersToFile(file);
    }

    /**
     * Populates the main (only) ListView with items from the file
     * @param view the calling View
     */
    public void loadListFromFile(View view) {
        // Get filename from resources file
        Resources res = getResources();
        String filename = res.getString(R.string.filename_list_file);

        // Get file object
        File file = getFile(filename);

        // Populate items list with data from the file
        itemsList = readNumbersFromFile(file);

        // Update the list adapter
        updateAdapter();
    }

    // PRIVATE FUNCTIONS

    /**
     * Attempts to get a File object
     * @param filename the name of the file to access
     * @return a File object representing the specified file
     */
    private File getFile(String filename) {
        Context context = getApplicationContext();
        // Get the file
        return new File(context.getFilesDir(), filename);
    }

    /**
     * Reads a set of numbers, as strings, from the specified file, with artificial slowness
     * @param file the File object to read from
     * @return a List of Strings containing the numbers from the file
     */
    private List<String> readNumbersFromFile(File file) {
        BufferedReader in;
        List<String> numbers = new ArrayList<>();

        try {
            in = new BufferedReader(new FileReader(file));

            String input = null;
            do {
                // Read a line
                try {
                    input = in.readLine();
                    if (input != null) {
                        numbers.add(input);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Time to sleep
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (input != null);

            // Close things up
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return numbers;
    }

    /**
     * Updates the local adapter with the local item data
     */
    private void updateAdapter() {
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemsList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
    }

    /**
     * (Over)writes a sequence of numbers to a text file, with artificial slowness
     * @param file the File object to write to
     */
    private void writeNumbersToFile(File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            PrintStream pout = new PrintStream(fileOutputStream);

            for (int i=0; i <= 10; i++) {
                pout.println(Integer.toString(i));

                // Artificial Slowness
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Close up
            pout.close();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
