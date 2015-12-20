package baasi.hackathon.sja.animtaion;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * Created by shota on 12/20/2015.
 */
public class MyAnim {

    public static void crossFadeAnimation(final View fadeInTarget, final View fadeOutTarget, long duration){
        final int[] counter = {0};
        AnimatorSet mAnimationSet = new AnimatorSet();
        final ObjectAnimator fadeOut = ObjectAnimator.ofFloat(fadeOutTarget, View.ALPHA,  1f, 0f);
        final ObjectAnimator fadeIn = ObjectAnimator.ofFloat(fadeInTarget, View.ALPHA, 0f, 1f);

        fadeOut.addListener(new Animator.AnimatorListener() {


            //აქ გადაეწერა მნიშვნელობა უხეშად
            @Override
            public void onAnimationStart(Animator animation) {
                counter[0]++;
            }


            //ამ დროს ჩანს "Выберите желаемый язык"
            @Override
            public void onAnimationEnd(Animator animation) {
                fadeIn.start();
                if (counter[0] % 2 ==0){
                    ((TextView)fadeInTarget).setText("Double tap to push Myo actions");
                }else {
                    ((TextView)fadeInTarget).setText("Double tap to push Myo actions");
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        fadeOut.setInterpolator(new LinearInterpolator());


        fadeIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {


            }


            //ამ დროს ჩანს "აირჩიეთ სასურველი ენა"
            @Override
            public void onAnimationEnd(Animator animation) {
                fadeOut.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        fadeIn.setInterpolator(new LinearInterpolator());

        mAnimationSet.setDuration(duration);
        mAnimationSet.playTogether(fadeOut, fadeIn);
        mAnimationSet.start();
    }
}
