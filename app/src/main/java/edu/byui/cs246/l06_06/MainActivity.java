package edu.byui.cs246.l06_06;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Local Variables
    private List<String> itemsList = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;

    /**
     * A Runnable file reader that reads a sequences of numbers from a text file and periodically
     * updates the main UI
     */
    public class ReadFileThread implements Runnable {
        // CONSTANTS & SETTINGS
        private static final int DEFAULT_COUNT = 10;
        private static final long DEFAULT_DELAY = 250;

        // LOCAL VARIABLES
        private File file;          // file to read from
        private int count;          // Expected line count
        private long delay;         // Thread.sleep() delay

        // CONSTRUCTORS

        /**
         * Creates a new instance of ReadFileThread
         * @param file the File object to read from
         */
        public ReadFileThread(File file) {
            this(file, DEFAULT_COUNT, DEFAULT_DELAY);
        }

        /**
         * Creates a new instance of ReadFileThread
         * @param file the File object to read from
         * @param count the expected number of lines to be read
         */
        public ReadFileThread(File file, int count) {
            this(file, count, DEFAULT_DELAY);
        }

        /**
         * Creates a new instance of ReadFileThread
         * @param file the File object to read from
         * @param count the expected number of lines to be read
         * @param delay the Thread.sleep() time between reading each line
         */
        public ReadFileThread(File file, int count, long delay) {
            this.file = file;
            this.count = count;
            this.delay = delay;
        }

        @Override
        /**
         * Runs the main task
         */
        public void run() {
            readNumbersFromFile(file, count, delay);

            // Reset progress bar
            updateProgress(0);
        }

        // PRIVATE FUNCTIONS

        /**
         * Reads a sequence of lines from the specified file and sets the main UI's itemList to the
         * read list
         * @param file the File obejct to read from
         * @param count the expected number of lines to be read from the file
         * @param delay the Thread.sleep() time between reading each line
         */
        private void readNumbersFromFile(File file, int count, long delay) {
            BufferedReader in;
            List<String> numbers = new ArrayList<>();
            int lineCount = 0;

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

                    if (delay > 0) {
                        // Time to sleep
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // Update progress
                    lineCount += 1;
                    float progress = (float)lineCount / Math.max((float)count,(float)lineCount) * 100.0f;
                    updateProgress((int)progress);

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

            updateListItems(numbers);
        }

        /**
         * Updates the main UI progress bar
         * @param progress a 0...100 number
         */
        private void updateProgress(final int progress) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.setProgressBarProgress(progress);
                }
            });
        }

        /**
         * Updates the main class's itemList
         * @param items String List containing the items to update the main class's itemList with
         */
        private void updateListItems(final List<String> items) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.setItemsList(items);
                }
            });
        }
    }

    /**
     * A Runnable file writer that writes a sequences of numbers to a text file and periodically
     * updates the main UI
     */
    public class WriteFileThread implements Runnable {
        // CONSTANTS & SETTINGS
        private static final int DEFAULT_COUNT = 10;
        private static final long DEFAULT_DELAY = 250;

        // LOCAL VARIABLES
        private File file;
        private int count;
        private long delay;

        // CONSTRUCTORS
        /**
         * Creates a new instance of WriteFileThread, with the default count and delay settings
         * @param file the File object to write to
         */
        public WriteFileThread(File file) {
            this(file, DEFAULT_COUNT, DEFAULT_DELAY);
        }

        /**
         * Creates a new instance of WriteFileThread, with the default delay setting
         * @param file the File object to write to
         * @param count the number of numbers to write to the file
         */
        public WriteFileThread(File file, int count) {
            this(file, count, DEFAULT_DELAY);
        }

        /**
         * Creates a new instance of WriteFileThread
         * @param file the File object to write to
         * @param count the number of numbers to write to the file
         * @param delay the Thread.sleep() time between writing each number
         */
        public WriteFileThread(File file, int count, long delay) {
            this.file = file;
            this.count = count;
            this.delay = delay;
        }

        /**
         * Runs the main task
         */
        @Override
        public void run() {
            writeNumbersToFile(file, count, delay);
            // Reset progress bar
            updateProgress(0);
        }

        // PRIVATE FUNCTIONS

        /**
         * Updates the main UI progress bar
         * @param progress a 0...100 number
         */
        private void updateProgress(final int progress) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.setProgressBarProgress(progress);
                }
            });
        }

        /**
         * Writes a sequence of numbers to a file, with a specified Thread.sleep() delay
         * @param file the File object to write to
         * @param count the number of numbers to write to the file
         * @param delay the Thread.sleep() time between writing each number
         */
        private void writeNumbersToFile(File file, int count, long delay) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                PrintStream pout = new PrintStream(fileOutputStream);

                for (int i=0; i <= count; i++) {
                    pout.println(Integer.toString(i));

                    if (delay > 0) {
                        // Artificial Slowness
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // Update our progress
                    updateProgress((int) ((float) i / (float)count * 100.0f));
                }

                // Close up
                pout.close();
                fileOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setItemsList(List<String> value) {
        this.itemsList = value;
        updateAdapter();
    }

    public void setProgressBarProgress(int progress) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(progress);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Connect the adapter
        updateAdapter();

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
    public void writeItemFile(View view) {
        // Get filename from resources file
        Resources res = getResources();
        String filename = res.getString(R.string.filename_list_file);

        // Get file object
        File file = getFile(filename);

        // Use Thread
        WriteFileThread writeFileThread = new WriteFileThread(file);
        Thread writeThread = new Thread(writeFileThread);
        writeThread.start();
    }

    /**
     * Populates the main (only) ListView with items from the file
     * @param view the calling View
     */
    public void readItemFile(View view) {
        // Get filename from resources file
        Resources res = getResources();
        String filename = res.getString(R.string.filename_list_file);

        // Get file object
        File file = getFile(filename);

        // Use Thread
        ReadFileThread readFileThread = new ReadFileThread(file);
        Thread readThread = new Thread(readFileThread);
        readThread.start();
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

            for (int i=0; i <= 50; i++) {
                pout.println(Integer.toString(i));

                // Artificial Slowness
                /*
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */
            }

            // Close up
            pout.close();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
