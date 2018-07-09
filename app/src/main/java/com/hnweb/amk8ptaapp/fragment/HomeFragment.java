package com.hnweb.amk8ptaapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hnweb.amk8ptaapp.R;
import com.hnweb.amk8ptaapp.adaptor.MessagesAdaptor;
import com.hnweb.amk8ptaapp.bo.Messages;
import com.hnweb.amk8ptaapp.contants.AppConstant;
import com.hnweb.amk8ptaapp.utils.AlertUtility;
import com.hnweb.amk8ptaapp.utils.AppUtils;
import com.hnweb.amk8ptaapp.utils.ConnectionDetector;
import com.hnweb.amk8ptaapp.utils.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {


    RecyclerView recyclerViewMessages;
    ConnectionDetector connectionDetector;
    LoadingDialog loadingDialog;
    TextView textViewEmptyReviews;

    String beautician_id;
    private ArrayList<Messages> messagesArrayList = null;
    MessagesAdaptor messagesAdaptor;
    SharedPreferences sharedPreferences;
    String userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.freagment_message, container, false);
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("AOP_PREFS", MODE_PRIVATE);
        userId = sharedPreferences.getString(AppConstant.KEY_ID, null);
        initViewById(view);
        return view;
    }

    private void initViewById(View view) {


        recyclerViewMessages = view.findViewById(R.id.recylerview_my_reviews);
        RecyclerView.LayoutManager layoutManagerNails = new GridLayoutManager(getActivity(), 1);
        recyclerViewMessages.setLayoutManager(layoutManagerNails);

        connectionDetector = new ConnectionDetector(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        if (connectionDetector.isConnectingToInternet()) {
            getMessages();
        } else {

            Toast.makeText(getActivity(), "No Internet Connection, Please try Again!!", Toast.LENGTH_SHORT).show();
        }

    }

    private void getMessages() {
        loadingDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_GET_MESSAGES,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println("MessagesResponse" + response);
                        if (loadingDialog.isShowing()) {
                            loadingDialog.dismiss(); }
                        Log.i("Response", "MessagesResponse= " + response);
                        try {
                            JSONObject jobj = new JSONObject(response);
                            int message_code = jobj.getInt("message_code");
                            String msg = jobj.getString("message");
                            Log.e("FLag", message_code + " :: " + msg);
                            if (message_code == 1) {
                                JSONArray userdetails = jobj.getJSONArray("login_details");
                                messagesArrayList = new ArrayList<Messages>();
                                Log.d("ArrayLengthNails", String.valueOf(userdetails.length()));

                                for (int j = 0; j < userdetails.length(); j++) {
                                    JSONObject jsonObject = userdetails.getJSONObject(j);
                                    Messages messages = new Messages();
                                    messages.setMid(jsonObject.getString("mid"));
                                    messages.setMsg_title(jsonObject.getString("msg_title"));
                                    messages.setMsg_descp(jsonObject.getString("msg_descp"));
                                    messages.setUpdated_dt(jsonObject.getString("updated_dt"));
                                    HomeFragment.this.messagesArrayList.add(messages);
                                    Log.d("ArraySize", String.valueOf(messagesArrayList.size()));
                                    }
                                messagesAdaptor = new MessagesAdaptor(getActivity(), messagesArrayList);
                                recyclerViewMessages.setAdapter(messagesAdaptor);
                                } else {
                                recyclerViewMessages.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String reason = AppUtils.getVolleyError(getActivity(), error);
                        AlertUtility.showAlert(getActivity(), reason);
                        System.out.println("jsonexeption" + error.toString());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put(AppConstant.KEY_ID, "1");

                } catch (Exception e) {
                    System.out.println("error" + e.toString());
                }
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

}
