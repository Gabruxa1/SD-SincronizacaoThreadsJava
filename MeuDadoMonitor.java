import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class MeuDadoMonitor {
    private int Dado;
    private boolean Pronto;
    private boolean Ocupado;

    public MeuDadoMonitor() {
        Pronto = false;
        Ocupado = true;
    }

    public synchronized void armazenar(int Dado) {
        while (!Ocupado) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.Dado = Dado;
        Ocupado = false;
        Pronto = true;
        notifyAll();
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
        Ocupado = true;
        notifyAll();
        return this.Dado;
    }
}

class ProdutorMonitor implements Runnable {
    MeuDadoMonitor dado;
    BufferedWriter logWriter;

    public ProdutorMonitor(MeuDadoMonitor dado, BufferedWriter logWriter) {
        this.dado = dado;
        this.logWriter = logWriter;
    }

    public void run() {
        for (int i = 0; i < 30; i++) {
            dado.armazenar(i);
            String message = "Produtor usando Monitor: " + i;
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

class ConsumidorMonitor implements Runnable {
    MeuDadoMonitor dado;
    BufferedWriter logWriter;

    public ConsumidorMonitor(MeuDadoMonitor dado, BufferedWriter logWriter) {
        this.dado = dado;
        this.logWriter = logWriter;
    }

    public void run() {
        for (int i = 0; i < 30; i++) {
            int value = dado.carregar();
            String message = "Consumidor usando Monitor: " + value;
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

class MeuDadoMonitorJava {
    public static void main(String[] argv) {
        MeuDadoMonitor dado = new MeuDadoMonitor();

        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("log2.txt"))) { // Nome do arquivo alterado para log2.txt
            Thread produtor = new Thread(new ProdutorMonitor(dado, logWriter));
            Thread consumidor = new Thread(new ConsumidorMonitor(dado, logWriter));
            produtor.start();
            consumidor.start();

            produtor.join();
            consumidor.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
