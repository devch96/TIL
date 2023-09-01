import context.Fighter;
import context.Kim;
import strategy.*;

public class StrategyDemo {
    public static void main(String[] args) {
        JumpBehavior shortJump = new ShortJump();
        JumpBehavior longJump = new LongJump();

        KickBehavior tornadoKick = new TornadoKick();

        Fighter kim = new Kim(tornadoKick, longJump);

        kim.punch();
        kim.kick();
        kim.jump();

        kim.setJumpBehavior(shortJump);
        kim.jump();
    }
}
