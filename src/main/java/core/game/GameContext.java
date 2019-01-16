package core.game;

import api.core.Context;
import api.core.Core;
import api.game.GameEvent;
import api.game.map.LevelMap;
import api.game.map.metadata.LevelMapMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GameContext implements Context{

  private static final Logger logger = LoggerFactory.getLogger(GameContext.class);

  @Autowired
  private LevelMap levelMap;

  @Autowired
  private Core core;

  @Override
  public Core getCore() {
    return core;
  }

  @Override
  public boolean fireGameEvent(GameEvent gameEvent) {
    return false;
  }

  @Override
  public LevelMap getLevelMap() {
    return null;
  }

  @Override
  public void loadMap(InputStream mapXml){
        try {
      JAXBContext jaxbContext = JAXBContext.newInstance(LevelMapMetaData.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      LevelMapMetaData mapMetadata = (LevelMapMetaData) jaxbUnmarshaller.unmarshal(mapXml);

      levelMap.init(mapMetadata);

    } catch (JAXBException e) {
      logger.error("Error load game.xml", e);
      throw new RuntimeException(e);
    }

  }
}
