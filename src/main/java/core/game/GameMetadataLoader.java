package core.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GameMetadataLoader {
  private static final Logger logger = LoggerFactory.getLogger(CoreImpl.class);
//
//  private GamePreparedMetadata gamePreparedMetadata = new GamePreparedMetadata();
//  private GameRawMetadata gameRawMetadata;
//
//  /**
//   * Перенос модификаторов
//   */
//  private void copyModofiers() {
//    // из основного справочника
//    gameRawMetadata.modifiers.stream().forEach(modifier -> gamePreparedMetadata.addModifier(modifier));
//
//    // из способностей
//    gameRawMetadata.abilities.stream().forEach(ability -> Optional.ofNullable(ability.abilityModifiers)
//            .ifPresent(modifiersList -> modifiersList.stream().forEach(modifier -> {
//              if (StringUtils.isEmpty(modifier.ref)) gamePreparedMetadata.addModifier(modifier);
//            })));
//
//    // из оружия
//    gameRawMetadata.weapons.stream().forEach(weapon -> Optional.ofNullable(weapon.abilities)
//            .ifPresent(modifiersList -> modifiersList.stream().forEach(modifier -> {
//              if (StringUtils.isEmpty(modifier.ref)) gamePreparedMetadata.addModifier(modifier);
//            })));
//
//    gameRawMetadata.baseWarriorClasses.stream().forEach(baseCreatureClass -> {
//      // из способностей классов созданий
//      Optional.ofNullable(baseCreatureClass.abilities).ifPresent(creatureAbilities ->
//              creatureAbilities.stream().forEach(creatureAbility -> {
//                Optional.ofNullable(creatureAbility.ability).orElseThrow(() -> new RuntimeException("Ability in creaureAbility can't be null in class \"" + baseCreatureClass.id + "\""));
//                if (StringUtils.isEmpty(creatureAbility.ability.ref)) {
//                  Optional.ofNullable(creatureAbility.ability.abilityModifiers)
//                          .ifPresent(abilityModifiers -> abilityModifiers.stream().forEach(modifier -> {
//                            if (StringUtils.isEmpty(modifier.ref)) gamePreparedMetadata.addModifier(modifier);
//                          }));
//                }
//              }));
//      // из оружия классов созданий
//      Optional.ofNullable(baseCreatureClass.hands).ifPresent(hands ->
//              hands.stream().forEach(hand -> {
//                Optional.ofNullable(hand.weapons).ifPresent(weapons ->
//                        weapons.stream().forEach(weapon -> {
//                          if (StringUtils.isEmpty(weapon.ref)) {
//                            Optional.ofNullable(weapon.abilities)
//                                    .ifPresent(weaponModifiers -> weaponModifiers.stream().forEach(modifier -> {
//                                      if (StringUtils.isEmpty(modifier.ref)) gamePreparedMetadata.addModifier(modifier);
//                                    }));
//                          }
//                        }));
//              }));
//    });
//
//  }
//
//  /**
//   * Перенос способностей
//   */
//  private void copyAbilities() {
//    gameRawMetadata.abilities.stream().forEach(ability -> gamePreparedMetadata.addAbility(ability));
//    gameRawMetadata.baseWarriorClasses.stream().forEach(baseCreatureClass -> {
//      Optional.ofNullable(baseCreatureClass.abilities)
//              .ifPresent(creatureAbilities -> creatureAbilities.stream().forEach(creatureAbility -> {
//                Optional.ofNullable(creatureAbility.ability).orElseThrow(() -> new RuntimeException("Ability in creaureAbility can't be null in class \"" + baseCreatureClass.id + "\""));
//                if (StringUtils.isEmpty(creatureAbility.ability.ref))
//                  gamePreparedMetadata.addAbility(creatureAbility.ability);
//              }));
//    });
//  }
//
//  /**
//   * Копирование оружия
//   */
//  private void copyWeapons() {
//    // из базового справочника
//    gameRawMetadata.weapons.stream().forEach(weapon -> gamePreparedMetadata.addWeapon(weapon));
//
//    // из классов созданий
//    gameRawMetadata.baseWarriorClasses.stream().forEach(creatureClass ->
//            Optional.ofNullable(creatureClass.hands)
//                    .ifPresent(hands -> hands.stream().forEach(hand -> Optional.ofNullable(hand.weapons)
//                            .ifPresent(weapons -> weapons.stream().forEach(weapon -> {
//                                      if (StringUtils.isEmpty(weapon.ref)) gamePreparedMetadata.addWeapon(weapon);
//                                    }
//
//                            ))))
//    );
//  }
//
//  /**
//   * Копирование классов созданий
//   */
//  private void copyBaseCreatureClasses() {
//    Optional.ofNullable(gameRawMetadata.baseWarriorClasses)
//            .ifPresent(creatureClasses -> creatureClasses.stream().forEach(creatureClass -> gamePreparedMetadata.addCreatureClass(creatureClass)));
//  }
//
//  public GamePreparedMetadata loadGameMetadata() {
//    try {
//      JAXBContext jaxbContext = JAXBContext.newInstance(GameRawMetadata.class);
//      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//      gameRawMetadata = (GameRawMetadata) jaxbUnmarshaller.unmarshal(this.getClass().getClassLoader().getResourceAsStream("game.xml"));
//
//      copyModofiers();
//      copyAbilities();
//      copyWeapons();
//      copyBaseCreatureClasses();
//
//      return gamePreparedMetadata;
//
//    } catch (JAXBException e) {
//      logger.error("Error load game.xml", e);
//      throw new RuntimeException(e);
//    }
//
//  }
}
