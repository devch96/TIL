public class MultiThreadSafeSingleton {
    private static MultiThreadSafeSingleton uniqueInstance;

    public String value;
    private MultiThreadSafeSingleton(String value){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.value = value;
    }

    public static synchronized MultiThreadSafeSingleton getInstance(String value) {
        if (uniqueInstance == null) {
            uniqueInstance = new MultiThreadSafeSingleton(value);
        }
        return uniqueInstance;
    }
}
