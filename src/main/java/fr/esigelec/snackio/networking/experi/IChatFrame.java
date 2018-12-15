package fr.esigelec.snackio.networking.experi;

// This class represents the chat window on the client.
public interface IChatFrame {
    public void addMessage (String message);

    public void setNames (String[] names);
}