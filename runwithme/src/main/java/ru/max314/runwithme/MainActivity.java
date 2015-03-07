package ru.max314.runwithme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import ru.max314.runwithme.util.LogHelper;


public class MainActivity extends Activity {
    private static LogHelper Log = new LogHelper(MainActivity.class);
    private final static String FILENAME = "runwithme.package";

    TextView tvInfo;
    String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature (Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        loadData();
        if (packageName.isEmpty()){
            tvInfo.setText("Не настроено. Вполните настройку");
        }
        else {
            tvInfo.setText(packageName);
            runPackage();
        }
    }

    private void runPackage() {
        try {
            runAndroidPackage(this,packageName);
            tvInfo.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },300);
        } catch (Exception e) {
            Log.e("Error run",e);
            tvInfo.setText(tvInfo.getText()+" Ошибка запуска.");
        }
    }

    /**
     * Запустить активити по имени пакета
     *
     * @param packageName
     */
    public static void runAndroidPackage(Context context, String packageName) {
        Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(LaunchIntent);
    }

    private void loadData() {

        packageName = readFileAsString(this, FILENAME);
        if (isEmpty(packageName)) {
            packageName = "";
        }
    }

    /**
     * Строка пустая или нулл
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null) return true;
        if (str.isEmpty()) return true;
        return false;
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
