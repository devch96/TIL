import devices.Device;
import devices.Radio;
import devices.Tv;
import remotes.BasicRemote;
import remotes.Remote;

public class BridgeDemo {
    public static void main(String[] args) {
        Device radio = new Radio();
        Device tv = new Tv();
        Remote remote = new BasicRemote(radio);
        remote.volumeDown();
        remote.volumeUp();
        remote.channelDown();
        remote.channelUp();
        System.out.println("=======================");
        remote = new BasicRemote(tv);
        remote.volumeDown();
        remote.volumeUp();
        remote.channelDown();
        remote.channelUp();
    }
}
