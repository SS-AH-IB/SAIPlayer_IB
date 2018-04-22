package saiplayer.triode.com.saiplayer_ib.AdapterAnimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by dell on 09-Aug-17.
 */

public class SongsScrollAnimation {

    public static void animateSongList(View view){

        AnimatorSet animatorSet = new AnimatorSet();

        /*ObjectAnimator translateY = ObjectAnimator.ofFloat(view, "translationY" , 50,-50,0);
        translateY.setDuration(700);*/

        ObjectAnimator translateX = ObjectAnimator.ofFloat(view, "translationX" , 50,-10,0);
        translateX.setDuration(500);

        animatorSet.playTogether(translateX);
        animatorSet.start();

    }
}
