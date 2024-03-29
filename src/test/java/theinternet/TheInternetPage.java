package theinternet;

public enum TheInternetPage {
    DYNAMIC_CONTROLS,
    DYNAMIC_LOADING("dynamic_loading/1"),
    HOVERS,
    INPUTS;

    private static final String BASE_URL = "http://the-internet.herokuapp.com/";

    private final String postfix;

    TheInternetPage() {
        this.postfix = name().toLowerCase();
    }

    TheInternetPage(String postfix) {
        this.postfix = postfix;
    }

    public String getAddress() {
        return BASE_URL.concat(postfix);
    }
}
