package app.aya.music;

public class VideoUtils {

    public static final int Max_process = 1000 ;


    public String milliSecondesToTime (long milliseconds){

        String finalTimeerString = "";
        String secondsString = "";

        int hours =(int) (milliseconds/(1000*60*60));
        int minutes =(int) (milliseconds%(1000*60*60)/(1000*60));
        int seconds =(int) (milliseconds%(1000*60*60)%(1000*60)/1000);

        if(hours > 0) {

            finalTimeerString =hours +":";
        }
        if(seconds < 10) {

            secondsString ="0" +seconds;
        }else{
            secondsString=""+seconds;
        }

        finalTimeerString = finalTimeerString+minutes+":"+secondsString;
        return finalTimeerString;
    }

    public int getProgressSeekBar(long currentDuration , long totalDuration){
        Double progress = (double) 0;
        progress = (((double) currentDuration)/totalDuration)*Max_process;
        return progress.intValue();
    }

    public int progressToTime(int progress , int totalDuration){
        int currentDuration = 0;
        totalDuration = (int)(totalDuration/1000);
        currentDuration=(int) ((double) progress/Max_process)*totalDuration;

        return currentDuration *1000 ;

    }
}
