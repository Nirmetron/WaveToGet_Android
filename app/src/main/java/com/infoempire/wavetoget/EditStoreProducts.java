package com.infoempire.wavetoget;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditStoreProducts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditStoreProducts extends Fragment {

    View view;
    TextView codetext;
    TextView pricetext;
    TextView quantitytext;
    Button addCodeButton;
    ImageView back;
    String type;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditStoreProducts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditStoreProducts.
     */
    // TODO: Rename and change types and number of parameters
    public static EditStoreProducts newInstance(String param1, String param2) {
        EditStoreProducts fragment = new EditStoreProducts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_store_products, container, false);
        codetext = view.findViewById(R.id.codetextview);
        pricetext = view.findViewById(R.id.amountedittext);
        quantitytext = view.findViewById(R.id.quantitytext);
        back = view.findViewById(R.id.backbutton4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreSettingsPage1 frag = new StoreSettingsPage1();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        addCodeButton = view.findViewById(R.id.addcodebut);
        addCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItem();
            }
        });
        type = "points";
        GetStoreItems();
        return view;
    }
    private void GetStoreItems() {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get-storeitems");
        fields.put("session",  Account.session);
        fields.put("store",  String.valueOf(StoreAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("getstoreitem response", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    StoreAccount.storeItemList = new ArrayList<StoreItem>();
                    JSONArray jarray = null;
                    try {
                        jarray = new JSONArray(_response);
                        for (int i=0; i < jarray.length(); i++)
                        {
                            StoreItem newitem = new StoreItem();
                            JSONObject oneObject = jarray.getJSONObject(i);
                            // Pulling items from the array
                            newitem.id = oneObject.optInt("id");
                            newitem.quantity = oneObject.optInt("quantity");
                            newitem.code = oneObject.optString("code");
                            newitem.amount = oneObject.optString("amount");
                            newitem.type = oneObject.optString("type");
                            StoreAccount.storeItemList.add(newitem);
                        }
//                        Cancel();
                        LayoutInflater inf = getLayoutInflater();
                        LinearLayout scrollViewLinearlayout = view.findViewById(R.id.lin); // The layout inside scroll view
                        for(int i = 0; i < StoreAccount.storeItemList.size(); i++) {
                            LinearLayout layout2 = new LinearLayout(getContext());
                            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            View item = inf.inflate(R.layout.store_product_layout, null, false);
                            layout2.setId(i);
                            layout2.addView(item);
                            scrollViewLinearlayout.addView(layout2);

                            TextView msg = item.findViewById(R.id.itemname);
                            String desc = StoreAccount.storeItemList.get(i).code;
                            msg.setText(desc);
                            TextView amt = item.findViewById(R.id.amount);
                            amt.setText(StoreAccount.storeItemList.get(i).amount);
                            TextView typ = item.findViewById(R.id.type);
                            typ.setText("coins");
                            TextView dur = item.findViewById(R.id.quantity);
                            dur.setText(String.valueOf(StoreAccount.storeItemList.get(i).quantity));
                            Button removebtn = item.findViewById(R.id.removebtn);
                            Integer id = StoreAccount.storeItemList.get(i).id;
                            removebtn.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v) {
                                    RemoveStoreItem(id);
                                    //Cancel();
                                }
                            });
                            Button savebtn = item.findViewById(R.id.savebtn);
                            savebtn.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v) {
                                    SaveImage(String.valueOf(id));
                                }
                            });
                            Button sharebtn = item.findViewById(R.id.sharebtn);
                            sharebtn.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v) {
                                    SendImage(String.valueOf(id));
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("JSON problem", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("failure getting items", t.getMessage());
            }
        });
    }
    private void AddItem() {
        if(codetext.getText().toString().isEmpty())
            return;
        if(quantitytext.getText().toString().isEmpty())
            return;
        if(pricetext.getText().toString().isEmpty())
            return;
        String code = codetext.getText().toString();
        String quantity = quantitytext.getText().toString();
        String price = pricetext.getText().toString();
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "add-storeitem");
        fields.put("session",  Account.session);
        fields.put("store",  String.valueOf(StoreAccount.id));
        fields.put("code",  code);
        fields.put("amount",  price);
        fields.put("type",  type);
        fields.put("quantity",  quantity);
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("additem response", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    StoreItem storeItem = new StoreItem();
                    storeItem.id = Integer.valueOf(_response);
                    storeItem.code = code;
                    storeItem.quantity = Integer.valueOf(quantity);
                    storeItem.amount = price;
                    storeItem.type = type;
                    if (StoreAccount.storeItemList == null){
                        StoreAccount.storeItemList = new ArrayList<StoreItem>();
                    }
                    StoreAccount.storeItemList.add(storeItem);
                    EditStoreProducts frag = new EditStoreProducts();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void RemoveStoreItem(Integer id) {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "remove-storeitem");
        fields.put("session",  Account.session);
        fields.put("id",  String.valueOf(id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("removeitem response", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false")&& _response.equals("1"))
                {
                    for(int i = 0; i < StoreAccount.storeItemList.size(); i++)
                    {
                        if(id == StoreAccount.storeItemList.get(i).id) {
                            StoreAccount.storeItemList.remove(i);
                            break;
                        }
                    }
                    EditStoreProducts frag = new EditStoreProducts();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    public void SendImage(String id) {
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(id, BarcodeFormat.QR_CODE, 350, 350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);

            String bitmapPath = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "palette", "share palette");
            Uri bitmapUri = Uri.parse(bitmapPath);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
            startActivity(Intent.createChooser(intent, "Share"));
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
    }
    void SaveImage(String id)
    {
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(id, BarcodeFormat.QR_CODE, 350, 350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);

            if (android.os.Build.VERSION.SDK_INT >= 29) {
                ContentValues values = contentValues();
                values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + getString(R.string.app_name));
                values.put(MediaStore.Images.Media.IS_PENDING, true);

                Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    try {
                        saveImageToStream(bitmap, getActivity().getContentResolver().openOutputStream(uri));
                        values.put(MediaStore.Images.Media.IS_PENDING, false);
                        getActivity().getContentResolver().update(uri, values, null, null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                File directory = new File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name));

                if (!directory.exists()) {
                    directory.mkdirs();
                }
                String fileName = System.currentTimeMillis() + ".png";
                File file = new File(directory, fileName);
                try {
                    saveImageToStream(bitmap, new FileOutputStream(file));
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                    getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("QR image saved to your photo library.");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Okay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } catch (WriterException e) {
            e.printStackTrace();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("QR image failed to save...");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Okay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }
    private ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        }
        return values;
    }

    private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}