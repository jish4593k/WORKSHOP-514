import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BotStatusChecker extends Application {
    private String sessionString;
    private int apiId;
    private String apiHash;
    private List<String> bots;
    private String botOwner;
    private int updateChannel;
    private int messageId;
    private int timeLimit;
    private int sleepTime;
    private String heading;
    private String attachLink;

    private TextArea statusTextArea;

    public BotStatusChecker(String sessionString, int apiId, String apiHash, List<String> bots,
                            String botOwner, int updateChannel, int messageId, int timeLimit,
                            int sleepTime, String heading, String attachLink) {
        this.sessionString = sessionString;
        this.apiId = apiId;
        this.apiHash = apiHash;
        this.bots = bots;
        this.botOwner = botOwner;
        this.updateChannel = updateChannel;
        this.messageId = messageId;
        this.timeLimit = timeLimit;
        this.sleepTime = sleepTime;
        this.heading = heading;
        this.attachLink = attachLink;
    }

    @Override
    public void start(Stage primaryStage) {
        statusTextArea = new TextArea();
        statusTextArea.setEditable(false);

        StackPane root = new StackPane();
        root.getChildren().add(statusTextArea);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("");
        primaryStage.setScene(scene);
        primaryStage.show();

        startChecking();
    }

    private void startChecking() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> statusTextArea.appendText(""));

                String statusText = heading + "\n";

                for (String bot : bots) {
                    Platform.runLater(() -> statusTextArea.appendText("@" + bot + "\n"));
                    boolean isBotOnline = simulateBotCommunication(bot);

                    if (!isBotOnline) {
                        Platform.runLater(() -> {
                            statusTextArea.appendText("" + bot + " n");
                            statusText += ")" +
                                    "n";
                            simulateSendMessage(botOwner, "@" + bot + " n`");
                        });
                    } else {
                        Platform.runLater(() -> statusTextArea.appendText(""));
                        statusText += ")" +
                                "\";
                    }
                }

                String utcNow = simulateGetCurrentTime();
                statusText += "\\n" + utcNow + " UTC ⏰";
                statusText += " " + timeLimit / 60 + " `";

                try {
                    Platform.runLater(() -> statusTextArea.appendText("[INFO] Everything done! Sleeping for "
                            + timeLimit / 60 + " hours...\n"));

                    simulateEditMessageText(updateChannel, messageId, statusText);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        String errorText = "r :- " + e.getMessage() + "\n";
                        statusTextArea.appendText(errorText);
                        simulateSendMessage(botOwner, errorText);
                    });
                }
            }
        }, 0, timeLimit * 60 * 1000);
    }


    private void simulateEditMessageText(int chatId, int messageId, String text) {
        
        Platform.runLater(() -> {
            statusTextArea.appendText(" " + chatId +
                    ", e " + messageId + ": " + text + "\n");
        });
    }

    public static void main(String[] args) {
       
        String sessionString = "_string";
        int apiId = 123456;
        String apiHash = "sh";
        List<String> bots = List.of("bot1", "bot2");
        String botOwner = "ome";
        int updateChannel = 123;
        int messageId = 456;
        int timeLimit = 300;
        int sleepTime = 30;
        String heading = "-*--";
        String attachLink = "";

      
        launch(args);

        
        BotStatusChecker botStatusChecker = new BotStatusChecker(
                sessionString, apiId, apiHash, bots, botOwner, updateChannel, messageId,
                timeLimit, sleepTime, heading, attachLink
        );
        botStatusChecker.start();
    }
}
