package com.test.telematicstechtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.test.telematicstechtask.Network.ApiClientWithHeadersAuth;
import com.test.telematicstechtask.Network.ApiInterface;
import com.test.telematicstechtask.ResponseModel.AddProductModel;
import com.test.telematicstechtask.ResponseModel.FuelType;
import com.test.telematicstechtask.ResponseModel.ManufactureYear;
import com.test.telematicstechtask.ResponseModel.VehicleCapacity;
import com.test.telematicstechtask.ResponseModel.VehicleMake;
import com.test.telematicstechtask.ResponseModel.VehicleType;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity implements VehicleTypeAdapter.Callbacks {
    ApiInterface apiInterface;
    Retrofit retrofit;
    Spinner manufactureType,fuel,vehicleCapacity,vehicleMake,vehicleModel;
    RecyclerView vehicleType;
    EditText tagName,registrationNumber;
    Button add;
    TextView txt_ImEI;
    private final ArrayList<String> vehicleCapacityList = new ArrayList<>();
    private final ArrayList<String> vehicleMakeList = new ArrayList<>();
    private final ArrayList<String> vehicleFuel = new ArrayList<>();
    private final ArrayList<String> manfYearList = new ArrayList<>();
    private final ArrayList<String> vehicleModelList = new ArrayList<>();
    List<VehicleType> vehicleTypes = new ArrayList<>();

    List<VehicleType> dummyvehicleTypes = new ArrayList<>();

    VehicleTypeAdapter vehicleTypeAdapter;

    Boolean flag = true;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Vehicle");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        retrofit = ApiClientWithHeadersAuth.getRetrofitInstance(MainActivity.this);
        manufactureType = findViewById(R.id.manf_year);
        fuel = findViewById(R.id.fuel_type);
        vehicleCapacity = findViewById(R.id.capacity);
        vehicleMake = findViewById(R.id.vehicle_make);
        vehicleModel = findViewById(R.id.vehicle_model);
        vehicleType = findViewById(R.id.RecyclerView);
        txt_ImEI = findViewById(R.id.imeivalue);

        txt_ImEI.setOnClickListener(view -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
            intentIntegrator.setBeepEnabled(true);
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.setCaptureActivity(Capture.class);
            intentIntegrator.initiateScan();
        });

        getProductDetails();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult.getContents()!=null){
          Toast.makeText(MainActivity.this,intentResult.getContents(),Toast.LENGTH_SHORT).show();
          txt_ImEI.setText(intentResult.getContents());
        }
    }
    private void getProductDetails() {
        apiInterface = retrofit.create(ApiInterface.class);
        JsonObject addJson = new JsonObject();
        addJson.addProperty("clientid",11);
        addJson.addProperty("enterprise_code",1007);
        addJson.addProperty("mno","9889897789");
        addJson.addProperty("passcode",3476);
        Call<AddProductModel> saveProducts = apiInterface.addProductToServer(addJson);
        saveProducts.enqueue(new Callback<AddProductModel>() {
            @Override
            public void onResponse(Call<AddProductModel> call, Response<AddProductModel> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        AddProductModel productResponse = response.body();

                        List<FuelType> fuelTypes = productResponse.getFuelType();

                        List<ManufactureYear> manufactureYears = productResponse.getManufactureYear();

                        vehicleTypes = productResponse.getVehicleType();
                        for(int i = 0; i<3; ++i) {
                            dummyvehicleTypes.add(vehicleTypes.get(i));
                        }

                        List<VehicleCapacity> vehicleCapacities = productResponse.getVehicleCapacity();
                        List<VehicleMake> vehicleMakes = productResponse.getVehicleMake();
                        setRecyclerItems();

                        for(int i =0 ;i<vehicleCapacities.size();i++){
                            vehicleCapacityList.add(vehicleCapacities.get(i).getText());}
                        for(int i =0 ;i<fuelTypes.size();i++){
                            vehicleFuel.add(fuelTypes.get(i).getText());}
                        for(int i =0 ;i<manufactureYears.size();i++){
                            manfYearList.add(manufactureYears.get(i).getText());}
                        for(int i =0 ;i<vehicleMakes.size();i++){
                            vehicleMakeList.add(vehicleMakes.get(i).getText());}



                        ArrayAdapter capacityListAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, vehicleCapacityList);
                        capacityListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleCapacity.setAdapter(capacityListAdapter);

                        ArrayAdapter vehicleMakeAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, vehicleMakeList);
                        vehicleMakeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleMake.setAdapter(vehicleMakeAdapter);
                        vehicleModel.setAdapter(vehicleMakeAdapter);


                        ArrayAdapter manfYearAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, manfYearList);
                        manfYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        manufactureType.setAdapter(manfYearAdapter);

                        ArrayAdapter fuelTypeAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, vehicleFuel);
                        fuelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        fuel.setAdapter(fuelTypeAdapter);


                        vehicleModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                String  vehMake = vehicleMake.getItemAtPosition(position).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        vehicleMake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                String  vehMake = vehicleMake.getItemAtPosition(position).toString();


                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        manufactureType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                String  manType = manufactureType.getItemAtPosition(position).toString();


                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        fuel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                String  fuelty = fuel.getItemAtPosition(position).toString();


                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        vehicleCapacity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                String  vehCap = vehicleCapacity.getItemAtPosition(position).toString();



                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }

                }
            }


            @Override
            public void onFailure(Call<AddProductModel> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void setRecyclerItems() {
        if(vehicleTypes.size()>0) {
            vehicleType.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            vehicleType.setItemAnimator(new DefaultItemAnimator());
            vehicleTypeAdapter = new VehicleTypeAdapter(MainActivity.this, (ArrayList<VehicleType>) dummyvehicleTypes);
            vehicleTypeAdapter.setCallback(MainActivity.this);
            vehicleTypeAdapter.setWithFooter(true); //enabling footer to show
            vehicleType.setAdapter(vehicleTypeAdapter);
        }
    }

    @Override
    public void onClickLoadMore() {
       vehicleTypeAdapter.setWithFooter(true);
        if(flag) {

                for (int i = 3; i < vehicleTypes.size(); ++i) {
                dummyvehicleTypes.add(vehicleTypes.get(i));
                flag = false;
            }
        }
        else{
            dummyvehicleTypes.clear();
            for(int i =0;i<3;i++){
                dummyvehicleTypes.add(vehicleTypes.get(i));
                flag = true;
            }
        }
        vehicleTypeAdapter.notifyDataSetChanged(); // more elements will be added
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addproductmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            getProductDetails();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
