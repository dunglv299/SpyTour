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
    //    private String urls[];
    //            "http://farm7.staticflickr.com/6101/6853156632_6374976d38_c.jpg",
//            "http://farm8.staticflickr.com/7232/6913504132_a0fce67a0e_c.jpg",
//            "http://farm5.staticflickr.com/4133/5096108108_df62764fcc_b.jpg",
//            "http://farm5.staticflickr.com/4074/4789681330_2e30dfcacb_b.jpg",
//            "http://farm9.staticflickr.com/8208/8219397252_a04e2184b2.jpg",
//            "http://farm9.staticflickr.com/8483/8218023445_02037c8fda.jpg",
//            "http://farm9.staticflickr.com/8335/8144074340_38a4c622ab.jpg",
//            "http://farm9.staticflickr.com/8060/8173387478_a117990661.jpg",
//            "http://farm9.staticflickr.com/8056/8144042175_28c3564cd3.jpg",
//            "http://farm9.staticflickr.com/8183/8088373701_c9281fc202.jpg",
//            "http://farm9.staticflickr.com/8185/8081514424_270630b7a5.jpg",
//            "http://farm9.staticflickr.com/8462/8005636463_0cb4ea6be2.jpg",
//            "http://farm9.staticflickr.com/8306/7987149886_6535bf7055.jpg",
//            "http://farm9.staticflickr.com/8444/7947923460_18ffdce3a5.jpg",
//            "http://farm9.staticflickr.com/8182/7941954368_3c88ba4a28.jpg",
//            "http://farm9.staticflickr.com/8304/7832284992_244762c43d.jpg",
//            "http://farm9.staticflickr.com/8163/7709112696_3c7149a90a.jpg",
//            "http://farm8.staticflickr.com/7127/7675112872_e92b1dbe35.jpg",
//            "http://farm8.staticflickr.com/7111/7429651528_a23ebb0b8c.jpg",
//            "http://farm9.staticflickr.com/8288/7525381378_aa2917fa0e.jpg",
//            "http://farm6.staticflickr.com/5336/7384863678_5ef87814fe.jpg",
//            "http://farm8.staticflickr.com/7102/7179457127_36e1cbaab7.jpg",
//            "http://farm8.staticflickr.com/7086/7238812536_1334d78c05.jpg",
//            "http://farm8.staticflickr.com/7243/7193236466_33a37765a4.jpg",
//            "http://farm8.staticflickr.com/7251/7059629417_e0e96a4c46.jpg",
//            "http://farm8.staticflickr.com/7084/6885444694_6272874cfc.jpg"};
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
        adapter.notifyDataSetChanged();
    }

    private void init() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new GetListAsyntask().execute("1", "8");
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


    private class GetListAsyntask extends AsyncTask<String, Void, List<Tour>> {
        List<Tour> tours;

        @Override
        protected List<Tour> doInBackground(String... params) {
            String page = params[0];
            String recordpp = params[1];
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
