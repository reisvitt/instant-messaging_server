/* ***************************************************************
* Autor............: Vitor Reis
* Matricula........: 201710793
* Inicio...........: 16/08/2024
* Ultima alteracao.: 01/12/2024
* Nome.............: Client
* Funcao...........: Representação do Cliente
*************************************************************** */
package src.domain.models;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;

public class Client {
  private String ip;
  private DatagramPacket datagramPacket;
  private Socket socket;

  public Client(Socket socket) throws IOException {
    this.socket = socket;
    this.ip = socket.getInetAddress().getHostAddress();
  }

  public Client(DatagramPacket datagramPacket) {
    this.datagramPacket = datagramPacket;
    this.ip = datagramPacket.getAddress().getHostAddress();
  }

  /*
   * ***************************************************************
   * Metodo: close
   * Funcao: Fecha conexao caso seja TCP
   * Parametros: void
   * Retorno: void
   */
  public void close() {
    try {
      if (this.socket != null) {
        this.socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
   * ***************************************************************
   * Metodo: getSocket
   * Funcao: Retorna Socket do TCP
   * Parametros: void
   * Retorno: Socket do TCP
   */
  public Socket getSocket() {
    return this.socket;
  }

  /*
   * ***************************************************************
   * Metodo: isConnected
   * Funcao: verifica se ha uma conexao ativa
   * Parametros: void
   * Retorno: Booleano
   */
  public Boolean isConnected() {
    if (this.socket != null) {
      return this.socket.isConnected() && !this.socket.isClosed();
    }

    return false;
  }

  /*
   * ***************************************************************
   * Metodo: setSocket
   * Funcao: Altera o socket do TCP
   * Parametros: Socket
   * Retorno: void
   */
  public void setSocket(Socket socket) {
    this.socket = socket;
  }

  /*
   * ***************************************************************
   * Metodo: getIp
   * Funcao: Retorna o IP
   * Parametros: void
   * Retorno: String com o IP
   */
  public String getIp() {
    return ip;
  }

  /*
   * ***************************************************************
   * Metodo: setIp
   * Funcao: Altera o IP
   * Parametros: String com o IP
   * Retorno: void
   */
  public void setIp(String ip) {
    this.ip = ip;
  }

  /*
   * ***************************************************************
   * Metodo: getDatagramPacket
   * Funcao: Retorna o Datagrama
   * Parametros: void
   * Retorno: DatagramPacket
   */
  public DatagramPacket getDatagramPacket() {
    return datagramPacket;
  }

  /*
   * ***************************************************************
   * Metodo: setDatagramPacket
   * Funcao: Altera o Datagrama
   * Parametros: DatagramPacket
   * Retorno: void
   */
  public void setDatagramPacket(DatagramPacket datagramPacket) {
    this.datagramPacket = datagramPacket;
  }
}