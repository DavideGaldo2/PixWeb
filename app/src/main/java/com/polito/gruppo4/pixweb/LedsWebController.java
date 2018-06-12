package com.polito.gruppo4.pixweb;

import android.os.Handler;
import android.os.Message;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import com.polito.gruppo4.pixweb.Color;


/**
 * Created by David on 23/09/2017.
 */

/*
 * Ogni ramo della ragnatela è comandato da un thread separato
 * Per inviare un nuovo led nella ragnatela bisogna inserire un dato nella
 * blocking queue con il metodo insertInWire
 */

public class LedsWebController extends Thread{


    private Handler mNetworkHandler;

    private final JSONArray pixels_array;

    private BlockingQueue<Color> bQueWire;


    final int  fullColor = 255;
    final int halfColor= 200;

    //Numero tirante da controllare
    final private int wireNumber;


    int wireStartLed;             //Primo indice del tirante
    int wireEndLed;               //Ultimo indice del tirante

    int firstWireStartLed;        //Primo Led del primo filo (quello lontano dal display)
    int firstWireEndLed;          //Ultimo Led del primo filo ( quello vicino al display)
    int secondWireStartLed;       //Primo Led del secondo filo(lontano dal display)
    int secondWireEndLed;         //Ultimo Led del secondo filo(vicino al display)
    int wireLength;               //Lunghezza del tirante

    private Integer pulseDelay=new Integer(0);         //Variabile usata per gestire la pulsazione degli anelli

    //Constructor
    public LedsWebController(JSONArray pix_array, Handler networkHandler, int wireNumber){
        this.mNetworkHandler=networkHandler;
        this.pixels_array=pix_array;
        this.wireNumber=wireNumber;
        bQueWire=new LinkedBlockingQueue<Color>();
        setWireParam();

    }

    //Set the parameters of the wire
    private void setWireParam(){
        switch(this.wireNumber){
            case 1:{
                wireStartLed=0;
                wireEndLed=51;
                firstWireStartLed=25;                          //Primo Led del primo filo (quello lontano dal display)
                firstWireEndLed= wireStartLed;                  //Ultimo Led del primo filo ( quello vicino al display)
                secondWireStartLed=firstWireStartLed+1;         //Primo Led del secondo filo(lontano dal display)
                secondWireEndLed=wireEndLed;                    //Ultimo Led del secondo filo(vicino al display)
                wireLength=firstWireStartLed-firstWireEndLed+1;   //Lunghezza del tirante
                break;
            }
            case 2:{
                wireStartLed=52;
                wireEndLed=185;
                firstWireStartLed=118;                          //Primo Led del primo filo (quello lontano dal display)
                firstWireEndLed= wireStartLed;                  //Ultimo Led del primo filo ( quello vicino al display)
                secondWireStartLed=firstWireStartLed+1;         //Primo Led del secondo filo(lontano dal display)
                secondWireEndLed=wireEndLed;                    //Ultimo Led del secondo filo(vicino al display)
                wireLength=firstWireStartLed-firstWireEndLed+1;   //Lunghezza del tirante
                break;
            }
            case 3:{
                wireStartLed=186;
                wireEndLed=317;
                firstWireStartLed=251;                          //Primo Led del primo filo (quello lontano dal display)
                firstWireEndLed= wireStartLed;                  //Ultimo Led del primo filo ( quello vicino al display)
                secondWireStartLed=firstWireStartLed+1;         //Primo Led del secondo filo(lontano dal display)
                secondWireEndLed=wireEndLed;                    //Ultimo Led del secondo filo(vicino al display)
                wireLength=firstWireStartLed-firstWireEndLed+1;   //Lunghezza del tirante
                break;
            }
            case 4:{
                wireStartLed=318;
                wireEndLed=423;
                firstWireStartLed=370;                          //Primo Led del primo filo (quello lontano dal display)
                firstWireEndLed= wireStartLed;                  //Ultimo Led del primo filo ( quello vicino al display)
                secondWireStartLed=firstWireStartLed+1;         //Primo Led del secondo filo(lontano dal display)
                secondWireEndLed=wireEndLed;                    //Ultimo Led del secondo filo(vicino al display)
                wireLength=firstWireStartLed-firstWireEndLed+1;   //Lunghezza del tirante
                break;
            }
            case 5:{
                wireStartLed=424;
                wireEndLed=521;
                firstWireStartLed=472;                          //Primo Led del primo filo (quello lontano dal display)
                firstWireEndLed= wireStartLed;                  //Ultimo Led del primo filo ( quello vicino al display)
                secondWireStartLed=firstWireStartLed+1;         //Primo Led del secondo filo(lontano dal display)
                secondWireEndLed=wireEndLed;                    //Ultimo Led del secondo filo(vicino al display)
                wireLength=firstWireStartLed-firstWireEndLed+1; //Lunghezza del tirante
                break;
            }
            case 6: {   //Primo anello - interno
                wireStartLed=522;
                wireEndLed=612;
                firstWireStartLed=wireStartLed;                 //Primo Led del primo filo (quello lontano dal display)
                firstWireEndLed= wireEndLed;                    //Ultimo Led del primo filo ( quello vicino al display)
                secondWireStartLed=0;                           //Primo Led del secondo filo(lontano dal display)
                secondWireEndLed=0;                             //Ultimo Led del secondo filo(vicino al display)
                wireLength=wireEndLed-wireStartLed+1;           //Lunghezza del tirante
                break;
            }
            case 7: {   //Secondo anello
                wireStartLed=613;
                wireEndLed=790;
                firstWireStartLed=wireStartLed;                 //Primo Led del primo filo (quello lontano dal display)
                firstWireEndLed= wireEndLed;                    //Ultimo Led del primo filo ( quello vicino al display)
                secondWireStartLed=0;                           //Primo Led del secondo filo(lontano dal display)
                secondWireEndLed=0;                             //Ultimo Led del secondo filo(vicino al display)
                wireLength=wireEndLed-wireStartLed+1;           //Lunghezza del tirante
                break;
            }
            case 8: {   //Terzo anello - esterno
                wireStartLed=791;
                wireEndLed=1071;
                firstWireStartLed=wireStartLed;                 //Primo Led del primo filo (quello lontano dal display)
                firstWireEndLed= wireEndLed;                    //Ultimo Led del primo filo ( quello vicino al display)
                secondWireStartLed=0;                           //Primo Led del secondo filo(lontano dal display)
                secondWireEndLed=0;                             //Ultimo Led del secondo filo(vicino al display)
                wireLength=wireEndLed-wireStartLed+1;           //Lunghezza del tirante
                break;
            }
        }
    }

