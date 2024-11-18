
# SD-SincronizacaoThreadsJava

  

# Atividade 1

  

### Comparação das Execuções

  

1.  **Ordem de Produção e Consumo**:

- Em ambos os logs, o **produtor** imprime valores de `0` a `23` e o **consumidor** consome os valores produzidos. No entanto, a ordem de produção e consumo **não é a mesma** entre as duas execuções.

- A execução das threads não é determinística, ou seja, a ordem das mensagens geradas pelas threads (produtor e consumidor) pode variar a cada execução, pois o escalonador de threads pode decidir rodar o **produtor** ou o **consumidor** em ordens diferentes a cada vez.

2.  **Diferenças Notáveis**:

-  **Na primeira execução**, o consumidor consome múltiplos valores do mesmo número consecutivamente (por exemplo, `Consumidor: 5` aparece várias vezes antes de consumir `6`).

-  **Na segunda execução**, o comportamento também é intercalado, mas a sequência de **"Produtor"** e **"Consumidor"** é diferente.

3.  **Consumo e Produção Intercalados**:

- Na primeira execução, vemos que o consumidor consome valores de forma mais "concentrada" (por exemplo, múltiplas instâncias de `Consumidor: 0`).

- Na segunda execução, o comportamento também é intercalado, mas o valor consumido logo após a produção de um novo número pode ser diferente, como no caso de `Consumidor: 9` aparecer mais de uma vez.

4.  **Relação entre Produtor e Consumidor**:

- Em ambos os logs, há pontos em que o consumidor está consumindo valores que o produtor ainda não produziu (devido ao comportamento concorrente das threads).

- Esse comportamento é **esperado**, pois as threads estão rodando de forma independente e não sincronizada.

  
  

### Conclusão

  

Sim, o **resultado foi diferente** entre as duas execuções. Isso é esperado devido à natureza **concorrente** das threads, onde a execução das threads não é garantida em uma ordem específica. O **produtor** e o **consumidor** podem ser executados de forma intercalada de maneiras diferentes a cada execução.

  

#### Por que isso acontece?

  

- O programa envolve **concorrência** entre as threads de **produtor** e **consumidor**, e a execução das threads é **não determinística**. O escalonador de threads do sistema operacional pode decidir rodar o produtor ou o consumidor em diferentes ordens a cada execução.

- Além disso, o uso do `Thread.sleep` com um valor **randômico** pode alterar o momento em que o produtor ou o consumidor executa suas tarefas, influenciando a ordem das mensagens nos logs.

  

Esse comportamento é característico de programas multithreaded onde não há sincronização explícita entre as threads. Se você quiser garantir uma ordem de execução mais controlada, seria necessário implementar algum tipo de **sincronização**, como o uso de `synchronized`, `wait()`, `notify()`, ou estruturas de dados como `BlockingQueue`.

  
  

# Atividade 2
#### **Semelhanças**

1.  **Correspondência entre os valores do Produtor e Consumidor:**
    
    -   Em todas as execuções, cada valor produzido pelo **Produtor** foi consumido pelo **Consumidor**, mantendo a sincronização entre os dois.
    -   Nenhum dado foi perdido ou consumido antes de ser produzido.
2.  **Ordem Geral dos Dados:**
    
    -   A sequência dos valores segue um padrão incremental, de `0` a `29` para o **Produtor** e para o **Consumidor**.
    -   O Consumidor consome sempre os dados na mesma ordem em que foram produzidos.
3.  **Operações intercaladas:**
    
    -   Em todas as execuções, há uma alternância de ações entre o **Produtor** e o **Consumidor**, refletindo o funcionamento correto da sincronização.

----------

#### **Diferenças**

1.  **Padrão de Intercalamento:**
    
    -   Na terceira execução (**`log2.txt`**), há mais situações onde o **Produtor** realiza múltiplas produções consecutivas (ex.: `Produtor: 3, Produtor: 4`), antes que o **Consumidor** consuma os dados (`Consumidor: 3, Consumidor: 4`).
    -   Nas execuções anteriores, o intercalamento entre produção e consumo é mais uniforme.
2.  **Ordens das Operações:**
    
    -   Em **`log2.txt`**, o **Consumidor** começa antes do **Produtor** (`Consumidor: 0` vem antes de `Produtor: 0` no arquivo), sugerindo um pequeno atraso inicial no Produtor. Nas execuções anteriores, o **Produtor** sempre inicia primeiro.
3.  **Diferentes Momentos de Bloqueio:**
    
    -   Na segunda execução anterior, há momentos onde o **Consumidor** parece estar mais "adiantado" que o **Produtor**
       
        
        Consumidor usando Monitor: 10.
        Consumidor usando Monitor: 11.
        Produtor usando Monitor: 11
        
        Aqui, o Consumidor está aguardando o Produtor atualizar o próximo dado, enquanto isso não ocorre com frequência em **`log2.txt`**.
