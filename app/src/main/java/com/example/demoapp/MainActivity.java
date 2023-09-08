package com.example.demoapp;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demoapp.database.MSSQL_Connector;
import com.example.demoapp.enums.Querry;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> list = new ArrayList<>();
    private EditText input;
    private TextView text;
    private Button save;
    private ListView listView;
    private Connection connection;
    private ResultSet result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        save = (Button) findViewById(R.id.btnInput);
        input = (EditText) findViewById(R.id.txtViewInput);
        listView = (ListView) findViewById(R.id.listViewProducts);
        text = (TextView) findViewById(R.id.txtResult);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    final Handler handler = new Handler();
                    list = new ArrayList<>();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            result = selectAll();
                            try {
                                while (result.next()) {
                                    list.add("ID :" + result.getString(1) + ", CODE :" + result.getString(2) + ", QUANTITY :" + result.getString(3));
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.item_view, R.id.txtItem, list);
                                listView.setAdapter(adapter);
                            } catch (Exception e) {
                                Log.d("run:", e.toString());
                            }
                        }
                    }, 1000);

                } catch (Exception e) {
                    Log.d("saveOnClick:", e.toString());
                }
            }
        });
    }

    protected void dbManager(Querry querry, String id, String code, String qty) {
        try {
            MSSQL_Connector mssqlConnector = new MSSQL_Connector();
            connection = mssqlConnector.connector();
            if (connection != null) {
                String sql;
                Statement statement;

                if (querry.equals(Querry.DELETE)) {
                    sql = "delete from product where code=" + code;
                    statement = connection.createStatement();
                    statement.executeQuery(sql);
                    Toast.makeText(MainActivity.this, "DELETE", Toast.LENGTH_SHORT).show();
                } else if (querry.equals(Querry.INSERT)) {
                    sql = "insert into product(code,qty) value(" + code + "," + qty + ")";
                    statement = connection.createStatement();
                    statement.executeQuery(sql);
                }


            } else {
                Log.d("ConnectionManager:", "database connection error");
            }
        } catch (Exception e) {
            Log.d("dbManager:", e.toString());
        }
    }

    protected ResultSet selectAll() {
        String sql;
        Statement statement;
        ResultSet resultSet = null;
        try {
            MSSQL_Connector mssqlConnector = new MSSQL_Connector();
            connection = mssqlConnector.connector();
            if (connection != null) {
                sql = "select * from product";
                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
            }
        } catch (Exception e) {
            Log.d("selectAll:", e.toString());
        }

        return resultSet;
    }

}