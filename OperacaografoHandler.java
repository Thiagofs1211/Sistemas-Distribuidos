package operacaografo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.apache.thrift.TException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import operacaografo.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.Semaphore;

public class OperacaografoHandler implements OperacaoGrafo.Iface {
    private List<Vertice> v = new ArrayList();
    private List<Aresta> a = new ArrayList();
    private Grafo grafo = new Grafo(v,a);
    private final Semaphore semaphore = new Semaphore(1);
    
    @Override
    public Grafo CriarGrafo() throws org.apache.thrift.TException{
        int i,j;
        List<Vertice> v = new ArrayList();
        List<Aresta> a = new ArrayList();
        File arq_vertice = new File("Vertice.txt");
        File arq_aresta = new File("Aresta.txt");
        if(!arq_vertice.exists()){
                  try{
                    new File("Vertice.txt").createNewFile();
                    new File("Aresta.txt").createNewFile();
                   }
                  catch (IOException ex){
                      System.out.println("Erro ao criar arquivo de vertices ou Arestas");
                      ex.getMessage();
                  }
        }
        else{
                 if(!arq_aresta.exists()){
                    try {
                        new File("Aresta.txt").createNewFile();
                    }catch (IOException ex){
                        System.out.println("Erro ao criar arquivo de Arestas");
                        ex.getMessage();
                    }
                 }
                 else{
                    try{
                        FileReader arq_ares = new FileReader("Aresta.txt");
                        BufferedReader lerarq = new BufferedReader(arq_ares);
                        String linha = lerarq.readLine();
                        String dados[] = linha.split(" ");
                            while (linha!=null){
                                Aresta aresta = new Aresta(Long.parseLong(dados[0]),Long.parseLong(dados[1]),Long.parseLong(dados[2]),Double.parseDouble(dados[3]),Boolean.getBoolean(dados[4]),dados[5]);
                                a.add(aresta);
                                linha = lerarq.readLine();
                                if(linha!=null){
                                    dados = linha.split(" ");
                                }
                            }
                        lerarq.close();
                        arq_ares.close();
                    }catch (FileNotFoundException ex) {
                        System.out.println("Erro ao abrir arquivo de Arestas");
                        ex.getMessage();
                    }catch (IOException ex) {
                        //Logger.getLogger(OperacaografoHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 }
                try{
                    FileReader arq_vert = new FileReader("Vertice.txt");
                    BufferedReader lerarq = new BufferedReader(arq_vert);
                    String linha = lerarq.readLine();
                    String dadosv[] = linha.split(" "); 
                    while (linha!=null){
                        Vertice vertice = new Vertice(Long.parseLong(dadosv[0]),Long.parseLong(dadosv[1]),dadosv[2],Double.parseDouble(dadosv[3]));
                        v.add(vertice);
                        linha = lerarq.readLine();
                        if(linha!=null){
                            dadosv = linha.split(" ");
                        }
                    }
                    lerarq.close();
                    arq_vert.close();
                }
                catch (FileNotFoundException ex){
                    System.out.println("Erro ao abrir arquivo de Vertices");
                    ex.getMessage();
                }catch (IOException ex){
                //Logger.getLogger(OperacaografoHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        grafo.V = v;
        grafo.A = a;
        return grafo;
    }
    
    public int VerificaVertice(long nome, Grafo grafo){
        int x;
        for(Vertice v1: grafo.V){
            if(v1.getNome() == nome)
                return 1;
        }
        return 0;
    }
    
    public int VerificaAresta(long id, Grafo grafo){
        int x;
        for(Aresta a1: grafo.A){
            if(a1.getId() == id)
                return 1;
        }
        return 0;
    }
    
    @Override
    public String criarVertice(long nome, long cor, String descricao, double peso) throws ExisteVertice, org.apache.thrift.TException{
        try {
            semaphore.acquire();
            System.out.println("a");
            Scanner x = new Scanner(System.in);
            int pause = x.nextInt();
            if(VerificaVertice(nome,grafo) == 0){
                Vertice v2 = new Vertice(nome, cor, descricao, peso);
                grafo.V.add(v2);
                try {
                    FileWriter arq = new FileWriter("Vertice.txt", true);
                    BufferedWriter arq_grava = new BufferedWriter(arq);

                    arq_grava.write(nome+" "+cor+" "+descricao+" "+peso);
                    arq_grava.newLine();

                    arq_grava.close();
                    arq.close();
                    System.out.println("a");
                    semaphore.release();
                    return "Vertice adicionado com sucesso\n";
                
            }catch (IOException ex) {
                semaphore.release();
                System.out.println("Erro ao abrir arquivo de Vertice");
            }
        }
        semaphore.release();
        return "Vertice já existente\n";
         }
        catch (InterruptedException ex) {
            //System.out.println("vixi");
            return "processo em uso\n";
        }
    }
    
    @Override
    public String criarAresta(long id, long v1, long v2, double peso, boolean direcionado, String descricao) throws ExisteAresta, NaoExisteVertice, org.apache.thrift.TException{
        try {
            semaphore.acquire();
            if(VerificaAresta(id, grafo) == 0){
                if(VerificaVertice(v1, grafo) == 1 && VerificaVertice(v2,grafo) == 1){
                    Aresta a1 = new Aresta(id,v1,v2,peso,direcionado,descricao);
                    grafo.A.add(a1);
                    try {
                        FileWriter arq = new FileWriter("Aresta.txt",true);
                        BufferedWriter arq_grava = new BufferedWriter(arq);

                        arq_grava.write(id+" "+v1+" "+v2+" "+peso+" "+direcionado+" "+descricao);
                        arq_grava.newLine();

                        arq_grava.close();
                        arq.close();
                        semaphore.release();
                        return "Aresta adicionada com sucesso\n";

                    }catch (IOException ex) {
                        semaphore.release();
                        System.out.println("Erro ao abrir arquivo de Aresta");
                    }
                }
                else{
                    semaphore.release();
                    return "Vertice(s) não existente(s)\n";
                }
            }
            semaphore.release();
            return "Aresta já existente\n";
        }catch (InterruptedException ex) {
            //System.out.println("vixi");
            return "processo em uso\n";
        }
    }
    
    @Override
    public String removeVertice(long nome) throws NaoExisteVertice, org.apache.thrift.TException{
        try {
            semaphore.acquire();
            if(VerificaVertice(nome,grafo) == 1){
                for(int i = 0;i < grafo.V.size();i++){
                    Vertice v1 = grafo.V.get(i);
                        if(v1.nome == nome){
                            grafo.V.remove(v1);
                        }
                }
                File arq = new File("Vertice.txt");
                try {
                    FileReader fr = new FileReader(arq);
                    BufferedReader br = new BufferedReader(fr);

                    ArrayList<String> save = new ArrayList();
                    String linha = br.readLine();
                    String dados[] = linha.split(" ");

                    while(linha!=null){
                        if(!dados[0].equals(String.valueOf(nome))){
                            save.add(linha);
                        }
                        linha = br.readLine();
                        if(linha!=null){
                            dados = linha.split(" ");
                        }
                    }

                    br.close();
                    fr.close();

                    FileWriter fw2 = new FileWriter(arq);
                    BufferedWriter bw = new BufferedWriter(fw2);

                    for(int i=0;i<save.size();i++){
                        bw.write(save.get(i));
                        bw.newLine();
                    }
                    bw.close();
                    fw2.close();

                } catch (FileNotFoundException ex) {
                    semaphore.release();
                    System.out.println("falha ao abrir o arquivo de Vertice");
                } catch (IOException ex) {}

                for(int i = 0; i < grafo.A.size();i++){
                    Aresta a1 = grafo.A.get(i);
                    if(a1.v1 == nome || a1.v2 == nome){
                        String s = removeAresta(a1.id);
                        System.out.println(s);
                    }
                }
                semaphore.release();
                return "Vertice removido com sucesso\n";
            }
            semaphore.release();
            return "Vertice inexistente\n";
        }catch (InterruptedException ex) {
            //System.out.println("vixi");
            return "processo em uso\n";
        }
    }
    
    @Override
    public String removeAresta(long id) throws NaoExisteAresta, org.apache.thrift.TException{
        try {
            semaphore.acquire();
            if(VerificaAresta(id, grafo) == 1){
                for(int i=0;i<grafo.getASize();i++){
                    Aresta a1 = grafo.A.get(i);
                    if(a1.id == id){
                        grafo.A.remove(a1);
                    }
                }
                File arq = new File("Aresta.txt");
                try {
                    FileReader fr = new FileReader(arq);
                    BufferedReader br = new BufferedReader(fr);

                    ArrayList<String> save = new ArrayList();
                    String linha = br.readLine();
                    String dados[] = linha.split(" ");

                    while(linha!=null){
                        if(!dados[0].equals(String.valueOf(id))){
                            save.add(linha);
                        }
                        linha = br.readLine();
                        if(linha!=null){
                            dados= linha.split(" ");
                        }
                    }

                    br.close();
                    fr.close();

                    FileWriter fw2 = new FileWriter(arq);
                    BufferedWriter bw = new BufferedWriter(fw2);

                    for(int i=0;i<save.size();i++){
                        bw.write(save.get(i));
                        bw.newLine();
                    }
                    bw.close();
                    fw2.close();

                } catch (FileNotFoundException ex) {
                    System.out.println("falha ao abrir o arquivo de Aresta");
                } catch (IOException ex) {}
                semaphore.release();

                return "Aresta removida com sucesso";
            }
            semaphore.release();
            return "Aresta inexistente\n";
        }catch (InterruptedException ex) {
            //System.out.println("vixi");
            return "processo em uso\n";
        }
    }

    @Override
    public String modificarVertice(long nome, long cor, String descricao, double peso) throws NaoExisteVertice, org.apache.thrift.TException{
        try {
            semaphore.acquire();
            if(VerificaVertice(nome,grafo) == 1){
                for(int i = 0;i < grafo.getVSize();i++){
                    Vertice v1 = grafo.V.get(i);
                    if(v1.nome == nome){
                        v1.cor = cor;
                        v1.descricao = descricao;
                        v1.peso = peso;
                        grafo.V.set(i, v1);
                        File arq = new File("Vertice.txt");
                        try {
                            FileReader fr = new FileReader(arq);
                            BufferedReader br = new BufferedReader(fr);

                            ArrayList<String> save = new ArrayList();
                            String linha = br.readLine();
                            String dados[] = linha.split(" ");

                            while(linha!=null){
                                if(!dados[0].equals(nome)){
                                    save.add(linha);
                                    linha = br.readLine();
                                    if(linha!=null){
                                        dados = linha.split(" ");
                                    }
                                }
                            }

                            br.close();
                            fr.close();

                            FileWriter fw2 = new FileWriter(arq);
                            BufferedWriter bw = new BufferedWriter(fw2);

                            for(int j=0;j<save.size();j++){
                                if(j == i){
                                    bw.write(nome+" "+cor+" "+descricao+" "+peso);
                                    bw.newLine();
                                }
                                else{
                                    bw.write(save.get(j));
                                    bw.newLine();
                                }
                            }
                            bw.close();
                            fw2.close();

                        } catch (FileNotFoundException ex) {
                            semaphore.release();
                            System.out.println("falha ao abrir o arquivo de Vertice");
                        } catch (IOException ex) {}
                        semaphore.release();
                        return "Vertice Modificado com sucesso\n";
                    }
                }
            }
            semaphore.release();
            return "Vertice inexistente\n";
        }catch (InterruptedException ex) {
            //System.out.println("vixi");
            return "processo em uso\n";
        }
    }

    @Override
    public String modificaAresta(long id, double peso, boolean direcionado, String descricao)throws NaoExisteAresta, org.apache.thrift.TException{
        try {
            semaphore.acquire();
            if(VerificaAresta(id,grafo) == 1){
                for(int i = 0;i < grafo.getASize();i++){
                    Aresta a1 = grafo.A.get(i);
                    if(a1.id == id){
                        long v1 = a1.v1;
                        long v2 = a1.v2;
                        a1.peso = peso;
                        a1.direcionado = direcionado;
                        a1.descricao = descricao;
                        grafo.A.set(i, a1);
                        File arq = new File("Aresta.txt");
                        try {
                            FileReader fr = new FileReader(arq);
                            BufferedReader br = new BufferedReader(fr);

                            ArrayList<String> save = new ArrayList();
                            String linha = br.readLine();
                            String dados[] = linha.split(" ");

                            while(linha!=null){
                                if(!dados[0].equals(id)){
                                    save.add(linha);
                                    linha = br.readLine();
                                    if(linha!=null){
                                        dados = linha.split(" ");
                                    }
                                }
                            }

                            br.close();
                            fr.close();

                            FileWriter fw2 = new FileWriter(arq);
                            BufferedWriter bw = new BufferedWriter(fw2);

                            for(int j=0;j<save.size();j++){
                                if(j == i){
                                    bw.write(id+" "+v1+" "+v2+" "+peso+" "+direcionado+" "+descricao);
                                    bw.newLine();
                                }
                                else{
                                    bw.write(save.get(j));
                                    bw.newLine();
                                }
                            }
                            bw.close();
                            fw2.close();

                        } catch (FileNotFoundException ex) {
                            semaphore.release();
                            System.out.println("falha ao abrir o arquivo de Aresta");
                        } catch (IOException ex) {}
                        semaphore.release();
                        return "Aresta Modificada com sucesso\n";
                    }
                }
            }
            semaphore.release();
            return "Aresta Inexistente\n";
        }catch (InterruptedException ex) {
            //System.out.println("vixi");
            return "processo em uso\n";
        }
    }

    @Override
    public String lerGrafo() throws org.apache.thrift.TException{
        if(grafo !=null){
            String result = "Vertices:\n\n";
            for(int i = 0; i < grafo.getVSize();i++){
                result = result+("Nome: "+grafo.V.get(i).nome+"\nCor: "+grafo.V.get(i).cor+"\ndescrição: "+grafo.V.get(i).descricao+"\nPeso: "+grafo.V.get(i).peso+"\n");
            }

            result = result+("\nArestas:\n");
            for(int i = 0; i < grafo.getASize(); i++){
                result=result+("ID: "+grafo.A.get(i).id+"\nVertices: "+grafo.A.get(i).v1+" "+grafo.A.get(i).v2+"\nPeso: "+grafo.A.get(i).peso+"\nDirecionado: "+grafo.A.get(i).direcionado+"\nDescricao: "+grafo.A.get(i).descricao+"\n");
                }
            return result;
        }
        return "Grafo Inexistente\n";
    }

    @Override
    public String lerVertice(long nome) throws NaoExisteVertice, org.apache.thrift.TException{
        if(VerificaVertice(nome,grafo) == 1){
            for(int i=0; i<grafo.getVSize();i++){
                if(grafo.V.get(i).nome == nome){
                    return ("Nome: "+grafo.V.get(i).nome+"\nCor: "+grafo.V.get(i).cor+"\ndescrição: "+grafo.V.get(i).descricao+"\nPeso: "+grafo.V.get(i).peso);
                }
            }
        }
        return "Vertice Inexistente\n";
    }

    @Override
    public String lerAresta(long id) throws NaoExisteAresta, org.apache.thrift.TException{
        if(VerificaAresta(id,grafo)== 1){
            for(int i = 0; i < grafo.getASize(); i++){
                if(grafo.A.get(i).id == id){
                    return ("ID: "+grafo.A.get(i).id+"\nVertices: "+grafo.A.get(i).v1+" "+grafo.A.get(i).v2+"\nPeso: "+grafo.A.get(i).peso+"\nDirecionado: "+grafo.A.get(i).direcionado+"\nDescricao: "+grafo.A.get(i).descricao);
                }
            }
        }
        return "Aresta Inexistente\n";
    }

    @Override
    public String listarVerticeDeAresta(long id) throws NaoExisteAresta, org.apache.thrift.TException{
        if(VerificaAresta(id, grafo) == 1){
            String result = "Vertices:\n\n";
            for(int i = 0;i < grafo.getASize();i++){
                if(grafo.A.get(i).id == id){
                    result = result + (lerVertice(grafo.A.get(i).v1)+"\n"+lerVertice(grafo.A.get(i).v2)+"\n");
                    return result;
                }
            }
        }
        return "Aresta Inexistente";
    }

    @Override
    public String listarArestaDeVertice(long nome) throws NaoExisteVertice, org.apache.thrift.TException{
        if(VerificaVertice(nome,grafo)==1){
            String result = "Aresta:\n\n";
            for(int i=0;i<grafo.getASize();i++){
                if(grafo.A.get(i).v1 == nome || grafo.A.get(i).v2 == nome){
                    result = result + (lerAresta(grafo.A.get(i).id)+"\n");
                }
            }
            return result;
        }
        return "Vertice Inexistente\n";
    }

    @Override
    public String listarVerticesVizinhos(long nome) throws NaoExisteVertice, org.apache.thrift.TException{
        if(VerificaVertice(nome,grafo) == 1){
            String result = "Vizinho:\n\n";
            for(int i = 0;i < grafo.getASize();i++){
                if(grafo.A.get(i).v1 == nome){
                    result = result+(lerVertice(grafo.A.get(i).v1)+"\n");
                }
                if(grafo.A.get(i).v2 == nome){
                    result = result + (lerVertice(grafo.A.get(i).v2)+"\n");
                }
            }
            return result;
        }
        return "Vertice Inexiste\n";
    }
}

