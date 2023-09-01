package proxySubject;

public class RealSubject implements Subject{
    private String name;
    @Override
    public void showName() {
        System.out.println(name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void complicatedWork() {
        System.out.println("Proxy가 처리 못하는 작업(실제 객체에서 처리)");
    }
}
