package me.code;

public class Main {

    public static void main(String[] args) {

        Proxy proxy = new Proxy(6000);
        proxy.load();
        proxy.start();

    }
}
