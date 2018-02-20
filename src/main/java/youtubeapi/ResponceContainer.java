package youtubeapi;

import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ResponceContainer {
    private RadioButton radioButton;
    private Text channel;
    private Text date;
    private Text delimiter = new Text("");
    private ImageView imageView;

    private List<Node> nodeList = new ArrayList<>(3);

    ResponceContainer(String radioButton, String channel, String date, String id, String imageUrl){
//------------------radioButton----------------------------
        this.radioButton = new RadioButton(radioButton);
        this.radioButton.setId(id);
        this.radioButton.setToggleGroup(SearchResult.toggleGroup);
//---------------------chanel------------------------------
        this.channel = new Text(channel);
//----------------------date-------------------------------
        Instant instant = Instant.parse(date);
        Date dateFromInst = Date.from(instant);
        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
        this.date = new Text("Date: " + format.format(dateFromInst));
//----------------------image------------------------------

        Image image = new Image(imageUrl);
        imageView = new ImageView(image);
        imageView.setFitWidth(120);
        imageView.setFitHeight(90);
//---------------------------------------------------------

        nodeList.add(this.radioButton);
        nodeList.add(this.channel);
        nodeList.add(this.date);
        nodeList.add(this.imageView);
        nodeList.add(delimiter);
    }

    List<Node> getNodeList() {
        return nodeList;
    }
}
