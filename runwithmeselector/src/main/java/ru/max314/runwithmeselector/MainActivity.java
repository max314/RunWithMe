package ru.max314.runwithmeselector;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.FileEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import ru.max314.runwithmeselector.util.LogHelper;


public class MainActivity extends ActionBarActivity {
    private static LogHelper Log = new LogHelper(MainActivity.class);
    private final static String FILENAME = "runwithme.package";


    TextView tvSelectedApp;
    String packageName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvSelectedApp = (TextView) findViewById(R.id.tvSelectedApp);
        loadData();
    }

    private void loadData() {

        packageName = readFileAsString(this,FILENAME);
        if (isEmpty(packageName)){
            tvSelectedApp.setText("Не выбрано");
            packageName ="";
        }
        else
            tvSelectedApp.setText(packageName);
    }


    public void selectApplication(View view) {
        Intent intent = new Intent(this, SelectApplicationActivity.class);
        startActivityForResult(intent, IndentActivityCodes.SELECT_APPLICATION_CODE);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IndentActivityCodes.SELECT_APPLICATION_CODE) {
            String str = data.getStringExtra(IndentActivityCodes.SELECT_APPLICATION_KEY);
            if (!isEmpty(str)) {
                packageName = str;
                tvSelectedApp.setText(str);
                Toast.makeText(this, "You selected: " + str, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeStringAsFile(this, FILENAME,packageName);
    }

    /***
     * Строка пустая или нулл
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if (str == null) return true;
        if (str.isEmpty()) return true;
        return false;
    }

    /**
     * Записать строку как файл
     *
     * @param fileContents строка содержимое
     * @param fileName     имя файла
     */
    public static void writeStringAsFile(Context context, String fileName,final String fileContents ) {
        try {
            FileWriter out = new FileWriter(new File("/sdcard/", fileName));
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
            Log.e("Error write string as file", e);
        }
    }

    /**
     * Считать файл как строку
     *
     * @param fileName имя файла
     * @return содержимое
     */
    public static String readFileAsString(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File("/sdcard/", fileName)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

        } catch (Exception e) {
            Log.e("Error read file as string", e);
        }
        return stringBuilder.toString();
    }
}
