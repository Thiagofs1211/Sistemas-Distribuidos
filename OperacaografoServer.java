package operacaografo;

import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;

import operacaografo.*;

import java.util.HashMap;
import org.apache.thrift.transport.TTransportException;

public class OperacaografoServer {
    
    public static void main(String[] args) throws TTransportException{
        try{
            TServerTransport serverTransport = new TServerSocket(9090);
            OperacaografoHandler handler = new OperacaografoHandler();
            OperacaoGrafo.Processor processor = new OperacaoGrafo.Processor(handler);
            
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
            
            System.out.println("Stating Server...");
            server.serve();
        }
        catch(Exception x){
            x.printStackTrace();
        }
    }
}