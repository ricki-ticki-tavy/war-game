package core.game;

import api.rule.ability.Modifier;
import api.rule.game.GamePreparedMetadata;
import api.rule.game.GameRawMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GameMetadataLoader {
  private static final Logger logger = LoggerFactory.getLogger(Game.class);

  private GamePreparedMetadata gamePreparedMetadata =new GamePreparedMetadata();
  private GameRawMetadata gameRawMetadata;

  /**
   * Перенос модификаторов
   */
  private void copyModofiers(){
    gameRawMetadata.modifiers.stream().forEach(modifier -> gamePreparedMetadata.modifiers.put(modifier.id, modifier));
  }

  /**
   * Перенос способностей
   */
  private void copyAbilities(){
    gameRawMetadata.abilities.stream().forEach(ability -> {
      gamePreparedMetadata.abilities.put(ability.id, ability);
      List<Modifier> abilityModifiers = new LinkedList<>(ability.abilityModificators);
      ability.abilityModificators.clear();
      abilityModifiers.stream().forEach(modificator -> {
        ability.abilityModificators.add(Optional.of(gamePreparedMetadata.modifiers.get(modificator.ref))
                .orElseThrow(() -> new RuntimeException("Unknown modifier \"" + modificator.ref + "\"")));
      });
    });
  }

  public GamePreparedMetadata loadGameMetadata(){
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(GameRawMetadata.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      gameRawMetadata = (GameRawMetadata) jaxbUnmarshaller.unmarshal(this.getClass().getClassLoader().getResourceAsStream("game.xml"));

      copyModofiers();
      copyAbilities();

      return gamePreparedMetadata;

    } catch (JAXBException e){
      logger.error("Error load game.xml", e);
      throw new RuntimeException(e);
    }

  }
}
