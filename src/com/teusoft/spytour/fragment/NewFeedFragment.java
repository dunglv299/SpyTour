package com.teusoft.spytour.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.origamilabs.library.views.StaggeredGridView;
import com.teusoft.spytour.R;
import com.teusoft.spytour.activity.DetailActivity;
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

public class NewFeedFragment extends Fragment implements StaggeredGridView.OnItemClickListener {
    private static final int LOAD_ITEM = 5;
    private List<Tour> listTour;
    private StaggeredAdapter adapter;
    private StaggeredGridView gridView;
    private RelativeLayout mProgressBar;
    private int page = 1;
    private boolean isFinishLoad;
    private boolean isLoadCompleted;

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
        mProgressBar = (RelativeLayout) v.findViewById(R.id.loading_pb);

        int margin = getResources().getDimensionPixelSize(R.dimen.margin);
        gridView.setItemMargin(margin); // set the GridView margin
        gridView.setPadding(margin, 0, margin, 0); // have the margin on the
        gridView.setOnItemClickListener(this);
        gridView.setOnLoadMoreListener(loadMoreListener);
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
        new GetListAsyntask().execute(page, LOAD_ITEM);
    }

    @Override
    public void onItemClick(StaggeredGridView parent, View view, int position, long id) {
        Log.e("position", position + "");
        Intent i = new Intent(getActivity(), DetailActivity.class);
        i.putExtra("tour", listTour.get(position));
        startActivity(i);
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
            if (tours.size() == 0) {
                isFinishLoad = true;
            }
            listTour.addAll(tours);
            adapter.notifyDataSetChanged();
            doneLoading();
            Log.e("size", listTour.size() + "");
        }
    }

    private StaggeredGridView.OnLoadMoreListener loadMoreListener = new StaggeredGridView.OnLoadMoreListener() {

        @Override
        public boolean onLoadMore() {
            // load more data from internet (not in the UI thread)
            if (isFinishLoad) {
                Log.e("empty", "empty");
                return false;
            }
            if (isLoadCompleted) {
                page++;
                Log.e("page", page + "");
                new GetListAsyntask().execute(page, LOAD_ITEM);
                isLoadCompleted = false;
                mProgressBar.setVisibility(View.VISIBLE);
            }
            return true; // true if you have more data to load, false you dont have more data to load
        }
    };

    private void doneLoading() {
        gridView.loadMoreCompleated();
        mProgressBar.setVisibility(View.GONE);
        isLoadCompleted = true;
    }
}
