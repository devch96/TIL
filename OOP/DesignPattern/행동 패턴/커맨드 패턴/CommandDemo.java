import command.Command;
import command.LightOffCommand;
import command.LightOnCommand;
import invoker.Light;
import reciever.RemoteControl;

public class CommandDemo {

    public static void main(String[] args) {
        RemoteControl remote = new RemoteControl();
        Light light = new Light();
        remote.setCommand(new LightOnCommand(light));
        remote.buttonWasPressed();
        remote.setCommand(new LightOffCommand(light));
        remote.buttonWasPressed();
    }
}
