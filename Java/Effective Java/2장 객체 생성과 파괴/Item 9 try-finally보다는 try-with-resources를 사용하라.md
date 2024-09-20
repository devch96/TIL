# try-finally보다는 try-with-resources를 사용하라

- close 메서드를 호출해 직접 닫아줘야 하는 자원이 많다.
- InputStream, OutputStream, java.sql.Connection 등이 있다.

### try-finally

```java
static String firstLineOfFile(String path) throws IOException{
    BufferedReader br = new BufferedReader(new FileReader(path));
    try{
        return br.readLine();
    }finally{
        br.close();
    }
}
```

### 자원이 둘 이상이면 try-finally 방식은 지저분하다

```java
static void copy(String src, String dst) throws IOException{
    InputStream in = new FileInputStream(src);
    try {
        OutputStream out = new FileOutputStream(dst);
        try {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n == in.read(buf)) >= 0) {
                out.write(buf, 0, n);
            }
        } finally {
            out.close();
        }
    } finally{
        in.close();
    }
}
```

- try 블록과 finally 블록 모두에서 예외가 발생할 수 있는데 예컨대 기기에 물리적인 문제가 생긴다면 firstLineOfFile
메서드 안의 readLine 메서드가 예외를 던지고 같은 이유로 close 메서드도 실패할 것이다.
- 이런 상황이면 두 번째 예외가 첫 번째 예외를 완전히 집어삼켜 버려서 첫 번째 예외에 관한 정보는 남지 않게 되어 디버깅을 몹시 어렵게 한다.
- 이러한 문제들을 자바 7 이 제공한 try-with-resources덕에 해결되었다.
- 이 구조를 사용하려면 AutoCloseable 인터페이스를 구현해야 한다.

### try-with-resources

```java
static String firstLineOfFile(String path) throws IOException{
    try (BufferedReader br = new BufferedReader(new FileReader(path))){
        return br.readLine();
    }
}

static void copy(String src, String dst) throws IOException{
    try (InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst)){
        byte[] buf = new byte[BUFFER_SIZE];
        int n;
        while ((n == in.read(buf)) >= 0){
        out.write(buf,0,n);
        }
    }
}
```

- 예외가 삼켜지는 것이 아닌 스택 추적 내역에 '숨겨졌다(suppressed)'라는 꼬리표를 달고 출력된다.
- catch절도 쓸 수 있어 try문을 더 중첩하지 않고도 다수의 예외처리를 할 수 있다.

## 핵심 정리

- 꼭 회수해야 하는 자원을 다룰 때는 try-finally 말고 try-with-resources를 사용하자.
- 코드는 더 짧고 분명해지고, 만들어지는 예외 정보도 훨씬 유용하다.