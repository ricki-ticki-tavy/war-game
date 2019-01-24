package api.game.map.metadata;

import javax.xml.bind.annotation.*;

/**
 * Координаты
 */
@XmlType(propOrder = {"x", "y"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class CoordsXml {
  @XmlElement(name = "x")
  public int x;

  @XmlElement(name = "y")
  public int y;
}
