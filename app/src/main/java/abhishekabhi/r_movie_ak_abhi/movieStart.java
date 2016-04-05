package abhishekabhi.r_movie_ak_abhi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by A.K ABHI on 30-03-2016.

    */
    public class movieStart extends Fragment {

        public  movieStart(){

        }

        private ArrayAdapter movie;
        JSONObject singleObjectResult;

       String poster_path;
       int ide;
       int id = 7;
       String title;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.refresh, menu);
        inflater.inflate(R.menu.mostpopular, menu);
        inflater.inflate(R.menu.movieout1, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.Refresh) {
            FetchMovie weatherTask = new FetchMovie();
            weatherTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            String[] data ={
                    "https://en.wikipedia.org/wiki/The_Woman_in_Black:_Angel_of_Death#/media/File:Womaninblack2poster.jpg",
                    "https://en.wikipedia.org/wiki/Taken_3#/media/File:Taken_3_poster.jpg",
                    "https://upload.wikimedia.org/wikipedia/en/5/58/Blackhat_poster.jpg",
                    "https://en.wikipedia.org/wiki/Little_Accidents#/media/File:Little_Accidents_Poster.jpg",
                    "https://en.wikipedia.org/wiki/Match_(film)#/media/File:Match_poster.jpg",
                    "https://en.wikipedia.org/wiki/Batman_v_Superman:_Dawn_of_Justice#/media/File:Batman_v_Superman_poster.jpg",
                    "https://en.wikipedia.org/wiki/Deadpool#/media/File:Deadpool-cover.jpg",

            };

            List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));//this is use to store the value in XMl file


            movie =
                    new ArrayAdapter(
                            getActivity(), // The current context (this activity)
                            R.layout.movieout1, // The name of the layout ID.
                            R.id.original_title, // The ID of the textview to populate.
                            weekForecast);

            View rootView = inflater.inflate(R.layout.movielayout2, container, false);
            // Get a reference to the ListView, and attach this adapter to it.
            GridView gridView = (GridView) rootView.findViewById(R.id.gridView2);
            gridView.setAdapter(movie);

            return rootView;
        }
         public class FetchMovie extends AsyncTask<String, Void, Void> {

             private final String LOG_TAG = FetchMovie.class.getSimpleName();

             private String[] getMoviesDataFromJson(String moviesJsonStr)
                     throws JSONException {


                 //the JSON object that we need to be extracted from net
                 final String RESULTS = "results";
                 final String POSTER_PATHS = "poster_path";
                 final String ID = "id";
                 final String ORGINAL_TITLE = "original_title";
                 JSONObject MovieJson = new JSONObject(moviesJsonStr);
                 JSONArray MovieArray = MovieJson.getJSONArray(RESULTS);


                 String[] resultStrs = new String[id];

                 for (int i = 0; i < MovieArray.length(); i++) {

                     singleObjectResult = MovieArray.getJSONObject(i);
                     poster_path = singleObjectResult.getString(POSTER_PATHS);
                     ide = singleObjectResult.getInt(ID);
                     title = singleObjectResult.getString(ORGINAL_TITLE);


                     resultStrs[i] = poster_path + " - " + ide + " - " + title;
                 }
                 for (String s : resultStrs) {
                     Log.e(LOG_TAG, "Movie entry: " + s);
                 }
                 return resultStrs;
             }

             @Override
             protected Void doInBackground(String... params) {

                 // These two need to be declared outside the try/catch
                 // so that they can be closed in the finally block.
                 HttpURLConnection urlConnection = null;
                 BufferedReader reader = null;

                 // Will contain the raw JSON response as a string.
                 String moviejsonstr = null;
                 String API_KEY;

                 try {
                     // Construct the URL for the OpenWeatherMap query
                     String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                     String POPULARITY_URL = "popular?";
                     String SORTING_URL = POPULARITY_URL;

                     // Uri poster_uri = Uri.parse(BASE_URL).buildUpon()
                     // .appendEncodedPath(POPULARITY_URL).build();
                     // .appendEncodedPath(IMAGE_BASE_URL)
                     //  .appendEncodedPath("w185")

                     API_KEY = "&api_key=" + BuildConfig.MOVIE_DB_API_KEY;
                     URL url = new URL(BASE_URL + SORTING_URL + API_KEY);
                     Log.v(LOG_TAG, "Built URI " + url.toString());


                     // Create the request to OpenWeatherMap, and open the connection
                     urlConnection = (HttpURLConnection) url.openConnection();
                     urlConnection.setRequestMethod("GET");
                     urlConnection.connect();

                     // Read the input stream into a String
                     InputStream inputStream = urlConnection.getInputStream();
                     StringBuffer buffer = new StringBuffer();
                     if (inputStream == null) {
                         // Nothing to do.
                         return null;
                     }
                     reader = new BufferedReader(new InputStreamReader(inputStream));

                     String line;
                     while ((line = reader.readLine()) != null) {
                         // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                         // But it does make debugging a *lot* easier if you print out the completed
                         // buffer for debugging.
                         buffer.append(line + "\n");
                     }

                     if (buffer.length() == 0) {
                         // Stream was empty.  No point in parsing.
                         return null;
                     }
                     //moviesJsonStr = buffer.toString();
                 } catch (IOException e) {
                     Log.e(" movieStart", "Error ", e);
                     // If the code didn't successfully get the weather data, there's no point in attemping
                     // to parse it.
                     return null;
                 } finally {
                     if (urlConnection != null) {
                         urlConnection.disconnect();
                     }
                 }

                 return null;
             }
         }
}




