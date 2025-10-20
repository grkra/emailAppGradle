package krawczyk.grzegorz.controllers;

import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.views.ViewFactory;

/**
 * Base controller.
 * All window controllers (LoginWindowController, MainWindowController, OptionsWindowController) extend it.
 */
public abstract class BaseController {

    protected EmailManager emailManager;
    protected ViewFactory viewFactory;
    private String fxmlName;

    /**
     * BaseController constructor.
     * <hr></hr>
     * Saves passed values to properties.
     * @param emailManager - an object of the class EmailManager.
     * @param viewFactory - an object of the class ViewFactory.
     * @param fxmlName - a String containing name of a fxml file with the extension.
     */
    public BaseController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        this.emailManager = emailManager;
        this.viewFactory = viewFactory;
        this.fxmlName = fxmlName;
    }

    public String getFxmlName() {
        return fxmlName;
    }
}
