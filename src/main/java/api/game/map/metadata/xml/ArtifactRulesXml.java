package api.game.map.metadata.xml;

import javax.xml.bind.annotation.*;

/**
 * Координаты
 */
@XmlType(propOrder = {"maxStartCount", "maxStartCost", "additionalArtifactRoundNumbers"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ArtifactRulesXml {
  /**
   * Кол-во артифактов, допустимых ДО начала игры
   */
  @XmlElement(name = "maxStartCount")
  public int maxStartCount;

  /**
   * Максимальная стоимость артефактов, даваемых игроку ДО начала расстановки
   */
  @XmlElement(name = "maxStartCost")
  public int maxStartCost;

  /**
   * Круги игры, после которых можно выбрать дополнительный артефакт
   */
  @XmlElement(name = "additionalArtifactRoundNumbers")
  public String additionalArtifactRoundNumbers ;
}
