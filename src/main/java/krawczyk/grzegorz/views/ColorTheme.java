package krawczyk.grzegorz.views;

/**
 * Enum holding possible color themes of the interface of the application
 * and paths to corresponding css stylesheets.
 */
public enum ColorTheme {
    LIGHT,
    DEFAULT,
    DARK;

    /**
     * Method returns path to css stylesheet corresponding with passed value of ColorTheme enum.
     * @param colorTheme - value from ColorTheme enum.
     * @return path to css stylesheet corresponding with passed value.
     */
    public static String getCssPath(ColorTheme colorTheme) {
        switch (colorTheme) {
            case LIGHT:
                return "/css/themeLight.css";
            case DEFAULT:
                return "/css/themeDefault.css";
            default:
                return "/css/themeDark.css";
        }
    }
}
