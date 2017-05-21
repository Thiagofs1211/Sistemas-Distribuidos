package operacaografo;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import operacaografo.*;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TBinaryProtocol;

public class OperacaografoClient {
    public static void main(String [] args) throws IOException{
        menu();
        try{
            TTransport transport = new TSocket("localhost",9090);
            transport.open();
            
            TProtocol protocol = new TBinaryProtocol(transport);
            OperacaoGrafo.Client client = new OperacaoGrafo.Client(protocol);
        }
        catch(TException x){
            x.printStackTrace();
        }
    }
    
    public static void menu() throws IOException{
        int opcao = 0;
        do{
            System.out.println("==============================================");
            System.out.println("|0- sair                                     |");
            System.out.println("|1- Criar Grafo                              |");
            System.out.println("|2- Criar Vertice                            |");
            System.out.println("|3- Criar Aresta                             |");
            System.out.println("|4- Remover Vertice                          |");
            System.out.println("|5- Remover Aresta                           |");
            System.out.println("|6- Modificar Vertice                        |");
            System.out.println("|7- Modificar Aresta                         |");
            System.out.println("|8- Ler Grafo                                |");
            System.out.println("|9- Ler Vertice                              |");
            System.out.println("|10- Ler Aresta                              |");
            System.out.println("|11- Listar vértices de uma aresta           |");
            System.out.println("|12- Listar Arestas de um vértice            |");
            System.out.println("|13- Listar vértices vizinhos de um vértice  |");
            System.out.println("==============================================");
            System.out.println("\n\nDigite sua opção: ");
            
            Scanner s = new Scanner(System.in);
            opcao = s.nextInt();
            
            switch(opcao){
                case 1:
                    try(TTransport transport = new TSocket("localhost",9090)) {
                        transport.open();

                        TProtocol protocol = new TBinaryProtocol(transport);
                        OperacaoGrafo.Client client = new OperacaoGrafo.Client(protocol);
                        
                        client.CriarGrafo();
                    }
                    catch(TException x){
                        x.printStackTrace();
                    }

                    break;
                    
                case 2:
                    try{
                        TTransport transport = new TSocket("localhost",9090);
                        transport.open();

                        TProtocol protocol = new TBinaryProtocol(transport);
                        OperacaoGrafo.Client client = new OperacaoGrafo.Client(protocol);
                        
                        System.out.println("Nome do Vertice: ");
                        long nome = s.nextLong();
                        System.out.println("Cor do Vertice: ");
                        long cor = s.nextLong();
                        System.out.println("Descrição do Vertice: ");
                        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                        String descricao = in.readLine();
                        System.out.println("Peso do Vertice: ");
                        double peso = s.nextDouble();
                        String string = client.criarVertice(nome, cor, descricao, peso);
                        System.out.println(string);
                        transport.close();
                    }
                    catch(TException x){
                        x.printStackTrace();
                    }
                    
                    break;
                
                case 3:
                    try{
                        TTransport transport = new TSocket("localhost",9090);
                        transport.open();

                        TProtocol protocol = new TBinaryProtocol(transport);
                        OperacaoGrafo.Client client = new OperacaoGrafo.Client(protocol);
                        
                        System.out.println("ID da Aresta: ");
                        long id = s.nextLong();
                        System.out.println("Vertices da Aresta: ");
                        long v1 = s.nextLong();
                        long v2 = s.nextLong();
                        System.out.println("Peso da Aresta: ");
                        double pesoA = s.nextDouble();
                        System.out.println("Aresta direcionada? (true or false)");
                        boolean direcionado = s.nextBoolean();
                        System.out.println("Descricao da Aresta: ");
                        BufferedReader in2 = new BufferedReader(new InputStreamReader(System.in));
                        String descricaoA = in2.readLine();

                        String str = client.criarAresta(id, v1, v2, pesoA, direcionado, descricaoA);
                        System.out.println(str);
                        transport.close();
                    }
                    catch(TException x){
                        x.printStackTrace();
                    }
                    break;
                    
                case 4:
                    try{
                        TTransport transport = new TSocket("localhost",9090);
                        transport.open();

                        TProtocol protocol = new TBinaryProtocol(transport);
                        OperacaoGrafo.Client client = new OperacaoGrafo.Client(protocol);
                        
                        System.out.println("Digite o nome do Vértice a ser removido: ");
                        long nomeRV = s.nextLong();
                        String str2 = client.removeVertice(nomeRV);
                        System.out.println(str2);
                        transport.close();
                    }
                    catch(TException x){
                        x.printStackTrace();
                    }
                    break;
                
                case 5:
                    try{
                        TTransport transport = new TSocket("localhost",9090);
                        transport.open();

                        TProtocol protocol = new TBinaryProtocol(transport);
                        OperacaoGrafo.Client client = new OperacaoGrafo.Client(protocol);
                        
                        System.out.println("Digite o ID da aresta a ser removida: ");
                        long idRV = s.nextLong();
                        String str3 = client.removeAresta(idRV);
                        System.out.println(str3);
                        transport.close();
                    }
                    catch(TException x){
                        x.printStackTrace();
                    }
                    break;
                    
                case 6:
                    try{
                        TTransport transport = new TSocket("localhost",9090);
                        transport.open();

                        TProtocol protocol = new TBinaryProtocol(transport);
                        OperacaoGrafo.Client client = new OperacaoGrafo.Client(protocol);
                        
                        System.out.println("Digite o nome do vértice a ser modificado: ");
                        long nomeM = s.nextLong();
                        System.out.println("Digite a nova cor: ");
                        long corM = s.nextLong();
                        System.out.println("Digite a nova descricao: ");
                        BufferedReader in3 = new BufferedReader(new InputStreamReader(System.in));
                        String descricaoM = in3.readLine();;
                        System.out.println("Digite o novo peso: ");
                        Double pesoM = s.nextDouble();
                        String str4 = client.modificarVertice(nomeM, corM, descricaoM, pesoM);
                        System.out.println(str4);
                        transport.close();
                    }
                    catch(TException x){
                        x.printStackTrace();
                    }
                    break;
                    
                case 7:
                    try{
                        TTransport transport = new TSocket("localhost",9090);
                        transport.open();

                        TProtocol protocol = new TBinaryProtocol(transport);
                        OperacaoGrafo.Client client = new OperacaoGrafo.Client(protocol);
                        
                        System.out.println("Digite o ID da Aresta a ser modificada: ");
                        long IDM = s.nextLong();
                        System.out.println("Digite o novo peso: ");
                        double pesoMA = s.nextDouble();
                        System.out.println("Aresta Direcionada? (true or false)");
                        boolean direcionadoM = s.nextBoolean();
                        System.out.println("Digite a nova descricao: ");
                        BufferedReader in4 = new BufferedReader(new InputStreamReader(System.in));
                        String descricaoMA = in4.readLine();
                        String str5 = client.modificaAresta(IDM, pesoMA, direcionadoM, descricaoMA);
                        System.out.println(str5);
                        transport.close();
                    }
                    catch(TException x){
                        x.printStackTrace();
                    }
                    break;
                    
                case 8:
                    try{
                        TTransport transport = new TSocket("localhost",9090);
                        transport.open();

                        TProtocol protocol = new TBinaryProtocol(transport);
                        OperacaoGrafo.Client client = new OperacaoGrafo.Client(protocol);
                        
                        String str6 = client.lerGrafo();
                        System.out.println(str6);
                        transport.close();
                    }
                    catch(TException x){
                        x.printStackTrace();
                    }
                    break;
                    
                case 9:
                    try{
                        TTransport transport = new TSocket("localhost",9090);
                        transport.open();

                        TProtocol protocol = new TBinaryProtocol(transport);
                        OperacaoGrafo.Client client = new OperacaoGrafo.Client(protocol);
                        
                        System.out.println("Digite o nome do Vértice: ");
                        long nomeL = s.nextLong();
                        String str7 = client.lerVertice(nomeL);
                        System.out.println(str7);
                        transport.close();
                    }
                    catch(TException x){
                        x.printStackTrace();
                    }
                    break;
                    
                case 10:
                    try{
                        TTransport transport = new TSocket("localhost",9090);
                        transport.open();

                        TProtocol protocol = new TBinaryProtocol(transport);
                        OperacaoGrafo.Client client = new OperacaoGrafo.Client(protocol);
                        
                        System.out.println("Digite o ID da Aresta: ");
                        long IDL = s.nextLong();
                        String str8 = client.lerAresta(IDL);
                        System.out.println(str8);
                        transport.close();
                    }
                    catch(TException x){
                        x.printStackTrace();
                    }
                    break;
                    
                case 11:
                    try{
                        TTransport transport = new TSocket("localhost",9090);
                        transport.open();

                        TProtocol protocol = new TBinaryProtocol(transport);
                        OperacaoGrafo.Client client = new OperacaoGrafo.Client(protocol);
                        
                        System.out.println("Digite o ID da Aresta: ");
                        long IDV = s.nextLong();
                        String str9 = client.listarVerticeDeAresta(IDV);
                        System.out.println(str9);
                        transport.close();
                    }
                    catch(TException x){
                        x.printStackTrace();
                    }
                    break;
                    
                case 12:
                    try{
                        TTransport transport = new TSocket("localhost",9090);
                        transport.open();

                        TProtocol protocol = new TBinaryProtocol(transport);
                        OperacaoGrafo.Client client = new OperacaoGrafo.Client(protocol);
                        
                         System.out.println("Digite o nome do Vértice: ");
                        long nomeAre = s.nextLong();
                        String str10 = client.listarArestaDeVertice(nomeAre);
                        System.out.println(str10);
                        transport.close();
                    }
                    catch(TException x){
                        x.printStackTrace();
                    }
                    break;
                    
                case 13:
                    try{
                        TTransport transport = new TSocket("localhost",9090);
                        transport.open();

                        TProtocol protocol = new TBinaryProtocol(transport);
                        OperacaoGrafo.Client client = new OperacaoGrafo.Client(protocol);
                        
                        System.out.println("Digite o nome do Vértice: ");
                        long nomeVi = s.nextLong();
                        String str11 = client.listarVerticesVizinhos(nomeVi);
                        System.out.println(str11);
                        transport.close();
                    }
                    catch(TException x){
                        x.printStackTrace();
                    }
                    break;
            }

        }while(opcao!=0);
        
    }
    
}
