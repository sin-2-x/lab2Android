package g404_6.GameRotationVector;

import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
public class SensorsActivity extends AppCompatActivity {
    private ListView listViewSensors;
    List<Sensor> sensors;
    View arrowBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        listViewSensors = findViewById(R.id.sensorsList);
        arrowBack = findViewById(R.id.arrowBack);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });
        addSensor();
    }

    // Метод для добавления сенсоров и получения списка доступных сенсоров
    private void addSensor() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); // Получаем доступ к сервису SensorManager
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL); // Получаем список доступных сенсоров всех типов
        List<String> sensorsArr = new ArrayList<String>();

        //Заполнение информации о каждом сенсоре в цикле
        for (Sensor sensor : sensors) {

            Resources res = getResources();
            String s = res.getString(R.string.sensorFormat,
                    sensor.getName(),
                    sensor.getType(),
                    sensor.getVendor(),
                    sensor.getVersion(),
                    sensor.getMinDelay(),
                    sensor.getMaxDelay(),
                    sensor.getMaximumRange(),
                    sensor.getPower(),
                    sensor.getResolution());
            sensorsArr.add(s);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_item, sensorsArr);
        listViewSensors.setAdapter(adapter); // Устанавливаем текст с информацией о сенсорах в TextView
    }

    private void goHome(){
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}