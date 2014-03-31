package com.teusoft.spytour.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.origamilabs.library.views.StaggeredGridView;
import com.teusoft.spytour.R;
import com.teusoft.spytour.adapter.StaggeredAdapter;
import com.teusoft.spytour.entity.LoginResponse;
import com.teusoft.spytour.entity.Tour;
import com.teusoft.spytour.entity.UserData;
import com.teusoft.spytour.util.Constant;
import com.teusoft.spytour.util.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewFeedFragment extends Fragment {
    private List<Tour> listTour;
    private StaggeredAdapter adapter;
    private StaggeredGridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_newfeed, container, false);
        initView(v);
        init();
        return v;
    }

    private void initView(View v) {
        gridView = (StaggeredGridView) v
                .findViewById(R.id.staggeredGridView1);

        int margin = getResources().getDimensionPixelSize(R.dimen.margin);
        gridView.setItemMargin(margin); // set the GridView margin
        gridView.setPadding(margin, 0, margin, 0); // have the margin on the
        // sides as well
        listTour = new ArrayList<Tour>();
        adapter = new StaggeredAdapter(getActivity(), listTour);
        gridView.setAdapter(adapter);
    }

    private void init() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new GetListAsyntask().execute(1, 20);
    }

    private class LoginAsyntask extends AsyncTask<String, Void, LoginResponse> {
        LoginResponse loginResponse;

        @Override
        protected LoginResponse doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("email", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));


            Gson gson = new Gson();
            try {
                String jsonResult = HttpRequest.makeHttpRequest(Constant.URL_LOGIN, "POST", nameValuePairs);
                loginResponse = gson.fromJson(jsonResult, LoginResponse.class);

                JsonObject jsonObj = gson.fromJson(jsonResult, JsonElement.class).getAsJsonObject();
                JsonElement elem = jsonObj.get("data");
                if (elem.isJsonObject()) { //**Object**
                    UserData userData = gson.fromJson(elem.toString(), UserData.class);
                    loginResponse.setUserData(userData);
                } else {  //**String**
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return loginResponse;
        }


        @Override
        protected void onPostExecute(LoginResponse loginResponse) {
            super.onPostExecute(loginResponse);
            // Login success
            if (loginResponse.getStatus().equals("success")) {
                Log.e("dunglv", "" + "success");
            }
        }
    }


    private class GetListAsyntask extends AsyncTask<Integer, Void, List<Tour>> {
        List<Tour> tours;

        @Override
        protected List<Tour> doInBackground(Integer... params) {
            String page = Integer.toString(params[0]);
            String recordpp = Integer.toString(params[1]);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("page", page));
            nameValuePairs.add(new BasicNameValuePair("recordpp", recordpp));
            try {
                String jsonResult = HttpRequest.makeHttpRequest(Constant.URL_GET_LIST, "GET", nameValuePairs);
                Log.e("jsonResult", jsonResult);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Tour>>() {
                }.getType();
                tours = (List<Tour>) gson.fromJson(jsonResult, listType);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return tours;
        }

        @Override
        protected void onPostExecute(List<Tour> tours) {
            super.onPostExecute(tours);
            listTour.addAll(tours);
            adapter.notifyDataSetChanged();
        }
    }
}
