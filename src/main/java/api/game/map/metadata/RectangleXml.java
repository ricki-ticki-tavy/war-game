package api.game.map.metadata;

import javax.xml.bind.annotation.*;

/**
 * Прямоугольник
 */
@XmlType(propOrder = {"topLeftConner", "bottomRightConner"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "reqtangle")
public class RectangleXml {

  @XmlElement(name = "topLeftConner")
  public CoordsXml topLeftConner;

  @XmlElement(name = "bottomRightConner")
  public CoordsXml bottomRightConner;
}
