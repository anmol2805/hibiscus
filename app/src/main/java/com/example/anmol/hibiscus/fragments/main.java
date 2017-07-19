package com.example.anmol.hibiscus.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.anmol.hibiscus.Httphandler;
import com.example.anmol.hibiscus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anmol on 2017-07-11.
 */

public class main extends Fragment {
    private String TAG = main.class.getSimpleName();
    private ProgressDialog pdialog;
    private ListView lv;
    private static String url = "http://api.androidhive.info/contacts/";
    ArrayList<HashMap<String, String>> contactList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.main,container,false);
        contactList = new ArrayList<>();
        lv = (ListView) vi.findViewById(R.id.list);
        new GetContacts().execute();
        return vi;
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new ProgressDialog(getActivity());
            pdialog.setMessage("Please wait...");
            pdialog.setCancelable(false);
            pdialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pdialog.isShowing())
                pdialog.dismiss();
            ListAdapter adapter = new SimpleAdapter(getActivity(), contactList, R.layout.list_notice, new String[]{"name", "email", "mobile"}, new int[]{R.id.name, R.id.email, R.id.mobile});
            lv.setAdapter(adapter);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Httphandler sh = new Httphandler();
            String jsonstr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonstr);
            if (jsonstr != null) {
                try {
                    JSONObject jsonobject = new JSONObject(jsonstr);
                    JSONArray contacts = jsonobject.getJSONArray("contacts");
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String address = c.getString("address");
                        String gender = c.getString("gender");
                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");
                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);
                        contactList.add(contact);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(), "Json parsing error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }else{
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }
    }
}
