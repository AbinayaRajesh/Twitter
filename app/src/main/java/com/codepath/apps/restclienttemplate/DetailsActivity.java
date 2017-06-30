package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.TweetAdapter.context;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.tvUserName) TextView tvUsername;
    @BindView(R.id.tvFavoriteCount) TextView tvFavoriteCount;
    @BindView(R.id.tvRetweetCount) TextView tvRetweetCount;
    @BindView(R.id.tvTimeStamp) TextView tvTimeStamp;
    @BindView(R.id.ivImg) ImageView ivImg;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.etReply) EditText etReply;

    @BindView(R.id.ivFavorite) ImageView ivFavorite;
    @BindView(R.id.ivReply) ImageView ivReply;
    @BindView(R.id.ivRetweet) ImageView ivRetweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        final Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvBody.setText(tweet.body);
        tvUsername.setText(tweet.user.name);
        tvFavoriteCount.setText(tweet.favorites_count+" FAVORITES");
        tvRetweetCount.setText(tweet.retweet_count+" RETWEETS");
        tvTimeStamp.setText(tweet.timestamp);
        etReply.setHint("Reply to "+ tweet.user.name);
        final long rc = tweet.retweet_count;
        final long fc = tweet.favorites_count;


        if(tweet.favorited==true) {

            ivFavorite.setImageResource(R.drawable.ic_favorite);

            Glide.with(context)
                    .load(R.drawable.ic_favorite)
                    .into(ivFavorite);
        }
        else{
            ivFavorite.setImageResource(R.drawable.ic_unfavorite);
        }

        if(tweet.reTweeted==true) {
            ivRetweet.setImageResource(R.drawable.ic_vector_retweet);

            Glide.with(context)
                    .load(R.drawable.ic_vector_retweet)
                    .into(ivRetweet);
        }
        else{
            ivRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
        }

        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .into(ivProfileImage);
        if(tweet.url==""){
            ivImg.setVisibility(View.GONE);
        }
        else {
            ivImg.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(tweet.url)
                    .into(ivImg);
        }

        ivRetweet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TwitterClient client = TwitterApp.getRestClient();

                    // get the movie at the position, this won't work if the class is static
                    if (!tweet.reTweeted) {
                        client.reTweet(tweet.uid, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                ivRetweet.setBackground(context.getResources().getDrawable(R.drawable.ic_vector_retweet));
                                tweet.retweet_count+=1;
                                tvRetweetCount.setText(String.valueOf(tweet.retweet_count)+" RETWEETS");
                                tweet.reTweeted = true;
                            }


                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Log.i("TweetAdapter", "here");
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }
                        });
                    }
                    else {
                        client.unRetweet(tweet.uid, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                ivRetweet.setBackground(context.getResources().getDrawable(R.drawable.ic_vector_retweet_stroke));
                                tweet.retweet_count-=1;
                                tvRetweetCount.setText(String.valueOf(tweet.retweet_count)+" RETWEETS");
                                tweet.reTweeted = false;
                            }


                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Log.i("TweetAdapter", "here");
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }
                        });


                    }

            }

        });

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            // launched for a result
            public void onClick(View v) {

                TwitterClient client = TwitterApp.getRestClient();


                    // get the movie at the position, this won't work if the class is static


                    if (tweet.favorited == false) {

                        client.addFavorite(tweet.uid, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                ivFavorite.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite));
                                tweet.favorites_count+=1;
                                tvFavoriteCount.setText(String.valueOf(tweet.favorites_count)+" FAVORITES");
                                tweet.favorited=true;
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }
                        });
                    } else {

                        client.unFavorite(tweet.uid, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                ivFavorite.setBackground(context.getResources().getDrawable(R.drawable.ic_unfavorite));
                                tweet.favorites_count-=1;
                                tvFavoriteCount.setText(String.valueOf(tweet.favorites_count)+" FAVORITES");
                                tweet.favorited=false;
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }
                        });
                    }

            }

        });

//        ivReply.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                TwitterClient client = TwitterApp.getRestClient();
//
//                // gets item position
//                int position = getAdapterPosition();
//                // make sure the position is valid, i.e. actually exists in the view
//                if (position != RecyclerView.NO_POSITION) {
//                    // get the movie at the position, this won't work if the class is static
//                    Tweet tweet = mTweets.get(position);
//
//                    long in_reply_to_status_id = tweet.uid;
//
//                    // create intent for the new activity
//                    Intent intent = new Intent(context, ComposeActivity.class);
//                    // serialize the movie using parceler, use its short name as a key
//                    intent.putExtra("reply", true);
//                    intent.putExtra("username", tweet.user.screenName);
//                    intent.putExtra("tweet_id", tweet.uid);
//                    // show the activity
//                    context.startActivity(intent);
//                    String message = "Hi";
//
//
//
//
//                    client.reply(message, in_reply_to_status_id, new JsonHttpResponseHandler() {
//
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                            Log.i("TweetAdapter","heree");
//
//                            Glide.with(context)
//                                    .load(R.drawable.ic_vector_retweet_stroke)
//                                    .into(ivRetweet);
//
//
//                        }
//
//
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                            Log.d("TwitterClient", responseString);
//                            throwable.printStackTrace();
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                            Log.d("TwitterClient", errorResponse.toString());
//                            throwable.printStackTrace();
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                            Log.d("TwitterClient", errorResponse.toString());
//                            throwable.printStackTrace();
//                        }
//                    });
//                }
//            }
//        });




    }
}
