package com.useek.useek_example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.useek.library_beta.USeekManager;
import com.useek.library_beta.USeekPlaybackResultDataModel;
import com.useek.library_beta.USeekPlayerListener;
import com.useek.library_beta.USeekPlayerView;

public class CustomViewProgrammaticallyActivity extends AppCompatActivity implements USeekPlayerListener {

    LinearLayout useekContainer;
    USeekPlayerView useekView;

    TextView        textViewScore;
    Button          buttonGetScore;
    EditText        editTextGameId;
    EditText        editTextUserId;
    Button          buttonPlay;
    TextView        textViewErrorLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_custom_view_programmatically);

        useekContainer = findViewById(R.id.useek_container);
        addUSeekPlayerView();
        textViewScore   = findViewById(R.id.programmatically_activity_score);
        buttonGetScore  = findViewById(R.id.programmatically_activity_get_score_button);
        buttonGetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedGetScore();
            }
        });
        editTextGameId  = findViewById(R.id.programmatically_activity_game_id);
        editTextUserId  = findViewById(R.id.programmatically_activity_user_id);
        buttonPlay      = findViewById(R.id.programmatically_activity_play_button);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedPlayVideo();
            }
        });
        textViewErrorLog = findViewById(R.id.programmatically_activity_error_text);

    }

    private void addUSeekPlayerView() {
        useekView = new USeekPlayerView(this);
        useekContainer.addView(
                useekView.getView(),
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
        useekView.setPlayerListener(this);
    }

    private void onPressedGetScore() {
        if (checkValidate()) {
            textViewErrorLog.setText("Loading score...");
            buttonGetScore.setEnabled(false);
            USeekManager.sharedInstance().requestPoints(
                    this.editTextGameId.getText().toString(),
                    this.editTextUserId.getText().toString(),
                    new USeekManager.RequestPointsListener() {
                        @Override
                        public void didSuccess(USeekPlaybackResultDataModel resultDataModel) {
                            textViewErrorLog.setText("");
                            textViewScore.setText(String.valueOf(resultDataModel.getPoints()));
                            buttonGetScore.setEnabled(true);
                        }

                        @Override
                        public void didFailure(Error error) {
                            if (error != null)
                                textViewErrorLog.setText(error.getLocalizedMessage());
                            else
                                textViewErrorLog.setText("Error to loading score.");

                            buttonGetScore.setEnabled(true);
                        }
                    }
            );
        }
    }

    private void onPressedPlayVideo() {
        if (checkValidate()) {
            String gameId = this.editTextGameId.getText().toString();
            String userId = this.editTextUserId.getText().toString();

            useekView.loadVideo(gameId, userId);
            useekView.setPlayerListener(this);
        }
    }

    private boolean checkValidate() {
        boolean isValid = true;
        String gameId = editTextGameId.getText().toString();
        if (gameId == null || gameId.length() == 0) {
            textViewErrorLog.setText("Invalid Game Id");
            isValid = false;
        }
        return isValid;
    }


    /** USeekPlayerView listener */

    @Override
    public void didFailedWithError(WebResourceError error) {
        Log.d("USeek Sample", "didFailedWithError video");
    }

    @Override
    public void didStartLoad() {
        Log.d("USeek Sample", "didStartLoad video");
    }

    @Override
    public void didFinishLoad() {
        Log.d("USeek Sample", "didFinishLoad video");
    }
}
