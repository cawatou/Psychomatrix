package com.psy;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {
    private ArrayList<String> arrayListNumbers, arrayListView, arrayGridView;
    private ArrayAdapter<String> arrayAdapterPsy, arrayAdapterList;
    public int number, sum, sumDateNumber;
    public int[] arrayCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void calcBtn(View v){
        arrayListNumbers = new ArrayList<String>();
        arrayGridView = new ArrayList<String>();
        arrayListView = new ArrayList<String>();
        arrayAdapterPsy = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayGridView);
        arrayAdapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListView);
        arrayCount = new int[9];

        GridView GridViewPsy = (GridView)findViewById(R.id.gridViewPsy);
        GridViewPsy.setAdapter(arrayAdapterPsy);

        ListView ListViewPsy = (ListView)findViewById(R.id.listViewPsy);
        ListViewPsy.setAdapter(arrayAdapterList);


        EditText editTextDay = (EditText) findViewById(R.id.editTextDay);
        EditText editTextMonth = (EditText) findViewById(R.id.editTextMonth);
        EditText editTextYear = (EditText) findViewById(R.id.editTextYear);

        String day = editTextDay.getText().toString().trim();
        String month = editTextMonth.getText().toString().trim();
        String year = editTextYear.getText().toString().trim();

        if(day.isEmpty() || month.isEmpty() || year.isEmpty()){
            return;
        }

        char[] mas_day = day.toCharArray();
        char[] mas_month = month.toCharArray();
        char[] mas_year = year.toCharArray();

        composeArray(mas_day, arrayListNumbers);
        composeArray(mas_month, arrayListNumbers);
        composeArray(mas_year, arrayListNumbers);

        stepOne(arrayListNumbers);

        arrayAdapterPsy.notifyDataSetChanged();

    }

    // Постоянно собираем массив, исключая ноль
    public ArrayList composeArray(char[] mas, ArrayList arrayListNumbers){
        for (int i = 0; i < mas.length; i++) {
            String stringValue = String.valueOf(mas[i]);

            if(!stringValue.equals("0")){
                try {
                    number = Integer.parseInt(stringValue);
                    arrayListNumbers.add(number);
                } catch (NumberFormatException nfe) {
                    Log.i("composeArray()", nfe.getMessage());
                }
            }
        }
        return arrayListNumbers;
    }

    // Считаем сумму всех элементов массива
    public void stepOne(ArrayList arrayListNumbers){
        sum = 0;
        for (int i = 0; i < arrayListNumbers.size(); i++){
            String tempValue = String.valueOf(arrayListNumbers.get(i));
            sum = sum + Integer.parseInt(tempValue);
        }

        sumDateNumber = sum;

        String stringValue = String.valueOf(sum);
        char[] result = stringValue.toCharArray();
        arrayListNumbers = composeArray(result, arrayListNumbers);

        stepTwo(result, arrayListNumbers);
    }

    // Считаем сумму чисел из которых состоит сумма из stepOne()
    public void stepTwo(char[] mas, ArrayList arrayListNumbers){
        int sum = 0;
        for (int i = 0; i < mas.length; i++) {
            String stringNumber = String.valueOf(mas[i]);
            sum = sum + Integer.parseInt(stringNumber);
        }

        String stringValue = String.valueOf(sum);

        char[] result = stringValue.toCharArray();
        arrayListNumbers = composeArray(result, arrayListNumbers);

        stepThree(arrayListNumbers);
    }

    // Считаем дельту (число 2 умножить на первое число дня рождения не равное нулю)
    public void stepThree(ArrayList arrayListNumbers){
        String tempValue = String.valueOf(arrayListNumbers.get(0));

        int delta = 2 * Integer.parseInt(tempValue);
        stepFour(arrayListNumbers, delta);
    }

    // Вычитаем из суммы sumDateNumber дельту
    public void stepFour(ArrayList arrayListNumbers, int delta){
        int res = sumDateNumber - delta;

        String stringValue = String.valueOf(res);
        char[] result = stringValue.toCharArray();

        composeArray(result, arrayListNumbers);
        stepFive(arrayListNumbers, result);
    }

    // Считаем последнюю сумму чисел из которых состоит результат stepFour()
    public void stepFive(ArrayList arrayListNumbers, char[] mas){
        sum = 0;
        for (int i = 0; i < mas.length; i++) {
            String stringNumber = String.valueOf(mas[i]);
            sum = sum + Integer.parseInt(stringNumber);
        }

        String stringValue = String.valueOf(sum);
        char[] result = stringValue.toCharArray();
        arrayListNumbers = composeArray(result, arrayListNumbers);
        lastStep(arrayListNumbers);
    }

    // Собираем окончательный массив для вывода в GridView
    public void lastStep(ArrayList arrayListNumbers){
        Collections.sort(arrayListNumbers, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        for(int i = 1; i < 10; i++){
            int count = 0;
            String cell_value = "";
            String iString = String.valueOf(i);
            for (int j = 0; j < arrayListNumbers.size(); j++){
                String tempValue = String.valueOf(arrayListNumbers.get(j));
                if(tempValue.equals(iString)){
                    count++;
                    if(cell_value.equals("")) cell_value = iString;
                    else cell_value = cell_value + ", " + iString ;
                }
            }
            if(cell_value.equals("")) cell_value = "-";
           /* switch (i){
                case 1: String text = ""
            }*/
            arrayCount[i] = count;
            arrayGridView.add(cell_value);
            arrayListView.add(String.valueOf(count));
            /*composeDescription(arrayCount);*/
        }
    }

    // Собираем окончательный массив для вывода в GridView
   /* public void composeDescription(ArrayList arrayListNumbers){


    }*/
}
