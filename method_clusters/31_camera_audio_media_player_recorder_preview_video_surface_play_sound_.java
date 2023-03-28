public void init() {
        applet = this;
        String media = "./audio";
        String param = null;
        if ((param = getParameter("dir")) != null) {
            media = param;
        } 
        getContentPane().add("Center", demo = new JavaSound(media));
    }
--------------------

@Override
    public Action getPreferredAction() {
        return Actions.alwaysEnabled(new PlayAction(), "Play", "", false);

    }
--------------------

public static void setMusic(String musicFilename) {
        if(music != null) {
            music.stop();
            music.dispose();
        }
        switch(musicFilename) {
        case NO_MUSIC:
            break;
        default:
            music = new MediaPlayer(new Media(musicFilename));
            music.setCycleCount(MediaPlayer.INDEFINITE);
            music.setVolume(masterVolume * musicMolume);
            if(playing) music.play();
            break;
        }
    }
--------------------

