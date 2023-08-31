public class SingletonTest {
    public static void main(String[] args) {
        SingletonTest singletonTest = new SingletonTest();
        System.out.println("기본 싱글턴 테스트");
        System.out.println();
//        singletonTest.singletonTest();

        System.out.println();
        System.out.println("멀티 스레드 상황");
        System.out.println("========================");
        Thread threadFoo = new Thread(new ThreadFoo());
        Thread threadBar = new Thread(new ThreadBar());
        threadFoo.start();
        threadBar.start();
    }

    private void singletonTest(){
        System.out.println("단일 스레드 상황");
        System.out.println("=========================");
        BasicSingleton singleton = BasicSingleton.getInstance("첫번째 생성");
        BasicSingleton anotherSingleton = BasicSingleton.getInstance("두번째 생성");
        System.out.println(singleton.value);
        System.out.println(anotherSingleton.value);
    }

    static class ThreadFoo implements Runnable {
        @Override
        public void run() {
//            BasicSingleton singleton = BasicSingleton.getInstance("첫번째 생성");
//            MultiThreadSafeSingleton singleton = MultiThreadSafeSingleton.getInstance("첫번째 생성");
            DCLSingleton singleton = DCLSingleton.getInstance("첫번째 생성");
            System.out.println(singleton.value);
        }
    }

    static class ThreadBar implements Runnable {
        @Override
        public void run() {
//            BasicSingleton singleton = BasicSingleton.getInstance("두번째 생성");
//            MultiThreadSafeSingleton singleton = MultiThreadSafeSingleton.getInstance("두번째 생성");
            DCLSingleton singleton = DCLSingleton.getInstance("두번째 생성");
            System.out.println(singleton.value);
        }
    }
}
