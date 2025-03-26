public class test {
    String name;
    int age;

    // 无参构造方法
    public test() {
        this("无名氏", 18); // 调用有参构造方法
        System.out.println("无参构造方法");
    }

    // 有参构造方法
    public test(String name, int age) {
        this.name = name;
        this.age = age;
        System.out.println("有参构造方法");
    }
 
    public static void main(String[] args) {
        test test1 = new test();
        test test2 = new test("张三", 20);
}
}