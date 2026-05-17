package com.gymflow.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import com.gymflow.model.ChatMessage;
import com.gymflow.service.RagService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatbotController implements Initializable {

    // ── FXML nodes ────────────────────────────────────────────────────────────
    @FXML private ScrollPane  scrollPane;
    @FXML private VBox        messagesContainer;
    @FXML private TextField   inputField;
    @FXML private Button      sendButton;
    @FXML private HBox        typingIndicator;
    @FXML private Circle      statusDot;
    @FXML private Label       statusLabel;

    // ── State ─────────────────────────────────────────────────────────────────
    private final RagService          ragService   = new RagService();
    private final List<ChatMessage>   chatHistory  = new ArrayList<>();

    // ── Init ──────────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Auto-scroll to bottom whenever messages are added
        messagesContainer.heightProperty().addListener(
                (obs, oldH, newH) -> scrollPane.setVvalue(1.0)
        );

        // Enter key sends message
        inputField.setOnAction(e -> sendMessage());

        // Check if Python backend is running
        checkServerStatus();

        // Welcome message
        addBotBubble(
                "Bonjour! 👋 Je suis votre assistant marketing GymFlow.\n\n" +
                "Je peux vous aider avec:\n" +
                "• Stratégies face à la concurrence low-cost\n" +
                "• Réduction du churn\n" +
                "• Fidélisation des membres\n" +
                "• Ciblage de nouveaux segments\n\n" +
                "Comment puis-je vous aider aujourd'hui?"
        );
    }

    // ── Send message ──────────────────────────────────────────────────────────
    @FXML
    private void sendMessage() {
        String query = inputField.getText().trim();
        if (query.isEmpty()) return;

        inputField.clear();
        inputField.setDisable(true);
        sendButton.setDisable(true);

        // Add user bubble immediately
        addUserBubble(query);
        chatHistory.add(new ChatMessage(query, ChatMessage.Sender.USER));

        // Show typing indicator
        setTyping(true);

        // Call RAG API on background thread (never block JavaFX thread)
        Task<RagService.RagResponse> task = new Task<>() {
            @Override
            protected RagService.RagResponse call() throws Exception {
                return ragService.ask(query);
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            setTyping(false);
            RagService.RagResponse resp = task.getValue();
            addBotBubble(resp.answer);
            chatHistory.add(new ChatMessage(resp.answer, ChatMessage.Sender.BOT));
            inputField.setDisable(false);
            sendButton.setDisable(false);
            inputField.requestFocus();
        }));

        task.setOnFailed(e -> Platform.runLater(() -> {
            setTyping(false);

            // Get the actual error message from the exception
            Throwable ex = task.getException();
            String detail = (ex != null) ? ex.getMessage() : "Erreur inconnue";

            addBotBubble(
                    "❌ Erreur RAG:\n\n" + detail
            );
            inputField.setDisable(false);
            sendButton.setDisable(false);
        }));

        new Thread(task, "rag-api-thread").start();
    }

    // ── Clear chat ────────────────────────────────────────────────────────────
    @FXML
    private void clearChat() {
        messagesContainer.getChildren().clear();
        chatHistory.clear();
        addBotBubble("Conversation réinitialisée. Comment puis-je vous aider?");
    }

    // ── Build bubbles programmatically ────────────────────────────────────────
    private void addUserBubble(String text) {
        ChatMessage msg = new ChatMessage(text, ChatMessage.Sender.USER);

        Label bubble = new Label(text);
        bubble.setWrapText(true);
        bubble.setMaxWidth(460);
        bubble.getStyleClass().add("user-bubble");

        Label time = new Label(msg.getTimestamp());
        time.getStyleClass().add("timestamp");

        VBox inner = new VBox(2, bubble, time);
        inner.setAlignment(Pos.CENTER_RIGHT);

        HBox wrapper = new HBox(inner);
        wrapper.setAlignment(Pos.CENTER_RIGHT);
        HBox.setMargin(inner, new Insets(0, 12, 0, 60));

        messagesContainer.getChildren().add(wrapper);
    }

    private void addBotBubble(String text) {
        ChatMessage msg = new ChatMessage(text, ChatMessage.Sender.BOT);

        Label bubble = new Label(text);
        bubble.setWrapText(true);
        bubble.setMaxWidth(460);
        bubble.getStyleClass().add("bot-bubble");

        Label time = new Label(msg.getTimestamp());
        time.getStyleClass().add("timestamp");

        VBox inner = new VBox(2, bubble, time);
        inner.setAlignment(Pos.CENTER_LEFT);

        HBox wrapper = new HBox(inner);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin(inner, new Insets(0, 60, 0, 12));

        messagesContainer.getChildren().add(wrapper);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void setTyping(boolean typing) {
        typingIndicator.setVisible(typing);
        typingIndicator.setManaged(typing);
    }

    private void checkServerStatus() {
        Task<Boolean> check = new Task<>() {
            @Override
            protected Boolean call() {
                return ragService.isServerRunning();
            }
        };
        check.setOnSucceeded(e -> Platform.runLater(() -> {
            boolean online = check.getValue();
            statusDot.getStyleClass().removeAll("status-online", "status-offline");
            statusDot.getStyleClass().add(online ? "status-online" : "status-offline");
            statusLabel.setText(online ? "En ligne" : "Hors ligne — lancez python api.py");
        }));
        new Thread(check, "status-check-thread").start();
    }
}
