import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class MeuDadoThreads {
    private int Dado;
    
    public void armazenar(int Dado) {
        this.Dado = Dado;
    }

    public int carregar() {
        return this.Dado;
    }
}

class ProdutorThreads implements Runnable {
    MeuDadoThreads dado;
    PrintWriter writer;

    public ProdutorThreads(MeuDadoThreads dado, PrintWriter writer) {
        this.dado = dado;
        this.writer = writer;
    }

    public void run() {
        int i;
        for (i = 0; i < 30; i++) {
            dado.armazenar(i);
            writer.println("Produtor: " + i);
            try {
                Thread.sleep((int)(Math.random() * 500));
            } catch (InterruptedException e) {}
        }
    }
}

class ConsumidorThreads implements Runnable {
    MeuDadoThreads dado;
    PrintWriter writer;

    public ConsumidorThreads(MeuDadoThreads dado, PrintWriter writer) {
        this.dado = dado;
        this.writer = writer;
    }

    public void run() {
        for (int i = 0; i < 30; i++) {
            writer.println("Consumidor: " + dado.carregar());  // Grava no log
            try {
                Thread.sleep((int)(Math.random() * 500));
            } catch (InterruptedException e) {}
        }
    }
}

class MeuDadoThreadsJava {
    public static void main(String argv[]) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("log.txt", true));

            MeuDadoThreads dado = new MeuDadoThreads();
            new Thread(new ProdutorThreads(dado, writer)).start();
            new Thread(new ConsumidorThreads(dado, writer)).start();

            Thread.sleep(5000);

            writer.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
