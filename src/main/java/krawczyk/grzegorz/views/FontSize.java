package krawczyk.grzegorz.views;

/**
 * Enum holding possible font sizes for the interface of the application
 * and paths to corresponding css stylesheets.
 */
public enum FontSize {
    SMALL,
    MEDIUM,
    BIG;

    /**
     * Method returns path to css stylesheet corresponding with passed value of FontSize enum.
     * @param fontSize - value from FontSize enum.
     * @return path to css stylesheet corresponding with passed value.
     */
    public static String getCssPath(FontSize fontSize) {
        switch (fontSize) {
            case SMALL:
                return "/css/fontSmall.css";
            case MEDIUM:
                return "/css/fontMedium.css";
            default:
                return "/css/fontBig.css";
        }
    }
}
