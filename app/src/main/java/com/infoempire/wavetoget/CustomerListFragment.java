package com.infoempire.wavetoget;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerListFragment extends Fragment {
    private static final String TAG = "CustomerListFragment";

    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;

    ArrayList<CustomerDetails> customers  = new ArrayList<>();
    ArrayList<CustomerDetails> filteredList = new ArrayList<>();

    View view;
    SearchView searchView;
    ImageView back;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerListFragment newInstance(String param1, String param2) {
        CustomerListFragment fragment = new CustomerListFragment();
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
        view =  inflater.inflate(R.layout.fragment_customer_list, container, false);

        back = view.findViewById(R.id.backbutton4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreSettingsPage1 frag = new StoreSettingsPage1();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });

        searchView = view.findViewById(R.id.sv_search_store);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterCustomers(newText);

                return true;
            }
        });

        recyclerView = view.findViewById(R.id.rv_store_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        customers = new ArrayList<>();

        GetCustomers();

        return view;
    }

    private void filterCustomers(String searchText) {

        filteredList.clear();

        for (CustomerDetails customer : customers) {
            if (customer.email.toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(customer);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No customer found!", Toast.LENGTH_SHORT).show();
            customerAdapter.setFilteredCustomers(filteredList);
        } else {
            customerAdapter.setFilteredCustomers(filteredList);
        }
    }

    private void GetCustomers() {

        customers.clear();

        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get-all-users");
        fields.put("session", Account.session);
        fields.put("store", String.valueOf(StoreAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String plain_text_response = response.body();
                Log.d("customers", plain_text_response);
                try {
                    JSONArray jArray = new JSONArray(plain_text_response);
                    for (int i=0; i < jArray.length(); i++)
                    {
                        try {
                            JSONObject oneObject = jArray.getJSONObject(i);
                            // Pulling items from the array
                            Integer customerID = oneObject.optInt("id");
                            Integer store = oneObject.optInt("store");
                            Integer location = oneObject.optInt("location");
                            String email = oneObject.optString("email");
                            String displayname = oneObject.optString("displayname");
                            String lastactive = oneObject.optString("lastactive");
                            String type = oneObject.optString("type");
                            String store_displayname = oneObject.optString("store_displayname");
                            String location_displayname = oneObject.optString("location_displayname");
                            String phone = oneObject.optString("phone");
                            String address = oneObject.optString("address");

                            CustomerDetails customerDetails = new CustomerDetails();
                            customerDetails.id = customerID;
                            customerDetails.store = store;
                            customerDetails.location = location;
                            customerDetails.email = email;
                            customerDetails.displayname = displayname;
                            customerDetails.lastactive = lastactive;
                            customerDetails.type = type;
                            customerDetails.store_displayname = store_displayname;
                            customerDetails.location_displayname = location_displayname;
                            customerDetails.phone = phone;
                            customerDetails.address = address;

                            customers.add(customerDetails);

                        } catch (JSONException e) {
                            // Oops
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                customerAdapter = new CustomerAdapter(getContext(), customers, new CustomerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(CustomerDetails customerDetails) {
                        CustomerDetails selectedCustomer = customerDetails;

                        Log.d(TAG, "onItemClick: a customer is tapped!!");

                        if (selectedCustomer == null) {
                            return;
                        }

                        CustomerDetailFragment frag = new CustomerDetailFragment();
                        Bundle args = new Bundle();
                        args.putSerializable("customerDetails", selectedCustomer);
                        frag.setArguments(args);
                        getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                    }
                });
                recyclerView.setAdapter(customerAdapter);
                filteredList.addAll(customers);
                customerAdapter.setFilteredCustomers(filteredList);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        //}
    }
}