package core.game;

import api.rule.ability.Modifier;
import api.rule.game.GamePreparedMetadata;
import api.rule.game.GameRawMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

  private GamePreparedMetadata gamePreparedMetadata = new GamePreparedMetadata();
  private GameRawMetadata gameRawMetadata;

  private void addModifier(Modifier modifier) {
    Optional.ofNullable(gamePreparedMetadata.modifiers.get(modifier.id))
            .ifPresent(dupModifier -> {
              throw new RuntimeException("Modifier with id \"" + dupModifier.id + "\" allredy exists");
            });
    gamePreparedMetadata.modifiers.put(modifier.id, modifier);
  }

  /**
   * Перенос модификаторов
   */
  private void copyModofiers() {
    gameRawMetadata.modifiers.stream().forEach(modifier -> addModifier(modifier));
    gameRawMetadata.weapons.stream().forEach(weapon -> Optional.ofNullable(weapon.additionalModifiers)
            .ifPresent(modifiersList -> modifiersList.stream().forEach(modifier -> {
              if (StringUtils.isEmpty(modifier.ref)) addModifier(modifier);
            })));
    gameRawMetadata.abilities.stream().forEach(ability -> Optional.ofNullable(ability.abilityModifiers)
            .ifPresent(modifiersList -> modifiersList.stream().forEach(modifier -> {
              if (StringUtils.isEmpty(modifier.ref)) addModifier(modifier);
            })));
  }

  /**
   * Перенос способностей
   */
  private void copyAbilities() {
    gameRawMetadata.abilities.stream().forEach(ability -> {
      gamePreparedMetadata.abilities.put(ability.id, ability);
      List<Modifier> abilityModifiers = new LinkedList<>(ability.abilityModifiers);
      ability.abilityModifiers.clear();

      abilityModifiers.stream().forEach(modifier -> {
        if (StringUtils.isEmpty(modifier.ref)) {
          // это непосредственно модификатор, ане ссылка. Так же надо его прооватьдобавить
          ability.abilityModifiers.add(gamePreparedMetadata.modifiers.get(modifier.id));
        } else {
          // Это ссылка
          ability.abilityModifiers.add(Optional.ofNullable(gamePreparedMetadata.modifiers.get(modifier.ref))
                  .orElseThrow(() ->
                          new RuntimeException("Modifier with id \"" + modifier.ref + "\" not found")
                  ));
        }
      });
    });
  }

  /**
   * Копирование оружия
   */
  private void copyWeapons() {
    gameRawMetadata.weapons.stream().forEach(weapon -> {
      if (weapon.additionalModifiers != null) {
        List<Modifier> weaponModifiers = new LinkedList<>(weapon.additionalModifiers);
        weapon.additionalModifiers.clear();
        weaponModifiers.stream().forEach(modifier -> {
          if (StringUtils.isEmpty(modifier.ref)) {
            // это непосредственно модификатор, ане ссылка. Так же надо его прооватьдобавить
            weapon.additionalModifiers.add(gamePreparedMetadata.modifiers.get(modifier.id));
          } else {
            // Это ссылка
            weapon.additionalModifiers.add(Optional.ofNullable(gamePreparedMetadata.modifiers.get(modifier.ref))
                    .orElseThrow(() ->

                            new RuntimeException("Modifier with id \"" + modifier.ref + "\" not found")
                    ));
          }
        });
      }
      gamePreparedMetadata.weapons.put(weapon.id, weapon);
    });
  }

  public GamePreparedMetadata loadGameMetadata() {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(GameRawMetadata.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      gameRawMetadata = (GameRawMetadata) jaxbUnmarshaller.unmarshal(this.getClass().getClassLoader().getResourceAsStream("game.xml"));

      copyModofiers();
      copyAbilities();
      copyWeapons();

      return gamePreparedMetadata;

    } catch (JAXBException e) {
      logger.error("Error load game.xml", e);
      throw new RuntimeException(e);
    }

  }
}
