package pt.admedia.simples.api;

/**
 * Created by mrfreitas on 03/03/2016.
 */
public enum BaseURL {

    //TODO Change base URL
    BASE_URL("https://simples.pt/api2"),
    IMAGE_URL("https://simples.pt/img-remote/"),
    CARD_IMG("https://simples.pt/api2/card/pic?width=1000&height=auto&token="),
    PARTNERS_IMG("https://simples.pt/img-remote/img-300-auto/"),
    LOGO_IMG("https://simples.pt/img-remote/img-100-auto/"),
    FACE_1("https://graph.facebook.com/"),
    FACE_2("/picture?type=large");

    private String url;

    BaseURL(String url)
    {
        this.url = url;
    }


    @Override
    public String toString()
    {
        return this.url;
    }
}
