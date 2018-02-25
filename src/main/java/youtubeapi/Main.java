package youtubeapi;

import com.alibaba.fastjson.JSON;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import entiti.Item;
import entiti.Responce;
import entiti.SnippetVideo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.VideoTrack;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.*;


public class Main extends Application {

    static Media media;// = new Media(new File("/home/valeriy/Downloads/gojavabonus2.mp4").toURI().toString());
    private static MediaPlayer player;// = new MediaPlayer(media);

    private List<ResponceContainer> containers;

    public static void main(String[] args) {
        launch(args);
    }
    private BorderPane mainPane = new BorderPane();
    private VBox left = new VBox();
    private VBox center = new VBox();

    private VBox top = new VBox();
    private HBox toTop = new HBox();

    private ScrollPane scrollPane = new ScrollPane();
    private static WebView webview;

    @Override
    public void start(Stage primaryStage) throws Exception {
        new UnirestSerialization();
        setParam(primaryStage);

        //toTop
        Button playButton = new Button("Play");
        Button searchButton = new Button ("Start search");
        CheckBox checkBox = new CheckBox("Advanced");
        TextField dateField = new TextField();
        dateField.setText("Days");
        TextField countField = new TextField();
        countField.setText("Count");
        playButton.setDisable(true);

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue)
            {
                toTop.getChildren().remove(playButton);
                toTop.getChildren().add(dateField);
                toTop.getChildren().add(countField);
                toTop.getChildren().add(playButton);
            }
            else {
                toTop.getChildren().remove(playButton);
                toTop.getChildren().remove(dateField);
                toTop.getChildren().remove(countField);
                toTop.getChildren().add(playButton);
            }
        });

        toTop.setSpacing(5);
        toTop.setStyle("-fx-background-color: GAINSBORO");
        toTop.getChildren().addAll(searchButton, checkBox, playButton);

        //top

        Text title = new Text("->");
        title.setFill(Color.BLUE);
        TextField searchField = new TextField();

        top.getChildren().addAll(searchField, toTop);
        top.setStyle("-fx-background-color: GAINSBORO");
        top.setSpacing(3);

        mainPane.setTop(top);

//left
        Text resultOfMax = new Text();
        left.getChildren().add(resultOfMax);

        left.setPadding(new Insets(15,15,10,10));
        left.setSpacing(10);


        //center
        MediaView viewer = new MediaView();
        viewer.setMediaPlayer(player);
        viewer.setFitWidth(width - left.getWidth());
        center.setStyle("-fx-background-color: black");
//        center.getChildren().add(viewer);
        mainPane.setCenter(center);


        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);
        primaryStage.show();

//-----------------------------Pool-----------------------------------------
        searchButton.setOnMouseClicked((MouseEvent event) -> {
            ExecutorService service = Executors.newFixedThreadPool(3);
            if (left.getChildren().size() > 1) left.getChildren().clear();
            Callable<VBox> toLeft = () -> {
                try {
                    Responce responce;
                    if (checkBox.isSelected())
                        responce = YoutubeAPI.search(searchField.getText(), countField.getText(), dateField.getText());
                    else
                        responce = YoutubeAPI.search(searchField.getText(), -1, -1);
                    resultOfMax.setText("Total video: " + responce.items.length);
                    SearchResult searchResult = new SearchResult(responce);
                    this.containers = searchResult.getContainers();
                    VBox vBoxCallable = new VBox();
//                    System.out.println("containers.length:" + containers.size());
                    for (ResponceContainer conteiner:
                         containers) {
                        vBoxCallable.getChildren().addAll(conteiner.getNodeList());
                    }
                     return vBoxCallable;
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            return null;
            };

            Future<VBox> vBoxFuture = service.submit(toLeft);

            Callable<Boolean> addvBoxFuture = () -> {
                try {
                    left.getChildren().addAll(vBoxFuture.get().getChildren());
                    scrollPane.setContent(left);
                    mainPane.setLeft(scrollPane);
                    playButton.setDisable(false);
                    return true;
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return false;
                }
            };

            Platform.runLater(() -> {

            try {
                Future<Boolean> fb = service.submit(addvBoxFuture);

                fb.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
                });
            service.shutdown();
            });//event->onMouseClicked: SearchButton

        playButton.setOnAction((event)-> {
                    RadioButton selected = (RadioButton) SearchResult.toggleGroup.getSelectedToggle();
//                    System.out.println(selected.getId());
                    center.getChildren().remove(webview);
                    webview = new WebView();
                    webview.getEngine().reload();
                    webview.getEngine().load(null);
                    webview.getEngine().load(
                            "https://www.youtube.com/embed/" + selected.getId()
                    );
                    webview.setPrefSize(640, 390);
                    center.getChildren().add(webview);


//                    media = new Media(new File("/home/valeriy/Downloads/videoplayback (5).mp4").toURI().toString());
//                    try {
//                        URL url = new URL("http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv");
//                        media = new Media(url.toURI().toString());
//                        player = new MediaPlayer(media);
//                        viewer.setMediaPlayer(player);
//                        player.play();
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
                }
        );

    }

    private double width = 1100;
    private double height = 700;

    private void setParam(Stage stage){
        stage.setMinWidth(width);
        stage.setMaxWidth(width);
        stage.setMaxHeight(height);
        stage.setMinHeight(height);


        left.setMaxWidth(400);
        scrollPane.setMaxWidth(400);
    }
}
