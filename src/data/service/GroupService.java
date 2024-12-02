/* ***************************************************************
* Autor............: Vitor Reis
* Matricula........: 201710793
* Inicio...........: 16/08/2024
* Ultima alteracao.: 01/12/2024
* Nome.............: GroupService
* Funcao...........: Serviços do servidor
*************************************************************** */

package src.data.service;

import java.util.List;
import java.util.stream.Collectors;

import src.domain.enums.Protocol;
import src.domain.models.Client;
import src.domain.models.Group;
import src.domain.models.Server;
import src.data.repository.GroupsStore;
import src.data.utils.GetData;

public class GroupService {
  private GroupsStore groupRepository;

  public GroupService(GroupsStore groupRepository) {
    this.groupRepository = groupRepository;
  }

  /*
   * ***************************************************************
   * Metodo: listMyGroup
   * Funcao: Lista os grupos de um cliente
   * Parametros: Cliente
   * Retorno: String com uma listagem de grupos separadas por virgula
   */
  public String listMyGroup(Client client) {

    String groupNames = this.groupRepository.getGroups().stream()
        .filter(group -> group.hasClient(client))
        .map(Group::getName)
        .collect(Collectors.joining(", "));

    return groupNames;
  }

  /*
   * ***************************************************************
   * Metodo: joinGroup
   * Funcao: Insere Cliente a um grupo e envia mensagem para no grupo informando
   * um novo integrante
   * Parametros: Server, Cliente e mensagem com o grupo
   * Retorno: void
   */
  public void joinGroup(Server server, Client client, String data) {
    data = GetData.getUtil(data);
    String[] parts = data.split(":"); // ["grupox"]

    String groupName = parts[0];
    String userIp = client.getIp();

    if (!this.groupRepository.exist(groupName)) { // needs to create
      this.groupRepository.addByName(groupName);
    }

    Group group = this.groupRepository.getGroupByName(groupName);

    if (!group.hasClient(client)) {
      group.addClient(client);

      System.out.println(userIp + " joined group: " + group.getName());

      // send to others user the message
      String dataToSend = "SEND " + groupName + ":" + server.getIp(Protocol.TCP) + ":" + client.getIp()
          + " joined group";
      List<Client> clients = group.getClients().stream().filter(clt -> !clt.getIp().equals(client.getIp()))
          .collect(Collectors.toList());
      server.sendToClients(Protocol.UDP, clients, dataToSend);
    } else {
      System.out.println(userIp + " already in group: " + group.getName());
      server.send(Protocol.UDP, client, "ERROR You already in this group");
    }

  }

  /*
   * ***************************************************************
   * Metodo: leaveAllGroup
   * Funcao: Remove Cliente de todos os grupo.
   * Parametros: Server, Cliente e mensagem com o grupo
   * Retorno: void
   */
  public void leaveAllGroup(Server server, Client client, String data) {
    for (Group group : this.groupRepository.getGroups()) {
      if (group.hasClient(client)) {
        this.leaveGroup(server, client, "LEAVE " + group.getName());
      }
    }
  }

  /*
   * ***************************************************************
   * Metodo: leaveGroup
   * Funcao: Remove Cliente de um grupo e envia mensagem para no grupo informando
   * um novo integrante
   * Parametros: Server, Cliente e mensagem com o grupo
   * Retorno: void
   */
  public void leaveGroup(Server server, Client client, String data) {
    data = GetData.getUtil(data);
    String[] parts = data.split(":"); // ["grupox"]

    String groupName = parts[0];
    String userIp = client.getIp();

    if (!this.groupRepository.exist(groupName)) {
      System.out.println("Group not found: " + groupName);
      return;
    }

    Group group = this.groupRepository.getGroupByName(groupName);

    if (group.hasClient(client)) {
      group.removeClient(client);
      System.out.println(userIp + " left group: " + group.getName());

      // send to others user the message
      String dataToSend = "SEND " + groupName + ":" + server.getIp(Protocol.TCP) + ":" + client.getIp() + " left group";
      List<Client> clients = group.getClients().stream().filter(clt -> !clt.getIp().equals(client.getIp()))
          .collect(Collectors.toList());
      server.sendToClients(Protocol.UDP, clients, dataToSend);

    } else {
      System.out.println(userIp + " isnt in group: " + group.getName());
      server.send(Protocol.UDP, client, "ERROR You isnt in this group");
    }
  }

  /*
   * ***************************************************************
   * Metodo: sendMessage
   * Funcao: Envia mensagem de um Cliente a um grupo
   * Parametros: Server, Cliente e mensagem
   * Retorno: void
   */
  public void sendMessage(Server server, Client client, String data) {
    data = GetData.getUtil(data);
    System.out.println("Util: " + data);

    String[] parts = data.split(":"); // ["group", "message"]

    String groupName = parts[0];
    String message = parts[1];

    Group group = this.groupRepository.getGroupByName(groupName);

    if (group == null) {
      System.out.println("Group not found: " + groupName);
      return;
    }

    System.out.println("Group: " + groupName + " Message: " + message);

    String sendData = "SEND " + groupName + ":" + client.getIp() + ":" + message;

    System.out.println("DATA TO SEND: " + sendData);

    List<Client> clients = group.getClients().stream()
        .filter(clt -> !clt.getIp().equals(client.getIp()))
        .collect(Collectors.toList());

    server.sendToClients(Protocol.UDP, clients, sendData);
  }
}
