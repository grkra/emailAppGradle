package krawczyk.grzegorz.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.views.ColorTheme;
import krawczyk.grzegorz.views.FontSize;
import krawczyk.grzegorz.views.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller of Options window of the app.
 */
public class OptionsWindowController extends BaseController implements Initializable {

    /**
     * OptionsWindowController constructor.
     * <hr></hr>
     * It calls BaseController constructor.
     * @param emailManager - an object of the class EmailManager.
     * @param viewFactory - an object of the class ViewFactory.
     * @param fxmlName - a String containing name of a fxml file with the extension.
     */
    public OptionsWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    private Slider fontSizePicker;

    @FXML
    private ChoiceBox<ColorTheme> themePicker;

    /**
     * Event listener triggered when Apply button is clicked.
     * @param event - click on the Apply button.
     */
    @FXML
    void applyButtonAction(ActionEvent event) {
        // It gets value from themePicker (set in the window)
        // and saves it to themePicker fielf (enum) in the viewFacotry.
        // themePicker is an object, so it has it's value and it can be got - no need to export it.
        this.viewFactory.setColorTheme(themePicker.getValue());


        // Slider has numeral value (0, 1 or 2). To set fontSize property in the viewFactory (enum too)
        // this value has to be converted to int,
        // and then it is possible to select value from enum using this int as index.
        this.viewFactory.setFontSize(FontSize.values()[(int) fontSizePicker.getValue()]);

        this.viewFactory.updateStyles();

        // From the class there is no access to the Stage initialized in viewFacotry.showLoginWindow(),
        // but it has access to eny interface element with id.
        // From any element it is possible to get Scene (which contains that element), and from Scene, Stage.
        // Ugly method.
        Stage stage = (Stage) this.fontSizePicker.getScene().getWindow();

//        // Closing old window (Stage) - without closing window, application would display multiple windows.
//        this.viewFactory.closeStage(stage);
    }

    /**
     * Event listener triggered when Cancel button is clicked.
     * @param event - click on the Cancel button.
     */
    @FXML
    void cancelButtonAction(ActionEvent event) {
        // From the class there is no access to the Stage initialized in viewFacotry.showLoginWindow(),
        // but it has access to eny interface element with id.
        // From any element it is possible to get Scene (which contains that element), and from Scene, Stage.
        // Ugly method.
        Stage stage = (Stage) this.fontSizePicker.getScene().getWindow();

        // i zamykamy to okno
        this.viewFactory.closeStage(stage);
    }

    // Method implemented from Initializable interface.
    // It lets to initialize values of the fields exactly after initialization of an object,
    // so they are already initialized when window is dislayed.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpThemePicker();
        setUpSizePicker();
    }

    /**
     * Method sets (initializes) Pick Theme choicebox element when displaying the window.
     */
    private void setUpThemePicker() {
        // setItems() method lets set possible values of an element of a graphical interface.
        // It requires an object of the ObservableList class which is populated with Enum options with FXCollections.observableArrayList() method.
        // With this method options from Enum are automatically displayed in the choicebox element in the window.
        this.themePicker.setItems(FXCollections.observableArrayList(ColorTheme.values()));

        // Metoda setValue() ustawia wartość domyślną elementu interfejsu
        // (tę, która będzie wybrana po wyświetleniu nowego okna).
        // Czyli dzięki tej metodzie domyślną (zaznaczoną) opcją w Selekcie w interfejsie będzie ta zapisana w viewFacotry.

        // setValue() method sets default value of an element of a graphical interface
        // (value which is selected when window is newly displayed).
        // With this method default selected option in the choicebox is one saved in viewFacotry in colorTheme field.
        this.themePicker.setValue(viewFactory.getColorTheme());
    }

    /**
     * Method sets (initializes) Set Up Text Size slider element when displaying the window.
     */
    private void setUpSizePicker() {
        // Slider in JavaFX is used to set Numerals. So options in Enum fontSize need to be transformed to numbers.

        // Sets minimum value of the Slider:
        this.fontSizePicker.setMin(0);
        // As maximum value of the Slider number of options in Enum minus 1 is used:
        this.fontSizePicker.setMax(FontSize.values().length-1);

        // setValue() method sets default value of an element of a graphical interface
        // (value which is selected when window is newly displayed).
        // With this method default selected option in the slider is one saved in viewFacotry in fontSize field.
        // Enum has ordinal() method which returns index of an option.
        this.fontSizePicker.setValue(viewFactory.getFontSize().ordinal());

        // Sets main scale under the Slider.
        // Scale is 1 unit because it equals indexes in Enum.
        this.fontSizePicker.setMajorTickUnit(1);
        // Sets minor scale under the Slider.
        // There in no need for minor scale under the Slider so it is set to 0 (0 minor ticks between major ticks).
        this.fontSizePicker.setMinorTickCount(0);

        // Sets how many units the thumb of the slider moves when click an arrow on keyboard
        // (without setting that the thumb would go to start or end of the slider when arrow pressed).
        this.fontSizePicker.setBlockIncrement(1);

        // Sets, that the thumb will be auto pulled to nearest scale.
        // WARNING: User can still move the thumb smoothly with mouse, but after release it
        // will be auto pulled to nearest scale.
        this.fontSizePicker.setSnapToTicks(true);

        // Sets scale ticks visible.
        this.fontSizePicker.setShowTickMarks(true);
        // Sets labels under scale visible.
        // WARNING: Labels will be 0, 1, 2.
        this.fontSizePicker.setShowTickLabels(true);

        // Formats labels under scale to text (from Enum) instead of numbers.
        // setLabelFormatter() method demands an object of the StringConverter class.
        this.fontSizePicker.setLabelFormatter(new StringConverter<Double>() {

            // StringCOnverter has 2 methods.
            // toString() method transforms passed object to String to be displayed under the scale.
            @Override
            public String toString(Double aDouble) {
                // Double passed to the method (label under scale: 0, 1, 2) is saved to i:
                int i = aDouble.intValue();
                // Option from Enum under index i is returned as String:
                return FontSize.values()[i].toString();
            }

            @Override
            public Double fromString(String s) {
                return 0.0;
            }
        });

        // Sets position of the thumb of the slider after change.
        // The thumb won't move swiftly and auto pull to scale after release anymore (like setSnapToTicks() sets)
        // Instead it will jump from tick to tick.
        fontSizePicker.valueProperty().addListener(
                (observableValue, number, t1) -> {
                    fontSizePicker.setValue(t1.intValue());
                }
        );
    }
}
