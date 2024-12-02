/* ***************************************************************
* Autor............: Vitor Reis
* Matricula........: 201710793
* Inicio...........: 16/08/2024
* Ultima alteracao.: 01/12/2024
* Nome.............: GroupsStore
* Funcao...........: Armazena em tempo de execução todos os grupos e seus integrantes
*************************************************************** */

package src.data.repository;

import java.util.ArrayList;

import src.domain.models.Group;

public class GroupsStore {
  private ArrayList<Group> groups;

  private static GroupsStore instance;

  private GroupsStore() {
    this.groups = new ArrayList<Group>();
  }

  public static GroupsStore getInstance() {
    if (GroupsStore.instance == null) {
      GroupsStore.instance = new GroupsStore();
    }
    return GroupsStore.instance;
  }

  public void add(Group group) {
    this.groups.add(group);
  }

  public void addByName(String name) {
    Group group = new Group(name);
    this.groups.add(group);
  }

  public boolean exist(String groupName) {
    return groups.stream().anyMatch(grupo -> grupo.getName().equals(groupName));
  }

  public boolean removeByName(String groupName) {
    for (Group group : groups) {
      if (group.getName().equals(groupName)) {
        groups.remove(group);
        return true;
      }
    }
    return false;
  }

  public ArrayList<Group> getGroups() {
    return groups;
  }

  public Group getGroupByName(String groupName) {
    for (Group group : groups) {
      if (group.getName().equals(groupName)) {
        return group;
      }
    }
    return null;
  }
}
