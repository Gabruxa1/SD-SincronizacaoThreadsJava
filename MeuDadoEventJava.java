import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class MeuDadoEvent {
    private int Dado;
    private boolean Pronto;

    public MeuDadoEvent() {
        Pronto = false;
    }

    public synchronized void armazenar(int Data) {
        while (Pronto) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.Dado = Data;
        Pronto = true;
        notify();
    }

    public synchronized int carregar() {
        while (!Pronto) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Pronto = false;
        notify();
        return this.Dado;
    }
}

class ProdutorEvent implements Runnable {
    MeuDadoEvent dado;
    BufferedWriter logWriter;

    public ProdutorEvent(MeuDadoEvent dado, BufferedWriter logWriter) {
        this.dado = dado;
        this.logWriter = logWriter;
    }

    public void run() {
        for (int i = 0; i < 30; i++) {
            dado.armazenar(i);
            String message = "Produtor usando Eventos: " + i;
            System.out.println(message);
            try {
                logWriter.write(message);
                logWriter.newLine();
                logWriter.flush();
                Thread.sleep((int) (Math.random() * 500));
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class ConsumidorEvent implements Runnable {
    MeuDadoEvent dado;
    BufferedWriter logWriter;

    public ConsumidorEvent(MeuDadoEvent dado, BufferedWriter logWriter) {
        this.dado = dado;
        this.logWriter = logWriter;
    }

    public void run() {
        for (int i = 0; i < 30; i++) {
            int value = dado.carregar();
            String message = "Consumidor usando Eventos: " + value;
            System.out.println(message);
            try {
                logWriter.write(message);
                logWriter.newLine();
                logWriter.flush();
                Thread.sleep((int) (Math.random() * 500));
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class MeuDadoEventJava {
    public static void main(String argv[]) {
        MeuDadoEvent dado = new MeuDadoEvent();

        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("log3.txt"))) {
            Thread produtor = new Thread(new ProdutorEvent(dado, logWriter));
            Thread consumidor = new Thread(new ConsumidorEvent(dado, logWriter));
            produtor.start();
            consumidor.start();

            produtor.join();
            consumidor.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
