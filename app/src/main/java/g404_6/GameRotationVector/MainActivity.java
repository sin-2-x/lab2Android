package g404_6.GameRotationVector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.os.Bundle;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Менеджер сенсоров для управления сенсорными данными
    private SensorManager sensorManager;
    //Массив элементов отображения текста заначений сенсора
    private final TextView[] sensorValuesViews = new TextView[3];
    //Идентификатор сенсора
    private int mrotationListener;
    //Демонстрационный объект
    private View trig;

    //Создание обработчика событий сенсора
    private final SensorEventListener rotationListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == mrotationListener) {
                for (int i = 0; i < 3; i++) {
                    //Вычисление положения по оси демонстрационного объекта
                    float value = event.values[i];
                    value = ((int) (value * 100)) / 100f;
                    //Обновление значений поворота осей сенсора
                    sensorValuesViews[i].setText(String.format("%s", value));
                }
                //обновление значений
                updateValues(event.values[2], event.values[1], event.values[0]);
        }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Привязка разметки
        setContentView(R.layout.activity_main);
        //Привязка элементов разметки
        getTextViews();
        //Добавление сенсора
        addSensor();
    }

    // Метод, вызываемый при возобновлении активити
    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR) != null) {
            mrotationListener = Sensor.TYPE_GAME_ROTATION_VECTOR;
        } else {
            mrotationListener = Sensor.TYPE_ROTATION_VECTOR;
        }
        sensorManager.registerListener(rotationListener, sensorManager.getDefaultSensor(mrotationListener), 200000);
    }

    // Метод, вызываемый при приостановке активити
    @Override
    protected void onPause() {
        super.onPause();
        //Отписка от событий обновления сенсора
        sensorManager.unregisterListener(rotationListener);
    }

    private void addSensor() {
        // Проверяем, есть ли у приложения разрешение на доступ к активности
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION)
                == PackageManager.PERMISSION_DENIED)
            requestPermissions(new String[]{ android.Manifest.permission.ACTIVITY_RECOGNITION }, 31); // Запрашиваем разрешение у пользователя на доступ к активности
        // Получаем доступ к сервису SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    //Привязка элементов разметки
    private void getTextViews() {
        //Создание массива полей привязки
        int[] ids = {R.id.x_value, R.id.y_value, R.id.z_value};
        for (int i = 0; i < sensorValuesViews.length; i++) {
            sensorValuesViews[i] = (TextView) this.findViewById(ids[i]);
        }
        //Привязка демонстрационного объекта
        trig = this.findViewById(R.id.demonstrationObject);

    }

    // Метод для перехода на страницу датчиков
    public void goToSensors(View view) {
        Intent myIntent = new Intent(MainActivity.this, SensorsActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    //Обновление позиции демонстрационного объекта
    public void updateValues(float rotation, float transformX, float transformY){
        FrameLayout f = this.findViewById(R.id.contentFrame);
        //Установка положения Z демонстрационного объекта
        trig.setRotation(180 * rotation);
        //Установка положения X демонстрационного объекта
        trig.setTranslationX(f.getWidth() / 2 * transformX);
        //Установка положения Y демонстрационного объекта
        trig.setTranslationY(f.getHeight() / 2 * transformY);

    }
}