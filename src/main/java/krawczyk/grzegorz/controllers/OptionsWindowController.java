package krawczyk.grzegorz.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.views.ViewFactory;

public class OptionsWindowController extends BaseController{

    public OptionsWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    private Slider fontSizePicker;

    @FXML
    private ChoiceBox<?> themePicker;

    @FXML
    void applyButtonAction(ActionEvent event) {

    }

    @FXML
    void cancelButtonAction(ActionEvent event) {

    }
}
