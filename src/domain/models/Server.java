package src.domain.models;

import java.util.List;

import src.domain.enums.Protocol;
import src.presentation.Manager;

public interface Server {
  void init() throws Exception;

  void send(Protocol type, Client client, String data);

  void setManager(Manager manager);

  String getIp(Protocol type);

  void sendToClients(Protocol type, List<Client> clients, String data);
}