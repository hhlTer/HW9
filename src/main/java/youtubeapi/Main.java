package youtubeapi;

import com.alibaba.fastjson.JSON;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import entiti.Item;
import entiti.Responce;
import entiti.SnippetVideo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
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
import java.util.concurrent.*;


public class Main extends Application {

    static Media media;// = new Media(new File("/home/valeriy/Downloads/gojavabonus2.mp4").toURI().toString());
    private static MediaPlayer player;// = new MediaPlayer(media);

    private ResponceContainer[] containers;

    public static void main(String[] args) {
        launch(args);
    }
    private BorderPane mainPane = new BorderPane();
    private VBox left = new VBox();
    private VBox right = new VBox();
    private VBox center = new VBox();
    private VBox top = new VBox();
    private HBox bottom = new HBox();

    @Override
    public void start(Stage primaryStage) throws Exception {
        setParam(primaryStage);

        //top

        Text title = new Text("->");
        title.setFill(Color.BLUE);

        TextField searchField = new TextField();
        Text nameText = new Text("Name: ");
        Text channelText = new Text("Channel: ");
        Text dateText = new Text("Date: ");

        Button playButton = new Button("Play");
        Button searchButton = new Button ("Start search");

        top.getChildren().addAll(searchField,nameText,channelText,dateText,playButton, searchButton);
        top.setStyle("-fx-background-color: GAINSBORO");
        top.setSpacing(3);
        mainPane.setTop(top);

//left
        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setOrientation(Orientation.VERTICAL);
        left.getChildren().add(scrollBar);

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
                    Responce responce = YoutubeAPI.search(searchField.getText(), 10);
                    SearchResult searchResult = new SearchResult(responce);
                    this.containers = searchResult.getContainers();
                    VBox vBoxCallable = new VBox();
                    for (ResponceContainer container : containers) {
                        vBoxCallable.getChildren().addAll(container.getNodeList());
                    }
                    return vBoxCallable;
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            return null;
            };

            Future<VBox> vBoxFuture = service.submit(toLeft);
                Platform.runLater(() -> {
                    try {
                        left.getChildren().addAll(vBoxFuture.get().getChildren());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });

            mainPane.setLeft(left);
            service.shutdown();
        });//event->onMouseClicked

        playButton.setOnAction((event)-> {
                    //unirest include

                    RadioButton selected = (RadioButton) SearchResult.toggleGroup.getSelectedToggle();
                    System.out.println(selected.getId());
                    center.getChildren().clear();

                    WebView webview = new WebView();
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
    }
}
