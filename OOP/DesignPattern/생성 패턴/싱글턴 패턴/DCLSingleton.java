public class DCLSingleton {
    private static DCLSingleton uniqueInstance;

    public String value;

    private DCLSingleton(String value){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.value = value;
    }

    public static synchronized DCLSingleton getInstance(String value) {
        if(uniqueInstance == null){
            synchronized (DCLSingleton.class){
                if(uniqueInstance == null){
                    uniqueInstance = new DCLSingleton(value);
                }
            }
        }
        return uniqueInstance;
    }
}
