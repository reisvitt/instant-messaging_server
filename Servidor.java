
/* ***************************************************************
* Autor............: Vitor Reis
* Matricula........: 201710793
* Inicio...........: 16/08/2024
* Ultima alteracao.: 01/12/2024
* Nome.............: Servidor
* Funcao...........: Inicia os servidores TCP e UDP
*************************************************************** */
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import src.domain.enums.Protocol;
import src.domain.models.Client;
import src.domain.models.Server;
import src.presentation.Manager;

public class Servidor implements Server {
  private DatagramSocket datagramSocket;
  final int UDP_PORT = 6790;

  private static Servidor instance = null;
  private ServerSocket serverSocket;
  final int TCP_PORT = 6789;
  private Manager manager;

  /*
   * ***************************************************************
   * Metodo: setManager
   * Funcao: Altera o Manager - Classe que contém todas as APDUS cadastradas
   * Parametros: Manager a ser definido
   * Retorno: void
   */
  @Override
  public void setManager(Manager manager) {
    this.manager = manager;
  }

  private Servidor() {
    try {
      this.serverSocket = new ServerSocket(this.TCP_PORT);
      this.datagramSocket = new DatagramSocket(this.UDP_PORT);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Servidor getInstance() {
    if (Servidor.instance == null) {
      Servidor.instance = new Servidor();
    }

    return Servidor.instance;
  }

  /*
   * ***************************************************************
   * Metodo: initTCP
   * Funcao: Inicia o servidor TCP
   * Parametros: void
   * Retorno: void
   */
  public void initTCP() {
    System.out.println("Starting TCP server on port: " + this.TCP_PORT);

    try {
      while (true) {
        Socket clientConection = this.serverSocket.accept();

        System.out.println("Client: " + clientConection.getInetAddress().getHostAddress() + " Port: "
            + clientConection.getLocalPort() + " (TCP) connected.");

        new Thread(() -> {
          receiveTCP(clientConection);
        }).start();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }// fim do metodo start

  /*
   * ***************************************************************
   * Metodo: initUDP
   * Funcao: Inicia o servidor UDP
   * Parametros: void
   * Retorno: void
   */
  public void initUDP() {
    System.out.println("Starting UDP server on port: " + this.UDP_PORT);

    while (true) {
      byte[] receivedData = new byte[1024];
      DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);

      try {
        this.datagramSocket.receive(receivedPacket);

        System.out.println("Client: " + receivedPacket.getAddress() + " (UDP) packet.");

        new Thread(() -> {
          receiveUDP(receivedPacket);
        }).start();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }// fim

  /*
   * ***************************************************************
   * Metodo: receiveTCP
   * Funcao: Aguarda mensagens TCP
   * Parametros: Socket da conexao
   * Retorno: void
   */
  public void receiveTCP(Socket connection) {
    Client client = null;
    try {
      client = new Client(connection);
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
      return;
    }

    while (client != null && client.isConnected()) {
      try {
        InputStream is = connection.getInputStream();

        ObjectInputStream oip = new ObjectInputStream(is);
        String in = oip.readUTF();

        System.out.println("(TCP) Received from client: " + client.getIp() + " data: " + in);
        this.manager.execute(this, client, in);
      } catch (Exception e) {
        this.manager.execute(this, client, "LEAVE ALL GROUPS");
        client.close();
      }
    }
  }

  /*
   * ***************************************************************
   * Metodo: receiveUDP
   * Funcao: Aguarda mensagens UDP
   * Parametros: DatagramPacket do UDP
   * Retorno: void
   */
  public void receiveUDP(DatagramPacket connection) {
    Client client = new Client(connection);

    String in = new String(connection.getData());
    System.out.println("(UDP) Received from client: " + client.getIp() + " data: " + in);
    this.manager.execute(this, client, in);
  }

  /*
   * ***************************************************************
   * Metodo: send
   * Funcao: Verifica o tipo da conexao e envia a msg
   * Parametros: type - Tipo de protocolo, client e data com os dados a serem
   * enviados
   * Retorno: void
   */
  @Override
  public void send(Protocol type, Client client, String data) {
    if (type == Protocol.TCP) {
      this.sendTCP(client, data);
    } else if (type == Protocol.UDP) {
      this.sendUDP(client, data);
    }
  }

  /*
   * ***************************************************************
   * Metodo: sendUDP
   * Funcao: Envia mensagem via UDP
   * Parametros: Client e data com os dados a serem enviados
   * Retorno: void
   */
  public void sendUDP(Client client, String data) {
    byte[] byteData = data.getBytes();

    System.out.println("(UDP) Sending " + data + " to " + client.getIp() + " Port: " + this.UDP_PORT);

    try {
      InetAddress address = InetAddress.getByName(client.getIp());

      DatagramPacket sendPacket = new DatagramPacket(byteData, byteData.length, address, this.UDP_PORT);
      this.datagramSocket.send(sendPacket);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
   * ***************************************************************
   * Metodo: sendTCP
   * Funcao: Envia mensagem via TCP
   * Parametros: Client e data com os dados a serem enviados
   * Retorno: void
   */
  public void sendTCP(Client client, String data) {
    Socket socket = client.getSocket();
    if (socket == null) {
      System.out.println("Socket of client " + client.getIp() + " is null.");
      return;
    }
    if (socket.isConnected()) {
      System.out.println("(TCP) Sending " + data + " to " + client.getIp());

      try {
        OutputStream output = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(output);
        oos.writeUTF(data);
        oos.flush();
      } catch (IOException e) {
        System.out.println("(TCP) Failure sending to " + client.getIp());
        e.printStackTrace();
      }
    } else {
      System.out.println("(TCP) Socket of client " + client.getIp() + " is not connected.");
      client.close();
    }
  }

  /*
   * ***************************************************************
   * Metodo: getIp
   * Funcao: Retorna o IP do socket ou datamgram
   * Parametros: Tipo da conexao
   * Retorno: String com o IP
   */
  @Override
  public String getIp(Protocol type) {
    if (type == Protocol.TCP) {
      return this.serverSocket.getInetAddress().getHostAddress();
    } else if (type == Protocol.UDP) {
      InetAddress address = this.datagramSocket.getInetAddress();
      if (address != null) {
        return this.datagramSocket.getInetAddress().getHostAddress();
      }
    }

    return "0.0.0.0";
  }

  /*
   * ***************************************************************
   * Metodo: sendToClients
   * Funcao: Envia mensagens a vários clientes
   * Parametros: Tipo da conexao, lista de clientes e dado
   * Retorno: void
   */
  @Override
  public void sendToClients(Protocol type, List<Client> clients, String data) {
    for (Client client : clients) {
      if (type == Protocol.TCP) {
        this.sendTCP(client, data);
      } else if (type == Protocol.UDP) {
        this.sendUDP(client, data);
      }
    }
  }

  /*
   * ***************************************************************
   * Metodo: init
   * Funcao: Inicializa os servidores TCP e UDP
   * Parametros: void
   * Retorno: void
   */
  @Override
  public void init() {
    try {
      new Thread(() -> {
        this.initTCP();
      }).start();
      new Thread(() -> {
        this.initUDP();
      }).start();

    } catch (Exception e) {
      System.out.println("Error initializing" + e.getMessage());
      e.printStackTrace();
    }
  }
}
