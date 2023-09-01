import proxy.SubjectProxy;

public class ProxyDemo {
    public static void main(String[] args) {
        SubjectProxy proxy = new SubjectProxy();
        proxy.setName("Hoon");
        proxy.showName();
        proxy.complicatedWork();
    }
}
