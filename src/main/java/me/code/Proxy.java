package me.code;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Proxy {

    private final int port;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final List<Server> servers;



    public Proxy(int port) {
        this.port = port;
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        this.servers = new ArrayList<>();
    }

    public void load(){
        System.out.println("loading...."+port);
        File file = new File("config.txt");
        if(!file.exists()){

            try{
                file.createNewFile();
            }catch (IOException e) {
            e.printStackTrace();
            }
        }

        try {
            Scanner serverInput = new Scanner(file);
            while (serverInput.hasNextLine()) {
                String line = serverInput.nextLine();
                String[] split = line.split(" ");
                servers.add(new Server(Integer.parseInt(split[1]),split[0]));
                System.out.println("Search: file--> " + servers);
            }
            serverInput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ProxyChannelInitializer(this))
                    .bind(this.port).sync().channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int portIndex = 0;

    public Server roundRobin() {
        System.out.println("currentPort: " + portIndex + " RoundRobin, " + servers);

        if (portIndex >= servers.size()) {
            portIndex = 0;
        }
        return servers.get(portIndex++);
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }
}
