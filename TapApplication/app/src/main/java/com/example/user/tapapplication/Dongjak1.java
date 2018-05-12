package com.example.user.tapapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.user.tapapplication.dongjak_play.BallchPlay;
import com.example.user.tapapplication.dongjak_play.BalldrPlay;
import com.example.user.tapapplication.dongjak_play.DigPlay;
import com.example.user.tapapplication.dongjak_play.HeeldrPlay;
import com.example.user.tapapplication.dongjak_play.HopshuPlay;
import com.example.user.tapapplication.dongjak_play.StepPlay;
import com.example.user.tapapplication.dongjak_play.Tap1Play;
import com.example.user.tapapplication.dongjak_play.Tap2Play;

/**
 * Created by user on 2018-01-14.
 */
/* if(v.getId() == R.id.tvActionbarBtnRight) {
            showScanDialog();

                    // 메시지 입력 창의 전송 버튼 클릭 시
                    } else if (v.getId() == R.id.llSendBtnLayout || v.getId() == R.id.ivSendBtn) {
                    참고해서 페이지 줄이자*/
public class Dongjak1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dongjak1);
    }
    public void onClickDongjak(View view){
        if(view.getId() == R.id.tap1btn){
            Intent i = new Intent(this, Tap1Play.class);
            startActivity(i);
        }
        if(view.getId() == R.id.tap2btn){
            Intent i = new Intent(this, Tap2Play.class);
            startActivity(i);
        }
        if(view.getId() == R.id.hopsubtn){
            Intent i = new Intent(this, HopshuPlay.class);
            startActivity(i);
        }
        if(view.getId() == R.id.heeldrbtn){
            Intent i = new Intent(this, HeeldrPlay.class);
            startActivity(i);
        }
        if(view.getId() == R.id.dikbtn){
            Intent i = new Intent(this, DigPlay.class);
            startActivity(i);
        }
        if(view.getId() == R.id.ballchbtn){
            Intent i = new Intent(this, BallchPlay.class);
            startActivity(i);
        }
        if(view.getId() == R.id.balldrbtn){
            Intent i = new Intent(this, BalldrPlay.class);
            startActivity(i);
        }
        if(view.getId() == R.id.stepbtn){
            Intent i = new Intent(this, StepPlay.class);
            startActivity(i);
        }
    }
}
