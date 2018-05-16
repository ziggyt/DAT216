package iMat;

class Message {
    Message(String messageTitle, String messageContent) {
        this.messageTitle = messageTitle;
        this.messageContent = messageContent;
    }

    private String messageTitle;
    private String messageContent;

    String getMessageTitle() {
        return messageTitle;
    }

    String getMessageContent() {
        return messageContent;
    }
}
