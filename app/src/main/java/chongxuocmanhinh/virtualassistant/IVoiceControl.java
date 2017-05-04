package chongxuocmanhinh.virtualassistant;

/**
 * Created by L on 04/05/2017.
 */

public interface IVoiceControl {
    // This will be executed when a voice command was found
    public abstract void processVoiceCommands(String... voiceCommands);

    // This will be executed after a voice command was processed to keep the recognition service activated
    public void restartListeningService();
}
