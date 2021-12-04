package me.code;

import lombok.Data;

@Data
public class Server {
    private int PORT;
    private String address;

    public Server(int PORT, String address){
        this.PORT = PORT;
        this.address = address;
    }
    public int getPORT(){
        return this.PORT;

    }

}
