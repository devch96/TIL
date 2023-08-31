package remotes;

import devices.Device;

public class BasicRemote implements Remote{
    public Device device;

    public BasicRemote(){}

    public BasicRemote(Device device){
        this.device = device;
    }

    @Override
    public void volumeDown() {
        System.out.println("리모컨 볼륨 다운");
        device.setVolume(device.getVolume()- 5);
    }

    @Override
    public void volumeUp() {
        System.out.println("리모컨 볼륨 업");
        device.setVolume(device.getVolume()+ 5);
    }

    @Override
    public void channelDown() {
        System.out.println("리모컨 채널 다운");
        device.setChannel(device.getChannel() - 1);
    }

    @Override
    public void channelUp() {
        System.out.println("리모컨 채널 업");
        device.setChannel(device.getChannel() + 1);
    }
}
