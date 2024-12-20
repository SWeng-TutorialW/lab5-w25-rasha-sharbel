package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.application.Platform;

public class SecondaryController {


    @FXML
    private Button b1;

    @FXML
    private Button b2;

    @FXML
    private Button b3;

    @FXML
    private Button b4;

    @FXML
    private Button b5;

    @FXML
    private Button b6;

    @FXML
    private Button b7;

    @FXML
    private Button b8;

    @FXML
    private Button b9;

    @FXML
    private Text winnerText;

    // List of buttons
    private List<Button> buttons;


    @Subscribe
    public void startTheGame(Object obj){
        if(obj instanceof String && !obj.equals("Start")){
            return;
        }
        Platform.runLater(() -> {
            removeTextFromButtons();
        });
    }

    @FXML
    void chooseCell(ActionEvent event){
        Platform.runLater(() -> {
        Button clickedButton = (Button) event.getSource();// Get the fx:id of the clicked button to get the button number
        char buttonNum = clickedButton.getId().charAt(1);// Taking only the first character

        EventBus.getDefault().post(buttonNum);
        });
    }

    @Subscribe
    public void displayChoiceFromServer(Object obj){
        if(!(obj instanceof String) || !(((String) obj).startsWith("Chosen"))) {
            return;
        }

        String[] parts = (obj.toString()).split(" ");
        Platform.runLater(() -> {
            switch (parts[1]){
                case("1"): b1.setText(parts[2]);
                    if(parts[2].equals("X")){
                        b1.setStyle("-fx-text-fill: blue;");
                    }
                    else {
                        b1.setStyle("-fx-text-fill: red;");
                    }
                    break;
                case("2"): b2.setText(parts[2]);
                    if(parts[2].equals("X")){
                        b2.setStyle("-fx-text-fill: blue;");
                    }
                    else {
                        b2.setStyle("-fx-text-fill: red;");
                    }
                    break;
                case("3"): b3.setText(parts[2]);
                    if(parts[2].equals("X")){
                        b3.setStyle("-fx-text-fill: blue;");
                    }
                    else {
                        b3.setStyle("-fx-text-fill: red;");
                    }
                    break;
                case("4"): b4.setText(parts[2]);
                    if(parts[2].equals("X")){
                        b4.setStyle("-fx-text-fill: blue;");
                    }
                    else {
                        b4.setStyle("-fx-text-fill: red;");
                    }
                    break;
                case("5"): b5.setText(parts[2]);
                    if(parts[2].equals("X")){
                        b5.setStyle("-fx-text-fill: blue;");
                    }
                    else {
                        b5.setStyle("-fx-text-fill: red;");
                    }
                    break;
                case("6"): b6.setText(parts[2]);
                    if(parts[2].equals("X")){
                        b6.setStyle("-fx-text-fill: blue;");
                    }
                    else {
                        b6.setStyle("-fx-text-fill: red;");
                    }
                    break;
                case("7"): b7.setText(parts[2]);
                    if(parts[2].equals("X")){
                        b7.setStyle("-fx-text-fill: blue;");
                    }
                    else {
                        b7.setStyle("-fx-text-fill: red;");
                    }
                    break;
                case("8"): b8.setText(parts[2]);
                    if(parts[2].equals("X")){
                        b8.setStyle("-fx-text-fill: blue;");
                    }
                    else {
                        b8.setStyle("-fx-text-fill: red;");
                    }
                    break;
                case("9"): b9.setText(parts[2]);
                    if(parts[2].equals("X")){
                        b9.setStyle("-fx-text-fill: blue;");
                    }
                    else {
                        b9.setStyle("-fx-text-fill: red;");
                    }
                    break;
            }
        });
    }

    @Subscribe
    public void HandleTurns(Object obj){
        if(obj instanceof String && !obj.equals("Play") && !obj.equals("Wait")){
            return;
        }
        String msg = obj.toString();
        Platform.runLater(() -> {
            if(msg.equals("Play")){
                for (Button button : buttons) {
                    button.setDisable(false);
                }
            }
            else if(msg.equals("Wait")) {
                for (Button button : buttons) {
                    button.setDisable(true);
                }
            }
        });
    }

    @Subscribe
    void theWinner(Object obj){
        if(obj instanceof String && !obj.equals("You Won!") && !obj.equals("You Lose!")){
            return;
        }
        String msg = obj.toString();
        Platform.runLater(() -> {
            winnerText.setText(msg);
        });
    }
    @Subscribe
    public void terminate(Object obj){
        Platform.runLater(() -> {
        if(obj instanceof String && obj.equals("Terminate")){
            try {
                App.setRoot("Primary");
            }catch (IOException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        });
    }

    @FXML
    // Method to remove text from all buttons
    void removeTextFromButtons() {
        // Removes text from buttonS
        b1.setText("");
        b2.setText("");
        b3.setText("");
        b4.setText("");
        b5.setText("");
        b6.setText("");
        b7.setText("");
        b8.setText("");
        b9.setText("");
    }


    @FXML
    void initialize(){
        buttons = List.of(b1, b2, b3, b4, b5, b6, b7, b8, b9);

        b1.setText("W");
        b2.setText("A");
        b3.setText("I");
        b4.setText("");
        b5.setText("T");
        b6.setText("");
        b7.setText("...");
        b8.setText("...");
        b9.setText("...");
        EventBus.getDefault().register(this);
    }
}