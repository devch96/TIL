package proxy;

import proxySubject.RealSubject;
import proxySubject.Subject;

public class SubjectProxy implements Subject {
    public RealSubject subject;
    public String name;
    @Override
    public void showName() {
        System.out.println("proxy가 대신 처리 가능");
        System.out.println(name);
    }

    @Override
    public void setName(String name) {
        System.out.println("proxy가 대신 처리 가능");
        if(subject!=null){
            subject.setName(name);
        }
        this.name = name;
    }

    @Override
    public void complicatedWork() {
        subject = new RealSubject();
        subject.complicatedWork();
    }
}
