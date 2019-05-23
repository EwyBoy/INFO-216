package fx.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GenreController {

    @FXML
    private CheckBox checkBoxDrama;
    @FXML
    private CheckBox checkBoxThriller;
    @FXML
    private CheckBox checkBoxComedy;
    @FXML
    private CheckBox checkBoxRomance;
    @FXML
    private CheckBox checkBoxAdventure;
    @FXML
    private CheckBox checkBoxHorror;
    @FXML
    private CheckBox checkBoxAction;
    @FXML
    private CheckBox checkBoxScienceFiction;
    @FXML
    private CheckBox checkBoxDocumentary;
    @FXML
    private Button buttonRecommend ;

    private ObservableSet<CheckBox> selectedCheckBoxes = FXCollections.observableSet();
    private ObservableSet<CheckBox> unselectedCheckBoxes = FXCollections.observableSet();

    private IntegerBinding numCheckBoxesSelected = Bindings.size(selectedCheckBoxes);

    private final int maxNumSelected =  3;

    public void initialize() {
        configureCheckBox(checkBoxDrama);
        configureCheckBox(checkBoxThriller);
        configureCheckBox(checkBoxComedy);
        configureCheckBox(checkBoxRomance);
        configureCheckBox(checkBoxAdventure);
        configureCheckBox(checkBoxHorror);
        configureCheckBox(checkBoxAction);
        configureCheckBox(checkBoxScienceFiction);
        configureCheckBox(checkBoxDocumentary);

        buttonRecommend.setDisable(true);

        numCheckBoxesSelected.addListener((obs, oldSelectedCount, newSelectedCount) -> {
            if (newSelectedCount.intValue() >= maxNumSelected) {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(true));
                buttonRecommend.setDisable(false);
            } else {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(false));
                buttonRecommend.setDisable(true);
            }
        });
    }

    private void configureCheckBox(CheckBox checkBox) {

        if (checkBox.isSelected()) {
            selectedCheckBoxes.add(checkBox);
        } else {
            unselectedCheckBoxes.add(checkBox);
        }

        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                unselectedCheckBoxes.remove(checkBox);
                selectedCheckBoxes.add(checkBox);
            } else {
                selectedCheckBoxes.remove(checkBox);
                unselectedCheckBoxes.add(checkBox);
            }
        });

    }

    public void backButtonPushed2(ActionEvent event) throws IOException
    {
        Parent movieInputScreenParent = FXMLLoader.load(getClass().getResource("MovieInputScreen.fxml"));
        Scene movieInputScreenScene = new Scene(movieInputScreenParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(movieInputScreenScene);
        window.show();
    }

    public void recommendButtonPushed(ActionEvent event) throws IOException
    {
        Parent recommendationScreenParent = FXMLLoader.load(getClass().getResource("RecommendationScreen.fxml"));
        Scene recommendationScreenScene = new Scene(recommendationScreenParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(recommendationScreenScene);
        window.show();
    }

}
