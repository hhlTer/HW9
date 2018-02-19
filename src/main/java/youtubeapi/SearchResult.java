package youtubeapi;

import entiti.Responce;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

class SearchResult {
    private Responce responces;

    SearchResult(Responce responce){
        this.responces = responce;
    }

    VBox getVBox(){
        VBox vBox = new VBox();
        int count = responces.items.length;
        for (int i = 0; i < count; i++) {
            Text name = new Text("Name: " + responces.items[i].snippet.title);
            Text channel = new Text("Chanel: " + responces.items[i].snippet.channelTitle);
            Button play = new Button("Play");
            Text delimiter = new Text("");

            vBox.getChildren().addAll(name, channel, play, delimiter);
        }
        return vBox;
    }
}
