package api.rule.ability;

import api.base.i.enums.EventEnum;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Способность создания
 */
@XmlType(propOrder = {"ability", "probability", "costInActionPoints", "restoreInterval", "triggers", "playerAbilityPointCost"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "creatureAbility")
public class CreatureAbility  implements Serializable {
  /**
   * Способность
   */
  @XmlElement(name ="ability")
  public Ability ability;

  /**
   * Вероятность успеха.
   */
  @XmlElement(name ="probability")
  public int probability;

  /**
   * цена попытки применения способности
   */
  @XmlElement(name ="costInActionPoints")
  public int costInActionPoints;

  /**
   * Время восстановления. В игровых кругах. 0 - повторно доступна сразу
   */
  @XmlElement(name ="restoreInterval")
  public int restoreInterval;

  /**
   * Когда доступна способность
   */
  @XmlElementWrapper(name = "triggers")
  @XmlElement(name = "trigger", defaultValue = "ALLWAYS")
  public List<EventEnum> triggers;

  /**
   * стоимость применения способности в очках способности. Например у игрока или создания
   */
  @XmlElement(name = "playerAbilityPointCost", defaultValue = "0")
  public int playerAbilityPointCost;


}
