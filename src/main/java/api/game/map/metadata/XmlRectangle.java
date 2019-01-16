package api.game.map.metadata;

import api.game.Coords;

import javax.xml.bind.annotation.*;

/**
 * Прямоугольник
 */
@XmlType(propOrder = {"topLeftConner", "bottomRightConner"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "reqtangle")
public class XmlRectangle {

  @XmlElement(name = "topLeftConner")
  public XmlCoords topLeftConner;

  @XmlElement(name = "bottomRightConner")
  public XmlCoords bottomRightConner;
}
