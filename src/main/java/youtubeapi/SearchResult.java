package youtubeapi;

import entiti.Responce;
import javafx.scene.control.*;

class SearchResult {
    private ResponceContainer[] containers;
    static ToggleGroup toggleGroup = new ToggleGroup();

    SearchResult(Responce responce){
        int count = 0;
        containers = new ResponceContainer[responce.items.length];
        for (int j = 0, i = 0; j < containers.length; j++, i++) {
            String radioButton = responce.items[j].snippet.title;
            String channel = "Chanel: " + responce.items[j].snippet.channelTitle;
            String date = responce.items[j].snippet.publishedAt;
            String id = responce.items[j].id.videoId;
            String imageUrl = responce.items[j].snippet.thumbnails.medium.url;
            System.out.println(i + "i: " + id);
            System.out.println("j: " + j);
            if (id == null) {
                i--;
                count++;
                if (count == 5) break; //if video id is null 5 time in a row - break
                continue;
            }
            containers[i] = new ResponceContainer(radioButton, channel, date, id, imageUrl);
        }
    }

    public ResponceContainer[] getContainers() {
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
