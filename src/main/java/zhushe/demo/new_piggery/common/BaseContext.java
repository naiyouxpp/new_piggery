package zhushe.demo.new_piggery.common;

public class BaseContext {

    /*
    *Thread指线程
    * ThreadLocal就是单个线程，一次请求内后端使用的方法都在同一个线程，doFilter方法update方法等都在同一线程
    * 前面有的值后面没法使用，也不可能传参，所以就可以自定义一个类，通过静态方法，结合threadLocal类
    * 让他成为我们的工具类，threadLocal方法可以实现存值和取值的功能，但是只能在同一线程内才有效，所以如果不在同一线程的方法
    * 是没法取到值的，也意味着取值不会混乱
    *
    * */


    //设置id值
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();


    public static void setCurrentId(Integer id){
        threadLocal.set(id);
    }

    //使用id值
    public static Integer getCurrentId(){
      return  threadLocal.get();
    }
}
