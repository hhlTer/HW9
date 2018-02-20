package youtubeapi;

import entiti.Responce;
import javafx.scene.control.*;

class SearchResult {
    private ResponceContainer[] containers;
    static ToggleGroup toggleGroup = new ToggleGroup();

    SearchResult(Responce responce){
        int count = 0;
        containers = new ResponceContainer[responce.items.length];
        for (int i = 0; i < containers.length; i++) {
            String radioButton = responce.items[i].snippet.title;
            String channel = "Chanel: " + responce.items[i].snippet.channelTitle;
            String date = responce.items[i].snippet.publishedAt;
            String id = responce.items[i].id.videoId;
            String imageUrl = responce.items[i].snippet.thumbnails.medium.url;
            System.out.println(i + ": " + id);
            if (id == null) {
                count++;
                i--;
                if (count == 5) break; //if video id is null 5 time in a row - break
                continue;
            }
            containers[i] = new ResponceContainer(radioButton, channel, date, id, imageUrl);
        }
    }

    ResponceContainer[] getContainers() {
        return containers;
    }

//    VBox getVBox(){
//        VBox vBox = new VBox();
//        int count = responces.items.length;
//        for (int i = 0; i < count; i++) {
//            RadioButton radioButton = new RadioButton(responces.items[i].snippet.title);
//            Text channel = new Text("Chanel: " + responces.items[i].snippet.channelTitle);
//            Text delimiter = new Text("");
//            containers[i] = new ResponceContainer(radioButton, channel);
////            vBox.getChildren().addAll(radioButton, channel, delimiter);
//        }
//        return vBox;
//    }

}
