package api.game.map.metadata.xml;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlType(propOrder = {"name", "description", "simpleUnitSize", "width", "height", "maxPlayersCount", "playerStartZones", "artifactRules"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "map")
public class LevelMapMetaDataXml {

  @XmlElement(name = "name", required = true)
  public String name;

  @XmlElement(name = "description")
  public String description;

  @XmlElement(name = "simpleUnitSize", required = true, defaultValue = "40")
  public int simpleUnitSize;

  @XmlElement(name = "width", required = true)
  public int width;

  @XmlElement(name = "height", required = true)
  public int height;

  @XmlElement(name = "maxPlayersCount", required = true, defaultValue = "2")
  public int maxPlayersCount;

  @XmlElementWrapper(name = "playerStartZones")
  @XmlElement(name = "startZone", required = true)
  public List<RectangleXml> playerStartZones;

//  @XmlElementWrapper(name = "artifactRules")
  @XmlElement(name = "artifactRules", required = true)
  public ArtifactRulesXml artifactRules;

}
