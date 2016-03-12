package pt.admedia.simples.lib;

/**
 * Created by mrfreitas on 07/03/2016.
 */
public enum SimplesPrefs {
    PREFS("simples_prefs"),
    USER("noreply@simples.pt"),
    PASS("tcsc2015#office"),
    CARD_NAME("simples_card.png");

    private String simplesPrefs;


    SimplesPrefs(String simplesPrefs)
    {
        this.simplesPrefs = simplesPrefs;

    }

    @Override
    public String toString()
    {
        return this.simplesPrefs;
    }

}


