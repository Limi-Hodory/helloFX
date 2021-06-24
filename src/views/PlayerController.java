package views;

public class PlayerController {

    //These functions will be activated when the buttons is pressed.
    //The controller tells us when to do but no what to do.
    //Therefore we need this runnable obj- because we need to get from outside the functions that tell us *what* to do.
    //The runnable obj allow us to activate an external function when we press the buttons (play,pause,play).

    public Runnable onOpen, onPlay, onPause, onStop;

    public void open() {
        if(onOpen!=null)
            onOpen.run();
    }


    public void play() {
        if(onPlay!=null)
            onPlay.run();

    }

    public void pause() {
        if(onPause!=null)
            onPause.run();

    }

    public void stop() {
        if(onStop!=null)
            onStop.run();

    }
}