    @Override
    public void run() {
        super.run();
        switch(wireNumber){
            case 6:
            case 7:
            case 8:
                this.RingController();
                break;
            default:
                this.WireController();
                break;
        }


    }

    //Add a led in wire
    public void insertInWire(int r,int g, int b){
        //Aggiunge un token nella blocking queue
        Color c = new Color(r,g,b);
        bQueWire.add(c);
    }

    //Usato per passare le informazioni di Colore e attesa pulsazione al controller degli anelli
    public void setRingColorAndPulse(Color c, int pulseDelay){
        switch(wireNumber){
            case 6:
            case 7:
            case 8:{
                synchronized (this.pulseDelay) {
                    this.pulseDelay = pulseDelay;
                    bQueWire.add(c);
                }
                break;
            }
        }
    }


    /*
    * Metodo che gira su thread separato che preleva dalla blocking queue e posiziona nella
    * struttura condivisa pix_Array al ramo di riferimento, l'inserimento avviene solo se
    * sono trascorsi almeno due scorrimenti dall'ultimo inserimento
    * Il metodo si occupa anche di far scorrere verso il basso la luce, se il ramo è vuoto rimane
    * in attesa dell'ingresso di un "token di luce"
    * */
    private void WireController(){


        int shiftCounter = 0;   //Numero di movimenti che devono essere effettuati per far uscire
        // un led dal tirante

        Color c = new Color(0,0,0);
        int mSec=100;
        while(!isInterrupted()){
            try {
                Thread.sleep(mSec);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Ci sono elementi nella ragnatela su cui effettuare shift
            if(shiftCounter>0) {
                ShiftDownWire();
                shiftCounter--;
                synchronized (pixels_array){
                    if (!bQueWire.isEmpty()) {   //Se la coda è piena prelevo ulteriore token
                        //La prima posizione è libera?
                        try {
                            if (((JSONObject) pixels_array.get(firstWireStartLed)).getInt("r") == 0 &&
                                    ((JSONObject) pixels_array.get(firstWireStartLed)).getInt("g") == 0 &&
                                    ((JSONObject) pixels_array.get(firstWireStartLed)).getInt("b") == 0) {
                                shiftCounter = wireLength; //Resetto contatore di shift
                                c = bQueWire.take();
                                //Inserisco nuovo token
                                ((JSONObject) pixels_array.get(firstWireStartLed)).put("r", c.red);
                                ((JSONObject) pixels_array.get(firstWireStartLed)).put("g", c.green);
                                ((JSONObject) pixels_array.get(firstWireStartLed)).put("b", c.blue);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handleNetworkRequest(NetworkThread.SET_PIXELS, pixels_array, 0, 0);
                }
            }
            else{ //Non ci sono elementi nel tirante
                Shutdown();
                handleNetworkRequest(NetworkThread.SET_PIXELS, pixels_array, 0 ,0);
                try {
                    c = bQueWire.take(); //attendo che vengano inseriti token nella ragnatela
                    shiftCounter=wireLength;
                    synchronized (pixels_array){
                        //Questo è un inserimento in ramo vuoto, non c'è bisogno di controllo
                        //Inserisco nuovo token
                        ((JSONObject) pixels_array.get(firstWireStartLed)).put("r", c.red);
                        ((JSONObject) pixels_array.get(firstWireStartLed)).put("g", c.green);
                        ((JSONObject) pixels_array.get(firstWireStartLed)).put("b", c.blue);
                        handleNetworkRequest(NetworkThread.SET_PIXELS, pixels_array, 0 ,0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //Controller degli anelli, animazione pulsazione Gira su thread separato
    private void RingController(){

        int pulseDel=0;                     //Delay di pulsazione - locale
        Color pulseColor= new Color(0,0,0); //Colore di pulsazione - locale
        Color[] wire= new Color[wireLength];//Vettore che rappresenta l'anello - locale
        Color actualColor=new Color(0,0,0); //Colore attualmente riprodotto sull'anello

        //Inizializzazione dell'array
        for(int i=0;i<wire.length;i++)
            wire[i]=new Color(0,0,0);


        //L'anello deve pulsare con frequenza variabile, la frequenza ed il colore vengono passati
        //tramite la blocking queue ed una variabile intera dal metodo
        while(true){
            //Se ho passato un colore alla coda allora lavoro
            if(pulseColor.red!=0 || pulseColor.green!=0 || pulseColor.blue!=0){
                //Ciclo di accensione dell'anello
                while(actualColor.red<pulseColor.red || actualColor.green<pulseColor.green || actualColor.blue<pulseColor.blue){
                    if(actualColor.red<pulseColor.red )
                        actualColor.red+=5;
                    if(actualColor.green<pulseColor.green)
                        actualColor.green+=5;
                    if(actualColor.blue<pulseColor.blue)
                        actualColor.blue+=5;

                    synchronized (pixels_array){
                        //Aggiorno la struttura dati json della ragnatela
                        for (int i=0;i<wireLength;i++){
                            try{
                                ((JSONObject) pixels_array.get(wireStartLed+i)).put("r",actualColor.red);
                                ((JSONObject) pixels_array.get(wireStartLed+i)).put("g",actualColor.green);
                                ((JSONObject) pixels_array.get(wireStartLed+i)).put("b",actualColor.blue);
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //Invio il dato alla ragnatela
                        handleNetworkRequest(NetworkThread.SET_PIXELS, pixels_array, 0, 0);
                    }
                    try {
                        Thread.sleep(pulseDel);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                //A questo punto la ragnatela è accesa al massimo
                //attendo e poi la spengo
                try {
                    Thread.sleep(pulseDel*2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Ciclo di spegnimento dell'anello
                while(actualColor.red!=0 || actualColor.green!=0 || actualColor.blue!=0) {
                    if (actualColor.red != 0)
                        actualColor.red--;
                    if (actualColor.green != 0)
                        actualColor.green--;
                    if (actualColor.blue != 0)
                        actualColor.blue--;

                    synchronized (pixels_array) {
                        //Aggiorno la struttura dati json della ragnatela
                        for (int i = 0; i < wireLength; i++) {
                            try {
                                ((JSONObject) pixels_array.get(wireStartLed + i)).put("r", actualColor.red);
                                ((JSONObject) pixels_array.get(wireStartLed + i)).put("g", actualColor.green);
                                ((JSONObject) pixels_array.get(wireStartLed + i)).put("b", actualColor.blue);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //Invio il dato alla ragnatela
                        handleNetworkRequest(NetworkThread.SET_PIXELS, pixels_array, 0, 0);
                        try {
                            Thread.sleep(pulseDel);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //Anello spento

                //Controllo che non sia stato passato altro valore di colore all'anello
                if(!bQueWire.isEmpty()){
                    try {
                        pulseColor=bQueWire.take();
                        synchronized (this.pulseDelay){
                            pulseDel=this.pulseDelay;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                try {
                    pulseColor=bQueWire.take();
                    synchronized (this.pulseDelay){
                        pulseDel=this.pulseDelay;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Shift del token nel ramo
    private void ShiftDownWire(){
        //Parti dal basso del tirante
        //se ultimo led è al massimo dimezzalo
        //se ultimo led è dimezzato spegnilo
        //dal penultimo al secondo
        //prendi il valore e spostalo in basso
        //se il primo led è al massimo, spostalo al secondo e dimezzalo
        //se dimezzato spegnilo


        Color c= new Color(0,0,0);   //Colore da inserire nel tirante
        Color[] wire= new Color[wireLength];
        //Inizializzazione dell'array
        for(int i=0;i<wire.length;i++)
            wire[i]=new Color(0,0,0);

        //Richiesta del lock sulla struttura dati ragnatela
        synchronized (pixels_array){
            //Estrazione della porzione di ragnatela interessata dallo shift
            //estraggo solo il filo di sinistra dato che sono uguali
            for (int i=0;i<wireLength;i++){
                try{
                    wire[i].red=((JSONObject) pixels_array.get(firstWireStartLed-i)).getInt("r");
                    wire[i].green=((JSONObject) pixels_array.get(firstWireStartLed-i)).getInt("g");
                    wire[i].blue=((JSONObject) pixels_array.get(firstWireStartLed-i)).getInt("b");
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        //A questo punto si può lavorare sulla copia dei dati rilasciando il lock e permettendo ad
        //altri thread di lavorare in parallelo sulla struttura dati

        //Shift del vettore verso il basso;
        //Modifica ultimo valore (quello a contatto con il display)
        //Se il valore della componente è maggiore di 120 lo dimezza altrimenti lo azzera
        //dato che sul simulatore non si vedono le componenti con intensità <80
        if(wire[wire.length-1].red>80)
            wire[wire.length-1].red/=2;
        else
            wire[wire.length-1].red=0;

        if(wire[wire.length-1].green>80)
            wire[wire.length-1].green/=2;
        else
            wire[wire.length-1].green=0;

        if(wire[wire.length-1].blue>80)
            wire[wire.length-1].blue/=2;
        else
            wire[wire.length-1].blue=0;

        //Modifica corpo vettore
        //Parto dalla coda ed arrivo alla testa ( ultimo indice verso il primo)
        for(int i=wire.length-2;i>0;i--){
            //Se almeno una componente è != 0 effettuo lo shift (verso la coda)
            if(wire[i].red!=0 || wire[i].green!=0 || wire[i].blue!=0){
                wire[i+1].red=wire[i].red;
                wire[i+1].green=wire[i].green;
                wire[i+1].blue=wire[i].blue;

                //Spengo iu pixel che sono troppo fiochi
                if(wire[i].red<80)
                    wire[i].red=0;

                if(wire[i].green<80)
                    wire[i].green=0;

                if(wire[i].blue<80)
                    wire[i].blue=0;
            }
        }

        //Modifica primo valore (quello più distante dal display)
        //se diverso da 0 sposto verso il basso ed affievolisco il valore
        if(wire[0].red!=0 || wire[0].green!=0 || wire[0].blue!=0){
            wire[1].red=wire[0].red;
            wire[1].green=wire[0].green;
            wire[1].blue=wire[0].blue;

            if(wire[0].red>120)
                wire[0].red/=2;
            else
                wire[0].red=0;

            if(wire[0].green>120)
                wire[0].green/=2;
            else
                wire[0].green=0;

            if(wire[0].blue>120)
                wire[0].blue/=2;
            else
                wire[0].blue=0;
        }

        //Terminato lo shift reinserisco i valori nella struttura dati principale
        //Per farlo richiedo il lock
        synchronized (pixels_array){
            //Inserimento della porzione di ragnatela interessata dallo shift
            //A questo punto posso inserire in entrambi i fili della tirante
            //dato che questi sono identici

            try{

                for (int i=0;i<wireLength;i++){
                    //Ramo Sx
                    ((JSONObject) pixels_array.get(firstWireStartLed-i)).put("r",wire[i].red);
                    ((JSONObject) pixels_array.get(firstWireStartLed-i)).put("g",wire[i].green);
                    ((JSONObject) pixels_array.get(firstWireStartLed-i)).put("b",wire[i].blue);
                    //Ramo Dx
                    ((JSONObject) pixels_array.get(secondWireStartLed+i)).put("r",wire[i].red);
                    ((JSONObject) pixels_array.get(secondWireStartLed+i)).put("g",wire[i].green);
                    ((JSONObject) pixels_array.get(secondWireStartLed+i)).put("b",wire[i].blue);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }






    }

    //Shutdown the wire
    public void Shutdown(){
        synchronized (pixels_array){
            try{
                for (int i=wireStartLed;i<wireEndLed+1;i++){
                    ((JSONObject) pixels_array.get(i)).put("a", 255);
                    ((JSONObject) pixels_array.get(i)).put("r", 0);
                    ((JSONObject) pixels_array.get(i)).put("g", 0);
                    ((JSONObject) pixels_array.get(i)).put("b", 0);
                }
            } catch (JSONException exception) {
                // No errors expected here
            }
        }
    }



    public void scanRingClockwise(Color c, int numberOfTimes){
        //Parto dal primo led dell'anello e faccio ciclare in senso orario il token di luce
        //il token lascia la scia alle sue spalle



        Color[] wire= new Color[wireLength];
        //Inizializzazione dell'array
        for(int i=0;i<wire.length;i++)
            wire[i]=new Color(0,0,0);

        //Richiesta del lock sulla struttura dati ragnatela
        synchronized (pixels_array){
            //Estrazione della porzione di ragnatela interessata dallo shift
            //estraggo solo il filo di sinistra dato che sono uguali
            for (int i=0;i<wireLength;i++){
                try{
                    wire[i].red=((JSONObject) pixels_array.get(wireStartLed+i)).getInt("r");
                    wire[i].green=((JSONObject) pixels_array.get(wireStartLed+i)).getInt("g");
                    wire[i].blue=((JSONObject) pixels_array.get(wireStartLed+i)).getInt("b");
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //A questo punto si può lavorare sulla copia dei dati rilasciando il lock e permettendo ad
        //altri thread di lavorare in parallelo sulla struttura dati

        //Inserisco il colore nell'array
        wire[0].red=c.red;
        wire[0].green=c.green;
        wire[0].blue=c.blue;


        //Invio i dati alla ragnatela
        synchronized (pixels_array) {
            //Inserimento della porzione di ragnatela interessata dallo shift
            try {
                for (int j = 0; j < wireLength; j++) {
                    ((JSONObject) pixels_array.get(wireStartLed + j)).put("r", wire[j].red);
                    ((JSONObject) pixels_array.get(wireStartLed + j)).put("g", wire[j].green);
                    ((JSONObject) pixels_array.get(wireStartLed + j)).put("b", wire[j].blue);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            handleNetworkRequest(NetworkThread.SET_PIXELS, pixels_array, 0, 0);
        }





        //Primo 9 cicli inserimento
        for (int i =0 ;i<9;i++){
            //Se almeno una componente è != 0 effettuo lo shift verso destra
            if(wire[i].red!=0 || wire[i].green!=0 || wire[i].blue!=0) {
                wire[i + 1].red = wire[i].red;
                wire[i + 1].green = wire[i].green;
                wire[i + 1].blue = wire[i].blue;

                //Affievolisco i precedenti
                for (int k = i; k > 0; k--) {
                    wire[k].red = (wire[k].red > 100) ? wire[k].red - 30 : 0;
                    wire[k].green = (wire[k].green > 100) ? wire[k].green - 30 : 0;
                    wire[k].blue = (wire[k].blue > 100) ? wire[k].blue - 30 : 0;
                }

                //Inserimento della porzione di ragnatela interessata dallo shift
                synchronized (pixels_array) {
                    try {
                        for (int j = 0; j < wireLength; j++) {
                            ((JSONObject) pixels_array.get(wireStartLed + j)).put("r", wire[j].red);
                            ((JSONObject) pixels_array.get(wireStartLed + j)).put("g", wire[j].green);
                            ((JSONObject) pixels_array.get(wireStartLed + j)).put("b", wire[j].blue);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    handleNetworkRequest(NetworkThread.SET_PIXELS, pixels_array, 0, 0);
                }
            }
        }

        //Posso effettuare i cicli
        //Effettua il ciclo numberOfTimes volte
        for(int i=0;i<numberOfTimes;i++) {
            //Sposta la luce

            //Salvo utimo valore
            Color tmp = new Color(wire[wireLength - 1].red, wire[wireLength - 1].green, wire[wireLength - 1].blue);
            for (int y = wireLength - 1; y > 0; y--) {
                wire[y].red = wire[y - 1].red;
                wire[y].green = wire[y - 1].green;
                wire[y].blue = wire[y - 1].blue;
            }
            //Sovrascrivo primo valore con l'ultimo valore
            wire[0].red = tmp.red;
            wire[0].green = tmp.green;
            wire[0].blue = tmp.blue;

            //Inserimento della porzione di ragnatela interessata dallo shift
            synchronized (pixels_array) {
                try {
                    for (int j = 0; j < wireLength; j++) {
                        ((JSONObject) pixels_array.get(wireStartLed + j)).put("r", wire[j].red);
                        ((JSONObject) pixels_array.get(wireStartLed + j)).put("g", wire[j].green);
                        ((JSONObject) pixels_array.get(wireStartLed + j)).put("b", wire[j].blue);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handleNetworkRequest(NetworkThread.SET_PIXELS, pixels_array, 0, 0);
            }
        }












/*

            for(int y=0;y<wireLength-1;y++){

                }

                //Terminato uno shift reinserisco i valori nella struttura dati principale
                //Per farlo richiedo il lock
                synchronized (pixels_array){
                    //Inserimento della porzione di ragnatela interessata dallo shift
                    try{
                        for (int j=0;j<wireLength;j++){
                            ((JSONObject) pixels_array.get(wireStartLed+j)).put("r",wire[j].red);
                            ((JSONObject) pixels_array.get(wireStartLed+j)).put("g",wire[j].green);
                            ((JSONObject) pixels_array.get(wireStartLed+j)).put("b",wire[j].blue);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    handleNetworkRequest(NetworkThread.SET_PIXELS, pixels_array, 0, 0);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            //Move led from the last position to the first
            wire[0].red=wire[wire.length-1].red;
            wire[0].green=wire[wire.length-1].green;
            wire[0].blue=wire[wire.length-1].blue;

            //Affievolisco ultimo
            //Affievolisco il precedente
            wire[wire.length-1].red = (wire[wire.length-1].red>100)? wire[wire.length-1].red-30 : 0;
            wire[wire.length-1].green = (wire[wire.length-1].green>100)? wire[wire.length-1].green-30 : 0;
            wire[wire.length-1].blue = (wire[wire.length-1].blue>100)? wire[wire.length-1].blue-30 : 0;

            synchronized (pixels_array) {
                //Inserimento della porzione di ragnatela interessata dallo shift
                try {
                    for (int j = 0; j < wireLength; j++) {
                        ((JSONObject) pixels_array.get(wireStartLed + j)).put("r", wire[j].red);
                        ((JSONObject) pixels_array.get(wireStartLed + j)).put("g", wire[j].green);
                        ((JSONObject) pixels_array.get(wireStartLed + j)).put("b", wire[j].blue);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handleNetworkRequest(NetworkThread.SET_PIXELS, pixels_array, 0, 0);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        */
    }

    //Set all the pixels to black
    private JSONArray preparePixelsArray() {
        JSONArray pixels_array = new JSONArray();
        JSONObject tmp;
        try {
            for (int i = 0; i < 1072; i++) {
                tmp = new JSONObject();
                tmp.put("a", 255);
                tmp.put("g", 0);
                tmp.put("b", 0);
                tmp.put("r", 0);
                pixels_array.put(tmp);
            }
        } catch (JSONException exception) {
            // No errors expected here
        }
        return pixels_array;
    }

    private void handleNetworkRequest(int what, Object payload, int arg1, int arg2) {
        Message msg = mNetworkHandler.obtainMessage();
        msg.what = what;
        msg.obj = payload;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.sendToTarget();
    }






    /*
    * OLD METHODS
    * */

    //Set the pixels fo the wire1 to black
    public void resetWire1(){
        int firstLedOfWire1 = 0;
        int lastLedOfWire1 = 51;
        synchronized (pixels_array){
            try {
                for (int i = firstLedOfWire1; i <= lastLedOfWire1; i++){
                    ((JSONObject) pixels_array.get(i)).put("a", 255);
                    ((JSONObject) pixels_array.get(i)).put("r", 0);
                    ((JSONObject) pixels_array.get(i)).put("g", 0);
                    ((JSONObject) pixels_array.get(i)).put("b", 0);
                }
            } catch (JSONException exception) {
                // No errors expected here
            }
        }
    }

    private void Wire1Controller( ){
        int firstLxIndex = 25;  //Primo indice del led in alto cavo di sx
        int firstRxIndex = 26;  //Primo indice del led in alto cavo di sx
        int wireLength = 26;    //Lunghezza del tirante
        int shiftCounter = 0;   //Numero di movimenti che devono essere effettuati per far uscire
        // un led dal tirante

        Color c = new Color(0,0,0);
        int mSec=100;
        while(!isInterrupted()){
            try {
                Thread.sleep(mSec);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Ci sono elementi nella ragnatela su cui effettuare shift
            if(shiftCounter>0) {
                ShiftDownWire1();
                shiftCounter--;
                synchronized (pixels_array){
                    if (!bQueWire.isEmpty()) {   //Se la coda è piena prelevo ulteriore token
                        //La prima posizione è libera?
                        try {
                            if (((JSONObject) pixels_array.get(firstLxIndex)).getInt("r") == 0 &&
                                    ((JSONObject) pixels_array.get(firstLxIndex)).getInt("g") == 0 &&
                                    ((JSONObject) pixels_array.get(firstLxIndex)).getInt("b") == 0) {
                                shiftCounter = wireLength; //Resetto contatore di shift
                                c = bQueWire.take();
                                //Inserisco nuovo token
                                ((JSONObject) pixels_array.get(firstLxIndex)).put("r", c.red);
                                ((JSONObject) pixels_array.get(firstLxIndex)).put("g", c.green);
                                ((JSONObject) pixels_array.get(firstLxIndex)).put("b", c.blue);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handleNetworkRequest(NetworkThread.SET_PIXELS, pixels_array, 0, 0);
                }
            }
            else{ //Non ci sono elementi nel tirante
                try {
                    c = bQueWire.take(); //attendo che vengano inseriti token nella ragnatela
                    shiftCounter=wireLength;
                    synchronized (pixels_array){
                        //Questo è un inserimento in ramo vuoto, non c'è bisogno di controllo
                        //Inserisco nuovo token
                        ((JSONObject) pixels_array.get(firstLxIndex)).put("r", c.red);
                        ((JSONObject) pixels_array.get(firstLxIndex)).put("g", c.green);
                        ((JSONObject) pixels_array.get(firstLxIndex)).put("b", c.blue);
                        handleNetworkRequest(NetworkThread.SET_PIXELS, pixels_array, 0 ,0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void Wire2Controller(){


        int shiftCounter = 0;   //Numero di movimenti che devono essere effettuati per far uscire
        // un led dal tirante

        Color c = new Color(0,0,0);
        int mSec=100;
        while(!isInterrupted()){
            try {
                Thread.sleep(mSec);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Ci sono elementi nella ragnatela su cui effettuare shift
            if(shiftCounter>0) {
                ShiftDownWire2();
                shiftCounter--;
                synchronized (pixels_array){
                    if (!bQueWire.isEmpty()) {   //Se la coda è piena prelevo ulteriore token
                        //La prima posizione è libera?
                        try {
                            if (((JSONObject) pixels_array.get(firstWireStartLed)).getInt("r") == 0 &&
                                    ((JSONObject) pixels_array.get(firstWireStartLed)).getInt("g") == 0 &&
                                    ((JSONObject) pixels_array.get(firstWireStartLed)).getInt("b") == 0) {
                                shiftCounter = wireLength; //Resetto contatore di shift
                                c = bQueWire.take();
                                //Inserisco nuovo token
                                ((JSONObject) pixels_array.get(firstWireStartLed)).put("r", c.red);
                                ((JSONObject) pixels_array.get(firstWireStartLed)).put("g", c.green);
                                ((JSONObject) pixels_array.get(firstWireStartLed)).put("b", c.blue);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handleNetworkRequest(NetworkThread.SET_PIXELS, pixels_array, 0, 0);
                }
            }
            else{ //Non ci sono elementi nel tirante
                try {
                    c = bQueWire.take(); //attendo che vengano inseriti token nella ragnatela
                    shiftCounter=wireLength;
                    synchronized (pixels_array){
                        //Questo è un inserimento in ramo vuoto, non c'è bisogno di controllo
                        //Inserisco nuovo token
                        ((JSONObject) pixels_array.get(firstWireStartLed)).put("r", c.red);
                        ((JSONObject) pixels_array.get(firstWireStartLed)).put("g", c.green);
                        ((JSONObject) pixels_array.get(firstWireStartLed)).put("b", c.blue);
                        handleNetworkRequest(NetworkThread.SET_PIXELS, pixels_array, 0 ,0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }



















    }

    private void Wire3Controller(){}

    private void Wire4Controller(){}

    private void Wire5Controller(){}

    //Shift del token nel ramo1
    public void ShiftDownWire1(){
        //Parti dal basso del tirante
        //se ultimo led è al massimo dimezzalo
        //se ultimo led è dimezzato spegnilo
        //dal penultimo al secondo
        //prendi il valore e spostalo in basso
        //se il primo led è al massimo, spostalo al secondo e dimezzalo
        //se dimezzato spegnilo
        int wireLength = 26;
        int firstLxIndex = 25;  //Primo indice del led in alto cavo di sx
        int lastLxIndex = 0;    //Ultimo indice del led in basso cavo di sx
        int firstRxIndex = 26;  //Primo indice del led in alto cavo di dx
        int lastRxIndex = 51;    //Ultimo indice del led in basso cavo di dx

        Color c= new Color(0,0,0);   //Colore da inserire nel tirante
        Color[] wire= new Color[wireLength];
        //Inizializzazione dell'array
        for(int i=0;i<wire.length;i++)
            wire[i]=new Color(0,0,0);

        //Richiesta del lock sulla struttura dati ragnatela
        synchronized (pixels_array){
            //Estrazione della porzione di ragnatela interessata dallo shift
            //estraggo solo il filo di sinistra dato che sono uguali
            for (int i=0;i<wireLength;i++){
                try{
                    wire[i].red=((JSONObject) pixels_array.get(firstLxIndex-i)).getInt("r");
                    wire[i].green=((JSONObject) pixels_array.get(firstLxIndex-i)).getInt("g");
                    wire[i].blue=((JSONObject) pixels_array.get(firstLxIndex-i)).getInt("b");
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        //A questo punto si può lavorare sulla copia dei dati rilasciando il lock e permettendo ad
        //altri thread di lavorare in parallelo sulla struttura dati

        //Shift del vettore verso il basso;
        //Modifica ultimo valore (quello a contatto con il display)
        //Se il valore della componente è maggiore di 120 lo dimezza altrimenti lo azzera
        //dato che sul simulatore non si vedono le componenti con intensità <80
        if(wire[wire.length-1].red>80)
            wire[wire.length-1].red/=2;
        else
            wire[wire.length-1].red=0;

        if(wire[wire.length-1].green>80)
            wire[wire.length-1].green/=2;
        else
            wire[wire.length-1].green=0;

        if(wire[wire.length-1].blue>80)
            wire[wire.length-1].blue/=2;
        else
            wire[wire.length-1].blue=0;

        //Modifica corpo vettore
        //Parto dalla coda ed arrivo alla testa ( ultimo indice verso il primo)
        for(int i=wire.length-2;i>0;i--){
            //Se almeno una componente è != 0 effettuo lo shift (verso la coda)
            if(wire[i].red!=0 || wire[i].green!=0 || wire[i].blue!=0){
                wire[i+1]=wire[i];
            }
        }

        //Modifica primo valore (quello più distante dal display)
        //se diverso da 0 sposto verso il basso ed affievolisco il valore
        if(wire[0].red!=0 || wire[0].green!=0 || wire[0].blue!=0){
            wire[1].red=wire[0].red;
            wire[1].green=wire[0].green;
            wire[1].blue=wire[0].blue;

            if(wire[0].red>120)
                wire[0].red/=2;
            else
                wire[0].red=0;

            if(wire[0].green>120)
                wire[0].green/=2;
            else
                wire[0].green=0;

            if(wire[0].blue>120)
                wire[0].blue/=2;
            else
                wire[0].blue=0;
        }

        //Terminato lo shift reinserisco i valori nella struttura dati principale
        //Per farlo richiedo il lock
        synchronized (pixels_array){
            //Inserimento della porzione di ragnatela interessata dallo shift
            //A questo punto posso inserire in entrambi i fili della tirante
            //dato che questi sono identici

            try{

                for (int i=0;i<wireLength;i++){
                    //Ramo Sx
                    ((JSONObject) pixels_array.get(firstLxIndex-i)).put("r",wire[i].red);
                    ((JSONObject) pixels_array.get(firstLxIndex-i)).put("g",wire[i].green);
                    ((JSONObject) pixels_array.get(firstLxIndex-i)).put("b",wire[i].blue);
                    //Ramo Dx
                    ((JSONObject) pixels_array.get(firstRxIndex+i)).put("r",wire[i].red);
                    ((JSONObject) pixels_array.get(firstRxIndex+i)).put("g",wire[i].green);
                    ((JSONObject) pixels_array.get(firstRxIndex+i)).put("b",wire[i].blue);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Shift del token nel ramo2
    public void ShiftDownWire2(){
        //Parti dal basso del tirante
        //se ultimo led è al massimo dimezzalo
        //se ultimo led è dimezzato spegnilo
        //dal penultimo al secondo
        //prendi il valore e spostalo in basso
        //se il primo led è al massimo, spostalo al secondo e dimezzalo
        //se dimezzato spegnilo


        Color c= new Color(0,0,0);   //Colore da inserire nel tirante
        Color[] wire= new Color[wireLength];
        //Inizializzazione dell'array
        for(int i=0;i<wire.length;i++)
            wire[i]=new Color(0,0,0);

        //Richiesta del lock sulla struttura dati ragnatela
        synchronized (pixels_array){
            //Estrazione della porzione di ragnatela interessata dallo shift
            //estraggo solo il filo di sinistra dato che sono uguali
            for (int i=0;i<wireLength;i++){
                try{
                    wire[i].red=((JSONObject) pixels_array.get(firstWireStartLed-i)).getInt("r");
                    wire[i].green=((JSONObject) pixels_array.get(firstWireStartLed-i)).getInt("g");
                    wire[i].blue=((JSONObject) pixels_array.get(firstWireStartLed-i)).getInt("b");
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        //A questo punto si può lavorare sulla copia dei dati rilasciando il lock e permettendo ad
        //altri thread di lavorare in parallelo sulla struttura dati

        //Shift del vettore verso il basso;
        //Modifica ultimo valore (quello a contatto con il display)
        //Se il valore della componente è maggiore di 120 lo dimezza altrimenti lo azzera
        //dato che sul simulatore non si vedono le componenti con intensità <80
        if(wire[wire.length-1].red>80)
            wire[wire.length-1].red/=2;
        else
            wire[wire.length-1].red=0;

        if(wire[wire.length-1].green>80)
            wire[wire.length-1].green/=2;
        else
            wire[wire.length-1].green=0;

        if(wire[wire.length-1].blue>80)
            wire[wire.length-1].blue/=2;
        else
            wire[wire.length-1].blue=0;

        //Modifica corpo vettore
        //Parto dalla coda ed arrivo alla testa ( ultimo indice verso il primo)
        for(int i=wire.length-2;i>0;i--){
            //Se almeno una componente è != 0 effettuo lo shift (verso la coda)
            if(wire[i].red!=0 || wire[i].green!=0 || wire[i].blue!=0){
                wire[i+1].red=wire[i].red;
                wire[i+1].green=wire[i].green;
                wire[i+1].blue=wire[i].blue;

                //Spengo iu pixel che sono troppo fiochi
                if(wire[i].red<80)
                    wire[i].red=0;

                if(wire[i].green<80)
                    wire[i].green=0;

                if(wire[i].blue<80)
                    wire[i].blue=0;
            }
        }

        //Modifica primo valore (quello più distante dal display)
        //se diverso da 0 sposto verso il basso ed affievolisco il valore
        if(wire[0].red!=0 || wire[0].green!=0 || wire[0].blue!=0){
            wire[1].red=wire[0].red;
            wire[1].green=wire[0].green;
            wire[1].blue=wire[0].blue;

            if(wire[0].red>120)
                wire[0].red/=2;
            else
                wire[0].red=0;

            if(wire[0].green>120)
                wire[0].green/=2;
            else
                wire[0].green=0;

            if(wire[0].blue>120)
                wire[0].blue/=2;
            else
                wire[0].blue=0;
        }

        //Terminato lo shift reinserisco i valori nella struttura dati principale
        //Per farlo richiedo il lock
        synchronized (pixels_array){
            //Inserimento della porzione di ragnatela interessata dallo shift
            //A questo punto posso inserire in entrambi i fili della tirante
            //dato che questi sono identici

            try{

                for (int i=0;i<wireLength;i++){
                    //Ramo Sx
                    ((JSONObject) pixels_array.get(firstWireStartLed-i)).put("r",wire[i].red);
                    ((JSONObject) pixels_array.get(firstWireStartLed-i)).put("g",wire[i].green);
                    ((JSONObject) pixels_array.get(firstWireStartLed-i)).put("b",wire[i].blue);
                    //Ramo Dx
                    ((JSONObject) pixels_array.get(secondWireStartLed+i)).put("r",wire[i].red);
                    ((JSONObject) pixels_array.get(secondWireStartLed+i)).put("g",wire[i].green);
                    ((JSONObject) pixels_array.get(secondWireStartLed+i)).put("b",wire[i].blue);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }






    }

    //Shift del token nel ramo3
    public void ShiftDownWire3(){}

    //Shift del token nel ramo4
    public void ShiftDownWire4(){}

    //Shift del token nel ramo5
    public void ShiftDownWire5(){}

    //Shift del token nel ramo1
    public void ShiftDownWire1_backup(){
        //Parti dal basso del tirante
        //se ultimo led è al massimo dimezzalo
        //se ultimo led è dimezzato spegnilo
        //dal penultimo al secondo
        //prendi il valore e spostalo in basso
        //se il primo led è al massimo, spostalo al secondo e dimezzalo
        //se dimezzato spegnilo
        int wireLength = 26;
        int firstLxIndex = 25;  //Primo indice del led in alto cavo di sx
        int lastLxIndex = 0;    //Ultimo indice del led in basso cavo di sx
        int firstRxIndex = 26;  //Primo indice del led in alto cavo di dx
        int lastRxIndex = 51;    //Ultimo indice del led in basso cavo di dx

        Color c= new Color(0,0,0);   //Colore da inserire nel tirante



        try {
            c.red=((JSONObject) pixels_array.get(lastLxIndex)).getInt("r");
            c.green=((JSONObject) pixels_array.get(lastLxIndex)).getInt("g");
            c.blue=((JSONObject) pixels_array.get(lastLxIndex)).getInt("b");
            if( (c.red!=0 || c.green!=0 || c.blue !=0)){
                ((JSONObject) pixels_array.get(lastLxIndex)).put("r", Math.round(c.red/2));
                ((JSONObject) pixels_array.get(lastLxIndex)).put("g", Math.round(c.blue/2));
                ((JSONObject) pixels_array.get(lastLxIndex)).put("b", Math.round(c.green/2));
            }

            for (int i=lastLxIndex+1;i<firstLxIndex;i++){
                c.red=((JSONObject) pixels_array.get(i)).getInt("r");
                c.green=((JSONObject) pixels_array.get(i)).getInt("g");
                c.blue=((JSONObject) pixels_array.get(i)).getInt("b");

                //Se il led è diverso da spento
                if( (c.red!=0 || c.green!=0 || c.blue !=0) ){
                    ((JSONObject) pixels_array.get(i-1)).put("r", c.red);
                    ((JSONObject) pixels_array.get(i-1)).put("g", c.green);
                    ((JSONObject) pixels_array.get(i-1)).put("b", c.blue);
                }
            }

            //Modifica del primo valore
            c.red=((JSONObject) pixels_array.get(firstLxIndex)).getInt("r");
            c.green=((JSONObject) pixels_array.get(firstLxIndex)).getInt("g");
            c.blue=((JSONObject) pixels_array.get(firstLxIndex)).getInt("b");
            if( (c.red!=0 || c.green!=0 || c.blue !=0) ){
                //Sposto in basso
                ((JSONObject) pixels_array.get(firstLxIndex-1)).put("r", c.red);
                ((JSONObject) pixels_array.get(firstLxIndex-1)).put("g", c.green);
                ((JSONObject) pixels_array.get(firstLxIndex-1)).put("b", c.blue);
                //Affievolisco
                ((JSONObject) pixels_array.get(firstLxIndex)).put("r", Math.round(c.red/2));
                ((JSONObject) pixels_array.get(firstLxIndex)).put("g", Math.round(c.green/2));
                ((JSONObject) pixels_array.get(firstLxIndex)).put("b", Math.round(c.blue/2));



            }

            /*
            //Aggiornamento del lato destro del tirante 1
            if( ((JSONObject) pixels_array.get(lastRxIndex)).getInt("r")==fullColor &&
                    ((JSONObject) pixels_array.get(lastRxIndex)).getInt("g")==fullColor &&
                    ((JSONObject) pixels_array.get(lastRxIndex)).getInt("b")==fullColor ){

                ((JSONObject) pixels_array.get(lastRxIndex)).put("r", halfColor);
                ((JSONObject) pixels_array.get(lastRxIndex)).put("g", halfColor);
                ((JSONObject) pixels_array.get(lastRxIndex)).put("b", halfColor);

            }else if(((JSONObject) pixels_array.get(lastRxIndex)).getInt("r")==halfColor &&
                    ((JSONObject) pixels_array.get(lastRxIndex)).getInt("g")==halfColor &&
                    ((JSONObject) pixels_array.get(lastRxIndex)).getInt("b")==halfColor) {

                ((JSONObject) pixels_array.get(lastRxIndex)).put("r", 0);
                ((JSONObject) pixels_array.get(lastRxIndex)).put("g", 0);
                ((JSONObject) pixels_array.get(lastRxIndex)).put("b", 0);
            }

            for (int i=lastRxIndex-1;i>firstRxIndex;i--){
                //Se il led è diverso da spento
                if( ((JSONObject) pixels_array.get(i)).getInt("r")!=0 &&
                        ((JSONObject) pixels_array.get(i)).getInt("g")!=0 &&
                        ((JSONObject) pixels_array.get(i)).getInt("b")!=0 ){

                    int c;
                    c=((JSONObject) pixels_array.get(i)).getInt("r");
                    ((JSONObject) pixels_array.get(i-1)).put("r", c);
                    c=((JSONObject) pixels_array.get(i)).getInt("g");
                    ((JSONObject) pixels_array.get(i-1)).put("g", c);
                    c=((JSONObject) pixels_array.get(i)).getInt("b");
                    ((JSONObject) pixels_array.get(i-1)).put("b", c);
                }
            }

            //Modifica del primo valore
            if( ((JSONObject) pixels_array.get(firstRxIndex)).getInt("r")==fullColor &&
                    ((JSONObject) pixels_array.get(firstRxIndex)).getInt("g")==fullColor &&
                    ((JSONObject) pixels_array.get(firstRxIndex)).getInt("b")==fullColor ){

                ((JSONObject) pixels_array.get(firstRxIndex)).put("r", halfColor);
                ((JSONObject) pixels_array.get(firstRxIndex)).put("g", halfColor);
                ((JSONObject) pixels_array.get(firstRxIndex)).put("b", halfColor);

            }else if(((JSONObject) pixels_array.get(firstRxIndex)).getInt("r")==halfColor &&
                    ((JSONObject) pixels_array.get(firstRxIndex)).getInt("g")==halfColor &&
                    ((JSONObject) pixels_array.get(firstRxIndex)).getInt("b")==halfColor) {

                ((JSONObject) pixels_array.get(firstRxIndex)).put("r", 0);
                ((JSONObject) pixels_array.get(firstRxIndex)).put("g", 0);
                ((JSONObject) pixels_array.get(firstRxIndex)).put("b", 0);
            }
            */
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }







}
