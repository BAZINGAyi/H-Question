package com.bazinga;

/**
 * Created by bazinga on 2017/4/27.
 */
enum Car {
    lamborghini,tata,audi,fiat,honda
}
public class EnumTest {
    private void set(){
        Car c;
        c = Car.tata;
        switch(c) {
            case lamborghini:
                System.out.println("你选择了 lamborghini!");
                break;
            case tata:
                System.out.println("你选择了 tata!");
                break;
            case audi:
                System.out.println("你选择了 audi!");
                break;
            case fiat:
                System.out.println("你选择了 fiat!");
                break;
            case honda:
                System.out.println("你选择了 honda!");
                break;
            default:
                System.out.println("我不知道你的车型。");
                break;
        }
    }
}