4.  **Confiabilidade da Sincronização:**
    
    -   As três execuções indicam que o **monitor** está funcionando corretamente, mas as diferenças entre os logs sugerem variações no tempo de execução devido à aleatoriedade dos **`Thread.sleep()`**.

----------

#### **Análise Geral**

-   **Execução em `log2.txt`:**
    
    -   Menos uniformidade no intercalamento, com momentos onde o Produtor realiza múltiplas produções antes do Consumidor consumir.
    -   Apesar disso, o sistema mantém a sincronização, e nenhum dado é perdido ou consumido fora de ordem.
-   **Execuções anteriores:**
    
    -   Intercalamento mais uniforme entre Produtor e Consumidor.
    -   Algumas pequenas variações, mas sempre respeitando a ordem correta dos dados.

----------

#### **Conclusão**

A terceira execução mostra uma maior variação no comportamento devido ao uso de `Thread.sleep` com valores aleatórios. No entanto, o **monitor** garante a consistência e sincronização entre as threads. Todas as execuções demonstram o funcionamento correto do programa, mas a variação nos padrões de intercalamento evidencia como o tempo de execução afeta a interação entre as threads.

# Atividade 3
#### 1. **Execução da Atividade Prática 01 (`log.txt`)**

-   **Características principais:**
    -   Utilização de `synchronized` com flags `Ocupado` e `Pronto`.
    -   O comportamento mostra **possíveis situações de atraso entre produção e consumo**, mas há sincronização suficiente para evitar inconsistências.
    -   Em alguns casos, o produtor gera mais de um valor consecutivamente antes que o consumidor consuma.
-   **Observações notáveis:**
    -   Exemplo: Há sequências onde o produtor adiciona dois valores consecutivos (ex: 8 e 9) antes do consumidor processá-los.

----------

#### 2. **Execução da Atividade Prática 02 (`log2.txt`)**

-   **Características principais:**
    -   Uso aprimorado de `synchronized`, com notificações (`notifyAll`) para controlar o fluxo entre produtor e consumidor.
    -   Sincronização entre as threads é mais precisa, e os eventos de produção e consumo estão **mais alinhados**.
    -   Ocasionalmente há interrupções onde o consumidor processa sem esperar a produção (possivelmente devido a tempos de espera ou chamadas randômicas de `sleep`).
-   **Observações notáveis:**
    -   Em geral, o comportamento é mais consistente do que na Atividade Prática 01.
    -   Exemplo: O consumidor consome valores logo após a produção, mas pode ocorrer ligeiro desalinhamento (ex: produtor produz dois valores antes de serem consumidos).

----------

#### 3. **Execução da Atividade Prática 03 (`log3.txt`)**

-   **Características principais:**
    -   Implementação baseada em eventos (`wait` e `notify`), substituindo as verificações manuais de flags.
    -   **Produção e consumo estão mais coordenados**. A sequência é visivelmente **mais equilibrada**, com pouca ou nenhuma interrupção no fluxo.
    -   Há momentos em que o produtor ou consumidor "espera" devido ao controle rigoroso do estado do dado (com `wait`).
-   **Observações notáveis:**
    -   A sincronização é mais eficiente e evita inconsistências. Exemplo: O produtor e consumidor alternam mais regularmente, com apenas algumas variações ocasionais devido ao `sleep`.

----------

### Comparação Geral

| **Aspecto**                  | **Atividade 01 (`log.txt`)**                         | **Atividade 02 (`log2.txt`)**                         | **Atividade 03 (`log3.txt`)**                         |
|------------------------------|-----------------------------------------------------|-----------------------------------------------------|-----------------------------------------------------|
| **Sincronização**            | Básica, com `synchronized` e flags manuais.         | Mais avançada, usando `notifyAll`.                  | Baseada em eventos com `wait` e `notify`.           |
| **Fluxo produtor-consumidor**| Intermitente, algumas quebras na alternância.       | Melhor, com desalinhamentos ocasionais.             | Quase perfeito, fluxo bem regulado.                 |
| **Desempenho**               | Propenso a atrasos em consumo após múltiplas produções consecutivas. | Menores atrasos, mas ainda dependente de `notifyAll`. | Ótimo desempenho, controle rigoroso do estado.      |
| **Consistência**             | Pode haver valores sobrepostos (produção "excessiva"). | Produção e consumo mais próximos em frequência.     | Sincronização mantém valores sempre alinhados.      |


----------

### Conclusão

-   **Atividade Prática 01**: Implementação funcional, mas com sincronização básica e propensa a inconsistências entre produção e consumo.
-   **Atividade Prática 02**: Melhor sincronização, mas o uso de `notifyAll` pode introduzir sobrecarga ou desalinhamentos.
-   **Atividade Prática 03**: Uso de eventos (`wait` e `notify`) resulta em sincronização robusta, alternância quase perfeita entre produção e consumo, e melhor desempenho geral.

A **implementação da Atividade Prática 03** é a mais eficiente e equilibrada, destacando-se como a melhor solução entre as três.