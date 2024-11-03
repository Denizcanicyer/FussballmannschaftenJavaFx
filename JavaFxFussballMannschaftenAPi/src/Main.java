import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Standard Fenster");
        TextField textField = new TextField("Mannschaft eingeben : ");
        // Button erstellen und Ereignis hinzufügen
        Button btn = new Button("Eingabe");
        Label name = new Label();
        Label marketValue = new Label();
        Label country = new Label();
        Label members = new Label();
        Hyperlink website = new Hyperlink();
        Label founded = new Label();
        Label liga = new Label();
        ImageView imageView = new ImageView();
        Mannschaft mannschaft = new Mannschaft();
        //  String url = "http://localhost:8000/clubs/search/"+textField.getText()+"?page_number=1";


        btn.setOnAction(event -> {
            try {
                String eingabe = textField.getText();
                //  String url = "http://localhost:8000/clubs/search/"+textField.getText()+"?page_number=1";
                if (eingabe.contains(" ")) {
                    eingabe = eingabe.replace(" ", "%20");
                    System.out.println(eingabe);
                }
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8000/clubs/search/" + eingabe + "?page_number=1"))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("http://localhost:8000/clubs/search/" + textField.getText() + "page_number=1");


                JSONObject jsonObject = new JSONObject(response.body());

                // Access the "results" array
                JSONArray resultsArray = jsonObject.getJSONArray("results");

                // Get the first object in the "results" array
                JSONObject firstResult = resultsArray.getJSONObject(0);

                // Get the "name" field from the first result
                mannschaft.setId(Integer.parseInt(firstResult.getString("id")));
                mannschaft.setName(firstResult.getString("name"));


                name.setText("Name : " + mannschaft.getName());


                request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8000/clubs/" + mannschaft.getId() + "/profile"))
                        .build();
                HttpResponse<String> Secondresponse = client.send(request, HttpResponse.BodyHandlers.ofString());
                jsonObject = new JSONObject(Secondresponse.body());
                mannschaft.setImage(jsonObject.getString("image"));
                mannschaft.setCountry(firstResult.getString("country"));
                mannschaft.setMarketvalue((firstResult.getString("marketValue")));
                mannschaft.setMembers((jsonObject.getString("members")));
                mannschaft.setMarketvalue((firstResult.getString("marketValue")));
                mannschaft.setWebsite((jsonObject.getString("website")));
                mannschaft.setFounded((jsonObject.getString("foundedOn")));
                mannschaft.setLiga(jsonObject.getJSONObject("league").getString("name"));
                Image image = new Image(mannschaft.getImage(), true);


                country.setText("Country : " + mannschaft.getCountry());
                marketValue.setText("Marketvalue : " + mannschaft.getMarketvalue());
                members.setText("Members : " + mannschaft.getMembers());
                website.setText(mannschaft.getWebsite());
                founded.setText("Founded : " + mannschaft.getFounded());
                liga.setText("Liga : " + mannschaft.getLiga());

                image.errorProperty().addListener((obs, oldError, newError) -> {
                    if (newError) {
                        System.out.println("Fehler beim Laden des Bildes");
                    }
                });

                imageView.setImage(image);

                // ImageView erstellen und Bild setzen
                imageView.setImage(image);
                imageView.setFitWidth(150);
                imageView.setPreserveRatio(true);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        website.setOnAction(event -> {
            getHostServices().showDocument(website.getText()); // URL öffnen
        });

        Pane root = new Pane();

        textField.setLayoutY(0);
        textField.setLayoutX(0);
        btn.setLayoutY(0);
        btn.setLayoutX(150);

        name.setLayoutY(25);
        name.setLayoutX(0);

        marketValue.setLayoutY(50);
        marketValue.setLayoutX(0);

        members.setLayoutY(75);
        members.setLayoutX(0);
        website.setLayoutY(100);
        website.setLayoutX(0);
        founded.setLayoutY(125);
        founded.setLayoutX(0);
        liga.setLayoutY(150);
        liga.setLayoutX(0);

        imageView.setLayoutY(0);
        imageView.setLayoutX(300);

        root.getChildren().addAll(textField, btn, name, imageView, marketValue, members, website, founded, liga);
        // Szene und Größe des Fensters festlegen
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);

        // Fenster anzeigen
        primaryStage.show();
    }
}
