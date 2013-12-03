package client;

import canvas.CanvasModel;


/**
 * Class that stores the state of each individual client's canvas.
 * Every time an edit is made, each client mutates its own ClientCanvasModel.
 */
public class ClientCanvasModel extends CanvasModel{
    public ClientCanvasModel(){
        super();
    }
}
