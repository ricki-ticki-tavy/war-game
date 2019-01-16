package api.enums;

/**
 * Тип карты. На скольких игроков
 */
public enum MapTypeEnum {
  TWO_PLAYERS("level2.xml"),
  THREE_PLAYERS("level3.xml"),
  FORE_PLAYERS("level4.xml");


  private String mapName;

  public String getMapName(){
    return mapName;
  }

  private MapTypeEnum(String mapName){
    this.mapName = mapName;
  }
}
