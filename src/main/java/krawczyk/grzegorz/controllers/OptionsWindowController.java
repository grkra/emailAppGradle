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

// Interfejs Initalizable deklaruje metodę initialize() która pozwala zainicjalizować wartości pól
// od razu po utworzeniu kontrolera
public class OptionsWindowController extends BaseController implements Initializable {

    public OptionsWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    private Slider fontSizePicker;

    @FXML
    private ChoiceBox<ColorTheme> themePicker;

    @FXML
    void applyButtonAction(ActionEvent event) {
        // pobieramy aktualną ustawioną wartość z themePicker i zapisujemy
        // do pola solorTheme (Enum) w viewFactory
        // themePicker jest obiektem, więc po prostu przechowuje swoją aktualną wartość - nie musimy jej za każdym razem eksportować
        this.viewFactory.setColorTheme(themePicker.getValue());


        // żeby w viewFactory ustawić fontSize (też enum) musimy pobrać aktualną wartość ze slidera
        // (0, 1 lub 2 - wartości w slider są liczbowe), skonwertować na int
        // i używając tego inta jako indeksu wybrać opcję z enuma
        this.viewFactory.setFontSize(FontSize.values()[(int) fontSizePicker.getValue()]);

        this.viewFactory.updateStyles();

        // nie mamy dostępu do stage zainicjalizowanego w viewFactory.showLoginWindow().
        // ale mamy dostęp do każdego elementu interfejsu któremu daliśmy id.
        // z elementu możemy pobrać scenę, a ze sceny okno - a to rzutujemy na Stage.
        // brzydki sposób
        Stage stage = (Stage) this.fontSizePicker.getScene().getWindow();

//        // i zamykamy to okno
//        this.viewFactory.closeStage(stage);
    }

    @FXML
    void cancelButtonAction(ActionEvent event) {
        // nie mamy dostępu do stage zainicjalizowanego w viewFactory.showLoginWindow().
        // ale mamy dostęp do każdego elementu interfejsu któremu daliśmy id.
        // z elementu możemy pobrać scenę, a ze sceny okno - a to rzutujemy na Stage.
        // brzydki sposób
        Stage stage = (Stage) this.fontSizePicker.getScene().getWindow();

        // i zamykamy to okno
        this.viewFactory.closeStage(stage);
    }

    // Metoda z interfejsu Initializable
    // zostanie wywołana od razu po utworzeniu obiektu i ustawi wartości pól
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpThemePicker();
        setUpSizePicker();
    }

    private void setUpThemePicker() {
        // Metoda setItems() pozwala ustawić możliwe wartości elementu interfejsu graficznego
        // setItems() wymaga przekazania jako argumentu obiektu klasy ObservableList,
        // Który tworzymy z Enuma metodą FXCollections.observableArrayList().
        // Czyli dzięki tej metodzie opcje z enuma automatycznie wyświetlą się w Selekcie w interfejsie
        this.themePicker.setItems(FXCollections.observableArrayList(ColorTheme.values()));

        // Metoda setValue() ustawia wartość domyślną elementu interfejsu
        // (tę, która będzie wybrana po wyświetleniu nowego okna).
        // Czyli dzięki tej metodzie domyślną (zaznaczoną) opcją w Selekcie w interfejsie będzie ta zapisana w viewFacotry.
        this.themePicker.setValue(viewFactory.getColorTheme());
    }

    private void setUpSizePicker() {
        // Suwak w JavaFX służy do ustawiania liczby. Dlatego wartosci z Enum FontSize musimy przerobić na liczby.

        // Ustawiamy min. suwaka:
        this.fontSizePicker.setMin(0);
        // Jako max suwaka ustawiamy wielkość (liczbę opcji) enuma, zmiejszoną o 1
        this.fontSizePicker.setMax(FontSize.values().length-1);

        // Metoda setValue() ustawia wartość domyślną elementu interfejsu
        // (tę, która będzie wybrana po wyświetleniu nowego okna).
        // Czyli dzięki tej metodzie domyślną (wybraną) opcją w suwaku w interfejsie będzie indeks opcji zapisanej w viewFacotry.
        // Enum ma specjalną metodę ordinal() która zwraca indeks opcji
        this.fontSizePicker.setValue(viewFactory.getFontSize().ordinal());

        // Ustawiamy główną podziałkę suwaka - podziałka ma być co 1 bo tak są numerowane indeksy w Enum
        this.fontSizePicker.setMajorTickUnit(1);
        // Ma nie być podziałek dodaktowych między głównymi - więc 0
        this.fontSizePicker.setMinorTickCount(0);

        // Ustawia o ile ma przeskakiwać suwak przy sterowaniu klawiszami (strzałkami),
        // bez tego wciśnięcie strzałki zawsze ustawiałoby pozycję skrajną:
        this.fontSizePicker.setBlockIncrement(1);

        // Suwak będzie automatycznie dociągany do najbliższej podziałki
        // UWAGA: suwakiem dalej można płynnie poruszać myszką, ale po puszczeniu dociągnie się do podziałki)
        this.fontSizePicker.setSnapToTicks(true);

        // Ma pokazywać dużą podziałkę i podpisy pod nią
        this.fontSizePicker.setShowTickMarks(true);
        this.fontSizePicker.setShowTickLabels(true);    // podpis pod podziałką będzie 1, 2, 3...

        // Formatuje podpisy pod podziałką - mają być tekstowe opcje z Enuma.
        // Metoda setLabelFormatter() wymaga jako argumentu obiektu klasy StringConverter
        this.fontSizePicker.setLabelFormatter(new StringConverter<Double>() {

            // StringCOnverter ma 2 metody.
            // metoda toString() zamienia przekazany obiekt na String który ma być wyświetlany jako podpis pod podziałką
            @Override
            public String toString(Double aDouble) {
                // Double przekazane do metody (podpis pod podziałką - 0, 1, 2) zapisujemy do i
                int i = aDouble.intValue();
                // Pobieramy wartość z enumu pod indeksem i i zwracamy jako String
                return FontSize.values()[i].toString();
            }

            @Override
            public Double fromString(String s) {
                return 0.0;
            }
        });


        // Ustawia wartość suwaka po zmianie
        // suwak nie będzie się przesuwał płynnie i tylko dociągał do podziałki po puszczeniu (tak działał setSnapToTicks)
        // zamiast tego będzie skakał tylko do podziałek
        fontSizePicker.valueProperty().addListener(
                (observableValue, number, t1) -> {
                    fontSizePicker.setValue(t1.intValue());
                }
        );
    }
}
